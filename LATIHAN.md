# LATIHAN - Lembar Kerja Peserta

Praktikum Java: Live Coding Problem Solving
JayJay Edukasi

**Tema: "Test yang hijau belum tentu test yang benar."**

---

## Cara memakai lembar ini

Ikuti bersama mentor. Setiap kasus punya pola yang sama:

1. **Amati** - jalankan test, baca apa yang terjadi
2. **Curigai** - tanyakan apakah hasilnya benar-benar bisa dipercaya
3. **Perbaiki** - ketik ulang kodenya, jangan salin-tempel
4. **Buktikan** - jalankan lagi, bandingkan dengan sebelumnya

Kotak **CHECKPOINT** adalah titik di mana layar kalian harus sama
dengan layar mentor. Kalau berbeda, angkat tangan sebelum lanjut.

Kotak **JAWAB DULU** adalah pertanyaan yang harus kalian jawab
sendiri sebelum mentor menjelaskan. Tebakan salah tidak masalah -
justru itu gunanya.

---

# Sebelum mulai

Jalankan:

```bash
./gradlew test
```

## CHECKPOINT 0

Layar kalian harus menunjukkan:

```
4 Scenarios (1 failed, 3 passed)
13 Steps (1 failed, 12 passed)
```

Satu merah, tiga hijau. **Ini normal dan disengaja.** Jangan
diperbaiki dulu.

> **JAWAB DULU:** Dari empat skenario ini, menurut kalian mana
> yang paling bisa dipercaya?

Simpan jawaban kalian. Di akhir sesi kita bandingkan.

---

# KASUS 1 - Assertion yang tidak menjelaskan apa pun

**Menit 13-33 | File: `LoginSteps.java` dan `login.feature`**

## Apa yang terjadi sekarang

Satu skenario di `login.feature` gagal. Buka pesan gagalnya:

```bash
./gradlew test
```

Pesan yang muncul:

```
org.opentest4j.AssertionFailedError:
expected: <true> but was: <false>
    at com.jayjay.steps.LoginSteps.pesanErrorMemuatTeks(
        LoginSteps.java:46)
```

> **JAWAB DULU:** Dari pesan di atas, tuliskan di kertas - apa
> yang sebenarnya salah? Bug di aplikasi, atau test-nya yang
> keliru? Kalau kalian tidak bisa menjawab, itu memang intinya.

Pesan itu hanya bilang "sesuatu yang seharusnya `true` ternyata
`false`". Kita tidak tahu pesan error apa yang muncul di layar,
tidak tahu apa yang diharapkan, tidak tahu siapa yang salah.

Penyebabnya ada di `LoginSteps.java` baris 46:

```java
assertTrue(pesanErrorSebenarnya.contains(teksYangDiharapkan));
```

Perhatikan: kita menghitung `contains` **sendiri**, lalu hanya
menyerahkan hasil `true`/`false` ke assertion. Saat itu juga teks
aslinya hilang - assertion tidak pernah melihatnya, jadi dia tidak
bisa menampilkannya.

## Tugas kalian

### Tugas 1.1 - Ganti library assertion

**File:** `src/test/java/com/jayjay/steps/LoginSteps.java`
**Baris:** 3

Hapus import `assertTrue` milik JUnit, ganti dengan `assertThat`
milik AssertJ.

> Petunjuk: import AssertJ juga bersifat `static`, dan berasal
> dari paket `org.assertj.core.api.Assertions`.

### Tugas 1.2 - Perbaiki `halamanDaftarProdukTampil`

**File:** `LoginSteps.java`
**Baris:** 38-41

Yang harus berubah:

- Keluarkan hasil `inventoryPage.apakahHalamanProdukTampil()` ke
  sebuah variabel dengan nama yang jelas
- Ganti `assertTrue(...)` menjadi `assertThat(...)` diikuti
  `.isTrue()`
- Sisipkan `.as(...)` di antaranya, berisi kalimat yang
  menjelaskan apa yang seharusnya terjadi
- Di dalam `.as(...)`, sertakan `webDriver.getCurrentUrl()`

Kerangkanya seperti ini - isi sendiri bagian yang kosong:

```java
    @Then("halaman daftar produk tampil")
    public void halamanDaftarProdukTampil() {
        boolean ____ = inventoryPage.apakahHalamanProdukTampil();

        assertThat(____)
                .as("____", webDriver.getCurrentUrl())
                .isTrue();
    }
```

> **Kenapa URL ikut disertakan?** Karena pesan gagal yang baik
> tidak hanya bilang "salah", tapi menunjukkan **keadaan sistem
> saat gagal**. Kalau login ternyata nyangkut di halaman login,
> URL langsung membocorkannya.

### Tugas 1.3 - Perbaiki `pesanErrorMemuatTeks`

**File:** `LoginSteps.java`
**Baris:** 43-47

Ini bagian terpenting. Yang harus berubah:

- **Jangan** panggil `.contains(...)` sendiri
- Serahkan `pesanErrorSebenarnya` apa adanya ke `assertThat(...)`
- Biarkan AssertJ yang memeriksa, lewat `.contains(...)` miliknya
- Tambahkan `.as(...)` yang menjelaskan niat pemeriksaan ini

Kerangkanya:

```java
    @Then("pesan error memuat teks {string}")
    public void pesanErrorMemuatTeks(String teksYangDiharapkan) {
        String pesanErrorSebenarnya = loginPage.ambilPesanError();

        assertThat(____)
                .as("____")
                .contains(____);
    }
```

> **Aturan yang harus kalian ingat seumur hidup:**
> Jangan pernah memasak data sebelum diserahkan ke assertion.
> Serahkan bahan mentahnya. Kalau kalian yang menghitung,
> assertion hanya menerima `true`/`false` dan sudah kehilangan
> informasi. Kalau assertion yang menghitung, dia masih memegang
> data aslinya dan bisa menampilkannya saat gagal.

## Jalankan lagi

```bash
./gradlew test
```

## CHECKPOINT 1

Pesan gagal kalian sekarang harus berbentuk seperti ini:

```
org.opentest4j.AssertionFailedError:
[Pesan error yang tampil di halaman login tidak sesuai harapan]
Expecting actual:
  "Epic sadface: Sorry, this user has been locked out."
to contain:
  "Username and password do not match"
```

Kalimat di dalam `[...]` boleh berbeda - itu kalimat kalian
sendiri. Yang wajib sama: **teks asli dan teks harapan sama-sama
muncul di layar.**

Kalau yang muncul masih `expected: <true> but was: <false>`,
berarti kalian masih menghitung `contains` sendiri. Baca ulang
Tugas 1.3.

## Berhenti sejenak dan sadari

Test kalian **masih merah**. Jumlah kegagalan sama persis seperti
sebelum refactor. Tidak ada satu pun bug aplikasi yang diperbaiki.

Tapi sekarang kalian bisa menjawab pertanyaan yang tadi mustahil
dijawab:

> **JAWAB DULU:** Aplikasi berkata `"this user has been locked
> out"`. Test mengharapkan `"Username and password do not match"`.
> Siapa yang salah?

Jawabannya: **test-nya.** `locked_out_user` memang akun yang
sengaja dikunci oleh SauceDemo, jadi pesan aplikasinya sudah
benar. Ekspektasi kita yang keliru sejak awal.

Waktu yang dibutuhkan untuk sampai ke kesimpulan ini: sebelum
refactor sekitar lima belas menit membaca kode dan mencoba manual.
Sesudah refactor, lima belas detik membaca layar.

Kodenya tidak jadi lebih pintar. Dia hanya jadi lebih jujur
bercerita.

## Tugas 1.4 - Betulkan ekspektasi yang salah

**File:** `src/test/resources/features/login.feature`
**Baris:** 17

Ganti teks harapannya menjadi potongan pesan yang **benar-benar**
ditampilkan aplikasi. Ambil dari pesan gagal di layar kalian,
jangan menebak.

> Petunjuk: tidak perlu menyalin kalimat penuh. `.contains(...)`
> hanya butuh potongan yang khas.

```bash
./gradlew test
```

## CHECKPOINT 2

```
4 Scenarios (4 passed)
13 Steps (13 passed)

BUILD SUCCESSFUL
```

Hijau. Tapi hijau yang berbeda dari hijau di awal sesi - ini hijau
yang kalian dapat setelah membaca pesan gagal, memahaminya, dan
membetulkan ekspektasi yang memang salah. Hijau yang kalian
**percayai**.

## Rangkuman Kasus 1

Assertion tanpa pesan bukan sekadar tidak nyaman. Dia membuang
informasi yang sebenarnya **sudah ada di tangan kita**. Datanya
ada di memori, kita saja yang tidak meneruskannya.

---

# KASUS 2 - Exception yang ditelan diam-diam

**Menit 42-57 | File: `CartSteps.java` dan `CartPage.java`**

## Apa yang terjadi sekarang

Sejak awal sesi, `cart.feature` hijau terus. Dua skenario, tidak
pernah merah sekalipun.

> **JAWAB DULU:** Menurut kalian, apakah dua skenario itu aman?

Mari kita uji keyakinan itu.

**File:** `src/test/resources/features/cart.feature`
**Baris:** 15

Ubah angka harapannya jadi sesuatu yang jelas mustahil:

```gherkin
    Then jumlah pada badge keranjang adalah "99"
```

Kita hanya memasukkan satu barang ke keranjang, tapi kita menuntut
badge berisi 99. Jalankan:

```bash
./gradlew test
```

## CHECKPOINT 3

```
4 Scenarios (4 passed)

BUILD SUCCESSFUL
```

Baca pelan-pelan. Kita berbohong terang-terangan, dan test-nya
**lolos**.

Test ini tidak pernah memeriksa apa pun sejak sesi dimulai. Dia
hanya berpura-pura bekerja. Kalau fitur keranjang rusak di
produksi minggu depan, dia akan tetap hijau.

Test seperti ini bukan cuma tidak berguna - dia **lebih buruk
daripada tidak ada test sama sekali**, karena dia membuat kita
merasa aman.

Kembalikan `cart.feature` baris 15 ke `"1"` sebelum lanjut.

## Kenapa bisa begitu

Buka `src/test/java/com/jayjay/steps/CartSteps.java` baris 42-56:

```java
        try {
            By lokatorBadgeSalahKetik =
                    By.className("shopping_cart_bdge");
            String jumlahSebenarnya =
                    webDriver.findElement(lokatorBadgeSalahKetik)
                            .getText();

            if (!jumlahSebenarnya.equals(jumlahYangDiharapkan)) {
                throw new AssertionError("badge tidak sesuai");
            }
        } catch (Exception exceptionYangDitelan) {
            // Sengaja dikosongkan pada versi BEFORE.
        }
```

Ada **dua** kesalahan yang bertumpuk, dan justru tumpukan itu yang
membuatnya mematikan:

**Kesalahan pertama - baris 46.** Locator-nya salah ketik:
`shopping_cart_bdge`, huruf `a` hilang. Elemen ini tidak pernah
ada di halaman, jadi `findElement` **selalu** melempar
`NoSuchElementException`.

**Kesalahan kedua - baris 53.** `catch (Exception)` dengan badan
kosong. Exception tadi ditangkap lalu dibuang. Method selesai
tanpa keluhan, Cucumber melihat method yang selesai normal, dan
menandainya lolos.

Perhatikan juga: baris 50-52 yang berisi perbandingan
sesungguhnya **tidak pernah tercapai**, karena baris 48 sudah
melempar duluan.

> **Kalimat untuk dibawa pulang:**
> `catch` kosong bukan penanganan error. Itu penyembunyian error.

## Tugas kalian

### Tugas 2.1 - Sesuaikan import

**File:** `CartSteps.java`
**Baris:** 1-11

Yang perlu ditambahkan:

- `assertThat` dari AssertJ, seperti di Kasus 1
- `WebElement` dari Selenium
- `ExpectedConditions` dari `org.openqa.selenium.support.ui`

Yang bisa dibuang: import `By`, karena locator tidak lagi ditulis
di sini.

### Tugas 2.2 - Tulis ulang method verifikasi badge

**File:** `CartSteps.java`
**Baris:** 42-56

Buang seluruh `try-catch`, lalu susun ulang dengan empat aturan
berikut:

**Aturan 1 - Hapus try-catch sepenuhnya.**

Kalian mungkin bertanya, bukankah membuang try-catch itu
berbahaya? Di kode test, justru sebaliknya. Exception yang lolos
ke atas adalah **hal yang kita inginkan** - itulah cara test
memberi tahu ada yang tidak beres. Menangkap dan membuangnya sama
dengan membungkam satu-satunya alarm yang kita punya.

Pegangannya: tangkap exception hanya kalau kalian benar-benar bisa
melakukan sesuatu yang berguna dengannya. Kalau yang bisa kalian
lakukan cuma menulis ulang pesannya, biarkan saja lewat.

**Aturan 2 - Pakai `WebDriverWait`, bukan `findElement`
telanjang.**

Badge keranjang muncul lewat JavaScript setelah tombol diklik.
`findElement` memeriksa satu kali saat itu juga; kalau badge belum
sempat muncul, langsung gagal. `WebDriverWait` mencoba berulang
sampai batas waktunya.

Dan ini penting: **`WebDriverWait` bukan `Thread.sleep`.**
`Thread.sleep` menunggu selama waktu yang ditentukan tanpa peduli
apa pun. `WebDriverWait` berhenti menunggu begitu elemennya
muncul. Di repo ini tidak ada satu pun `Thread.sleep`, dan tolong
jangan ditambahkan.

Objek `WebDriverWait` sudah tersedia lewat
`cartPage.getExplicitWait()`.

**Aturan 3 - Ambil locator dari Page Object.**

Salah ketik tadi bisa terjadi karena locator ditulis langsung di
dalam step. Kalau locator hanya hidup di satu tempat - di Page
Object - kesalahan semacam itu jauh lebih mudah ketahuan.

Pakai `cartPage.lokatorBadgeKeranjang()`.

**Aturan 4 - Assertion-nya informatif**, persis seperti Kasus 1.

Kerangkanya:

```java
    @Then("jumlah pada badge keranjang adalah {string}")
    public void jumlahPadaBadgeKeranjangAdalah(
            String jumlahYangDiharapkan) {
        WebElement badgeKeranjang = cartPage.getExplicitWait()
                .until(ExpectedConditions
                        .visibilityOfElementLocated(____));

        String jumlahSebenarnya = ____;

        assertThat(____)
                .as("____")
                .isEqualTo(____);
    }
```

## Jalankan lagi

```bash
./gradlew test
```

## CHECKPOINT 4

Sekarang test kalian harus **MERAH**:

```
org.openqa.selenium.TimeoutException:
Expected condition failed: waiting for visibility of element
located by By.className: shopping_cart_bdge

    at com.jayjay.steps.CartSteps.jumlahPadaBadgeKeranjangAdalah(
        CartSteps.java:44)

4 Scenarios (2 failed, 2 passed)
```

**Merah itu kabar baik di sini.** Bug-nya sudah ada sejak awal
sesi - kita saja yang tidak pernah diberi tahu. Yang berubah hari
ini bukan aplikasinya, melainkan test kita yang akhirnya mau
bicara.

### Tugas 2.3 - Betulkan locator yang salah ketik

**File:** `src/test/java/com/jayjay/pages/CartPage.java`
**Baris:** 35-37

Pastikan nama class CSS-nya benar. Kalau `TimeoutException` masih
menyebut `shopping_cart_bdge`, berarti masih ada sisa locator lama
di `CartSteps.java` - periksa lagi.

```bash
./gradlew test
```

## CHECKPOINT 5

```
4 Scenarios (4 passed)
13 Steps (13 passed)

BUILD SUCCESSFUL
```

## Uji ulang keyakinan kalian

Ubah lagi `cart.feature` baris 15 menjadi `"99"`, jalankan:

```bash
./gradlew test
```

Sekarang hasilnya:

```
org.opentest4j.AssertionFailedError:
[Jumlah pada badge keranjang tidak sesuai dengan jumlah produk
 yang ditambahkan]
expected: "99"
 but was: "1"
```

Test-nya **marah** saat kita berbohong. Di awal sesi, dia diam
saja. Inilah bedanya test yang hijau dengan test yang benar.

Kembalikan ke `"1"`.

## Rangkuman Kasus 2

`catch` kosong menyembunyikan kegagalan, bukan menanganinya. Di
kode test, biarkan exception naik ke atas - dia alarm kalian.

---

# Penutup

Buka lagi jawaban kalian di CHECKPOINT 0: skenario mana yang
paling bisa dipercaya?

Jawaban yang benar adalah **yang merah**. Sejak awal sesi, satu-
satunya test yang jujur adalah test yang gagal. Dua skenario
keranjang yang hijau justru sedang membohongi kita.

## Tiga hal yang dibawa pulang

**Satu.** Assertion tanpa pesan membuang informasi yang sudah ada
di tangan kalian. Serahkan nilai mentahnya ke assertion, jangan
dimasak dulu.

**Dua.** `catch` kosong bukan penanganan error, itu penyembunyian
error. Di kode test, biarkan exception naik.

**Tiga.** Sebelum percaya pada test yang hijau, rusak dulu
ekspektasinya dengan sengaja. Kalau dia tetap hijau, dia tidak
pernah memeriksa apa pun.

## Mengulang latihan ini di rumah

Kembalikan repo ke kondisi awal:

```bash
git checkout .
```

Seluruh perubahan hari ini hilang, dan kalian bisa mengerjakannya
lagi dari nol tanpa dipandu.

## Langkah berikutnya

Tugas take-home ada di `CHALLENGE.md`. Kalian akan menulis feature
checkout sendiri, memakai dua prinsip yang baru saja dipraktikkan.
Dikumpulkan lewat pull request, batas waktu tujuh hari.
