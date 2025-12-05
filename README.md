# 🍽️ University Canteen Ordering System

**Final Exam Project (UAS APL) - Design Patterns Implementation**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.1-green.svg)](https://spring.io/projects/spring-boot)
[![H2 Database](https://img.shields.io/badge/Database-H2%20In--Memory-blue.svg)](http://www.h2database.com/)
[![Thymeleaf](https://img.shields.io/badge/Template-Thymeleaf-green.svg)](https://www.thymeleaf.org/)
[![Bootstrap](https://img.shields.io/badge/UI-Bootstrap%205-purple.svg)](https://getbootstrap.com/)

## 📋 Project Overview

Sistem pemesanan kantin universitas yang mengimplementasikan **3 Design Pattern** wajib:

### 🎯 Design Patterns Implemented

1. **🔄 Singleton Pattern** - `MenuRepository`
   - Thread-safe Bill Pugh Singleton implementation
   - Centralized menu data access across the application
   - Ensures single instance for data consistency

2. **🎨 Decorator Pattern** - `Pesanan` class enhancement
   - Dynamic order customization without changing base structure
   - `SambalDecorator` - Extra spicy sauce (+Rp 2,000)
   - `KemasanKhususDecorator` - Premium packaging (+Rp 3,000)
   - `PrioritasDecorator` - Express priority (+Rp 5,000)

3. **🏭 Factory Method Pattern** - `Pembayaran` object creation
   - Dynamic payment object instantiation based on type
   - `TUNAI` → `PembayaranTunai`
   - `TRANSFER` → `PembayaranDigital`
   - `QRIS` → `PembayaranDigital`

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Your favorite IDE (VS Code, IntelliJ, Eclipse)

### Run the Application

```bash
# Clone or extract the project
cd UAS_APL

# Run with Maven (Windows)
mvnw.cmd spring-boot:run

# Run with Maven (Linux/Mac)
./mvnw spring-boot:run
```

### Access the Application

- **Main Application**: http://localhost:8080
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:canteen_db`
  - Username: `sa`
  - Password: `password`

## 🎮 Demo Features

### For Students (Mahasiswa)
1. **Browse Menu** - View available food/drinks (Singleton Pattern demo)
2. **Add to Cart** - Build your order with quantity selection
3. **Customize Order** - Add decorators (Sambal, Kemasan, Prioritas)
4. **Choose Payment** - Select payment method (Factory Pattern demo)
5. **Complete Order** - Integration of all 3 patterns

### For Staff
1. **Dashboard Overview** - Statistics and pattern analysis
2. **Menu Management** - Update menu availability (Requirement R04)
3. **Order Monitoring** - View all customer orders (Requirement R05)
4. **Pattern Testing** - Dedicated singleton and pattern demos

## 📁 Project Structure

```
src/main/java/com/university/canteen/
├── controller/           # Web controllers (Spring MVC)
│   ├── HomeController.java       # Authentication & landing
│   ├── MenuController.java       # Menu display & management  
│   ├── KeranjangController.java  # Shopping cart operations
│   ├── PesananController.java    # Order processing (ALL PATTERNS)
│   └── StafController.java       # Staff dashboard & management
├── service/              # Business logic layer
│   ├── MenuService.java          # Menu operations (Singleton wrapper)
│   ├── KeranjangService.java     # Cart management with OCL
│   └── PesananService.java       # Order processing (Pattern integration)
├── model/entity/         # JPA entities
│   ├── Menu.java                 # Menu items with OCL constraints
│   ├── Keranjang.java           # Shopping cart
│   ├── ItemKeranjang.java       # Cart items
│   └── ItemPesanan.java         # Order items
└── patterns/             # 🎯 DESIGN PATTERN IMPLEMENTATIONS
    ├── singleton/
    │   └── MenuRepository.java   # 1️⃣ Singleton Pattern
    ├── decorator/
    │   ├── IPesanan.java         # 2️⃣ Decorator Pattern Interface
    │   ├── Pesanan.java          # Base order implementation
    │   ├── PesananDecorator.java # Abstract decorator
    │   ├── SambalDecorator.java  # Sambal enhancement
    │   ├── KemasanKhususDecorator.java # Premium packaging
    │   └── PrioritasDecorator.java # Express priority
    └── factory/
        ├── Pembayaran.java       # 3️⃣ Factory Pattern Interface
        ├── PembayaranFactory.java # Factory method implementation
        ├── PembayaranTunai.java  # Cash payment
        ├── PembayaranDigital.java # Digital payment
        └── enums/TipePembayaran.java # Payment types enum
```

## 🔧 Technical Implementation

### OCL (Object Constraint Language) Implementation
- **Pre-conditions**: Input validation before operations
- **Post-conditions**: State verification after operations  
- **Invariants**: Object consistency constraints
- Applied in: Cart management, order processing, menu validation

### Spring Boot Features Used
- **Thymeleaf**: Server-side template rendering
- **JPA/Hibernate**: Database operations with H2
- **Spring MVC**: Web layer with form handling
- **DevTools**: Hot reload during development
- **Bootstrap 5**: Responsive UI components

### Pattern Integration Example
```java
// In PesananService.buatPesanan() - ALL 3 PATTERNS WORKING TOGETHER:

// 1. Singleton Pattern - Get menu data
MenuRepository menuRepo = MenuRepository.getInstance();

// 2. Decorator Pattern - Enhance order
IPesanan pesanan = new Pesanan(items);
if (decorators.contains("sambal")) {
    pesanan = new SambalDecorator(pesanan);
}
if (decorators.contains("kemasan")) {
    pesanan = new KemasanKhususDecorator(pesanan);
}
if (decorators.contains("prioritas")) {
    pesanan = new PrioritasDecorator(pesanan);
}

// 3. Factory Pattern - Create payment
Pembayaran payment = PembayaranFactory.createPembayaran(
    TipePembayaran.valueOf(metodePembayaran), 
    pesanan.getTotalHarga()
);
```

## 🧪 Testing the Patterns

### Singleton Pattern Test
- Visit: http://localhost:8080/menu/singleton-demo
- Verify same instance across multiple calls
- Thread-safety demonstration

### Decorator Pattern Test  
- Add items to cart: http://localhost:8080/keranjang
- Select various decorators (Sambal, Kemasan, Prioritas)
- See dynamic price calculation

### Factory Pattern Test
- Go through checkout process
- Try different payment methods (Tunai, Transfer, QRIS)
- Observe different payment object creation

## 📊 Requirements Fulfillment

| Requirement | Implementation | Status |
|------------|----------------|--------|
| **R01** | Login simulation with role-based access | ✅ |
| **R02** | Menu display with search/filter | ✅ |
| **R03** | Shopping cart with quantity management | ✅ |
| **R04** | Staff menu management (add/update/status) | ✅ |
| **R05** | Order monitoring for staff | ✅ |
| **3 Design Patterns** | Singleton + Decorator + Factory | ✅ |
| **OCL Constraints** | Pre/post conditions + invariants | ✅ |
| **Web Interface** | Bootstrap 5 responsive UI | ✅ |
| **Database** | H2 with JPA/Hibernate | ✅ |

## 💡 Key Features Demo

1. **Pattern Integration**: Single business flow demonstrating all 3 patterns working together
2. **Real-world Usage**: Practical implementation beyond academic examples  
3. **Interactive UI**: Visual demonstration of pattern behaviors
4. **Comprehensive Logging**: Pattern execution tracking in console
5. **Educational Value**: Code comments explaining pattern benefits and usage

## 🎓 Educational Value

This project demonstrates:
- **Enterprise-level** design pattern usage
- **Spring Boot** framework integration
- **Real business logic** implementation  
- **Clean architecture** principles
- **Responsive web design** with Bootstrap
- **Database operations** with JPA
- **OCL constraint** application

## 👨‍💻 Development Notes

- **Package Structure**: Organized by functionality and patterns
- **Code Quality**: Comprehensive JavaDoc documentation  
- **Error Handling**: Graceful exception management
- **UI/UX**: Intuitive interface with pattern visualization
- **Performance**: Efficient singleton and factory implementations

---

**🎯 Final Exam Project (UAS APL) - Successfully implementing 3 Design Patterns in a realistic University Canteen Ordering System**

*Developed with ❤️ using Spring Boot, Thymeleaf, and Bootstrap 5*