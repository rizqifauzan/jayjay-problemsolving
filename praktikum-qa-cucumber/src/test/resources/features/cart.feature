Feature: Keranjang belanja SauceDemo
  Sebagai pengguna yang sudah login
  Saya ingin menambah produk ke keranjang
  Supaya bisa melanjutkan ke checkout

  # Feature ini dipakai untuk KASUS 2: exception ditelan catch kosong.
  # Kedua skenario akan HIJAU, padahal verifikasi badge tidak pernah
  # benar-benar terjadi.

  Background:
    Given pengguna sudah login sebagai "standard_user"

  Scenario: Menambah satu produk ke keranjang
    When pengguna menambahkan produk "Sauce Labs Backpack" ke keranjang
    Then jumlah pada badge keranjang adalah "1"

  Scenario: Menambah dua produk ke keranjang
    When pengguna menambahkan produk "Sauce Labs Backpack" ke keranjang
    And pengguna menambahkan produk "Sauce Labs Bike Light" ke keranjang
    Then jumlah pada badge keranjang adalah "2"
