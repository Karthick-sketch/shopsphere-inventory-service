package com.shopsphere.inventoryservice.kafka.consumer;

import com.shopsphere.inventoryservice.kafka.KafkaConstants;
import com.shopsphere.inventoryservice.kafka.events.OrderPlacedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

  @Value("${kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ConsumerFactory<String, OrderPlacedEvent> consumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    props.put(
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
      KafkaConstants.AUTO_OFFSET_RESET_EARLIEST
    );
    props.put(
      ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS,
      StringDeserializer.class
    );
    props.put(
      ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
      JacksonJsonDeserializer.class
    );

    JacksonJsonDeserializer<OrderPlacedEvent> valueDeserializer =
      new JacksonJsonDeserializer<>(OrderPlacedEvent.class);
    valueDeserializer.addTrustedPackages(KafkaConstants.TRUST_ALL_PACKAGES);
    valueDeserializer.setUseTypeHeaders(false);

    return new DefaultKafkaConsumerFactory<>(
      props,
      new ErrorHandlingDeserializer<>(new StringDeserializer()),
      new ErrorHandlingDeserializer<>(valueDeserializer)
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<
    String,
    OrderPlacedEvent
  > kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, OrderPlacedEvent> factory =
      new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
