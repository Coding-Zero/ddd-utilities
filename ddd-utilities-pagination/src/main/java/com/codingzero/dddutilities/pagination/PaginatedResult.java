package com.codingzero.dddutilities.pagination;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * This class define a protocol for you to access the data page by page.
 *
 * Remember that you don't actually access data until you invoke #{@link #getData()} or #{@link #getPage()} method.
 *
 */
public abstract class PaginatedResult<T, P extends Paging> {

    private final PaginatedResultDelegate<T, P> delegate;
    private final ResultCountDelegate resultCountDelegate;
    private BiFunction<P, T, P> nextPagingHandler;
    private P currentPaging;
    private List<FieldSort> currentFieldSorts;
    private List<FieldSort> filteredFieldSorts;
    private final SortableFieldMapper sortableFieldMapper;
    private T currentData;

    public PaginatedResult(PaginatedResultDelegate<T, P> delegate,
                           ResultCountDelegate resultCountDelegate,
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

    public P getCurrentPaging() {
        return currentPaging;
    }

    public void handleNextPaging(BiFunction<P, T, P> handler) {
        this.nextPagingHandler = handler;
    }

    public <R extends PaginatedResult<T, P>> R start(P paging) {
        return start(paging, Collections.emptyList());
    }
    /**
     * Initial the current page and sorting conditions.
     *
     * @param paging P
     * @param fieldSorts List%3CFieldSort%3E list of FieldSort
     * @param <R> R extends PaginatedResult%3CT, P%3E
     * @return R extends PaginatedResult%3CT, P%3E
     */
    @SuppressWarnings("unchecked")
    public <R extends PaginatedResult<T, P>> R start(P paging, List<FieldSort> fieldSorts) {
        this.currentPaging = paging;
        this.currentFieldSorts = Collections.unmodifiableList(fieldSorts);
        this.filteredFieldSorts = translateFieldSorts(currentFieldSorts);
        return (R) this;
    }

    public <R extends PaginatedResult<T, P>> R next() {
        if (Objects.isNull(this.nextPagingHandler)) {
            throw new IllegalStateException("nextPagingHandler is required");
        }
        this.currentPaging = nextPagingHandler.apply(this.currentPaging, this.currentData);
        return (R) this;
    }

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
     * Returns the data with the current page.
     *
     * @return T type of accessing data
     */
    public T getData() {
        this.currentData =
                delegate.fetchResult(
                        new ResultFetchRequest<>(this.currentPaging, filteredFieldSorts));
        return currentData;
    }

    public Page<T, P> getPage() {
        checkValidStateForGetPage();
        return new Page<>(
                getData(),
                this.currentPaging,
                getTotalCount(),
                this.currentFieldSorts);
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
        if (Objects.isNull(this.currentPaging)) {
            throw new IllegalArgumentException("Page size is required!");
        }
    }

}