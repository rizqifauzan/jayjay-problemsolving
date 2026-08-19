# CHALLENGE - Tugas Take-Home

Praktikum Java: Live Coding Problem Solving
JayJay Edukasi

---

## Ringkasan tugas

Tambahkan **satu feature file baru** untuk alur checkout di
SauceDemo, lengkap dengan step definition dan Page Object-nya.

Dua prinsip yang dipakai sama persis dengan yang kalian praktikkan
di `LATIHAN.md`. Buka lagi kalau lupa.

Syarat mutlak - keduanya adalah inti sesi kemarin:

1. Setiap assertion harus **informatif**: saat gagal, pesannya
   menjelaskan sendiri apa yang terjadi tanpa perlu membuka kode.
2. Exception harus **ditangani dengan benar**: tidak ada `catch`
   kosong, tidak ada exception yang ditelan diam-diam.

Batas waktu: **7 hari** sejak sesi berakhir.

---

## Alur yang diuji

Alur checkout SauceDemo dari awal sampai selesai:

1. Login sebagai `standard_user` / `secret_sauce`
2. Tambahkan minimal dua produk ke keranjang
3. Buka halaman keranjang lewat ikon keranjang
4. Klik tombol **Checkout**
5. Isi form informasi: First Name, Last Name, Postal Code
6. Klik **Continue**
7. Di halaman ringkasan, verifikasi:
   - daftar produk sesuai dengan yang tadi ditambahkan
   - **Item total** sesuai jumlah harga produk
8. Klik **Finish**
9. Verifikasi pesan sukses: `Thank you for your order!`

---

## File yang harus kalian buat

```
src/test/resources/features/checkout.feature
src/test/java/com/jayjay/steps/CheckoutSteps.java
src/test/java/com/jayjay/pages/CheckoutPage.java
```

Boleh menambah method di Page Object yang sudah ada
(`LoginPage`, `InventoryPage`, `CartPage`) kalau memang perlu.

**Jangan** mengubah `login.feature`, `cart.feature`,
`LoginSteps.java`, dan `CartSteps.java`. Itu bahan sesi, biarkan
apa adanya.

---

## Skenario minimum yang harus ada

Minimal **tiga** skenario:

### Skenario 1 - Checkout berhasil (jalur normal)

Alur lengkap seperti di atas sampai pesan sukses muncul.

### Skenario 2 - Checkout gagal karena data wajib kosong

Kosongkan First Name, klik Continue, verifikasi pesan error yang
muncul.

Hati-hati di sini: jangan menyalin bulat-bulat teks error dari
tebakan kalian. **Jalankan dulu, baca pesan aslinya, baru tulis
ekspektasi.** Persis seperti yang kita lakukan pada
`locked_out_user` kemarin.

### Skenario 3 - Verifikasi total harga

Tambahkan dua produk yang harganya kalian tahu, lalu pastikan
Item total di halaman ringkasan sama dengan penjumlahannya.

Skenario ini yang paling mudah dibuat "hijau palsu". Uji sendiri:
ubah angka ekspektasinya jadi salah, jalankan. Kalau tetap hijau,
berarti test kalian tidak memeriksa apa pun.

---

## Aturan teknis

Sama persis dengan repo praktikum:

- **Dilarang `Thread.sleep`.** Pakai `WebDriverWait` dan
  `ExpectedConditions`. Satu saja `Thread.sleep` ditemukan,
  nilai kriteria 4 langsung nol.
- **Dilarang `catch` kosong**, dan dilarang `catch (Exception e)`
  yang isinya hanya `e.printStackTrace()` atau
  `System.out.println`. Itu sama saja menelan error.
- **Locator hanya di Page Object.** Tidak ada `By.id(...)` yang
  berkeliaran di dalam step definition.
- **Maksimal 80 karakter per baris.** Kode kalian akan dibaca
  lewat share screen saat review.
- **Nama variabel eksplisit.** `jumlahProdukDiKeranjang`, bukan
  `jml` atau `x` atau `data1`.
- Test harus tetap berjalan **headless secara default** dan bisa
  dimatikan lewat `-Dheadless=false`. Jangan sentuh
  `DriverFactory`.
- Seluruh suite (feature lama + checkout milik kalian) harus
  selesai **di bawah 90 detik**.

---

## Kriteria penilaian

Total 100 poin. Lulus di angka **70**.

### 1. Assertion informatif - 30 poin

| Poin | Kondisi |
|------|---------|
| 30 | Semua assertion memakai `assertThat` dengan `.as(...)` yang menjelaskan niat; nilai mentah diserahkan ke assertion, tidak dimasak lebih dulu |
| 20 | Sebagian besar informatif, ada satu-dua yang masih polos |
| 10 | Ada pesan, tapi isinya tidak membantu ("gagal", "error", "test failed") |
| 0 | Masih memakai `assertTrue(kondisi)` tanpa pesan |

Cara kami menilai: kami **sengaja merusak** ekspektasi kalian,
lalu membaca pesan gagalnya. Kalau dari pesan itu kami bisa tahu
apa yang salah tanpa membuka kode kalian - poin penuh.

### 2. Penanganan exception - 25 poin

| Poin | Kondisi |
|------|---------|
| 25 | Tidak ada `catch` yang menelan error; exception dibiarkan naik, atau ditangkap dengan konteks tambahan yang berguna lalu dilempar ulang |
| 15 | Ada `catch` yang mencatat error tapi tetap melanjutkan eksekusi |
| 5 | Ada `catch` yang hanya `printStackTrace` |
| 0 | Ada `catch` kosong |

### 3. Test benar-benar memeriksa sesuatu - 20 poin

Kami akan mengubah data aplikasi atau ekspektasi kalian, lalu
menjalankan test.

| Poin | Kondisi |
|------|---------|
| 20 | Semua skenario menjadi merah saat ekspektasinya dirusak |
| 10 | Sebagian skenario tetap hijau padahal sudah dirusak |
| 0 | Test hijau apa pun yang terjadi |

Ini kriteria terpenting. Skenario yang tidak pernah bisa merah
tidak dihitung sebagai test.

### 4. Kualitas kode - 15 poin

| Poin | Kondisi |
|------|---------|
| 15 | Page Object rapi, locator terpusat, tanpa `Thread.sleep`, baris maksimal 80 karakter, nama variabel eksplisit |
| 10 | Ada satu-dua pelanggaran kecil |
| 5 | Locator berserakan di step definition |
| 0 | Ada `Thread.sleep` |

### 5. Feature file yang terbaca - 10 poin

| Poin | Kondisi |
|------|---------|
| 10 | Gherkin ditulis dari sudut pandang pengguna, bukan sudut pandang browser; ada `Background` kalau memang berulang |
| 5 | Terbaca, tapi masih menyebut detail teknis |
| 0 | Berisi instruksi teknis mentah |

Contoh yang **salah** - ini menceritakan cara kerja browser:

```gherkin
    When pengguna klik elemen dengan id "checkout"
    And pengguna mengetik "Budi" ke field "first-name"
```

Contoh yang **benar** - ini menceritakan niat pengguna:

```gherkin
    When pengguna melanjutkan ke proses checkout
    And pengguna mengisi data pengiriman atas nama "Budi Santoso"
```

---

## Nilai tambahan - masing-masing 5 poin, maksimal 10

- **Scenario Outline** dengan `Examples` untuk menguji beberapa
  kombinasi data form checkout sekaligus.
- **Screenshot otomatis saat skenario gagal**, dipasang lewat
  `@After` di `Hooks.java` dengan pengecekan
  `scenario.isFailed()`.

---

## Cara mengumpulkan

1. Buat branch dari `main`:

   ```bash
   git checkout -b challenge/<nama-kalian>
   ```

2. Kerjakan, lalu pastikan seluruh suite hijau:

   ```bash
   ./gradlew test
   ```

3. Commit dan push:

   ```bash
   git add .
   git commit -m "Challenge checkout - <nama kalian>"
   git push -u origin challenge/<nama-kalian>
   ```

4. Buka pull request. Di deskripsinya, tulis:
   - berapa detik suite kalian berjalan
   - satu pesan gagal yang paling kalian banggakan, disalin apa
     adanya dari terminal
   - satu hal yang membuat kalian tersangkut, dan bagaimana
     akhirnya terpecahkan

Bagian nomor 4 ikut dinilai di kriteria 1. Kalau kalian tidak bisa
menunjukkan satu pesan gagal yang bagus, kemungkinan besar
assertion kalian belum informatif.

---

## Petunjuk kalau tersangkut

**Locator checkout SauceDemo** - cari lewat Inspect Element,
jangan menebak. Yang kalian butuhkan ada di sekitar `#checkout`,
`#first-name`, `#last-name`, `#postal-code`, `#continue`,
`#finish`, dan `.complete-header`.

**Item total** berada di elemen dengan class
`summary_subtotal_label`, isinya berupa teks seperti
`Item total: $39.98`. Kalian perlu memisahkan angkanya dari teks.
Kerjakan pemisahan itu di Page Object, jangan di step definition.

**Kalau badge atau elemen tidak ketemu** - jangan bungkus dengan
try-catch supaya "aman". Baca pesan `TimeoutException`-nya. Dia
menyebutkan locator mana yang gagal, dan itu justru informasi yang
kalian butuhkan.

**Kalau ragu apakah test kalian benar-benar menguji sesuatu** -
rusak ekspektasinya dengan sengaja. Kalau tetap hijau, kalian
sedang menulis test palsu. Ini cara paling cepat memeriksa
pekerjaan sendiri sebelum mengumpulkan.

---

Selamat mengerjakan. Ingat kalimat kita kemarin:
**test yang hijau belum tentu test yang benar.**
