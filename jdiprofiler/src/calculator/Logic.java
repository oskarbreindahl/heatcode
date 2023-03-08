package calculator;

public class Logic {
    private int currentNumber;

    private String currentOperator;

    public Logic() {
        currentNumber = 0;
        currentOperator = "";
        clear();
    }

    public int getValue() {
        return getCurrentNumber();
    }

    public void setOperator(String operator) {
        setCurrentOperator(operator);
    }

    public void compute(int newValue) {
        switch(getCurrentOperator()){
            case "+":
                currentNumber += newValue;
                break;
            case "-":
                currentNumber -= newValue;
                break;
            case "*":
                currentNumber *= newValue;
                break;
            case "/":
                currentNumber /= newValue;
                break;
            default:
                currentNumber = newValue;
        }
    }

    public void clear(){
        setCurrentNumber(0);
        setCurrentOperator("");
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
    }

    public String getCurrentOperator() {
        return currentOperator;
    }

    public void setCurrentOperator(String currentOperator) {
        this.currentOperator = currentOperator;
    }
}
