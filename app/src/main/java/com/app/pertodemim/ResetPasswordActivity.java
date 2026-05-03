package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Tela para solicitar a recuperação de senha via e-mail
public class ResetPasswordActivity extends AppCompatActivity {

    // Componentes para digitar o e-mail de recuperação
    private TextInputLayout layoutEmailReset;
    private TextInputEditText textEmailReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Ativa modo de tela cheia
        EdgeToEdge.enable(this);
        // Define o layout da tela
        setContentView(R.layout.activity_reset_password);

        // Inicializa os componentes da interface
        TextView tvVoltar = findViewById(R.id.tvVoltar);
        layoutEmailReset = findViewById(R.id.layoutEmailReset);
        textEmailReset = findViewById(R.id.textEmailReset);
        MaterialButton btEnviarReset = findViewById(R.id.btEnviarReset);

        // Limpa erros ao clicar ou focar no campo de e-mail
        textEmailReset.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutEmailReset.setError(null); });
        textEmailReset.setOnClickListener(v -> layoutEmailReset.setError(null));

        // Volta para a tela de login ao clicar em "Voltar"
        tvVoltar.setOnClickListener(v -> finish());

        // Ação do botão "Enviar": valida o e-mail e vai para a tela de verificação de código
        btEnviarReset.setOnClickListener(v -> {
            if (validateFields()) {
                startActivity(new Intent(this, VerifyCodeActivity.class));
            }
        });
    }

    // Valida se o e-mail foi preenchido e se tem o formato correto (@ e domínio)
    private boolean validateFields() {
        layoutEmailReset.setError(null);
        String email = textEmailReset.getText() != null ? textEmailReset.getText().toString().trim() : "";
        
        // Verifica se o campo está vazio
        if (TextUtils.isEmpty(email)) {
            layoutEmailReset.setError(getString(R.string.error_required));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // Verifica se o formato do e-mail é válido
            layoutEmailReset.setError(getString(R.string.error_invalid_email));
            return false;
        }
        return true;
    }
}