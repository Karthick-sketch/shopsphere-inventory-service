package com.shopsphere.inventoryservice.service;

import com.shopsphere.inventoryservice.dto.*;
import com.shopsphere.inventoryservice.dto.order.*;
import com.shopsphere.inventoryservice.entity.Inventory;
import com.shopsphere.inventoryservice.repository.InventoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;

  public List<Inventory> findAll() {
    return inventoryRepository.findAll();
  }

  public Inventory findById(Long id) {
    return inventoryRepository
      .findById(id)
      .orElseThrow(() ->
        new RuntimeException("Inventory record not found with id: " + id)
      );
  }

  public Inventory findByProductId(Long productId) {
    return inventoryRepository
      .findByProductId(productId)
      .orElseThrow(() ->
        new RuntimeException("Inventory not found for product id: " + productId)
      );
  }

  public AvailabilityResponse getAvailability(AvailabilityRequest request) {
    Boolean available = inventoryRepository.checkAvailability(
      request.getProductId(),
      request.getQuantity()
    );
    return toAvailabilityResponse(request.getProductId(), available);
  }

  public Inventory create(Inventory inventory) {
    return inventoryRepository.save(inventory);
  }

  public Inventory update(Long id, Inventory updated) {
    Inventory existing = findById(id);
    existing.setStock(updated.getStock());
    existing.setIsAvailable(updated.getIsAvailable());
    return inventoryRepository.save(existing);
  }

  public void delete(Long id) {
    findById(id);
    inventoryRepository.deleteById(id);
  }

  public void reduceStock(OrderPlacedData data) {
    for (OrderItemData item : data.getOrderItems()) {
      Inventory inventory = findByProductId(item.getProductId());
      if (inventory.getStock() < item.getQuantity()) {
        throw new RuntimeException(
          "Inventory not sufficient for product id: " + item.getProductId()
        );
      }
      inventory.setStock(inventory.getStock() - item.getQuantity());
      inventoryRepository.save(inventory);
    }
  }

  private AvailabilityResponse toAvailabilityResponse(
    Long productId,
    Boolean available
  ) {
    return new AvailabilityResponse(productId, available);
  }
}
