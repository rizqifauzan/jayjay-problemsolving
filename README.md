# Praktikum Java - Live Coding Problem Solving

Repo praktikum untuk live session mentoring QA di JayJay Edukasi.

Tema: **"Test yang hijau belum tentu test yang benar."**

Aplikasi yang diuji (AUT): <https://www.saucedemo.com>
Akun: `standard_user` / `secret_sauce`

---

## Setup peserta (3 langkah)

### 1. Pastikan Java 17 terpasang

```bash
java -version
```

Harus muncul `17` di awal, contoh:

```
openjdk version "17.0.11" 2024-04-16
```

Kalau belum ada, pasang JDK 17 dari
<https://adoptium.net/temurin/releases/?version=17>.

Gradle **tidak perlu** dipasang. Repo ini sudah membawa Gradle
wrapper.

### 2. Clone repo

```bash
git clone <url-repo-ini>
cd <nama-folder-repo>
```

### 3. Jalankan test

```bash
./gradlew test
```

Di Windows (Command Prompt / PowerShell):

```
gradlew.bat test
```

Selesai. Tidak ada langkah lain.

---

## Mode browser

Secara default browser berjalan **headless** (tidak terlihat),
supaya ringan di laptop peserta.

Untuk melihat browsernya bergerak:

```bash
./gradlew test -Dheadless=false
```

Mentor akan memakai mode ini saat live. Peserta dengan laptop
ringan sebaiknya tetap headless.

---

## Apa yang akan terjadi saat pertama dijalankan

Ini **normal dan disengaja**:

| Feature         | Hasil          | Kenapa                              |
|-----------------|----------------|-------------------------------------|
| `login.feature` | 1 skenario MERAH | Assertion tanpa pesan (Kasus 1)   |
| `cart.feature`  | Semua HIJAU    | Exception ditelan catch kosong (Kasus 2) |

Jangan diperbaiki dulu. Dua kondisi itu adalah bahan bedah saat
live session.

Laporan HTML Cucumber ada di:

```
build/reports/cucumber/report.html
```

---

## Kenapa ada WebDriverManager kalau Selenium sudah punya Selenium Manager?

Pertanyaan ini pasti muncul, jadi dijawab di sini.

**Selenium Manager** sudah menjadi bawaan Selenium sejak versi 4.6.
Ia otomatis mencari versi Chrome yang terpasang, mengunduh
chromedriver yang cocok, lalu memakainya. Artinya, di proyek ini
`WebDriverManager.chromedriver().setup()` sebenarnya **opsional** -
kalau baris itu dihapus, test tetap jalan.

Lalu kenapa tetap dipasang di praktikum ini? Tiga alasan:

1. **Banyak proyek nyata masih memakainya.** Ribuan repo Selenium
   dibuat sebelum 4.6 dan masih hidup sampai sekarang. Kalian akan
   bertemu kode seperti ini di tempat kerja, jadi harus kenal.
2. **Kontrolnya lebih detail.** WebDriverManager bisa diminta
   versi driver tertentu, browser tertentu, mirror unduhan
   tertentu, atau cache di folder tertentu. Berguna di jaringan
   kantor yang memblokir server Google, karena mirror internal
   bisa ditunjuk manual. Selenium Manager belum sefleksibel itu.
3. **Fitur tambahan.** WebDriverManager bisa menjalankan browser
   di dalam Docker dan merekam sesinya - sesuatu yang di luar
   cakupan Selenium Manager.

**Kapan cukup pakai Selenium Manager saja?** Untuk proyek baru,
Selenium 4.6 ke atas, jaringan tanpa pembatasan khusus. Itu kasus
mayoritas. Jangan tambahkan dependency yang tidak dibutuhkan.

**Kapan WebDriverManager masih berguna?** Saat jaringan kantor
memblokir unduhan driver dan kalian perlu menunjuk mirror sendiri,
saat harus mengunci versi driver secara ketat, atau saat mewarisi
proyek lama yang sudah memakainya.

---

## Struktur repo

```
build.gradle                 konfigurasi Gradle (Groovy DSL)
settings.gradle
gradle/wrapper/              Gradle wrapper, jangan dihapus
gradlew, gradlew.bat         perintah untuk menjalankan Gradle

src/test/java/com/jayjay/
    runner/TestRunner.java       titik masuk JUnit 5 + Cucumber
    driver/DriverFactory.java    pembuat WebDriver, atur headless
    pages/LoginPage.java         Page Object halaman login
    pages/InventoryPage.java     Page Object daftar produk
    pages/CartPage.java          Page Object keranjang
    steps/LoginSteps.java        step definition Kasus 1
    steps/CartSteps.java         step definition Kasus 2
    hooks/Hooks.java             buka dan tutup browser

src/test/resources/features/
    login.feature
    cart.feature
```

---

## Troubleshooting

### 1. Versi Chrome tidak cocok dengan chromedriver

Pesan yang muncul:

```
SessionNotCreatedException: session not created:
This version of ChromeDriver only supports Chrome version 126
Current browser version is 124.0.6367.207
```

Artinya driver dan browser beda versi.

**Perbaikan tercepat:** update Chrome ke versi terbaru, lalu
bersihkan cache driver.

```bash
# Mac / Linux
rm -rf ~/.cache/selenium ~/.m2/repository/webdriver

# Windows (PowerShell)
Remove-Item -Recurse -Force $env:USERPROFILE\.cache\selenium
```

Jalankan lagi `./gradlew test`. Driver akan diunduh ulang sesuai
versi Chrome yang sekarang.

**Kalau Chrome tidak boleh diupdate** (kebijakan kantor), tunjuk
chromedriver secara manual:

```bash
./gradlew test -DchromeDriverPath=/path/ke/chromedriver
```

Unduh chromedriver yang cocok dari
<https://googlechromelabs.github.io/chrome-for-testing/>.

### 2. `./gradlew: Permission denied` di Mac atau Linux

Pesan yang muncul:

```
bash: ./gradlew: Permission denied
```

File `gradlew` kehilangan bit executable. Perbaiki:

```bash
chmod +x gradlew
./gradlew test
```

Kalau ingin permanen di Git:

```bash
git update-index --chmod=+x gradlew
```

Di Windows masalah ini tidak ada, pakai `gradlew.bat`.

### 3. Proxy atau firewall kantor memblokir unduhan driver

Gejalanya bermacam-macam:

```
ConnectException: Connection timed out
UnknownHostException: storage.googleapis.com
403 Forbidden
```

Yang diblokir biasanya `storage.googleapis.com` (tempat
chromedriver disimpan) atau `repo1.maven.org` (tempat dependency
Gradle).

**Opsi A - beri tahu Gradle alamat proxy kantor.** Buat file
`gradle.properties` di root repo:

```properties
systemProp.https.proxyHost=proxy.kantor.co.id
systemProp.https.proxyPort=8080
systemProp.http.proxyHost=proxy.kantor.co.id
systemProp.http.proxyPort=8080
```

**Opsi B - siapkan chromedriver manual, lewati unduhan.** Unduh
chromedriver dari komputer yang punya akses internet, taruh di
laptop, lalu:

```bash
./gradlew test -DchromeDriverPath=/path/ke/chromedriver
```

**Opsi C - pakai hotspot HP** untuk sekali jalan pertama. Setelah
driver dan dependency masuk cache, jalan berikutnya tidak perlu
internet lagi.

### 4. Browser tidak muncul padahal sudah `-Dheadless=false`

Pastikan tanda hubungnya satu (`-D`, bukan `--D`) dan tidak ada
spasi setelah `-D`. Yang benar:

```bash
./gradlew test -Dheadless=false
```

### 5. Test lambat atau menggantung

Setiap skenario membuka browser baru. Kalau laptop berat, tutup
aplikasi lain dan pakai mode headless (default). Jangan tambahkan
`Thread.sleep` - proyek ini sengaja hanya memakai `WebDriverWait`.

---

## Dokumen lain di repo ini

- `RUNSHEET.md` - panduan jalannya sesi 60 menit (untuk mentor)
- `CHALLENGE.md` - tugas take-home untuk peserta
