import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.MethodEntryRequest;

import java.util.List;
import java.util.Map;

public class JDIExampleDebugger {
    private Class debugClass;
    private int[] breakPointLines;

    private List<String> filteredClasses = List.of("java.*", "jdk.*", "com.sun.*", "sun.*", "javax.*");

    public static void main(String[] args) throws Exception {
        JDIExampleDebugger debuggerInstance = new JDIExampleDebugger();
        debuggerInstance.setDebugClass(calculator.Main.class);
        int[] breakPoints = {7,  13};
        debuggerInstance.setBreakPointLines(breakPoints);
        VirtualMachine vm = null;
        try {
            vm = debuggerInstance.connectAndLaunchVM();
            debuggerInstance.enableClassPrepareRequest(vm);
            debuggerInstance.enableMethodEntryRequest(vm);
            EventSet eventSet = null;
            while ((eventSet = vm.eventQueue().remove()) != null) {
                for (Event event : eventSet) {
                    if (event instanceof ClassPrepareEvent) {
                        //debuggerInstance.setBreakPoints(vm, (ClassPrepareEvent)event);
                        System.out.println("loaded class " + ((ClassPrepareEvent) event).referenceType().name());
                    }
                    if (event instanceof BreakpointEvent) {
                        debuggerInstance.displayVariables((BreakpointEvent) event);
                    }
                    if (event instanceof MethodEntryEvent) {
                        var method = ((MethodEntryEvent) event).method();
                        System.out.println("entered " + method.name() + " in " + method.declaringType().name());
                    }
                    vm.resume();
                }
            }
        } catch (VMDisconnectedException e) {
            System.out.println("Virtual Machine is disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableClassPrepareRequest(VirtualMachine vm) {
        ClassPrepareRequest classPrepareRequest = vm.eventRequestManager().createClassPrepareRequest();
        //classPrepareRequest.addClassFilter(debugClass.getName());
        filteredClasses.forEach(classPrepareRequest::addClassExclusionFilter);
        classPrepareRequest.enable();
    }

    public void enableMethodEntryRequest(VirtualMachine vm) {
        MethodEntryRequest methodEntryRequest = vm.eventRequestManager().createMethodEntryRequest();
        var jdiPackagePrefix = "com.sun.tools.jdi.*";
        //methodEntryRequest.addClassFilter(jdiPackagePrefix);
        //methodEntryRequest.addClassFilter(debugClass.getName());
        //methodEntryRequest.addClassFilter(DummyClass.class.getName());
        filteredClasses.forEach(methodEntryRequest::addClassExclusionFilter);
        methodEntryRequest.enable();
    }

    public VirtualMachine connectAndLaunchVM() throws Exception {
        LaunchingConnector launchingConnector = Bootstrap.virtualMachineManager()
                .defaultConnector();
        Map<String, Connector.Argument> arguments = launchingConnector.defaultArguments();
        arguments.get("main").setValue(debugClass.getName());
        var vm = launchingConnector.launch(arguments);
        //vm.eventRequestManager().createMethodEntryRequest().enable();

        return vm;
    }

    public void setBreakPoints(VirtualMachine vm, ClassPrepareEvent event) throws AbsentInformationException {
        ClassType classType = (ClassType) event.referenceType();
        for(int lineNumber: breakPointLines) {
            Location location = classType.locationsOfLine(lineNumber).get(0);
            BreakpointRequest bpReq = vm.eventRequestManager().createBreakpointRequest(location);
            bpReq.enable();
        }
    }

    public void displayVariables(LocatableEvent event) throws IncompatibleThreadStateException,
            AbsentInformationException {
        StackFrame stackFrame = event.thread().frame(0);
        if(stackFrame.location().toString().contains(debugClass.getName())) {
            Map<LocalVariable, Value> visibleVariables = stackFrame
                    .getValues(stackFrame.visibleVariables());
            System.out.println("Variables at " + stackFrame.location().toString() +  " > ");
            for (Map.Entry<LocalVariable, Value> entry : visibleVariables.entrySet()) {
                System.out.println(entry.getKey().name() + " = " + entry.getValue());
            }
        }
    }

    public Class getDebugClass() {
        return debugClass;
    }

    public void setDebugClass(Class debugClass) {
        this.debugClass = debugClass;
    }

    public int[] getBreakPointLines() {
        return breakPointLines;
    }

    public void setBreakPointLines(int[] breakPointLines) {
        this.breakPointLines = breakPointLines;
    }

}