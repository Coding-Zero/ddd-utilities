package com.codingzero.dddutilities.pagination;

import java.util.List;
import java.util.Objects;

/**
 * This class represents offset (index) based paging result.
 * 
 * Also provide a helper PagingDelegate for supporting next page logic #{@link PagingDelegate#nextPage(ResultFetchRequest)}
 * 
 * @param <T> type of accessing data
 */
public final class OffsetPaginatedResult<T> extends PaginatedResult<T, Integer> {

    public OffsetPaginatedResult(PaginatedResultDelegate<T, Integer> delegate,
                                 ResultCountDelegate<Integer> resultCountDelegate) {
        super(delegate, resultCountDelegate, null);
    }

    public OffsetPaginatedResult(PaginatedResultDelegate<T, Integer> delegate,
                                 ResultCountDelegate<Integer> resultCountDelegate,
                                 SortableFieldMapper sortableFieldMapper) {
        super(delegate, resultCountDelegate, sortableFieldMapper);
    }

    @Override
    protected boolean verifyPageStart(Integer pageStart) {
        return (Objects.nonNull(pageStart) && pageStart >= 0);
    }

    @Override
    protected OffsetPage<T> createPage(T content,
                                       Integer nextStart,
                                       Integer start,
                                       int size,
                                       int totalCount,
                                       List<FieldSort> fieldSorts) {
        return new OffsetPage<T>(
                content,
                nextStart,
                start,
                size,
                totalCount,
                fieldSorts);
    }
}
