package com.example.monitoring;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrivpolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_privpol);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.privpol), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Button ke Beranda Activity
        findViewById(R.id.Buttonback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivpolActivity.this, MainActivity.class));
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
