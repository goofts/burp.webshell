package com.jgoodies.common.collect;

public interface ObservableList2<E> extends ObservableList<E> {
    void fireContentsChanged(int i);

    void fireContentsChanged(int i, int i2);
}
