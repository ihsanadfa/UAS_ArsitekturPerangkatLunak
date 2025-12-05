package com.university.canteen.controller;

import com.university.canteen.model.entity.Menu;
import com.university.canteen.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller MenuController
 * 
 * Controller untuk menampilkan daftar menu kantin universitas.
 * 
 * *** SINGLETON PATTERN INTEGRATION ***
 * Controller ini menggunakan MenuService yang merupakan wrapper untuk
 * MenuRepository Singleton. Setiap request akan mengakses data menu
 * melalui instance tunggal MenuRepository.getInstance().
 * 
 * Mendukung:
 * - Requirement R01: Menampilkan daftar menu dengan harga
 * - Filter berdasarkan kategori dan ketersediaan
 * - Search functionality
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
    
    @Autowired
    private MenuService menuService;
    
    /**
     * *** SINGLETON PATTERN USAGE ***
     * Menampilkan daftar menu utama.
     * Menggunakan MenuService yang mengakses MenuRepository Singleton.
     * 
     * @param kategori filter kategori (optional)
     * @param search keyword pencarian (optional) 
     * @param showAll tampilkan semua menu atau hanya yang tersedia
     * @param model Model untuk passing data ke view
     * @param session HttpSession untuk cek login status
     * @return view name untuk menu/list.html
     */
    @GetMapping
    public String listMenu(@RequestParam(value = "kategori", required = false) String kategori,
                          @RequestParam(value = "search", required = false) String search,
                          @RequestParam(value = "showAll", defaultValue = "false") boolean showAll,
                          Model model,
                          HttpSession session) {
        
        // Cek login status untuk UI customization
        boolean isLoggedIn = session.getAttribute("userType") != null;
        boolean isMahasiswa = HomeController.isMahasiswa(session);
        boolean isStaf = HomeController.isStaf(session);
        
        List<Menu> menuList;
        
        // *** SINGLETON PATTERN - MenuService calls MenuRepository.getInstance() ***
        if (search != null && !search.trim().isEmpty()) {
            // Search functionality menggunakan Singleton
            menuList = menuService.searchMenuByNama(search);
        } else if (kategori != null && !kategori.trim().isEmpty()) {
            // Filter by kategori menggunakan Singleton
            menuList = menuService.getMenuByKategori(kategori);
        } else if (showAll || isStaf) {
            // Show all menu (untuk staf atau jika diminta)
            menuList = menuService.getAllMenu();
        } else {
            // Default: show only available menu (Requirement R01)
            menuList = menuService.getMenuTersedia();
        }
        
        // Filter lebih lanjut jika tidak showAll dan bukan staf
        if (!showAll && !isStaf) {
            menuList = menuList.stream()
                              .filter(Menu::isTersedia)
                              .toList();
        }
        
        // *** SINGLETON PATTERN - Get categories using Singleton ***
        List<String> kategoriList = menuService.getKategoriTersedia();
        
        // *** SINGLETON PATTERN - Get statistics using Singleton ***
        String statistikMenu = menuService.getStatistikMenu();
        String singletonInfo = menuService.getSingletonInfo();
        
        // Pass data to view
        model.addAttribute("menuList", menuList);
        model.addAttribute("kategoriList", kategoriList);
        model.addAttribute("selectedKategori", kategori);
        model.addAttribute("searchKeyword", search);
        model.addAttribute("showAll", showAll);
        model.addAttribute("statistikMenu", statistikMenu);
        model.addAttribute("singletonInfo", singletonInfo);
        
        // Login information
        model.addAttribute("isLoggedIn", isLoggedIn);
        model.addAttribute("isMahasiswa", isMahasiswa);
        model.addAttribute("isStaf", isStaf);
        model.addAttribute("userName", HomeController.getUserName(session));
        
        // Count information
        model.addAttribute("totalMenu", menuList.size());
        
        return "menu/list";
    }
    
    /**
     * Endpoint untuk menampilkan menu berdasarkan kategori spesifik.
     * 
     * @param kategori nama kategori yang dipilih
     * @param model Model untuk passing data
     * @param session HttpSession untuk cek login
     * @return redirect ke list menu dengan filter kategori
     */
    @GetMapping("/kategori")
    public String menuByKategori(@RequestParam("kategori") String kategori,
                                Model model,
                                HttpSession session) {
        return "redirect:/menu?kategori=" + kategori;
    }
    
    /**
     * *** SINGLETON PATTERN DEMONSTRATION ***
     * Endpoint khusus untuk mendemonstrasikan Singleton Pattern.
     * Menampilkan informasi tentang instance MenuRepository.
     * 
     * @param model Model untuk passing data
     * @param session HttpSession untuk authorization
     * @return view dengan informasi singleton
     */
    @GetMapping("/singleton-demo")
    public String singletonDemo(Model model, HttpSession session) {
        // Hanya staf yang bisa akses demo ini
        if (!HomeController.isStaf(session)) {
            return "redirect:/menu?error=access-denied";
        }
        
        // *** SINGLETON PATTERN - Get repository instance for demonstration ***
        com.university.canteen.patterns.singleton.MenuRepository repository = com.university.canteen.patterns.singleton.MenuRepository.getInstance();
        
        // Get instance hash and total menus
        String instanceHash = String.valueOf(repository.hashCode());
        int totalMenus = repository.getTotalMenu();
        
        // *** SINGLETON PATTERN - Multiple calls should return same instance ***
        String singletonInfo1 = menuService.getSingletonInfo();
        String singletonInfo2 = menuService.getSingletonInfo();
        String singletonInfo3 = menuService.getSingletonInfo();
        
        // Demonstrate that MenuRepository is always the same instance
        boolean isSameInstance = singletonInfo1.equals(singletonInfo2) && singletonInfo2.equals(singletonInfo3);
        
        model.addAttribute("repository", repository);
        model.addAttribute("instanceHash", instanceHash);
        model.addAttribute("totalMenus", totalMenus);
        model.addAttribute("singletonInfo1", singletonInfo1);
        model.addAttribute("singletonInfo2", singletonInfo2);
        model.addAttribute("singletonInfo3", singletonInfo3);
        model.addAttribute("isSameInstance", isSameInstance);
        model.addAttribute("userName", HomeController.getUserName(session));
        
        return "menu/singleton-demo";
    }
    
    /**
     * Endpoint untuk mendapatkan menu dalam range harga tertentu.
     * 
     * @param minHarga harga minimum
     * @param maxHarga harga maksimum
     * @param model Model untuk passing data
     * @param session HttpSession untuk cek login
     * @return view menu list dengan filter harga
     */
    @GetMapping("/range-harga")
    public String menuByRangeHarga(@RequestParam(value = "min", defaultValue = "0") double minHarga,
                                  @RequestParam(value = "max", defaultValue = "50000") double maxHarga,
                                  Model model,
                                  HttpSession session) {
        
        try {
            // *** SINGLETON PATTERN - Filter by price range ***
            List<Menu> menuList = menuService.getMenuByRangeHarga(minHarga, maxHarga);
            
            // Filter untuk mahasiswa (hanya yang tersedia)
            if (HomeController.isMahasiswa(session)) {
                menuList = menuList.stream()
                                  .filter(Menu::isTersedia)
                                  .toList();
            }
            
            model.addAttribute("menuList", menuList);
            model.addAttribute("minHarga", minHarga);
            model.addAttribute("maxHarga", maxHarga);
            model.addAttribute("totalMenu", menuList.size());
            model.addAttribute("isLoggedIn", session.getAttribute("userType") != null);
            model.addAttribute("isMahasiswa", HomeController.isMahasiswa(session));
            model.addAttribute("isStaf", HomeController.isStaf(session));
            
            return "menu/list";
            
        } catch (IllegalArgumentException e) {
            return "redirect:/menu?error=" + e.getMessage();
        }
    }
}