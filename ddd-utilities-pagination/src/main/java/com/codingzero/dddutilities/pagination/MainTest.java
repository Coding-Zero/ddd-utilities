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
        result = result.start(0, 25);
        do {
            OffsetPage<List<String>> page = result.getPage();
            lines = page.getContent();
            System.out.println("2: " + page);
            result = result.next();
        } while (!lines.isEmpty());
    }

    private static void demoOffsetPagingWithTotalCountEnabled() {
        OffsetPaginatedResult<List<String>> result = getLines();

        //Accessing data page by page
        List<String> lines;
        result = result.start(0, 31);
        int total = result.getTotalCount();
        OffsetPage<List<String>> page = result.getPage();
        while (!page.getContent().isEmpty()) {
            System.out.println("1: " + result.getPage() + " <= " + total);
            lines = result.getData();
            System.out.println("1: " + lines);
            result = result.next();
            page = result.getPage();
        }
    }

    private static OffsetPaginatedResult<List<String>> getLines() {
        return new OffsetPaginatedResult<>(
                request -> {
                    int fromIndex = request.getPageStart();
                    int toIndex = fromIndex + request.getPageSize();
                    if (toIndex > LINES.size()) {
                        toIndex = LINES.size();
                    }
                    List<String> result = LINES.subList(fromIndex, toIndex);
                    return new ResultFetchResponse<>(
                            result,
                            fromIndex + result.size());
                },
                LINES::size
        );
    }

    private static void demoCursorPagingNested() {
        CursorPaginatedResult<List<Integer>> resultBase = getNumbers();
        CursorPaginatedResult<List<String>> result = new CursorPaginatedResult<>(
                request -> {
                    CursorPage<List<Integer>> page = resultBase.start(request.getPageStart(), request.getPageSize()).getPage();
                    List<Integer> numbers = page.getContent();
                    List<String> numStrings = new ArrayList<>(numbers.size());
                    for (Integer num : numbers) {
                        numStrings.add(String.valueOf(num));
                    }
                    return new ResultFetchResponse<>(
                            numStrings, page.getNextStart());
                },
                resultBase::getTotalCount
        );

        //Accessing data page by page
        List<String> lines;
        result = result.start(null, 30);
        do {
            CursorPage<List<String>> page = result.getPage();
            System.out.println(page);
            lines = page.getContent();
            System.out.println(lines);
            result = result.next();
        } while (lines.size() > 0);
    }

    private static void demoCursorPaging() {
        CursorPaginatedResult<List<Integer>> result = getNumbers();

        //Accessing data page by page
        List<Integer> lines;
        result = result.start(null, 25);
        do {
            CursorPage<List<Integer>> page = result.getPage();
            System.out.println(page);
            lines = page.getContent();
            System.out.println(lines);
            result = result.next();
        } while (lines.size() > 0);
    }

    private static CursorPaginatedResult<List<Integer>> getNumbers() {
        return new CursorPaginatedResult<>(
                request -> {
                    String cursor = request.getPageStart();
                    int size = request.getPageSize();
                    List<Integer> result = new ArrayList<>(size);
                    boolean foundPageStart = false;
                    String nextPageStartCursor = null;
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
                            result.add(entry.getValue());
                            nextPageStartCursor = entry.getKey();
                        }
                        if (result.size() >= size) {
                            break;
                        }
                    }
                    return new ResultFetchResponse<>(result, nextPageStartCursor);
                },
                NUM_DICT::size
        );
    }

}
