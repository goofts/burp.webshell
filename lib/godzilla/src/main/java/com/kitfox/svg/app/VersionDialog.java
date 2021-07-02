package com.kitfox.svg.app;

import com.kitfox.svg.SVGConst;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javassist.compiler.TokenId;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class VersionDialog extends JDialog {
    public static final long serialVersionUID = 1;
    private JButton bn_close;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JTextPane textpane_text;
    final boolean verbose;

    public VersionDialog(Frame parent, boolean modal, boolean verbose2) {
        super(parent, modal);
        initComponents();
        this.verbose = verbose2;
        this.textpane_text.setContentType("text/html");
        StringBuffer sb = new StringBuffer();
        try {
            URL url = getClass().getResource("/res/help/about/about.html");
            if (verbose2) {
                System.err.println("" + getClass() + " trying to load about html " + url);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    this.textpane_text.setText(sb.toString());
                    return;
                }
                sb.append(line);
            }
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
        }
    }

    private void initComponents() {
        this.jPanel1 = new JPanel();
        this.textpane_text = new JTextPane();
        this.jPanel2 = new JPanel();
        this.bn_close = new JButton();
        setDefaultCloseOperation(2);
        setTitle("About SVG Salamander");
        this.jPanel1.setLayout(new BorderLayout());
        this.textpane_text.setEditable(false);
        this.textpane_text.setPreferredSize(new Dimension((int) TokenId.Identifier, (int) TokenId.ABSTRACT));
        this.jPanel1.add(this.textpane_text, "Center");
        getContentPane().add(this.jPanel1, "Center");
        this.bn_close.setText("Close");
        this.bn_close.addActionListener(new ActionListener() {
            /* class com.kitfox.svg.app.VersionDialog.AnonymousClass1 */

            public void actionPerformed(ActionEvent evt) {
                VersionDialog.this.bn_closeActionPerformed(evt);
            }
        });
        this.jPanel2.add(this.bn_close);
        getContentPane().add(this.jPanel2, "South");
        pack();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void bn_closeActionPerformed(ActionEvent evt) {
        setVisible(false);
        dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            /* class com.kitfox.svg.app.VersionDialog.AnonymousClass2 */

            public void run() {
                new VersionDialog(new JFrame(), true, true).setVisible(true);
            }
        });
    }
}
