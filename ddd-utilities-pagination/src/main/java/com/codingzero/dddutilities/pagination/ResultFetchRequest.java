package com.codingzero.dddutilities.pagination;

import java.util.List;

/**
 * This class encapsulate all parameters which are required to fetch data
 * from persistence systems, like database, file system etc.
 */
public class ResultFetchRequest<S> {

    private final S pageStart;
    private final int pageSize;
    private final List<FieldSort> fieldSorts;

    public ResultFetchRequest(S pageStart, int pageSize, List<FieldSort> fieldSorts) {
        this.pageStart = pageStart;
        this.pageSize = pageSize;
        this.fieldSorts = fieldSorts;
    }

    public S getPageStart() {
        return pageStart;
    }

    public int getPageSize() {
        return pageSize;
    }

    public List<FieldSort> getFieldSorts() {
        return fieldSorts;
    }
}
