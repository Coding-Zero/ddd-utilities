package com.codingzero.dddutilities.pagination;

import java.util.LinkedHashMap;
import java.util.Map;

public class SortableFieldMapper {

    private Map<String, String> dictionary;

    public SortableFieldMapper() {
        this.dictionary = new LinkedHashMap<>();
    }

    public SortableFieldMapper map(String thisField, String thatField) {
        dictionary.put(thisField, thatField);
        return this;
    }

    public String translate(String thisField) {
        if (!dictionary.containsKey(thisField)) {
            throw new IllegalArgumentException("No such field mapped for the given field, " + thisField);
        }
        return dictionary.get(thisField);
    }

}
