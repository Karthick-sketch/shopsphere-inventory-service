package com.shopsphere.inventoryservice.dto;

import java.util.List;
import lombok.Data;

@Data
public class ProductIdsRequest {

  private List<Long> productIds;
}
