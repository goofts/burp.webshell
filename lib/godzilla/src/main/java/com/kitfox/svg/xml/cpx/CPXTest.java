package com.kitfox.svg.xml.cpx;

import com.kitfox.svg.SVGConst;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CPXTest {
    public CPXTest() {
        writeTest();
        readTest();
    }

    public void writeTest() {
        try {
            InputStream is = CPXTest.class.getResourceAsStream("/data/readme.txt");
            CPXOutputStream cout = new CPXOutputStream(new FileOutputStream("C:\\tmp\\cpxFile.cpx"));
            byte[] buffer = new byte[1024];
            while (true) {
                int numBytes = is.read(buffer);
                if (numBytes != -1) {
                    cout.write(buffer, 0, numBytes);
                } else {
                    cout.close();
                    return;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
        }
    }

    public void readTest() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new CPXInputStream(new FileInputStream("C:\\tmp\\cpxFile.cpx"))));
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    System.err.println(line);
                } else {
                    return;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
        }
    }

    public static void main(String[] args) {
        new CPXTest();
    }
}
