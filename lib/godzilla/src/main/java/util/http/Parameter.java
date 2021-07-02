package util.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import util.functions;

public class Parameter {
    protected HashMap<String, byte[]> hashMap = new HashMap<>();
    protected long size;

    public String getParameterString(String key) {
        byte[] retByteArray = getParameterByteArray(key);
        if (retByteArray != null) {
            return new String(retByteArray);
        }
        return null;
    }

    public byte[] getParameterByteArray(String key) {
        return this.hashMap.get(key);
    }

    public void addParameterString(String key, String value) {
        addParameterByteArray(key, value.getBytes());
    }

    public void addParameterByteArray(String key, byte[] value) {
        this.hashMap.put(key, value);
        this.size += (long) value.length;
    }

    public byte[] get(String key) {
        return getParameterByteArray(key);
    }

    public void add(String key, String value) {
        addParameterString(key, value);
    }

    public void add(String key, byte[] value) {
        addParameterByteArray(key, value);
    }

    public long getSize() {
        return this.size;
    }

    public static Parameter unSerialize(byte[] parameterByte) {
        Parameter resParameter = new Parameter();
        ByteArrayInputStream tStream = new ByteArrayInputStream(parameterByte);
        ByteArrayOutputStream tp = new ByteArrayOutputStream();
        byte[] lenB = new byte[4];
        while (true) {
            try {
                byte t = (byte) tStream.read();
                if (t == -1) {
                    break;
                } else if (t == 2) {
                    String key = tp.toString();
                    tStream.read(lenB);
                    byte[] data = new byte[functions.bytesToInt(lenB)];
                    int readOneLen = 0;
                    do {
                        readOneLen += tStream.read(data, readOneLen, data.length - readOneLen);
                    } while (readOneLen < data.length);
                    resParameter.addParameterByteArray(key, data);
                    tp.reset();
                } else {
                    tp.write(t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tp.close();
        tStream.close();
        tStream.close();
        return resParameter;
    }

    public byte[] serialize() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (String key : this.hashMap.keySet()) {
            try {
                byte[] value = this.hashMap.get(key);
                outputStream.write(key.getBytes());
                outputStream.write(2);
                outputStream.write(functions.intToBytes(value.length));
                outputStream.write(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return outputStream.toByteArray();
    }
}
