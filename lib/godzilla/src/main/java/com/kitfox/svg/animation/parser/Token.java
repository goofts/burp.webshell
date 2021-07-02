package com.kitfox.svg.animation.parser;

import java.io.Serializable;

public class Token implements Serializable {
    private static final long serialVersionUID = 1;
    public int beginColumn;
    public int beginLine;
    public int endColumn;
    public int endLine;
    public String image;
    public int kind;
    public Token next;
    public Token specialToken;

    public Token() {
    }

    public Token(int nKind) {
        this(nKind, null);
    }

    public Token(int nKind, String sImage) {
        this.kind = nKind;
        this.image = sImage;
    }

    public Object getValue() {
        return null;
    }

    public String toString() {
        return this.image;
    }

    public static Token newToken(int ofKind, String image2) {
        return new Token(ofKind, image2);
    }

    public static Token newToken(int ofKind) {
        return newToken(ofKind, null);
    }
}
