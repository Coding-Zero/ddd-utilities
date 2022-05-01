package com.codingzero.dddutilities.error;

public abstract class UnskippableError extends RuntimeException {

    private static final UnskippableSingleError.Builder SINGLE = new UnskippableSingleError.Builder();
    private static final UnskippableMultipleErrors.Builder MULTIPLE = new UnskippableMultipleErrors.Builder();

    private Type type;

    protected UnskippableError(Type type, String message) {
        super(message);
        this.type = type;
    }

    public enum Type {

        SINGLE,
        MULTIPLE

    }
}
