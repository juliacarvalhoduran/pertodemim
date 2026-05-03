package com.app.pertodemim;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

// Tela para adicionar ou editar um endereço
public class AddressDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextInputEditText editLabel = findViewById(R.id.editAddressLabel);
        TextInputEditText editCEP = findViewById(R.id.editCEP);
        TextInputEditText editLogradouro = findViewById(R.id.editLogradouro);
        TextInputEditText editNumero = findViewById(R.id.editNumero);
        TextInputEditText editBairro = findViewById(R.id.editBairro);
        Button btnSave = findViewById(R.id.btnSaveAddress);

        // Verifica se é edição ou novo endereço
        String mode = getIntent().getStringExtra("mode");
        if ("edit".equals(mode)) {
            tvTitle.setText(R.string.editar_endereco_title);
            // Preenche com dados fictícios para simular edição
            editLabel.setText("Minha Casa");
            editCEP.setText("01234-567");
            editLogradouro.setText("Rua das Palmeiras");
            editNumero.setText("456");
            editBairro.setText("Jardim Paulista");
        } else {
            tvTitle.setText(R.string.novo_endereco_title);
        }

        // Botão voltar
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Botão salvar
        btnSave.setOnClickListener(v -> {
            String successMsg = "edit".equals(mode) ? "Endereço atualizado!" : "Endereço salvo com sucesso!";
            Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}