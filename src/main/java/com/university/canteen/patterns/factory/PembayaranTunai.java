package com.university.canteen.patterns.factory;

import com.university.canteen.patterns.factory.enums.TipePembayaran;

/**
 * Class PembayaranTunai (Concrete Implementation)
 * 
 * Concrete class yang mengimplementasikan pembayaran tunai di kasir.
 * Class ini merupakan salah satu produk dalam Factory Method Pattern
 * yang menangani proses pembayaran secara langsung dengan uang cash.
 * 
 * Karakteristik pembayaran tunai:
 * - Pembayaran dilakukan langsung di kasir
 * - Tidak memerlukan verifikasi digital
 * - Proses instant tanpa delay
 * - Staf kasir langsung mengkonfirmasi pembayaran
 * 
 * Berdasarkan Assumption A02 dari konteks.md: pengguna akan membayar
 * pesanannya di kasir saat pengambilan makanan.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class PembayaranTunai extends Pembayaran {
    
    /** 
     * Nomor kasir tempat pembayaran dilakukan.
     * Berguna untuk tracking dan audit.
     */
    private String nomorKasir;
    
    /** 
     * Nama staf kasir yang melayani.
     * Untuk keperluan record dan accountability.
     */
    private String namaStafKasir;
    
    /**
     * Constructor untuk membuat pembayaran tunai.
     * 
     * @param jumlahBayar nominal yang harus dibayar
     * @throws IllegalArgumentException jika jumlahBayar tidak valid
     */
    public PembayaranTunai(double jumlahBayar) {
        super(jumlahBayar, TipePembayaran.TUNAI);
        this.nomorKasir = "KASIR-01"; // default kasir
        this.namaStafKasir = "Staf Kantin"; // akan diset saat proses
    }
    
    /**
     * Constructor lengkap dengan informasi kasir.
     * 
     * @param jumlahBayar nominal yang harus dibayar
     * @param nomorKasir nomor kasir tempat pembayaran
     * @param namaStafKasir nama staf yang melayani
     */
    public PembayaranTunai(double jumlahBayar, String nomorKasir, String namaStafKasir) {
        super(jumlahBayar, TipePembayaran.TUNAI);
        this.nomorKasir = nomorKasir != null ? nomorKasir : "KASIR-01";
        this.namaStafKasir = namaStafKasir != null ? namaStafKasir : "Staf Kantin";
    }
    
    /**
     * Implementasi proses pembayaran tunai.
     * 
     * Method ini mensimulasikan proses pembayaran tunai di kasir:
     * 1. Menampilkan instruksi kepada pengguna
     * 2. Mencatat detail transaksi
     * 3. Memberikan konfirmasi pembayaran
     * 
     * @return string berisi hasil dan instruksi proses pembayaran
     */
    @Override
    public String prosesPembayaran() {
        StringBuilder hasil = new StringBuilder();
        
        hasil.append("=== PROSES PEMBAYARAN TUNAI ===\n");
        hasil.append(String.format("ID Pembayaran: %s\n", getIdPembayaran()));
        hasil.append(String.format("Jumlah yang harus dibayar: Rp %.0f\n", getJumlahBayar()));
        hasil.append(String.format("Kasir: %s\n", nomorKasir));
        hasil.append(String.format("Petugas: %s\n", namaStafKasir));
        hasil.append("\n");
        hasil.append("INSTRUKSI:\n");
        hasil.append("1. Datang ke kasir dengan menunjukkan nomor antrean\n");
        hasil.append("2. Sebutkan ID Pembayaran kepada petugas kasir\n");
        hasil.append("3. Bayar dengan uang tunai sesuai nominal\n");
        hasil.append("4. Simpan struk pembayaran sebagai bukti\n");
        hasil.append("5. Tunggu pesanan selesai untuk pengambilan\n");
        hasil.append("\n");
        hasil.append("Status: MENUNGGU PEMBAYARAN DI KASIR\n");
        hasil.append("Terima kasih telah menggunakan layanan kantin universitas!");
        
        return hasil.toString();
    }
    
    /**
     * Method khusus untuk konfirmasi pembayaran oleh kasir.
     * Method ini akan dipanggil ketika kasir mengkonfirmasi pembayaran telah diterima.
     * 
     * @param uangDiterima jumlah uang yang diterima dari pembeli
     * @return string konfirmasi dengan kembalian jika ada
     */
    public String konfirmasiPembayaranKasir(double uangDiterima) {
        if (uangDiterima < getJumlahBayar()) {
            return String.format("ERROR: Uang tidak mencukupi. Kurang Rp %.0f", 
                               getJumlahBayar() - uangDiterima);
        }
        
        double kembalian = uangDiterima - getJumlahBayar();
        StringBuilder konfirmasi = new StringBuilder();
        
        konfirmasi.append("=== PEMBAYARAN BERHASIL ===\n");
        konfirmasi.append(String.format("ID Pembayaran: %s\n", getIdPembayaran()));
        konfirmasi.append(String.format("Jumlah tagihan: Rp %.0f\n", getJumlahBayar()));
        konfirmasi.append(String.format("Uang diterima: Rp %.0f\n", uangDiterima));
        
        if (kembalian > 0) {
            konfirmasi.append(String.format("Kembalian: Rp %.0f\n", kembalian));
        } else {
            konfirmasi.append("Uang pas, tidak ada kembalian\n");
        }
        
        konfirmasi.append(String.format("Waktu pembayaran: %s\n", getFormattedDate()));
        konfirmasi.append("Pembayaran telah dikonfirmasi oleh kasir.");
        
        return konfirmasi.toString();
    }
    
    /**
     * Override method isProcessed untuk pembayaran tunai.
     * Pembayaran tunai dianggap belum selesai sampai dikonfirmasi kasir.
     * 
     * @return false karena memerlukan konfirmasi manual di kasir
     */
    @Override
    public boolean isProcessed() {
        return false; // Memerlukan konfirmasi manual di kasir
    }
    
    // Getter methods
    public String getNomorKasir() {
        return nomorKasir;
    }
    
    public String getNamaStafKasir() {
        return namaStafKasir;
    }
    
    // Setter methods
    public void setNomorKasir(String nomorKasir) {
        this.nomorKasir = nomorKasir;
    }
    
    public void setNamaStafKasir(String namaStafKasir) {
        this.namaStafKasir = namaStafKasir;
    }
    
    /**
     * Override toString untuk informasi lengkap pembayaran tunai.
     * 
     * @return representasi string dengan detail kasir
     */
    @Override
    public String toString() {
        return String.format("PembayaranTunai{id='%s', jumlah=Rp%.0f, kasir='%s', petugas='%s', tanggal=%s}", 
                           getIdPembayaran(), getJumlahBayar(), nomorKasir, 
                           namaStafKasir, getFormattedDate());
    }
}