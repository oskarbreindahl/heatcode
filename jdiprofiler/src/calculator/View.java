package calculator;

import java.awt.*;
import javax.swing.*;

public class View {
    private Logic logic;
    private JFrame frame;
    private JPanel buttonPanel;
    private JTextField output;
    private Font font;


    public View() {
        logic = new Logic();
        font = new Font("Sans-Serif", Font.PLAIN, 60);
        frame = new JFrame();
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Calculator");
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        frame.setLayout(new BorderLayout());
        output = new JTextField();
        output.setEditable(false);
        output.setText("0");
        output.setFont(font);
        output.setHorizontalAlignment(JTextField.RIGHT);

        frame.add(output, BorderLayout.NORTH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5,3));
        frame.add(buttonPanel, BorderLayout.CENTER);
        addNumberButtons();

        addOperatorButton("+");
        addOperatorButton("-");
        addOperatorButton("/");
        addOperatorButton("*");

        JButton clearButton = new JButton("C");
        clearButton.addActionListener(
                e -> {
                    logic.clear();
                    output.setText("0");
                }
        );
        clearButton.setFont(font);
        clearButton.setForeground(Color.red);
        buttonPanel.add(clearButton);

        JButton equalsButton = new JButton("=");
        equalsButton.addActionListener(
                e -> {
                    logic.compute(getValue());
                    output.setText(""+ logic.getValue());
                    logic.setOperator("");
                }
        );
        equalsButton.setFont(font);
        equalsButton.setBackground(Color.green);
        frame.add(equalsButton, BorderLayout.SOUTH);


        frame.setVisible(true);
    }

    public void addNumberButtons() {
        for (int i = 0; i < 10; i++) {
            String label = "" + i;
            JButton button = new JButton(label);

            button.addActionListener(
                    e -> {
                        String before = output.getText();
                        if (before.equals("0")) {
                            output.setText(label);
                        } else {
                            output.setText(before + label);
                        }
                    }
            );
            button.setFont(font);
            button.setBackground(Color.LIGHT_GRAY);
            buttonPanel.add(button);
        }
    }

    public void addOperatorButton(String operator) {
        JButton button = new JButton(operator);
        button.addActionListener(
                e -> {
                    if (!output.getText().isEmpty()){
                        logic.compute(getValue());
                        logic.setOperator(operator);
                        output.setText("");
                    }
                }
        );
        button.setFont(font);
        buttonPanel.add(button);

    }

    public int getValue() {
        return Integer.parseInt(output.getText());
    }
}
