package com.codingzero.dddutilities.pagination;

import java.util.List;

public class Page<T, P> {

    private final T content;
    private final P paging;
    private final int totalCount;
    private final List<FieldSort> fieldSorts;

    public Page(T content, P paging, int totalCount, List<FieldSort> fieldSorts) {
        this.content = content;
        this.paging = paging;
        this.totalCount = totalCount;
        this.fieldSorts = fieldSorts;
    }

    public T getContent() {
        return content;
    }

    public P getPaging() {
        return paging;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<FieldSort> getFieldSorts() {
        return fieldSorts;
    }

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", paging=" + paging +
                ", totalCount=" + totalCount +
                ", fieldSorts=" + fieldSorts +
                '}';
    }
}
