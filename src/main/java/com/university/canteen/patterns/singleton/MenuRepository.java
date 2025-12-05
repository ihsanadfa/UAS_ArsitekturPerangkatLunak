package com.university.canteen.patterns.singleton;

import com.university.canteen.model.entity.Menu;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class MenuRepository (Singleton Pattern Implementation)
 * 
 * Class ini mengimplementasikan Singleton Pattern untuk memastikan hanya ada
 * satu instance repository yang mengelola data menu dalam sistem.
 * 
 * Menggunakan Bill Pugh Singleton design pattern yang thread-safe dan lazy loading.
 * Pattern ini memanfaatkan inner static class untuk memastikan thread safety
 * tanpa menggunakan synchronized method yang dapat menurunkan performa.
 * 
 * Fungsi utama class ini:
 * - Menyimpan data menu dalam List sebagai simulasi database in-memory
 * - Menyediakan operasi CRUD untuk data menu
 * - Mendukung Requirement R04: update status ketersediaan menu
 * - Memastikan data consistency dalam single instance
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
public class MenuRepository {
    
    /** 
     * List untuk menyimpan data menu sebagai simulasi database in-memory.
     * Menggunakan ArrayList untuk kemudahan akses dan manipulasi data.
     */
    private List<Menu> menuList;
    
    /**
     * Private constructor untuk mencegah instantiation dari luar class.
     * Constructor ini akan menginisialisasi menuList dan mengisi dengan data dummy.
     */
    private MenuRepository() {
        this.menuList = new ArrayList<>();
        initializeData();
    }
    
    /**
     * Inner static class untuk implementasi Bill Pugh Singleton Pattern.
     * Class ini akan dimuat hanya ketika getInstance() dipanggil pertama kali,
     * sehingga memberikan lazy loading dan thread safety.
     */
    private static class SingletonHelper {
        /** 
         * Instance tunggal dari MenuRepository.
         * Static final untuk memastikan immutability setelah inisialisasi.
         */
        private static final MenuRepository INSTANCE = new MenuRepository();
    }
    
    /**
     * Method public static untuk mendapatkan instance tunggal MenuRepository.
     * Menggunakan Bill Pugh pattern untuk thread safety tanpa synchronized overhead.
     * 
     * @return instance tunggal dari MenuRepository
     */
    public static MenuRepository getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    /**
     * Method private untuk menginisialisasi data dummy menu kantin universitas.
     * Data ini akan diisi saat instance pertama kali dibuat.
     * 
     * Menu yang diisi mencakup berbagai kategori:
     * - Makanan Utama: Nasi Gudeg, Ayam Geprek, Gado-gado
     * - Minuman: Es Teh Manis, Es Jeruk, Kopi Hitam
     * - Cemilan: Pisang Goreng, Tahu Isi
     */
    private void initializeData() {
        // Kategori Makanan Utama
        menuList.add(new Menu("M001", "Nasi Gudeg", 15000.0, "Makanan Utama", true));
        menuList.add(new Menu("M002", "Ayam Geprek", 18000.0, "Makanan Utama", true));
        menuList.add(new Menu("M003", "Gado-gado", 12000.0, "Makanan Utama", true));
        menuList.add(new Menu("M004", "Nasi Goreng Spesial", 16000.0, "Makanan Utama", true));
        menuList.add(new Menu("M005", "Mie Ayam", 13000.0, "Makanan Utama", false)); // habis
        
        // Kategori Minuman
        menuList.add(new Menu("D001", "Es Teh Manis", 4000.0, "Minuman", true));
        menuList.add(new Menu("D002", "Es Jeruk", 6000.0, "Minuman", true));
        menuList.add(new Menu("D003", "Kopi Hitam", 5000.0, "Minuman", true));
        menuList.add(new Menu("D004", "Jus Alpukat", 8000.0, "Minuman", true));
        menuList.add(new Menu("D005", "Es Cendol", 7000.0, "Minuman", false)); // habis
        
        // Kategori Cemilan
        menuList.add(new Menu("S001", "Pisang Goreng", 8000.0, "Cemilan", true));
        menuList.add(new Menu("S002", "Tahu Isi", 6000.0, "Cemilan", true));
        menuList.add(new Menu("S003", "Bakwan Jagung", 5000.0, "Cemilan", true));
    }
    
    /**
     * Mendapatkan semua menu yang tersedia dalam sistem.
     * Method ini mendukung Requirement R01: menampilkan daftar menu.
     * 
     * @return List berisi semua menu dalam repository
     */
    public List<Menu> getAllMenu() {
        // Return copy untuk mencegah external modification
        return new ArrayList<>(menuList);
    }
    
    /**
     * Mendapatkan menu berdasarkan ID.
     * 
     * @param id identitas unik menu yang dicari
     * @return Optional berisi menu jika ditemukan, empty jika tidak
     */
    public Optional<Menu> getMenuById(String id) {
        return menuList.stream()
                      .filter(menu -> menu.getId().equals(id))
                      .findFirst();
    }
    
    /**
     * Mendapatkan menu berdasarkan kategori.
     * 
     * @param kategori kategori menu yang dicari
     * @return List berisi menu dalam kategori tersebut
     */
    public List<Menu> getMenuByKategori(String kategori) {
        return menuList.stream()
                      .filter(menu -> menu.getKategori().equalsIgnoreCase(kategori))
                      .toList();
    }
    
    /**
     * Mendapatkan menu yang tersedia saja (status = true).
     * Method ini berguna untuk menampilkan menu yang bisa dipesan.
     * 
     * @return List berisi menu yang statusKetersediaannya true
     */
    public List<Menu> getMenuTersedia() {
        return menuList.stream()
                      .filter(Menu::isTersedia)
                      .toList();
    }
    
    /**
     * Update status ketersediaan menu berdasarkan ID.
     * Method ini mendukung Requirement R04: staf dapat memperbarui status menu.
     * 
     * @param id identitas menu yang akan diupdate
     * @param status status baru (true = tersedia, false = habis)
     * @return true jika update berhasil, false jika menu tidak ditemukan
     */
    public boolean updateStatusMenu(String id, boolean status) {
        Optional<Menu> menuOpt = getMenuById(id);
        if (menuOpt.isPresent()) {
            Menu menu = menuOpt.get();
            menu.setStatusKetersediaan(status);
            return true;
        }
        return false;
    }
    
    /**
     * Menambahkan menu baru ke repository.
     * 
     * @param menu objek Menu yang akan ditambahkan
     * @return true jika berhasil ditambahkan, false jika ID sudah ada
     */
    public boolean addMenu(Menu menu) {
        // Cek apakah ID sudah ada
        if (getMenuById(menu.getId()).isPresent()) {
            return false; // ID sudah ada
        }
        return menuList.add(menu);
    }
    
    /**
     * Menghapus menu berdasarkan ID.
     * 
     * @param id identitas menu yang akan dihapus
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean removeMenu(String id) {
        return menuList.removeIf(menu -> menu.getId().equals(id));
    }
    
    /**
     * Mendapatkan jumlah total menu dalam repository.
     * 
     * @return jumlah menu
     */
    public int getTotalMenu() {
        return menuList.size();
    }
    
    /**
     * Mendapatkan jumlah menu yang tersedia.
     * 
     * @return jumlah menu dengan status tersedia
     */
    public long getTotalMenuTersedia() {
        return menuList.stream()
                      .filter(Menu::isTersedia)
                      .count();
    }
    
    /**
     * Override toString() untuk debugging dan monitoring instance.
     * 
     * @return informasi tentang repository
     */
    @Override
    public String toString() {
        return String.format("MenuRepository{totalMenu=%d, menuTersedia=%d}", 
                           getTotalMenu(), getTotalMenuTersedia());
    }
}