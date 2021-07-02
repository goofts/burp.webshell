package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.EventQueue;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFormattedTextField;
import javax.swing.plaf.UIResource;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class FlatCaret extends DefaultCaret implements UIResource {
    private boolean isMousePressed;
    private final String selectAllOnFocusPolicy;
    private final boolean selectAllOnMouseClick;
    private boolean wasFocused;
    private boolean wasTemporaryLost;

    public FlatCaret(String selectAllOnFocusPolicy2, boolean selectAllOnMouseClick2) {
        this.selectAllOnFocusPolicy = selectAllOnFocusPolicy2;
        this.selectAllOnMouseClick = selectAllOnMouseClick2;
    }

    public void install(JTextComponent c) {
        int length;
        FlatCaret.super.install(c);
        Document doc = c.getDocument();
        if (doc != null && getDot() == 0 && getMark() == 0 && (length = doc.getLength()) > 0) {
            setDot(length);
        }
    }

    public void focusGained(FocusEvent e) {
        if (!this.wasTemporaryLost && (!this.isMousePressed || this.selectAllOnMouseClick)) {
            selectAllOnFocusGained();
        }
        this.wasTemporaryLost = false;
        this.wasFocused = true;
        FlatCaret.super.focusGained(e);
    }

    public void focusLost(FocusEvent e) {
        this.wasTemporaryLost = e.isTemporary();
        FlatCaret.super.focusLost(e);
    }

    public void mousePressed(MouseEvent e) {
        this.isMousePressed = true;
        FlatCaret.super.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        this.isMousePressed = false;
        FlatCaret.super.mouseReleased(e);
    }

    /* access modifiers changed from: protected */
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
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:99)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:143)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:99)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:143)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
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
    public void selectAllOnFocusGained() {
        /*
            r6 = this;
            javax.swing.text.JTextComponent r0 = r6.getComponent()
            javax.swing.text.Document r1 = r0.getDocument()
            if (r1 == 0) goto L_0x0016
            boolean r5 = r0.isEnabled()
            if (r5 == 0) goto L_0x0016
            boolean r5 = r0.isEditable()
            if (r5 != 0) goto L_0x0017
        L_0x0016:
            return
        L_0x0017:
            java.lang.String r5 = "JTextField.selectAllOnFocusPolicy"
            java.lang.Object r4 = r0.getClientProperty(r5)
            if (r4 != 0) goto L_0x0021
            java.lang.String r4 = r6.selectAllOnFocusPolicy
        L_0x0021:
            java.lang.String r5 = "never"
            boolean r5 = r5.equals(r4)
            if (r5 != 0) goto L_0x0016
            java.lang.String r5 = "always"
            boolean r5 = r5.equals(r4)
            if (r5 != 0) goto L_0x0045
            boolean r5 = r6.wasFocused
            if (r5 != 0) goto L_0x0016
            int r2 = r6.getDot()
            int r3 = r6.getMark()
            if (r2 != r3) goto L_0x0016
            int r5 = r1.getLength()
            if (r2 != r5) goto L_0x0016
        L_0x0045:
            boolean r5 = r0 instanceof javax.swing.JFormattedTextField
            if (r5 == 0) goto L_0x0051
            r5 = move-result
            java.awt.EventQueue.invokeLater(r5)
            goto L_0x0016
        L_0x0051:
            r5 = 0
            r6.setDot(r5)
            int r5 = r1.getLength()
            r6.moveDot(r5)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatCaret.selectAllOnFocusGained():void");
    }

    private /* synthetic */ void lambda$selectAllOnFocusGained$0(Document doc) {
        setDot(0);
        moveDot(doc.getLength());
    }
}
