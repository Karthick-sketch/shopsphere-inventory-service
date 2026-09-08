package com.shopsphere.inventoryservice.repository;

import com.shopsphere.inventoryservice.entity.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
  Optional<Inventory> findByProductId(Long productId);
}
