package com.university.canteen.patterns.factory;

import com.university.canteen.patterns.factory.enums.TipePembayaran;
import java.util.UUID;

/**
 * Class PembayaranDigital (Concrete Implementation)
 * 
 * Concrete class yang mengimplementasikan pembayaran digital untuk
 * sistem kantin universitas. Class ini menangani pembayaran melalui
 * QRIS dan Transfer Bank sebagai bagian dari Factory Method Pattern.
 * 
 * Karakteristik pembayaran digital:
 * - Menggunakan teknologi digital (QR Code atau transfer)
 * - Memerlukan verifikasi otomatis atau manual
 * - Dapat diproses secara real-time atau batch
 * - Menyediakan reference number untuk tracking
 * 
 * Tipe yang didukung:
 * - QRIS: Pembayaran via QR Code (GoPay, OVO, DANA, dll.)
 * - TRANSFER: Pembayaran via transfer bank
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class PembayaranDigital extends Pembayaran {
    
    /** 
     * Nomor referensi untuk tracking pembayaran digital.
     * Digunakan untuk verifikasi dan reconciliation.
     */
    private String nomorReferensi;
    
    /** 
     * Informasi platform atau bank yang digunakan.
     * Contoh: "GoPay", "OVO", "BCA", "Mandiri"
     */
    private String platformPembayaran;
    
    /** 
     * Status verifikasi pembayaran digital.
     * true = sudah diverifikasi, false = menunggu verifikasi
     */
    private boolean statusVerifikasi;
    
    /**
     * Constructor untuk membuat pembayaran digital dengan tipe tertentu.
     * 
     * @param jumlahBayar nominal yang harus dibayar
     * @param tipePembayaran QRIS atau TRANSFER
     * @throws IllegalArgumentException jika tipe bukan digital atau jumlah tidak valid
     */
    public PembayaranDigital(double jumlahBayar, TipePembayaran tipePembayaran) {
        super(jumlahBayar, tipePembayaran);
        
        // Validasi bahwa tipe pembayaran adalah digital
        if (!tipePembayaran.isDigital()) {
            throw new IllegalArgumentException("Tipe pembayaran harus QRIS atau TRANSFER");
        }
        
        this.nomorReferensi = generateNomorReferensi();
        this.platformPembayaran = getDefaultPlatform(tipePembayaran);
        this.statusVerifikasi = false; // default belum diverifikasi
    }
    
    /**
     * Constructor lengkap dengan platform spesifik.
     * 
     * @param jumlahBayar nominal yang harus dibayar
     * @param tipePembayaran QRIS atau TRANSFER
     * @param platformPembayaran nama platform/bank yang digunakan
     */
    public PembayaranDigital(double jumlahBayar, TipePembayaran tipePembayaran, 
                           String platformPembayaran) {
        super(jumlahBayar, tipePembayaran);
        
        if (!tipePembayaran.isDigital()) {
            throw new IllegalArgumentException("Tipe pembayaran harus QRIS atau TRANSFER");
        }
        
        this.nomorReferensi = generateNomorReferensi();
        this.platformPembayaran = platformPembayaran != null ? 
                                 platformPembayaran : getDefaultPlatform(tipePembayaran);
        this.statusVerifikasi = false;
    }
    
    /**
     * Implementasi proses pembayaran digital.
     * 
     * Method ini mensimulasikan proses pembayaran digital:
     * 1. Generate instruksi sesuai tipe pembayaran
     * 2. Menyediakan informasi yang diperlukan (QR, rekening, dll)
     * 3. Memberikan status dan langkah selanjutnya
     * 
     * @return string berisi hasil dan instruksi proses pembayaran
     */
    @Override
    public String prosesPembayaran() {
        StringBuilder hasil = new StringBuilder();
        
        hasil.append("=== PROSES PEMBAYARAN DIGITAL ===\n");
        hasil.append(String.format("ID Pembayaran: %s\n", getIdPembayaran()));
        hasil.append(String.format("Tipe: %s\n", getTipePembayaran().getDisplayName()));
        hasil.append(String.format("Jumlah: Rp %.0f\n", getJumlahBayar()));
        hasil.append(String.format("Platform: %s\n", platformPembayaran));
        hasil.append(String.format("Nomor Referensi: %s\n", nomorReferensi));
        hasil.append("\n");
        
        if (getTipePembayaran() == TipePembayaran.QRIS) {
            hasil.append(prosesQRIS());
        } else if (getTipePembayaran() == TipePembayaran.TRANSFER) {
            hasil.append(prosesTransfer());
        }
        
        hasil.append("\n");
        hasil.append("PENTING:\n");
        hasil.append("- Simpan nomor referensi untuk verifikasi\n");
        hasil.append("- Pembayaran akan diverifikasi secara otomatis\n");
        hasil.append("- Hubungi staf jika ada kendala\n");
        hasil.append("\n");
        hasil.append("Status: MENUNGGU VERIFIKASI PEMBAYARAN");
        
        return hasil.toString();
    }
    
    /**
     * Method khusus untuk proses pembayaran QRIS.
     * 
     * @return instruksi pembayaran QRIS
     */
    private String prosesQRIS() {
        StringBuilder qris = new StringBuilder();
        
        qris.append("INSTRUKSI PEMBAYARAN QRIS:\n");
        qris.append("1. Buka aplikasi e-wallet (GoPay, OVO, DANA, ShopeePay)\n");
        qris.append("2. Pilih fitur 'Scan QR' atau 'Bayar QR'\n");
        qris.append("3. Scan QR Code yang ditampilkan di kasir\n");
        qris.append("4. Pastikan nominal sesuai: Rp " + String.format("%.0f", getJumlahBayar()) + "\n");
        qris.append("5. Konfirmasi pembayaran di aplikasi\n");
        qris.append("6. Screenshot bukti pembayaran\n");
        qris.append("7. Tunjukkan ke staf untuk verifikasi\n");
        
        // Simulasi QR Code (dalam implementasi nyata ini akan generate QR image)
        qris.append("\n");
        qris.append("QR CODE: [QR_" + nomorReferensi + "]\n");
        qris.append("(Tunjukkan QR Code ini kepada staf untuk di-scan)\n");
        
        return qris.toString();
    }
    
    /**
     * Method khusus untuk proses pembayaran transfer.
     * 
     * @return instruksi pembayaran transfer
     */
    private String prosesTransfer() {
        StringBuilder transfer = new StringBuilder();
        
        transfer.append("INSTRUKSI PEMBAYARAN TRANSFER:\n");
        transfer.append("1. Buka aplikasi mobile banking atau internet banking\n");
        transfer.append("2. Pilih menu 'Transfer'\n");
        transfer.append("3. Transfer ke rekening kantin:\n");
        transfer.append("   Bank: BNI\n");
        transfer.append("   No. Rekening: 1234567890\n");
        transfer.append("   Atas Nama: Kantin Universitas Syiah Kuala\n");
        transfer.append("4. Nominal: Rp " + String.format("%.0f", getJumlahBayar()) + "\n");
        transfer.append("5. Keterangan: " + getIdPembayaran() + "\n");
        transfer.append("6. Konfirmasi dan lakukan transfer\n");
        transfer.append("7. Screenshot bukti transfer\n");
        transfer.append("8. Tunjukkan ke staf untuk verifikasi\n");
        
        return transfer.toString();
    }
    
    /**
     * Method untuk verifikasi pembayaran digital.
     * 
     * @param referensiExternal nomor referensi dari platform pembayaran
     * @return string konfirmasi verifikasi
     */
    public String verifikasiPembayaran(String referensiExternal) {
        this.statusVerifikasi = true;
        
        StringBuilder konfirmasi = new StringBuilder();
        konfirmasi.append("=== PEMBAYARAN TERVERIFIKASI ===\n");
        konfirmasi.append(String.format("ID Pembayaran: %s\n", getIdPembayaran()));
        konfirmasi.append(String.format("Tipe: %s\n", getTipePembayaran().getDisplayName()));
        konfirmasi.append(String.format("Platform: %s\n", platformPembayaran));
        konfirmasi.append(String.format("Jumlah: Rp %.0f\n", getJumlahBayar()));
        konfirmasi.append(String.format("Ref Internal: %s\n", nomorReferensi));
        konfirmasi.append(String.format("Ref External: %s\n", referensiExternal));
        konfirmasi.append(String.format("Waktu Verifikasi: %s\n", getFormattedDate()));
        konfirmasi.append("\nPembayaran berhasil diverifikasi!");
        
        return konfirmasi.toString();
    }
    
    /**
     * Override method isProcessed untuk pembayaran digital.
     * 
     * @return status verifikasi pembayaran
     */
    @Override
    public boolean isProcessed() {
        return statusVerifikasi;
    }
    
    /**
     * Generate nomor referensi unik untuk tracking.
     * 
     * @return string nomor referensi
     */
    private String generateNomorReferensi() {
        String prefix = getTipePembayaran() == TipePembayaran.QRIS ? "QR" : "TF";
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + uuid;
    }
    
    /**
     * Mendapatkan platform default berdasarkan tipe pembayaran.
     * 
     * @param tipe tipe pembayaran
     * @return nama platform default
     */
    private String getDefaultPlatform(TipePembayaran tipe) {
        return switch (tipe) {
            case QRIS -> "QRIS Universal";
            case TRANSFER -> "Bank Transfer";
            default -> "Digital Payment";
        };
    }
    
    // Getter dan Setter methods
    public String getNomorReferensi() {
        return nomorReferensi;
    }
    
    public String getPlatformPembayaran() {
        return platformPembayaran;
    }
    
    public void setPlatformPembayaran(String platformPembayaran) {
        this.platformPembayaran = platformPembayaran;
    }
    
    public boolean isStatusVerifikasi() {
        return statusVerifikasi;
    }
    
    /**
     * Override toString untuk informasi lengkap pembayaran digital.
     * 
     * @return representasi string dengan detail digital payment
     */
    @Override
    public String toString() {
        String status = statusVerifikasi ? "VERIFIED" : "PENDING";
        return String.format("PembayaranDigital{id='%s', tipe=%s, platform='%s', " +
                           "jumlah=Rp%.0f, ref='%s', status='%s', tanggal=%s}", 
                           getIdPembayaran(), getTipePembayaran(), platformPembayaran,
                           getJumlahBayar(), nomorReferensi, status, getFormattedDate());
    }
}