package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.value_object.ProductId;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

  private static final String UTC = "UTC";

  @Override
  public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
    validateRestaurante(restaurant);
    setOrderProductInformation(order, restaurant);
    order.validateOrder();
    order.initializeOrder();
    log.info("Order with id: {} has been initialized", order.getId());
    return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  private void setOrderProductInformation(Order order, Restaurant restaurant) {
    Map<ProductId, Product> restaurantProductMap = new HashMap<>();
    restaurant.getProducts().forEach(product -> restaurantProductMap.put(product.getId(), product));

    order.getItems().forEach(orderItem -> {
      ProductId productId = orderItem.getProduct().getId();
      Product restaurantProduct = restaurantProductMap.get(productId);
      if (restaurantProduct != null) {
        orderItem.getProduct().updateWithConfirmedNameAndPrice(
            restaurantProduct.getName(), restaurantProduct.getPrice());
      }
    });
  }

  private void validateRestaurante(Restaurant restaurant) {
    if (!restaurant.isActive()) {
      throw new OrderDomainException("Restaurant is not active");
    }

  }

  @Override
  public OrderPaidEvent payOrder(Order order) {
    order.pay();
    log.info("Order with id: {} has been paid", order.getId());
    return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public void approveOrder(Order order) {
    order.approve();
    log.info("Order with id: {} has been approved", order.getId());
  }

  @Override
  public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
    order.initCancellation(failureMessages);
    log.info("Order with id: {} has been cancelled", order.getId());
    return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
  }

  @Override
  public void cancelOrder(Order order, List<String> failureMessages) {
    order.cancel();
    log.info("Order with id: {} has been cancelled", order.getId());
  }
}
