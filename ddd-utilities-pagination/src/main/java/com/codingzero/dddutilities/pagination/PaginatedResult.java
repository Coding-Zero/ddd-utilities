package com.codingzero.dddutilities.pagination;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * This class define a protocol for you to access the data page by page.
 *
 * Remember that you don't actually access data until you invoke #{@link #getData()} or #{@link #getPage()} method.
 *
 */
public abstract class PaginatedResult<T, S> {

    private final PaginatedResultDelegate<T, S> delegate;
    private final ResultCountDelegate<S> resultCountDelegate;
    private S currentPageStart;
    private Integer currentPageSize;
    private List<FieldSort> currentFieldSorts;
    private List<FieldSort> filteredFieldSorts;
    private final SortableFieldMapper sortableFieldMapper;
    private Page<T, S> currentPage;

    public PaginatedResult(PaginatedResultDelegate<T, S> delegate,
                           ResultCountDelegate<S> resultCountDelegate,
                           SortableFieldMapper sortableFieldMapper) {
        if (Objects.isNull(delegate)) {
            throw new NullPointerException("Need assign a delegate, "
                    + PaginatedResultDelegate.class.getName() + " to this result first.");
        }
        this.resultCountDelegate = resultCountDelegate;
        this.sortableFieldMapper = sortableFieldMapper;
        this.delegate = delegate;
        this.filteredFieldSorts = null;
        this.currentFieldSorts = null;
    }

    public S getCurrentPageStart() {
        return currentPageStart;
    }

    public Integer getCurrentPageSize() {
        return currentPageSize;
    }

    public List<FieldSort> getCurrentFieldSorts() {
        return currentFieldSorts;
    }

    public <R extends PaginatedResult<T, S>> R start(S pageStart,
                                                     int pageSize) {
        return start(pageStart, pageSize, Collections.emptyList());
    }
    /**
     * Initial the current page and sorting conditions.
     *
     * @param pageStart -- page start
     * @param pageSize -- size of each page
     * @param fieldSorts array of FieldSort
     * @param <R> R extends PaginatedResult%3CT, P%3E
     * @return R extends PaginatedResult%3CT, P%3E
     */
    @SuppressWarnings("unchecked")
    public <R extends PaginatedResult<T, S>> R start(S pageStart,
                                                     int pageSize,
                                                     List<FieldSort> fieldSorts) {
        if (!verifyPageStart(pageStart)) {
            throw new IllegalArgumentException("Page start " + pageStart + " is not valid");
        }
        this.currentPageStart = pageStart;
        this.currentPageSize = pageSize;
        this.currentFieldSorts = Collections.unmodifiableList(fieldSorts);
        this.filteredFieldSorts = translateFieldSorts(currentFieldSorts);
        return (R) this;
    }

    protected abstract boolean verifyPageStart(S pageStart);

    private List<FieldSort> translateFieldSorts(List<FieldSort> fieldSorts) {
        if (Objects.isNull(sortableFieldMapper)) {
            return Collections.unmodifiableList(fieldSorts);
        }
        List<FieldSort> result = new LinkedList<>();
        for (FieldSort fieldSort: fieldSorts) {
            String translatedField = sortableFieldMapper.translate(fieldSort.getFieldName());
            FieldSort translated = new FieldSort(translatedField, fieldSort.getOrder());
            result.add(translated);
        }
        return result;
    }

    /**
     * Move to next page based on the current page
     *
     * @param <R> PaginatedResult
     * @return R extends PaginatedResult%3CT, P%3E
     */
    @SuppressWarnings("unchecked")
    public <R extends PaginatedResult<T, S>> R next() {
        checkValidStateForNextPage();
        this.currentPageStart = this.currentPage.getNextStart();
        return (R) this;
    }

    /**
     * Returns the data with the current page.
     *
     * @return T type of accessing data
     */
    public T getData() {
        Page<T, S> page = getPage();
        return page.getContent();
    }

    abstract protected Page<T, S> createPage(T content,
                                             S nextStart,
                                             S start,
                                             int size,
                                             int totalCount,
                                             List<FieldSort> fieldSorts);

    @SuppressWarnings("unchecked")
    public <R extends Page<?, ?>> R getPage() {
        checkValidStateForGetPage();
        ResultFetchResponse<T, S> response =
                delegate.fetchResult(
                        new ResultFetchRequest<>(
                                getCurrentPageStart(),
                                getCurrentPageSize(),
                                filteredFieldSorts));
        this.currentPage =
                createPage(
                        response.getContent(),
                        response.getNextStart(),
                        getCurrentPageStart(),
                        getCurrentPageSize(),
                        getTotalCount(),
                        getCurrentFieldSorts());
        return (R) this.currentPage;
    }

    /**
     * Returns the total numbers of data is going to return.
     *
     * @return long
     */
    public int getTotalCount() {
        checkValidStateForGetPage();
        return resultCountDelegate.countTotal();
    }

    protected void checkValidStateForGetPage() {
        if (Objects.isNull(getCurrentPageSize())) {
            throw new IllegalArgumentException("Page size is required!");
        }
    }

    protected void checkValidStateForNextPage() {
        if (Objects.isNull(this.currentPage)) {
            throw new IllegalArgumentException("Need to call getPage() or getData() first!");
        }
    }

}