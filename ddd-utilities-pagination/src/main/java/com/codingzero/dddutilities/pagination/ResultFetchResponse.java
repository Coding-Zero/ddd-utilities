package com.codingzero.dddutilities.pagination;

public class ResultFetchResponse<T, S> {

    private final T content;
    private final S nextStart;

    public ResultFetchResponse(T content, S nextStart) {
        this.content = content;
        this.nextStart = nextStart;
    }

    public T getContent() {
        return content;
    }

    public S getNextStart() {
        return nextStart;
    }

    @Override
    public String toString() {
        return "ResultFetchResponse{" +
                "content=" + content +
                ", nextStart=" + nextStart +
                '}';
    }
}
