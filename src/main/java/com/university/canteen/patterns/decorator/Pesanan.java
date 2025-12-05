package com.university.canteen.patterns.decorator;

/**
 * Class Pesanan (Concrete Component)
 * 
 * Class ini adalah implementasi konkret dari interface IPesanan.
 * Ini adalah objek pesanan dasar yang akan "dibungkus" atau "didekorasi"
 * dengan berbagai opsi tambahan menggunakan Decorator Pattern.
 * 
 * Class ini menyimpan informasi dasar pesanan seperti nama menu dan harga dasar,
 * tanpa tambahan opsi apapun. Decorator akan menambahkan fungsionalitas
 * tambahan di atas objek ini.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class Pesanan implements IPesanan {
    
    /** Nama menu yang dipesan */
    private String namaMenu;
    
    /** Harga dasar menu tanpa tambahan apapun */
    private double hargaDasar;
    
    /**
     * Constructor untuk membuat pesanan dengan nama menu dan harga dasar.
     * 
     * @param namaMenu nama menu yang dipesan
     * @param hargaDasar harga dasar menu dalam rupiah
     */
    public Pesanan(String namaMenu, double hargaDasar) {
        this.namaMenu = namaMenu;
        this.hargaDasar = hargaDasar;
    }
    
    /**
     * Menghitung total harga pesanan dasar (tanpa tambahan).
     * 
     * @return harga dasar pesanan
     */
    @Override
    public double hitungTotal() {
        return hargaDasar;
    }
    
    /**
     * Mendapatkan deskripsi pesanan dasar.
     * 
     * @return deskripsi berisi nama menu dan harga dasar
     */
    @Override
    public String getDeskripsi() {
        return namaMenu + " (Rp " + String.format("%.0f", hargaDasar) + ")";
    }
    
    // Getter methods untuk akses ke atribut privat
    
    /**
     * Mendapatkan nama menu.
     * 
     * @return nama menu yang dipesan
     */
    public String getNamaMenu() {
        return namaMenu;
    }
    
    /**
     * Mendapatkan harga dasar menu.
     * 
     * @return harga dasar dalam rupiah
     */
    public double getHargaDasar() {
        return hargaDasar;
    }
    
    /**
     * Mengatur nama menu baru.
     * 
     * @param namaMenu nama menu baru
     */
    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }
    
    /**
     * Mengatur harga dasar baru.
     * 
     * @param hargaDasar harga dasar baru dalam rupiah
     */
    public void setHargaDasar(double hargaDasar) {
        this.hargaDasar = hargaDasar;
    }
}