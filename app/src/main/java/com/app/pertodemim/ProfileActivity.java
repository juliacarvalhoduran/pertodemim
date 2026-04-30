package com.app.pertodemim;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inicializa o botão de sair
        MaterialButton btnSair = findViewById(R.id.btnSair);
        btnSair.setOnClickListener(v -> {
            // Volta para a tela de login limpando o histórico
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Configura a barra de navegação inferior
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_perfil); // Marca "Perfil" como selecionado

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_mapa) {
                // Volta para a tela do Mapa (HomeActivity)
                startActivity(new Intent(this, HomeActivity.class));
                finish(); // Fecha a tela de perfil
                return true;
            }
            return true; // Mantém na tela de perfil se clicar nela mesma
        });
    }
}