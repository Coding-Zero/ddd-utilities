package com.codingzero.dddutilities.pagination;

/**
 * This class represents cursor based paging result.
 *
 * @param <T> type of data you're accessing
 */
public final class CursorPaginatedResult<T> extends PaginatedResult<T, CursorPaging> {

    public CursorPaginatedResult(PaginatedResultDelegate<T, CursorPaging> delegate,
                                 ResultCountDelegate resultCountDelegate) {
        super(delegate, resultCountDelegate, null);
    }

    public CursorPaginatedResult(PaginatedResultDelegate<T, CursorPaging> delegate,
                                 ResultCountDelegate resultCountDelegate,
                                 SortableFieldMapper sortableFieldMapper) {
        super(delegate, resultCountDelegate, sortableFieldMapper);
    }

}
