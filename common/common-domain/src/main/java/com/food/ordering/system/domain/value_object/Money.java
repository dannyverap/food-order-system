package com.food.ordering.system.domain.value_object;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {

  public static final Money ZERO = new Money(BigDecimal.ZERO);

  public Money {
    Objects.requireNonNull(amount, "Amount cannot be null");
    amount = setScale(amount);
  }

  private static BigDecimal setScale(BigDecimal input) {
    return input.setScale(2, RoundingMode.HALF_EVEN);
  }

  public boolean isGreaterThanZero() {
    return amount.compareTo(BigDecimal.ZERO) > 0;
  }

  public boolean isGreaterThan(Money money) {
    return this.amount.compareTo(money.amount) > 0;
  }

  public Money add(Money money) {
    return new Money(this.amount.add(money.amount));
  }

  public Money subtract(Money money) {
    return new Money(this.amount.subtract(money.amount));
  }

  public Money multiply(int multiplier) {
    return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)));
  }
}

