package com.shopsphere.inventoryservice.kafka;

public final class KafkaConstants {

  private KafkaConstants() {}

  // consumer configs
  public static final String AUTO_OFFSET_RESET_EARLIEST = "earliest";
  public static final String TRUST_ALL_PACKAGES = "*";

  // event types
  public static final String ORDER_PLACED_EVENT_TYPE = "ORDER_PLACED";
}
