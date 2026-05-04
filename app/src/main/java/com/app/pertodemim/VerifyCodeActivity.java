package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Tela de verificação de código enviado por e-mail para recuperação de senha
public class VerifyCodeActivity extends AppCompatActivity {

    // Componentes da interface: texto de instrução e campo de entrada do código
    private TextView subtitleVerify;
    private TextInputLayout layoutCodigo;
    private TextInputEditText textCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configura a tela para ocupar toda a área (modo imersivo)
        EdgeToEdge.enable(this);
        // Define o layout da tela de verificação
        setContentView(R.layout.activity_verify_code);

        // Inicializa os componentes buscando-os pelos IDs no XML
        TextView tvVoltar = findViewById(R.id.tvVoltarVerify);
        subtitleVerify = findViewById(R.id.subtitleVerify);
        layoutCodigo = findViewById(R.id.layoutCodigo);
        textCodigo = findViewById(R.id.textCodigo);
        MaterialButton btVerificar = findViewById(R.id.btVerificar);
        TextView tvReenviar = findViewById(R.id.tvReenviar);

        // Faz a mensagem de erro desaparecer quando o usuário começar a interagir com o campo
        textCodigo.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutCodigo.setError(null); });
        textCodigo.setOnClickListener(v -> layoutCodigo.setError(null));

        // Botão voltar: retorna para a tela anterior de digitar e-mail
        tvVoltar.setOnClickListener(v -> finish());

        // Ação do botão "Verificar": valida o campo e abre a tela de cadastrar nova senha
        btVerificar.setOnClickListener(v -> {
            if (validateFields()) {
                startActivity(new Intent(this, NewPasswordActivity.class));
            }
        });

        // Ação do texto "Reenviar": simula o reenvio alterando o texto da tela
        tvReenviar.setOnClickListener(v -> {
            subtitleVerify.setText(getString(R.string.email_reenviado));
            subtitleVerify.setTextColor(ContextCompat.getColor(this, R.color.verde_petroleo_profundo));
        });
    }

    // Função que verifica se o código obrigatório foi digitado
    private boolean validateFields() {
        layoutCodigo.setError(null); // Reseta erros visuais
        String codigo = textCodigo.getText() != null ? textCodigo.getText().toString().trim() : "";
        
        // Exibe erro se o campo estiver vazio
        if (TextUtils.isEmpty(codigo)) {
            layoutCodigo.setError(getString(R.string.error_required));
            return false;
        }
        return true;
    }
}