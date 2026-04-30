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

public class VerifyCodeActivity extends AppCompatActivity {

    private TextView subtitleVerify;
    private TextInputLayout layoutCodigo;
    private TextInputEditText textCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_code);

        TextView tvVoltar = findViewById(R.id.tvVoltarVerify);
        subtitleVerify = findViewById(R.id.subtitleVerify);
        layoutCodigo = findViewById(R.id.layoutCodigo);
        textCodigo = findViewById(R.id.textCodigo);
        MaterialButton btVerificar = findViewById(R.id.btVerificar);
        TextView tvReenviar = findViewById(R.id.tvReenviar);

        // Limpa erro ao interagir
        textCodigo.setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layoutCodigo.setError(null); });
        textCodigo.setOnClickListener(v -> layoutCodigo.setError(null));

        tvVoltar.setOnClickListener(v -> finish());

        btVerificar.setOnClickListener(v -> {
            if (validateFields()) {
                startActivity(new Intent(this, NewPasswordActivity.class));
            }
        });

        tvReenviar.setOnClickListener(v -> {
            subtitleVerify.setText(getString(R.string.email_reenviado));
            subtitleVerify.setTextColor(ContextCompat.getColor(this, R.color.verde_petroleo_profundo));
        });
    }

    private boolean validateFields() {
        layoutCodigo.setError(null);
        String codigo = textCodigo.getText() != null ? textCodigo.getText().toString().trim() : "";
        if (TextUtils.isEmpty(codigo)) {
            layoutCodigo.setError(getString(R.string.error_required));
            return false;
        }
        return true;
    }
}