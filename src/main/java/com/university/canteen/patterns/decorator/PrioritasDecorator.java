package com.university.canteen.patterns.decorator;

/**
 * Class PrioritasDecorator (Concrete Decorator)
 * 
 * Decorator konkret yang menambahkan opsi "Pesanan Prioritas" pada pesanan.
 * Berdasarkan spesifikasi dari konteks.md, opsi pesanan prioritas akan
 * menambah biaya sebesar Rp 3.000 dan pesanan akan dikerjakan lebih dulu
 * dari antrian normal.
 * 
 * Class ini mengimplementasikan Decorator Pattern dengan cara:
 * 1. Meng-extend PesananDecorator sebagai base decorator
 * 2. Meng-override method hitungTotal() untuk menambah biaya prioritas
 * 3. Meng-override method getDeskripsi() untuk menambah informasi prioritas
 * 
 * Decorator ini dapat dikombinasikan dengan decorator lain (seperti SambalDecorator
 * atau KemasanKhususDecorator) untuk memberikan pengalaman pesanan yang lengkap
 * dan fleksibel sesuai kebutuhan pengguna.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class PrioritasDecorator extends PesananDecorator {
    
    /** Biaya tambahan untuk opsi pesanan prioritas dalam rupiah */
    private static final double BIAYA_PRIORITAS = 3000.0;
    
    /** Deskripsi opsi prioritas yang akan ditambahkan */
    private static final String DESKRIPSI_PRIORITAS = "Pesanan Prioritas (Dikerjakan Lebih Dulu)";
    
    /**
     * Constructor untuk membuat PrioritasDecorator yang membungkus pesanan existing.
     * 
     * @param pesanan objek IPesanan yang akan ditambahkan opsi prioritas
     * @throws IllegalArgumentException jika parameter pesanan null
     */
    public PrioritasDecorator(IPesanan pesanan) {
        super(pesanan);
    }
    
    /**
     * Menghitung total harga pesanan dengan tambahan biaya prioritas.
     * Method ini memanggil hitungTotal() dari objek yang dibungkus,
     * kemudian menambahkan biaya prioritas di atasnya.
     * 
     * @return total harga pesanan termasuk biaya prioritas
     */
    @Override
    public double hitungTotal() {
        double totalDasar = pesananTerbungkus.hitungTotal();
        return totalDasar + BIAYA_PRIORITAS;
    }
    
    /**
     * Mendapatkan deskripsi pesanan dengan informasi prioritas.
     * Method ini mengambil deskripsi dari objek yang dibungkus,
     * kemudian menambahkan informasi tentang pesanan prioritas.
     * 
     * @return deskripsi lengkap pesanan termasuk opsi prioritas
     */
    @Override
    public String getDeskripsi() {
        String deskripsiDasar = pesananTerbungkus.getDeskripsi();
        return deskripsiDasar + " + " + DESKRIPSI_PRIORITAS + " (+" + 
               String.format("Rp %.0f", BIAYA_PRIORITAS) + ")";
    }
    
    /**
     * Mendapatkan biaya tambahan untuk opsi prioritas.
     * Method ini berguna untuk keperluan display atau kalkulasi terpisah.
     * 
     * @return biaya prioritas dalam rupiah
     */
    public static double getBiayaPrioritas() {
        return BIAYA_PRIORITAS;
    }
    
    /**
     * Mendapatkan deskripsi opsi prioritas.
     * 
     * @return string deskripsi pesanan prioritas
     */
    public static String getDeskripsiPrioritas() {
        return DESKRIPSI_PRIORITAS;
    }
}