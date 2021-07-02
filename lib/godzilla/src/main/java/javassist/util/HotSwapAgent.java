package javassist.util;

import com.sun.tools.attach.VirtualMachine;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.management.ManagementFactory;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public class HotSwapAgent {
    private static Instrumentation instrumentation = null;

    public Instrumentation instrumentation() {
        return instrumentation;
    }

    public static void premain(String agentArgs, Instrumentation inst) throws Throwable {
        agentmain(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws Throwable {
        if (!inst.isRedefineClassesSupported()) {
            throw new RuntimeException("this JVM does not support redefinition of classes");
        }
        instrumentation = inst;
    }

    public static void redefine(Class<?> oldClass, CtClass newClass) throws NotFoundException, IOException, CannotCompileException {
        redefine(new Class[]{oldClass}, new CtClass[]{newClass});
    }

    public static void redefine(Class<?>[] oldClasses, CtClass[] newClasses) throws NotFoundException, IOException, CannotCompileException {
        startAgent();
        ClassDefinition[] defs = new ClassDefinition[oldClasses.length];
        for (int i = 0; i < oldClasses.length; i++) {
            defs[i] = new ClassDefinition(oldClasses[i], newClasses[i].toBytecode());
        }
        try {
            instrumentation.redefineClasses(defs);
        } catch (ClassNotFoundException e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (UnmodifiableClassException e2) {
            throw new CannotCompileException(e2.getMessage(), (Throwable) e2);
        }
    }

    private static void startAgent() throws NotFoundException {
        if (instrumentation == null) {
            try {
                File agentJar = createJarFile();
                String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
                VirtualMachine vm = VirtualMachine.attach(nameOfRunningVM.substring(0, nameOfRunningVM.indexOf(64)));
                vm.loadAgent(agentJar.getAbsolutePath(), (String) null);
                vm.detach();
                for (int sec = 0; sec < 10; sec++) {
                    if (instrumentation == null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        return;
                    }
                }
                throw new NotFoundException("hotswap agent (timeout)");
            } catch (Exception e2) {
                throw new NotFoundException("hotswap agent", e2);
            }
        }
    }

    public static File createAgentJarFile(String fileName) throws IOException, CannotCompileException, NotFoundException {
        return createJarFile(new File(fileName));
    }

    private static File createJarFile() throws IOException, CannotCompileException, NotFoundException {
        File jar = File.createTempFile("agent", ".jar");
        jar.deleteOnExit();
        return createJarFile(jar);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0097  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File createJarFile(java.io.File r11) throws java.io.IOException, javassist.CannotCompileException, javassist.NotFoundException {
        /*
        // Method dump skipped, instructions count: 158
        */
        throw new UnsupportedOperationException("Method not decompiled: javassist.util.HotSwapAgent.createJarFile(java.io.File):java.io.File");
    }
}
