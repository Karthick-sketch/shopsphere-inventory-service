package com.shopsphere.inventoryservice.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrderReserveRequest {

  private Long orderId;
  private List<OrderItemData> orderItems;
}
