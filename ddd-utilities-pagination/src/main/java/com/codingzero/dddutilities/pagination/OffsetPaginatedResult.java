package com.codingzero.dddutilities.pagination;

/**
 * This class represents offset (index) based paging result.
 * 
 * Also provide a helper PagingDelegate for supporting next page logic #{@link PagingDelegate#nextPage(ResultFetchRequest)}
 * 
 * @param <T> type of accessing data
 */
public final class OffsetPaginatedResult<T> extends PaginatedResult<T, OffsetPaging> {

    public OffsetPaginatedResult(PaginatedResultDelegate<T, OffsetPaging> delegate,
                                 ResultCountDelegate resultCountDelegate) {
        super(delegate, resultCountDelegate, null);
    }

    public OffsetPaginatedResult(PaginatedResultDelegate<T, OffsetPaging> delegate,
                                 ResultCountDelegate resultCountDelegate,
                                 SortableFieldMapper sortableFieldMapper) {
        super(delegate, resultCountDelegate, sortableFieldMapper);
    }

}
