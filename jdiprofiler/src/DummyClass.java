public class DummyClass {
    String doSomething() {
        return new DummyClassInner().help();
    }

    void goNuts() {
        return;
    }

    int countToTargetRecursive(int target) {
        return countToTargetRecursiveAux(0, target);
    }

    private int countToTargetRecursiveAux(int current, int target) {
        if (current != target) {
            return countToTargetRecursiveAux(current+1, target);
        }
        return current;
    }
}

class DummyClassInner {
    String help() {
        return "hej";
    }
}
