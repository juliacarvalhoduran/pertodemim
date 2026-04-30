package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class NewPasswordActivity extends AppCompatActivity {

    private TextInputLayout layoutNovaSenha, layoutConfirmaNovaSenha;
    private TextInputEditText textNovaSenha, textConfirmaNovaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_password);

        TextView tvVoltar = findViewById(R.id.tvVoltarNewPass);
        layoutNovaSenha = findViewById(R.id.layoutNovaSenha);
        layoutConfirmaNovaSenha = findViewById(R.id.layoutConfirmaNovaSenha);
        textNovaSenha = findViewById(R.id.textNovaSenha);
        textConfirmaNovaSenha = findViewById(R.id.textConfirmaNovaSenha);
        MaterialButton btAlterarSenha = findViewById(R.id.btAlterarSenha);

        setupClearErrorOnTouch();
        tvVoltar.setOnClickListener(v -> finish());

        btAlterarSenha.setOnClickListener(v -> {
            if (validateFields()) {
                Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void setupClearErrorOnTouch() {
        textNovaSenha.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutNovaSenha.setError(null); });
        textNovaSenha.setOnClickListener(v -> layoutNovaSenha.setError(null));
        textConfirmaNovaSenha.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutConfirmaNovaSenha.setError(null); });
        textConfirmaNovaSenha.setOnClickListener(v -> layoutConfirmaNovaSenha.setError(null));
    }

    private boolean validateFields() {
        boolean valid = true;
        layoutNovaSenha.setError(null);
        layoutConfirmaNovaSenha.setError(null);

        String senha = textNovaSenha.getText() != null ? textNovaSenha.getText().toString() : "";
        String confirma = textConfirmaNovaSenha.getText() != null ? textConfirmaNovaSenha.getText().toString() : "";

        if (TextUtils.isEmpty(senha)) {
            layoutNovaSenha.setError(getString(R.string.error_required));
            valid = false;
        }
        if (TextUtils.isEmpty(confirma)) {
            layoutConfirmaNovaSenha.setError(getString(R.string.error_required));
            valid = false;
        } else if (!Objects.equals(senha, confirma)) {
            layoutNovaSenha.setError(getString(R.string.error_password_mismatch));
            layoutConfirmaNovaSenha.setError(getString(R.string.error_password_mismatch));
            valid = false;
        }

        return valid;
    }
}