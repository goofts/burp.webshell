package org.sqlite;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.sqlite.util.OSInfo;
import org.sqlite.util.StringUtils;

public class SQLiteJDBCLoader {
    private static boolean extracted = false;

    public static synchronized boolean initialize() throws Exception {
        boolean z;
        synchronized (SQLiteJDBCLoader.class) {
            if (!extracted) {
                cleanup();
            }
            loadSQLiteNativeLibrary();
            z = extracted;
        }
        return z;
    }

    private static File getTempDir() {
        return new File(System.getProperty("org.sqlite.tmpdir", System.getProperty("java.io.tmpdir")));
    }

    static void cleanup() {
        File[] nativeLibFiles = new File(getTempDir().getAbsolutePath()).listFiles(new FilenameFilter() {
            /* class org.sqlite.SQLiteJDBCLoader.AnonymousClass1 */
            private final String searchPattern = ("sqlite-" + SQLiteJDBCLoader.getVersion());

            public boolean accept(File dir, String name) {
                return name.startsWith(this.searchPattern) && !name.endsWith(".lck");
            }
        });
        if (nativeLibFiles != null) {
            for (File nativeLibFile : nativeLibFiles) {
                if (!new File(nativeLibFile.getAbsolutePath() + ".lck").exists()) {
                    try {
                        nativeLibFile.delete();
                    } catch (SecurityException e) {
                        System.err.println("Failed to delete old native lib" + e.getMessage());
                    }
                }
            }
        }
    }

    @Deprecated
    static boolean getPureJavaFlag() {
        return Boolean.parseBoolean(System.getProperty("sqlite.purejava", "false"));
    }

    @Deprecated
    public static boolean isPureJavaMode() {
        return false;
    }

    public static boolean isNativeMode() throws Exception {
        initialize();
        return extracted;
    }

    static String md5sum(InputStream input) throws IOException {
        BufferedInputStream in = new BufferedInputStream(input);
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            do {
            } while (new DigestInputStream(in, digest).read() >= 0);
            ByteArrayOutputStream md5out = new ByteArrayOutputStream();
            md5out.write(digest.digest());
            String byteArrayOutputStream = md5out.toString();
            in.close();
            return byteArrayOutputStream;
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm is not available: " + e);
        } catch (Throwable th) {
            in.close();
            throw th;
        }
    }

    private static boolean contentsEquals(InputStream in1, InputStream in2) throws IOException {
        if (!(in1 instanceof BufferedInputStream)) {
            in1 = new BufferedInputStream(in1);
        }
        if (!(in2 instanceof BufferedInputStream)) {
            in2 = new BufferedInputStream(in2);
        }
        for (int ch = in1.read(); ch != -1; ch = in1.read()) {
            if (ch != in2.read()) {
                return false;
            }
        }
        if (in2.read() == -1) {
            return true;
        }
        return false;
    }

    private static boolean extractAndLoadLibraryFile(String libFolderForCurrentOS, String libraryFileName, String targetFolder) {
        String nativeLibraryFilePath = libFolderForCurrentOS + "/" + libraryFileName;
        String extractedLibFileName = String.format("sqlite-%s-%s-%s", getVersion(), UUID.randomUUID().toString(), libraryFileName);
        File extractedLibFile = new File(targetFolder, extractedLibFileName);
        File extractedLckFile = new File(targetFolder, extractedLibFileName + ".lck");
        try {
            InputStream reader = SQLiteJDBCLoader.class.getResourceAsStream(nativeLibraryFilePath);
            if (!extractedLckFile.exists()) {
                new FileOutputStream(extractedLckFile).close();
            }
            FileOutputStream writer = new FileOutputStream(extractedLibFile);
            try {
                byte[] buffer = new byte[8192];
                while (true) {
                    int bytesRead = reader.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    writer.write(buffer, 0, bytesRead);
                }
                extractedLibFile.setReadable(true);
                extractedLibFile.setWritable(true, true);
                extractedLibFile.setExecutable(true);
                InputStream nativeIn = SQLiteJDBCLoader.class.getResourceAsStream(nativeLibraryFilePath);
                InputStream extractedLibIn = new FileInputStream(extractedLibFile);
                try {
                    if (contentsEquals(nativeIn, extractedLibIn)) {
                        return loadNativeLibrary(targetFolder, extractedLibFileName);
                    }
                    throw new RuntimeException(String.format("Failed to write a native library file at %s", extractedLibFile));
                } finally {
                    if (nativeIn != null) {
                        nativeIn.close();
                    }
                    if (extractedLibIn != null) {
                        extractedLibIn.close();
                    }
                }
            } finally {
                extractedLibFile.deleteOnExit();
                extractedLckFile.deleteOnExit();
                if (writer != null) {
                    writer.close();
                }
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static boolean loadNativeLibrary(String path, String name) {
        if (!new File(path, name).exists()) {
            return false;
        }
        try {
            System.load(new File(path, name).getAbsolutePath());
            return true;
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load native library:" + name + ". osinfo: " + OSInfo.getNativeLibFolderPathForCurrentOS());
            System.err.println(e);
            return false;
        }
    }

    private static void loadSQLiteNativeLibrary() throws Exception {
        if (!extracted) {
            List<String> triedPaths = new LinkedList<>();
            String sqliteNativeLibraryPath = System.getProperty("org.sqlite.lib.path");
            String sqliteNativeLibraryName = System.getProperty("org.sqlite.lib.name");
            if (sqliteNativeLibraryName == null && (sqliteNativeLibraryName = System.mapLibraryName("sqlitejdbc")) != null && sqliteNativeLibraryName.endsWith(".dylib")) {
                sqliteNativeLibraryName = sqliteNativeLibraryName.replace(".dylib", ".jnilib");
            }
            if (sqliteNativeLibraryPath != null) {
                if (loadNativeLibrary(sqliteNativeLibraryPath, sqliteNativeLibraryName)) {
                    extracted = true;
                    return;
                }
                triedPaths.add(sqliteNativeLibraryPath);
            }
            String sqliteNativeLibraryPath2 = String.format("/%s/native/%s", SQLiteJDBCLoader.class.getPackage().getName().replaceAll("\\.", "/"), OSInfo.getNativeLibFolderPathForCurrentOS());
            boolean hasNativeLib = hasResource(sqliteNativeLibraryPath2 + "/" + sqliteNativeLibraryName);
            if (!hasNativeLib && OSInfo.getOSName().equals("Mac") && hasResource(sqliteNativeLibraryPath2 + "/" + "libsqlitejdbc.jnilib")) {
                sqliteNativeLibraryName = "libsqlitejdbc.jnilib";
                hasNativeLib = true;
            }
            if (hasNativeLib) {
                if (extractAndLoadLibraryFile(sqliteNativeLibraryPath2, sqliteNativeLibraryName, getTempDir().getAbsolutePath())) {
                    extracted = true;
                    return;
                }
                triedPaths.add(sqliteNativeLibraryPath2);
            }
            String[] split = System.getProperty("java.library.path", "").split(File.pathSeparator);
            for (String ldPath : split) {
                if (!ldPath.isEmpty()) {
                    if (loadNativeLibrary(ldPath, sqliteNativeLibraryName)) {
                        extracted = true;
                        return;
                    }
                    triedPaths.add(ldPath);
                }
            }
            extracted = false;
            throw new Exception(String.format("No native library found for os.name=%s, os.arch=%s, paths=[%s]", OSInfo.getOSName(), OSInfo.getArchName(), StringUtils.join(triedPaths, File.pathSeparator)));
        }
    }

    private static boolean hasResource(String path) {
        return SQLiteJDBCLoader.class.getResource(path) != null;
    }

    private static void getNativeLibraryFolderForTheCurrentOS() {
        OSInfo.getOSName();
        OSInfo.getArchName();
    }

    public static int getMajorVersion() {
        String[] c = getVersion().split("\\.");
        if (c.length > 0) {
            return Integer.parseInt(c[0]);
        }
        return 1;
    }

    public static int getMinorVersion() {
        String[] c = getVersion().split("\\.");
        if (c.length > 1) {
            return Integer.parseInt(c[1]);
        }
        return 0;
    }

    public static String getVersion() {
        URL versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/pom.properties");
        if (versionFile == null) {
            versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/VERSION");
        }
        if (versionFile == null) {
            return "unknown";
        }
        try {
            Properties versionData = new Properties();
            versionData.load(versionFile.openStream());
            return versionData.getProperty("version", "unknown").trim().replaceAll("[^0-9\\.]", "");
        } catch (IOException e) {
            System.err.println(e);
            return "unknown";
        }
    }
}
