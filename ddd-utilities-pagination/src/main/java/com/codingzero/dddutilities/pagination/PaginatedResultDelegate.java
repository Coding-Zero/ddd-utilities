package com.codingzero.dddutilities.pagination;

/**
 * This interface define the protocol of how to fetch data.
 *
 * @param <T> type of data
 * @param <S> page start type
 */
public interface PaginatedResultDelegate<T, S> {

    /**
     * Return data based on the given parameters
     *
     * @param request -- request
     * @return ResultFetchResponse
     */
    ResultFetchResponse<T, S> fetchResult(ResultFetchRequest<S> request);

}
