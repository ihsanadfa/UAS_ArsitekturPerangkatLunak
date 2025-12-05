package com.university.canteen.patterns.factory.enums;

/**
 * Enum TipePembayaran
 * 
 * Enumerasi yang mendefinisikan berbagai tipe pembayaran yang tersedia
 * dalam sistem pemesanan kantin universitas.
 * 
 * Enum ini digunakan dalam Factory Method Pattern untuk menentukan
 * jenis objek Pembayaran yang akan dibuat berdasarkan pilihan pengguna.
 * 
 * Tipe pembayaran yang didukung:
 * - TUNAI: Pembayaran secara langsung di kasir dengan uang tunai
 * - QRIS: Pembayaran digital menggunakan QR Code (GoPay, OVO, DANA, dll.)
 * - TRANSFER: Pembayaran melalui transfer bank
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public enum TipePembayaran {
    
    /**
     * Pembayaran tunai di kasir.
     * Pengguna membayar langsung dengan uang cash kepada staf kantin.
     */
    TUNAI("Tunai", "Pembayaran dengan uang tunai di kasir"),
    
    /**
     * Pembayaran digital menggunakan QRIS.
     * Pengguna scan QR code untuk pembayaran melalui e-wallet.
     */
    QRIS("QRIS", "Pembayaran digital menggunakan QR Code"),
    
    /**
     * Pembayaran melalui transfer bank.
     * Pengguna melakukan transfer ke rekening kantin.
     */
    TRANSFER("Transfer Bank", "Pembayaran melalui transfer bank");
    
    /** Nama tampilan untuk tipe pembayaran */
    private final String displayName;
    
    /** Deskripsi detail tipe pembayaran */
    private final String deskripsi;
    
    /**
     * Constructor untuk enum TipePembayaran.
     * 
     * @param displayName nama yang akan ditampilkan kepada pengguna
     * @param deskripsi deskripsi detail tipe pembayaran
     */
    TipePembayaran(String displayName, String deskripsi) {
        this.displayName = displayName;
        this.deskripsi = deskripsi;
    }
    
    /**
     * Mendapatkan nama tampilan tipe pembayaran.
     * 
     * @return nama yang user-friendly
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Mendapatkan deskripsi detail tipe pembayaran.
     * 
     * @return deskripsi lengkap tipe pembayaran
     */
    public String getDeskripsi() {
        return deskripsi;
    }
    
    /**
     * Mengecek apakah tipe pembayaran adalah digital (bukan tunai).
     * 
     * @return true jika QRIS atau TRANSFER, false jika TUNAI
     */
    public boolean isDigital() {
        return this == QRIS || this == TRANSFER;
    }
    
    /**
     * Mengecek apakah tipe pembayaran memerlukan konfirmasi manual.
     * 
     * @return true jika TRANSFER, false untuk yang lain
     */
    public boolean requiresManualConfirmation() {
        return this == TRANSFER;
    }
    
    /**
     * Override toString untuk kemudahan display.
     * 
     * @return display name dari tipe pembayaran
     */
    @Override
    public String toString() {
        return displayName;
    }
}