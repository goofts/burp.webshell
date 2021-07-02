package javassist.tools.web;

import java.io.IOException;
import java.net.Socket;

/* access modifiers changed from: package-private */
/* compiled from: Webserver */
public class ServiceThread extends Thread {
    Socket sock;
    Webserver web;

    public ServiceThread(Webserver w, Socket s) {
        this.web = w;
        this.sock = s;
    }

    public void run() {
        try {
            this.web.process(this.sock);
        } catch (IOException e) {
        }
    }
}
