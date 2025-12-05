package com.university.canteen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

/**
 * Controller HomeController
 * 
 * Controller untuk menangani halaman utama dan simulasi login system.
 * Menyediakan entry point untuk pengguna (mahasiswa) dan staf kantin.
 * 
 * Simulasi login sederhana menggunakan session attribute:
 * - "MAHASISWA" -> akses ke menu dan keranjang
 * - "STAF" -> akses ke dashboard staf untuk manage menu dan lihat pesanan
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Controller
public class HomeController {
    
    /**
     * Halaman landing page utama.
     * Menampilkan pilihan login sebagai Mahasiswa atau Staf.
     * 
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk cek existing login
     * @return view name untuk index.html
     */
    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        // Cek apakah user sudah login
        String userType = (String) session.getAttribute("userType");
        String userId = (String) session.getAttribute("userId");
        String userName = (String) session.getAttribute("userName");
        
        model.addAttribute("isLoggedIn", userType != null);
        model.addAttribute("userType", userType);
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        
        return "index";
    }
    
    /**
     * Endpoint untuk simulasi login.
     * Menerima parameter type (MAHASISWA/STAF) dan nama pengguna.
     * 
     * @param type tipe pengguna (MAHASISWA atau STAF)
     * @param nama nama pengguna
     * @param session HttpSession untuk menyimpan data login
     * @return redirect ke halaman yang sesuai
     */
    @PostMapping("/login")
    public String login(@RequestParam("type") String type,
                       @RequestParam("nama") String nama,
                       HttpSession session) {
        
        // Validasi input
        if (nama == null || nama.trim().isEmpty()) {
            return "redirect:/?error=nama-kosong";
        }
        
        // Generate user ID sederhana
        String userId = type.toLowerCase() + "_" + nama.replaceAll("\\s+", "_").toLowerCase();
        
        // Simpan ke session
        session.setAttribute("userType", type.toUpperCase());
        session.setAttribute("userId", userId);
        session.setAttribute("userName", nama);
        
        // Redirect berdasarkan tipe user
        if ("STAF".equalsIgnoreCase(type)) {
            return "redirect:/staf/dashboard";
        } else {
            return "redirect:/menu";
        }
    }
    
    /**
     * Endpoint untuk logout.
     * Menghapus semua data dari session.
     * 
     * @param session HttpSession yang akan dibersihkan
     * @return redirect ke halaman utama
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/?message=logout-success";
    }
    
    /**
     * Method helper untuk validasi apakah user sudah login sebagai mahasiswa.
     * 
     * @param session HttpSession untuk cek data login
     * @return true jika user login sebagai mahasiswa
     */
    public static boolean isMahasiswa(HttpSession session) {
        String userType = (String) session.getAttribute("userType");
        return "MAHASISWA".equals(userType);
    }
    
    /**
     * Method helper untuk validasi apakah user sudah login sebagai staf.
     * 
     * @param session HttpSession untuk cek data login
     * @return true jika user login sebagai staf
     */
    public static boolean isStaf(HttpSession session) {
        String userType = (String) session.getAttribute("userType");
        return "STAF".equals(userType);
    }
    
    /**
     * Method helper untuk mendapatkan User ID dari session.
     * 
     * @param session HttpSession untuk ambil data
     * @return User ID atau null jika tidak login
     */
    public static String getUserId(HttpSession session) {
        return (String) session.getAttribute("userId");
    }
    
    /**
     * Method helper untuk mendapatkan User Name dari session.
     * 
     * @param session HttpSession untuk ambil data
     * @return User Name atau null jika tidak login
     */
    public static String getUserName(HttpSession session) {
        return (String) session.getAttribute("userName");
    }
}