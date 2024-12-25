# Data
total_kendaraan = 690
deteksi_yolov3 = 350
deteksi_yolov8 = 610

# Fungsi untuk menghitung akurasi
def hitung_akurasi(deteksi, total):
    akurasi = (deteksi / total) * 100
    return akurasi

# Menghitung akurasi untuk YOLOv3 dan YOLOv8
akurasi_yolov3 = hitung_akurasi(deteksi_yolov3, total_kendaraan)
akurasi_yolov8 = hitung_akurasi(deteksi_yolov8, total_kendaraan)

# Menampilkan hasil
print(f"Akurasi YOLOv3: {akurasi_yolov3:.2f}%")
print(f"Akurasi YOLOv8: {akurasi_yolov8:.2f}%")
