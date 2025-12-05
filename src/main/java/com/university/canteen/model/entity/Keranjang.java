package com.university.canteen.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

/**
 * Entity Keranjang
 * 
 * Class entity yang merepresentasikan keranjang belanja pengguna dalam sistem
 * kantin universitas. Setiap keranjang berisi kumpulan ItemKeranjang yang
 * dipilih oleh pengguna sebelum melakukan checkout.
 * 
 * Class ini mendukung:
 * - Requirement R02: Pengguna dapat menambahkan item ke keranjang
 * - Fact F02: Setiap pengguna hanya memiliki satu keranjang pada satu waktu
 * - OCL dari konteks.md untuk method tambahItem(), hapusItem(), kosongkanKeranjang()
 * 
 * Berdasarkan OCL:
 * - tambahItem(): pre: menu.tersedia && kuantitas > 0; post: item ada di keranjang
 * - hapusItem(): pre: item ada di keranjang; post: item tidak ada lagi
 * - kosongkanKeranjang(): post: keranjang kosong
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "keranjang")
@Data
@NoArgsConstructor
public class Keranjang {
    
    /** 
     * ID unik untuk keranjang.
     * Format: CART-YYYYMMDD-HHMMSS-XXX
     */
    @Id
    @Column(name = "id", length = 50)
    private String idKeranjang;
    
    /** 
     * ID pengguna pemilik keranjang.
     * Sesuai Fact F02: setiap pengguna hanya punya satu keranjang.
     */
    @Column(name = "user_id", nullable = false, length = 50)
    private String idPengguna;
    
    /** 
     * Daftar item yang ada dalam keranjang.
     * Menggunakan List untuk memudahkan operasi CRUD.
     */
    @OneToMany(mappedBy = "keranjang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ItemKeranjang> daftarItem;
    
    /** 
     * Timestamp kapan keranjang dibuat atau terakhir diupdate.
     */
    @Column(name = "waktu_update")
    private LocalDateTime waktuUpdate;
    
    /**
     * Constructor untuk membuat keranjang baru dengan ID pengguna.
     * 
     * @param idPengguna ID pengguna pemilik keranjang
     * @throws IllegalArgumentException jika idPengguna null atau kosong
     */
    public Keranjang(String idPengguna) {
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        this.idPengguna = idPengguna;
        this.idKeranjang = generateIdKeranjang();
        this.daftarItem = new ArrayList<>();
        this.waktuUpdate = LocalDateTime.now();
    }
    
    /**
     * Method untuk menambah item ke keranjang.
     * Implementasi OCL: pre: menu.tersedia && kuantitas > 0; post: item ada di keranjang
     * 
     * @param menu menu yang akan ditambahkan
     * @param kuantitas jumlah item yang dipesan
     * @return true jika berhasil ditambahkan
     * @throws IllegalArgumentException jika pre-condition tidak terpenuhi
     */
    public boolean tambahItem(Menu menu, int kuantitas) {
        // OCL Pre-condition: menu harus tersedia dan kuantitas > 0
        if (menu == null) {
            throw new IllegalArgumentException("Menu tidak boleh null");
        }
        
        if (!menu.isTersedia()) {
            throw new IllegalArgumentException("Menu " + menu.getNamaMenu() + " sedang tidak tersedia");
        }
        
        if (kuantitas <= 0) {
            throw new IllegalArgumentException("Kuantitas harus lebih dari nol");
        }
        
        // Cek apakah item sudah ada di keranjang
        Optional<ItemKeranjang> itemExisting = daftarItem.stream()
            .filter(item -> item.getMenu().getId().equals(menu.getId()))
            .findFirst();
        
        if (itemExisting.isPresent()) {
            // Jika sudah ada, tambah kuantitasnya
            ItemKeranjang item = itemExisting.get();
            item.tambahKuantitas(kuantitas);
        } else {
            // Jika belum ada, buat item baru
            ItemKeranjang itemBaru = new ItemKeranjang(menu, kuantitas);
            daftarItem.add(itemBaru);
        }
        
        updateWaktu();
        
        // OCL Post-condition: item harus ada di keranjang
        boolean itemAda = daftarItem.stream()
            .anyMatch(item -> item.getMenu().getId().equals(menu.getId()));
        
        if (!itemAda) {
            throw new RuntimeException("Post-condition gagal: item tidak berhasil ditambahkan");
        }
        
        return true;
    }
    
    /**
     * Method untuk menghapus item dari keranjang.
     * Implementasi OCL: pre: item ada di keranjang; post: item tidak ada lagi
     * 
     * @param idMenu ID menu yang akan dihapus
     * @return true jika berhasil dihapus
     * @throws IllegalArgumentException jika pre-condition tidak terpenuhi
     */
    public boolean hapusItem(String idMenu) {
        if (idMenu == null || idMenu.trim().isEmpty()) {
            throw new IllegalArgumentException("ID menu tidak boleh kosong");
        }
        
        // OCL Pre-condition: item harus ada di keranjang
        boolean itemAda = daftarItem.stream()
            .anyMatch(item -> item.getMenu().getId().equals(idMenu));
        
        if (!itemAda) {
            throw new IllegalArgumentException("Item dengan ID " + idMenu + " tidak ada di keranjang");
        }
        
        // Hapus item dari keranjang
        boolean berhasilDihapus = daftarItem.removeIf(item -> item.getMenu().getId().equals(idMenu));
        
        if (berhasilDihapus) {
            updateWaktu();
            
            // OCL Post-condition: item tidak boleh ada lagi di keranjang
            boolean itemMasihAda = daftarItem.stream()
                .anyMatch(item -> item.getMenu().getId().equals(idMenu));
            
            if (itemMasihAda) {
                throw new RuntimeException("Post-condition gagal: item masih ada di keranjang");
            }
        }
        
        return berhasilDihapus;
    }
    
    /**
     * Method untuk mengosongkan seluruh keranjang.
     * Implementasi OCL: post: keranjang kosong
     */
    public void kosongkanKeranjang() {
        daftarItem.clear();
        updateWaktu();
        
        // OCL Post-condition: keranjang harus kosong
        if (!daftarItem.isEmpty()) {
            throw new RuntimeException("Post-condition gagal: keranjang tidak kosong");
        }
    }
    
    /**
     * Method untuk menghitung total harga semua item dalam keranjang.
     * 
     * @return total harga dalam rupiah
     */
    public double hitungTotalHarga() {
        return daftarItem.stream()
                        .mapToDouble(ItemKeranjang::getSubtotal)
                        .sum();
    }
    
    /**
     * Method untuk mendapatkan jumlah total item dalam keranjang.
     * 
     * @return jumlah total item
     */
    public int getJumlahTotalItem() {
        return daftarItem.stream()
                        .mapToInt(ItemKeranjang::getKuantitas)
                        .sum();
    }
    
    /**
     * Method untuk mengecek apakah keranjang kosong.
     * 
     * @return true jika kosong, false jika ada item
     */
    public boolean isEmpty() {
        return daftarItem.isEmpty();
    }
    
    /**
     * Method untuk mendapatkan item berdasarkan ID menu.
     * 
     * @param idMenu ID menu yang dicari
     * @return Optional berisi ItemKeranjang jika ditemukan
     */
    public Optional<ItemKeranjang> getItemByMenuId(String idMenu) {
        return daftarItem.stream()
                        .filter(item -> item.getMenu().getId().equals(idMenu))
                        .findFirst();
    }
    
    /**
     * Method untuk update kuantitas item yang sudah ada.
     * 
     * @param idMenu ID menu yang akan diupdate
     * @param kuantitasBaru kuantitas baru
     * @return true jika berhasil diupdate
     */
    public boolean updateKuantitasItem(String idMenu, int kuantitasBaru) {
        if (kuantitasBaru <= 0) {
            // Jika kuantitas 0 atau negatif, hapus item
            return hapusItem(idMenu);
        }
        
        Optional<ItemKeranjang> itemOpt = getItemByMenuId(idMenu);
        if (itemOpt.isPresent()) {
            itemOpt.get().setKuantitas(kuantitasBaru);
            updateWaktu();
            return true;
        }
        
        return false;
    }
    
    /**
     * Method untuk mendapatkan ringkasan keranjang.
     * 
     * @return string berisi ringkasan semua item
     */
    public String getRingkasanKeranjang() {
        if (isEmpty()) {
            return "Keranjang kosong";
        }
        
        StringBuilder ringkasan = new StringBuilder();
        ringkasan.append("=== RINGKASAN KERANJANG ===\n");
        ringkasan.append(String.format("ID Keranjang: %s\n", idKeranjang));
        ringkasan.append(String.format("Pemilik: %s\n", idPengguna));
        ringkasan.append(String.format("Jumlah Item: %d\n\n", daftarItem.size()));
        
        for (ItemKeranjang item : daftarItem) {
            ringkasan.append(item.getRingkasan()).append("\n");
        }
        
        ringkasan.append("\n");
        ringkasan.append(String.format("TOTAL: Rp%.0f", hitungTotalHarga()));
        
        return ringkasan.toString();
    }
    
    /**
     * Generate ID keranjang yang unik.
     * 
     * @return string ID keranjang
     */
    private String generateIdKeranjang() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return String.format("CART-%d-%03d", timestamp, random);
    }
    
    /**
     * Update timestamp keranjang.
     */
    private void updateWaktu() {
        this.waktuUpdate = LocalDateTime.now();
    }
    
    /**
     * Override toString untuk debugging.
     * 
     * @return representasi string dari keranjang
     */
    @Override
    public String toString() {
        return String.format("Keranjang{id='%s', user='%s', items=%d, total=Rp%.0f}", 
                           idKeranjang, idPengguna, daftarItem.size(), hitungTotalHarga());
    }
}