package com.university.canteen.service;

import com.university.canteen.model.entity.Menu;
import com.university.canteen.patterns.singleton.MenuRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service MenuService
 * 
 * Service class yang bertindak sebagai wrapper/client untuk MenuRepository Singleton.
 * Class ini TIDAK menyimpan data sendiri, melainkan menggunakan Singleton Pattern
 * melalui MenuRepository.getInstance() untuk semua operasi data menu.
 * 
 * *** SINGLETON PATTERN INTEGRATION ***
 * Service ini mendemonstrasikan penggunaan Singleton Pattern:
 * - Semua method memanggil MenuRepository.getInstance()
 * - Memastikan hanya satu instance repository yang digunakan
 * - Menyediakan abstraksi Spring Boot di atas Singleton
 * 
 * Fungsi utama:
 * - Requirement R01: Menampilkan daftar menu (getMenuTersedia())
 * - Requirement R04: Update status ketersediaan menu
 * - Wrapper untuk operasi CRUD menu melalui Singleton
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Service
public class MenuService {
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Method untuk mendapatkan semua menu.
     * Memanggil Singleton MenuRepository.getInstance().getAllMenu()
     * 
     * @return List berisi semua menu dalam sistem
     */
    public List<Menu> getAllMenu() {
        // Menggunakan Singleton Pattern - MenuRepository.getInstance()
        return MenuRepository.getInstance().getAllMenu();
    }
    
    /**
     * *** SINGLETON PATTERN USAGE + REQUIREMENT R01 ***
     * Method untuk mendapatkan menu yang tersedia saja.
     * Mendukung Requirement R01: Sistem harus menampilkan daftar menu yang tersedia.
     * 
     * @return List berisi menu dengan status tersedia (statusKetersediaan = true)
     */
    public List<Menu> getMenuTersedia() {
        // Menggunakan Singleton Pattern untuk akses data
        return MenuRepository.getInstance().getMenuTersedia();
    }
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Method untuk mendapatkan menu berdasarkan ID.
     * 
     * @param id ID menu yang dicari
     * @return Optional berisi menu jika ditemukan
     */
    public Optional<Menu> getMenuById(String id) {
        // Delegate ke Singleton MenuRepository
        return MenuRepository.getInstance().getMenuById(id);
    }
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Method untuk mendapatkan menu berdasarkan kategori.
     * 
     * @param kategori kategori menu yang dicari
     * @return List berisi menu dalam kategori tersebut
     */
    public List<Menu> getMenuByKategori(String kategori) {
        // Menggunakan Singleton untuk filtering by kategori
        return MenuRepository.getInstance().getMenuByKategori(kategori);
    }
    
    /**
     * *** SINGLETON PATTERN USAGE + REQUIREMENT R04 ***
     * Method untuk update status ketersediaan menu.
     * Mendukung Requirement R04: Staf dapat memperbarui status menu.
     * 
     * @param id ID menu yang akan diupdate
     * @param status status baru (true = tersedia, false = habis)
     * @return true jika update berhasil, false jika menu tidak ditemukan
     */
    public boolean updateStatusMenu(String id, boolean status) {
        // Menggunakan Singleton Pattern untuk update data
        return MenuRepository.getInstance().updateStatusMenu(id, status);
    }
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Method untuk menambah menu baru ke sistem.
     * 
     * @param menu objek Menu yang akan ditambahkan
     * @return true jika berhasil ditambahkan
     * @throws IllegalArgumentException jika menu null atau ID sudah ada
     */
    public boolean addMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu tidak boleh null");
        }
        
        // Delegate ke Singleton MenuRepository
        return MenuRepository.getInstance().addMenu(menu);
    }
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Method untuk menghapus menu dari sistem.
     * 
     * @param id ID menu yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean removeMenu(String id) {
        // Menggunakan Singleton untuk remove operation
        return MenuRepository.getInstance().removeMenu(id);
    }
    
    /**
     * Method untuk mendapatkan statistik menu.
     * Menggunakan Singleton untuk akses data statistik.
     * 
     * @return string berisi statistik menu
     */
    public String getStatistikMenu() {
        MenuRepository repo = MenuRepository.getInstance();
        
        long totalMenu = repo.getTotalMenu();
        long menuTersedia = repo.getTotalMenuTersedia();
        long menuHabis = totalMenu - menuTersedia;
        
        return String.format("Total Menu: %d | Tersedia: %d | Habis: %d", 
                           totalMenu, menuTersedia, menuHabis);
    }
    
    /**
     * Method untuk validasi menu sebelum digunakan dalam pesanan.
     * Mendukung OCL pre-condition untuk tambah item ke keranjang.
     * 
     * @param id ID menu yang akan divalidasi
     * @return true jika menu valid dan tersedia
     */
    public boolean isMenuValidUntukPesanan(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        
        // Menggunakan Singleton untuk cek ketersediaan
        Optional<Menu> menuOpt = MenuRepository.getInstance().getMenuById(id);
        
        if (menuOpt.isEmpty()) {
            return false;
        }
        
        Menu menu = menuOpt.get();
        
        // Validasi: menu harus tersedia (OCL pre-condition)
        return menu.isTersedia();
    }
    
    /**
     * Method untuk mendapatkan daftar kategori yang tersedia.
     * 
     * @return List string berisi kategori unik
     */
    public List<String> getKategoriTersedia() {
        // Menggunakan Singleton untuk get all menu, lalu extract kategori
        return MenuRepository.getInstance().getAllMenu()
                .stream()
                .map(Menu::getKategori)
                .distinct()
                .sorted()
                .toList();
    }
    
    /**
     * Method untuk search menu berdasarkan nama.
     * 
     * @param keyword kata kunci pencarian
     * @return List menu yang mengandung keyword
     */
    public List<Menu> searchMenuByNama(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllMenu();
        }
        
        String keywordLower = keyword.toLowerCase();
        
        // Menggunakan Singleton untuk get semua menu, lalu filter
        return MenuRepository.getInstance().getAllMenu()
                .stream()
                .filter(menu -> menu.getNamaMenu().toLowerCase().contains(keywordLower))
                .toList();
    }
    
    /**
     * Method untuk mendapatkan menu dalam range harga tertentu.
     * 
     * @param minHarga harga minimum
     * @param maxHarga harga maksimum
     * @return List menu dalam range harga
     */
    public List<Menu> getMenuByRangeHarga(double minHarga, double maxHarga) {
        if (minHarga < 0 || maxHarga < minHarga) {
            throw new IllegalArgumentException("Range harga tidak valid");
        }
        
        // Menggunakan Singleton untuk filtering
        return MenuRepository.getInstance().getAllMenu()
                .stream()
                .filter(menu -> menu.getHarga() >= minHarga && menu.getHarga() <= maxHarga)
                .toList();
    }
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Method untuk menambah menu baru ke sistem.
     * Menggunakan Singleton MenuRepository untuk menyimpan data.
     * 
     * @param namaMenu nama menu baru
     * @param kategori kategori menu
     * @param harga harga menu
     * @param deskripsi deskripsi menu
     * @return true jika berhasil menambahkan, false jika gagal
     */
    public boolean tambahMenu(String namaMenu, String kategori, double harga, String deskripsi) {
        try {
            // Generate ID baru
            String newId = "MNU" + String.format("%03d", getAllMenu().size() + 1);
            
            // Cek apakah menu sudah ada
            boolean menuExists = getAllMenu().stream()
                                           .anyMatch(menu -> menu.getNamaMenu().equalsIgnoreCase(namaMenu.trim()));
            
            if (menuExists) {
                return false; // Menu sudah ada
            }
            
            // Buat menu baru
            Menu menuBaru = new Menu();
            menuBaru.setId(newId);
            menuBaru.setNamaMenu(namaMenu.trim());
            menuBaru.setKategori(kategori.trim());
            menuBaru.setHarga(harga);
            menuBaru.setDeskripsi(deskripsi.trim());
            menuBaru.setTersedia(true); // Menu baru default tersedia
            
            // Tambah ke repository menggunakan Singleton Pattern
            return MenuRepository.getInstance().tambahMenu(menuBaru);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * *** SINGLETON PATTERN DEMONSTRATION ***
     * Method untuk menunjukkan bahwa instance repository selalu sama.
     * Berguna untuk debugging dan memastikan Singleton bekerja dengan benar.
     * 
     * @return informasi tentang Singleton instance
     */
    public String getSingletonInfo() {
        MenuRepository repo1 = MenuRepository.getInstance();
        MenuRepository repo2 = MenuRepository.getInstance();
        
        boolean sameInstance = (repo1 == repo2);
        
        return String.format("Singleton Pattern Status: %s | Instance Hash: %d | %s", 
                           sameInstance ? "WORKING" : "FAILED",
                           repo1.hashCode(),
                           repo1.toString());
    }
}