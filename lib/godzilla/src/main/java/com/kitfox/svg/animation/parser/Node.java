package com.kitfox.svg.animation.parser;

import java.io.Serializable;

public interface Node extends Serializable {
    int getId();

    void jjtAddChild(Node node, int i);

    void jjtClose();

    Node jjtGetChild(int i);

    int jjtGetNumChildren();

    Node jjtGetParent();

    void jjtOpen();

    void jjtSetParent(Node node);
}
