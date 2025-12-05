package com.university.canteen.patterns.factory;

import com.university.canteen.patterns.factory.enums.TipePembayaran;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract Class Pembayaran
 * 
 * Class abstract yang menjadi base class untuk semua jenis pembayaran
 * dalam sistem kantin universitas. Class ini mendefinisikan struktur
 * dan behavior dasar yang harus dimiliki oleh setiap tipe pembayaran.
 * 
 * Class ini merupakan bagian dari Factory Method Pattern, dimana
 * concrete subclass akan mengimplementasikan method abstract sesuai
 * dengan karakteristik masing-masing tipe pembayaran.
 * 
 * Atribut yang disimpan:
 * - idPembayaran: identifier unik untuk setiap transaksi pembayaran
 * - jumlahBayar: nominal yang harus dibayar
 * - tanggal: timestamp kapan pembayaran dilakukan
 * - tipePembayaran: jenis pembayaran yang dipilih
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Data
public abstract class Pembayaran {
    
    /** 
     * Identifier unik untuk setiap transaksi pembayaran.
     * Format: PAY-YYYYMMDD-HHMMSS-XXX (XXX = counter)
     */
    protected String idPembayaran;
    
    /** 
     * Jumlah nominal yang harus dibayar dalam rupiah.
     * Berdasarkan OCL: jumlah pembayaran tidak boleh bernilai negatif.
     */
    protected double jumlahBayar;
    
    /** 
     * Timestamp kapan pembayaran dibuat/diproses.
     * Menggunakan LocalDateTime untuk kemudahan manipulasi waktu.
     */
    protected LocalDateTime tanggal;
    
    /** 
     * Tipe pembayaran yang dipilih oleh pengguna.
     * Digunakan untuk menentukan flow proses pembayaran.
     */
    protected TipePembayaran tipePembayaran;
    
    /**
     * Constructor untuk membuat objek pembayaran dengan parameter dasar.
     * 
     * @param jumlahBayar nominal yang harus dibayar
     * @param tipePembayaran jenis pembayaran yang dipilih
     * @throws IllegalArgumentException jika jumlahBayar negatif atau nol
     */
    protected Pembayaran(double jumlahBayar, TipePembayaran tipePembayaran) {
        // Validasi berdasarkan OCL: jumlah pembayaran tidak boleh negatif
        if (jumlahBayar <= 0) {
            throw new IllegalArgumentException("Jumlah pembayaran harus lebih dari nol");
        }
        
        this.jumlahBayar = jumlahBayar;
        this.tipePembayaran = tipePembayaran;
        this.tanggal = LocalDateTime.now();
        this.idPembayaran = generateIdPembayaran();
    }
    
    /**
     * Method abstract yang harus diimplementasikan oleh setiap concrete class.
     * Setiap tipe pembayaran memiliki proses yang berbeda, sehingga
     * implementasinya diserahkan kepada subclass masing-masing.
     * 
     * @return String berisi hasil proses pembayaran (sukses/gagal/pending)
     */
    public abstract String prosesPembayaran();
    
    /**
     * Method untuk generate ID pembayaran yang unik.
     * Format: PAY-YYYYMMDD-HHMMSS-XXX
     * 
     * @return string ID pembayaran yang unik
     */
    private String generateIdPembayaran() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String timestamp = tanggal.format(formatter);
        
        // Tambahkan random 3 digit untuk memastikan uniqueness
        int randomSuffix = (int) (Math.random() * 1000);
        
        return String.format("PAY-%s-%03d", timestamp, randomSuffix);
    }
    
    /**
     * Method untuk mendapatkan informasi ringkas pembayaran.
     * 
     * @return string ringkasan pembayaran
     */
    public String getRingkasan() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("ID: %s | Tipe: %s | Jumlah: Rp%.0f | Tanggal: %s", 
                           idPembayaran, 
                           tipePembayaran.getDisplayName(), 
                           jumlahBayar, 
                           tanggal.format(displayFormatter));
    }
    
    /**
     * Method untuk validasi apakah pembayaran sudah diproses.
     * Default implementation bisa di-override oleh subclass jika diperlukan.
     * 
     * @return true jika pembayaran sudah diproses
     */
    public boolean isProcessed() {
        // Default: anggap sudah diproses jika object sudah dibuat
        return true;
    }
    
    /**
     * Method helper untuk format tanggal ke string.
     * 
     * @return tanggal dalam format yang mudah dibaca
     */
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss");
        return tanggal.format(formatter);
    }
    
    /**
     * Override toString() untuk keperluan debugging dan logging.
     * 
     * @return representasi string dari objek Pembayaran
     */
    @Override
    public String toString() {
        return String.format("Pembayaran{id='%s', tipe=%s, jumlah=Rp%.0f, tanggal=%s}", 
                           idPembayaran, tipePembayaran, jumlahBayar, getFormattedDate());
    }
}