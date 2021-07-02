package com.kitfox.svg.animation.parser;

public class SimpleNode implements Node {
    protected Node[] children;
    protected int id;
    protected Node parent;
    protected AnimTimeParser parser;
    protected Object value;

    public SimpleNode(int i) {
        this.id = i;
    }

    public SimpleNode(AnimTimeParser p, int i) {
        this(i);
        this.parser = p;
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public void jjtOpen() {
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public void jjtClose() {
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public void jjtSetParent(Node n) {
        this.parent = n;
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public Node jjtGetParent() {
        return this.parent;
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public void jjtAddChild(Node n, int i) {
        if (this.children == null) {
            this.children = new Node[(i + 1)];
        } else if (i >= this.children.length) {
            Node[] c = new Node[(i + 1)];
            System.arraycopy(this.children, 0, c, 0, this.children.length);
            this.children = c;
        }
        this.children[i] = n;
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public Node jjtGetChild(int i) {
        return this.children[i];
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public int jjtGetNumChildren() {
        if (this.children == null) {
            return 0;
        }
        return this.children.length;
    }

    public void jjtSetValue(Object aValue) {
        this.value = aValue;
    }

    public Object jjtGetValue() {
        return this.value;
    }

    public String toString() {
        return AnimTimeParserTreeConstants.jjtNodeName[this.id];
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    /* access modifiers changed from: protected */
    public void dumpString(String s) {
        System.out.println(s);
    }

    public void dump(String prefix) {
        dumpString(toString(prefix));
        if (this.children != null) {
            for (int i = 0; i < this.children.length; i++) {
                SimpleNode n = (SimpleNode) this.children[i];
                if (n != null) {
                    n.dump(prefix + " ");
                }
            }
        }
    }

    @Override // com.kitfox.svg.animation.parser.Node
    public int getId() {
        return this.id;
    }
}
