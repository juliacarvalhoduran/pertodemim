package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.slider.RangeSlider;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Tela principal do aplicativo que exibe o mapa e filtros de busca
public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Componentes de filtro (Sliders de preço e distância)
    private RangeSlider sliderPreco, sliderDistancia;
    private EditText editMinPreco, editMaxPreco, editMinDistancia, editMaxDistancia;
    private TextView tvSelectedAvaliacao, tvSelectedCategoria;
    private View panelPreco, panelDistancia;
    private boolean isUpdating = false;

    // Listas para armazenar os filtros selecionados
    private List<String> selectedAvaliacoes = new ArrayList<>();
    private List<String> selectedCategorias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializa o mapa do Google
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Lógica para abrir e fechar o menu lateral de filtros
        View btnMenu = findViewById(R.id.btnMenu);
        View filtersContainer = findViewById(R.id.filtersContainer);
        View btnCloseFilters = findViewById(R.id.btnCloseFilters);

        btnMenu.setOnClickListener(v -> filtersContainer.setVisibility(View.VISIBLE));
        btnCloseFilters.setOnClickListener(v -> {
            filtersContainer.setVisibility(View.GONE);
            hideAllPanels();
        });

        // Painéis de filtro (Preço e Distância)
        panelPreco = findViewById(R.id.panelPreco);
        panelDistancia = findViewById(R.id.panelDistancia);

        // Alterna a exibição dos painéis ao clicar nos botões de filtro
        findViewById(R.id.btnFiltroPreco).setOnClickListener(v -> togglePanel(panelPreco));
        findViewById(R.id.btnFiltroDistancia).setOnClickListener(v -> togglePanel(panelDistancia));

        tvSelectedAvaliacao = findViewById(R.id.tvSelectedAvaliacao);
        tvSelectedCategoria = findViewById(R.id.tvSelectedCategoria);

        // Abre diálogo para selecionar múltiplas avaliações
        findViewById(R.id.btnFiltroAvaliacao).setOnClickListener(v -> showMultiSelectDialog("Avaliação", 
                new String[]{"1 estrela", "2 estrelas", "3 estrelas", "4 estrelas", "5 estrelas"}, 
                selectedAvaliacoes, tvSelectedAvaliacao));
        
        // Abre diálogo para selecionar múltiplas categorias
        findViewById(R.id.btnFiltroCategoria).setOnClickListener(v -> showMultiSelectDialog("Categoria", 
                new String[]{"Beleza e Estética", "Saúde", "Alimentação", "Educação", "Manutenção", "Tecnologia", "Outros"}, 
                selectedCategorias, tvSelectedCategoria));

        // Configura os sliders e campos de texto para preço e distância
        setupPriceFilter();
        setupDistanceFilter();

        // Configuração da barra de navegação inferior
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_mapa);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_perfil) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_chat) {
                startActivity(new Intent(this, ChatActivity.class));
                return true;
            }
            return true;
        });
    }

    // Mostra ou esconde um painel de filtro
    private void togglePanel(View panel) {
        if (panel.getVisibility() == View.VISIBLE) {
            panel.setVisibility(View.GONE);
        } else {
            hideAllPanels();
            panel.setVisibility(View.VISIBLE);
        }
    }

    // Esconde todos os painéis de filtro
    private void hideAllPanels() {
        panelPreco.setVisibility(View.GONE);
        panelDistancia.setVisibility(View.GONE);
    }

    // Cria um diálogo com lista de marcação (CheckBox) para filtros múltiplos
    private void showMultiSelectDialog(String title, String[] options, List<String> selectedList, TextView targetTextView) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_filtros_multi, null);
        LinearLayout container = view.findViewById(R.id.llOptionsContainer);
        List<CheckBox> checkBoxes = new ArrayList<>();

        for (String option : options) {
            CheckBox cb = new CheckBox(this);
            cb.setText(option);
            cb.setPadding(16, 16, 16, 16);
            cb.setChecked(selectedList.contains(option));
            container.addView(cb);
            checkBoxes.add(cb);
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
                    selectedList.clear();
                    for (CheckBox cb : checkBoxes) {
                        if (cb.isChecked()) {
                            selectedList.add(cb.getText().toString());
                        }
                    }
                    updateTargetTextView(selectedList, targetTextView);
                })
                .setNeutralButton("Limpar", (dialog, which) -> {
                    selectedList.clear();
                    updateTargetTextView(selectedList, targetTextView);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Atualiza o texto que mostra os filtros selecionados
    private void updateTargetTextView(List<String> selectedList, TextView tv) {
        if (selectedList.isEmpty()) {
            tv.setText(R.string.todas);
        } else if (selectedList.size() == 1) {
            tv.setText(selectedList.get(0));
        } else {
            tv.setText(selectedList.size() + " selecionadas");
        }
    }

    // Configura o comportamento do filtro de preço
    private void setupPriceFilter() {
        sliderPreco = findViewById(R.id.sliderPreco);
        editMinPreco = findViewById(R.id.editMinPreco);
        editMaxPreco = findViewById(R.id.editMaxPreco);

        // Atualiza os campos de texto quando o slider é movido
        sliderPreco.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                isUpdating = true;
                editMinPreco.setText(formatCurrency(slider.getValues().get(0)));
                editMaxPreco.setText(formatCurrency(slider.getValues().get(1)));
                isUpdating = false;
            }
        });

        View.OnFocusChangeListener focusListener = (v, hasFocus) -> {
            if (!hasFocus) applyPriceInputs();
        };

        TextView.OnEditorActionListener editorActionListener = (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                applyPriceInputs();
                return true;
            }
            return false;
        };

        editMinPreco.setOnFocusChangeListener(focusListener);
        editMaxPreco.setOnFocusChangeListener(focusListener);
        editMinPreco.setOnEditorActionListener(editorActionListener);
        editMaxPreco.setOnEditorActionListener(editorActionListener);

        editMinPreco.setText(formatCurrency(sliderPreco.getValues().get(0)));
        editMaxPreco.setText(formatCurrency(sliderPreco.getValues().get(1)));
    }

    // Aplica os valores digitados manualmente no filtro de preço
    private void applyPriceInputs() {
        if (isUpdating) return;
        float min = parseCurrency(editMinPreco.getText().toString());
        float max = parseCurrency(editMaxPreco.getText().toString());
        
        min = Math.max(0, Math.min(10000, min));
        max = Math.max(0, Math.min(10000, max));
        if (max < min) max = min;

        isUpdating = true;
        sliderPreco.setValues(min, max);
        editMinPreco.setText(formatCurrency(min));
        editMaxPreco.setText(formatCurrency(max));
        isUpdating = false;
    }

    // Formata o valor numérico para moeda real (R$)
    private String formatCurrency(float val) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(val);
    }

    // Converte texto de moeda para número decimal
    private float parseCurrency(String input) {
        if (input == null || input.isEmpty()) return 0;
        String clean = input.replaceAll("[^0-9,]", "").replace(",", ".");
        try {
            return clean.isEmpty() ? 0 : Float.parseFloat(clean);
        } catch (Exception e) { return 0; }
    }

    // Configura o comportamento do filtro de distância
    private void setupDistanceFilter() {
        sliderDistancia = findViewById(R.id.sliderDistancia);
        editMinDistancia = findViewById(R.id.editMinDistancia);
        editMaxDistancia = findViewById(R.id.editMaxDistancia);

        // Atualiza os campos de texto quando o slider de distância é movido
        sliderDistancia.addOnChangeListener((slider, value, fromUser) -> {
            if (fromUser) {
                isUpdating = true;
                editMinDistancia.setText(formatDistance(slider.getValues().get(0)));
                editMaxDistancia.setText(formatDistance(slider.getValues().get(1)));
                isUpdating = false;
            }
        });

        View.OnFocusChangeListener focusListener = (v, hasFocus) -> {
            if (!hasFocus) applyDistanceInputs();
        };

        TextView.OnEditorActionListener editorActionListener = (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                applyDistanceInputs();
                return true;
            }
            return false;
        };

        editMinDistancia.setOnFocusChangeListener(focusListener);
        editMaxDistancia.setOnFocusChangeListener(focusListener);
        editMinDistancia.setOnEditorActionListener(editorActionListener);
        editMaxDistancia.setOnEditorActionListener(editorActionListener);

        editMinDistancia.setText(formatDistance(sliderDistancia.getValues().get(0)));
        editMaxDistancia.setText(formatDistance(sliderDistancia.getValues().get(1)));
    }

    // Aplica os valores digitados manualmente no filtro de distância
    private void applyDistanceInputs() {
        if (isUpdating) return;
        float min = parseDistanceSmart(editMinDistancia.getText().toString());
        float max = parseDistanceSmart(editMaxDistancia.getText().toString());

        min = Math.max(0, Math.min(5, min));
        max = Math.max(0, Math.min(5, max));
        if (max < min) max = min;

        isUpdating = true;
        sliderDistancia.setValues(min, max);
        editMinDistancia.setText(formatDistance(min));
        editMaxDistancia.setText(formatDistance(max));
        isUpdating = false;
    }

    // Formata a distância para metros (m) ou quilômetros (km)
    private String formatDistance(float valueInKm) {
        if (valueInKm < 1.0f) {
            int meters = (int) (valueInKm * 1000);
            return meters + " m";
        } else {
            DecimalFormat df = new DecimalFormat("0.#", DecimalFormatSymbols.getInstance(Locale.getDefault()));
            return df.format(valueInKm) + " km";
        }
    }

    // Converte texto de distância para valor numérico, entendendo unidades
    private float parseDistanceSmart(String input) {
        if (input == null || input.isEmpty()) return 0;
        String lowInput = input.toLowerCase().trim();
        String clean = lowInput.replaceAll("[^0-9,.]", "").replace(",", ".");
        try {
            float val = clean.isEmpty() ? 0 : Float.parseFloat(clean);
            
            // Reconhece metros e variações
            if (lowInput.contains("metro") || (lowInput.contains("m") && !lowInput.contains("km"))) {
                return val / 1000f;
            }
            
            // Se for um número grande sem unidade (ex: 500), assume metros
            if (!lowInput.contains("k") && val >= 10) {
                return val / 1000f;
            }
            
            return val; // Assume km para o restante
        } catch (Exception e) { return 0; }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Chamado quando o mapa está pronto para ser usado
    }
}