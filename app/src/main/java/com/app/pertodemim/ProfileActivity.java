package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

// Tela de Perfil do usuário logado
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da tela de perfil
        setContentView(R.layout.activity_profile);

        // Inicializa o botão de sair (Logout)
        MaterialButton btnSair = findViewById(R.id.btnSair);
        btnSair.setOnClickListener(v -> {
            // Volta para a tela de login limpando o histórico de telas abertas
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Configura a barra de navegação inferior
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        // Marca o item "Perfil" como o selecionado no momento
        bottomNavigation.setSelectedItemId(R.id.nav_perfil);

        // Define as ações ao clicar nos itens da barra de navegação
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_mapa) {
                // Abre a tela inicial (Mapa) e fecha a tela atual
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_chat) {
                // Abre a tela de Chat e fecha a tela atual
                startActivity(new Intent(this, ChatActivity.class));
                finish();
                return true;
            }
            return true;
        });
    }
}