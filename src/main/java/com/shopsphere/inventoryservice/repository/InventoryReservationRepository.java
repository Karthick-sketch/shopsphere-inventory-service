package com.shopsphere.inventoryservice.repository;

import com.shopsphere.inventoryservice.entity.InventoryReservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryReservationRepository
  extends JpaRepository<InventoryReservation, Long>
{
  List<InventoryReservation> findByOrderId(Long orderId);
}
