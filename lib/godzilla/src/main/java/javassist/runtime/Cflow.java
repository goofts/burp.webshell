package javassist.runtime;

public class Cflow extends ThreadLocal<Depth> {

    /* access modifiers changed from: protected */
    public static class Depth {
        private int depth = 0;

        Depth() {
        }

        /* access modifiers changed from: package-private */
        public int value() {
            return this.depth;
        }

        /* access modifiers changed from: package-private */
        public void inc() {
            this.depth++;
        }

        /* access modifiers changed from: package-private */
        public void dec() {
            this.depth--;
        }
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.ThreadLocal
    public synchronized Depth initialValue() {
        return new Depth();
    }

    public void enter() {
        ((Depth) get()).inc();
    }

    public void exit() {
        ((Depth) get()).dec();
    }

    public int value() {
        return ((Depth) get()).value();
    }
}
