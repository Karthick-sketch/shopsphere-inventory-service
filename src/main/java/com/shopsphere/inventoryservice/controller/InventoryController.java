package com.shopsphere.inventoryservice.controller;

import com.shopsphere.inventoryservice.entity.Inventory;
import com.shopsphere.inventoryservice.service.InventoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

  private final InventoryService inventoryService;

  @GetMapping
  public ResponseEntity<List<Inventory>> getAll() {
    return ResponseEntity.ok(inventoryService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Inventory> getById(@PathVariable Long id) {
    return ResponseEntity.ok(inventoryService.findById(id));
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<Inventory> getByProductId(
    @PathVariable Long productId
  ) {
    return ResponseEntity.ok(inventoryService.findByProductId(productId));
  }

  @PostMapping
  public ResponseEntity<Inventory> create(@RequestBody Inventory inventory) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
      inventoryService.create(inventory)
    );
  }

  @PutMapping("/{id}")
  public ResponseEntity<Inventory> update(
    @PathVariable Long id,
    @RequestBody Inventory inventory
  ) {
    return ResponseEntity.ok(inventoryService.update(id, inventory));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    inventoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
