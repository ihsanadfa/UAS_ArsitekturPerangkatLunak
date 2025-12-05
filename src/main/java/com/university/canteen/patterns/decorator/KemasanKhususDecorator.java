package com.university.canteen.patterns.decorator;

/**
 * Class KemasanKhususDecorator (Concrete Decorator)
 * 
 * Decorator konkret yang menambahkan opsi "Kemasan Khusus" pada pesanan.
 * Berdasarkan spesifikasi dari konteks.md, opsi kemasan khusus akan
 * menambah biaya sebesar Rp 2.000 dan makanan akan dibungkus terpisah
 * dengan kemasan yang lebih baik.
 * 
 * Class ini mengimplementasikan Decorator Pattern dengan cara:
 * 1. Meng-extend PesananDecorator sebagai base decorator
 * 2. Meng-override method hitungTotal() untuk menambah biaya kemasan khusus
 * 3. Meng-override method getDeskripsi() untuk menambah informasi kemasan
 * 
 * Decorator ini dapat dikombinasikan dengan decorator lain (seperti SambalDecorator
 * atau PrioritasDecorator) untuk memberikan fleksibilitas dalam kustomisasi pesanan.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class KemasanKhususDecorator extends PesananDecorator {
    
    /** Biaya tambahan untuk opsi kemasan khusus dalam rupiah */
    private static final double BIAYA_KEMASAN_KHUSUS = 2000.0;
    
    /** Deskripsi opsi kemasan khusus yang akan ditambahkan */
    private static final String DESKRIPSI_KEMASAN = "Kemasan Khusus (Dibungkus Terpisah)";
    
    /**
     * Constructor untuk membuat KemasanKhususDecorator yang membungkus pesanan existing.
     * 
     * @param pesanan objek IPesanan yang akan ditambahkan opsi kemasan khusus
     * @throws IllegalArgumentException jika parameter pesanan null
     */
    public KemasanKhususDecorator(IPesanan pesanan) {
        super(pesanan);
    }
    
    /**
     * Menghitung total harga pesanan dengan tambahan biaya kemasan khusus.
     * Method ini memanggil hitungTotal() dari objek yang dibungkus,
     * kemudian menambahkan biaya kemasan khusus di atasnya.
     * 
     * @return total harga pesanan termasuk biaya kemasan khusus
     */
    @Override
    public double hitungTotal() {
        double totalDasar = pesananTerbungkus.hitungTotal();
        return totalDasar + BIAYA_KEMASAN_KHUSUS;
    }
    
    /**
     * Mendapatkan deskripsi pesanan dengan informasi kemasan khusus.
     * Method ini mengambil deskripsi dari objek yang dibungkus,
     * kemudian menambahkan informasi tentang kemasan khusus.
     * 
     * @return deskripsi lengkap pesanan termasuk opsi kemasan khusus
     */
    @Override
    public String getDeskripsi() {
        String deskripsiDasar = pesananTerbungkus.getDeskripsi();
        return deskripsiDasar + " + " + DESKRIPSI_KEMASAN + " (+" + 
               String.format("Rp %.0f", BIAYA_KEMASAN_KHUSUS) + ")";
    }
    
    /**
     * Mendapatkan biaya tambahan untuk opsi kemasan khusus.
     * Method ini berguna untuk keperluan display atau kalkulasi terpisah.
     * 
     * @return biaya kemasan khusus dalam rupiah
     */
    public static double getBiayaKemasanKhusus() {
        return BIAYA_KEMASAN_KHUSUS;
    }
    
    /**
     * Mendapatkan deskripsi opsi kemasan khusus.
     * 
     * @return string deskripsi kemasan khusus
     */
    public static String getDeskripsiKemasan() {
        return DESKRIPSI_KEMASAN;
    }
}