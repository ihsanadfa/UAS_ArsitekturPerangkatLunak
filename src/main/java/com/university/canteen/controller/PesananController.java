package com.university.canteen.controller;

import com.university.canteen.model.entity.Keranjang;
import com.university.canteen.service.KeranjangService;
import com.university.canteen.service.PesananService;
import com.university.canteen.patterns.factory.enums.TipePembayaran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Controller PesananController - CRITICAL ALL PATTERNS INTEGRATION
 * 
 * Controller ini adalah showcase utama yang mendemonstrasikan integrasi
 * SEMUA Design Patterns dalam satu flow checkout:
 * 
 * 1. DECORATOR PATTERN: User pilih opsi tambahan (Sambal, Kemasan, Prioritas)
 * 2. FACTORY METHOD PATTERN: User pilih tipe pembayaran, factory create object
 * 3. SINGLETON PATTERN: Akses data menu melalui MenuService wrapper
 * 
 * Flow utama:
 * /konfirmasi -> Form pilih opsi & payment -> /submit -> buatPesanan() -> /success
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Controller
@RequestMapping("/pesanan")
public class PesananController {
    
    @Autowired
    private PesananService pesananService;
    
    @Autowired
    private KeranjangService keranjangService;
    
    /**
     * *** CRITICAL PAGE - PATTERN SELECTION UI ***
     * Halaman konfirmasi pesanan dimana user memilih:
     * 1. Extra Options untuk Decorator Pattern (Sambal, Kemasan, Prioritas)
     * 2. Payment Type untuk Factory Method Pattern (TUNAI, QRIS, TRANSFER)
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk user validation
     * @return view name untuk pesanan/konfirmasi.html
     */
    @GetMapping("/konfirmasi")
    public String konfirmasiPesanan(Model model, HttpSession session) {
        // Validasi login sebagai mahasiswa
        if (!HomeController.isMahasiswa(session)) {
            return "redirect:/?error=access-denied";
        }
        
        String userId = HomeController.getUserId(session);
        String userName = HomeController.getUserName(session);
        
        try {
            // Validasi keranjang tidak kosong
            if (keranjangService.isKeranjangKosong(userId)) {
                return "redirect:/keranjang?error=empty-cart";
            }
            
            // Validasi keranjang untuk checkout
            keranjangService.validasiKeranjangUntukCheckout(userId);
            
            // Get keranjang data
            Keranjang keranjang = keranjangService.getKeranjangPengguna(userId);
            double totalHargaDasar = keranjangService.hitungTotalKeranjang(userId);
            
            // *** DECORATOR PATTERN PREVIEW ***
            // Hitung harga dengan berbagai kombinasi opsi
            double hargaDenganSambal = totalHargaDasar + 1500;           // +Sambal
            double hargaDenganKemasan = totalHargaDasar + 2000;          // +Kemasan  
            double hargaDenganPrioritas = totalHargaDasar + 3000;        // +Prioritas
            double hargaLengkap = totalHargaDasar + 1500 + 2000 + 3000;  // All options
            
            // *** FACTORY PATTERN PREVIEW ***
            // Informasi tipe pembayaran yang tersedia
            TipePembayaran[] tipePembayaranList = TipePembayaran.values();
            
            // Pass data to view
            model.addAttribute("keranjang", keranjang);
            model.addAttribute("daftarItem", keranjang.getDaftarItem());
            model.addAttribute("totalHargaDasar", totalHargaDasar);
            model.addAttribute("userName", userName);
            model.addAttribute("userId", userId);
            
            // Decorator Pattern Preview
            model.addAttribute("hargaDenganSambal", hargaDenganSambal);
            model.addAttribute("hargaDenganKemasan", hargaDenganKemasan);
            model.addAttribute("hargaDenganPrioritas", hargaDenganPrioritas);
            model.addAttribute("hargaLengkap", hargaLengkap);
            
            // Factory Pattern Options
            model.addAttribute("tipePembayaranList", tipePembayaranList);
            
            return "pesanan/konfirmasi";
            
        } catch (IllegalStateException e) {
            return "redirect:/keranjang?error=" + e.getMessage();
        } catch (Exception e) {
            model.addAttribute("error", "System error: " + e.getMessage());
            return "pesanan/konfirmasi";
        }
    }
    
    /**
     * *** CRITICAL METHOD - ALL PATTERNS INTEGRATION ***
     * 
     * Endpoint untuk submit pesanan yang mendemonstrasikan penggunaan
     * SEMUA Design Patterns dalam satu request:
     * 
     * 1. DECORATOR PATTERN: Process opsi tambahan yang dipilih user
     * 2. FACTORY METHOD PATTERN: Create pembayaran berdasarkan tipe
     * 3. SINGLETON PATTERN: Access menu data via MenuService
     * 
     * @param opsiTambahan List opsi yang dipilih (Sambal, Kemasan, Prioritas)
     * @param tipePembayaran Tipe pembayaran yang dipilih
     * @param session HttpSession untuk user validation
     * @param redirectAttributes untuk passing data ke success page
     * @return redirect ke success page atau kembali ke konfirmasi jika error
     */
    @PostMapping("/submit")
    public String submitPesanan(@RequestParam(value = "opsiTambahan", required = false) List<String> opsiTambahan,
                               @RequestParam("tipePembayaran") String tipePembayaran,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        // Validasi login
        if (!HomeController.isMahasiswa(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        String userId = HomeController.getUserId(session);
        String userName = HomeController.getUserName(session);
        
        try {
            // Validasi input
            TipePembayaran tipe = TipePembayaran.valueOf(tipePembayaran.toUpperCase());
            
            System.out.println("=== STARTING ORDER CREATION PROCESS ===");
            System.out.println("User: " + userName + " (" + userId + ")");
            System.out.println("Selected Options: " + opsiTambahan);
            System.out.println("Payment Type: " + tipe.getDisplayName());
            
            // *** ALL PATTERNS INTEGRATION - CALL SERVICE ***
            // Method ini akan mendemonstrasikan:
            // 1. Decorator Pattern: wrapping pesanan dengan opsi
            // 2. Factory Pattern: creating pembayaran object
            // 3. Singleton Pattern: accessing menu data
            PesananService.PesananInfo pesananInfo = pesananService.buatPesanan(userId, opsiTambahan, tipe);
            
            System.out.println("=== ORDER CREATION SUCCESSFUL ===");
            System.out.println("Order ID: " + pesananInfo.getIdPesanan());
            System.out.println("Queue Number: " + pesananInfo.getNomorAntrean());
            System.out.println("Final Price: Rp " + pesananInfo.getHargaFinal());
            
            // Pass order info untuk ditampilkan di success page
            redirectAttributes.addFlashAttribute("pesananInfo", pesananInfo);
            redirectAttributes.addFlashAttribute("success", "Pesanan berhasil dibuat!");
            
            return "redirect:/pesanan/success";
            
        } catch (IllegalArgumentException e) {
            System.out.println("=== ORDER CREATION FAILED: Input Error ===");
            System.out.println("Error: " + e.getMessage());
            
            redirectAttributes.addFlashAttribute("error", "Input error: " + e.getMessage());
            return "redirect:/pesanan/konfirmasi";
            
        } catch (Exception e) {
            System.out.println("=== ORDER CREATION FAILED: System Error ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            
            redirectAttributes.addFlashAttribute("error", "System error: " + e.getMessage());
            return "redirect:/pesanan/konfirmasi";
        }
    }
    
    /**
     * *** SUCCESS PAGE - SHOW PATTERN RESULTS ***
     * Halaman success yang menampilkan hasil dari semua Design Patterns:
     * - Deskripsi lengkap dari Decorator Pattern
     * - Harga final hasil kalkulasi Decorator
     * - Payment info dari Factory Pattern
     * - Queue number yang unik
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk user validation
     * @return view name untuk pesanan/success.html
     */
    @GetMapping("/success")
    public String pesananSuccess(Model model, HttpSession session) {
        // Validasi login
        if (!HomeController.isMahasiswa(session)) {
            return "redirect:/?error=access-denied";
        }
        
        String userName = HomeController.getUserName(session);
        model.addAttribute("userName", userName);
        
        // PesananInfo sudah dikirim via RedirectAttributes dari submit method
        // Akan otomatis tersedia di model dengan key "pesananInfo"
        
        return "pesanan/success";
    }
    
    /**
     * Halaman untuk melihat detail pesanan berdasarkan ID.
     * 
     * @param idPesanan ID pesanan yang akan dilihat
     * @param model Model untuk passing data
     * @param session HttpSession untuk validation
     * @return view dengan detail pesanan
     */
    @GetMapping("/{idPesanan}")
    public String detailPesanan(@PathVariable("idPesanan") String idPesanan,
                               Model model,
                               HttpSession session) {
        
        if (!HomeController.isMahasiswa(session)) {
            return "redirect:/?error=access-denied";
        }
        
        String userId = HomeController.getUserId(session);
        
        Optional<PesananService.PesananInfo> pesananOpt = pesananService.getPesananById(idPesanan);
        
        if (pesananOpt.isEmpty()) {
            return "redirect:/pesanan/riwayat?error=not-found";
        }
        
        PesananService.PesananInfo pesanan = pesananOpt.get();
        
        // Validasi ownership (user hanya bisa lihat pesanan sendiri)
        if (!pesanan.getIdPengguna().equals(userId)) {
            return "redirect:/pesanan/riwayat?error=access-denied";
        }
        
        model.addAttribute("pesanan", pesanan);
        model.addAttribute("userName", HomeController.getUserName(session));
        
        return "pesanan/detail";
    }
    
    /**
     * Halaman untuk melihat riwayat pesanan user.
     * 
     * @param model Model untuk passing data
     * @param session HttpSession untuk user validation
     * @return view dengan list riwayat pesanan
     */
    @GetMapping("/riwayat")
    public String riwayatPesanan(Model model, HttpSession session) {
        if (!HomeController.isMahasiswa(session)) {
            return "redirect:/?error=access-denied";
        }
        
        String userId = HomeController.getUserId(session);
        String userName = HomeController.getUserName(session);
        
        List<PesananService.PesananInfo> riwayatPesanan = pesananService.getPesananByPengguna(userId);
        
        model.addAttribute("riwayatPesanan", riwayatPesanan);
        model.addAttribute("totalPesanan", riwayatPesanan.size());
        model.addAttribute("userName", userName);
        model.addAttribute("userId", userId);
        
        return "pesanan/riwayat";
    }
    
    /**
     * *** DESIGN PATTERNS DEMONSTRATION PAGE ***
     * Endpoint khusus untuk mendemonstrasikan semua Design Patterns.
     * Berguna untuk presentasi dan testing.
     * 
     * @param model Model untuk passing data
     * @param session HttpSession untuk validation
     * @return view dengan demonstrasi pattern
     */
    @GetMapping("/demo-patterns")
    public String demonstrasiPatterns(Model model, HttpSession session) {
        // Bisa diakses oleh mahasiswa dan staf
        if (session.getAttribute("userType") == null) {
            return "redirect:/?error=login-required";
        }
        
        try {
            // Get demonstration dari service
            String demo = pesananService.demonstrasiDesignPatterns();
            
            model.addAttribute("demonstrasi", demo);
            model.addAttribute("userName", HomeController.getUserName(session));
            model.addAttribute("userType", session.getAttribute("userType"));
            
            return "pesanan/demo-patterns";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error generating demo: " + e.getMessage());
            return "pesanan/demo-patterns";
        }
    }
}