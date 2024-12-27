package com.example.monitoring;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BerandaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_beranda);

            // Inisialisasi TextView lokasi
            TextView textViewLocation = findViewById(R.id.textViewLocation);

            // Ambil district ID dari Intent
            Intent intent = getIntent();
            if (intent != null) {
                String districtId = intent.getStringExtra("district_id");
                if (districtId != null && !districtId.isEmpty()) {
                    // Set nilai district ID ke TextView
                    textViewLocation.setText(districtId);
                }
            }




        // Inisialisasi TextView untuk feedback
        TextView feedbackTextView = findViewById(R.id.fdbck);

        // Menangani WindowInsets untuk elemen dengan id 'beranda'
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.beranda), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Menangani klik pada Button untuk kembali ke MainActivity
        findViewById(R.id.Buttonback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BerandaActivity.this, MainActivity.class));
            }
        });

        // Menangani klik pada Button untuk kembali ke MainActivity
        findViewById(R.id.monit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BerandaActivity.this, MonitoringActivity.class));
            }
        });

        // Menangani klik pada Button untuk membuka AboutUsActivity
        findViewById(R.id.abs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BerandaActivity.this, AboutusActivity.class));
            }
        });

        // Menangani klik pada TextView untuk mengirim feedback melalui WhatsApp
        feedbackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Subjek dan isi pesan feedback
                String message = "Terima kasih atas kerja kerasnya dalam mengembangkan fitur ini. Namun, saya menemukan beberapa hal yang perlu diperbaiki, seperti [sebutkan masalah atau bug]. Akan lebih baik jika [berikan saran atau solusi]. Saya berharap perbaikan ini bisa segera dilakukan agar pengalaman pengguna menjadi lebih baik. Terima kasih atas perhatian dan kerjasamanya!";

                // Nomor WhatsApp tujuan
                String phoneNumber = "+6282191370364";

                // Format URL untuk WhatsApp
                String url = "https://api.whatsapp.com/send/?phone=" + phoneNumber +
                        "&text=" + Uri.encode(message) +
                        "&type=phone_number&app_absent=0";

                try {
                    // Intent untuk membuka URL di Chrome
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage("com.android.chrome");

                    // Jalankan Intent
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        // Jika Chrome tidak tersedia, buka dengan browser lain
                        intent.setPackage(null);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(BerandaActivity.this, "Terjadi kesalahan saat membuka link", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
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
