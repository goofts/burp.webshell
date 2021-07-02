package com.formdev.flatlaf.json;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Json {
    public static Object parse(Reader reader) throws IOException, ParseException {
        DefaultHandler handler = new DefaultHandler();
        new JsonParser(handler).parse(reader);
        return handler.getValue();
    }

    static class DefaultHandler extends JsonHandler<List<Object>, Map<String, Object>> {
        private Object value;

        DefaultHandler() {
        }

        @Override // com.formdev.flatlaf.json.JsonHandler
        public List<Object> startArray() {
            return new ArrayList();
        }

        @Override // com.formdev.flatlaf.json.JsonHandler
        public Map<String, Object> startObject() {
            return new LinkedHashMap();
        }

        @Override // com.formdev.flatlaf.json.JsonHandler
        public void endNull() {
            this.value = "null";
        }

        @Override // com.formdev.flatlaf.json.JsonHandler
        public void endBoolean(boolean bool) {
            this.value = bool ? "true" : "false";
        }

        @Override // com.formdev.flatlaf.json.JsonHandler
        public void endString(String string) {
            this.value = string;
        }

        @Override // com.formdev.flatlaf.json.JsonHandler
        public void endNumber(String string) {
            this.value = string;
        }

        public void endArray(List<Object> array) {
            this.value = array;
        }

        public void endObject(Map<String, Object> object) {
            this.value = object;
        }

        public void endArrayValue(List<Object> array) {
            array.add(this.value);
        }

        public void endObjectValue(Map<String, Object> object, String name) {
            object.put(name, this.value);
        }

        /* access modifiers changed from: package-private */
        public Object getValue() {
            return this.value;
        }
    }
}
