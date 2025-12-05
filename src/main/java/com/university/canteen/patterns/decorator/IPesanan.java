package com.university.canteen.patterns.decorator;

/**
 * Interface IPesanan
 * 
 * Interface dasar untuk semua komponen pesanan dalam Decorator Pattern.
 * Interface ini mendefinisikan operasi-operasi utama yang harus dimiliki
 * oleh pesanan dasar maupun decorator yang membungkusnya.
 * 
 * Berdasarkan analisis dari konteks.md, interface ini menyediakan:
 * - Method untuk menghitung total harga pesanan
 * - Method untuk mendapatkan deskripsi lengkap pesanan
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public interface IPesanan {
    
    /**
     * Menghitung total harga pesanan termasuk semua tambahan yang dipilih.
     * 
     * @return total harga pesanan dalam bentuk double
     */
    double hitungTotal();
    
    /**
     * Mendapatkan deskripsi lengkap pesanan termasuk semua opsi tambahan.
     * 
     * @return string yang berisi deskripsi lengkap pesanan
     */
    String getDeskripsi();
}