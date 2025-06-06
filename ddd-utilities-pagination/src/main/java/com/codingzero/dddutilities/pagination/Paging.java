package com.codingzero.dddutilities.pagination;

/**
 * This abstract class encapsulates paging related parameters.
 *
 * @param <S> the start position
 */
public abstract class Paging<S> {

    private final S start;
    private final int size;

    public Paging(S start, int size) {
        this.start = start;
        this.size = size;
    }

    public S getStart() {
        return start;
    }
    
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "start=" + start +
                ", size=" + size +
                '}';
    }
}
