package com.revature.project.factory.dao.query;

/**
 * Contains Admin / Employee related queries
 *
 * @author Revature
 */
public class AdminQuery {

  private AdminQuery() {}

  public static final StringBuilder GET_SYSTEM_USER_BY_EMAIL = new StringBuilder(
      "select em.ID, em.FIRST_NAME, em.LAST_NAME, em.EMAIL_ID,  From employees em where =:userName");
  public static final String GET_USER_BY_ID = "from Employee e where e.id=:userId";
  public static final String GET_USER_BY_EMAIL = "from Employee e where e.emailId=:emailId";

}
