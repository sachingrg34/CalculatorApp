package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private double firstNumber = 0;
    private double secondNumber = 0;
    private TextView Text_display;
    private boolean newInput = true;

    private String operator = "";

    private ArrayList<Double> numbers = new ArrayList<>();
    private ArrayList<String> operators = new ArrayList<>();
    private String currentInput = "";

    private boolean errorState = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Text_display = findViewById(R.id.Text_display);


        setupNumberButtons();
        setOperatorListeners();
        setupSpecialButtons();
        updateDisplay();
    }

    private void  setupNumberButtons() {
        int[] numberIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        };
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                onNumberClick(b.getText().toString());
            }
        };



        for (int id : numberIds) {
            findViewById(id).setOnClickListener(listener);
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> onNumberClick("."));
    }

    private void setOperatorListeners() {
        Button btnPlus = findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClick("+");
            }
        });

        Button btnMinus = findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClick("-");
            }
        });

        Button btnMultiply = findViewById(R.id.btnMultiply);
        btnMultiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClick("*");
            }
        });
        Button btnDivide = findViewById(R.id.btnDivide);
        btnDivide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOperatorClick("/");
            }
        });

    }

    private void setupSpecialButtons() {
        Button btnEquals = findViewById(R.id.btnEquals);
        btnEquals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEqualsClick();
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackspaceClick();
            }
        });
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onClearClick();
            }
        });
        Button btnPercent = findViewById(R.id.btnPercent);
        btnPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onPercentClick();
            }
        });
        Button btnPlusMinus = findViewById(R.id.btnPlusMinus);
        btnPlusMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlusMinusClick();
            }
        });


    }



    private void onNumberClick(String num) {
        if (errorState) return;

        if (newInput) {
            currentInput = num;
            newInput = false;
        } else {
            currentInput += num;
        }

        updateDisplay();
    }

    private void onOperatorClick(String op) {

        if (errorState) return;

        if (!currentInput.isEmpty()) {
            double num = Double.parseDouble(currentInput);
            numbers.add(num);
        }

        operators.add(op);

        currentInput = "";
        newInput = true;

        updateDisplay();
    }


    private void onEqualsClick() {

        if (!currentInput.isEmpty()) {
            numbers.add(Double.parseDouble(currentInput));
        }

        if (numbers.isEmpty()) return;

        double result = numbers.get(0);

        for (int i = 0; i < operators.size(); i++) {
            double nextNum = numbers.get(i + 1);
            String op = operators.get(i);

            switch (op) {
                case "+":
                    result += nextNum;
                    break;
                case "-":
                    result -= nextNum;
                    break;
                case "*":


                    result *= nextNum;
                    break;
                case "/":
                    if (nextNum == 0) {
                        Text_display.setText("Undefined");
                        clearLists();
                        currentInput = "";
                        errorState = true;
                        return;
                    }

                    result /= nextNum;
                    break;
            }
        }


        Text_display.setText(formatResult(result));
        clearLists();
        currentInput = formatResult(result);
        newInput = false;
    }

    private void clearLists() {
        numbers.clear();
        operators.clear();
    }



    private void onClearClick() {
        numbers.clear();
        operators.clear();
        currentInput = "";
        newInput = true;
        errorState = false;
        Text_display.setText("0");
    }


    private void onBackspaceClick() {
        if (errorState) return;

        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        }
        updateDisplay();
    }



    private void onPercentClick() {
        if (!currentInput.isEmpty()) {
            double value = Double.parseDouble(currentInput) / 100;
            currentInput = formatResult(value);
            Text_display.setText(currentInput);
            newInput = true;
        }
    }


    private void onPlusMinusClick() {
        String current = Text_display.getText().toString();

        if (current.equals("0")) return;

        if (current.startsWith("-")) {
            Text_display.setText(current.substring(1));
        } else {
            Text_display.setText("-" + current);
        }
    }


    private String formatResult(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.valueOf(value);
        }
    }
    private void updateDisplay() {
        StringBuilder expression = new StringBuilder();

        for (int i = 0; i < numbers.size(); i++) {
            expression.append(formatResult(numbers.get(i)));

            if (i < operators.size()) {
                expression.append(" ").append(operators.get(i)).append(" ");
            }
        }

        expression.append(currentInput);

        if (expression.length() == 0) {
            Text_display.setText("0");
        } else {
            Text_display.setText(expression.toString());
        }
    }





}
