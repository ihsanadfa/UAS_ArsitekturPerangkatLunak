package com.university.canteen.config;

import com.university.canteen.patterns.singleton.MenuRepository;
import com.university.canteen.model.entity.Menu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataInitializer - Sample Data Population
 * 
 * Component ini akan mengisi data sample menu setelah aplikasi berhasil startup.
 * Data ini menggunakan Singleton Pattern melalui MenuRepository.
 * 
 * @author Student UAS APL
 * @version 1.0
 */
@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Override
    public void run(String... args) throws Exception {
        logger.info("🏗️ Initializing sample data using Singleton Pattern...");
        
        // Menggunakan Singleton Pattern untuk akses data
        MenuRepository menuRepo = MenuRepository.getInstance();
        
        // Cek apakah data sudah ada
        if (menuRepo.getAllMenu().isEmpty()) {
            logger.info("📝 Populating sample menu data...");
            
            // Sample menu data
            menuRepo.addMenu(new Menu("M001", "Nasi Gudeg Yogya", 15000, "Makanan"));
            menuRepo.addMenu(new Menu("M002", "Soto Ayam Lamongan", 12000, "Makanan"));
            menuRepo.addMenu(new Menu("M003", "Gado-Gado Jakarta", 10000, "Makanan"));
            menuRepo.addMenu(new Menu("M004", "Nasi Rawon Surabaya", 18000, "Makanan"));
            menuRepo.addMenu(new Menu("M005", "Bakso Malang Original", 13000, "Makanan"));
            
            menuRepo.addMenu(new Menu("M006", "Es Teh Manis", 3000, "Minuman"));
            menuRepo.addMenu(new Menu("M007", "Es Jeruk Peras", 4000, "Minuman"));
            menuRepo.addMenu(new Menu("M008", "Kopi Hitam", 5000, "Minuman"));
            menuRepo.addMenu(new Menu("M009", "Jus Alpukat", 8000, "Minuman"));
            menuRepo.addMenu(new Menu("M010", "Es Campur Special", 9000, "Minuman"));
            
            menuRepo.addMenu(new Menu("M011", "Keripik Singkong", 5000, "Snack"));
            menuRepo.addMenu(new Menu("M012", "Pisang Goreng", 6000, "Snack"));
            menuRepo.addMenu(new Menu("M013", "Tahu Isi", 4000, "Snack"));
            menuRepo.addMenu(new Menu("M014", "Risoles Mayo", 7000, "Snack"));
            menuRepo.addMenu(new Menu("M015", "Lumpia Semarang", 8000, "Snack"));
            
            // Beberapa menu yang habis untuk demo
            Menu menu16 = new Menu("M016", "Nasi Pecel Madiun", 11000, "Makanan");
            menu16.setTersedia(false);
            menuRepo.addMenu(menu16);
            
            menuRepo.addMenu(new Menu("M017", "Sate Ayam Madura", 16000, "Makanan"));
            
            Menu menu18 = new Menu("M018", "Es Dawet Ayu", 6000, "Minuman");
            menu18.setTersedia(false);
            menuRepo.addMenu(menu18);
            
            menuRepo.addMenu(new Menu("M019", "Martabak Mini", 9000, "Snack"));
            menuRepo.addMenu(new Menu("M020", "Sup Kimlo", 14000, "Makanan"));
            
            logger.info("✅ Sample data populated successfully! Total menus: {}", menuRepo.getAllMenu().size());
        } else {
            logger.info("📊 Data already exists. Total menus: {}", menuRepo.getAllMenu().size());
        }
    }
}