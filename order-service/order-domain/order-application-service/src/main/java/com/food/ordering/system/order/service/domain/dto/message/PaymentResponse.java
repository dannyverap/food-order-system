package com.food.ordering.system.order.service.domain.dto.message;

import com.food.ordering.system.domain.value_object.PaymentStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PaymentResponse(
    String id,
    String sagaId,
    String orderId,
    String paymentId,
    String customerId,
    BigDecimal price,
    Instant createdAt,
    PaymentStatus paymentStatus,
    List<String> failureMessages

) {

}
