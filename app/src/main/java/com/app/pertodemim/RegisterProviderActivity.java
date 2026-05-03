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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

// Tela para cadastro de Fornecedores (empresas ou prestadores de serviço)
public class RegisterProviderActivity extends AppCompatActivity {

    // Componentes de interface para dados da empresa, contato, endereço e horários
    private TextInputLayout tilNomeFantasia, tilRazaoSocial, tilCNPJ, tilCategoria,
            tilDescricao, tilEmail, tilWhatsapp, tilCEP,
            tilLogradouro, tilNumero, tilBairro, tilCidade, tilEstado,
            tilDias, tilAbertura, tilFechamento, tilSenha, tilConfirmarSenha;

    private TextInputEditText editNomeFantasia, editRazaoSocial, editCNPJ,
            editDescricao, editEmail, editWhatsapp, editCEP,
            editLogradouro, editNumero, editBairro, editCidade, editEstado,
            editSenha, editConfirmarSenha;

    private AutoCompleteTextView editCategoria, editDias, editAbertura, editFechamento;

    // Variáveis para guardar os dias da semana e categorias selecionadas
    private boolean[] selectedDays;
    private final ArrayList<Integer> dayList = new ArrayList<>();
    private String[] daysArray;
    private Set<String> selectedCategories = new HashSet<>();
    private String lastOtherCategoryText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da tela de cadastro de fornecedor
        setContentView(R.layout.activity_register_provider);

        initViews(); // Inicializa os componentes
        setupClearErrorOnTouch(); // Limpa avisos de erro ao interagir

        // Botão voltar
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Configura seletores especiais (Categorias, Dias e Horários)
        setupCategoriasDialog();
        setupDayPicker();
        setupTimePickers();

        // Ação do botão "Cadastrar"
        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(v -> {
            // Se as validações passarem, finaliza e vai para a Home
            if (validateFields()) {
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    // Liga as variáveis aos IDs definidos no layout XML
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

        // Prepara a lista de dias da semana para o seletor
        daysArray = new String[]{
                getString(R.string.segunda), getString(R.string.terca), getString(R.string.quarta),
                getString(R.string.quinta), getString(R.string.sexta), getString(R.string.sabado),
                getString(R.string.domingo)
        };
        selectedDays = new boolean[daysArray.length];
    }

    // Remove erros visuais quando o usuário interage com os campos
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

    // Configura o diálogo para selecionar as categorias de serviço
    private void setupCategoriasDialog() {
        View.OnClickListener listener = v -> {
            tilCategoria.setError(null);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_filtros_multi, null);
            LinearLayout container = view.findViewById(R.id.llOptionsContainer);
            
            String[] categorias = {
                "Beleza e Estética", "Saúde", "Alimentação", "Educação", "Manutenção", "Tecnologia"
            };
            
            List<CheckBox> checkBoxes = new ArrayList<>();
            for (String cat : categorias) {
                CheckBox cb = new CheckBox(this);
                cb.setText(cat);
                cb.setPadding(16, 16, 16, 16);
                cb.setChecked(selectedCategories.contains(cat));
                container.addView(cb);
                checkBoxes.add(cb);
            }

            // Opção "Outros" que permite digitar uma nova categoria
            CheckBox cbOutros = new CheckBox(this);
            cbOutros.setText("Outros");
            cbOutros.setPadding(16, 16, 16, 16);
            cbOutros.setChecked(selectedCategories.contains("Outros"));
            container.addView(cbOutros);
            
            EditText editOther = new EditText(this);
            editOther.setHint(R.string.adicionar_categoria);
            editOther.setText(lastOtherCategoryText);
            editOther.setTextSize(16);
            editOther.setVisibility(cbOutros.isChecked() ? View.VISIBLE : View.GONE);
            container.addView(editOther);
            
            cbOutros.setOnCheckedChangeListener((buttonView, isChecked) -> {
                editOther.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            });

            new AlertDialog.Builder(this)
                .setTitle(R.string.selecione_categoria)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    selectedCategories.clear();
                    StringBuilder sb = new StringBuilder();
                    
                    for (CheckBox cb : checkBoxes) {
                        if (cb.isChecked()) {
                            selectedCategories.add(cb.getText().toString());
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(cb.getText().toString());
                        }
                    }
                    
                    if (cbOutros.isChecked()) {
                        selectedCategories.add("Outros");
                        lastOtherCategoryText = editOther.getText().toString();
                        if (!lastOtherCategoryText.isEmpty()) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(lastOtherCategoryText);
                        }
                    }
                    editCategoria.setText(sb.toString());
                })
                .setNegativeButton("Cancelar", null).show();
        };
        editCategoria.setOnClickListener(listener);
        tilCategoria.setEndIconOnClickListener(listener);
    }

    // Configura o diálogo para selecionar os dias de funcionamento
    private void setupDayPicker() {
        View.OnClickListener listener = v -> {
            tilDias.setError(null);
            new AlertDialog.Builder(this)
                .setTitle(R.string.selecione_dias)
                .setCancelable(false)
                .setMultiChoiceItems(daysArray, selectedDays, (dialog, which, isChecked) -> {
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

    // Configura os seletores de horário (Relógio)
    private void setupTimePickers() {
        View.OnClickListener aperturaListener = v -> showTimePicker(editAbertura);
        editAbertura.setOnClickListener(aperturaListener);
        tilAbertura.setEndIconOnClickListener(aperturaListener);

        View.OnClickListener fechamentoListener = v -> showTimePicker(editFechamento);
        editFechamento.setOnClickListener(fechamentoListener);
        tilFechamento.setEndIconOnClickListener(fechamentoListener);
    }

    // Mostra o relógio para escolher hora e minuto
    private void showTimePicker(AutoCompleteTextView et) {
        new TimePickerDialog(this, (view, h, m) -> {
            et.setText(String.format(Locale.getDefault(), "%02d:%02d", h, m));
            if (et == editAbertura) tilAbertura.setError(null); else tilFechamento.setError(null);
        }, 12, 0, true).show();
    }

    // Valida se todos os campos obrigatórios foram preenchidos corretamente
    private boolean validateFields() {
        boolean valid = true;
        resetErrors();

        // Valida campos de texto simples
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

        // Valida se a hora de abertura é antes da hora de fechamento
        if (!isEmpty(editAbertura) && !isEmpty(editFechamento)) {
            if (editAbertura.getText().toString().compareTo(editFechamento.getText().toString()) >= 0) {
                tilAbertura.setError(getString(R.string.error_opening_time));
                tilFechamento.setError(getString(R.string.error_closing_time));
                valid = false;
            }
        }

        // Validação de e-mail, CNPJ (14 dígitos) e CEP (8 dígitos)
        String email = editEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) { tilEmail.setError(getString(R.string.error_required)); valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { tilEmail.setError(getString(R.string.error_invalid_email)); valid = false; }

        if (editCNPJ.getText().length() != 14) { tilCNPJ.setError(getString(R.string.error_invalid_cnpj)); valid = false; }
        if (editCEP.getText().length() != 8) { tilCEP.setError(getString(R.string.error_invalid_cep)); valid = false; }

        // Validação de senha
        String s = editSenha.getText().toString(), c = editConfirmarSenha.getText().toString();
        if (s.isEmpty()) { tilSenha.setError(getString(R.string.error_required)); valid = false; }
        if (c.isEmpty()) { tilConfirmarSenha.setError(getString(R.string.error_required)); valid = false; }
        else if (!s.equals(c)) { tilSenha.setError(getString(R.string.error_password_mismatch)); tilConfirmarSenha.setError(getString(R.string.error_password_mismatch)); valid = false; }

        return valid;
    }

    // Limpa todas as mensagens de erro visuais
    private void resetErrors() {
        TextInputLayout[] layouts = {tilNomeFantasia, tilRazaoSocial, tilCNPJ, tilCategoria, 
                tilDescricao, tilEmail, tilWhatsapp, tilCEP, tilLogradouro, tilNumero, tilBairro, 
                tilCidade, tilEstado, tilDias, tilAbertura, tilFechamento, tilSenha, tilConfirmarSenha};
        for (TextInputLayout l : layouts) l.setError(null);
    }

    // Verifica se um campo está vazio
    private boolean isEmpty(View v) {
        if (v instanceof EditText) return TextUtils.isEmpty(((EditText) v).getText());
        return true;
    }
}