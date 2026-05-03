package com.booking.app.controller;

import com.booking.app.dto.BookingRequest;
import com.booking.app.entity.ServiceCategory;
import com.booking.app.service.BookingService;
import com.booking.app.service.ServiceService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final ServiceService serviceService;
    private final BookingService bookingService;
    
    public UserController(ServiceService serviceService, BookingService bookingService) {
        this.serviceService = serviceService;
        this.bookingService = bookingService;
    }
    
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("categories", ServiceCategory.values());
        return "user/home";
    }
    
    @GetMapping("/services")
    public String listServices(@RequestParam(required = false) ServiceCategory category, Model model) {
        var services = (category != null) ? 
            serviceService.getServicesByCategory(category) : 
            serviceService.getAllServices().stream()
                .filter(s -> s.getStatus() == com.booking.app.entity.ServiceStatus.AVAILABLE).toList();
        
        model.addAttribute("services", services);
        model.addAttribute("categories", ServiceCategory.values());
        model.addAttribute("selectedCategory", category);
        return "user/services";
    }
    
    @GetMapping("/services/{id}")
    public String serviceDetail(@PathVariable Long id, Model model) {
        var service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setServiceId(id);
        model.addAttribute("bookingRequest", bookingRequest);
        return "user/service-detail";
    }
    
    @PostMapping("/bookings")
    public String createBooking(@ModelAttribute BookingRequest request,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        try {
            bookingService.createBooking(auth.getName(), request);
            redirectAttributes.addFlashAttribute("success", "Booking created successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-bookings";
    }
    
    @GetMapping("/my-bookings")
    public String myBookings(Authentication auth, Model model) {
        model.addAttribute("bookings", bookingService.getUserBookings(auth.getName()));
        return "user/my-bookings";
    }
    
    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, Authentication auth, 
                               RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id, auth.getName());
            redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/my-bookings";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/my-bookings";
    }
}
