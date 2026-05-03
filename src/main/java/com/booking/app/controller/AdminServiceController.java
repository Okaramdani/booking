package com.booking.app.controller;

import com.booking.app.dto.ServiceRequest;
import com.booking.app.entity.ServiceCategory;
import com.booking.app.entity.ServiceStatus;
import com.booking.app.service.ServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminServiceController {
    private final ServiceService serviceService;
    
    public AdminServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }
    
    @GetMapping("/services/new")
    public String newServiceForm(Model model) {
        model.addAttribute("serviceRequest", new ServiceRequest());
        model.addAttribute("categories", ServiceCategory.values());
        model.addAttribute("statuses", ServiceStatus.values());
        return "admin/service-form";
    }
    
    @PostMapping("/services")
    public String createService(@ModelAttribute ServiceRequest request) throws IOException {
        serviceService.createService(request);
        return "redirect:/admin/services";
    }
    
    @GetMapping("/services/{id}/edit")
    public String editServiceForm(@PathVariable Long id, Model model) {
        var service = serviceService.getServiceById(id);
        ServiceRequest request = new ServiceRequest();
        request.setName(service.getName());
        request.setDescription(service.getDescription());
        request.setCategory(service.getCategory());
        request.setPrice(service.getPrice());
        request.setDuration(service.getDuration());
        request.setStatus(service.getStatus());
        request.setImageUrl(service.getImageUrl());
        
        model.addAttribute("serviceRequest", request);
        model.addAttribute("categories", ServiceCategory.values());
        model.addAttribute("statuses", ServiceStatus.values());
        model.addAttribute("serviceId", id);
        return "admin/service-form";
    }
    
    @PostMapping("/services/{id}")
    public String updateService(@PathVariable Long id, @ModelAttribute ServiceRequest request) throws IOException {
        serviceService.updateService(id, request);
        return "redirect:/admin/services";
    }
    
    @PostMapping("/services/{id}/delete")
    public String deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return "redirect:/admin/services";
    }
}
