# RUNSHEET - Praktikum Java: Live Coding Problem Solving

Panduan mentor untuk sesi 60 menit.
Tema: **"Test yang hijau belum tentu test yang benar."**

Dokumen ini memuat versi AFTER dari seluruh kode. Repo sendiri
hanya berisi versi BEFORE, supaya peserta tidak bisa mengintip
jawabannya lebih dulu. **Jangan bagikan file ini sebelum sesi
selesai.**

---

## Persiapan sebelum peserta masuk (10 menit sebelum mulai)

Lakukan ini di luar jam sesi, jangan dihitung dalam 60 menit.

```bash
git clone <url-repo>
cd <nama-folder-repo>
./gradlew test -Dheadless=false
```

Kenapa dijalankan lebih dulu? Supaya Gradle mengunduh seluruh
dependency dan chromedriver ke cache. Kalau tidak, unduhan pertama
memakan 2-3 menit di depan peserta dan layar hanya menampilkan
progress bar.

Checklist:

- [ ] Dependency dan chromedriver sudah masuk cache
- [ ] `login.feature` MERAH 1 skenario, `cart.feature` HIJAU semua
- [ ] Font editor diperbesar, minimal 16pt
- [ ] Terminal dan editor tampil bersamaan di layar
- [ ] `git stash` atau `git checkout .` siap, untuk mengembalikan
      kode ke versi BEFORE kalau sesi diulang

---

## Tabel alur 60 menit

| Menit | Durasi | Blok | Aktivitas |
|-------|--------|------|-----------|
| 00-04 | 4' | Pembuka | Tema sesi, aturan main, orientasi struktur repo |
| 04-09 | 5' | Baseline | Jalankan suite, lihat merah dan hijau apa adanya |
| 09-13 | 4' | Bedah 1 | Baca output kegagalan login, tanya "apa yang salah?" |
| 13-24 | 11' | **Refactor 1** | Tulis ulang assertion jadi informatif (AssertJ) |
| 24-29 | 5' | Bukti 1 | Jalankan ulang, pesan gagal kini menjelaskan sendiri |
| 29-33 | 4' | Tutup 1 | Perbaiki ekspektasi di feature, login jadi hijau jujur |
| 33-36 | 3' | Jeda | Tanya jawab, regangkan badan |
| 36-42 | 6' | Bedah 2 | `cart.feature` hijau - buktikan bahwa itu bohong |
| 42-53 | 11' | **Refactor 2** | Buang catch kosong, tangani exception dengan benar |
| 53-57 | 4' | Bukti 2 | Merah jujur muncul, perbaiki locator, hijau sungguhan |
| 57-60 | 3' | Penutup | Rangkuman 3 kalimat, bagikan `CHALLENGE.md` |

Catatan waktu: blok refactor dihitung dengan asumsi mentor
**mengetik sambil menjelaskan**, bukan menyalin-tempel. Mengetik
ulang satu method ~15 baris sambil bicara memakan 4-6 menit.
Sisanya untuk pertanyaan peserta yang pasti muncul di tengah.

---

# BLOK 00-04 - Pembuka (4 menit)

## Yang Anda katakan

> "Hari ini kita tidak belajar menulis test baru. Kita belajar
> membaca test yang sudah ada, dan mencurigainya.
>
> Satu kalimat yang saya mau kalian bawa pulang: **test yang hijau
> belum tentu test yang benar.** Hijau itu artinya kode selesai
> berjalan tanpa protes. Hijau tidak otomatis berarti aplikasi
> kalian benar.
>
> Aturan main: ketik ulang di komputer masing-masing, jangan
> salin-tempel. Kalau tertinggal, bilang, saya tunggu. Kalau error
> beda dengan layar saya, itu bagus, kita bahas."

## Perintah terminal

```bash
ls
```

Tunjukkan struktur singkat: `src/test/java/com/jayjay/` berisi
`pages`, `steps`, `hooks`, `driver`, `runner`. Feature file di
`src/test/resources/features/`.

Jangan jelaskan Page Object panjang lebar di sini. Cukup:

> "Page Object itu tempat menyimpan locator. Step definition itu
> tempat menyimpan logika test. Hari ini kita hanya menyentuh step
> definition."

---

# BLOK 04-09 - Baseline (5 menit)

## Perintah terminal

```bash
./gradlew test -Dheadless=false
```

## Yang Anda katakan sambil menunggu

> "Browser akan muncul karena saya pakai `-Dheadless=false`. Kalau
> laptop kalian berat, buang saja bagian itu, defaultnya headless.
>
> Sambil menunggu: menurut kalian, dari empat skenario ini, berapa
> yang lolos?"

Biarkan mereka menebak. Ini penting - tebakan mereka akan
dibandingkan dengan kenyataan sebentar lagi.

## Output yang muncul di layar peserta

```
Feature: Keranjang belanja SauceDemo

  Scenario: Menambah satu produk ke keranjang       PASSED
  Scenario: Menambah dua produk ke keranjang        PASSED

Feature: Login SauceDemo

  Scenario: Login berhasil dengan standard_user     PASSED
  Scenario: Login gagal dengan user yang terkunci   FAILED

Failed scenarios:
classpath:features/login.feature:14 # Login gagal dengan user
                                      yang terkunci

4 Scenarios (1 failed, 3 passed)
13 Steps (1 failed, 12 passed)
```

## Yang Anda katakan

> "Tiga hijau, satu merah. Sekarang perhatikan baik-baik: yang
> merah ini justru test yang paling jujur hari ini. Dia berteriak.
> Yang hijau, dua di antaranya sedang membohongi kita - tapi itu
> nanti."

---

# BLOK 09-13 - Bedah Kasus 1 (4 menit)

## Perintah terminal

Buka detail kegagalannya:

```bash
./gradlew test --tests '*TestRunner*' --info 2>&1 | grep -A 12 "FAILED"
```

Atau lebih mudah, buka laporan HTML:

```bash
open build/reports/cucumber/report.html      # Mac
xdg-open build/reports/cucumber/report.html  # Linux
start build/reports/cucumber/report.html     # Windows
```

## Output kegagalan yang mereka lihat

```
org.opentest4j.AssertionFailedError:
expected: <true> but was: <false>
    at com.jayjay.steps.LoginSteps.pesanErrorMemuatTeks(
        LoginSteps.java:46)
```

## Yang Anda katakan

> "Ini seluruh informasi yang test berikan kepada kita.
>
> `expected: true but was: false`.
>
> Coba jujur - dari kalimat ini, kalian tahu apa? Kalian tahu ada
> sesuatu yang seharusnya benar ternyata salah. Titik. Kalian
> tidak tahu:
>
> - Pesan error apa yang sebenarnya muncul di layar
> - Pesan apa yang kita harapkan
> - Apakah ini bug aplikasi, atau test-nya yang salah tulis
>
> Bayangkan ini gagal jam 2 pagi di CI, dan kalian yang piket.
> Kalian harus buka kode, baca ulang, jalankan manual. Lima belas
> menit hilang hanya untuk tahu apa yang terjadi.
>
> Ini bukan salah JUnit. Ini salah kita yang menulis assertion
> tanpa memberi konteks. Mari kita perbaiki."

Buka file bersama-sama:

```bash
code src/test/java/com/jayjay/steps/LoginSteps.java
```

---

# BLOK 13-24 - REFACTOR 1: Assertion informatif (11 menit)

## File yang diubah

`src/test/java/com/jayjay/steps/LoginSteps.java`

### Ubahan 1 - baris 1-13, ganti import

**BEFORE (baris 3):**

```java
import static org.junit.jupiter.api.Assertions.assertTrue;
```

**AFTER:**

```java
import static org.assertj.core.api.Assertions.assertThat;
```

### Yang Anda katakan sambil mengetik

> "Kita ganti JUnit assertion dengan AssertJ. Bukan karena JUnit
> jelek - JUnit juga punya versi berpesan. Tapi AssertJ memaksa
> kita menulis assertion yang menjelaskan dirinya sendiri.
>
> Perhatikan pola bacanya: `assertThat(yangSebenarnya)` lalu
> `.isEqualTo(yangDiharapkan)`. Dibaca seperti kalimat Inggris
> biasa: 'pastikan bahwa X sama dengan Y'."

### Ubahan 2 - baris 38-41, method `halamanDaftarProdukTampil`

**BEFORE:**

```java
    @Then("halaman daftar produk tampil")
    public void halamanDaftarProdukTampil() {
        assertTrue(inventoryPage.apakahHalamanProdukTampil());
    }
```

**AFTER:**

```java
    @Then("halaman daftar produk tampil")
    public void halamanDaftarProdukTampil() {
        boolean halamanProdukTampil =
                inventoryPage.apakahHalamanProdukTampil();

        assertThat(halamanProdukTampil)
                .as("Halaman daftar produk seharusnya tampil "
                        + "setelah login. URL saat ini: %s",
                        webDriver.getCurrentUrl())
                .isTrue();
    }
```

### Yang Anda katakan

> "Tiga hal yang saya lakukan di sini, satu per satu.
>
> **Satu**, saya keluarkan hasil pemanggilan ke variabel bernama
> `halamanProdukTampil`. Kenapa? Karena nanti saat gagal, saya
> ingin bisa berhenti di baris ini pakai debugger dan melihat
> isinya. Kalau semuanya ditumpuk dalam satu baris, tidak ada yang
> bisa diperiksa.
>
> **Dua**, `.as(...)` - ini deskripsi yang muncul saat gagal.
> Isinya bukan 'assertion failed', tapi kalimat yang menjelaskan
> apa yang seharusnya terjadi.
>
> **Tiga**, saya sertakan `getCurrentUrl()`. Ini kunci. Pesan
> gagal yang baik tidak hanya bilang 'salah', tapi juga
> menunjukkan **keadaan sistem saat gagal**. Kalau login ternyata
> nyangkut di halaman login, URL akan menunjukkannya."

### Ubahan 3 - baris 43-47, method `pesanErrorMemuatTeks`

Ini bagian terpenting sesi. Kerjakan pelan-pelan.

**BEFORE:**

```java
    @Then("pesan error memuat teks {string}")
    public void pesanErrorMemuatTeks(String teksYangDiharapkan) {
        String pesanErrorSebenarnya = loginPage.ambilPesanError();
        assertTrue(pesanErrorSebenarnya.contains(teksYangDiharapkan));
    }
```

**AFTER:**

```java
    @Then("pesan error memuat teks {string}")
    public void pesanErrorMemuatTeks(String teksYangDiharapkan) {
        String pesanErrorSebenarnya = loginPage.ambilPesanError();

        assertThat(pesanErrorSebenarnya)
                .as("Pesan error yang tampil di halaman login "
                        + "tidak sesuai harapan")
                .contains(teksYangDiharapkan);
    }
```

### Yang Anda katakan

> "Perhatikan: saya tidak lagi menghitung `contains` sendiri lalu
> menyerahkan hasil boolean ke assertion. Saya serahkan **teks
> aslinya** ke AssertJ, dan biarkan AssertJ yang memeriksa.
>
> Ini perbedaan besar. Kalau kita yang menghitung, AssertJ hanya
> menerima `true` atau `false` - dia sudah kehilangan informasi.
> Kalau AssertJ yang menghitung, dia masih memegang teks aslinya,
> jadi dia bisa menampilkannya saat gagal.
>
> Aturan praktisnya: **jangan pernah memasak data sebelum
> diserahkan ke assertion.** Serahkan bahan mentahnya."

Pertanyaan yang biasanya muncul di titik ini - siapkan jawabannya:

- *"Kalau pakai JUnit saja bisa tidak?"*
  Bisa: `assertTrue(kondisi, "pesan")`. Tapi kalian harus menulis
  sendiri nilai sebenarnya ke dalam pesan itu, dan orang biasanya
  lupa. AssertJ melakukannya otomatis.
- *"Apakah `.as()` wajib?"*
  Tidak. Tanpa `.as()`, AssertJ sudah menampilkan nilai aktual dan
  ekspektasi. `.as()` menambahkan **niat** kita - kenapa hal ini
  diperiksa.

---

# BLOK 24-29 - Bukti Kasus 1 (5 menit)

## Perintah terminal

```bash
./gradlew test
```

Kali ini tanpa `-Dheadless=false`, supaya lebih cepat.

## Output yang muncul di layar peserta

```
org.opentest4j.AssertionFailedError:
[Pesan error yang tampil di halaman login tidak sesuai harapan]
Expecting actual:
  "Epic sadface: Sorry, this user has been locked out."
to contain:
  "Username and password do not match"

    at com.jayjay.steps.LoginSteps.pesanErrorMemuatTeks(
        LoginSteps.java:46)
```

## Yang Anda katakan

> "Berhenti sebentar. Lihat baik-baik.
>
> Test ini **masih merah**. Satu skenario gagal, sama seperti tadi.
> Tidak ada satu pun bug aplikasi yang saya perbaiki barusan.
>
> Tapi sekarang saya tahu persis apa yang terjadi: aplikasi
> berkata 'this user has been locked out', sedangkan test
> mengharapkan 'username and password do not match'.
>
> Dan sekarang muncul pertanyaan yang benar - yang tadi tidak
> mungkin kita ajukan: **siapa yang salah di sini, aplikasinya
> atau testnya?**
>
> Jawabannya: testnya. `locked_out_user` memang user yang sengaja
> dikunci oleh SauceDemo. Pesan aplikasi sudah benar. Ekspektasi
> kita yang keliru sejak awal.
>
> Inilah nilai assertion informatif. Waktu diagnosis tadi lima
> belas menit. Sekarang lima belas detik. Kodenya tidak lebih
> pintar - dia hanya lebih jujur bercerita."

---

# BLOK 29-33 - Menutup Kasus 1 (4 menit)

## File yang diubah

`src/test/resources/features/login.feature`, baris 17

**BEFORE:**

```gherkin
    Then pesan error memuat teks "Username and password do not match"
```

**AFTER:**

```gherkin
    Then pesan error memuat teks "this user has been locked out"
```

## Perintah terminal

```bash
./gradlew test
```

## Output yang muncul

```
Feature: Login SauceDemo

  Scenario: Login berhasil dengan standard_user     PASSED
  Scenario: Login gagal dengan user yang terkunci   PASSED

4 Scenarios (4 passed)
13 Steps (13 passed)

BUILD SUCCESSFUL
```

## Yang Anda katakan

> "Hijau. Tapi hijau yang berbeda dari hijau di awal sesi.
>
> Hijau ini kita dapat setelah membaca pesan gagal, memahaminya,
> dan memperbaiki ekspektasi yang memang salah. Ini hijau yang
> kita **percayai**.
>
> Rangkuman Kasus 1: assertion tanpa pesan itu bukan cuma tidak
> nyaman. Dia menyembunyikan informasi yang sudah ada di tangan
> kita. Datanya ada, kita saja yang membuangnya."

---

# BLOK 33-36 - Jeda (3 menit)

Jangan lewati blok ini. Peserta baru saja mengetik banyak.

> "Tiga menit istirahat. Kalau ada yang error-nya beda dengan
> layar saya, sekarang waktunya bilang."

---

# BLOK 36-42 - Bedah Kasus 2 (6 menit)

## Yang Anda katakan

> "Sekarang bagian yang lebih berbahaya.
>
> Sejak awal sesi, `cart.feature` hijau terus. Dua skenario, tidak
> pernah merah sekalipun. Menurut kalian, aman?"

Tunggu jawaban. Biasanya mereka bilang aman.

> "Mari kita uji keyakinan itu. Saya akan merusak aplikasinya -
> secara sengaja - dan kita lihat apakah test-nya sadar."

## Perintah terminal - demonstrasi pertama

Buka `src/test/resources/features/cart.feature` dan ubah angka
ekspektasi menjadi sesuatu yang jelas mustahil, baris 15:

```gherkin
    Then jumlah pada badge keranjang adalah "99"
```

Jalankan:

```bash
./gradlew test
```

## Output yang muncul

```
4 Scenarios (4 passed)
13 Steps (13 passed)

BUILD SUCCESSFUL
```

## Yang Anda katakan

Beri jeda beberapa detik sebelum bicara. Biarkan mereka membaca
layar sendiri.

> "Saya bilang badge keranjang harus berisi angka **99**. Kita
> hanya memasukkan satu barang. Dan test-nya... lolos.
>
> Test ini tidak pernah memeriksa apa pun. Sejak sesi dimulai, dia
> hanya berpura-pura bekerja.
>
> Kalau ada fitur keranjang rusak di produksi minggu depan, test
> ini akan tetap hijau. Dia bukan cuma tidak berguna - dia lebih
> buruk dari tidak ada, karena dia membuat kita merasa aman."

Kembalikan feature ke `"1"` sebelum lanjut.

## Perintah terminal - tunjukkan penyebabnya

```bash
code src/test/java/com/jayjay/steps/CartSteps.java
```

Sorot baris 42-56:

```java
    @Then("jumlah pada badge keranjang adalah {string}")
    public void jumlahPadaBadgeKeranjangAdalah(String jumlahYangDiharapkan) {
        try {
            By lokatorBadgeSalahKetik =
                    By.className("shopping_cart_bdge");
            String jumlahSebenarnya =
                    webDriver.findElement(lokatorBadgeSalahKetik).getText();

            if (!jumlahSebenarnya.equals(jumlahYangDiharapkan)) {
                throw new AssertionError("badge tidak sesuai");
            }
        } catch (Exception exceptionYangDitelan) {
            // Sengaja dikosongkan pada versi BEFORE.
        }
    }
```

## Yang Anda katakan

> "Dua kesalahan bertumpuk di sini, dan itu yang membuatnya
> mematikan.
>
> Kesalahan pertama ada di baris 46: `shopping_cart_bdge`. Salah
> ketik. Huruf `a` hilang. Elemen ini tidak pernah ada di halaman,
> jadi `findElement` selalu melempar `NoSuchElementException`.
>
> Kesalahan kedua ada di baris 53: `catch (Exception)` dengan
> badan kosong. Exception tadi ditangkap, lalu dibuang ke tempat
> sampah. Method selesai tanpa keluhan. Cucumber melihat method
> yang selesai normal, dan menandainya lolos.
>
> Perhatikan juga: `AssertionError` di baris 51 pun ikut tertelan,
> karena... nah, ini pertanyaan buat kalian. Kenapa `catch
> (Exception)` bisa menelan `AssertionError`?"

Tunggu. Jawabannya: sebenarnya **tidak bisa** - `AssertionError`
turunan `Error`, bukan `Exception`. Tapi di sini tidak penting,
karena `findElement` sudah melempar duluan di baris 48, jadi
baris 51 tidak pernah tercapai. Ini poin bagus untuk dibahas kalau
ada peserta yang jeli.

> "Satu kalimat yang saya mau kalian ingat: **catch kosong itu
> bukan penanganan error. Itu penyembunyian error.**"

---

# BLOK 42-53 - REFACTOR 2: Penanganan exception yang benar (11 menit)

## File yang diubah

`src/test/java/com/jayjay/steps/CartSteps.java`

### Ubahan 1 - baris 1-11, sesuaikan import

**BEFORE:**

```java
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
```

**AFTER:**

```java
import static org.assertj.core.api.Assertions.assertThat;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
```

Import `By` tidak lagi diperlukan di sini - locator akan diambil
dari Page Object.

### Ubahan 2 - baris 42-56, tulis ulang seluruh method

**AFTER:**

```java
    @Then("jumlah pada badge keranjang adalah {string}")
    public void jumlahPadaBadgeKeranjangAdalah(String jumlahYangDiharapkan) {
        WebElement badgeKeranjang = cartPage.getExplicitWait()
                .until(ExpectedConditions.visibilityOfElementLocated(
                        cartPage.lokatorBadgeKeranjang()));

        String jumlahSebenarnya = badgeKeranjang.getText();

        assertThat(jumlahSebenarnya)
                .as("Jumlah pada badge keranjang tidak sesuai "
                        + "dengan jumlah produk yang ditambahkan")
                .isEqualTo(jumlahYangDiharapkan);
    }
```

### Yang Anda katakan sambil mengetik

Ini bagian yang paling perlu pelan. Ada empat hal yang harus
mereka tangkap.

> "**Satu - try-catch-nya saya hapus seluruhnya.**
>
> Kalian mungkin bertanya: bukankah menghapus try-catch itu
> berbahaya? Justru sebaliknya. Di dalam test, exception yang
> lolos ke atas itu **hal yang kita inginkan**. Exception itu cara
> test memberi tahu kita ada yang tidak beres. Kalau kita tangkap
> dan buang, kita membungkam satu-satunya alarm yang kita punya.
>
> Aturannya: di kode test, **tangkap exception hanya kalau kalian
> benar-benar bisa melakukan sesuatu yang berguna dengannya.**
> Kalau yang bisa kalian lakukan hanya menulis ulang pesannya -
> biarkan saja lewat.
>
> **Dua - saya pakai `WebDriverWait`, bukan `findElement`
> langsung.**
>
> Badge keranjang itu muncul lewat JavaScript setelah tombol
> diklik. `findElement` memeriksa satu kali, saat itu juga. Kalau
> badge-nya belum sempat muncul, gagal. `WebDriverWait` mencoba
> berulang sampai sepuluh detik.
>
> Dan tolong dicatat: ini **bukan** `Thread.sleep`. `Thread.sleep`
> menunggu selama waktu yang ditentukan, titik - kalau elemennya
> muncul dalam 0,2 detik, sisanya terbuang. `WebDriverWait`
> berhenti menunggu begitu elemennya ada. Di repo ini tidak ada
> satu pun `Thread.sleep`, dan tolong jangan ditambahkan.
>
> **Tiga - locator diambil dari Page Object.**
>
> `cartPage.lokatorBadgeKeranjang()`. Salah ketik tadi terjadi
> karena locator ditulis langsung di dalam step. Kalau locator
> hanya ditulis di satu tempat - di Page Object - salah ketik
> semacam itu jauh lebih mudah ketahuan.
>
> **Empat - assertion-nya informatif**, persis seperti yang kita
> pelajari di Kasus 1. `assertThat` menerima nilai mentahnya,
> `.as()` menjelaskan niatnya."

### Ubahan 3 - opsional, rapikan `CartPage.java`

Kalau waktu masih longgar (jarang), hapus method
`getWebDriver()` di `src/test/java/com/jayjay/pages/CartPage.java`
baris 22-29 - sekarang sudah tidak dipakai siapa pun. Kalau waktu
mepet, lewati saja dan sebutkan sebagai PR:

> "Ada satu method di CartPage yang sekarang jadi sampah. Cari
> sendiri yang mana, itu latihan kecil buat kalian."

---

# BLOK 53-57 - Bukti Kasus 2 (4 menit)

## Perintah terminal

```bash
./gradlew test
```

## Output yang muncul - MERAH

```
org.openqa.selenium.TimeoutException:
Expected condition failed: waiting for visibility of element
located by By.className: shopping_cart_bdge
(tried for 10 second(s) with 500 milliseconds interval)

    at com.jayjay.steps.CartSteps.jumlahPadaBadgeKeranjangAdalah(
        CartSteps.java:44)

4 Scenarios (2 failed, 2 passed)
```

## Yang Anda katakan

> "Merah. Dan ini **kabar baik**.
>
> Test ini akhirnya jujur. Bug-nya sudah ada di sana sejak awal
> sesi - kita saja yang tidak diberi tahu. Yang berubah hari ini
> bukan aplikasinya. Yang berubah adalah test kita akhirnya mau
> bicara.
>
> Sekarang kita perbaiki salah ketiknya."

## File yang diubah

Salah ketik ada di Page Object. Buka
`src/test/java/com/jayjay/pages/CartPage.java` baris 35-37 dan
pastikan isinya benar:

```java
    public By lokatorBadgeKeranjang() {
        return By.className("shopping_cart_badge");
    }
```

Kalau `TimeoutException` masih menyebut `shopping_cart_bdge`,
berarti masih ada sisa locator lama di `CartSteps.java` -
periksa lagi.

## Perintah terminal

```bash
./gradlew test
```

## Output akhir yang muncul

```
Feature: Keranjang belanja SauceDemo

  Scenario: Menambah satu produk ke keranjang       PASSED
  Scenario: Menambah dua produk ke keranjang        PASSED

Feature: Login SauceDemo

  Scenario: Login berhasil dengan standard_user     PASSED
  Scenario: Login gagal dengan user yang terkunci   PASSED

4 Scenarios (4 passed)
13 Steps (13 passed)

BUILD SUCCESSFUL
```

## Uji ulang keyakinan - lakukan kalau sempat (1 menit)

Ubah lagi `cart.feature` baris 15 menjadi `"99"`, jalankan:

```bash
./gradlew test
```

Output:

```
org.opentest4j.AssertionFailedError:
[Jumlah pada badge keranjang tidak sesuai dengan jumlah produk
 yang ditambahkan]
expected: "99"
 but was: "1"
```

> "Sekarang test-nya marah saat saya berbohong. Di awal sesi, dia
> diam saja. Ini bedanya test yang hijau dengan test yang benar."

Kembalikan ke `"1"`.

---

# BLOK 57-60 - Penutup (3 menit)

## Yang Anda katakan

> "Tiga kalimat untuk dibawa pulang.
>
> **Satu.** Assertion tanpa pesan membuang informasi yang sudah
> ada di tangan kalian. Serahkan nilai mentahnya ke assertion,
> jangan dimasak dulu.
>
> **Dua.** `catch` kosong bukan penanganan error, itu
> penyembunyian error. Di kode test, biarkan exception naik - dia
> alarm kalian.
>
> **Tiga.** Sebelum percaya pada test yang hijau, rusak dulu
> ekspektasinya dengan sengaja. Kalau dia tetap hijau, dia tidak
> pernah memeriksa apa pun.
>
> Tugas take-home ada di `CHALLENGE.md`. Kalian akan menulis
> feature checkout sendiri, dengan dua prinsip yang baru saja kita
> pakai. Kumpulkan lewat pull request."

## Perintah terminal

```bash
git checkout .
```

> "Saya kembalikan repo ke versi awal, supaya kalian bisa
> mengulang latihan ini sendiri dari nol di rumah."

---

## Rencana cadangan kalau waktu mepet

| Sisa waktu | Yang dilakukan |
|------------|----------------|
| Tertinggal 5' di menit 30 | Lewati blok jeda, potong tanya jawab Kasus 1 |
| Tertinggal 10' di menit 40 | Lewati "uji ulang keyakinan", langsung refactor |
| Tertinggal 15' | Kasus 2 didemokan tanpa peserta ikut mengetik, kode AFTER diberikan setelah sesi |

Yang **tidak boleh** dilewati dalam keadaan apa pun: demo
`"99"` di blok 36-42. Itu momen paling berkesan sepanjang sesi.

---

## Masalah teknis yang mungkin muncul saat live

| Gejala | Penanganan cepat |
|--------|------------------|
| Peserta kena versi Chrome tidak cocok | Suruh jalankan headless dan lanjut menonton; perbaiki setelah sesi lewat `README.md` |
| `gradlew: Permission denied` | `chmod +x gradlew` |
| Unduhan dependency lambat | Suruh menonton layar Anda dulu, lanjut mengetik nanti |
| SauceDemo lambat atau tumbang | Ada laporan HTML dari run persiapan Anda - pakai itu sebagai bukti |
| Suite berjalan lebih dari 60 detik | Biasanya jaringan; sebutkan bahwa waktu normal di bawah satu menit |
