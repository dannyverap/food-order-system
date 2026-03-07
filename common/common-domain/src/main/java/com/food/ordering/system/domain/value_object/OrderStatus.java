package com.food.ordering.system.domain.value_object;

public enum OrderStatus {

  PENDING("Pending", "PEN"),
  PAID("Paid", "PAI"),
  APPROVED("Approved", "APP"),
  CANCELLING("Cancelling", "CAN"),
  CANCELLED("Cancelled", "CAC"),
  COMPLETED("Completed", "COM");

  private final String value;
  private final String code;

  OrderStatus(String value, String code) {
    this.value = value;
    this.code = code;
  }

  public String getValue() {
    return value;
  }

  public String getCode() {
    return code;
  }
}
