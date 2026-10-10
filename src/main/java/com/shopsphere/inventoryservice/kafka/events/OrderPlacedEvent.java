package com.shopsphere.inventoryservice.kafka.events;

import com.shopsphere.inventoryservice.dto.order.OrderEventData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlacedEvent extends KafkaEventBase {

  private OrderEventData data;
}
