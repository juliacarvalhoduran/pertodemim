package com.app.pertodemim;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class RegisterProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_provider);

        // Configurar botão voltar
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Configurar Categorias
        setupCategorias();

        // Configurar Time Pickers
        setupTimePickers();

        // Configurar Botão Cadastrar
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterProviderActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void setupCategorias() {
        String[] categorias = {
                "Beleza e Estética",
                "Saúde",
                "Alimentação",
                "Educação",
                "Manutenção",
                "Tecnologia",
                "Outros"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categorias
        );

        AutoCompleteTextView autoCompleteCategoria = findViewById(R.id.autoCompleteCategoria);
        autoCompleteCategoria.setAdapter(adapter);
    }

    private void setupTimePickers() {
        TextInputEditText editAbertura = findViewById(R.id.editAbertura);
        TextInputEditText editFechamento = findViewById(R.id.editFechamento);

        editAbertura.setOnClickListener(v -> showTimePicker(editAbertura));
        editFechamento.setOnClickListener(v -> showTimePicker(editFechamento));
    }

    private void showTimePicker(TextInputEditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    editText.setText(time);
                }, 12, 0, true);
        timePickerDialog.show();
    }
}