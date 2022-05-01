package com.codingzero.dddutilities.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UnskippableSingleError extends UnskippableError {

    private ErrorCode code;
    private Map<String, Object> details;

    protected UnskippableSingleError(String message, ErrorCode code, Map<String, Object> details) {
        super(Type.SINGLE, message);
        this.code = code;
        this.details = Collections.unmodifiableMap(details);
    }

    public ErrorCode getCode() {
        return code;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public static class Builder {

        private ErrorCode code;
        private String message;
        private Map<String, Object> details;

        protected Builder() {
            this.code = null;
            this.message = null;
            this.details = new HashMap<>();
        }

        public Builder code(ErrorCode code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder details(String key, Object value) {
            details.put(key, value);
            return this;
        }

        public ErrorCode getCode() {
            if (null == code) {
                throw new IllegalStateException("Code is required!");
            }
            return code;
        }

        public String getMessage() {
            return message;
        }

        public Map<String, Object> getDetails() {
            return details;
        }

        public UnskippableSingleError build() {
            return new UnskippableSingleError(getMessage(), getCode(), getDetails());
        }

    }

}
