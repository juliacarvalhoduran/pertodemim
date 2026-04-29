package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText textUsuario;
    private TextInputEditText textSenha;
    private Button btEntrar;
    private TextView textRedefinirSenha;
    private Button btCriarCliente;
    private Button btCriarFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 1. Inicializar os componentes
        textUsuario = findViewById(R.id.textUsuario);
        textSenha = findViewById(R.id.textSenha);
        btEntrar = findViewById(R.id.btEntrar);
        textRedefinirSenha = findViewById(R.id.textView2);
        btCriarCliente = findViewById(R.id.btCriarCliente);
        btCriarFornecedor = findViewById(R.id.btCriarFornecedor);

        // Clique para criar conta cliente
        btCriarCliente.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterClientActivity.class);
            startActivity(intent);
        });

        // Clique para criar conta fornecedor
        btCriarFornecedor.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterProviderActivity.class);
            startActivity(intent);
        });

        // Clique para redefinir senha
        textRedefinirSenha.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        // 2. Configurar o clique do botão
        btEntrar.setOnClickListener(v -> {
            // 3. Pegar as informações digitadas
            String usuario = textUsuario.getText().toString();
            String senha = textSenha.getText().toString();

            // Validar se os campos não estão vazios
            if (usuario.isEmpty() || senha.isEmpty()) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Atenção")
                        .setMessage("Preencha todos os campos para continuar!")
                        .setPositiveButton("Ok", null)
                        .show();
            } else {
                // Abrir a nova tela (HomeActivity)
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left + v.getPaddingLeft(), 
                         systemBars.top + v.getPaddingTop(), 
                         systemBars.right + v.getPaddingRight(), 
                         systemBars.bottom + v.getPaddingBottom());
            return insets;
        });
    }
}