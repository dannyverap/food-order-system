package com.food.ordering.system.order.service.domain.dto.message;

import com.food.ordering.system.domain.value_object.OrderApprovalStatus;
import java.time.Instant;
import java.util.List;

public record RestaurantApprovalResponse(
    String id,
    String sagaId,
    String orderId,
    String restaurantId,
    Instant createdAt,
    OrderApprovalStatus orderApprovalStatus,
    List<String> failureMessages
) {

}
