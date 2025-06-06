package com.codingzero.dddutilities.pagination;

/**
 * This interface define the protocol of counting of the total number of result.
 *
 */
public interface ResultCountDelegate {

    /**
     * Return total records at the moment based on the given parameters
     *
     * @return int -- total number of records
     */
    int countTotal();

}
