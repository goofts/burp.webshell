package com.jgoodies.common.collect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public final class ArrayListModel<E> extends ArrayList<E> implements ObservableList2<E> {
    private static final long serialVersionUID = -6165677201152015546L;
    private EventListenerList listenerList;

    public ArrayListModel() {
        this(10);
    }

    public ArrayListModel(int initialCapacity) {
        super(initialCapacity);
    }

    public ArrayListModel(Collection<? extends E> c) {
        super(c);
    }

    @Override // java.util.AbstractList, java.util.ArrayList, java.util.List
    public final void add(int index, E element) {
        super.add(index, element);
        fireIntervalAdded(index, index);
    }

    @Override // java.util.Collection, java.util.AbstractList, java.util.ArrayList, java.util.AbstractCollection, java.util.List
    public final boolean add(E e) {
        int newIndex = size();
        super.add(e);
        fireIntervalAdded(newIndex, newIndex);
        return true;
    }

    @Override // java.util.AbstractList, java.util.ArrayList, java.util.List
    public final boolean addAll(int index, Collection<? extends E> c) {
        boolean changed = super.addAll(index, c);
        if (changed) {
            fireIntervalAdded(index, (c.size() + index) - 1);
        }
        return changed;
    }

    @Override // java.util.Collection, java.util.ArrayList, java.util.AbstractCollection, java.util.List
    public final boolean addAll(Collection<? extends E> c) {
        int firstIndex = size();
        boolean changed = super.addAll(c);
        if (changed) {
            fireIntervalAdded(firstIndex, (c.size() + firstIndex) - 1);
        }
        return changed;
    }

    @Override // java.util.Collection, java.util.ArrayList, java.util.AbstractCollection, java.util.List
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override // java.util.Collection, java.util.ArrayList, java.util.AbstractCollection, java.util.List
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> e = iterator();
        while (e.hasNext()) {
            if (!c.contains(e.next())) {
                e.remove();
                modified = true;
            }
        }
        return modified;
    }

    public final void clear() {
        if (!isEmpty()) {
            super.clear();
            fireIntervalRemoved(0, size() - 1);
        }
    }

    @Override // java.util.AbstractList, java.util.ArrayList, java.util.List
    public final E remove(int index) {
        E removedElement = (E) super.remove(index);
        fireIntervalRemoved(index, index);
        return removedElement;
    }

    @Override // java.util.ArrayList, java.util.List
    public final boolean remove(Object o) {
        int index = indexOf(o);
        boolean contained = index != -1;
        if (contained) {
            remove(index);
        }
        return contained;
    }

    /* access modifiers changed from: protected */
    public final void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        fireIntervalRemoved(fromIndex, toIndex - 1);
    }

    @Override // java.util.AbstractList, java.util.ArrayList, java.util.List
    public final E set(int index, E element) {
        E previousElement = (E) super.set(index, element);
        fireContentsChanged(index, index);
        return previousElement;
    }

    public final void addListDataListener(ListDataListener l) {
        getEventListenerList().add(ListDataListener.class, l);
    }

    public final void removeListDataListener(ListDataListener l) {
        getEventListenerList().remove(ListDataListener.class, l);
    }

    public final Object getElementAt(int index) {
        return get(index);
    }

    public final int getSize() {
        return size();
    }

    @Override // com.jgoodies.common.collect.ObservableList2
    public final void fireContentsChanged(int index) {
        fireContentsChanged(index, index);
    }

    @Override // com.jgoodies.common.collect.ObservableList2
    public final void fireContentsChanged(int index0, int index1) {
        Object[] listeners = getEventListenerList().getListenerList();
        ListDataEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, 0, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).contentsChanged(e);
            }
        }
    }

    public final ListDataListener[] getListDataListeners() {
        return getEventListenerList().getListeners(ListDataListener.class);
    }

    private void fireIntervalAdded(int index0, int index1) {
        Object[] listeners = getEventListenerList().getListenerList();
        ListDataEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, 1, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalAdded(e);
            }
        }
    }

    private void fireIntervalRemoved(int index0, int index1) {
        Object[] listeners = getEventListenerList().getListenerList();
        ListDataEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                if (e == null) {
                    e = new ListDataEvent(this, 2, index0, index1);
                }
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
        }
    }

    private EventListenerList getEventListenerList() {
        if (this.listenerList == null) {
            this.listenerList = new EventListenerList();
        }
        return this.listenerList;
    }
}
