import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

public class JDIExampleDebugger {
    private String debugJar;
    private List<String> filteredClasses = List.of("java.*", "jdk.*", "com.sun.*", "sun.*", "javax.*",
            "com.apple.*", "apple.*");
    private VirtualMachine vm;
    private CallContainer callContainer;

    public JDIExampleDebugger(String jar) throws Exception {
        this.debugJar = jar;
        this.callContainer = new CallContainer();
        connectAndLaunchVM();
        enableClassPrepareRequest();
        enableMethodEntryRequest();
    }
    public void run() {
        try {
            EventSet eventSet = null;
            while ((eventSet = vm.eventQueue().remove()) != null) {
                for (Event event : eventSet) {
                    if (event instanceof ClassPrepareEvent) {
                        System.out.println("loaded class " + ((ClassPrepareEvent) event).referenceType().name());
                    }
                    if (event instanceof MethodEntryEvent) {
                        var method = ((MethodEntryEvent) event).method();
                        callContainer.call(method);
                        System.out.println("entered " + method.name() + " in " + method.declaringType().name());
                    }
                    vm.resume();
                }
            }
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");
            callContainer.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        var debugger = new JDIExampleDebugger(args[0]);
        debugger.run();
    }

    public void enableClassPrepareRequest() {
        var classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        filteredClasses.forEach(classPrepareRequest::addClassExclusionFilter);
        classPrepareRequest.enable();
    }

    public void enableMethodEntryRequest() {
        var methodEntryRequest = vm.eventRequestManager().createMethodEntryRequest();
        filteredClasses.forEach(methodEntryRequest::addClassExclusionFilter);
        methodEntryRequest.enable();
    }

    public void connectAndLaunchVM() throws Exception {
        var launchingConnector = Bootstrap.virtualMachineManager().defaultConnector();
        var arguments = launchingConnector.defaultArguments();

        var invocationPath = System.getProperty("user.dir") + "\\" + this.debugJar;
        arguments.get("options").setValue("-cp " + invocationPath);

        var mainClass = getManifestMain(invocationPath);
        System.out.println(mainClass);
        arguments.get("main").setValue(mainClass);

        vm = launchingConnector.launch(arguments);
    }

    private String getManifestMain(String path) throws IOException {
        var jar = new JarFile(path);
        var main = jar.getManifest().getMainAttributes().getValue("Main-Class");
        jar.close();
        return main;
    }

}