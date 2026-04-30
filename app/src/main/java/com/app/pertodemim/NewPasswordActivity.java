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

// Tela para cadastrar uma nova senha após a recuperação
public class NewPasswordActivity extends AppCompatActivity {

    // Campos para digitar e confirmar a nova senha
    private TextInputLayout layoutNovaSenha, layoutConfirmaNovaSenha;
    private TextInputEditText textNovaSenha, textConfirmaNovaSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ativa o modo de tela cheia
        EdgeToEdge.enable(this);
        // Define o layout da tela
        setContentView(R.layout.activity_new_password);

        // Inicializa os componentes da tela
        TextView tvVoltar = findViewById(R.id.tvVoltarNewPass);
        layoutNovaSenha = findViewById(R.id.layoutNovaSenha);
        layoutConfirmaNovaSenha = findViewById(R.id.layoutConfirmaNovaSenha);
        textNovaSenha = findViewById(R.id.textNovaSenha);
        textConfirmaNovaSenha = findViewById(R.id.textConfirmaNovaSenha);
        MaterialButton btAlterarSenha = findViewById(R.id.btAlterarSenha);

        // Limpa erros ao clicar nos campos
        setupClearErrorOnTouch();
        
        // Volta para a tela anterior ao clicar em "Voltar"
        tvVoltar.setOnClickListener(v -> finish());

        // Ação do botão "Alterar Senha"
        btAlterarSenha.setOnClickListener(v -> {
            if (validateFields()) {
                // Se as senhas forem iguais e válidas, mostra sucesso e volta para o login
                Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    // Função que remove a mensagem de erro quando o usuário interage com o campo
    private void setupClearErrorOnTouch() {
        textNovaSenha.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutNovaSenha.setError(null); });
        textNovaSenha.setOnClickListener(v -> layoutNovaSenha.setError(null));
        textConfirmaNovaSenha.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutConfirmaNovaSenha.setError(null); });
        textConfirmaNovaSenha.setOnClickListener(v -> layoutConfirmaNovaSenha.setError(null));
    }

    // Valida se as senhas foram preenchidas e se são idênticas
    private boolean validateFields() {
        boolean valid = true;
        layoutNovaSenha.setError(null);
        layoutConfirmaNovaSenha.setError(null);

        String senha = textNovaSenha.getText() != null ? textNovaSenha.getText().toString() : "";
        String confirma = textConfirmaNovaSenha.getText() != null ? textConfirmaNovaSenha.getText().toString() : "";

        // Verifica se os campos estão vazios
        if (TextUtils.isEmpty(senha)) {
            layoutNovaSenha.setError(getString(R.string.error_required));
            valid = false;
        }
        if (TextUtils.isEmpty(confirma)) {
            layoutConfirmaNovaSenha.setError(getString(R.string.error_required));
            valid = false;
        } else if (!Objects.equals(senha, confirma)) {
            // Verifica se a confirmação é igual à senha
            layoutNovaSenha.setError(getString(R.string.error_password_mismatch));
            layoutConfirmaNovaSenha.setError(getString(R.string.error_password_mismatch));
            valid = false;
        }

        return valid;
    }
}