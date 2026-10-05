package com.shopsphere.inventoryservice.dto.order;

import java.util.List;
import lombok.Data;

@Data
public class OrderPlacedData {

  private Long userId;
  private Long orderId;
  private List<OrderItemData> orderItems;
}
