package com.codingzero.dddutilities.pagination;

public class OffsetPaging extends Paging<Integer> {

    protected OffsetPaging(Integer start, int size) {
        super(start, size);
    }
}
