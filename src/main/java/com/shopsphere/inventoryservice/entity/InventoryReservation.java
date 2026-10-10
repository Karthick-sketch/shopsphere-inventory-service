package com.shopsphere.inventoryservice.entity;

import com.shopsphere.inventoryservice.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory_reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long orderId;

  @Column(nullable = false)
  private Long productId;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ReservationStatus status;
}
