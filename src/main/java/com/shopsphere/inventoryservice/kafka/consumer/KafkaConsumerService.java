package com.shopsphere.inventoryservice.kafka.consumer;

import com.shopsphere.inventoryservice.kafka.events.*;
import com.shopsphere.inventoryservice.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class KafkaConsumerService {

  private final InventoryService inventoryService;

  @KafkaListener(
    topics = "${kafka.topic.order-placed}",
    groupId = "${kafka.consumer.group-id}"
  )
  public void handleOrderPlacedEvent(OrderPlacedEvent event) {
    inventoryService.reduceStock(event.getData());
  }

  @KafkaListener(
    topics = "${kafka.topic.order-failed}",
    groupId = "${kafka.consumer.group-id}"
  )
  public void handleOrderFailedEvent(OrderFailedEvent event) {
    inventoryService.rollbackStock(event.getData());
  }
}
