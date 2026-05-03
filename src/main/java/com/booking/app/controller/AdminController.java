package com.booking.app.controller;

import com.booking.app.entity.BookingStatus;
import com.booking.app.service.BookingService;
import com.booking.app.service.ServiceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ServiceService serviceService;
    private final BookingService bookingService;
    
    public AdminController(ServiceService serviceService, BookingService bookingService) {
        this.serviceService = serviceService;
        this.bookingService = bookingService;
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        model.addAttribute("totalServices", serviceService.getAllServices().size());
        model.addAttribute("totalBookings", bookingService.getAllBookings().size());
        model.addAttribute("pendingBookings", bookingService.getAllBookings().stream()
            .filter(b -> b.getStatus() == BookingStatus.PENDING).count());
        model.addAttribute("todayBookings", bookingService.getAllBookings().stream()
            .filter(b -> b.getDate().equals(LocalDate.now())).count());
        return "admin/dashboard";
    }
    
    @GetMapping("/services")
    public String manageServices(Model model) {
        model.addAttribute("services", serviceService.getAllServices());
        return "admin/manage-services";
    }
    
    @GetMapping("/bookings")
    public String manageBookings(Model model, 
                                @RequestParam(required = false) LocalDate date,
                                @RequestParam(required = false) BookingStatus status) {
        var bookings = bookingService.getAllBookings();
        if (date != null) {
            bookings = bookings.stream().filter(b -> b.getDate().equals(date)).toList();
        }
        if (status != null) {
            bookings = bookings.stream().filter(b -> b.getStatus() == status).toList();
        }
        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", BookingStatus.values());
        return "admin/manage-bookings";
    }
    
    @PostMapping("/booking/{id}/approve")
    public String approveBooking(@PathVariable Long id) {
        bookingService.updateBookingStatus(id, BookingStatus.APPROVED);
        return "redirect:/admin/bookings";
    }
    
    @PostMapping("/booking/{id}/reject")
    public String rejectBooking(@PathVariable Long id) {
        bookingService.updateBookingStatus(id, BookingStatus.REJECTED);
        return "redirect:/admin/bookings";
    }
    
    @PostMapping("/booking/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.updateBookingStatus(id, BookingStatus.CANCELLED);
        return "redirect:/admin/bookings";
    }
    
    @PostMapping("/booking/{id}/delete")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "redirect:/admin/bookings";
    }
}
