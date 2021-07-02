package javassist;

import java.io.InputStream;
import java.net.URL;

public interface ClassPath {
    URL find(String str);

    InputStream openClassfile(String str) throws NotFoundException;
}
