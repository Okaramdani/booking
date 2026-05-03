# Smart Booking System - Complete Setup Guide

## ⚙️ Prerequisites
- Java 17 or higher
- XAMPP (with MySQL)
- Maven (or use included mvnw)

## 📦 Technology Stack
- Spring Boot 3.2.0
- Spring Security (BCrypt password encryption)
- Spring Data JPA (Hibernate)
- MySQL (XAMPP localhost:3306)
- Thymeleaf + Bootstrap 5
- Maven

## 🚀 Step-by-Step Setup

### 1. Start XAMPP & Create Database
```bash
1. Open XAMPP Control Panel
2. Start Apache and MySQL services
3. Open browser: http://localhost/phpmyadmin
4. Create database: booking_app
```

Or run SQL:
```sql
CREATE DATABASE IF NOT EXISTS booking_app;
```

### 2. Run the Application

**Using Maven Wrapper (Windows):**
```bash
cd C:\xampp\htdocs\booking
mvnw.cmd spring-boot:run
```

**Or build and run:**
```bash
mvnw.cmd clean install
java -jar target/booking-app-0.0.1-SNAPSHOT.jar
```

### 3. Access Application
- **Home:** http://localhost:8080/
- **Login:** http://localhost:8080/login
- **Register:** http://localhost:8080/register

### 4. Default Admin Account
```
Email: admin@booking.com
Password: admin123
```

## 🎯 Multi-Business Categories
- **CLINIC** - Medical services, doctor appointments
- **BARBERSHOP** - Haircuts, beard trimming
- **SALON** - Facial, hair spa, nail art
- **ROOM** - Meeting rooms, event spaces

## ✨ Features

### User Features:
1. Register new account
2. Login with session-based auth
3. Browse services by category (modern card UI)
4. Book services (date, time, notes)
5. View own bookings with status badges
6. Cancel pending bookings

### Admin Features:
1. Dashboard with statistics (total services, bookings, pending)
2. CRUD services (multi-business support)
3. Manage all bookings
4. Filter bookings by date/status
5. Approve/Reject/Cancel bookings
6. Delete bookings

## 🏗 Project Structure (MVC Architecture)
```
src/main/java/com/booking/app/
├── controller/           # MVC Controllers
│   ├── AuthController.java
│   ├── UserController.java
│   ├── AdminController.java
│   └── AdminServiceController.java
├── service/             # Business Logic Layer
│   ├── UserService.java
│   ├── ServiceService.java
│   └── BookingService.java
├── repository/          # JPA Data Access
│   ├── UserRepository.java
│   ├── ServiceRepository.java
│   └── BookingRepository.java
├── entity/              # JPA Entities
│   ├── User.java
│   ├── Service.java
│   ├── Booking.java
│   ├── Role.java
│   ├── ServiceCategory.java
│   ├── BookingStatus.java
│   └── ServiceStatus.java
├── dto/                 # Data Transfer Objects
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── ServiceRequest.java
│   └── BookingRequest.java
└── config/              # Configuration
    ├── SecurityConfig.java
    └── DataInitializer.java

src/main/resources/
├── templates/
│   ├── auth/
│   │   ├── login.html
│   │   └── register.html
│   ├── user/
│   │   ├── home.html
│   │   ├── services.html
│   │   ├── service-detail.html
│   │   └── my-bookings.html
│   └── admin/
│       ├── dashboard.html
│       ├── manage-services.html
│       ├── manage-bookings.html
│       └── service-form.html
└── application.properties
```

## 🔐 Security & Authentication Flow
1. **Spring Security** with session-based login
2. **BCrypt** password encryption
3. **Role-based access**: ADMIN vs USER
4. **Protected routes**: /admin/** requires ROLE_ADMIN
5. **Auto-redirect** to /dashboard after login

## 📊 Booking System Flow
1. User browses services → filters by category
2. Selects service → views detail page
3. Picks date & time → submits booking
4. System validates:
   - Service must be AVAILABLE
   - No time conflict for same service
   - Auto-calculates endTime from duration
5. Booking status = PENDING
6. Admin reviews → APPROVE/REJECT
7. User can CANCEL if still PENDING

## ⚠️ Business Logic Validations
- ✅ No double booking (same service + date + time conflict)
- ✅ Cannot book UNAVAILABLE services
- ✅ endTime auto-calculated from service duration
- ✅ Booking defaults to PENDING status
- ✅ Users cannot edit APPROVED bookings
- ✅ Only admin can APPROVE/REJECT

## 🎨 UI Features
- Modern Bootstrap 5 responsive design
- Font Awesome icons
- Card-based service listing
- Gradient headers
- Status badges (PENDING/APPROVED/REJECTED/CANCELLED)
- Toast/alert notifications
- Modal confirmations for delete
- Mobile-first responsive grid

## 📝 Notes
- Database tables auto-created by JPA (ddl-auto=update)
- Sample services auto-inserted on first run
- Passwords encrypted with BCrypt
- Session timeout managed by Spring Security
- Thymeleaf template caching disabled for development