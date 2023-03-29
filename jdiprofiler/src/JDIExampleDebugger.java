import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
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

    public void run() throws Exception {
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
            //callContainer.print();
            var output = JsonSerializer.serialize(callContainer);
            writeToFile(output);
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

        var debuggerLocation = new File(JDIExampleDebugger.class.getProtectionDomain().getCodeSource().getLocation().toURI());

        arguments.get("options").setValue("-cp " + debuggerLocation + ";" + debugJar);
        System.out.println(debuggerLocation);
        System.out.println(debugJar);
        System.out.println(arguments.get("options"));

        var mainClass = getManifestMain(debugJar);
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

    private void writeToFile(String output) throws IOException {
        var writer = new BufferedWriter(new FileWriter("out.json"));
        writer.write(output);
        writer.close();
    }

}