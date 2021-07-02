package javassist.bytecode.analysis;

import java.util.NoSuchElementException;

/* access modifiers changed from: package-private */
public class IntQueue {
    private Entry head;
    private Entry tail;

    IntQueue() {
    }

    private static class Entry {
        private Entry next;
        private int value;

        private Entry(int value2) {
            this.value = value2;
        }
    }

    /* access modifiers changed from: package-private */
    public void add(int value) {
        Entry entry = new Entry(value);
        if (this.tail != null) {
            this.tail.next = entry;
        }
        this.tail = entry;
        if (this.head == null) {
            this.head = entry;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.head == null;
    }

    /* access modifiers changed from: package-private */
    public int take() {
        if (this.head == null) {
            throw new NoSuchElementException();
        }
        int value = this.head.value;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;
        }
        return value;
    }
}
