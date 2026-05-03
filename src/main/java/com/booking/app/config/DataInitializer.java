package com.booking.app.config;

import com.booking.app.entity.*;
import com.booking.app.repository.ServiceRepository;
import com.booking.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public DataInitializer(UserRepository userRepository, ServiceRepository serviceRepo) {
        this.userRepository = userRepository;
        this.serviceRepo = serviceRepo;
    }
    
    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@booking.com")) {
            User admin = new User();
            admin.setName("Administrator");
            admin.setEmail("admin@booking.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
        
        if (serviceRepo.count() == 0) {
            createSampleServices();
        }
    }
    
    private void createSampleServices() {
        ServiceEntity s1 = new ServiceEntity();
        s1.setName("General Checkup");
        s1.setDescription("Basic medical examination");
        s1.setCategory(ServiceCategory.CLINIC);
        s1.setPrice(150000.0);
        s1.setDuration(30);
        s1.setStatus(ServiceStatus.AVAILABLE);
        s1.setImageUrl("https://via.placeholder.com/300");
        serviceRepo.save(s1);
        
        ServiceEntity s2 = new ServiceEntity();
        s2.setName("Haircut");
        s2.setDescription("Professional haircut service");
        s2.setCategory(ServiceCategory.BARBERSHOP);
        s2.setPrice(50000.0);
        s2.setDuration(45);
        s2.setStatus(ServiceStatus.AVAILABLE);
        s2.setImageUrl("https://via.placeholder.com/300");
        serviceRepo.save(s2);
        
        ServiceEntity s3 = new ServiceEntity();
        s3.setName("Facial Treatment");
        s3.setDescription("Refreshing facial care");
        s3.setCategory(ServiceCategory.SALON);
        s3.setPrice(200000.0);
        s3.setDuration(60);
        s3.setStatus(ServiceStatus.AVAILABLE);
        s3.setImageUrl("https://via.placeholder.com/300");
        serviceRepo.save(s3);
        
        ServiceEntity s4 = new ServiceEntity();
        s4.setName("Meeting Room A");
        s4.setDescription("Capacity 10 people");
        s4.setCategory(ServiceCategory.ROOM);
        s4.setPrice(300000.0);
        s4.setDuration(120);
        s4.setStatus(ServiceStatus.AVAILABLE);
        s4.setImageUrl("https://via.placeholder.com/300");
        serviceRepo.save(s4);
    }
}
