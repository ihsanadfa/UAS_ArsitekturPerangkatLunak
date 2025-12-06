package com.university.canteen.controller;

import com.university.canteen.service.PesananService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Debug Controller untuk inspeksi in-memory data
 * 
 * @author M. Ihsan Rizqullah Adfa - 2208107010029
 */
@Controller
public class DebugController {
    
    @Autowired
    private PesananService pesananService;
    
    /**
     * Debug endpoint untuk melihat semua pesanan yang tersimpan di memory
     * URL: /debug
     */
    @GetMapping("/debug")
    @ResponseBody
    public String debugPesanan() throws JsonProcessingException {
        Map<String, Object> debugInfo = new HashMap<>();
        
        // Get all stored orders
        Map<String, PesananService.PesananInfo> semuaPesanan = pesananService.getSemuaPesanan();
        
        debugInfo.put("totalOrders", semuaPesanan.size());
        debugInfo.put("orders", semuaPesanan);
        
        // Pretty print JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // For LocalDateTime serialization
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><title>Debug - In Memory Orders</title>");
        html.append("<style>body{font-family:monospace;margin:20px;} .order{border:1px solid #ccc;margin:10px 0;padding:10px;}</style>");
        html.append("</head><body>");
        html.append("<h1>🐛 Debug: In-Memory Orders Data</h1>");
        html.append("<h2>Total Orders: ").append(semuaPesanan.size()).append("</h2>");
        
        if (semuaPesanan.isEmpty()) {
            html.append("<p><strong>No orders found in memory!</strong></p>");
        } else {
            for (Map.Entry<String, PesananService.PesananInfo> entry : semuaPesanan.entrySet()) {
                PesananService.PesananInfo order = entry.getValue();
                html.append("<div class='order'>");
                html.append("<h3>Order ID: ").append(entry.getKey()).append("</h3>");
                html.append("<p><strong>User:</strong> ").append(order.getIdPengguna()).append("</p>");
                html.append("<p><strong>Queue:</strong> ").append(order.getNomorAntrean()).append("</p>");
                html.append("<p><strong>Final Price:</strong> Rp ").append(order.getHargaFinal()).append("</p>");
                html.append("<p><strong>🎯 DECORATORS (opsiTambahan):</strong> ").append(order.getOpsiTambahan()).append("</p>");
                html.append("<p><strong>Description:</strong> ").append(order.getDeskripsi()).append("</p>");
                html.append("<p><strong>Payment Type:</strong> ").append(order.getPembayaran().getTipePembayaran().getDisplayName()).append("</p>");
                html.append("<p><strong>Created:</strong> ").append(order.getWaktuPesan()).append("</p>");
                html.append("</div>");
            }
        }
        
        html.append("<hr><h2>🔍 Raw JSON Data:</h2>");
        html.append("<pre>").append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(debugInfo)).append("</pre>");
        html.append("</body></html>");
        
        return html.toString();
    }
}