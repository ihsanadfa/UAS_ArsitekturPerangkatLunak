package com.university.canteen.service;

import com.university.canteen.model.entity.*;
import com.university.canteen.patterns.decorator.*;
import com.university.canteen.patterns.factory.Pembayaran;
import com.university.canteen.patterns.factory.PembayaranFactory;
import com.university.canteen.patterns.factory.enums.TipePembayaran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Service PesananService - CRITICAL PATTERN INTEGRATION
 * 
 * Service class yang mendemonstrasikan integrasi SEMUA Design Patterns:
 * 1. DECORATOR PATTERN: Untuk menambahkan opsi tambahan pada pesanan
 * 2. FACTORY METHOD PATTERN: Untuk membuat objek pembayaran
 * 3. SINGLETON PATTERN: Melalui MenuService untuk akses data menu
 * 
 * *** DESIGN PATTERNS INTEGRATION SHOWCASE ***
 * Method buatPesanan() adalah showcase utama yang menunjukkan:
 * - Konversi Keranjang ke Pesanan (Basic Component)
 * - Wrapping dengan Decorator berdasarkan opsi yang dipilih
 * - Factory untuk membuat objek Pembayaran
 * - Generate nomor antrean (Requirement R03)
 * 
 * OCL Implementation:
 * - buatPesanan: pre: keranjang tidak kosong; post: pesanan dibuat dengan nomor antrean
 * - generateNomorAntrean: post: nomor antrean unik
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Service
public class PesananService {
    
    @Autowired
    private KeranjangService keranjangService;
    
    @Autowired
    private MenuService menuService;
    
    /** 
     * In-memory storage untuk pesanan yang sudah dibuat.
     * Key = idPesanan, Value = informasi pesanan lengkap
     */
    private final Map<String, PesananInfo> pesananMap = new HashMap<>();
    
    /** 
     * Set untuk tracking nomor antrean yang sudah digunakan.
     * Memastikan OCL post-condition: nomor antrean unik.
     */
    private final Set<String> nomorAntreanTerpakai = new HashSet<>();
    
    /**
     * *** CRITICAL METHOD - ALL PATTERNS INTEGRATION ***
     * 
     * Method utama yang mendemonstrasikan penggunaan SEMUA Design Patterns:
     * 
     * STEP 1: Convert Keranjang to Pesanan (Basic Component)
     * STEP 2: DECORATOR PATTERN - Wrap dengan decorator berdasarkan opsi
     * STEP 3: Calculate final price menggunakan pesanan.hitungTotal()
     * STEP 4: FACTORY PATTERN - Create Pembayaran object
     * STEP 5: Generate Queue Number (Requirement R03)
     * 
     * @param idPengguna ID pengguna yang melakukan pesanan
     * @param opsiTambahan List opsi tambahan ("Sambal", "Kemasan", "Prioritas")
     * @param tipeBayar Tipe pembayaran yang dipilih
     * @return PesananInfo berisi detail pesanan lengkap
     */
    public PesananInfo buatPesanan(String idPengguna, 
                                  List<String> opsiTambahan, 
                                  TipePembayaran tipeBayar) {
        
        // ===== VALIDATION & OCL PRE-CONDITION =====
        if (idPengguna == null || idPengguna.trim().isEmpty()) {
            throw new IllegalArgumentException("ID pengguna tidak boleh kosong");
        }
        
        if (tipeBayar == null) {
            throw new IllegalArgumentException("Tipe pembayaran tidak boleh null");
        }
        
        // OCL Pre-condition: keranjang tidak boleh kosong
        if (keranjangService.isKeranjangKosong(idPengguna)) {
            throw new IllegalArgumentException("OCL Pre-condition gagal: Keranjang kosong, tidak dapat membuat pesanan");
        }
        
        // Validasi keranjang untuk checkout (cek ketersediaan menu)
        keranjangService.validasiKeranjangUntukCheckout(idPengguna);
        
        Keranjang keranjang = keranjangService.getKeranjangPengguna(idPengguna);
        
        // ===== STEP 1: CONVERT KERANJANG TO PESANAN (Basic Component) =====
        System.out.println("=== STEP 1: Creating Basic Pesanan Component ===");
        
        // Convert ItemKeranjang ke ItemPesanan untuk snapshot
        List<ItemPesanan> daftarItemPesanan = new ArrayList<>();
        for (ItemKeranjang itemKeranjang : keranjang.getDaftarItem()) {
            ItemPesanan itemPesanan = new ItemPesanan(itemKeranjang);
            daftarItemPesanan.add(itemPesanan);
            System.out.println("Converted: " + itemPesanan.getRingkasan());
        }
        
        // Buat Pesanan dasar (Concrete Component untuk Decorator Pattern)
        double hargaDasar = keranjang.hitungTotalHarga();
        String deskripsiDasar = "Pesanan dari " + daftarItemPesanan.size() + " item menu";
        
        // *** DECORATOR PATTERN - BASIC COMPONENT ***
        IPesanan pesananDecorated = new Pesanan(deskripsiDasar, hargaDasar);
        
        System.out.println("Basic Component Created: " + pesananDecorated.getDeskripsi());
        System.out.println("Basic Price: Rp " + pesananDecorated.hitungTotal());
        
        // ===== STEP 2: DECORATOR PATTERN IMPLEMENTATION =====
        System.out.println("\n=== STEP 2: Applying Decorator Pattern ===");
        
        if (opsiTambahan != null && !opsiTambahan.isEmpty()) {
            
            for (String opsi : opsiTambahan) {
                switch (opsi.toUpperCase()) {
                    
                    case "SAMBAL" -> {
                        // *** DECORATOR PATTERN - SAMBAL DECORATOR ***
                        System.out.println("Applying SambalDecorator...");
                        pesananDecorated = new SambalDecorator(pesananDecorated);
                        System.out.println("After Sambal: " + pesananDecorated.getDeskripsi());
                        System.out.println("New Price: Rp " + pesananDecorated.hitungTotal());
                    }
                    
                    case "KEMASAN" -> {
                        // *** DECORATOR PATTERN - KEMASAN DECORATOR ***
                        System.out.println("Applying KemasanKhususDecorator...");
                        pesananDecorated = new KemasanKhususDecorator(pesananDecorated);
                        System.out.println("After Kemasan: " + pesananDecorated.getDeskripsi());
                        System.out.println("New Price: Rp " + pesananDecorated.hitungTotal());
                    }
                    
                    case "PRIORITAS" -> {
                        // *** DECORATOR PATTERN - PRIORITAS DECORATOR ***
                        System.out.println("Applying PrioritasDecorator...");
                        pesananDecorated = new PrioritasDecorator(pesananDecorated);
                        System.out.println("After Prioritas: " + pesananDecorated.getDeskripsi());
                        System.out.println("New Price: Rp " + pesananDecorated.hitungTotal());
                    }
                    
                    default -> {
                        System.out.println("Unknown option: " + opsi + " - Skipping...");
                    }
                }
            }
        } else {
            System.out.println("No additional options selected.");
        }
        
        // ===== STEP 3: CALCULATE FINAL PRICE ===== 
        System.out.println("\n=== STEP 3: Final Price Calculation ===");
        double hargaFinal = pesananDecorated.hitungTotal();
        String deskripsiFinal = pesananDecorated.getDeskripsi();
        
        System.out.println("Final Description: " + deskripsiFinal);
        System.out.println("Final Price: Rp " + hargaFinal);
        
        // ===== STEP 4: FACTORY METHOD PATTERN IMPLEMENTATION =====
        System.out.println("\n=== STEP 4: Factory Method Pattern - Creating Payment ===");
        
        // *** FACTORY METHOD PATTERN - CREATE PEMBAYARAN ***
        Pembayaran pembayaran = PembayaranFactory.createPembayaran(tipeBayar, hargaFinal);
        
        System.out.println("Payment Created via Factory:");
        System.out.println("Type: " + pembayaran.getTipePembayaran().getDisplayName());
        System.out.println("Payment ID: " + pembayaran.getIdPembayaran());
        System.out.println("Amount: Rp " + pembayaran.getJumlahBayar());
        
        // ===== STEP 5: GENERATE QUEUE NUMBER (Requirement R03) =====
        System.out.println("\n=== STEP 5: Generate Queue Number (R03) ===");
        
        String nomorAntrean = generateNomorAntrean();
        String idPesanan = generateIdPesanan();
        
        System.out.println("Queue Number Generated: " + nomorAntrean);
        System.out.println("Order ID Generated: " + idPesanan);
        
        // ===== CREATE FINAL PESANAN INFO =====
        // Normalize opsiTambahan to uppercase for consistent display
        List<String> normalizedOpsiTambahan = new ArrayList<>();
        if (opsiTambahan != null && !opsiTambahan.isEmpty()) {
            for (String opsi : opsiTambahan) {
                if (opsi != null && !opsi.trim().isEmpty()) {
                    normalizedOpsiTambahan.add(opsi.trim().toUpperCase());
                }
            }
        }
        
        PesananInfo pesananInfo = new PesananInfo(
            idPesanan,
            idPengguna,
            nomorAntrean,
            daftarItemPesanan,
            normalizedOpsiTambahan,
            hargaDasar,
            hargaFinal,
            deskripsiFinal,
            pembayaran,
            LocalDateTime.now()
        );
        
        // DEBUG: Verify PesananInfo was created correctly
        System.out.println("=== VERIFICATION: PesananInfo.getOpsiTambahan(): " + pesananInfo.getOpsiTambahan());
        
        // Simpan pesanan ke storage
        pesananMap.put(idPesanan, pesananInfo);
        
        // Kosongkan keranjang setelah checkout (OCL)
        keranjangService.kosongkanKeranjang(idPengguna);
        
        System.out.println("\n=== ORDER CREATION COMPLETED ===");
        System.out.println("All Design Patterns Successfully Applied!");
        
        // OCL Post-condition: pesanan harus tersimpan dengan nomor antrean unik
        if (!pesananMap.containsKey(idPesanan) || nomorAntrean == null) {
            throw new RuntimeException("OCL Post-condition gagal: pesanan tidak berhasil dibuat");
        }
        
        return pesananInfo;
    }
    
    /**
     * *** OCL IMPLEMENTATION ***
     * Generate nomor antrean yang unik.
     * OCL: post: result belum pernah digunakan sebelumnya.
     * 
     * @return string nomor antrean unik
     */
    public String generateNomorAntrean() {
        String nomorAntrean;
        int attempt = 0;
        
        do {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String tanggal = LocalDateTime.now().format(formatter);
            
            // Format: Q-YYYYMMDD-### (### = counter)
            int counter = nomorAntreanTerpakai.size() + 1 + attempt;
            nomorAntrean = String.format("Q-%s-%03d", tanggal, counter);
            
            attempt++;
            
            // Prevent infinite loop
            if (attempt > 1000) {
                throw new RuntimeException("Gagal generate nomor antrean unik setelah 1000 percobaan");
            }
            
        } while (nomorAntreanTerpakai.contains(nomorAntrean));
        
        // OCL Post-condition: nomor antrean harus unik
        if (nomorAntreanTerpakai.contains(nomorAntrean)) {
            throw new RuntimeException("OCL Post-condition gagal: nomor antrean tidak unik");
        }
        
        nomorAntreanTerpakai.add(nomorAntrean);
        return nomorAntrean;
    }
    
    /**
     * Generate ID pesanan yang unik.
     * 
     * @return string ID pesanan
     */
    private String generateIdPesanan() {
        long timestamp = System.currentTimeMillis();
        int random = (int) (Math.random() * 1000);
        return String.format("ORDER-%d-%03d", timestamp, random);
    }
    
    /**
     * Method untuk mendapatkan pesanan berdasarkan ID.
     * 
     * @param idPesanan ID pesanan yang dicari
     * @return Optional berisi PesananInfo jika ditemukan
     */
    public Optional<PesananInfo> getPesananById(String idPesanan) {
        return Optional.ofNullable(pesananMap.get(idPesanan));
    }
    
    /**
     * Method untuk mendapatkan pesanan berdasarkan nomor antrean.
     * 
     * @param nomorAntrean nomor antrean yang dicari
     * @return Optional berisi PesananInfo jika ditemukan
     */
    public Optional<PesananInfo> getPesananByNomorAntrean(String nomorAntrean) {
        return pesananMap.values().stream()
                .filter(p -> p.getNomorAntrean().equals(nomorAntrean))
                .findFirst();
    }
    
    /**
     * Method untuk mendapatkan semua pesanan.
     * Mendukung Requirement R05: Staf dapat melihat daftar pesanan.
     * 
     * @return List berisi semua pesanan yang terurut berdasarkan waktu
     */
    public List<PesananInfo> getAllPesanan() {
        return pesananMap.values().stream()
                .sorted(Comparator.comparing(PesananInfo::getWaktuPesanan))
                .toList();
    }
    
    /**
     * Method untuk mendapatkan pesanan berdasarkan pengguna.
     * 
     * @param idPengguna ID pengguna
     * @return List pesanan dari pengguna tersebut
     */
    public List<PesananInfo> getPesananByPengguna(String idPengguna) {
        return pesananMap.values().stream()
                .filter(p -> p.getIdPengguna().equals(idPengguna))
                .sorted(Comparator.comparing(PesananInfo::getWaktuPesanan).reversed())
                .toList();
    }
    
    /**
     * Method untuk demo semua Design Patterns.
     * Berguna untuk testing dan presentasi.
     * 
     * @return string berisi demonstrasi pattern
     */
    public String demonstrasiDesignPatterns() {
        StringBuilder demo = new StringBuilder();
        
        demo.append("=== DESIGN PATTERNS DEMONSTRATION ===\n\n");
        
        // 1. Singleton Pattern Demo
        demo.append("1. SINGLETON PATTERN:\n");
        demo.append(menuService.getSingletonInfo()).append("\n\n");
        
        // 2. Decorator Pattern Demo
        demo.append("2. DECORATOR PATTERN:\n");
        IPesanan pesananDemo = new Pesanan("Demo Nasi Gudeg", 15000);
        demo.append("Basic: ").append(pesananDemo.getDeskripsi()).append(" - Rp").append(pesananDemo.hitungTotal()).append("\n");
        
        pesananDemo = new SambalDecorator(pesananDemo);
        demo.append("+ Sambal: ").append(pesananDemo.getDeskripsi()).append(" - Rp").append(pesananDemo.hitungTotal()).append("\n");
        
        pesananDemo = new KemasanKhususDecorator(pesananDemo);
        demo.append("+ Kemasan: ").append(pesananDemo.getDeskripsi()).append(" - Rp").append(pesananDemo.hitungTotal()).append("\n");
        
        pesananDemo = new PrioritasDecorator(pesananDemo);
        demo.append("+ Prioritas: ").append(pesananDemo.getDeskripsi()).append(" - Rp").append(pesananDemo.hitungTotal()).append("\n\n");
        
        // 3. Factory Pattern Demo
        demo.append("3. FACTORY METHOD PATTERN:\n");
        for (TipePembayaran tipe : TipePembayaran.values()) {
            Pembayaran bayar = PembayaranFactory.createPembayaran(tipe, 20000);
            demo.append("Factory created: ").append(bayar.getClass().getSimpleName())
                .append(" for ").append(tipe.getDisplayName()).append("\n");
        }
        
        return demo.toString();
    }
    
    /**
     * Inner class untuk menyimpan informasi pesanan lengkap.
     */
    public static class PesananInfo {
        private String idPesanan;
        private String idPengguna;
        private String nomorAntrean;
        private List<ItemPesanan> daftarItem;
        private List<String> opsiTambahan;
        private double hargaDasar;
        private double hargaFinal;
        private String deskripsiLengkap;
        private Pembayaran pembayaran;
        private LocalDateTime waktuPesanan;
        
        public PesananInfo(String idPesanan, String idPengguna, String nomorAntrean,
                          List<ItemPesanan> daftarItem, List<String> opsiTambahan,
                          double hargaDasar, double hargaFinal, String deskripsiLengkap,
                          Pembayaran pembayaran, LocalDateTime waktuPesanan) {
            this.idPesanan = idPesanan;
            this.idPengguna = idPengguna;
            this.nomorAntrean = nomorAntrean;
            this.daftarItem = daftarItem;
            
            // CRITICAL FIX: Create defensive copy to avoid reference issues
            if (opsiTambahan != null) {
                this.opsiTambahan = new ArrayList<>(opsiTambahan);
                System.out.println("=== PESANAN INFO: Stored opsiTambahan: " + this.opsiTambahan);
            } else {
                this.opsiTambahan = new ArrayList<>();
                System.out.println("=== PESANAN INFO: opsiTambahan was null, created empty list");
            }
            
            this.hargaDasar = hargaDasar;
            this.hargaFinal = hargaFinal;
            this.deskripsiLengkap = deskripsiLengkap;
            this.pembayaran = pembayaran;
            this.waktuPesanan = waktuPesanan;
        }
        
        // Getters
        public String getIdPesanan() { return idPesanan; }
        public String getIdPengguna() { return idPengguna; }
        public String getNomorAntrean() { return nomorAntrean; }
        public List<ItemPesanan> getDaftarItem() { return daftarItem; }
        public List<String> getOpsiTambahan() { return opsiTambahan; }
        public double getHargaDasar() { return hargaDasar; }
        public double getHargaFinal() { return hargaFinal; }
        public String getDeskripsiLengkap() { return deskripsiLengkap; }
        public Pembayaran getPembayaran() { return pembayaran; }
        public LocalDateTime getWaktuPesanan() { return waktuPesanan; }
        
        public String getRingkasan() {
            return String.format("Pesanan %s | Antrean: %s | Total: Rp%.0f | %s", 
                               idPesanan, nomorAntrean, hargaFinal, 
                               pembayaran.getTipePembayaran().getDisplayName());
        }
        
        // Add getter for deskripsi as alias for deskripsiLengkap (used by debug)
        public String getDeskripsi() { return deskripsiLengkap; }
        
        // Add getter for waktuPesan as alias for waktuPesanan (used by debug)
        public LocalDateTime getWaktuPesan() { return waktuPesanan; }
    }
    
    /**
     * Get all orders for debugging purposes
     * @return Map of all stored orders
     */
    public Map<String, PesananInfo> getSemuaPesanan() {
        return new HashMap<>(pesananMap); // Return defensive copy
    }
}