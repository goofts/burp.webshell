package com.kitfox.svg.animation.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JJTAnimTimeParserState implements Serializable {
    private List<Integer> marks = new ArrayList();
    private int mk = 0;
    private boolean node_created;
    private List<Node> nodes = new ArrayList();
    private int sp = 0;

    public boolean nodeCreated() {
        return this.node_created;
    }

    public void reset() {
        this.nodes.clear();
        this.marks.clear();
        this.sp = 0;
        this.mk = 0;
    }

    public Node rootNode() {
        return this.nodes.get(0);
    }

    public void pushNode(Node n) {
        this.nodes.add(n);
        this.sp++;
    }

    public Node popNode() {
        this.sp--;
        if (this.sp < this.mk) {
            this.mk = this.marks.remove(this.marks.size() - 1).intValue();
        }
        return this.nodes.remove(this.nodes.size() - 1);
    }

    public Node peekNode() {
        return this.nodes.get(this.nodes.size() - 1);
    }

    public int nodeArity() {
        return this.sp - this.mk;
    }

    public void clearNodeScope(Node n) {
        while (this.sp > this.mk) {
            popNode();
        }
        this.mk = this.marks.remove(this.marks.size() - 1).intValue();
    }

    public void openNodeScope(Node n) {
        this.marks.add(Integer.valueOf(this.mk));
        this.mk = this.sp;
        n.jjtOpen();
    }

    public void closeNodeScope(Node n, int numIn) {
        this.mk = this.marks.remove(this.marks.size() - 1).intValue();
        int num = numIn;
        while (true) {
            num--;
            if (num > 0) {
                Node c = popNode();
                c.jjtSetParent(n);
                n.jjtAddChild(c, num);
            } else {
                n.jjtClose();
                pushNode(n);
                this.node_created = true;
                return;
            }
        }
    }

    public void closeNodeScope(Node n, boolean condition) {
        if (condition) {
            int a = nodeArity();
            this.mk = this.marks.remove(this.marks.size() - 1).intValue();
            while (true) {
                a--;
                if (a > 0) {
                    Node c = popNode();
                    c.jjtSetParent(n);
                    n.jjtAddChild(c, a);
                } else {
                    n.jjtClose();
                    pushNode(n);
                    this.node_created = true;
                    return;
                }
            }
        } else {
            this.mk = this.marks.remove(this.marks.size() - 1).intValue();
            this.node_created = false;
        }
    }
}
