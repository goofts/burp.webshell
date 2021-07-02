package javassist.util;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.AttachingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.event.EventIterator;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.request.EventRequestManager;
import com.sun.jdi.request.MethodEntryRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotSwapper {
    private static final String HOST_NAME = "localhost";
    private static final String TRIGGER_NAME = Trigger.class.getName();
    private VirtualMachine jvm;
    private Map<ReferenceType, byte[]> newClassFiles;
    private MethodEntryRequest request;
    private Trigger trigger;

    public HotSwapper(int port) throws IOException, IllegalConnectorArgumentsException {
        this(Integer.toString(port));
    }

    public HotSwapper(String port) throws IOException, IllegalConnectorArgumentsException {
        this.jvm = null;
        this.request = null;
        this.newClassFiles = null;
        this.trigger = new Trigger();
        AttachingConnector connector = findConnector("com.sun.jdi.SocketAttach");
        Map<String, Connector.Argument> arguments = connector.defaultArguments();
        arguments.get("hostname").setValue(HOST_NAME);
        arguments.get("port").setValue(port);
        this.jvm = connector.attach(arguments);
        this.request = methodEntryRequests(this.jvm.eventRequestManager(), TRIGGER_NAME);
    }

    private Connector findConnector(String connector) throws IOException {
        for (Connector con : Bootstrap.virtualMachineManager().allConnectors()) {
            if (con.name().equals(connector)) {
                return con;
            }
        }
        throw new IOException("Not found: " + connector);
    }

    private static MethodEntryRequest methodEntryRequests(EventRequestManager manager, String classpattern) {
        MethodEntryRequest mereq = manager.createMethodEntryRequest();
        mereq.addClassFilter(classpattern);
        mereq.setSuspendPolicy(1);
        return mereq;
    }

    private void deleteEventRequest(EventRequestManager manager, MethodEntryRequest request2) {
        manager.deleteEventRequest(request2);
    }

    public void reload(String className, byte[] classFile) {
        ReferenceType classtype = toRefType(className);
        Map<ReferenceType, byte[]> map = new HashMap<>();
        map.put(classtype, classFile);
        reload2(map, className);
    }

    public void reload(Map<String, byte[]> classFiles) {
        Map<ReferenceType, byte[]> map = new HashMap<>();
        String className = null;
        for (Map.Entry<String, byte[]> e : classFiles.entrySet()) {
            className = e.getKey();
            map.put(toRefType(className), e.getValue());
        }
        if (className != null) {
            reload2(map, className + " etc.");
        }
    }

    private ReferenceType toRefType(String className) {
        List<ReferenceType> list = this.jvm.classesByName(className);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        throw new RuntimeException("no such class: " + className);
    }

    private void reload2(Map<ReferenceType, byte[]> map, String msg) {
        synchronized (this.trigger) {
            startDaemon();
            this.newClassFiles = map;
            this.request.enable();
            this.trigger.doSwap();
            this.request.disable();
            if (this.newClassFiles != null) {
                this.newClassFiles = null;
                throw new RuntimeException("failed to reload: " + msg);
            }
        }
    }

    private void startDaemon() {
        new Thread() {
            /* class javassist.util.HotSwapper.AnonymousClass1 */

            private void errorMsg(Throwable e) {
                System.err.print("Exception in thread \"HotSwap\" ");
                e.printStackTrace(System.err);
            }

            public void run() {
                EventSet events = null;
                try {
                    events = HotSwapper.this.waitEvent();
                    EventIterator iter = events.eventIterator();
                    while (true) {
                        if (iter.hasNext()) {
                            if (iter.nextEvent() instanceof MethodEntryEvent) {
                                HotSwapper.this.hotswap();
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Throwable e) {
                    errorMsg(e);
                }
                if (events != null) {
                    try {
                        events.resume();
                    } catch (Throwable e2) {
                        errorMsg(e2);
                    }
                }
            }
        }.start();
    }

    /* access modifiers changed from: package-private */
    public EventSet waitEvent() throws InterruptedException {
        return this.jvm.eventQueue().remove();
    }

    /* access modifiers changed from: package-private */
    public void hotswap() {
        this.jvm.redefineClasses(this.newClassFiles);
        this.newClassFiles = null;
    }
}
