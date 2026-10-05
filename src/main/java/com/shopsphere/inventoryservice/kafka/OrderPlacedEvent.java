package com.shopsphere.inventoryservice.kafka;

import com.shopsphere.inventoryservice.dto.order.OrderPlacedData;
import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderPlacedEvent {

  private UUID eventId;
  private String eventType;
  private Instant initiatedAt;
  private OrderPlacedData data;
}
