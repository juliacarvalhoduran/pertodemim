package com.app.pertodemim;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView tvVoltar;
    private TextInputEditText textEmailReset;
    private MaterialButton btEnviarReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        tvVoltar = findViewById(R.id.tvVoltar);
        textEmailReset = findViewById(R.id.textEmailReset);
        btEnviarReset = findViewById(R.id.btEnviarReset);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainReset), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left + v.getPaddingLeft(),
                    systemBars.top + v.getPaddingTop(),
                    systemBars.right + v.getPaddingRight(),
                    systemBars.bottom + v.getPaddingBottom());
            return insets;
        });

        // Voltar para a tela anterior
        tvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Lógica de envio
        btEnviarReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = textEmailReset.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Digite seu e-mail!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Instruções enviadas para: " + email, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}