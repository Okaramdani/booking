package com.booking.app.repository;

import com.booking.app.entity.ServiceEntity;
import com.booking.app.entity.ServiceCategory;
import com.booking.app.entity.ServiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByCategory(ServiceCategory category);
    List<ServiceEntity> findByStatus(ServiceStatus status);
    List<ServiceEntity> findByCategoryAndStatus(ServiceCategory category, ServiceStatus status);
}
