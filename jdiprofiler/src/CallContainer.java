import com.sun.jdi.Method;
import com.sun.jdi.ReferenceType;

import java.util.HashMap;
import java.util.Map;

public class CallContainer {
    private Map<Method, Integer> map;

    public CallContainer() {
        map = new HashMap<>();
    }

    public void call(Method method) {
        if (map.containsKey(method)) {
            map.put(method, map.get(method) + 1);
            return;
        }
        map.put(method, 1);
    }

    public void print() {
        map.forEach((method,calls) -> System.out.println(method.declaringType().name() + "." + method.name() + "()" + " called " + calls + " times"));
    }
}