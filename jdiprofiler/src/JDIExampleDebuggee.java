public class JDIExampleDebuggee {
    public static void main(String[] args) {
        var dum = new DummyClass();
        String x = dum.doSomething();
        System.out.println(x);

        print();

        for(int i = 0; i < 2; i++){
            dum.goNuts();
        }

        dum.countToTargetRecursive(3);

        String jpda = "Java Platform Debugger Architecture";
        System.out.println("Hi Everyone, Welcome to " + jpda); // add a break point here

        String jdi = "Java Debug Interface"; // add a break point here and also stepping in here
        String text = "Today, we'll dive into " + jdi;
        System.out.println(text);
    }

    static void print(){
        System.out.println("jeg printer :D");
    }
}