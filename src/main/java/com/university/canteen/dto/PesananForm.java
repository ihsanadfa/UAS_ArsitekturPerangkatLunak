package com.university.canteen.dto;

import com.university.canteen.patterns.factory.enums.TipePembayaran;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object PesananForm
 * 
 * DTO untuk menangani form submission pesanan dari konfirmasi.html ke PesananController.
 * Menggunakan @ModelAttribute binding untuk mengatasi masalah Spring Boot List binding
 * yang terjadi dengan @RequestParam approach.
 * 
 * Class ini memastikan data form (terutama opsiTambahan List) ditransmisikan
 * dengan reliabel dari view ke controller.
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 * @version 1.0
 * @since 2025
 */
@Data
public class PesananForm {
    
    /**
     * Nama pengguna yang melakukan pesanan.
     * Diambil dari session untuk validasi.
     */
    private String namaPengguna;
    
    /**
     * Tipe pembayaran yang dipilih pengguna.
     * Menggunakan enum TipePembayaran (TUNAI, E_WALLET, TRANSFER).
     */
    private TipePembayaran tipePembayaran;
    
    /**
     * String opsi tambahan (decorators) yang dipilih pengguna - MANUAL PARSING APPROACH.
     * Format: comma-separated string (e.g. "SAMBAL,KEMASAN,PRIORITAS")
     * 
     * Eliminates Spring List binding issues by using simple String binding.
     */
    private String opsiTambahanStr = "";
    
    /**
     * Override toString() untuk debugging dan logging.
     * 
     * @return representasi string dari form data
     */
    @Override
    public String toString() {
        return String.format("PesananForm{namaPengguna='%s', tipePembayaran=%s, opsiTambahanStr='%s'}", 
                           namaPengguna, tipePembayaran, opsiTambahanStr);
    }
}