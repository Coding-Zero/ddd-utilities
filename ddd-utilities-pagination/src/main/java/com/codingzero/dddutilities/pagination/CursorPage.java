package com.codingzero.dddutilities.pagination;

import java.util.List;

public class CursorPage<T> extends Page<T, String> {

    public CursorPage(T content,
                      String nextStart,
                      String start,
                      int size,
                      int totalCount,
                      List<FieldSort> fieldSorts) {
        super(content, nextStart, start, size, totalCount, fieldSorts);
    }

    @Override
    public String toString() {
        return "CursorPage{" +
                "content=" + getContent() +
                ", nextStart=" + getNextStart() +
                ", start=" + getStart() +
                ", size=" + getSize() +
                ", totalCount=" + getTotalCount() +
                ", fieldSorts=" + getFieldSorts() +
                '}';
    }
}
