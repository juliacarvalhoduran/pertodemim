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

// Tela de verificação de código enviado por e-mail
public class VerifyCodeActivity extends AppCompatActivity {

    // Componentes da interface
    private TextView subtitleVerify;
    private TextInputLayout layoutCodigo;
    private TextInputEditText textCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configura a tela para ocupar toda a área (incluindo barras de sistema)
        EdgeToEdge.enable(this);
        // Define o layout da tela
        setContentView(R.layout.activity_verify_code);

        // Inicializa os componentes buscando pelo ID
        TextView tvVoltar = findViewById(R.id.tvVoltarVerify);
        subtitleVerify = findViewById(R.id.subtitleVerify);
        layoutCodigo = findViewById(R.id.layoutCodigo);
        textCodigo = findViewById(R.id.textCodigo);
        MaterialButton btVerificar = findViewById(R.id.btVerificar);
        TextView tvReenviar = findViewById(R.id.tvReenviar);

        // Remove a mensagem de erro quando o usuário começa a interagir com o campo de texto
        textCodigo.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutCodigo.setError(null); });
        textCodigo.setOnClickListener(v -> layoutCodigo.setError(null));

        // Volta para a tela anterior ao clicar no botão "Voltar"
        tvVoltar.setOnClickListener(v -> finish());

        // Ação do botão "Verificar": valida o campo e vai para a tela de nova senha
        btVerificar.setOnClickListener(v -> {
            if (validateFields()) {
                startActivity(new Intent(this, NewPasswordActivity.class));
            }
        });

        // Ação do texto "Reenviar": simula o reenvio alterando a mensagem e a cor
        tvReenviar.setOnClickListener(v -> {
            subtitleVerify.setText(getString(R.string.email_reenviado));
            subtitleVerify.setTextColor(ContextCompat.getColor(this, R.color.verde_petroleo_profundo));
        });
    }

    // Função que verifica se o código foi digitado corretamente
    private boolean validateFields() {
        layoutCodigo.setError(null); // Limpa erros
        String codigo = textCodigo.getText() != null ? textCodigo.getText().toString().trim() : "";
        
        // Se o campo estiver vazio, exibe uma mensagem de erro
        if (TextUtils.isEmpty(codigo)) {
            layoutCodigo.setError(getString(R.string.error_required));
            return false;
        }
        return true;
    }
}