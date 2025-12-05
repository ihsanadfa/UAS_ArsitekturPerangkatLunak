package com.university.canteen.patterns.decorator;

/**
 * Abstract Class PesananDecorator (Base Decorator)
 * 
 * Class abstract ini adalah dasar dari semua decorator dalam Decorator Pattern.
 * Class ini mengimplementasikan interface IPesanan dan memiliki referensi
 * ke objek IPesanan lain yang akan dibungkusnya (wrappee).
 * 
 * Fungsi utama class ini adalah:
 * 1. Menyimpan referensi ke objek yang dibungkus
 * 2. Mendelegasikan semua method call ke objek yang dibungkus
 * 3. Menyediakan struktur dasar untuk concrete decorator
 * 
 * Concrete decorator akan meng-extend class ini dan meng-override
 * method-method untuk menambahkan fungsionalitas tambahan.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public abstract class PesananDecorator implements IPesanan {
    
    /** 
     * Referensi ke objek IPesanan yang dibungkus oleh decorator ini.
     * Bisa berupa objek Pesanan dasar atau decorator lain.
     */
    protected IPesanan pesananTerbungkus;
    
    /**
     * Constructor untuk menginisialisasi decorator dengan objek yang akan dibungkus.
     * 
     * @param pesanan objek IPesanan yang akan dibungkus/didekorasi
     * @throws IllegalArgumentException jika parameter pesanan null
     */
    public PesananDecorator(IPesanan pesanan) {
        if (pesanan == null) {
            throw new IllegalArgumentException("Pesanan yang akan dibungkus tidak boleh null");
        }
        this.pesananTerbungkus = pesanan;
    }
    
    /**
     * Implementasi default method hitungTotal() yang mendelegasikan
     * ke objek yang dibungkus. Concrete decorator akan meng-override
     * method ini untuk menambahkan biaya tambahan.
     * 
     * @return total harga dari objek yang dibungkus
     */
    @Override
    public double hitungTotal() {
        return pesananTerbungkus.hitungTotal();
    }
    
    /**
     * Implementasi default method getDeskripsi() yang mendelegasikan
     * ke objek yang dibungkus. Concrete decorator akan meng-override
     * method ini untuk menambahkan deskripsi tambahan.
     * 
     * @return deskripsi dari objek yang dibungkus
     */
    @Override
    public String getDeskripsi() {
        return pesananTerbungkus.getDeskripsi();
    }
    
    /**
     * Mendapatkan referensi ke objek yang dibungkus.
     * Method ini berguna untuk debugging atau testing.
     * 
     * @return objek IPesanan yang dibungkus
     */
    protected IPesanan getPesananTerbungkus() {
        return pesananTerbungkus;
    }
}