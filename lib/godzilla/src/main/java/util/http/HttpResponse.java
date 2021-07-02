package util.http;

import core.shell.ShellEntity;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class HttpResponse {
    private Map<String, List<String>> headerMap;
    private String message;
    private byte[] result;
    private ShellEntity shellEntity;

    public byte[] getResult() {
        return this.result;
    }

    public Map<String, List<String>> getHeaderMap() {
        return this.headerMap;
    }

    public void setResult(byte[] result2) {
        this.result = result2;
    }

    public void setHeaderMap(Map<String, List<String>> headerMap2) {
        this.headerMap = headerMap2;
    }

    public HttpResponse(HttpURLConnection http, ShellEntity shellEntity2) throws IOException {
        this.shellEntity = shellEntity2;
        handleHeader(http.getHeaderFields());
        ReadAllData(getInputStream(http));
    }

    /* JADX WARN: Type inference failed for: r4v11, types: [java.util.function.Consumer, void] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleHeader(java.util.Map<java.lang.String, java.util.List<java.lang.String>> r8) {
        /*
        // Method dump skipped, instructions count: 103
        */
        throw new UnsupportedOperationException("Method not decompiled: util.http.HttpResponse.handleHeader(java.util.Map):void");
    }

    /* access modifiers changed from: protected */
    public InputStream getInputStream(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = httpURLConnection.getErrorStream();
        return inputStream != null ? inputStream : httpURLConnection.getInputStream();
    }

    /* access modifiers changed from: protected */
    public void ReadAllData(InputStream inputStream) throws IOException {
        try {
            if (this.headerMap.get("Content-Length") == null || this.headerMap.get("Content-Length").size() <= 0) {
                this.result = ReadUnknownNumData(inputStream);
            } else {
                this.result = ReadKnownNumData(inputStream, Integer.parseInt(this.headerMap.get("Content-Length").get(0)));
            }
        } catch (NumberFormatException e) {
            this.result = ReadUnknownNumData(inputStream);
        }
        this.result = this.shellEntity.getCryptionModel().decode(this.result);
    }

    /* access modifiers changed from: protected */
    public byte[] ReadKnownNumData(InputStream inputStream, int num) throws IOException {
        if (num > 0) {
            byte[] temp = new byte[5120];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while (true) {
                int readOneNum = inputStream.read(temp);
                if (readOneNum == -1) {
                    return bos.toByteArray();
                }
                bos.write(temp, 0, readOneNum);
            }
        } else if (num == 0) {
            return ReadUnknownNumData(inputStream);
        } else {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public byte[] ReadUnknownNumData(InputStream inputStream) throws IOException {
        byte[] temp = new byte[5120];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (true) {
            int readOneNum = inputStream.read(temp);
            if (readOneNum == -1) {
                return bos.toByteArray();
            }
            bos.write(temp, 0, readOneNum);
        }
    }
}
