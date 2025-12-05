package com.university.canteen.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity ItemPesanan
 * 
 * Class entity yang merepresentasikan snapshot item menu dalam pesanan yang
 * sudah final/dikonfirmasi. Berbeda dengan ItemKeranjang yang masih bisa
 * diubah, ItemPesanan adalah record permanen dari item yang dipesan.
 * 
 * Class ini menyimpan informasi menu pada saat pesanan dibuat, sehingga
 * jika harga menu berubah di kemudian hari, record pesanan tetap akurat.
 * 
 * Berdasarkan OCL dari konteks.md:
 * - inv: kuantitas > 0 (kuantitas harus selalu positif)
 * - inv: subtotal = hargaSatuan * kuantitas (subtotal harus dihitung benar)
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
public class ItemPesanan {
    
    /** 
     * ID menu yang dipesan.
     * Diambil dari Menu.id pada saat pesanan dibuat.
     */
    private String idMenu;
    
    /** 
     * Nama menu pada saat pesanan dibuat.
     * Snapshot untuk memastikan konsistensi data historis.
     */
    private String namaMenu;
    
    /** 
     * Harga satuan menu pada saat pesanan dibuat.
     * Snapshot harga untuk mencegah perubahan retroaktif.
     */
    private double hargaSatuan;
    
    /** 
     * Kuantitas item yang dipesan.
     * Berdasarkan OCL: kuantitas > 0
     */
    private int kuantitas;
    
    /** 
     * Kategori menu pada saat pesanan dibuat.
     * Untuk keperluan laporan dan analisis.
     */
    private String kategoriMenu;
    
    /** 
     * Subtotal untuk item ini.
     * Berdasarkan OCL: subtotal = hargaSatuan * kuantitas
     */
    private double subtotal;
    
    /**
     * Constructor untuk membuat ItemPesanan dari ItemKeranjang.
     * Method ini melakukan snapshot data menu pada saat pesanan dibuat.
     * 
     * @param itemKeranjang item dari keranjang yang akan dikonversi
     * @throws IllegalArgumentException jika itemKeranjang null atau tidak valid
     */
    public ItemPesanan(ItemKeranjang itemKeranjang) {
        if (itemKeranjang == null) {
            throw new IllegalArgumentException("ItemKeranjang tidak boleh null");
        }
        
        if (itemKeranjang.getMenu() == null) {
            throw new IllegalArgumentException("Menu dalam ItemKeranjang tidak boleh null");
        }
        
        // OCL Pre-condition: kuantitas > 0
        if (itemKeranjang.getKuantitas() <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih dari nol");
        }
        
        Menu menu = itemKeranjang.getMenu();
        
        // Snapshot data menu pada saat pesanan dibuat
        this.idMenu = menu.getId();
        this.namaMenu = menu.getNamaMenu();
        this.hargaSatuan = menu.getHarga();
        this.kuantitas = itemKeranjang.getKuantitas();
        this.kategoriMenu = menu.getKategori();
        
        // Hitung subtotal sesuai OCL: subtotal = hargaSatuan * kuantitas
        hitungSubtotal();
        
        // Validasi OCL post-condition
        validateOCLConstraints();
    }
    
    /**
     * Constructor lengkap dengan semua parameter.
     * 
     * @param idMenu ID menu
     * @param namaMenu nama menu
     * @param hargaSatuan harga per unit
     * @param kuantitas jumlah item
     * @param kategoriMenu kategori menu
     */
    public ItemPesanan(String idMenu, String namaMenu, double hargaSatuan, 
                      int kuantitas, String kategoriMenu) {
        // Validasi input
        if (idMenu == null || idMenu.trim().isEmpty()) {
            throw new IllegalArgumentException("ID menu tidak boleh kosong");
        }
        
        if (namaMenu == null || namaMenu.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama menu tidak boleh kosong");
        }
        
        if (hargaSatuan <= 0) {
            throw new IllegalArgumentException("Harga satuan harus lebih dari nol");
        }
        
        if (kuantitas <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih dari nol");
        }
        
        this.idMenu = idMenu;
        this.namaMenu = namaMenu;
        this.hargaSatuan = hargaSatuan;
        this.kuantitas = kuantitas;
        this.kategoriMenu = kategoriMenu != null ? kategoriMenu : "Tidak Terkategorisasi";
        
        // Hitung subtotal
        hitungSubtotal();
        
        // Validasi OCL constraints
        validateOCLConstraints();
    }
    
    /**
     * Method private untuk menghitung subtotal.
     * Implementasi OCL: subtotal = hargaSatuan * kuantitas
     */
    private void hitungSubtotal() {
        this.subtotal = this.hargaSatuan * this.kuantitas;
    }
    
    /**
     * Method private untuk validasi OCL constraints.
     * 
     * @throws RuntimeException jika constraint tidak terpenuhi
     */
    private void validateOCLConstraints() {
        // OCL Invariant: kuantitas > 0
        if (kuantitas <= 0) {
            throw new RuntimeException("OCL Constraint gagal: kuantitas harus lebih dari nol");
        }
        
        // OCL Invariant: subtotal = hargaSatuan * kuantitas
        double expectedSubtotal = hargaSatuan * kuantitas;
        if (Math.abs(subtotal - expectedSubtotal) > 0.01) { // toleransi floating point
            throw new RuntimeException("OCL Constraint gagal: subtotal tidak sesuai kalkulasi");
        }
    }
    
    /**
     * Setter untuk kuantitas dengan recalculate subtotal.
     * 
     * @param kuantitas kuantitas baru
     */
    public void setKuantitas(int kuantitas) {
        if (kuantitas <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih dari nol");
        }
        
        this.kuantitas = kuantitas;
        hitungSubtotal();
        validateOCLConstraints();
    }
    
    /**
     * Setter untuk harga satuan dengan recalculate subtotal.
     * 
     * @param hargaSatuan harga satuan baru
     */
    public void setHargaSatuan(double hargaSatuan) {
        if (hargaSatuan <= 0) {
            throw new IllegalArgumentException("Harga satuan harus lebih dari nol");
        }
        
        this.hargaSatuan = hargaSatuan;
        hitungSubtotal();
        validateOCLConstraints();
    }
    
    /**
     * Method untuk mendapatkan informasi ringkas item pesanan.
     * 
     * @return string informasi item
     */
    public String getRingkasan() {
        return String.format("%s (%s) x%d @ Rp%.0f = Rp%.0f", 
                           namaMenu, kategoriMenu, kuantitas, hargaSatuan, subtotal);
    }
    
    /**
     * Method untuk membandingkan dengan ItemKeranjang berdasarkan menu ID.
     * 
     * @param itemKeranjang item keranjang yang akan dibandingkan
     * @return true jika menu ID sama
     */
    public boolean isSameMenu(ItemKeranjang itemKeranjang) {
        if (itemKeranjang == null || itemKeranjang.getMenu() == null) {
            return false;
        }
        
        return this.idMenu.equals(itemKeranjang.getMenu().getId());
    }
    
    /**
     * Method untuk membandingkan dengan Menu berdasarkan ID.
     * 
     * @param menu menu yang akan dibandingkan
     * @return true jika ID sama
     */
    public boolean isSameMenu(Menu menu) {
        if (menu == null) {
            return false;
        }
        
        return this.idMenu.equals(menu.getId());
    }
    
    /**
     * Override equals berdasarkan ID menu dan kuantitas.
     * 
     * @param obj objek yang akan dibandingkan
     * @return true jika sama
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        ItemPesanan that = (ItemPesanan) obj;
        return kuantitas == that.kuantitas && 
               Double.compare(that.hargaSatuan, hargaSatuan) == 0 &&
               idMenu.equals(that.idMenu);
    }
    
    /**
     * Override hashCode berdasarkan ID menu.
     * 
     * @return hash code dari ID menu
     */
    @Override
    public int hashCode() {
        return idMenu != null ? idMenu.hashCode() : 0;
    }
    
    /**
     * Override toString untuk debugging dan logging.
     * 
     * @return representasi string dari ItemPesanan
     */
    @Override
    public String toString() {
        return String.format("ItemPesanan{id='%s', nama='%s', harga=Rp%.0f, " +
                           "qty=%d, kategori='%s', subtotal=Rp%.0f}", 
                           idMenu, namaMenu, hargaSatuan, kuantitas, 
                           kategoriMenu, subtotal);
    }
}