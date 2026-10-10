package com.shopsphere.inventoryservice.dto;

import lombok.Data;

@Data
public class OrderItemData {

  private Long productId;
  private Integer quantity;
}
