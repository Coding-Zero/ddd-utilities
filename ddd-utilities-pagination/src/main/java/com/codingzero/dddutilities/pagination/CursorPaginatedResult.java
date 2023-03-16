package com.codingzero.dddutilities.pagination;

/**
 * This class represents cursor based paging result.
 *
 * @param <T> type of data you're accessing
 */
public final class CursorPaginatedResult<T> extends PaginatedResult<T, CursorPaging> {

    public CursorPaginatedResult(PaginatedResultDelegate<T, CursorPaging> delegate,
                                 PagingDelegate<CursorPaging> pagingDelegate,
                                 Object... arguments) {
        this(delegate, null, pagingDelegate, null, arguments);
    }

    public CursorPaginatedResult(PaginatedResultDelegate<T, CursorPaging> delegate,
                                 ResultCountDelegate resultCountDelegate,
                                 PagingDelegate<CursorPaging> pagingDelegate,
                                 Object... arguments) {
        this(delegate, resultCountDelegate, pagingDelegate, null, arguments);
    }

    public CursorPaginatedResult(PaginatedResultDelegate<T, CursorPaging> delegate,
                                 ResultCountDelegate resultCountDelegate,
                                 PagingDelegate<CursorPaging> pagingDelegate,
                                 SortableFieldMapper sortableFieldMapper,
                                 Object... arguments) {
        super(delegate, resultCountDelegate, pagingDelegate, sortableFieldMapper, arguments);
    }

}
