package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// Tela de Chat onde os usuários podem conversar
public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define o layout da tela de chat
        setContentView(R.layout.activity_chat);

        // Inicializa a barra de navegação inferior
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigationChat);
        // Marca o item de chat como selecionado
        bottomNavigation.setSelectedItemId(R.id.nav_chat);

        // Define as ações ao clicar nos itens da barra de navegação
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_mapa) {
                // Abre a tela inicial (Mapa)
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_perfil) {
                // Abre a tela de Perfil
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_vitrine) {
                // Navegação para Vitrine (a implementar)
                return true;
            }
            return true;
        });
    }
}