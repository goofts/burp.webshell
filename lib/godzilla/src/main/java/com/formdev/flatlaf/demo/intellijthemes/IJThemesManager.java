package com.formdev.flatlaf.demo.intellijthemes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* access modifiers changed from: package-private */
public class IJThemesManager {
    final List<IJThemeInfo> bundledThemes = new ArrayList();
    private final Map<File, Long> lastModifiedMap = new HashMap();
    final List<IJThemeInfo> moreThemes = new ArrayList();

    IJThemesManager() {
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadBundledThemes() {
        /*
        // Method dump skipped, instructions count: 179
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.intellijthemes.IJThemesManager.loadBundledThemes():void");
    }

    /* access modifiers changed from: package-private */
    /*  JADX ERROR: MOVE_RESULT instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: MOVE_RESULT instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:604)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:542)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:746)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:367)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    public void loadThemesFromDirectory() {
        /*
        // Method dump skipped, instructions count: 129
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.intellijthemes.IJThemesManager.loadThemesFromDirectory():void");
    }

    private static /* synthetic */ boolean lambda$loadThemesFromDirectory$0(File dir, String name) {
        return name.endsWith(".theme.json") || name.endsWith(".properties");
    }

    /* access modifiers changed from: package-private */
    public boolean hasThemesFromDirectoryChanged() {
        for (Map.Entry<File, Long> e : this.lastModifiedMap.entrySet()) {
            if (e.getKey().lastModified() != e.getValue().longValue()) {
                return true;
            }
        }
        return false;
    }
}
