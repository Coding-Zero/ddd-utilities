package com.codingzero.dddutilities.pagination;

import java.util.List;

public class Page<T, S> {

    private final T content;
    private final S nextStart;
    private final S start;
    private final int size;
    private final int totalCount;
    private final List<FieldSort> fieldSorts;

    public Page(T content,
                S nextStart,
                S start,
                int size,
                int totalCount,
                List<FieldSort> fieldSorts) {
        this.content = content;
        this.nextStart = nextStart;
        this.start = start;
        this.size = size;
        this.totalCount = totalCount;
        this.fieldSorts = fieldSorts;
    }

    public T getContent() {
        return content;
    }

    public S getNextStart() {
        return nextStart;
    }

    public S getStart() {
        return start;
    }

    public int getSize() {
        return size;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public List<FieldSort> getFieldSorts() {
        return fieldSorts;
    }

}
