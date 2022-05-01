package com.codingzero.dddutilities.pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Some examples you can check out.
 */
public class Examples {

    private static final List<String> LINES = new ArrayList<>(100);
    static {
        for (int i = 0; i < 100; i ++) {
            LINES.add("Line #" + i);
        }
    }
    private static final Map<String, Integer> NUM_DICT = new LinkedHashMap<>(100);
    static {
        for (int i = 0; i < 100; i ++) {
            NUM_DICT.put(String.valueOf(i), i);
        }
    }

    public static void main(String[] args) {
        demoOffsetPaging();
        demoOffsetPagingWithTotalCountEnabled();
        demoCursorPaging();
        demoCursorPagingNested();
    }

    private static void demoOffsetPaging() {
        OffsetPaginatedResult<List<String>> result = getLines();

        //Accessing data page by page
        List<String> lines;
        result = result.start(new OffsetPaging(1, 25));
        do {
            System.out.println(result.getCurrentPage());
            lines = result.getData();
            System.out.println(lines);
            result = result.next();
        } while (lines.size() > 0);
    }

    private static void demoOffsetPagingWithTotalCountEnabled() {
        OffsetPaginatedResult<List<String>> result = getLines();
        if (!result.isTotalCountAvailable()) {
            return;
        }

        //Accessing data page by page
        List<String> lines;
        int total = result.getTotalCount();
        result = result.start(new OffsetPaging(1, 31));
        while (result.getCurrentPage().getStart() <= total) {
            System.out.println(result.getCurrentPage() + " <= " + total);
            lines = result.getData();
            System.out.println(lines);
            result = result.next();
        }
    }

    private static OffsetPaginatedResult<List<String>> getLines() {
        return new OffsetPaginatedResult<>(
                request -> {
                    OffsetPaging paging = request.getPage();
                    int fromIndex = paging.getStart() - 1;
                    int toIndex = paging.getStart() + paging.getSize() - 1;
                    if (toIndex > LINES.size()) {
                        toIndex = LINES.size();
                    }
                    return LINES.subList(fromIndex, toIndex);
                },
                request -> LINES.size()
        );
    }

    private static void demoCursorPagingNested() {
        CursorPaginatedResult<List<Integer>> resultBase = getNumbers();
        CursorPaginatedResult<List<String>> result = new CursorPaginatedResult<>(
                (PaginatedResultDelegate<List<String>, CursorPaging>) request -> {
                    CursorPaging paging = request.getPage();
                    List<Integer> numbers = resultBase.start(paging).getData();
                    List<String> numStrings = new ArrayList<>(numbers.size());
                    for (Integer num: numbers) {
                        numStrings.add(String.valueOf(num));
                    }
                    return numStrings;
                },
                resultBase.getPagingDelegate()
        );

        //Accessing data page by page
        List<String> lines;
        result = result.start(CursorPaging.firstPage(30));
        do {
            System.out.println(result.getCurrentPage());
            lines = result.getData();
            System.out.println(lines);
            result = result.next();
        } while (lines.size() > 0);
    }

    private static void demoCursorPaging() {
        CursorPaginatedResult<List<Integer>> result = getNumbers();

        //Accessing data page by page
        List<Integer> lines;
        result = result.start(CursorPaging.firstPage(25));
        do {
            System.out.println(result.getCurrentPage());
            lines = result.getData();
            System.out.println(lines);
            result = result.next();
        } while (lines.size() > 0);
    }

    private static CursorPaginatedResult<List<Integer>> getNumbers() {
        return new CursorPaginatedResult<>(
                (PaginatedResultDelegate<List<Integer>, CursorPaging>) request -> {
                    CursorPaging paging = request.getPage();
                    if (paging.isLastPage()) {
                        return Collections.EMPTY_LIST;
                    }
                    String cursor = paging.getStart();
                    int size = paging.getSize();
                    List<Integer> result = new ArrayList<>(size);
                    boolean needReturn = false;
                    for (Map.Entry<String, Integer> entry : NUM_DICT.entrySet()) {
                        if (paging.isFirstPage()
                                || cursor.equalsIgnoreCase(entry.getKey())) {
                            needReturn = true;
                        }
                        if (needReturn && result.size() < size) {
                            result.add(entry.getValue());
                        }
                    }
                    return result;
                },
                request -> {
                    CursorPaging paging = request.getPage();
                    String cursor = paging.getStart();
                    int size = paging.getSize();
                    int index = 0;
                    boolean foundCursor = false;
                    String nextCursor = null;
                    for (String key: NUM_DICT.keySet()){
                        if (paging.isFirstPage()
                                || key.equalsIgnoreCase(cursor)) {
                            foundCursor = true;
                        }
                        if (foundCursor) {
                            if ((index ++) == size) {
                                nextCursor = key;
                                break;
                            }
                        }
                    }
                    if (Objects.isNull(nextCursor)) {
                        return CursorPaging.lastPage(size);
                    }
                    return new CursorPaging(nextCursor, size);
                }
        );
    }

}
