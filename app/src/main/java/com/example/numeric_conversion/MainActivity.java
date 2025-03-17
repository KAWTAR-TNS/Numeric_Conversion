package com.example.numeric_conversion;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private boolean isDarkMode = false;
    private boolean isHistoryOpen = false;
    private ConstraintLayout mainLayout;
    private LinearLayout historyPanel;
    private ImageButton btnThemeToggle, btnHistory;
    private TextView titleText, fromLabel, toLabel, resultText;
    private EditText inputNumber;
    private Spinner fromSpinner, toSpinner;
    private Button convertButton;
    private ArrayList<String> historyList = new ArrayList<>();
    private ArrayAdapter<String> historyAdapter;
    private ListView historyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main);
        historyPanel = findViewById(R.id.historyPanel);
        btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnHistory = findViewById(R.id.btnHistory);
        titleText = findViewById(R.id.titleText);
        fromLabel = findViewById(R.id.textView);
        toLabel = findViewById(R.id.textView2);
        inputNumber = findViewById(R.id.inputNumber);
        fromSpinner = findViewById(R.id.spinnerFrom);
        toSpinner = findViewById(R.id.spinnerTo);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.conversionResult);
        historyListView = findViewById(R.id.historyList);

        historyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
        historyListView.setAdapter(historyAdapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] bases = {"Decimal", "Hexadecimal", "Binary", "Octal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bases);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(v -> performConversion());

        btnThemeToggle.setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            applyTheme();
        });

        btnHistory.setOnClickListener(v -> {
            isHistoryOpen = !isHistoryOpen;
            historyPanel.setVisibility(isHistoryOpen ? View.VISIBLE : View.GONE);
        });
        applyTheme();
        btnHistory.setOnClickListener(v -> {
            isHistoryOpen = !isHistoryOpen;
            historyPanel.setVisibility(isHistoryOpen ? View.VISIBLE : View.GONE);
        });

        mainLayout.setOnClickListener(v -> {
            if (isHistoryOpen) {
                historyPanel.setVisibility(View.GONE);
                isHistoryOpen = false;
            }
        });

    }

    private void applyTheme() {
        if (isDarkMode) {
            mainLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.black));
            titleText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            fromLabel.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            toLabel.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            inputNumber.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            inputNumber.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            inputNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.input_dark));
            convertButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_dark));
            convertButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            resultText.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            historyPanel.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
            btnThemeToggle.setImageResource(R.drawable.ic_light_mode);
        } else {
            mainLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.purple_triangle));
            titleText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            fromLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            toLabel.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            inputNumber.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            inputNumber.setHintTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
            inputNumber.setBackground(ContextCompat.getDrawable(this, R.drawable.input_light));
            convertButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_light));
            convertButton.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            resultText.setTextColor(ContextCompat.getColor(this, android.R.color.black));
            historyPanel.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
            btnThemeToggle.setImageResource(R.drawable.ic_dark_mode);
        }
    }

    private void performConversion() {
        String inputStr = inputNumber.getText().toString().trim();
        if (inputStr.isEmpty()) {
            resultText.setText("Please enter a number.");
            return;
        }

        String fromBase = fromSpinner.getSelectedItem().toString();
        String toBase = toSpinner.getSelectedItem().toString();

        try {
            int inputValue = Integer.parseInt(inputStr, getBase(fromBase));

            String result = Integer.toString(inputValue, getBase(toBase)).toUpperCase();

            resultText.setText("Result: " + result);

            String historyEntry = inputStr + " (" + fromBase + ") → " + result + " (" + toBase + ")";
            historyList.add(0, historyEntry);
            historyAdapter.notifyDataSetChanged(); // Update list without making it visible

            hideKeyboard();
        } catch (NumberFormatException e) {
            resultText.setText("Invalid input for the selected base.");
        }
    }


    private int getBase(String base) {
        switch (base) {
            case "Binary": return 2;
            case "Octal": return 8;
            case "Hexadecimal": return 16;
            default: return 10;
        }}

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
