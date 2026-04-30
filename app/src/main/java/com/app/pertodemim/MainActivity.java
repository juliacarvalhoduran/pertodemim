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

public class MainActivity extends AppCompatActivity {

    // Componentes da interface
    private TextInputEditText textUsuario, textSenha;
    private TextInputLayout layoutUsuario, layoutSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Ativa o modo tela cheia (desenha atrás das barras de sistema)
        setContentView(R.layout.activity_main);

        // Inicialização dos campos e layouts
        layoutUsuario = findViewById(R.id.layoutUsuario);
        layoutSenha = findViewById(R.id.layoutSenha);
        textUsuario = findViewById(R.id.textUsuario);
        textSenha = findViewById(R.id.textSenha);
        Button btEntrar = findViewById(R.id.btEntrar);
        TextView textRedefinirSenha = findViewById(R.id.textView2);
        Button btCriarCliente = findViewById(R.id.btCriarCliente);
        Button btCriarFornecedor = findViewById(R.id.btCriarFornecedor);

        // Configura para limpar a borda vermelha/erro quando o usuário clica nos campos
        setupClearErrorOnTouch();

        // Navegação entre as telas de cadastro e redefinição
        btCriarCliente.setOnClickListener(v -> startActivity(new Intent(this, RegisterClientActivity.class)));
        btCriarFornecedor.setOnClickListener(v -> startActivity(new Intent(this, RegisterProviderActivity.class)));
        textRedefinirSenha.setOnClickListener(v -> startActivity(new Intent(this, ResetPasswordActivity.class)));

        // Lógica do botão de entrar
        btEntrar.setOnClickListener(v -> {
            if (validateFields()) {
                // Se o e-mail e senha forem válidos, vai para a tela principal
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        // CORREÇÃO DO ENQUADRAMENTO E MARGENS:
        // Capturamos os paddings originais (24dp das laterais) definidos no XML antes de aplicar as barras
        View mainView = findViewById(R.id.main);
        int pL = mainView.getPaddingLeft();
        int pT = mainView.getPaddingTop();
        int pR = mainView.getPaddingRight();
        int pB = mainView.getPaddingBottom();

        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Somamos as barras de sistema às margens originais, mantendo o enquadramento correto
            v.setPadding(systemBars.left + pL, systemBars.top + pT, systemBars.right + pR, systemBars.bottom + pB);
            return insets;
        });
    }

    // Metodo que remove o erro visual assim que o campo ganha foco ou é clicado
    private void setupClearErrorOnTouch() {
        textUsuario.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutUsuario.setError(null); });
        textUsuario.setOnClickListener(v -> layoutUsuario.setError(null));
        textSenha.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutSenha.setError(null); });
        textSenha.setOnClickListener(v -> layoutSenha.setError(null));
    }

    // Validação estrita: o campo agora exige obrigatoriamente o formato de e-mail (com @ e domínio)
    private boolean validateFields() {
        boolean valid = true;
        layoutUsuario.setError(null); // Reseta erros anteriores
        layoutSenha.setError(null);

        String inputUsuario = textUsuario.getText() != null ? textUsuario.getText().toString().trim() : "";
        String inputSenha = textSenha.getText() != null ? textSenha.getText().toString() : "";

        // Validação de E-mail: verifica se está vazio ou se não segue o padrão de e-mail (arroba, ponto, etc)
        if (TextUtils.isEmpty(inputUsuario)) {
            layoutUsuario.setError(getString(R.string.error_required));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputUsuario).matches()) {
            layoutUsuario.setError(getString(R.string.error_invalid_email));
            valid = false;
        }

        // Validação de Senha: apenas verifica se foi preenchida
        if (TextUtils.isEmpty(inputSenha)) {
            layoutSenha.setError(getString(R.string.error_required));
            valid = false;
        }

        return valid;
    }
}