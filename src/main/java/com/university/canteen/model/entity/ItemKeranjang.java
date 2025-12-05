package com.university.canteen.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity ItemKeranjang
 * 
 * Class entity yang merepresentasikan item individual dalam keranjang belanja.
 * Setiap ItemKeranjang berisi referensi ke Menu dan jumlah quantity yang dipesan.
 * 
 * Class ini mendukung:
 * - Requirement R02: Pengguna dapat menambahkan item ke keranjang
 * - OCL Constraint: kuantitas item selalu lebih dari nol
 * - Fact F02: Setiap pengguna hanya memiliki satu keranjang pada satu waktu
 * 
 * Berdasarkan OCL dari konteks.md:
 * - inv: kuantitas > 0 (invariant untuk memastikan kuantitas valid)
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "item_keranjang")
@Data
@NoArgsConstructor
public class ItemKeranjang {
    
    /** 
     * ID unik untuk item keranjang.
     */
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    /** 
     * Referensi ke keranjang pemilik item ini.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keranjang_id", nullable = false)
    private Keranjang keranjang;
    
    /** 
     * Referensi ke menu yang dipilih pengguna.
     * Berisi informasi lengkap tentang menu (nama, harga, status ketersediaan).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    
    /** 
     * Jumlah quantity item yang dipesan.
     * Berdasarkan OCL: kuantitas harus selalu lebih dari nol.
     */
    @Column(name = "kuantitas", nullable = false)
    private int kuantitas;
    
    /**
     * Constructor dengan validasi OCL constraint.
     * 
     * @param menu objek menu yang akan ditambahkan
     * @param kuantitas jumlah item yang dipesan
     * @throws IllegalArgumentException jika menu null atau kuantitas tidak valid
     */
    public ItemKeranjang(Menu menu, int kuantitas) {
        // Validasi menu tidak boleh null
        if (menu == null) {
            throw new IllegalArgumentException("Menu tidak boleh null");
        }
        
        // Validasi OCL: kuantitas > 0
        if (kuantitas <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih dari nol");
        }
        
        this.menu = menu;
        this.kuantitas = kuantitas;
    }
    
    /**
     * Menghitung subtotal harga untuk item ini.
     * Subtotal = harga menu * kuantitas.
     * 
     * @return subtotal dalam rupiah
     */
    public double getSubtotal() {
        return menu.getHarga() * kuantitas;
    }
    
    /**
     * Setter untuk kuantitas dengan validasi OCL.
     * 
     * @param kuantitas kuantitas baru
     * @throws IllegalArgumentException jika kuantitas tidak valid
     */
    public void setKuantitas(int kuantitas) {
        // Validasi OCL: kuantitas > 0
        if (kuantitas <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih dari nol");
        }
        this.kuantitas = kuantitas;
    }
    
    /**
     * Method helper untuk menambah kuantitas item.
     * 
     * @param tambahan jumlah yang akan ditambahkan
     * @throws IllegalArgumentException jika hasil akhir kuantitas tidak valid
     */
    public void tambahKuantitas(int tambahan) {
        if (tambahan <= 0) {
            throw new IllegalArgumentException("Tambahan kuantitas harus positif");
        }
        setKuantitas(this.kuantitas + tambahan);
    }
    
    /**
     * Method helper untuk mengurangi kuantitas item.
     * 
     * @param pengurangan jumlah yang akan dikurangi
     * @throws IllegalArgumentException jika hasil akhir kuantitas tidak valid
     */
    public void kurangiKuantitas(int pengurangan) {
        if (pengurangan <= 0) {
            throw new IllegalArgumentException("Pengurangan kuantitas harus positif");
        }
        
        int kuantitasBaru = this.kuantitas - pengurangan;
        if (kuantitasBaru <= 0) {
            throw new IllegalArgumentException("Kuantitas tidak boleh menjadi nol atau negatif");
        }
        
        setKuantitas(kuantitasBaru);
    }
    
    /**
     * Mengecek apakah menu item masih tersedia untuk dipesan.
     * 
     * @return true jika menu tersedia, false jika habis
     */
    public boolean isMenuTersedia() {
        return menu != null && menu.isTersedia();
    }
    
    /**
     * Mendapatkan informasi ringkas item keranjang.
     * 
     * @return string informasi item
     */
    public String getRingkasan() {
        return String.format("%s x%d = Rp%.0f", 
                           menu.getNamaMenu(), kuantitas, getSubtotal());
    }
    
    /**
     * Override equals untuk comparing ItemKeranjang berdasarkan menu ID.
     * 
     * @param obj objek yang akan dibandingkan
     * @return true jika menu ID sama
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemKeranjang that = (ItemKeranjang) obj;
        return menu != null && menu.getId() != null && 
               menu.getId().equals(that.menu.getId());
    }
    
    /**
     * Override hashCode berdasarkan menu ID.
     * 
     * @return hash code dari menu ID
     */
    @Override
    public int hashCode() {
        return menu != null && menu.getId() != null ? menu.getId().hashCode() : 0;
    }
    
    /**
     * Override toString untuk debugging dan logging.
     * 
     * @return representasi string dari ItemKeranjang
     */
    @Override
    public String toString() {
        return String.format("ItemKeranjang{menu='%s', kuantitas=%d, subtotal=Rp%.0f}", 
                           menu != null ? menu.getNamaMenu() : "null", 
                           kuantitas, getSubtotal());
    }
}