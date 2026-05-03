package com.booking.app.repository;

import com.booking.app.entity.Booking;
import com.booking.app.entity.BookingStatus;
import com.booking.app.entity.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserEmail(String email);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByDate(LocalDate date);
    List<Booking> findByService(ServiceEntity service);
}
