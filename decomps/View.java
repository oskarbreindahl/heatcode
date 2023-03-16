import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class View {
   private Logic logic = new Logic();
   private JFrame frame = new JFrame();
   private JPanel buttonPanel;
   private JTextField output;
   private Font font = new Font("Sans-Serif", 0, 60);

   public View() {
      this.frame.setBackground(Color.BLACK);
      this.frame.setDefaultCloseOperation(3);
      this.frame.setTitle("Calculator");
      this.frame.setSize(400, 600);
      this.frame.setResizable(false);
      this.frame.setLocationRelativeTo((Component)null);
      this.frame.setLayout(new BorderLayout());
      this.output = new JTextField();
      this.output.setEditable(false);
      this.output.setText("0");
      this.output.setFont(this.font);
      this.output.setHorizontalAlignment(4);
      this.frame.add(this.output, "North");
      this.buttonPanel = new JPanel();
      this.buttonPanel.setLayout(new GridLayout(5, 3));
      this.frame.add(this.buttonPanel, "Center");
      this.addNumberButtons();
      this.addOperatorButton("+");
      this.addOperatorButton("-");
      this.addOperatorButton("/");
      this.addOperatorButton("*");
      JButton var1 = new JButton("C");
      var1.addActionListener((var1x) -> {
         this.logic.clear();
         this.output.setText("0");
      });
      var1.setFont(this.font);
      var1.setForeground(Color.red);
      this.buttonPanel.add(var1);
      JButton var2 = new JButton("=");
      var2.addActionListener((var1x) -> {
         this.logic.compute(this.getValue());
         this.output.setText("" + this.logic.getValue());
         this.logic.setOperator("");
      });
      var2.setFont(this.font);
      var2.setBackground(Color.green);
      this.frame.add(var2, "South");
      this.frame.setVisible(true);
   }

   public void addNumberButtons() {
      for(int var1 = 0; var1 < 10; ++var1) {
         String var2 = "" + var1;
         JButton var3 = new JButton(var2);
         var3.addActionListener((var2x) -> {
            String var3 = this.output.getText();
            if (var3.equals("0")) {
               this.output.setText(var2);
            } else {
               this.output.setText(var3 + var2);
            }

         });
         var3.setFont(this.font);
         var3.setBackground(Color.LIGHT_GRAY);
         this.buttonPanel.add(var3);
      }

   }

   public void addOperatorButton(String var1) {
      JButton var2 = new JButton(var1);
      var2.addActionListener((var2x) -> {
         if (!this.output.getText().isEmpty()) {
            this.logic.compute(this.getValue());
            this.logic.setOperator(var1);
            this.output.setText("");
         }

      });
      var2.setFont(this.font);
      this.buttonPanel.add(var2);
   }

   public int getValue() {
      return Integer.parseInt(this.output.getText());
   }
}
