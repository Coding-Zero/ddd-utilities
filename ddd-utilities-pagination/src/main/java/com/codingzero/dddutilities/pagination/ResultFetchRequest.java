package com.codingzero.dddutilities.pagination;

import java.util.Collections;
import java.util.List;

/**
 * This class encapsulate all parameters which are required to fetch data
 * from persistence systems, like database, file system etc.
 */
public class ResultFetchRequest<P extends Paging> {

    private final P page;
    private final List<FieldSort> fieldSorts;

    public ResultFetchRequest(P page, List<FieldSort> fieldSorts) {
        this.page = page;
        this.fieldSorts = Collections.unmodifiableList(fieldSorts);
    }

    public P getPage() {
        return page;
    }

    public List<FieldSort> getFieldSorts() {
        return fieldSorts;
    }
}
