package com.food.ordering.system.order.service.domain.ports.input.handler;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;

public interface OrderCreateCommandHandler {

  CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand);

}

