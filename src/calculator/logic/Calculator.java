package calculator.logic;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator implements ActionListener {

	private static Calculator calculator;

	// FONT VALUES
	private static final String FONT_TYPE = "LCD";
	private static final int FONT_SIZE = 30;

	// PANEL BOUNDS
	private static final int PANEL_X = 50, PANEL_Y = 100, PANEL_WEIGHT = 300, PANEL_HEIGHT = 300;

	// TEXT FIELD BOUNDS
	private static final int TEXTF_X = 50, TEXTF_Y = 25, TEXTF_WEIGHT = 300, TEXTF_HEIGHT = 40;

	// DELETE BOTTON BOUNDS
	private static final int DELBUTTON_X = 50, DELBUTTON_Y = 430, DELBUTTON_WEIGHT = 145, DELBUTTON_HEIGHT = 50;

	// CLEAR BUTTON BOUNDS
	private static final int CLEARBUTTON_X = 205, CLEARBUTTON_Y = 430, CLEARBUTTON_WEIGHT = 145,
			CLEARBUTTON_HEIGHT = 50;

	JFrame frame;
	JTextField textField;

	// Buttons lists
	JButton[] numberButtons = new JButton[10];
	JButton[] functionButtons = new JButton[8];

	// Function buttons
	JButton addButton, subButton, mulButton, divButton;
	JButton decButton, equalButton, delButton, clearButton;

	JPanel panel;

	Font myFont = new Font(FONT_TYPE, Font.PLAIN, FONT_SIZE);

	Double num1 = 0d, num2 = 0d, result = 0d;
	
	//Chain is used for the first number you introduce and after a result, it calculates multiples number that you set in a row, 
	//reset is for when you want to introduce a number after the result.
	private boolean chain = false, reset = false;
	char operator = '?';

	private Calculator() {
		frame = new JFrame("Calculator");
		configFrame(frame);

		textField = new JTextField();
		configTextField(textField);

		setFunctionButtons();
		configFunctionButtons();

		setNumberBottons();

		confDelClearButtons();

		panel = new JPanel();
		setPanel();

		setFrame();
	}

	// *-*-*-*-*-*-* Configurations *-*-*-*-*-*-*
	
	/**
	 * Singleton design pattern.
	 * 
	 * @return
	 */
	public static Calculator getCalculator() {
		if (calculator == null)
			calculator = new Calculator();
		return calculator;
	}

	/**
	 * Sets all the components of the frame and make it visible.
	 */
	private void setFrame() {
		frame.add(panel);
		frame.add(delButton);
		frame.add(clearButton);
		frame.add(textField);
		frame.setVisible(true);
	}

	/**
	 * Sets size, layout and all the buttons to the panel.
	 */
	private void setPanel() {
		panel.setBounds(PANEL_X, PANEL_Y, PANEL_WEIGHT, PANEL_HEIGHT);
		panel.setLayout(new GridLayout(4, 4, 10, 10));

		panel.add(numberButtons[7]);
		panel.add(numberButtons[8]);
		panel.add(numberButtons[9]);
		panel.add(addButton);
		panel.add(numberButtons[4]);
		panel.add(numberButtons[5]);
		panel.add(numberButtons[6]);
		panel.add(subButton);
		panel.add(numberButtons[1]);
		panel.add(numberButtons[2]);
		panel.add(numberButtons[3]);
		panel.add(mulButton);
		panel.add(decButton);
		panel.add(numberButtons[0]);
		panel.add(equalButton);
		panel.add(divButton);
	}

	/**
	 * configure the size of delete and clear buttons.
	 */
	private void confDelClearButtons() {
		delButton.setBounds(DELBUTTON_X, DELBUTTON_Y, DELBUTTON_WEIGHT, DELBUTTON_HEIGHT);
		clearButton.setBounds(CLEARBUTTON_X, CLEARBUTTON_Y, CLEARBUTTON_WEIGHT, CLEARBUTTON_HEIGHT);
	}

	/**
	 * add all buttons to the list numberButtons also add action listener, font
	 * type, and focusable to false.
	 */
	private void setNumberBottons() {
		for (int i = 0; i < 10; i++) {
			numberButtons[i] = new JButton(String.valueOf(i));
			numberButtons[i].addActionListener(this);
			numberButtons[i].setFont(myFont);
			numberButtons[i].setFocusable(false);
		}
	}

	/**
	 * Configure size, Close method and layout of the frame passed by parameter.
	 * 
	 * @param frame
	 */
	private void configFrame(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 550);
		frame.setLayout(null);
	}

	/**
	 * Configure text field size, font and make it not editable.
	 * 
	 * @param textField
	 */
	private void configTextField(JTextField textField) {
		textField.setBounds(TEXTF_X, TEXTF_Y, TEXTF_WEIGHT, TEXTF_HEIGHT);
		textField.setFont(myFont);
		textField.setEditable(false);
	}

	/**
	 * Set all buttons a function and a symbol, then added to the function buttons
	 * list.
	 */
	private void setFunctionButtons() {
		addButton = new JButton("+");
		subButton = new JButton("-");
		mulButton = new JButton("x");
		divButton = new JButton("/");
		decButton = new JButton(",");
		equalButton = new JButton("=");
		delButton = new JButton("Delete");
		clearButton = new JButton("Clear");

		functionButtons[0] = addButton;
		functionButtons[1] = subButton;
		functionButtons[2] = mulButton;
		functionButtons[3] = divButton;
		functionButtons[4] = decButton;
		functionButtons[5] = equalButton;
		functionButtons[6] = delButton;
		functionButtons[7] = clearButton;
	}

	/**
	 * Configure all buttons in function Buttons adding action listener, font style
	 * and making them not focusable.
	 */
	private void configFunctionButtons() {
		for (JButton b : functionButtons) {
			b.addActionListener(this);
			b.setFont(myFont);
			b.setFocusable(false);
		}
	}

	// *-*-*-*-*-* Functions *-*-*-*-*-*
	
	/**
	 * All functions
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		checkForANumber(e);

		// Verifies field is not empty
		if (!textField.getText().isEmpty()) {

			makeAnOperation(e);

			clearAndDelete(e);
		}
	}

	/**
	 * Check if the button is a number, if reset is true it will first blank the field, also check for decimal
	 * @param e
	 */
	private void checkForANumber(ActionEvent e) {
		
		for (int i = 0; i < 10; i++) {
			if (e.getSource() == numberButtons[i]) {
				if (reset == true) {
					blankTextField();
					reset = false;
				}
				textField.setText(textField.getText().concat(String.valueOf(i)));
				break;
			}		
		}

		if (e.getSource() == decButton)
			textField.setText(textField.getText().concat("."));
		
	}

	/**
	 * Make operations if inserts numbers and operations in a row, or when you hit the button equals.
	 * @param e
	 */
	private void makeAnOperation(ActionEvent e) {

		if (e.getSource() == addButton) {
			doThisOperation('+');
		}

		else if (e.getSource() == subButton) {
			doThisOperation('-');
		}

		else if (e.getSource() == mulButton) {
			doThisOperation('x');
		}

		else if (e.getSource() == divButton) {
			doThisOperation('/');
		}

		else if (e.getSource() == equalButton) {
			getFinalResult();
		}
	}

	/**
	 * Make the basics operations +, -, *, /
	 * 
	 * @param n1 first number
	 * @param n2 second number
	 * @return result or and exception if you try to divide by 0
	 */
	private Double operation(Double n1, Double n2) {
		Double r = 0d;

		System.out.println(n1 + " " + operator + " " + n2);
		switch (operator) {
		case '+':
			r = n1 + n2;
			break;

		case '-':
			r = n1 - n2;
			break;

		case 'x':
			r = n1 * n2;
			break;

		case '/': {
			if (n2 == 0)
				throw new ArithmeticException();
			else
				r = n1 / n2;
			break;
			}
		}
		
		System.out.println(r);
		return r;
	}

	/**
	 * Save the 2nd number, calculate the result and shows it, previously you had to select any operation. 
	 * if try to divide by 0, shows an error, finally set reset to true.
	 */
	private void getFinalResult() {
		num2 = getNumber();
		
		if (operator != '?') {
			try {
				result = operation(num1, num2);
				chain = false;

				textField.setText(String.valueOf(result));
			} catch (Exception arException) {
				textField.setText("Cannot divide by 0");
			} finally {
				reset = true;
			}
		}
	}
	
	/**
	 * If the chain is false then only set the first number and save the operation.
	 * If the chain is true, then the first number will be the operation between the previous num1 and the current number in the field
	 * with the previous operation then save the new operation.
	 * @param op operation
	 */
	private void doThisOperation(char op) {
		setFirstNumber();
		blankTextField();
		operator = op;
	}
	
	/**
	 * Set the first number depending if it's a chain operation or the first number.
	 */
	private void setFirstNumber() {
		if (chain == false) {
			num1 = getNumber();
			chain = true;
		} else
			num1 = operation(num1, getNumber());
	}
	
	/**
	 * Clear function blanks the field and Delete function remove the last number.
	 * @param e
	 */
	private void clearAndDelete(ActionEvent e) {
		if (e.getSource() == clearButton) {
			blankTextField();
		}

		else if (e.getSource() == delButton) {
			textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
		}
	}

	/**
	 * Gets the number from the field.
	 * @return
	 */
	private Double getNumber() {
		return Double.parseDouble(textField.getText());
	}
	
	/**
	 * It just does that :).
	 */
	private void blankTextField() {
		textField.setText("");
	}
}