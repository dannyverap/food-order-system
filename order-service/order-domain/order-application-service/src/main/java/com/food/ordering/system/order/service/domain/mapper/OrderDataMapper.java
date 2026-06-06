package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface OrderDataMapper {

  @Mapping(target ="orderTrackingId", source = "trackingId.id")
  @Mapping(target = "orderStatus", source = "orderStatus")
  CreateOrderResponse toCreateOrderResponse(Order order);

  @Mapping(target = "customerId.id", source = "customerId")
  @Mapping(target = "restaurantId.id", source = "restaurantId")
  @Mapping(target = "deliveryAddress.id", expression = "java(java.util.UUID.randomUUID())")
  @Mapping(target = "deliveryAddress.street", source = "address.street")
  @Mapping(target = "deliveryAddress.postalCode", source = "address.postalCode")
  @Mapping(target = "deliveryAddress.city", source = "address.city")
  @Mapping(target = "price.amount", source = "price")
  @Mapping(target = "items", source = "items", qualifiedByName = "toOrderItemDomain")
  Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand);

  @Named("toOrderItemDomain")
  @Mapping(target = "product.productId.id", source = "productId")
  @Mapping(target = "quantity", source = "quantity")
  @Mapping(target = "price.amount", source = "price")
  @Mapping(target = "subTotal.amount", source = "subTotal")
  OrderItem toOrderItemDomain(
      com.food.ordering.system.order.service.domain.dto.create.OrderItem orderItem);


  @Mapping(target = "restaurantId.id", source = "restaurantId")
  @Mapping(target = "products", source = "items")
  Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand);

  @Mapping(target = "productId.id", source = "productId")
  @Mapping(target = "name", ignore = true)
  @Mapping(target = "price", ignore = true)
  Product orderItemToProduct(
      com.food.ordering.system.order.service.domain.dto.create.OrderItem orderItem);


}
