package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Tela de Login (entrada principal do aplicativo)
public class MainActivity extends AppCompatActivity {

    // Componentes para digitar e-mail e senha
    private TextInputEditText textUsuario, textSenha;
    private TextInputLayout layoutUsuario, layoutSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Permite que o app ocupe a tela inteira
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializa os campos da tela
        layoutUsuario = findViewById(R.id.layoutUsuario);
        layoutSenha = findViewById(R.id.layoutSenha);
        textUsuario = findViewById(R.id.textUsuario);
        textSenha = findViewById(R.id.textSenha);
        Button btEntrar = findViewById(R.id.btEntrar);
        TextView textRedefinirSenha = findViewById(R.id.textView2);
        Button btCriarCliente = findViewById(R.id.btCriarCliente);
        Button btCriarFornecedor = findViewById(R.id.btCriarFornecedor);

        // Limpa as mensagens de erro ao clicar nos campos
        setupClearErrorOnTouch();

        // Configura as ações de clique para abrir outras telas (Cadastro e Esqueci Senha)
        btCriarCliente.setOnClickListener(v -> startActivity(new Intent(this, RegisterClientActivity.class)));
        btCriarFornecedor.setOnClickListener(v -> startActivity(new Intent(this, RegisterProviderActivity.class)));
        textRedefinirSenha.setOnClickListener(v -> startActivity(new Intent(this, ResetPasswordActivity.class)));

        // Ação do botão "Entrar"
        btEntrar.setOnClickListener(v -> {
            if (validateFields()) {
                // Se tudo estiver certo, vai para a Home e fecha o login
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // Ajusta o preenchimento da tela para não ficar atrás das barras do sistema (bateria, relógio)
        View mainView = findViewById(R.id.main);
        int pL = mainView.getPaddingLeft();
        int pT = mainView.getPaddingTop();
        int pR = mainView.getPaddingRight();
        int pB = mainView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left + pL, systemBars.top + pT, systemBars.right + pR, systemBars.bottom + pB);
            return insets;
        });
    }

    // Função que remove o aviso de erro quando o usuário interage com o campo
    private void setupClearErrorOnTouch() {
        textUsuario.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutUsuario.setError(null); });
        textUsuario.setOnClickListener(v -> layoutUsuario.setError(null));
        textSenha.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutSenha.setError(null); });
        textSenha.setOnClickListener(v -> layoutSenha.setError(null));
    }

    // Valida se os campos foram preenchidos corretamente (e-mail válido e senha não vazia)
    private boolean validateFields() {
        boolean valid = true;
        layoutUsuario.setError(null);
        layoutSenha.setError(null);

        String inputUsuario = textUsuario.getText() != null ? textUsuario.getText().toString().trim() : "";
        String inputSenha = textSenha.getText() != null ? textSenha.getText().toString() : "";

        // Verifica se o e-mail é válido
        if (TextUtils.isEmpty(inputUsuario)) {
            layoutUsuario.setError(getString(R.string.error_required));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputUsuario).matches()) {
            layoutUsuario.setError(getString(R.string.error_invalid_email));
            valid = false;
        }

        // Verifica se a senha foi preenchida
        if (TextUtils.isEmpty(inputSenha)) {
            layoutSenha.setError(getString(R.string.error_required));
            valid = false;
        }

        return valid;
    }
}