package javassist.util.proxy;

public interface ProxyObject extends Proxy {
    MethodHandler getHandler();

    @Override // javassist.util.proxy.Proxy
    void setHandler(MethodHandler methodHandler);
}
