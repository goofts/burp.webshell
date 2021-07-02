package com.kitfox.svg;

public class SVGElementException extends SVGException {
    public static final long serialVersionUID = 0;
    private final SVGElement element;

    public SVGElementException(SVGElement element2) {
        this(element2, null, null);
    }

    public SVGElementException(SVGElement element2, String msg) {
        this(element2, msg, null);
    }

    public SVGElementException(SVGElement element2, String msg, Throwable cause) {
        super(msg, cause);
        this.element = element2;
    }

    public SVGElementException(SVGElement element2, Throwable cause) {
        this(element2, null, cause);
    }

    public SVGElement getElement() {
        return this.element;
    }
}
