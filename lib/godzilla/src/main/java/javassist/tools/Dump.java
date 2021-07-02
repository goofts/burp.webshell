package javassist.tools;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ClassFilePrinter;

public class Dump {
    private Dump() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Dump <class file name>");
            return;
        }
        ClassFile w = new ClassFile(new DataInputStream(new FileInputStream(args[0])));
        PrintWriter out = new PrintWriter((OutputStream) System.out, true);
        out.println("*** constant pool ***");
        w.getConstPool().print(out);
        out.println();
        out.println("*** members ***");
        ClassFilePrinter.print(w, out);
    }
}
