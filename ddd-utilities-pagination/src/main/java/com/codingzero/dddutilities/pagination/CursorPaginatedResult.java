package com.codingzero.dddutilities.pagination;

import java.util.List;

/**
 * This class represents cursor based paging result.
 *
 * @param <T> type of data you're accessing
 */
public final class CursorPaginatedResult<T> extends PaginatedResult<T, String> {

    public CursorPaginatedResult(PaginatedResultDelegate<T, String> delegate,
                                 ResultCountDelegate<String> resultCountDelegate) {
        super(delegate, resultCountDelegate, null);
    }

    public CursorPaginatedResult(PaginatedResultDelegate<T, String> delegate,
                                 ResultCountDelegate<String> resultCountDelegate,
                                 SortableFieldMapper sortableFieldMapper) {
        super(delegate, resultCountDelegate, sortableFieldMapper);
    }

    @Override
    protected boolean verifyPageStart(String pageStart) {
        return true;
    }

    @Override
    protected CursorPage<T> createPage(T content,
                                       String nextStart,
                                       String start,
                                       int size,
                                       int totalCount,
                                       List<FieldSort> fieldSorts) {
        return new CursorPage<>(
                content,
                nextStart,
                start,
                size,
                totalCount,
                fieldSorts);
    }
}
