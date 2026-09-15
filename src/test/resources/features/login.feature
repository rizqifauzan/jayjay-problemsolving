Feature: Login SauceDemo
  Sebagai pengguna SauceDemo
  Saya ingin masuk ke aplikasi
  Supaya bisa melihat daftar produk

  # Feature ini dipakai untuk KASUS 1: assertion tanpa pesan.
  # Skenario kedua memang dirancang GAGAL saat pertama dijalankan.

  Scenario: Login berhasil dengan standard_user
    Given pengguna membuka halaman login SauceDemo
    When pengguna login dengan username "standard_user" dan password "secret_sauce"
    Then halaman daftar produk tampil

  Scenario: Login gagal dengan user yang terkunci
    Given pengguna membuka halaman login SauceDemo
    When pengguna login dengan username "locked_out_user" dan password "secret_sauce"
    Then pesan error memuat teks "Epic sadface: Sorry, this user has been locked out."
