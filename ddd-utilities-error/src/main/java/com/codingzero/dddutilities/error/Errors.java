package com.codingzero.dddutilities.error;

public enum Errors implements ErrorCode {

    DUPLICATE_STUDENT_ID("ERR_123", "DuplicateStudentId")
    ;

    private String code;
    private String brief;

    Errors(String code, String brief) {
        this.code = code;
        this.brief = brief;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getBrief() {
        return brief;
    }
}
