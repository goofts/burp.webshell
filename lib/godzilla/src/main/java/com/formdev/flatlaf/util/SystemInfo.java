package com.formdev.flatlaf.util;

import java.util.Locale;
import java.util.StringTokenizer;

public class SystemInfo {
    public static final boolean isJava_11_orLater;
    public static final boolean isJava_15_orLater;
    public static final boolean isJava_9_orLater;
    public static final boolean isJetBrainsJVM = System.getProperty("java.vm.vendor", "Unknown").toLowerCase(Locale.ENGLISH).contains("jetbrains");
    public static final boolean isJetBrainsJVM_11_orLater;
    public static final boolean isKDE;
    public static final boolean isLinux;
    public static final boolean isMacOS;
    public static final boolean isMacOS_10_11_ElCapitan_orLater;
    public static final boolean isMacOS_10_14_Mojave_orLater;
    public static final boolean isMacOS_10_15_Catalina_orLater;
    public static final boolean isWindows;
    public static final boolean isWindows_10_orLater = (isWindows && osVersion >= toVersion(10, 0, 0, 0));
    public static final long javaVersion = scanVersion(System.getProperty("java.version"));
    public static final long osVersion = scanVersion(System.getProperty("os.version"));

    static {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8 = true;
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        isWindows = osName.startsWith("windows");
        isMacOS = osName.startsWith("mac");
        isLinux = osName.startsWith("linux");
        if (!isMacOS || osVersion < toVersion(10, 11, 0, 0)) {
            z = false;
        } else {
            z = true;
        }
        isMacOS_10_11_ElCapitan_orLater = z;
        if (!isMacOS || osVersion < toVersion(10, 14, 0, 0)) {
            z2 = false;
        } else {
            z2 = true;
        }
        isMacOS_10_14_Mojave_orLater = z2;
        if (!isMacOS || osVersion < toVersion(10, 15, 0, 0)) {
            z3 = false;
        } else {
            z3 = true;
        }
        isMacOS_10_15_Catalina_orLater = z3;
        if (javaVersion >= toVersion(9, 0, 0, 0)) {
            z4 = true;
        } else {
            z4 = false;
        }
        isJava_9_orLater = z4;
        if (javaVersion >= toVersion(11, 0, 0, 0)) {
            z5 = true;
        } else {
            z5 = false;
        }
        isJava_11_orLater = z5;
        if (javaVersion >= toVersion(15, 0, 0, 0)) {
            z6 = true;
        } else {
            z6 = false;
        }
        isJava_15_orLater = z6;
        if (!isJetBrainsJVM || !isJava_11_orLater) {
            z7 = false;
        } else {
            z7 = true;
        }
        isJetBrainsJVM_11_orLater = z7;
        if (!isLinux || System.getenv("KDE_FULL_SESSION") == null) {
            z8 = false;
        }
        isKDE = z8;
    }

    public static long scanVersion(String version) {
        int major = 1;
        int minor = 0;
        int micro = 0;
        int patch = 0;
        try {
            StringTokenizer st = new StringTokenizer(version, "._-+");
            major = Integer.parseInt(st.nextToken());
            minor = Integer.parseInt(st.nextToken());
            micro = Integer.parseInt(st.nextToken());
            patch = Integer.parseInt(st.nextToken());
        } catch (Exception e) {
        }
        return toVersion(major, minor, micro, patch);
    }

    public static long toVersion(int major, int minor, int micro, int patch) {
        return (((long) major) << 48) + (((long) minor) << 32) + (((long) micro) << 16) + ((long) patch);
    }
}
