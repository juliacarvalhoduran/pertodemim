package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterClientActivity extends AppCompatActivity {

    // Containers de erro e campos de texto
    private TextInputLayout tilNome, tilEmail, tilTelefone, tilCPF, tilNascimento,
            tilCEP, tilLogradouro, tilNumero, tilBairro, tilCidade, tilEstado,
            tilSenha, tilConfirmarSenha;
    private TextInputEditText editNome, editEmail, editTelefone, editCPF, editNascimento,
            editCEP, editLogradouro, editNumero, editBairro, editCidade, editEstado,
            editSenha, editConfirmarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        initViews(); // Inicializa componentes
        setupClearErrorOnTouch(); // Limpa erros ao interagir

        // Botão voltar
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Ação de cadastro
        MaterialButton btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            if (validateFields()) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        tilNome = findViewById(R.id.tilNome);
        tilEmail = findViewById(R.id.tilEmail);
        tilTelefone = findViewById(R.id.tilTelefone);
        tilCPF = findViewById(R.id.tilCPF);
        tilNascimento = findViewById(R.id.tilNascimento);
        tilCEP = findViewById(R.id.tilCEP);
        tilLogradouro = findViewById(R.id.tilLogradouro);
        tilNumero = findViewById(R.id.tilNumero);
        tilBairro = findViewById(R.id.tilBairro);
        tilCidade = findViewById(R.id.tilCidade);
        tilEstado = findViewById(R.id.tilEstado);
        tilSenha = findViewById(R.id.tilSenha);
        tilConfirmarSenha = findViewById(R.id.tilConfirmarSenha);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editTelefone = findViewById(R.id.editTelefone);
        editCPF = findViewById(R.id.editCPF);
        editNascimento = findViewById(R.id.editNascimento);
        editCEP = findViewById(R.id.editCEP);
        editLogradouro = findViewById(R.id.editLogradouro);
        editNumero = findViewById(R.id.editNumero);
        editBairro = findViewById(R.id.editBairro);
        editCidade = findViewById(R.id.editCidade);
        editEstado = findViewById(R.id.editEstado);
        editSenha = findViewById(R.id.editSenha);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha);
    }

    private void setupClearErrorOnTouch() {
        TextInputEditText[] fields = {editNome, editEmail, editTelefone, editCPF, editNascimento, editCEP, 
                editLogradouro, editNumero, editBairro, editCidade, editEstado, editSenha, editConfirmarSenha};
        TextInputLayout[] layouts = {tilNome, tilEmail, tilTelefone, tilCPF, tilNascimento, tilCEP, 
                tilLogradouro, tilNumero, tilBairro, tilCidade, tilEstado, tilSenha, tilConfirmarSenha};

        for (int i = 0; i < fields.length; i++) {
            final TextInputLayout layout = layouts[i];
            fields[i].setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layout.setError(null); });
            fields[i].setOnClickListener(v -> layout.setError(null));
        }
    }

    private boolean validateFields() {
        boolean valid = true;
        resetErrors();

        if (isEmpty(editNome)) { tilNome.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editTelefone)) { tilTelefone.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editNascimento)) { tilNascimento.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editLogradouro)) { tilLogradouro.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editNumero)) { tilNumero.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editBairro)) { tilBairro.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editCidade)) { tilCidade.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editEstado)) { tilEstado.setError(getString(R.string.error_required)); valid = false; }

        String email = editEmail.getText() != null ? editEmail.getText().toString().trim() : "";
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError(getString(R.string.error_required));
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        }

        String cpf = editCPF.getText() != null ? editCPF.getText().toString().trim() : "";
        if (TextUtils.isEmpty(cpf)) {
            tilCPF.setError(getString(R.string.error_required));
            valid = false;
        } else if (cpf.length() != 11) {
            tilCPF.setError(getString(R.string.error_invalid_cpf));
            valid = false;
        }

        String cep = editCEP.getText() != null ? editCEP.getText().toString().trim() : "";
        if (TextUtils.isEmpty(cep)) {
            tilCEP.setError(getString(R.string.error_required));
            valid = false;
        } else if (cep.length() != 8) {
            tilCEP.setError(getString(R.string.error_invalid_cep));
            valid = false;
        }

        String senha = editSenha.getText() != null ? editSenha.getText().toString() : "";
        String confirmacao = editConfirmarSenha.getText() != null ? editConfirmarSenha.getText().toString() : "";

        if (TextUtils.isEmpty(senha)) {
            tilSenha.setError(getString(R.string.error_required));
            valid = false;
        }
        if (TextUtils.isEmpty(confirmacao)) {
            tilConfirmarSenha.setError(getString(R.string.error_required));
            valid = false;
        } else if (!Objects.equals(senha, confirmacao)) {
            tilSenha.setError(getString(R.string.error_password_mismatch));
            tilConfirmarSenha.setError(getString(R.string.error_password_mismatch));
            valid = false;
        }

        return valid;
    }

    private void resetErrors() {
        tilNome.setError(null); tilEmail.setError(null); tilTelefone.setError(null);
        tilCPF.setError(null); tilNascimento.setError(null); tilCEP.setError(null);
        tilLogradouro.setError(null); tilNumero.setError(null); tilBairro.setError(null);
        tilCidade.setError(null); tilEstado.setError(null); tilSenha.setError(null);
        tilConfirmarSenha.setError(null);
    }

    private boolean isEmpty(TextInputEditText et) {
        return et.getText() == null || TextUtils.isEmpty(et.getText().toString().trim());
    }
}