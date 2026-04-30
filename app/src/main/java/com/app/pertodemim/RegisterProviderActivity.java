package com.app.pertodemim;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class RegisterProviderActivity extends AppCompatActivity {

    // Componentes da interface
    private TextInputLayout tilNomeFantasia, tilRazaoSocial, tilCNPJ, tilCategoria,
            tilDescricao, tilEmail, tilWhatsapp, tilCEP,
            tilLogradouro, tilNumero, tilBairro, tilCidade, tilEstado,
            tilDias, tilAbertura, tilFechamento, tilSenha, tilConfirmarSenha;

    private TextInputEditText editNomeFantasia, editRazaoSocial, editCNPJ,
            editDescricao, editEmail, editWhatsapp, editCEP,
            editLogradouro, editNumero, editBairro, editCidade, editEstado,
            editSenha, editConfirmarSenha;

    private AutoCompleteTextView editCategoria, editDias, editAbertura, editFechamento;

    // Persistência da seleção de dias
    private boolean[] selectedDays;
    private final ArrayList<Integer> dayList = new ArrayList<>();
    private String[] daysArray;

    // Persistência da seleção de categorias
    private int lastCheckedCategoryId = -1;
    private String lastOtherCategoryText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_provider);

        initViews(); // Inicializa os componentes
        setupClearErrorOnTouch(); // Limpa erros ao interagir

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        setupCategoriasDialog(); // Configura o seletor de categorias com memória
        setupDayPicker(); // Configura o seletor de dias com memória
        setupTimePickers(); // Configura os seletores de hora

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            if (validateFields()) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        tilNomeFantasia = findViewById(R.id.tilNomeFantasia);
        tilRazaoSocial = findViewById(R.id.tilRazaoSocial);
        tilCNPJ = findViewById(R.id.tilCNPJ);
        tilCategoria = findViewById(R.id.tilCategoria);
        tilDescricao = findViewById(R.id.tilDescricao);
        tilEmail = findViewById(R.id.tilEmail);
        tilWhatsapp = findViewById(R.id.tilWhatsapp);
        tilCEP = findViewById(R.id.tilCEP);
        tilLogradouro = findViewById(R.id.tilLogradouro);
        tilNumero = findViewById(R.id.tilNumero);
        tilBairro = findViewById(R.id.tilBairro);
        tilCidade = findViewById(R.id.tilCidade);
        tilEstado = findViewById(R.id.tilEstado);
        tilDias = findViewById(R.id.tilDias);
        tilAbertura = findViewById(R.id.tilAbertura);
        tilFechamento = findViewById(R.id.tilFechamento);
        tilSenha = findViewById(R.id.tilSenha);
        tilConfirmarSenha = findViewById(R.id.tilConfirmarSenha);

        editNomeFantasia = findViewById(R.id.editNomeFantasia);
        editRazaoSocial = findViewById(R.id.editRazaoSocial);
        editCNPJ = findViewById(R.id.editCNPJ);
        editDescricao = findViewById(R.id.editDescricao);
        editEmail = findViewById(R.id.editEmail);
        editWhatsapp = findViewById(R.id.editWhatsapp);
        editCEP = findViewById(R.id.editCEP);
        editLogradouro = findViewById(R.id.editLogradouro);
        editNumero = findViewById(R.id.editNumero);
        editBairro = findViewById(R.id.editBairro);
        editCidade = findViewById(R.id.editCidade);
        editEstado = findViewById(R.id.editEstado);
        editSenha = findViewById(R.id.editSenha);
        editConfirmarSenha = findViewById(R.id.editConfirmarSenha);

        editCategoria = findViewById(R.id.editCategoria);
        editDias = findViewById(R.id.editDias);
        editAbertura = findViewById(R.id.editAbertura);
        editFechamento = findViewById(R.id.editFechamento);

        daysArray = new String[]{
                getString(R.string.segunda), getString(R.string.terca), getString(R.string.quarta),
                getString(R.string.quinta), getString(R.string.sexta), getString(R.string.sabado),
                getString(R.string.domingo)
        };
        selectedDays = new boolean[daysArray.length];
    }

    private void setupClearErrorOnTouch() {
        View[] fields = {editNomeFantasia, editRazaoSocial, editCNPJ, editCategoria, 
                editDescricao, editEmail, editWhatsapp, editCEP, editLogradouro, editNumero, 
                editBairro, editCidade, editEstado, editDias, editAbertura, editFechamento, editSenha, editConfirmarSenha};
        TextInputLayout[] layouts = {tilNomeFantasia, tilRazaoSocial, tilCNPJ, tilCategoria, 
                tilDescricao, tilEmail, tilWhatsapp, tilCEP, tilLogradouro, tilNumero, 
                tilBairro, tilCidade, tilEstado, tilDias, tilAbertura, tilFechamento, tilSenha, tilConfirmarSenha};

        for (int i = 0; i < fields.length; i++) {
            final TextInputLayout layout = layouts[i];
            if (fields[i] != null) {
                fields[i].setOnFocusChangeListener((v, hasFocus) -> { if (hasFocus) layout.setError(null); });
                fields[i].setOnClickListener(v -> layout.setError(null));
            }
        }
    }

    private void setupCategoriasDialog() {
        View.OnClickListener listener = v -> {
            tilCategoria.setError(null);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_categorias, null);
            RadioGroup rg = view.findViewById(R.id.rgCategorias);
            EditText editOther = view.findViewById(R.id.editOutrosInput);

            // Re-aplica a seleção anterior ao abrir o menu
            if (lastCheckedCategoryId != -1) {
                rg.check(lastCheckedCategoryId);
                if (lastCheckedCategoryId == R.id.rbOutros) {
                    editOther.setVisibility(View.VISIBLE);
                    editOther.setText(lastOtherCategoryText);
                }
            }

            rg.setOnCheckedChangeListener((group, checkedId) -> {
                editOther.setVisibility(checkedId == R.id.rbOutros ? View.VISIBLE : View.GONE);
            });

            new AlertDialog.Builder(this)
                .setTitle(R.string.selecione_categoria)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    int selectedId = rg.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        lastCheckedCategoryId = selectedId; // Salva para a próxima vez
                        if (selectedId == R.id.rbOutros) {
                            lastOtherCategoryText = editOther.getText().toString();
                            editCategoria.setText(lastOtherCategoryText);
                        } else {
                            RadioButton rb = view.findViewById(selectedId);
                            editCategoria.setText(rb.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancelar", null).show();
        };
        editCategoria.setOnClickListener(listener);
        tilCategoria.setEndIconOnClickListener(listener);
    }

    private void setupDayPicker() {
        View.OnClickListener listener = v -> {
            tilDias.setError(null);
            new AlertDialog.Builder(this)
                .setTitle(R.string.selecione_dias)
                .setCancelable(false)
                .setMultiChoiceItems(daysArray, selectedDays, (dialog, which, isChecked) -> {
                    // O array 'selectedDays' já mantém o estado da seleção automaticamente
                    if (isChecked) {
                        if (!dayList.contains(which)) { dayList.add(which); Collections.sort(dayList); }
                    } else {
                        dayList.remove(Integer.valueOf(which));
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < dayList.size(); i++) {
                        sb.append(daysArray[dayList.get(i)]).append(i == dayList.size() - 1 ? "" : ", ");
                    }
                    editDias.setText(sb.toString());
                })
                .setNegativeButton("Cancelar", null)
                .setNeutralButton("Limpar", (dialog, which) -> {
                    Arrays.fill(selectedDays, false);
                    dayList.clear(); editDias.setText("");
                }).show();
        };
        editDias.setOnClickListener(listener);
        tilDias.setEndIconOnClickListener(listener);
    }

    private void setupTimePickers() {
        View.OnClickListener aperturaListener = v -> showTimePicker(editAbertura);
        editAbertura.setOnClickListener(aperturaListener);
        tilAbertura.setEndIconOnClickListener(aperturaListener);

        View.OnClickListener fechamentoListener = v -> showTimePicker(editFechamento);
        editFechamento.setOnClickListener(fechamentoListener);
        tilFechamento.setEndIconOnClickListener(fechamentoListener);
    }

    private void showTimePicker(AutoCompleteTextView et) {
        new TimePickerDialog(this, (view, h, m) -> {
            et.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m));
            if (et == editAbertura) tilAbertura.setError(null); else tilFechamento.setError(null);
        }, 12, 0, true).show();
    }

    private boolean validateFields() {
        boolean valid = true;
        resetErrors();

        if (isEmpty(editNomeFantasia)) { tilNomeFantasia.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editRazaoSocial)) { tilRazaoSocial.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editDescricao)) { tilDescricao.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editWhatsapp)) { tilWhatsapp.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editLogradouro)) { tilLogradouro.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editNumero)) { tilNumero.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editBairro)) { tilBairro.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editCidade)) { tilCidade.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editEstado)) { tilEstado.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editDias)) { tilDias.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editCategoria)) { tilCategoria.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editAbertura)) { tilAbertura.setError(getString(R.string.error_required)); valid = false; }
        if (isEmpty(editFechamento)) { tilFechamento.setError(getString(R.string.error_required)); valid = false; }

        if (!isEmpty(editAbertura) && !isEmpty(editFechamento)) {
            if (editAbertura.getText().toString().compareTo(editFechamento.getText().toString()) >= 0) {
                tilAbertura.setError(getString(R.string.error_opening_time));
                tilFechamento.setError(getString(R.string.error_closing_time));
                valid = false;
            }
        }

        String email = editEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) { tilEmail.setError(getString(R.string.error_required)); valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { tilEmail.setError(getString(R.string.error_invalid_email)); valid = false; }

        if (editCNPJ.getText().length() != 14) { tilCNPJ.setError(getString(R.string.error_invalid_cnpj)); valid = false; }
        if (editCEP.getText().length() != 8) { tilCEP.setError(getString(R.string.error_invalid_cep)); valid = false; }

        String s = editSenha.getText().toString(), c = editConfirmarSenha.getText().toString();
        if (s.isEmpty()) { tilSenha.setError(getString(R.string.error_required)); valid = false; }
        if (c.isEmpty()) { tilConfirmarSenha.setError(getString(R.string.error_required)); valid = false; }
        else if (!s.equals(c)) { tilSenha.setError(getString(R.string.error_password_mismatch)); tilConfirmarSenha.setError(getString(R.string.error_password_mismatch)); valid = false; }

        return valid;
    }

    private void resetErrors() {
        TextInputLayout[] layouts = {tilNomeFantasia, tilRazaoSocial, tilCNPJ, tilCategoria, 
                tilDescricao, tilEmail, tilWhatsapp, tilCEP, tilLogradouro, tilNumero, tilBairro, 
                tilCidade, tilEstado, tilDias, tilAbertura, tilFechamento, tilSenha, tilConfirmarSenha};
        for (TextInputLayout l : layouts) l.setError(null);
    }

    private boolean isEmpty(View v) {
        if (v instanceof EditText) return TextUtils.isEmpty(((EditText) v).getText());
        return true;
    }
}