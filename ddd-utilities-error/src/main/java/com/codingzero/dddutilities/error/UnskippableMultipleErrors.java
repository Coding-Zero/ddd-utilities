package com.codingzero.dddutilities.error;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class UnskippableMultipleErrors extends UnskippableError {

    private List<UnskippableSingleError> errors;

    protected UnskippableMultipleErrors(List<UnskippableSingleError> errors) {
        super(Type.MULTIPLE, "");
        this.errors = Collections.unmodifiableList(errors);
    }

    public List<UnskippableSingleError> getErrors() {
        return errors;
    }

    public static class Builder {

        private List<UnskippableSingleError> errors;

        protected Builder() {
            this.errors = new LinkedList<>();
        }

        public Builder error(UnskippableSingleError error) {
            this.errors.add(error);
            return this;
        }

        public List<UnskippableSingleError> getErrors() {
            return Collections.unmodifiableList(errors);
        }

        public UnskippableMultipleErrors build() {
            return new UnskippableMultipleErrors(this.errors);
        }
    }

}
