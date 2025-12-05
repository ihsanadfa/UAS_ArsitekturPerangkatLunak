package com.university.canteen.controller;

import com.university.canteen.model.entity.Keranjang;
import com.university.canteen.service.KeranjangService;
import com.university.canteen.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

/**
 * Controller KeranjangController
 * 
 * Controller untuk mengelola keranjang belanja pengguna.
 * 
 * Mendukung:
 * - Requirement R02: Pengguna dapat menambahkan item ke keranjang
 * - OCL Implementation: Pre-condition validation sebelum tambah item
 * - Integrasi dengan MenuService (Singleton) untuk validasi menu
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Controller
@RequestMapping("/keranjang")
public class KeranjangController {
    
    @Autowired
    private KeranjangService keranjangService;
    
    @Autowired
    private MenuService menuService;
    
    /**
     * Menampilkan isi keranjang pengguna.
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk mendapatkan user ID
     * @return view name untuk pesanan/keranjang.html
     */
    @GetMapping
    public String viewKeranjang(Model model, HttpSession session) {
        // Validasi login sebagai mahasiswa
        if (!HomeController.isMahasiswa(session)) {
            return "redirect:/?error=access-denied";
        }
        
        String userId = HomeController.getUserId(session);
        String userName = HomeController.getUserName(session);
        
        try {
            // Get keranjang dari service
            Keranjang keranjang = keranjangService.getKeranjangPengguna(userId);
            
            // Hitung informasi keranjang
            double totalHarga = keranjangService.hitungTotalKeranjang(userId);
            int jumlahItem = keranjangService.hitungJumlahItemKeranjang(userId);
            boolean isEmpty = keranjangService.isKeranjangKosong(userId);
            String ringkasan = keranjangService.getRingkasanKeranjang(userId);
            
            // Pass data to view
            model.addAttribute("keranjang", keranjang);
            model.addAttribute("itemKeranjangList", keranjang.getDaftarItem());
            model.addAttribute("daftarItem", keranjang.getDaftarItem()); // Keep for backward compatibility
            model.addAttribute("totalHarga", totalHarga);
            model.addAttribute("subtotalHarga", totalHarga); // Add subtotalHarga for template
            model.addAttribute("jumlahItem", jumlahItem);
            model.addAttribute("totalItems", jumlahItem); // Add missing totalItems for navbar badge
            model.addAttribute("isEmpty", isEmpty);
            model.addAttribute("ringkasan", ringkasan);
            model.addAttribute("userName", userName);
            model.addAttribute("userId", userId);
            
            return "pesanan/keranjang";
            
        } catch (Exception e) {
            model.addAttribute("error", "Error loading keranjang: " + e.getMessage());
            return "pesanan/keranjang";
        }
    }
    
    /**
     * *** REQUIREMENT R02 + OCL IMPLEMENTATION ***
     * Endpoint untuk menambah item ke keranjang.
     * Mengimplementasikan OCL pre-condition: menu.tersedia && kuantitas > 0
     * 
     * @param idMenu ID menu yang akan ditambahkan
     * @param kuantitas jumlah item yang dipesan
     * @param session HttpSession untuk mendapatkan user ID
     * @param redirectAttributes untuk flash messages
     * @return redirect ke halaman keranjang atau menu
     */
    @PostMapping("/add")
    public String addToKeranjang(@RequestParam("idMenu") String idMenu,
                                @RequestParam("kuantitas") int kuantitas,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        
        // Validasi login sebagai mahasiswa
        if (!HomeController.isMahasiswa(session)) {
            redirectAttributes.addFlashAttribute("error", "Anda harus login sebagai mahasiswa");
            return "redirect:/";
        }
        
        String userId = HomeController.getUserId(session);
        
        try {
            // *** OCL PRE-CONDITION CHECK ***
            // Service akan melakukan validasi:
            // 1. menu.tersedia (melalui MenuService -> Singleton)
            // 2. kuantitas > 0
            boolean berhasil = keranjangService.tambahItemKeKeranjang(userId, idMenu, kuantitas);
            
            if (berhasil) {
                // Get menu name untuk success message
                String namaMenu = menuService.getMenuById(idMenu)
                                            .map(menu -> menu.getNamaMenu())
                                            .orElse("Menu");
                
                redirectAttributes.addFlashAttribute("success", 
                    String.format("%s (x%d) berhasil ditambahkan ke keranjang", namaMenu, kuantitas));
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal menambahkan item ke keranjang");
            }
            
        } catch (IllegalArgumentException e) {
            // OCL pre-condition failed
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "System error: " + e.getMessage());
        }
        
        // Redirect kembali ke keranjang
        return "redirect:/keranjang";
    }
    
    /**
     * Endpoint untuk update kuantitas item dalam keranjang.
     * 
     * @param idMenu ID menu yang akan diupdate
     * @param kuantitas kuantitas baru
     * @param session HttpSession untuk user validation
     * @param redirectAttributes untuk flash messages
     * @return redirect ke keranjang
     */
    @PostMapping("/update")
    public String updateKuantitas(@RequestParam("idMenu") String idMenu,
                                 @RequestParam("kuantitas") int kuantitas,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        
        if (!HomeController.isMahasiswa(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        String userId = HomeController.getUserId(session);
        
        try {
            boolean berhasil = keranjangService.updateKuantitasItem(userId, idMenu, kuantitas);
            
            if (berhasil) {
                if (kuantitas > 0) {
                    redirectAttributes.addFlashAttribute("success", "Kuantitas berhasil diupdate");
                } else {
                    redirectAttributes.addFlashAttribute("success", "Item berhasil dihapus dari keranjang");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal update kuantitas");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/keranjang";
    }
    
    /**
     * Endpoint untuk menghapus item dari keranjang.
     * 
     * @param idMenu ID menu yang akan dihapus
     * @param session HttpSession untuk user validation
     * @param redirectAttributes untuk flash messages
     * @return redirect ke keranjang
     */
    @PostMapping("/remove")
    public String removeFromKeranjang(@RequestParam("idMenu") String idMenu,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        
        if (!HomeController.isMahasiswa(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        String userId = HomeController.getUserId(session);
        
        try {
            boolean berhasil = keranjangService.hapusItemDariKeranjang(userId, idMenu);
            
            if (berhasil) {
                redirectAttributes.addFlashAttribute("success", "Item berhasil dihapus dari keranjang");
            } else {
                redirectAttributes.addFlashAttribute("error", "Gagal menghapus item");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/keranjang";
    }
    
    /**
     * Endpoint untuk mengosongkan seluruh keranjang.
     * 
     * @param session HttpSession untuk user validation
     * @param redirectAttributes untuk flash messages
     * @return redirect ke keranjang
     */
    @PostMapping("/clear")
    public String clearKeranjang(HttpSession session,
                                RedirectAttributes redirectAttributes) {
        
        if (!HomeController.isMahasiswa(session)) {
            redirectAttributes.addFlashAttribute("error", "Access denied");
            return "redirect:/";
        }
        
        String userId = HomeController.getUserId(session);
        
        try {
            keranjangService.kosongkanKeranjang(userId);
            redirectAttributes.addFlashAttribute("success", "Keranjang berhasil dikosongkan");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        
        return "redirect:/keranjang";
    }
    
    /**
     * Endpoint untuk proceed ke halaman checkout.
     * Melakukan validasi keranjang sebelum ke konfirmasi.
     * 
     * @param session HttpSession untuk user validation
     * @param redirectAttributes untuk flash messages
     * @return redirect ke halaman konfirmasi atau kembali ke keranjang jika error
     */
    @GetMapping("/checkout")
    public String proceedToCheckout(HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        
        if (!HomeController.isMahasiswa(session)) {
            redirectAttributes.addFlashAttribute("error", "Anda harus login sebagai mahasiswa");
            return "redirect:/";
        }
        
        String userId = HomeController.getUserId(session);
        
        try {
            // Validasi keranjang untuk checkout
            boolean valid = keranjangService.validasiKeranjangUntukCheckout(userId);
            
            if (valid) {
                // Redirect ke halaman konfirmasi pesanan
                return "redirect:/pesanan/konfirmasi";
            } else {
                redirectAttributes.addFlashAttribute("error", "Keranjang tidak valid untuk checkout");
                return "redirect:/keranjang";
            }
            
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/keranjang";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "System error: " + e.getMessage());
            return "redirect:/keranjang";
        }
    }
    
    /**
     * API endpoint untuk mendapatkan jumlah item dalam keranjang (AJAX).
     * Berguna untuk update cart counter di navigation.
     * 
     * @param session HttpSession untuk user validation
     * @return ResponseEntity dengan jumlah item
     */
    @GetMapping("/count")
    @ResponseBody
    public int getKeranjangCount(HttpSession session) {
        if (!HomeController.isMahasiswa(session)) {
            return 0;
        }
        
        String userId = HomeController.getUserId(session);
        
        try {
            return keranjangService.hitungJumlahItemKeranjang(userId);
        } catch (Exception e) {
            return 0;
        }
    }
}