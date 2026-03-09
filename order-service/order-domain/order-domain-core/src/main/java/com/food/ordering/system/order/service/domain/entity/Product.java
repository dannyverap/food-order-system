package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.value_object.Money;
import com.food.ordering.system.domain.value_object.ProductId;

public class Product extends BaseEntity<ProductId> {

  private String name;
  private Money price;

  private Product(Builder builder) {
    super.setId(builder.productId);
    name = builder.name;
    price = builder.price;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String getName() {
    return name;
  }

  public Money getPrice() {
    return price;
  }

  public static final class Builder {

    private ProductId productId;
    private String name;
    private Money price;

    private Builder() {
    }


    public Builder productId(ProductId val) {
      productId = val;
      return this;
    }

    public Builder name(String val) {
      name = val;
      return this;
    }

    public Builder price(Money val) {
      price = val;
      return this;
    }

    public Product build() {
      return new Product(this);
    }
  }
}
