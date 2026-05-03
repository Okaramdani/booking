package com.booking.app.service;

import com.booking.app.dto.BookingRequest;
import com.booking.app.entity.Booking;
import com.booking.app.entity.BookingStatus;
import com.booking.app.entity.ServiceEntity;
import com.booking.app.entity.ServiceStatus;
import com.booking.app.entity.User;
import com.booking.app.repository.BookingRepository;
import com.booking.app.repository.ServiceRepository;
import com.booking.app.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ServiceRepository serviceRepo;
    private final UserRepository userRepository;
    
    public BookingService(BookingRepository bookingRepository, ServiceRepository serviceRepo, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.serviceRepo = serviceRepo;
        this.userRepository = userRepository;
    }
    
    public List<Booking> getUserBookings(String email) {
        return bookingRepository.findByUserEmail(email);
    }
    
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public Booking createBooking(String userEmail, BookingRequest request) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        ServiceEntity service = serviceRepo.findById(request.getServiceId())
            .orElseThrow(() -> new RuntimeException("Service not found"));
        
        if (service.getStatus() != ServiceStatus.AVAILABLE) {
            throw new RuntimeException("Service is not available");
        }
        
        LocalTime endTime = request.getStartTime().plusMinutes(service.getDuration());

        boolean conflict = bookingRepository.findAll().stream().anyMatch(existingBooking ->
            existingBooking.getDate().equals(request.getDate()) &&
            existingBooking.getService().equals(service) &&
            existingBooking.getStatus() != BookingStatus.CANCELLED &&
            existingBooking.getStartTime().isBefore(endTime) &&
            existingBooking.getEndTime().isAfter(request.getStartTime())
        );
        
        if (conflict) {
            throw new RuntimeException("Time slot already booked for this service");
        }
        
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setService(service);
        booking.setDate(request.getDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(endTime);
        booking.setStatus(BookingStatus.PENDING);
        booking.setNote(request.getNote());
        
        return bookingRepository.save(booking);
    }
    
    public Booking updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
    
    public void cancelBooking(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }
        
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new RuntimeException("Cannot cancel approved booking");
        }
        
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}
