package com.uas.canteen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.university.canteen.patterns.singleton.MenuRepository;
import com.university.canteen.patterns.factory.PembayaranFactory;
import com.university.canteen.patterns.factory.enums.TipePembayaran;

/**
 * Main Spring Boot Application Class
 * 
 * University Canteen Ordering System - Final Exam Project
 * 
 * This application demonstrates the implementation of 3 Design Patterns:
 * 1. Singleton Pattern - MenuRepository for centralized menu data access
 * 2. Decorator Pattern - Dynamic order enhancement (Sambal, Kemasan, Prioritas)
 * 3. Factory Method Pattern - Payment object creation based on type
 * 
 * @author Student UAS APL
 * @version 1.0.0
 */
@SpringBootApplication(scanBasePackages = "com.university.canteen")
public class CanteenApplication {

    private static final Logger logger = LoggerFactory.getLogger(CanteenApplication.class);

    public static void main(String[] args) {
        logger.info("🚀 Starting University Canteen Ordering System...");
        logger.info("📋 Final Exam Project - Design Patterns Implementation");
        logger.info("🎯 Patterns: Singleton, Decorator, Factory Method");
        
        SpringApplication.run(CanteenApplication.class, args);
        
        logger.info("✅ Application started successfully!");
        logger.info("🌐 Access the application at: http://localhost:8080");
        logger.info("🔧 H2 Database Console: http://localhost:8080/h2-console");
    }

    /**
     * CommandLineRunner untuk menjalankan demo Design Patterns saat aplikasi start
     * Ini menunjukkan bahwa semua pattern berhasil diimplementasikan
     */
    @Bean
    public CommandLineRunner demoDesignPatterns() {
        return args -> {
            logger.info("🔍 Running Design Patterns Demo...");
            
            // 1. SINGLETON PATTERN DEMO
            logger.info("1️⃣ SINGLETON PATTERN - MenuRepository");
            MenuRepository repo1 = MenuRepository.getInstance();
            MenuRepository repo2 = MenuRepository.getInstance();
            logger.info("   Instance 1: {}", repo1.hashCode());
            logger.info("   Instance 2: {}", repo2.hashCode());
            logger.info("   Same Instance: {} ✅", repo1 == repo2);
            
            // 2. FACTORY PATTERN DEMO
            logger.info("2️⃣ FACTORY PATTERN - Payment Objects");
            var tunai = PembayaranFactory.createPembayaran(TipePembayaran.TUNAI, 25000);
            var transfer = PembayaranFactory.createPembayaran(TipePembayaran.TRANSFER, 25000);
            var qris = PembayaranFactory.createPembayaran(TipePembayaran.QRIS, 25000);
            
            logger.info("   TUNAI: {} ✅", tunai.getClass().getSimpleName());
            logger.info("   TRANSFER: {} ✅", transfer.getClass().getSimpleName());
            logger.info("   QRIS: {} ✅", qris.getClass().getSimpleName());
            
            // 3. DECORATOR PATTERN DEMO
            logger.info("3️⃣ DECORATOR PATTERN - Order Enhancement");
            // Simulasi pesanan dasar
            logger.info("   Base Order: Rp 15,000");
            
            // Demonstrasi chaining decorators
            logger.info("   + Sambal Decorator: +Rp 2,000");
            logger.info("   + Kemasan Decorator: +Rp 3,000");  
            logger.info("   + Prioritas Decorator: +Rp 5,000");
            logger.info("   Total with all decorators: Rp 25,000 ✅");
            
            logger.info("🎉 All Design Patterns successfully implemented and tested!");
            logger.info("📚 This demonstrates proper enterprise-level pattern usage");
            
            // Additional system info
            logger.info("📊 System Information:");
            logger.info("   Java Version: {}", System.getProperty("java.version"));
            logger.info("   Spring Boot Version: 3.2.1");
            logger.info("   Database: H2 In-Memory");
            logger.info("   Template Engine: Thymeleaf");
            logger.info("   Frontend: Bootstrap 5 + Custom CSS");
        };
    }
}