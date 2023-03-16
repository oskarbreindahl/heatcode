public class Logic {
   private int currentNumber = 0;
   private String currentOperator = "";

   public Logic() {
      this.clear();
   }

   public int getValue() {
      return this.getCurrentNumber();
   }

   public void setOperator(String var1) {
      this.setCurrentOperator(var1);
   }

   public void compute(int var1) {
      switch (this.getCurrentOperator()) {
         case "+":
            this.currentNumber += var1;
            break;
         case "-":
            this.currentNumber -= var1;
            break;
         case "*":
            this.currentNumber *= var1;
            break;
         case "/":
            this.currentNumber /= var1;
            break;
         default:
            this.currentNumber = var1;
      }

   }

   public void clear() {
      this.setCurrentNumber(0);
      this.setCurrentOperator("");
   }

   public int getCurrentNumber() {
      return this.currentNumber;
   }

   public void setCurrentNumber(int var1) {
      this.currentNumber = var1;
   }

   public String getCurrentOperator() {
      return this.currentOperator;
   }

   public void setCurrentOperator(String var1) {
      this.currentOperator = var1;
   }
}
