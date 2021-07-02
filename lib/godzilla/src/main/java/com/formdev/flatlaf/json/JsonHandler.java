package com.formdev.flatlaf.json;

/* access modifiers changed from: package-private */
public abstract class JsonHandler<A, O> {
    JsonParser parser;

    JsonHandler() {
    }

    /* access modifiers changed from: protected */
    public Location getLocation() {
        return this.parser.getLocation();
    }

    public void startNull() {
    }

    public void endNull() {
    }

    public void startBoolean() {
    }

    public void endBoolean(boolean value) {
    }

    public void startString() {
    }

    public void endString(String string) {
    }

    public void startNumber() {
    }

    public void endNumber(String string) {
    }

    public A startArray() {
        return null;
    }

    public void endArray(A a) {
    }

    public void startArrayValue(A a) {
    }

    public void endArrayValue(A a) {
    }

    public O startObject() {
        return null;
    }

    public void endObject(O o) {
    }

    public void startObjectName(O o) {
    }

    public void endObjectName(O o, String name) {
    }

    public void startObjectValue(O o, String name) {
    }

    public void endObjectValue(O o, String name) {
    }
}
