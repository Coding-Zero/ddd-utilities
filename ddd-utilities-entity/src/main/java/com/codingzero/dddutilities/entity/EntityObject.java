package com.codingzero.dddutilities.entity;

public abstract class EntityObject<T> extends DirtyMark {
    
    private T objectSegment;
    
    public EntityObject(T objectSegment) {
        this.objectSegment = objectSegment;
    }

    public T getObjectSegment() {
        return objectSegment;
    }

    public void refresh() {}
}
