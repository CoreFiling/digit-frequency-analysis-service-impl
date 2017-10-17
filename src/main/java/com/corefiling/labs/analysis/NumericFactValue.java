package com.corefiling.labs.analysis;

/** The value of a numeric fact. */
public interface NumericFactValue {

  boolean hasValue();

  String getValue();

  boolean isMonetary();

}
