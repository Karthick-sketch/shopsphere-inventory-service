package com.shopsphere.inventoryservice.dto.order;

import lombok.Data;

@Data
public class OrderEventData {

  private Long userId;
  private Long orderId;
}
