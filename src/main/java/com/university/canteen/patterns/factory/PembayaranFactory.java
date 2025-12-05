package com.university.canteen.patterns.factory;

import com.university.canteen.patterns.factory.enums.TipePembayaran;

/**
 * Class PembayaranFactory (Factory Method Pattern Implementation)
 * 
 * Class ini mengimplementasikan Factory Method Pattern untuk membuat
 * objek-objek Pembayaran berdasarkan tipe yang diminta.
 * 
 * Factory Pattern membantu dalam:
 * 1. Encapsulation object creation logic
 * 2. Loose coupling antara client dan concrete classes
 * 3. Extensibility untuk menambah tipe pembayaran baru
 * 4. Centralized object creation dengan validasi
 * 
 * Factory ini akan membuat instance yang tepat berdasarkan TipePembayaran:
 * - TUNAI -> PembayaranTunai
 * - QRIS -> PembayaranDigital dengan tipe QRIS
 * - TRANSFER -> PembayaranDigital dengan tipe TRANSFER
 * 
 * Berdasarkan konteks.md, factory ini mendukung berbagai metode pembayaran
 * yang dapat dipilih oleh pengguna sistem kantin universitas.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class PembayaranFactory {
    
    /**
     * Method utama factory untuk membuat objek Pembayaran.
     * 
     * Method ini mengimplementasikan Factory Method Pattern dengan
     * menerima tipe pembayaran dan jumlah, kemudian mengembalikan
     * instance concrete class yang sesuai.
     * 
     * @param tipe enum TipePembayaran yang menentukan jenis pembayaran
     * @param jumlah nominal yang harus dibayar dalam rupiah
     * @return objek Pembayaran yang sesuai dengan tipe yang diminta
     * @throws IllegalArgumentException jika parameter tidak valid
     * @throws UnsupportedOperationException jika tipe tidak didukung
     */
    public static Pembayaran createPembayaran(TipePembayaran tipe, double jumlah) {
        // Validasi input parameters
        if (tipe == null) {
            throw new IllegalArgumentException("Tipe pembayaran tidak boleh null");
        }
        
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pembayaran harus lebih dari nol");
        }
        
        // Factory method logic - return appropriate concrete class
        return switch (tipe) {
            case TUNAI -> {
                // Untuk pembayaran tunai, buat PembayaranTunai
                yield new PembayaranTunai(jumlah);
            }
            case QRIS -> {
                // Untuk pembayaran QRIS, buat PembayaranDigital dengan tipe QRIS
                yield new PembayaranDigital(jumlah, TipePembayaran.QRIS);
            }
            case TRANSFER -> {
                // Untuk pembayaran transfer, buat PembayaranDigital dengan tipe TRANSFER
                yield new PembayaranDigital(jumlah, TipePembayaran.TRANSFER);
            }
            default -> {
                // Untuk tipe yang belum didukung
                throw new UnsupportedOperationException(
                    "Tipe pembayaran " + tipe + " belum didukung oleh sistem");
            }
        };
    }
    
    /**
     * Overloaded method untuk membuat pembayaran digital dengan platform spesifik.
     * 
     * Method ini berguna ketika pengguna sudah memilih platform pembayaran
     * tertentu (misal: "GoPay" untuk QRIS atau "BCA" untuk Transfer).
     * 
     * @param tipe enum TipePembayaran (harus QRIS atau TRANSFER)
     * @param jumlah nominal yang harus dibayar
     * @param platform nama platform pembayaran spesifik
     * @return objek Pembayaran digital dengan platform yang ditentukan
     * @throws IllegalArgumentException jika parameter tidak valid
     * @throws UnsupportedOperationException jika tipe tidak didukung
     */
    public static Pembayaran createPembayaranDigital(TipePembayaran tipe, 
                                                   double jumlah, 
                                                   String platform) {
        // Validasi input parameters
        if (tipe == null) {
            throw new IllegalArgumentException("Tipe pembayaran tidak boleh null");
        }
        
        if (!tipe.isDigital()) {
            throw new IllegalArgumentException("Method ini hanya untuk pembayaran digital (QRIS/TRANSFER)");
        }
        
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pembayaran harus lebih dari nol");
        }
        
        if (platform == null || platform.trim().isEmpty()) {
            throw new IllegalArgumentException("Platform pembayaran tidak boleh kosong");
        }
        
        // Buat pembayaran digital dengan platform spesifik
        return new PembayaranDigital(jumlah, tipe, platform);
    }
    
    /**
     * Method helper untuk membuat pembayaran tunai dengan detail kasir.
     * 
     * @param jumlah nominal yang harus dibayar
     * @param nomorKasir nomor kasir tempat pembayaran
     * @param namaStaf nama staf kasir yang melayani
     * @return objek PembayaranTunai dengan detail kasir
     * @throws IllegalArgumentException jika jumlah tidak valid
     */
    public static PembayaranTunai createPembayaranTunai(double jumlah, 
                                                      String nomorKasir, 
                                                      String namaStaf) {
        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah pembayaran harus lebih dari nol");
        }
        
        return new PembayaranTunai(jumlah, nomorKasir, namaStaf);
    }
    
    /**
     * Method utility untuk mendapatkan daftar tipe pembayaran yang tersedia.
     * 
     * @return array berisi semua TipePembayaran yang didukung
     */
    public static TipePembayaran[] getTipePembayaranTersedia() {
        return TipePembayaran.values();
    }
    
    /**
     * Method untuk validasi apakah tipe pembayaran tertentu didukung.
     * 
     * @param tipe tipe pembayaran yang akan dicek
     * @return true jika didukung, false jika tidak
     */
    public static boolean isTipePembayaranDidukung(TipePembayaran tipe) {
        if (tipe == null) {
            return false;
        }
        
        // Saat ini semua enum value didukung
        return tipe == TipePembayaran.TUNAI || 
               tipe == TipePembayaran.QRIS || 
               tipe == TipePembayaran.TRANSFER;
    }
    
    /**
     * Method utility untuk mendapatkan deskripsi singkat setiap tipe pembayaran.
     * Berguna untuk ditampilkan dalam UI selection.
     * 
     * @return array string berisi deskripsi setiap tipe pembayaran
     */
    public static String[] getDeskripsiTipePembayaran() {
        return new String[]{
            "TUNAI - Pembayaran langsung di kasir dengan uang tunai",
            "QRIS - Pembayaran digital menggunakan QR Code (GoPay, OVO, DANA)",
            "TRANSFER - Pembayaran melalui transfer bank"
        };
    }
    
    /**
     * Method untuk menghitung estimasi waktu proses berdasarkan tipe pembayaran.
     * 
     * @param tipe tipe pembayaran
     * @return estimasi waktu proses dalam menit
     */
    public static int getEstimasiWaktuProses(TipePembayaran tipe) {
        return switch (tipe) {
            case TUNAI -> 1;      // Instant di kasir
            case QRIS -> 2;       // 1-2 menit verifikasi
            case TRANSFER -> 5;   // 3-5 menit verifikasi manual
            default -> 0;
        };
    }
    
    /**
     * Method untuk mendapatkan informasi biaya admin jika ada.
     * Saat ini semua pembayaran gratis biaya admin.
     * 
     * @param tipe tipe pembayaran
     * @return biaya admin dalam rupiah (saat ini selalu 0)
     */
    public static double getBiayaAdmin(TipePembayaran tipe) {
        // Saat ini tidak ada biaya admin untuk semua tipe pembayaran
        return 0.0;
    }
}