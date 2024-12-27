package com.example.monitoring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aboutus);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.aboutus), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button ke Beranda Activity
        findViewById(R.id.Buttonback2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutusActivity.this, BerandaActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        // Abaikan fungsi back button
        super.onBackPressed(); // Jika lint mengharuskan
        // Namun tidak ada logika tambahan yang dijalankan
    }


}


