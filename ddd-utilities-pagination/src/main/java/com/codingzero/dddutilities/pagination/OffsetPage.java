package com.codingzero.dddutilities.pagination;

import java.util.List;

public class OffsetPage<T> extends Page<T, Integer> {

    public OffsetPage(T content,
                      Integer nextStart,
                      Integer start,
                      int size,
                      int totalCount,
                      List<FieldSort> fieldSorts) {
        super(content, nextStart, start, size, totalCount, fieldSorts);
    }


    @Override
    public String toString() {
        return "OffsetPage{" +
                "content=" + getContent() +
                ", nextStart=" + getNextStart() +
                ", start=" + getStart() +
                ", size=" + getSize() +
                ", totalCount=" + getTotalCount() +
                ", fieldSorts=" + getFieldSorts() +
                '}';
    }

}
