package javassist.tools.rmi;

import java.io.Serializable;

public class RemoteRef implements Serializable {
    private static final long serialVersionUID = 1;
    public String classname;
    public int oid;

    public RemoteRef(int i) {
        this.oid = i;
        this.classname = null;
    }

    public RemoteRef(int i, String name) {
        this.oid = i;
        this.classname = name;
    }
}