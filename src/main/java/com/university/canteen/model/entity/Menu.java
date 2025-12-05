package com.university.canteen.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity Menu
 * 
 * Class entity yang merepresentasikan item menu dalam sistem kantin universitas.
 * Class ini menyimpan informasi lengkap tentang setiap menu yang tersedia,
 * termasuk identitas, nama, harga, kategori, dan status ketersediaannya.
 * 
 * Berdasarkan analisis dari konteks.md, class ini mendukung:
 * - Requirement R01: Menampilkan daftar menu dengan harga
 * - Requirement R04: Update status ketersediaan menu
 * - Fact F01: Setiap item menu memiliki satu harga yang pasti
 * - Fact F03: Makanan yang sudah habis tidak dapat dipesan lagi
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    
    /** 
     * Identitas unik untuk setiap menu.
     * Digunakan sebagai primary key dalam sistem.
     */
    @Id
    @Column(name = "id", length = 20)
    private String id;
    
    /** 
     * Nama menu yang akan ditampilkan kepada pengguna.
     * Contoh: "Nasi Gudeg", "Es Teh Manis", "Ayam Geprek"
     */
    @Column(name = "nama_menu", nullable = false, length = 100)
    private String namaMenu;
    
    /** 
     * Harga menu dalam rupiah.
     * Sesuai dengan Fact F01: setiap item menu memiliki satu harga yang pasti.
     */
    @Column(name = "harga", nullable = false)
    private double harga;
    
    /** 
     * Kategori menu untuk memudahkan pengelompokan.
     * Contoh: "Makanan Utama", "Minuman", "Cemilan", "Dessert"
     */
    @Column(name = "kategori", nullable = false, length = 50)
    private String kategori;
    
    /** 
     * Status ketersediaan menu.
     * true = tersedia untuk dipesan
     * false = habis/tidak tersedia
     * 
     * Sesuai dengan Fact F03: makanan yang sudah habis tidak dapat dipesan lagi.
     */
    @Column(name = "tersedia", nullable = false)
    private boolean tersedia;
    
    /**
     * Constructor khusus untuk membuat menu dengan status tersedia secara default.
     * 
     * @param id identitas unik menu
     * @param namaMenu nama menu
     * @param harga harga menu dalam rupiah
     * @param kategori kategori menu
     */
    public Menu(String id, String namaMenu, double harga, String kategori) {
        this.id = id;
        this.namaMenu = namaMenu;
        this.harga = harga;
        this.kategori = kategori;
        this.tersedia = true; // default tersedia
    }
    
    /**
     * Method helper untuk mengecek apakah menu tersedia untuk dipesan.
     * 
     * @return true jika menu tersedia, false jika habis
     */
    public boolean isTersedia() {
        return tersedia;
    }
    
    /**
     * Method helper untuk mengubah status ketersediaan menu.
     * Method ini akan digunakan oleh staf kantin untuk update status.
     * 
     * @param status status baru (true = tersedia, false = habis)
     */
    public void setStatusKetersediaan(boolean status) {
        this.tersedia = status;
    }
    
    /**
     * Override toString() untuk keperluan display dan debugging.
     * 
     * @return representasi string dari objek Menu
     */
    @Override
    public String toString() {
        String statusText = tersedia ? "Tersedia" : "Habis";
        return String.format("Menu{id='%s', nama='%s', harga=Rp%.0f, kategori='%s', status='%s'}", 
                           id, namaMenu, harga, kategori, statusText);
    }
}