package com.university.canteen.controller;

import com.university.canteen.model.entity.Menu;
import com.university.canteen.service.MenuService;
import com.university.canteen.service.PesananService;
import com.university.canteen.service.KeranjangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller StafController
 * 
 * Controller untuk dashboard staf kantin yang mendukung:
 * - Requirement R04: Staf dapat memperbarui status ketersediaan menu
 * - Requirement R05: Staf dapat melihat daftar pesanan yang masuk
 * 
 * *** SINGLETON PATTERN INTEGRATION ***
 * Controller ini menggunakan MenuService yang mengakses MenuRepository Singleton
 * untuk operations update status menu.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Controller
@RequestMapping("/staf")
public class StafController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private PesananService pesananService;
    
    @Autowired
    private KeranjangService keranjangService;
    
    /**
     * Dashboard utama staf dengan overview statistik.
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk validation
     * @return view name untuk staf/dashboard.html
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Validasi login sebagai staf
        if (!HomeController.isStaf(session)) {
            return "redirect:/?error=access-denied";
        }
        
        String userName = HomeController.getUserName(session);
        
        try {
            // *** SINGLETON PATTERN - Get statistics via MenuService ***
            String statistikMenu = menuService.getStatistikMenu();
            List<Menu> allMenu = menuService.getAllMenu();
            List<Menu> menuHabis = allMenu.stream()
                                          .filter(menu -> !menu.isTersedia())
                                          .toList();
            
            // *** REQUIREMENT R05 - Get pesanan statistics ***
            List<PesananService.PesananInfo> allPesanan = pesananService.getAllPesanan();
            
            // Statistik keranjang
            String statistikKeranjang = keranjangService.getStatistikKeranjang();
            
            // *** SINGLETON PATTERN DEMO ***
            String singletonInfo = menuService.getSingletonInfo();
            
            // Pass data to view
            model.addAttribute("userName", userName);
            model.addAttribute("statistikMenu", statistikMenu);
            model.addAttribute("totalMenu", allMenu.size());
            model.addAttribute("menuTersedia", allMenu.size() - menuHabis.size());
            model.addAttribute("menuHabis", menuHabis.size());
            model.addAttribute("totalPesanan", allPesanan.size());
            model.addAttribute("statistikKeranjang", statistikKeranjang);
            model.addAttribute("singletonInfo", singletonInfo);
            
            // Recent orders (last 10)
            List<PesananService.PesananInfo> recentOrders = allPesanan.stream()
                                                                     .sorted((a, b) -> b.getWaktuPesanan().compareTo(a.getWaktuPesanan()))
                                                                     .limit(10)
                                                                     .toList();
            model.addAttribute("recentOrders", recentOrders);
            
            return "staf/dashboard";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
            return "staf/dashboard";
        }
    }
    
    /**
     * *** REQUIREMENT R04 - SINGLETON PATTERN INTEGRATION ***
     * Halaman untuk mengelola menu (update status ketersediaan).
     * Menggunakan MenuService yang mengakses MenuRepository Singleton.
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk validation
     * @return view name untuk staf/manage-menu.html
     */
    @GetMapping("/manage-menu")
    public String manageMenu(Model model, HttpSession session) {
        if (!HomeController.isStaf(session)) {
            return "redirect:/?error=access-denied";
        }
        
        try {
            // *** SINGLETON PATTERN - Get all menu via MenuService ***
            List<Menu> allMenu = menuService.getAllMenu();
            List<String> kategoriList = menuService.getKategoriTersedia();
            String statistikMenu = menuService.getStatistikMenu();
            
            // Group menu by category for better display
            var menuByKategori = allMenu.stream()
                                       .collect(java.util.stream.Collectors.groupingBy(Menu::getKategori));
            
            model.addAttribute("allMenu", allMenu);
            model.addAttribute("kategoriList", kategoriList);
            model.addAttribute("menuByKategori", menuByKategori);
            model.addAttribute("statistikMenu", statistikMenu);
            model.addAttribute("userName", HomeController.getUserName(session));
            
            return "staf/manage-menu";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error loading menu: " + e.getMessage());
            return "staf/manage-menu";
        }
    }
    
    /**
     * *** REQUIREMENT R04 IMPLEMENTATION ***
     * Endpoint untuk toggle status ketersediaan menu.
     * Menggunakan Singleton Pattern melalui MenuService.
     * 
     * @param idMenu ID menu yang akan diupdate
     * @param status status baru (tersedia/habis)
     * @param session HttpSession untuk validation
     * @param redirectAttributes untuk flash messages
     * @return redirect kembali ke manage menu
     */
    @PostMapping("/update-menu-status")
    public String updateMenuStatus(@RequestParam("idMenu") String idMenu,
                                  @RequestParam("status") boolean status,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        
        if (!HomeController.isStaf(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        try {
            // *** SINGLETON PATTERN - Update via MenuService -> MenuRepository.getInstance() ***
            boolean berhasil = menuService.updateStatusMenu(idMenu, status);
            
            if (berhasil) {
                // Get menu name untuk success message
                String namaMenu = menuService.getMenuById(idMenu)
                                            .map(Menu::getNamaMenu)
                                            .orElse("Menu");
                
                String statusText = status ? "TERSEDIA" : "HABIS";
                redirectAttributes.addFlashAttribute("success", 
                    String.format("Status menu '%s' berhasil diubah menjadi %s", namaMenu, statusText));
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal mengubah status menu. Menu tidak ditemukan.");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "System error: " + e.getMessage());
        }
        
        return "redirect:/staf/dashboard";
    }
    
    /**
     * *** REQUIREMENT R05 IMPLEMENTATION ***
     * Halaman untuk melihat daftar pesanan yang masuk secara berurutan.
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk validation
     * @return view name untuk staf/orders.html
     */
    @GetMapping("/orders")
    public String viewOrders(Model model, HttpSession session) {
        if (!HomeController.isStaf(session)) {
            return "redirect:/?error=access-denied";
        }
        
        try {
            // *** REQUIREMENT R05 - Get all orders in sequence ***
            List<PesananService.PesananInfo> allOrders = pesananService.getAllPesanan();
            
            // Group orders by today, yesterday, older
            var today = java.time.LocalDate.now();
            var yesterday = today.minusDays(1);
            
            var todayOrders = allOrders.stream()
                                     .filter(order -> order.getWaktuPesanan().toLocalDate().equals(today))
                                     .toList();
            
            var yesterdayOrders = allOrders.stream()
                                          .filter(order -> order.getWaktuPesanan().toLocalDate().equals(yesterday))
                                          .toList();
            
            var olderOrders = allOrders.stream()
                                      .filter(order -> order.getWaktuPesanan().toLocalDate().isBefore(yesterday))
                                      .toList();
            
            // Statistics
            double totalRevenue = allOrders.stream()
                                          .mapToDouble(PesananService.PesananInfo::getHargaFinal)
                                          .sum();
            
            double todayRevenue = todayOrders.stream()
                                           .mapToDouble(PesananService.PesananInfo::getHargaFinal)
                                           .sum();
            
            model.addAttribute("allOrders", allOrders);
            model.addAttribute("todayOrders", todayOrders);
            model.addAttribute("yesterdayOrders", yesterdayOrders);
            model.addAttribute("olderOrders", olderOrders);
            model.addAttribute("totalOrders", allOrders.size());
            model.addAttribute("todayOrdersCount", todayOrders.size());
            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("todayRevenue", todayRevenue);
            model.addAttribute("userName", HomeController.getUserName(session));
            
            return "staf/orders";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error loading orders: " + e.getMessage());
            return "staf/orders";
        }
    }
    
    /**
     * Halaman untuk melihat detail pesanan spesifik.
     * 
     * @param idPesanan ID pesanan yang akan dilihat
     * @param model Model untuk passing data
     * @param session HttpSession untuk validation
     * @return view dengan detail pesanan
     */
    @GetMapping("/orders/{idPesanan}")
    public String orderDetail(@PathVariable("idPesanan") String idPesanan,
                             Model model,
                             HttpSession session) {
        
        if (!HomeController.isStaf(session)) {
            return "redirect:/?error=access-denied";
        }
        
        var pesananOpt = pesananService.getPesananById(idPesanan);
        
        if (pesananOpt.isEmpty()) {
            return "redirect:/staf/orders?error=order-not-found";
        }
        
        model.addAttribute("pesanan", pesananOpt.get());
        model.addAttribute("userName", HomeController.getUserName(session));
        
        return "staf/order-detail";
    }
    
    /**
     * *** DESIGN PATTERNS ANALYSIS FOR STAFF ***
     * Halaman analisis penggunaan Design Patterns untuk keperluan educational.
     * 
     * @param model Model untuk passing data
     * @param session HttpSession untuk validation
     * @return view dengan analisis patterns
     */
    @GetMapping("/patterns-analysis")
    public String patternsAnalysis(Model model, HttpSession session) {
        if (!HomeController.isStaf(session)) {
            return "redirect:/?error=access-denied";
        }
        
        try {
            // *** SINGLETON PATTERN ANALYSIS ***
            String singletonAnalysis = menuService.getSingletonInfo();
            
            // *** DECORATOR & FACTORY PATTERN ANALYSIS ***
            String patternDemo = pesananService.demonstrasiDesignPatterns();
            
            // Menu statistics via Singleton
            String menuStats = menuService.getStatistikMenu();
            
            // Order statistics showing pattern usage
            List<PesananService.PesananInfo> allOrders = pesananService.getAllPesanan();
            
            // Analyze decorator usage in orders
            long ordersWithSambal = allOrders.stream()
                                           .filter(order -> order.getOpsiTambahan().contains("SAMBAL"))
                                           .count();
            
            long ordersWithKemasan = allOrders.stream()
                                            .filter(order -> order.getOpsiTambahan().contains("KEMASAN"))
                                            .count();
            
            long ordersWithPrioritas = allOrders.stream()
                                              .filter(order -> order.getOpsiTambahan().contains("PRIORITAS"))
                                              .count();
            
            // Analyze factory usage (payment types)
            var paymentTypes = allOrders.stream()
                                      .collect(java.util.stream.Collectors.groupingBy(
                                          order -> order.getPembayaran().getTipePembayaran().getDisplayName(),
                                          java.util.stream.Collectors.counting()
                                      ));
            
            model.addAttribute("singletonAnalysis", singletonAnalysis);
            model.addAttribute("patternDemo", patternDemo);
            model.addAttribute("menuStats", menuStats);
            model.addAttribute("totalOrders", allOrders.size());
            model.addAttribute("ordersWithSambal", ordersWithSambal);
            model.addAttribute("ordersWithKemasan", ordersWithKemasan);
            model.addAttribute("ordersWithPrioritas", ordersWithPrioritas);
            model.addAttribute("paymentTypes", paymentTypes);
            model.addAttribute("userName", HomeController.getUserName(session));
            
            return "staf/patterns-analysis";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error generating analysis: " + e.getMessage());
            return "staf/patterns-analysis";
        }
    }
    
    /**
     * Handler untuk menambahkan menu baru dari dashboard.
     * 
     * @param namaMenu nama menu baru
     * @param kategori kategori menu
     * @param harga harga menu
     * @param deskripsi deskripsi menu
     * @param session HttpSession untuk validation
     * @param redirectAttributes untuk flash messages
     * @return redirect ke dashboard
     */
    @PostMapping("/add-menu")
    public String addMenu(@RequestParam("namaMenu") String namaMenu,
                         @RequestParam("kategori") String kategori,
                         @RequestParam("harga") double harga,
                         @RequestParam(value = "deskripsi", defaultValue = "") String deskripsi,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        
        if (!HomeController.isStaf(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        try {
            // Validasi input
            if (namaMenu == null || namaMenu.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Nama menu tidak boleh kosong!");
                return "redirect:/staf/dashboard";
            }
            
            if (harga <= 0) {
                redirectAttributes.addFlashAttribute("error", "Harga menu harus lebih dari 0!");
                return "redirect:/staf/dashboard";
            }
            
            // Tambah menu via MenuService (Singleton Pattern)
            boolean berhasil = menuService.tambahMenu(namaMenu, kategori, harga, deskripsi);
            
            if (berhasil) {
                redirectAttributes.addFlashAttribute("success", 
                    String.format("Menu '%s' berhasil ditambahkan dengan harga Rp%.0f", namaMenu, harga));
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal menambahkan menu. Menu mungkin sudah ada.");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "System error: " + e.getMessage());
        }
        
        return "redirect:/staf/dashboard";
    }
    
    /**
     * Handler untuk export report (dummy implementation).
     * 
     * @param session HttpSession untuk validation
     * @param redirectAttributes untuk flash messages
     * @return redirect ke dashboard dengan pesan
     */
    @GetMapping("/export-report")
    public String exportReport(HttpSession session, RedirectAttributes redirectAttributes) {
        if (!HomeController.isStaf(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        try {
            // Dummy report generation
            String reportContent = generateDummyReport();
            
            // For now, just show success message
            // In real implementation, this could return ResponseEntity<byte[]> for file download
            redirectAttributes.addFlashAttribute("success", 
                "Report berhasil di-generate! Feature download akan segera tersedia.");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error generating report: " + e.getMessage());
        }
        
        return "redirect:/staf/dashboard";
    }
    
    /**
     * Helper method untuk generate dummy report content.
     */
    private String generateDummyReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== LAPORAN PENJUALAN KANTIN UNIVERSITAS ===\n");
        report.append("Tanggal: ").append(java.time.LocalDate.now()).append("\n\n");
        
        try {
            List<PesananService.PesananInfo> allOrders = pesananService.getAllPesanan();
            List<Menu> allMenu = menuService.getAllMenu();
            
            report.append("STATISTIK UMUM:\n");
            report.append("- Total Menu: ").append(allMenu.size()).append("\n");
            report.append("- Total Pesanan: ").append(allOrders.size()).append("\n");
            
            double totalPendapatan = allOrders.stream()
                                            .mapToDouble(PesananService.PesananInfo::getHargaFinal)
                                            .sum();
            report.append("- Total Pendapatan: Rp").append(String.format("%.0f", totalPendapatan)).append("\n\n");
            
            report.append("DESIGN PATTERNS USAGE:\n");
            report.append("- Singleton Pattern: MenuRepository instance active\n");
            report.append("- Decorator Pattern: Order customizations available\n");
            report.append("- Factory Method: Payment processing implemented\n\n");
            
            report.append("=== END OF REPORT ===");
            
        } catch (Exception e) {
            report.append("Error generating detailed report: ").append(e.getMessage());
        }
        
        return report.toString();
    }
}