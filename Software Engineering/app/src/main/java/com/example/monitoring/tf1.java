package com.example.monitoring;

import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class tf1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mengatur tampilan fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_tf1);

        // Konfigurasi WebView untuk ESP32-CAM
        WebView webView = findViewById(R.id.webview_tf1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Aktifkan JavaScript
        webSettings.setSupportZoom(false); // Nonaktifkan zoom
        webSettings.setBuiltInZoomControls(false); // Sembunyikan zoom controls
        webSettings.setDisplayZoomControls(false); // Jangan tampilkan tombol zoom
        webSettings.setLoadWithOverviewMode(true); // Konten disesuaikan dengan layar
        webSettings.setUseWideViewPort(true); // Aktifkan viewport yang luas

        webView.setHorizontalScrollBarEnabled(false); // Nonaktifkan scroll horizontal
        webView.setVerticalScrollBarEnabled(false); // Nonaktifkan scroll vertikal
        webView.setScrollContainer(false);

        webView.setWebViewClient(new WebViewClient()); // Agar tetap di aplikasi
        webView.loadUrl("http://172.20.10.3/cam-hi.jpg?" + System.currentTimeMillis());

        // Timer untuk refresh setiap 2 detik
        final Handler handler = new Handler();
        final Runnable refreshRunnable = new Runnable() {
            @Override
            public void run() {
                // Memuat ulang gambar dalam WebView
                webView.loadUrl("http://172.20.10.3/cam-hi.jpg?" + System.currentTimeMillis());
                // Mengatur refresh setiap 2 detik
                handler.postDelayed(this, 2000);
            }
        };

        // Memulai pembaruan gambar
        handler.post(refreshRunnable);
    }
}
