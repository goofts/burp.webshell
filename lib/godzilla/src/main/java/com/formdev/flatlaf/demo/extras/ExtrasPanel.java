package com.formdev.flatlaf.demo.extras;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTriStateCheckBox;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

public class ExtrasPanel extends JPanel {
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JPanel svgIconsPanel;
    private FlatTriStateCheckBox triStateCheckBox1;
    private JLabel triStateLabel1;

    public ExtrasPanel() {
        initComponents();
        this.triStateLabel1.setText(this.triStateCheckBox1.getState().toString());
        addSVGIcon("actions/copy.svg");
        addSVGIcon("actions/colors.svg");
        addSVGIcon("actions/execute.svg");
        addSVGIcon("actions/suspend.svg");
        addSVGIcon("actions/intentionBulb.svg");
        addSVGIcon("actions/quickfixOffBulb.svg");
        addSVGIcon("objects/abstractClass.svg");
        addSVGIcon("objects/abstractMethod.svg");
        addSVGIcon("objects/annotationtype.svg");
        addSVGIcon("objects/annotationtype.svg");
        addSVGIcon("objects/css.svg");
        addSVGIcon("objects/javaScript.svg");
        addSVGIcon("objects/xhtml.svg");
        addSVGIcon("errorDialog.svg");
        addSVGIcon("informationDialog.svg");
        addSVGIcon("warningDialog.svg");
    }

    private void addSVGIcon(String name) {
        this.svgIconsPanel.add(new JLabel(new FlatSVGIcon("com/formdev/flatlaf/demo/extras/svg/" + name)));
    }

    private void triStateCheckBox1Changed() {
        this.triStateLabel1.setText(this.triStateCheckBox1.getState().toString());
    }

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
    private void initComponents() {
        /*
        // Method dump skipped, instructions count: 186
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.extras.ExtrasPanel.initComponents():void");
    }

    private /* synthetic */ void lambda$initComponents$0(ActionEvent e) {
        triStateCheckBox1Changed();
    }
}
