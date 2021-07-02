package org.sqlite.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OSInfo {
    public static final String IA64 = "ia64";
    public static final String IA64_32 = "ia64_32";
    public static final String PPC = "ppc";
    public static final String PPC64 = "ppc64";
    public static final String X86 = "x86";
    public static final String X86_64 = "x86_64";
    private static HashMap<String, String> archMapping = new HashMap<>();

    static {
        archMapping.put(X86, X86);
        archMapping.put("i386", X86);
        archMapping.put("i486", X86);
        archMapping.put("i586", X86);
        archMapping.put("i686", X86);
        archMapping.put("pentium", X86);
        archMapping.put(X86_64, X86_64);
        archMapping.put("amd64", X86_64);
        archMapping.put("em64t", X86_64);
        archMapping.put("universal", X86_64);
        archMapping.put(IA64, IA64);
        archMapping.put("ia64w", IA64);
        archMapping.put(IA64_32, IA64_32);
        archMapping.put("ia64n", IA64_32);
        archMapping.put(PPC, PPC);
        archMapping.put("power", PPC);
        archMapping.put("powerpc", PPC);
        archMapping.put("power_pc", PPC);
        archMapping.put("power_rs", PPC);
        archMapping.put(PPC64, PPC64);
        archMapping.put("power64", PPC64);
        archMapping.put("powerpc64", PPC64);
        archMapping.put("power_pc64", PPC64);
        archMapping.put("power_rs64", PPC64);
    }

    public static void main(String[] args) {
        if (args.length >= 1) {
            if ("--os".equals(args[0])) {
                System.out.print(getOSName());
                return;
            } else if ("--arch".equals(args[0])) {
                System.out.print(getArchName());
                return;
            }
        }
        System.out.print(getNativeLibFolderPathForCurrentOS());
    }

    public static String getNativeLibFolderPathForCurrentOS() {
        return getOSName() + "/" + getArchName();
    }

    public static String getOSName() {
        return translateOSNameToFolderName(System.getProperty("os.name"));
    }

    public static boolean isAndroid() {
        return System.getProperty("java.runtime.name", "").toLowerCase().contains("android");
    }

    public static boolean isAlpine() {
        try {
            Process p = Runtime.getRuntime().exec("cat /etc/os-release | grep ^ID");
            p.waitFor(300, TimeUnit.MILLISECONDS);
            InputStream in = p.getInputStream();
            try {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                byte[] buf = new byte[32];
                while (true) {
                    int readLen = in.read(buf, 0, buf.length);
                    if (readLen < 0) {
                        break;
                    }
                    b.write(buf, 0, readLen);
                }
                boolean contains = b.toString().toLowerCase().contains("alpine");
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (Throwable th) {
            return false;
        }
    }

    static String getHardwareName() {
        try {
            Process p = Runtime.getRuntime().exec("uname -m");
            p.waitFor();
            InputStream in = p.getInputStream();
            try {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                byte[] buf = new byte[32];
                while (true) {
                    int readLen = in.read(buf, 0, buf.length);
                    if (readLen < 0) {
                        break;
                    }
                    b.write(buf, 0, readLen);
                }
                String byteArrayOutputStream = b.toString();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } catch (Throwable e) {
            System.err.println("Error while running uname -m: " + e.getMessage());
            return "unknown";
        }
    }

    static String resolveArmArchType() {
        if (System.getProperty("os.name").contains("Linux")) {
            String armType = getHardwareName();
            if (armType.startsWith("armv6")) {
                return "armv6";
            }
            if (armType.startsWith("armv7")) {
                return "armv7";
            }
            if (armType.startsWith("armv5")) {
                return "arm";
            }
            if (armType.equals("aarch64")) {
                return "arm64";
            }
            String abi = System.getProperty("sun.arch.abi");
            if (abi != null && abi.startsWith("gnueabihf")) {
                return "armv7";
            }
            String javaHome = System.getProperty("java.home");
            try {
                if (Runtime.getRuntime().exec("which readelf").waitFor() == 0) {
                    if (Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "find '" + javaHome + "' -name 'libjvm.so' | head -1 | xargs readelf -A | grep 'Tag_ABI_VFP_args: VFP registers'"}).waitFor() == 0) {
                        return "armv7";
                    }
                } else {
                    System.err.println("WARNING! readelf not found. Cannot check if running on an armhf system, armel architecture will be presumed.");
                }
            } catch (IOException | InterruptedException e) {
            }
        }
        return "arm";
    }

    public static String getArchName() {
        String osArch = System.getProperty("os.arch");
        if (isAndroid()) {
            return "android-arm";
        }
        if (osArch.startsWith("arm")) {
            osArch = resolveArmArchType();
        } else {
            String lc = osArch.toLowerCase(Locale.US);
            if (archMapping.containsKey(lc)) {
                return archMapping.get(lc);
            }
        }
        return translateArchNameToFolderName(osArch);
    }

    static String translateOSNameToFolderName(String osName) {
        if (osName.contains("Windows")) {
            return "Windows";
        }
        if (osName.contains("Mac") || osName.contains("Darwin")) {
            return "Mac";
        }
        if (isAlpine()) {
            return "Linux-Alpine";
        }
        if (osName.contains("Linux")) {
            return "Linux";
        }
        if (osName.contains("AIX")) {
            return "AIX";
        }
        return osName.replaceAll("\\W", "");
    }

    static String translateArchNameToFolderName(String archName) {
        return archName.replaceAll("\\W", "");
    }
}
