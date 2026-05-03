package com.booking.app.service;

import com.booking.app.dto.ServiceRequest;
import com.booking.app.entity.ServiceEntity;
import com.booking.app.entity.ServiceCategory;
import com.booking.app.entity.ServiceStatus;
import com.booking.app.repository.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final FileStorageService fileStorageService;
    
    public ServiceService(ServiceRepository serviceRepository, FileStorageService fileStorageService) {
        this.serviceRepository = serviceRepository;
        this.fileStorageService = fileStorageService;
    }
    
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }
    
    public List<ServiceEntity> getServicesByCategory(ServiceCategory category) {
        return serviceRepository.findByCategoryAndStatus(category, ServiceStatus.AVAILABLE);
    }
    
    public List<ServiceEntity> getAllServicesByCategory(ServiceCategory category) {
        return serviceRepository.findByCategory(category);
    }
    
    public ServiceEntity getServiceById(Long id) {
        return serviceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Service not found"));
    }
    
    public ServiceEntity createService(ServiceRequest request) throws IOException {
        ServiceEntity service = new ServiceEntity();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setCategory(request.getCategory());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setStatus(request.getStatus());
        
        MultipartFile imageFile = request.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            service.setImageUrl(fileStorageService.storeFile(imageFile));
        } else {
            service.setImageUrl(request.getImageUrl());
        }
        
        return serviceRepository.save(service);
    }
    
    public ServiceEntity updateService(Long id, ServiceRequest request) throws IOException {
        ServiceEntity service = getServiceById(id);
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setCategory(request.getCategory());
        service.setPrice(request.getPrice());
        service.setDuration(request.getDuration());
        service.setStatus(request.getStatus());
        
        MultipartFile imageFile = request.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            service.setImageUrl(fileStorageService.storeFile(imageFile));
        } else if (request.getImageUrl() != null) {
            service.setImageUrl(request.getImageUrl());
        }
        
        return serviceRepository.save(service);
    }
    
    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
