package com.food.ordering.system.domain.value_object;

import java.util.UUID;

public class ProductId extends BaseId<UUID> {

  public ProductId(UUID id) {
    super(id);
  }
}
