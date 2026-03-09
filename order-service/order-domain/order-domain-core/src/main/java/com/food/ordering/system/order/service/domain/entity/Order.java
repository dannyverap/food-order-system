package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.AggregateRoot;
import com.food.ordering.system.domain.value_object.CustomerId;
import com.food.ordering.system.domain.value_object.Money;
import com.food.ordering.system.domain.value_object.OrderId;

import com.food.ordering.system.domain.value_object.OrderStatus;
import com.food.ordering.system.domain.value_object.RestaurantId;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class Order extends AggregateRoot<OrderId> {

  private final CustomerId customerId;
  private final RestaurantId restaurantId;
  private final StreetAddress deliveryAddress;
  private final Money price;
  private final List<OrderItem> items;
  private TrackingId trackingId;
  private OrderStatus orderStatus;
  private List<String> failureMessages;

  private Order(Builder builder) {
    super.setId(builder.orderId);
    customerId = builder.customerId;
    restaurantId = builder.restaurantId;
    deliveryAddress = builder.deliveryAddress;
    price = builder.price;
    items = builder.items;
    trackingId = builder.trackingId;
    orderStatus = builder.orderStatus;
    failureMessages = builder.failureMessages != null
        ? builder.failureMessages
        : new ArrayList<>();
  }

  public static Builder builder() {
    return new Builder();
  }

  public void validateOrder() {
    validateInitialOrder();
    validateTotalPrice();
    validateItemsPrice();
  }

  public void pay() {
    if (!orderStatus.equals(OrderStatus.PENDING)) {
      throw new OrderDomainException("Order is not in correct state for pay operation");
    }
    orderStatus = OrderStatus.PAID;
  }

  public void approve() {
    if (!orderStatus.equals(OrderStatus.PAID)) {
      throw new OrderDomainException("Order is not in correct state for approve operation");
    }
    orderStatus = OrderStatus.APPROVED;
  }

  public void initCancellation(List<String> failureMessages) {
    if (!orderStatus.equals(OrderStatus.PAID)) {
      throw new OrderDomainException(
          "Order is not in correct state for init cancellation operation");
    }
    orderStatus = OrderStatus.CANCELLING;
    addFailureMessages(failureMessages);
  }

  public void cancel() {
    if (!(orderStatus.equals(OrderStatus.CANCELLING) || orderStatus.equals(OrderStatus.PENDING))) {
      throw new OrderDomainException("Order is not in correct state for cancel operation");
    }
    orderStatus = OrderStatus.CANCELLED;
  }

  private void addFailureMessages(List<String> failureMessages) {
    if (failureMessages == null) {
      return;
    }
    this.failureMessages.addAll(
        failureMessages.stream()
            .filter(Predicate.not(String::isEmpty))
            .toList()
    );
  }

  private void validateItemsPrice() {
    Money orderItemsTotal = (items.stream().map(orderItem -> {
      validateItemPrice(orderItem);
      return orderItem.getSubTotal();
    }).reduce(Money.ZERO, Money::add));
    if (!price.amount().equals(orderItemsTotal.amount())) {
      throw new OrderDomainException(
          "Total price: " + price.amount() + " is not equal to Order items total: "
              + orderItemsTotal);
    }
  }

  private void validateItemPrice(OrderItem orderItem) {
    if (!orderItem.isPriceValid()) {
      throw new OrderDomainException(
          "Order item price: " + orderItem.getPrice().amount() + " is not valid for product: "
              + orderItem.getProduct().getId());
    }
  }

  private void validateTotalPrice() {
    if (price == null || !price.isGreaterThanZero()) {
      throw new OrderDomainException("Total price must be greater than zero");
    }
  }

  private void validateInitialOrder() {
    if (orderStatus != null || getId() != null) {
      throw new OrderDomainException("Order is not in correct state for initialization");
    }

  }

  public void initializeOrder() {
    setId(new OrderId(UUID.randomUUID()));
    trackingId = new TrackingId(UUID.randomUUID());
    orderStatus = OrderStatus.PENDING;
    initializerOrderItems();
  }

  private void initializerOrderItems() {
    long itemId = 1;
    for (OrderItem orderItem : items) {
      orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));

    }
  }

  public CustomerId getCustomerId() {
    return customerId;
  }

  public RestaurantId getRestaurantId() {
    return restaurantId;
  }

  public StreetAddress getDeliveryAddress() {
    return deliveryAddress;
  }

  public Money getPrice() {
    return price;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public TrackingId getTrackingId() {
    return trackingId;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public List<String> getFailureMessages() {
    return failureMessages;
  }

  public static final class Builder {

    private OrderId orderId;
    private CustomerId customerId;
    private RestaurantId restaurantId;
    private StreetAddress deliveryAddress;
    private Money price;
    private List<OrderItem> items;
    private TrackingId trackingId;
    private OrderStatus orderStatus;
    private List<String> failureMessages;

    private Builder() {
    }


    public Builder orderId(OrderId val) {
      orderId = val;
      return this;
    }

    public Builder customerId(CustomerId val) {
      customerId = val;
      return this;
    }

    public Builder restaurantId(RestaurantId val) {
      restaurantId = val;
      return this;
    }

    public Builder deliveryAddress(StreetAddress val) {
      deliveryAddress = val;
      return this;
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Builder items(List<OrderItem> val) {
      items = val;
      return this;
    }

    public Builder trackingId(TrackingId val) {
      trackingId = val;
      return this;
    }

    public Builder orderStatus(OrderStatus val) {
      orderStatus = val;
      return this;
    }

    public Builder failureMessages(List<String> val) {
      failureMessages = val;
      return this;
    }

    public Order build() {
      return new Order(this);
    }
  }
}

