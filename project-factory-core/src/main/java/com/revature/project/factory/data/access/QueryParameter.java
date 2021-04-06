package com.revature.project.factory.data.access;

public class QueryParameter<V> {

  private String name;
  private V value;

  public QueryParameter(String name, V value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public V getValue() {
    return value;
  }

}
