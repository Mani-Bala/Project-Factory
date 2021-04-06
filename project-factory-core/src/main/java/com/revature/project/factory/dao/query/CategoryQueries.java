package com.revature.project.factory.dao.query;

public class CategoryQueries {

  public static final StringBuilder GET_ALL_CATEGORY =
      new StringBuilder("SELECT c.ID id,c.NAME name,c.IS_ACTIVE isActive "
          + " FROM categories c WHERE {status} " + " Order By c.name");

  private CategoryQueries() {
    // empty constructor
  }
}
