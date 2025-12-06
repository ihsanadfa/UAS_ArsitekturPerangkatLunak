# Praktikum Arsitektur Perangkat Lunak

**PRAKTIKUM ARSITEKTUR PERANGKAT LUNAK**

*Disusun Untuk Memenuhi Tugas Praktikum Arsitektur Perangkat Lunak*

**Oleh:**
- **M. IHSAN RIZQULLAH ADFA**
- **NPM: 2208107010029**

---

**PROGRAM STUDI INFORMATIKA**  
**FAKULTAS MATEMATIKA DAN ILMU PENGETAHUAN ALAM**  
**UNIVERSITAS SYIAH KUALA**  
**DARUSSALAM, BANDA ACEH**  
**2025**

---

## Studi Kasus: Sistem Informasi Menu dan Pemesanan Makanan di Kantin Universitas

### Deskripsi

Sebuah kantin di lingkungan universitas ingin menerapkan sistem digital untuk mempermudah mahasiswa dan staf. Melalui aplikasi atau situs web, sistem harus:

- Menampilkan daftar menu makanan dan minuman yang tersedia beserta harganya **(R01)** secara real-time
- Memungkinkan pengguna memilih dan menambahkan item menu yang mereka inginkan ke dalam keranjang pesanan **(R02)**
- Menyimpan pesanan dan secara otomatis menghasilkan nomor antrean unik **(R03)** setelah pengguna selesai memilih dan memasukkan nama mereka
- Memberikan akses kepada staf kantin untuk memperbarui status ketersediaan menu, seperti mengubahnya menjadi 'tersedia' atau 'habis' **(R04)**
- Memungkinkan staf melihat daftar semua pesanan yang masuk secara berurutan **(R05)** untuk mempercepat proses penyiapan

Pembayaran akan tetap dilakukan secara manual di kasir dengan menunjukkan nomor antrean.
## 1. Requirements (R)

* **(R01)** Sistem harus menampilkan daftar menu makanan dan minuman yang tersedia beserta harganya
* **(R02)** Pengguna dapat memilih dan menambahkan item menu ke dalam keranjang pesanan
* **(R03)** Setelah selesai memilih, sistem akan menyimpan pesanan dan menghasilkan nomor antrean untuk pengguna
* **(R04)** Staf kantin dapat memperbarui status ketersediaan menu (tersedia/habis)
* **(R05)** Staf kantin dapat melihat daftar pesanan yang masuk secara berurutan

## 2. Facts (F)

* **(F01)** Setiap item menu memiliki satu harga yang pasti
* **(F02)** Setiap pengguna hanya dapat memiliki satu keranjang belanja pada satu waktu
* **(F03)** Makanan yang sudah habis tidak dapat dipesan lagi
* **(F04)** Setiap pesanan yang berhasil dibuat bersifat unik dan memiliki nomor antrean yang berbeda
* **(F05)** Waktu operasional kantin terbatas pada jam tertentu setiap harinya

## 3. Assumptions (A)

* **(A01)** Pengguna memiliki koneksi internet untuk mengakses sistem
* **(A02)** Pengguna akan membayar pesanannya di kasir saat pengambilan makanan
* **(A03)** Staf kantin akan secara rutin dan akurat memperbarui status ketersediaan menu
* **(A04)** Pengguna akan segera mengambil pesanannya setelah pesanan tersebut siap
* **(A05)** Tidak ada pembatalan pesanan setelah pesanan berhasil dikonfirmasi oleh sistem
---

# Problem Frames: Sistem Informasi Menu dan Pemesanan Makanan di Kantin Universitas

## 1. (R01) Sistem harus menampilkan daftar menu makanan dan minuman yang tersedia beserta harganya

### Keterangan

* **Machine:** Sistem Pemesanan (SP)
* **Domains:** Menu Kantin (X - Lexical) dan Pengguna (B - Biddable)
* **Fenomena:**
  * a: SP!Y1: Sistem membaca data dari domain Menu Kantin
  * b: SP!Y2: Sistem menampilkan data ke Pengguna
* **Requirement (c: Y3):** Memastikan menu yang dilihat Pengguna sesuai dengan yang ada di Menu Kantin

2.	(R02) Pengguna dapat memilih dan menambahkan item menu ke dalam keranjang pesanan.
 
Keterangan
•	Machine: Sistem Pemesanan (SP)
•	Domains: Pengguna (P) dan Keranjang (X - Lexical, merepresentasikan data keranjang).
•	Fenomena:
	a: P!E1: Pengguna melakukan aksi (event) menambah item.
	b: SP!C1: Sistem melakukan kontrol untuk mengubah data di Keranjang.
•	Requirement (c: E1, C1): Memastikan aksi dari Pengguna menyebabkan perubahan yang benar pada Keranjang.

3.	(R03) Setelah selesai memilih, sistem akan menyimpan pesanan dan menghasilkan nomor antrean untuk pengguna.
 
Keterangan
•	Machine: Sistem Pemesanan (SP)
•	Domains: Pengguna (P) dan Database Pesanan (X).
•	Fenomena:
	a: P!E1: Pengguna mengonfirmasi pesanan.
	b: SP!C1: Sistem menyimpan data ke Database Pesanan.
	c: SP!Y1: Sistem menampilkan nomor antrean ke Pengguna.
•	Requirement (d): Memastikan konfirmasi dari Pengguna menghasilkan data yang tersimpan dan nomor antrean yang ditampilkan.

4.	(R04) Staf kantin dapat memperbarui status ketersediaan menu (tersedia/habis).
 
Keterangan
•	Machine: Sistem Pemesanan (SP)
•	Domains: Staf Kantin (SK) dan Status Menu (X).
•	Fenomena:
	a: SK!E1: Staf Kantin memberi perintah update.
	b: SP!C1: Sistem mengubah data pada Status Menu.
•	Requirement (c): Memastikan perintah dari Staf menyebabkan perubahan yang benar pada data menu.

5.	(R05) Staf kantin dapat melihat daftar pesanan yang masuk secara berurutan.
 
Keterangan
•	Machine: Sistem Pemesanan (SP)
•	Domains: Database Pesanan (X) dan Staf Kantin (B).
•	Fenomena:
	a: SP!Y1: Sistem membaca data dari Database Pesanan.
	b: SP!Y2: Sistem menampilkan data ke Staf Kantin.
•	Requirement (c: Y3): Memastikan data pesanan yang ditampilkan ke Staf sesuai dengan yang ada di database.



















---

# UML: Sistem Informasi Menu dan Pemesanan Makanan di Kantin Universitas

## 1. Use Case Diagram

*[Diagram tidak ditampilkan dalam format teks]*

## 2. Class Diagram

*[Diagram tidak ditampilkan dalam format teks]*

## 3. Activity Diagram

*[Diagram tidak ditampilkan dalam format teks]*

## 4. Sequence Diagram

*[Diagram tidak ditampilkan dalam format teks]*
 













---

# OCL: Sistem Informasi Menu dan Pemesanan Makanan di Kantin Universitas

## 1. Class Menu

### Konteks
Mendefinisikan syarat dan hasil untuk method `setStatusKetersediaan` pada class Menu.

### Penjelasan
* **pre:** Sebelum method dijalankan, memastikan nilai status yang dimasukkan adalah nilai boolean yang valid (true atau false)
* **post:** Setelah method berhasil dijalankan, memastikan atribut statusKetersediaan dari objek Menu tersebut telah berubah sesuai dengan nilai status yang baru

## 2. Class Keranjang

### Konteks
Mendefinisikan syarat dan hasil untuk method-method yang ada di dalam class Keranjang.

### Penjelasan
* **tambahItem:** 
  * *pre:* Memastikan menu yang ditambahkan harus tersedia dan kuantitasnya lebih dari 0
  * *post:* Memastikan item tersebut benar-benar ada di dalam keranjang dan jumlah item di keranjang bertambah
* **hapusItem:**
  * *pre:* Memastikan item yang akan dihapus memang ada di dalam keranjang
  * *post:* Memastikan item tersebut sudah tidak ada lagi dan jumlah item berkurang
* **kosongkanKeranjang:**
  * *pre:* Tidak ada syarat khusus (pre: true)
  * *post:* Memastikan keranjang benar-benar kosong

## 3. Class Pesanan

### Konteks
Mendefinisikan syarat dan hasil untuk method `hitungTotal` pada class Pesanan.

### Penjelasan
* **pre:** Sebelum menghitung total, memastikan ada item di dalam pesanan
* **post:** Memastikan nilai yang dikembalikan (result) adalah hasil dari penjumlahan hargaSatuan dikali kuantitas untuk setiap item di dalam pesanan

















## 4. Class SistemPemesanan (Controller)

### Konteks
Mendefinisikan syarat dan hasil untuk method-method utama pada class SistemPemesanan.

### Penjelasan
* **buatPesanan:**
  * *pre:* Memastikan keranjang tidak kosong
  * *post:* Memastikan sebuah objek Pesanan baru telah dibuat, pesanan tersebut masuk ke dalam daftar semua pesanan, dan keranjang yang tadi digunakan menjadi kosong
* **generateNomorAntrean:**
  * *pre:* Tidak ada syarat khusus
  * *post:* Memastikan nomor antrean yang dihasilkan (result) belum pernah digunakan oleh pesanan lain sebelumnya
* **updateStatusMenu:**
  * *pre:* Memastikan menu dengan idMenu yang dituju memang ada
  * *post:* Memastikan status ketersediaan dari menu tersebut benar-benar telah diperbarui










## 5. Class Tanpa Method (Invariants)

### Konteks
Untuk memenuhi syarat "satu OCL per class", class yang tidak memiliki method akan diberikan Invariant (aturan yang harus selalu benar) sederhana berdasarkan atributnya.

### Penjelasan
* **Pengguna & StafKantin:** Memastikan ID dan nama tidak boleh kosong
* **ItemKeranjang & ItemPesanan:** 
  * Memastikan kuantitas item selalu lebih dari nol
  * Untuk ItemPesanan, juga memastikan subtotal dihitung dengan benar
* **Pembayaran:** Memastikan jumlah pembayaran tidak boleh bernilai negatif













---

# Decorator: Sistem Informasi Menu dan Pemesanan Makanan di Kantin Universitas

## 1. Masalah Awal (Desain Sebelum Decorator)

Kita akan berfokus pada class Pesanan. Bayangkan kita ingin menambah fungsionalitas di mana pengguna bisa meminta opsi tambahan saat membuat pesanan. Opsi-opsi ini memengaruhi total harga dan deskripsi pesanan.

### Contoh Opsi Tambahan:
* **"Sambal Tambahan"** (+ Rp1.500)
* **"Kemasan Khusus"** (dibungkus terpisah, + Rp2.000)
* **"Pesanan Prioritas"** (dikerjakan lebih dulu, + Rp3.000)
### Pendekatan Inheritance (Bermasalah)

Jika kita menggunakan pendekatan pewarisan (inheritance) untuk menyelesaikan ini, kita akan membuat subclass baru dari Pesanan untuk setiap variasi:

* `PesananDenganSambal` (mewarisi dari Pesanan)
* `PesananKemasanKhusus` (mewarisi dari Pesanan)  
* `PesananPrioritas` (mewarisi dari Pesanan)

### Masalah Kombinatorial

Masalah utamanya muncul ketika pengguna ingin menggabungkan opsi-opsi ini:

* Bagaimana jika pengguna ingin **"Sambal Tambahan" DAN "Kemasan Khusus"**?
* Bagaimana jika mereka ingin **ketiga opsi sekaligus**?

Dengan inheritance, kita terpaksa membuat class baru untuk setiap kombinasi yang mungkin:

* `PesananSambalDanKemasan`
* `PesananSambalDanPrioritas`
* `PesananKemasanDanPrioritas` 
* `PesananSambalKemasanPrioritas`

Hal ini menyebabkan **"ledakan jumlah kelas"** (combinatorial explosion). Desain ini menjadi sangat kaku, sulit dirawat, dan tidak fleksibel jika kita ingin menambahkan opsi baru (misalnya, "Tanpa MSG") di kemudian hari.

### UML Class Diagram Awal (Sebelum Decorator)

Diagram berikut mengilustrasikan masalah "ledakan jumlah kelas" yang disebabkan oleh penggunaan inheritance murni.

*[Diagram tidak ditampilkan dalam format teks]*

#### Penjelasan
Diagram ini menunjukkan Pesanan sebagai parent class. Setiap opsi tambahan, dan setiap kombinasi dari opsi tersebut, harus dibuat sebagai subclass baru yang meng-override method `hitungTotal()` dan `getDeskripsi()`. Ini jelas tidak efisien dan sulit dikelola.

## 2. Solusi dengan Decorator Pattern

Decorator Pattern menyelesaikan masalah "ledakan jumlah kelas" dengan mengubah pendekatan secara fundamental: **dari pewarisan (inheritance) menjadi komposisi (composition)**.

Alih-alih membuat subclass baru untuk setiap kombinasi fitur, kita akan **"membungkus" (wrap)** objek Pesanan dasar dengan lapisan-lapisan decorator. Setiap decorator bertugas menambahkan satu fungsionalitas spesifik (satu opsi tambahan) secara dinamis.
### Implementasi pada Sistem Pemesanan Kantin

Kita akan mendefinisikan elemen-elemen penting berikut:

#### 1. Komponen Dasar (Interface Utama)
* **IPesanan** - Interface baru yang mendefinisikan method utama:
  * `hitungTotal(): Real`
  * `getDeskripsi(): String`

#### 2. Komponen Konkret (Concrete Component)
* **Pesanan** - Class yang dimodifikasi agar mengimplementasikan interface IPesanan
* Objek dasar yang akan "dihias" atau "dibungkus"

#### 3. Base Decorator (Class Pembungkus Dasar)
* **PesananDecorator** - Abstract class yang:
  * Mengimplementasikan IPesanan
  * Memiliki referensi (komposisi) ke objek IPesanan lain yang dibungkusnya (wrappee)
  * Method-nya mendelegasikan pemanggilan ke objek yang dibungkusnya

#### 4. Concrete Decorators (Class-Class Tambahan)
Class-class spesifik untuk setiap opsi tambahan:
* **SambalDecorator**
* **KemasanKhususDecorator** 
* **PrioritasDecorator**

Setiap class mewarisi dari PesananDecorator dan meng-override:
* `hitungTotal()` - menambahkan biayanya sendiri ke total
* `getDeskripsi()` - menambahkan deskripsi opsinya

### Fleksibilitas Desain

Pendekatan ini membuat sistem menjadi jauh lebih fleksibel. Kita dapat mengombinasikan opsi dalam urutan apa pun saat runtime tanpa perlu membuat class baru untuk setiap kombinasi.

#### Contoh Penggunaan
Jika seorang pengguna ingin memesan dengan tambahan sambal dan kemasan khusus, kode kita tidak perlu mencari class `PesananSambalDanKemasan`. Sebaliknya, kita cukup "membungkus" objeknya secara dinamis:

```java
pesananBaru = new SambalDecorator(
    new KemasanKhususDecorator(
        new Pesanan()
    )
);
```

#### Extensibility
Jika di masa depan kita ingin menambahkan opsi baru (misalnya, `TanpaMsgDecorator`), kita cukup membuat **satu class decorator baru** tanpa perlu mengubah kode di class Pesanan atau decorator lainnya yang sudah ada. 

Ini menyelesaikan masalah "ledakan jumlah kelas" dan membuat kode **lebih mudah dirawat**.

## 3. Desain UML Class Diagram (Setelah Menggunakan Decorator)

Berikut adalah Class Diagram yang telah diperbarui untuk mengimplementasikan Decorator Pattern pada class Pesanan. Diagram ini menambahkan interface IPesanan dan beberapa class decorator baru, yang memungkinkan penambahan opsi secara dinamis.

*[Diagram tidak ditampilkan dalam format teks]*

### Penjelasan Diagram

* **IPesanan** adalah interface utama yang mendefinisikan operasi dasar
* **Pesanan** adalah class konkret yang mengimplementasikan IPesanan - objek dasar yang akan dibungkus
* **PesananDecorator** adalah class abstract pembungkus yang:
  * Mengimplementasikan IPesanan
  * "Memegang" (wraps) objek IPesanan lain
* **SambalDecorator, KemasanKhususDecorator, dan PrioritasDecorator** adalah concrete decorators yang:
  * "Membungkus" Pesanan (atau decorator lain)
  * Menambahkan biaya dan deskripsi baru secara dinamis

## 4. Penjelasan Singkat Alur

Penerapan Decorator Pattern ini mengubah cara sistem menangani opsi tambahan pada pesanan. Alih-alih mencari class yang berbeda untuk setiap kombinasi pesanan, sistem akan menggunakan **proses pembungkusan (wrapping) yang dinamis**.

### Proses Wrapping
1. **Pesanan Dasar:** Saat pengguna menyelesaikan pesanan dasar dan mulai menambahkan opsi "Sambal Tambahan", sistem tidak mengubah objek Pesanan asli
2. **Layer Pertama:** Sistem membuat objek `SambalDecorator` baru dan "membungkus" objek Pesanan di dalamnya
3. **Layer Kedua:** Jika pengguna menambahkan "Kemasan Khusus", sistem membungkus decorator sebelumnya dengan `KemasanKhususDecorator`, menciptakan **tumpukan lapisan**

### Proses Kalkulasi
Ketika sistem perlu menghitung total harga:

1. **Pemanggilan Awal:** Memanggil `hitungTotal()` pada lapisan terluar (`KemasanKhususDecorator`)
2. **Delegasi Bertingkat:** 
   * `KemasanKhususDecorator` → `SambalDecorator`
   * `SambalDecorator` → `Pesanan` (dasar)
3. **Return Bertingkat:** 
   * Pesanan dasar mengembalikan harga awal
   * Setiap lapisan decorator menambahkan biayanya sendiri saat pemanggilan kembali ke atas

**Proses yang sama berlaku untuk `getDeskripsi()`**, di mana setiap lapisan menambahkan deskripsi opsinya sendiri ke deskripsi dasar.
