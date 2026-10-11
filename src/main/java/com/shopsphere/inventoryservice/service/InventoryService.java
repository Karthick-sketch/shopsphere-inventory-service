package com.shopsphere.inventoryservice.service;

import com.shopsphere.inventoryservice.dto.*;
import com.shopsphere.inventoryservice.dto.order.*;
import com.shopsphere.inventoryservice.entity.Inventory;
import com.shopsphere.inventoryservice.entity.InventoryReservation;
import com.shopsphere.inventoryservice.enums.ReservationStatus;
import com.shopsphere.inventoryservice.exceptions.InsufficientStockException;
import com.shopsphere.inventoryservice.repository.InventoryRepository;
import com.shopsphere.inventoryservice.repository.InventoryReservationRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

  private final InventoryRepository inventoryRepository;
  private final InventoryReservationRepository reservationRepository;

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

  @Transactional
  public void reserveStock(OrderReserveRequest request) {
    List<InventoryReservation> reservations = new ArrayList<>();
    for (OrderItemData item : request.getOrderItems()) {
      Inventory inventory = findByProductId(item.getProductId());
      if (inventory.getStock() < item.getQuantity()) {
        throw new InsufficientStockException(
          "Inventory not sufficient for product id: " + item.getProductId()
        );
      }

      inventory.setStock(inventory.getStock() - item.getQuantity());
      inventoryRepository.save(inventory);
      reservations.add(toInventoryReservation(request.getOrderId(), item));
    }
    reservationRepository.saveAll(reservations);
  }

  // Kafka order placed event handler
  @Transactional
  public void reduceStock(OrderEventData data) {
    List<InventoryReservation> reservations =
      reservationRepository.findByOrderId(data.getOrderId());
    for (InventoryReservation res : reservations) {
      res.setStatus(ReservationStatus.CONFIRMED);
    }
    reservationRepository.saveAll(reservations);
  }

  // Kafka order failed/cancelled event handler
  @Transactional
  public void rollbackStock(OrderEventData data) {
    List<InventoryReservation> reservations =
      reservationRepository.findByOrderId(data.getOrderId());
    for (InventoryReservation res : reservations) {
      res.setStatus(ReservationStatus.RELEASED);
      Inventory inventory = findByProductId(res.getProductId());
      inventory.setStock(inventory.getStock() + res.getQuantity());
      inventoryRepository.save(inventory);
    }
    reservationRepository.saveAll(reservations);
  }

  private AvailabilityResponse toAvailabilityResponse(
    Long productId,
    Boolean available
  ) {
    return new AvailabilityResponse(productId, available);
  }

  private InventoryReservation toInventoryReservation(
    Long orderId,
    OrderItemData item
  ) {
    return InventoryReservation.builder()
      .orderId(orderId)
      .productId(item.getProductId())
      .quantity(item.getQuantity())
      .status(ReservationStatus.RESERVED)
      .build();
  }
}
