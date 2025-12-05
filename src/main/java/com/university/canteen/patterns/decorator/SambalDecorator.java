package com.university.canteen.patterns.decorator;

/**
 * Class SambalDecorator (Concrete Decorator)
 * 
 * Decorator konkret yang menambahkan opsi "Sambal Tambahan" pada pesanan.
 * Berdasarkan spesifikasi dari konteks.md, opsi sambal tambahan akan
 * menambah biaya sebesar Rp 1.500 pada total harga pesanan.
 * 
 * Class ini mengimplementasikan Decorator Pattern dengan cara:
 * 1. Meng-extend PesananDecorator sebagai base decorator
 * 2. Meng-override method hitungTotal() untuk menambah biaya sambal
 * 3. Meng-override method getDeskripsi() untuk menambah informasi sambal
 * 
 * Decorator ini dapat dikombinasikan dengan decorator lain secara dinamis
 * untuk memberikan fleksibilitas dalam penambahan opsi pesanan.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class SambalDecorator extends PesananDecorator {
    
    /** Biaya tambahan untuk opsi sambal dalam rupiah */
    private static final double BIAYA_SAMBAL = 1500.0;
    
    /** Deskripsi opsi sambal yang akan ditambahkan */
    private static final String DESKRIPSI_SAMBAL = "Sambal Tambahan";
    
    /**
     * Constructor untuk membuat SambalDecorator yang membungkus pesanan existing.
     * 
     * @param pesanan objek IPesanan yang akan ditambahkan opsi sambal
     * @throws IllegalArgumentException jika parameter pesanan null
     */
    public SambalDecorator(IPesanan pesanan) {
        super(pesanan);
    }
    
    /**
     * Menghitung total harga pesanan dengan tambahan biaya sambal.
     * Method ini memanggil hitungTotal() dari objek yang dibungkus,
     * kemudian menambahkan biaya sambal di atasnya.
     * 
     * @return total harga pesanan termasuk biaya sambal tambahan
     */
    @Override
    public double hitungTotal() {
        double totalDasar = pesananTerbungkus.hitungTotal();
        return totalDasar + BIAYA_SAMBAL;
    }
    
    /**
     * Mendapatkan deskripsi pesanan dengan informasi sambal tambahan.
     * Method ini mengambil deskripsi dari objek yang dibungkus,
     * kemudian menambahkan informasi tentang sambal tambahan.
     * 
     * @return deskripsi lengkap pesanan termasuk opsi sambal
     */
    @Override
    public String getDeskripsi() {
        String deskripsiDasar = pesananTerbungkus.getDeskripsi();
        return deskripsiDasar + " + " + DESKRIPSI_SAMBAL + " (+" + 
               String.format("Rp %.0f", BIAYA_SAMBAL) + ")";
    }
    
    /**
     * Mendapatkan biaya tambahan untuk opsi sambal.
     * Method ini berguna untuk keperluan display atau kalkulasi terpisah.
     * 
     * @return biaya sambal dalam rupiah
     */
    public static double getBiayaSambal() {
        return BIAYA_SAMBAL;
    }
    
    /**
     * Mendapatkan deskripsi opsi sambal.
     * 
     * @return string deskripsi sambal tambahan
     */
    public static String getDeskripsiSambal() {
        return DESKRIPSI_SAMBAL;
    }
}