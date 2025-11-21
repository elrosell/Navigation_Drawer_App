package com.example.navigation_drawer_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalcuActActivity extends AppCompatActivity {

    private TextView display;
    private double currentValue = 0.0;
    private String pendingOperation = "";
    private boolean resetInput = true;
    private StringBuilder currentInput = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calcu_act);

        Toolbar toolbar = findViewById(R.id.simple_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.menu_calcu_act);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calcu_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        display = findViewById(R.id.calculator_display);
        setupNumericButtons();
        setupOperationButtons();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupNumericButtons() {
        int[] numericButtons = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_decimal
        };

        for (int id : numericButtons) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> appendNumber(((Button) v).getText().toString()));
        }
    }

    private void setupOperationButtons() {
        int[] operationButtons = {R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide};
        for (int id : operationButtons) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> handleOperation(((Button) v).getText().toString()));
        }

        Button clear = findViewById(R.id.btn_clear);
        clear.setOnClickListener(v -> clearAll());

        Button equals = findViewById(R.id.btn_equals);
        equals.setOnClickListener(v -> performEquals());
    }

    private void appendNumber(String value) {
        if (resetInput) {
            currentInput.setLength(0);
            resetInput = false;
        }

        if (value.equals(".") && currentInput.toString().contains(".")) {
            return;
        }

        currentInput.append(value);
        display.setText(currentInput.toString());
    }

    private void handleOperation(String operationSymbol) {
        double newValue = parseCurrentInput();
        if (!Double.isNaN(newValue)) {
            computePendingOperation(newValue);
        }
        pendingOperation = operationSymbol;
        resetInput = true;
    }

    private void performEquals() {
        double newValue = parseCurrentInput();
        if (!Double.isNaN(newValue)) {
            computePendingOperation(newValue);
            pendingOperation = "";
            resetInput = true;
            display.setText(formatNumber(currentValue));
        }
    }

    private void computePendingOperation(double newValue) {
        if (pendingOperation.isEmpty()) {
            currentValue = newValue;
            display.setText(formatNumber(currentValue));
            return;
        }

        switch (pendingOperation) {
            case "+":
                currentValue += newValue;
                break;
            case "-":
                currentValue -= newValue;
                break;
            case "×":
                currentValue *= newValue;
                break;
            case "÷":
                if (newValue == 0) {
                    display.setText(R.string.calculator_error);
                    resetInput = true;
                    pendingOperation = "";
                    currentInput.setLength(0);
                    currentValue = 0;
                    return;
                }
                currentValue /= newValue;
                break;
            default:
                currentValue = newValue;
                break;
        }

        display.setText(formatNumber(currentValue));
        currentInput.setLength(0);
    }

    private double parseCurrentInput() {
        if (currentInput.length() == 0) {
            return currentValue;
        }
        try {
            return Double.parseDouble(currentInput.toString());
        } catch (NumberFormatException e) {
            display.setText(R.string.calculator_error);
            resetInput = true;
            currentInput.setLength(0);
            return Double.NaN;
        }
    }

    private String formatNumber(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.format("%s", value);
        }
    }

    private void clearAll() {
        currentInput.setLength(0);
        currentValue = 0.0;
        pendingOperation = "";
        resetInput = true;
        display.setText(R.string.calculator_zero);
    }
}
