package com.shopsphere.inventoryservice.kafka.events;

import java.time.Instant;
import java.util.UUID;
import lombok.Data;

@Data
public abstract class KafkaEventBase {

  private UUID eventId;
  private String eventType;
  private Instant initiatedAt;
}
