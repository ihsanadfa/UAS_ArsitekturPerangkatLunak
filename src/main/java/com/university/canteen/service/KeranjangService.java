package com.university.canteen.service;

import com.university.canteen.model.entity.Keranjang;
import com.university.canteen.model.entity.Menu;
import com.university.canteen.model.entity.ItemKeranjang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service KeranjangService
 * 
 * Service class yang mengelola operasi keranjang belanja pengguna.
 * Service ini mengimplementasikan logic bisnis untuk Requirement R02:
 * Pengguna dapat memilih dan menambahkan item menu ke dalam keranjang pesanan.
 * 
 * *** OCL INTEGRATION ***
 * Service ini mengimplementasikan OCL constraints dari konteks.md:
 * - Pre-condition: menu.tersedia && kuantitas > 0 sebelum tambah item
 * - Post-condition: memastikan item berhasil ditambahkan ke keranjang
 * - Fact F02: Setiap pengguna hanya memiliki satu keranjang pada satu waktu
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Service
public class KeranjangService {
    
    @Autowired
    private MenuService menuService;
    
    /** 
     * In-memory storage untuk keranjang pengguna.
     * Key = idPengguna, Value = Keranjang
     * Implementasi Fact F02: setiap pengguna hanya punya satu keranjang.
     */
    private final Map<String, Keranjang> keranjangMap = new HashMap<>();
    
    /**
     * *** REQUIREMENT R02 + OCL PRE-CONDITION ***
     * Method untuk menambah item ke keranjang pengguna.
     * Mengimplementasikan OCL: pre: menu.tersedia && kuantitas > 0
     * 
     * @param idPengguna ID pengguna yang menambah item
     * @param idMenu ID menu yang akan ditambahkan
     * @param kuantitas jumlah item yang dipesan
     * @return true jika berhasil ditambahkan
     * @throws IllegalArgumentException jika OCL pre-condition tidak terpenuhi
     */
    public boolean tambahItemKeKeranjang(String idPengguna, String idMenu, int kuantitas) {
        // Validasi input parameters
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        if (idMenu == null || idMenu.trim().isEmpty()) {
            throw new IllegalArgumentException("ID menu tidak boleh kosong");
        }
        
        // OCL Pre-condition: kuantitas > 0
        if (kuantitas <= 0) {
            throw new IllegalArgumentException("OCL Pre-condition gagal: kuantitas harus lebih dari nol");
        }
        
        // Dapatkan menu dari MenuService (yang menggunakan Singleton)
        Optional<Menu> menuOpt = menuService.getMenuById(idMenu);
        if (menuOpt.isEmpty()) {
            throw new IllegalArgumentException("Menu dengan ID " + idMenu + " tidak ditemukan");
        }
        
        Menu menu = menuOpt.get();
        
        // OCL Pre-condition: menu.tersedia
        if (!menu.isTersedia()) {
            throw new IllegalArgumentException("OCL Pre-condition gagal: Menu " + menu.getNamaMenu() + " sedang tidak tersedia");
        }
        
        // Dapatkan atau buat keranjang untuk pengguna
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        
        // Tambahkan item ke keranjang (method tambahItem sudah implement OCL)
        boolean berhasil = keranjang.tambahItem(menu, kuantitas);
        
        // Update keranjang di map
        keranjangMap.put(idPengguna, keranjang);
        
        return berhasil;
    }
    
    /**
     * Method untuk menghapus item dari keranjang.
     * 
     * @param idPengguna ID pengguna pemilik keranjang
     * @param idMenu ID menu yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean hapusItemDariKeranjang(String idPengguna, String idMenu) {
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        
        // Method hapusItem sudah implement OCL constraints
        boolean berhasil = keranjang.hapusItem(idMenu);
        
        // Update keranjang di map
        keranjangMap.put(idPengguna, keranjang);
        
        return berhasil;
    }
    
    /**
     * Method untuk update kuantitas item dalam keranjang.
     * 
     * @param idPengguna ID pengguna pemilik keranjang
     * @param idMenu ID menu yang akan diupdate
     * @param kuantitasBaru kuantitas baru
     * @return true jika berhasil diupdate
     */
    public boolean updateKuantitasItem(String idPengguna, String idMenu, int kuantitasBaru) {
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        // Jika kuantitas baru 0 atau negatif, hapus item
        if (kuantitasBaru <= 0) {
            return hapusItemDariKeranjang(idPengguna, idMenu);
        }
        
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        
        boolean berhasil = keranjang.updateKuantitasItem(idMenu, kuantitasBaru);
        
        if (berhasil) {
            keranjangMap.put(idPengguna, keranjang);
        }
        
        return berhasil;
    }
    
    /**
     * Method untuk mengosongkan keranjang pengguna.
     * 
     * @param idPengguna ID pengguna pemilik keranjang
     */
    public void kosongkanKeranjang(String idPengguna) {
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        
        // Method kosongkanKeranjang sudah implement OCL post-condition
        keranjang.kosongkanKeranjang();
        
        keranjangMap.put(idPengguna, keranjang);
    }
    
    /**
     * Method untuk mendapatkan keranjang pengguna.
     * Implementasi Fact F02: setiap pengguna hanya punya satu keranjang.
     * 
     * @param idPengguna ID pengguna
     * @return objek Keranjang pengguna
     */
    public Keranjang getKeranjangPengguna(String idPengguna) {
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        // Fact F02: Setiap pengguna hanya memiliki satu keranjang pada satu waktu
        return keranjangMap.computeIfAbsent(idPengguna, id -> new Keranjang(id));
    }
    
    /**
     * Method untuk mendapatkan item dalam keranjang berdasarkan menu ID.
     * 
     * @param idPengguna ID pengguna
     * @param idMenu ID menu yang dicari
     * @return Optional berisi ItemKeranjang jika ditemukan
     */
    public Optional<ItemKeranjang> getItemKeranjang(String idPengguna, String idMenu) {
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        return keranjang.getItemByMenuId(idMenu);
    }
    
    /**
     * Method untuk menghitung total harga keranjang.
     * 
     * @param idPengguna ID pengguna
     * @return total harga dalam rupiah
     */
    public double hitungTotalKeranjang(String idPengguna) {
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        return keranjang.hitungTotalHarga();
    }
    
    /**
     * Method untuk mendapatkan jumlah total item dalam keranjang.
     * 
     * @param idPengguna ID pengguna
     * @return jumlah total item
     */
    public int hitungJumlahItemKeranjang(String idPengguna) {
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        return keranjang.getJumlahTotalItem();
    }
    
    /**
     * Method untuk mengecek apakah keranjang kosong.
     * 
     * @param idPengguna ID pengguna
     * @return true jika keranjang kosong
     */
    public boolean isKeranjangKosong(String idPengguna) {
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        return keranjang.isEmpty();
    }
    
    /**
     * Method untuk mendapatkan ringkasan keranjang dalam format string.
     * 
     * @param idPengguna ID pengguna
     * @return string berisi ringkasan keranjang
     */
    public String getRingkasanKeranjang(String idPengguna) {
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        return keranjang.getRingkasanKeranjang();
    }
    
    /**
     * Method untuk validasi keranjang sebelum checkout.
     * Memastikan semua item dalam keranjang masih tersedia.
     * 
     * @param idPengguna ID pengguna
     * @return true jika semua item valid untuk checkout
     * @throws IllegalStateException jika ada item yang tidak valid
     */
    public boolean validasiKeranjangUntukCheckout(String idPengguna) {
        Keranjang keranjang = getKeranjangPengguna(idPengguna);
        
        if (keranjang.isEmpty()) {
            throw new IllegalStateException("Keranjang kosong, tidak dapat melakukan checkout");
        }
        
        // Validasi setiap item dalam keranjang
        for (ItemKeranjang item : keranjang.getDaftarItem()) {
            Menu menu = item.getMenu();
            
            // Cek ulang status ketersediaan menu dari MenuService (Singleton)
            Optional<Menu> menuTerbaru = menuService.getMenuById(menu.getId());
            
            if (menuTerbaru.isEmpty()) {
                throw new IllegalStateException("Menu " + menu.getNamaMenu() + " tidak ditemukan dalam sistem");
            }
            
            if (!menuTerbaru.get().isTersedia()) {
                throw new IllegalStateException("Menu " + menu.getNamaMenu() + " sudah tidak tersedia");
            }
        }
        
        return true;
    }
    
    /**
     * Method untuk mendapatkan statistik keranjang sistem.
     * 
     * @return string berisi statistik
     */
    public String getStatistikKeranjang() {
        int totalKeranjang = keranjangMap.size();
        long keranjangBerisi = keranjangMap.values().stream()
                                          .filter(k -> !k.isEmpty())
                                          .count();
        
        double totalNilai = keranjangMap.values().stream()
                                       .mapToDouble(Keranjang::hitungTotalHarga)
                                       .sum();
        
        return String.format("Total Keranjang: %d | Berisi: %d | Total Nilai: Rp%.0f", 
                           totalKeranjang, keranjangBerisi, totalNilai);
    }
    
    /**
     * Method untuk membersihkan keranjang yang sudah lama tidak digunakan.
     * (Dalam implementasi nyata bisa menggunakan scheduled task)
     */
    public void cleanupKeranjangLama() {
        // Implementasi cleanup logic untuk keranjang yang tidak aktif
        // Bisa berdasarkan waktu update terakhir
        keranjangMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}