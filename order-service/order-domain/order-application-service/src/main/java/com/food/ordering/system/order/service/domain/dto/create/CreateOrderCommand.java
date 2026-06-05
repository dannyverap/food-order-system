package com.food.ordering.system.order.service.domain.dto.create;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record CreateOrderCommand(@NotNull UUID customerId, @NotNull UUID restaurantId,
                                 @NotNull BigDecimal price, @NotNull Map<UUID, OrderItem> items,
                                 @NotNull OrderAddress address) {

}
