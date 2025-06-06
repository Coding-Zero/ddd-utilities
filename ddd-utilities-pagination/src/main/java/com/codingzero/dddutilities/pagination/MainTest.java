package com.codingzero.dddutilities.pagination;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Some examples you can check out.
 */
public class MainTest {

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
        result = result.start(new OffsetPaging(0, 25));
        do {
            Page<List<String>, OffsetPaging> page = result.getPage();
            lines = page.getContent();
            System.out.println("1A: " + page);
            System.out.println();
            result = result.next();
        } while (!lines.isEmpty());
    }

    private static void demoOffsetPagingWithTotalCountEnabled() {
        OffsetPaginatedResult<List<String>> result = getLines();

        //Accessing data page by page
        List<String> lines;
        result = result.start(new OffsetPaging(0, 31));
        int total = result.getTotalCount();
        Page<List<String>, OffsetPaging> page = result.getPage();
        while (!page.getContent().isEmpty()) {
            System.out.println("1B: " + result.getPage() + " <= " + total);
            System.out.println();
            result = result.next();
            page = result.getPage();
        }
    }

    private static OffsetPaginatedResult<List<String>> getLines() {
        OffsetPaginatedResult<List<String>> paginatedResult =
                new OffsetPaginatedResult<>(
                        request -> {
                            int fromIndex = request.getPage().getStart();
                        int toIndex = fromIndex + request.getPage().getSize();
                        if (toIndex > LINES.size()) {
                            toIndex = LINES.size();
                        }
                        return LINES.subList(fromIndex, toIndex);
                    },
                    LINES::size);
        paginatedResult.handleNextPaging(
                (paging, data)
                        -> new OffsetPaging(paging.getStart() + data.size(), paging.getSize()));
        return paginatedResult;
    }

    private static void demoCursorPagingNested() {
        CursorPaginatedResult<Map<String, Integer>> resultBase = getNumbers();
        CursorPaginatedResult<List<String>> result = new CursorPaginatedResult<>(
                request -> {
                    Page<Map<String, Integer>, CursorPaging> page =
                            resultBase.start(request.getPage(), request.getFieldSorts()).getPage();
                    Map<String, Integer> data = page.getContent();
                    List<String> numStrings = new ArrayList<>(data.size());
                    for (Map.Entry<String, Integer> entry : data.entrySet()) {
                        numStrings.add(String.valueOf(entry.getValue()));
                    }
                    return numStrings;
                },
                resultBase::getTotalCount
        );

        result.handleNextPaging(
                (paging, data) -> {
                    String nextPageStart = null;
                    if (!data.isEmpty()) {
                        nextPageStart = data.get(data.size() - 1);
                    }
                    return new CursorPaging(nextPageStart, paging.getSize());
                });

        //Accessing data page by page
        List<String> lines;
        result = result.start(new CursorPaging(null, 30));
        do {
            Page<List<String>, CursorPaging> page = result.getPage();
            System.out.println("2A: " + page);
            lines = page.getContent();
            System.out.println();
            result = result.next();
        } while (lines.size() > 0);
    }

    private static void demoCursorPaging() {
        CursorPaginatedResult<Map<String, Integer>> result = getNumbers();

        //Accessing data page by page
        Map<String, Integer> lines;
        result = result.start(new CursorPaging(null, 25));
        do {
            Page<Map<String, Integer>, CursorPaging> page = result.getPage();
            System.out.println("2B: " + page);
            lines = page.getContent();
            result = result.next();
        } while (lines.size() > 0);
    }

    private static CursorPaginatedResult<Map<String, Integer>> getNumbers() {
        CursorPaginatedResult<Map<String, Integer>> paginatedResult =
                new CursorPaginatedResult<>(
                        request -> {
                            String cursor = request.getPage().getStart();
                            int size = request.getPage().getSize();
                            Map<String, Integer> result = new LinkedHashMap<>(size);
                            boolean foundPageStart = false;
                            for (Map.Entry<String, Integer> entry : NUM_DICT.entrySet()) {
                                if (Objects.isNull(cursor)) {
                                    foundPageStart = true;
                                } else {
                                    if (entry.getKey().equalsIgnoreCase(cursor)) {
                                        foundPageStart = true;
                                        continue; //jump to next
                                    }
                                }

                                if (foundPageStart) {
                                    result.put(entry.getKey(), entry.getValue());
                                }
                                if (result.size() >= size) {
                                    break;
                                }
                            }
                            return result;
                        },
                        NUM_DICT::size);
        paginatedResult.handleNextPaging(
                (paging, data) -> {
                    String nextPageStart = null;
                    for (Map.Entry<String, Integer> entry: data.entrySet()) {
                        nextPageStart = entry.getKey();
                    }
                    return new CursorPaging(nextPageStart, paging.getSize());
                });
        return paginatedResult;
    }

}
