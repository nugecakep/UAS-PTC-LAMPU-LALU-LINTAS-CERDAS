package com.example.monitoring;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi View dari layout XML
        EditText districtIdInput = findViewById(R.id.district_id_input);
        EditText districtPasswordInput = findViewById(R.id.district_password_input);
        Button loginButton = findViewById(R.id.button);
        TextView requestText = findViewById(R.id.request);

        // Daftar ID dan Password valid
        String[] validIds = {"barru", "parepare", "pinrang"};
        String[] validPasswords = {"districtbarru", "districtparepare", "districtpinrang"};

        // Aksi pada tombol Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil input dari pengguna
                String inputId = districtIdInput.getText().toString().trim();
                String inputPassword = districtPasswordInput.getText().toString().trim();

                // Validasi input tidak boleh kosong
                if (TextUtils.isEmpty(inputId)) {
                    Toast.makeText(MainActivity.this, "District ID tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(inputPassword)) {
                    Toast.makeText(MainActivity.this, "District Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validasi ID dan Password
                boolean isValid = false;
                for (int i = 0; i < validIds.length; i++) {
                    if (validIds[i].equals(inputId) && validPasswords[i].equals(inputPassword)) {
                        isValid = true;
                        break;
                    }
                }

                if (isValid) {
                    // Login Berhasil, pindah ke BerandaActivity
                    Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                    // Kirim inputId ke BerandaActivity
                    Intent intent = new Intent(MainActivity.this, BerandaActivity.class);
                    intent.putExtra("district_id", inputId); // Kirimkan district ID
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, "District ID atau Password salah", Toast.LENGTH_SHORT).show();
                }

            }
        });

        findViewById(R.id.privacy_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PrivpolActivity.class));
            }
        });

        // Aksi pada Request District ID
        requestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+6282191370364";
                String message = "Subject: Permintaan District ID dan District Password\n\n" +
                        "Isi Pesan:\n" +
                        "Halo Tim Developer,\n\n" +
                        "Saya harap kabar Anda baik. Saya ingin meminta bantuan untuk memperoleh District ID dan District Password yang diperlukan untuk login ke sistem.\n\n" +
                        "Apabila ada prosedur atau dokumen yang perlu saya lengkapi, mohon informasinya agar dapat segera saya tindaklanjuti.\n\n" +
                        "Terima kasih atas bantuan dan kerja samanya. Saya tunggu konfirmasi dari Anda.";

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
                    Toast.makeText(MainActivity.this, "Terjadi kesalahan saat membuka link", Toast.LENGTH_SHORT).show();
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
