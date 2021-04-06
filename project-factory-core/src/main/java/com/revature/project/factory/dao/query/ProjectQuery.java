package com.revature.project.factory.dao.query;

public class ProjectQuery {
  private ProjectQuery() {}

  public static final StringBuilder GET_PROJECT_BY_ID = new StringBuilder(
      "SELECT p from Project p left join fetch p.category left join fetch p.createdBy left join fetch p.projectSkills where p.id = :id");

  public static final StringBuilder GET_PROJECTS =
      new StringBuilder(" SELECT p.ID id, p.NAME name, p.IS_ACTIVE isActive, c.NAME categoryName, "
          + " CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS employeeName " + " FROM projects p "
          + " LEFT JOIN categories c ON c.ID=p.CATEGORY_ID "
          + " LEFT JOIN employees e ON e.ID=p.CREATED_BY ");

  public static final StringBuilder ACTIVATE_DEACTIVATE_PROJECT_BY_ID = new StringBuilder(
      "UPDATE projects p SET p.IS_ACTIVE = :status,p.UPDATED_BY = :updatedBy, p.UPDATED_ON = :updatedOn WHERE p.id=:id");

  public static final StringBuilder CHECK_UNIQUE_PROJECT_BY_PROJECT_NAME =
      new StringBuilder("Select i.id From projects i WHERE i.name=:projectName");

  public static final StringBuilder DELETE_PROJECT_SKILLS_BY_PROJECT_ID =
      new StringBuilder(" Delete from project_skills ps  WHERE ps.PROJECT_ID=:projectId ");

  public static final StringBuilder DELETE_PROJECT_BY_PROJECT_ID =
      new StringBuilder(" DELETE FROM projects p WHERE p.id= :projectId ");

  public static final StringBuilder GET_SKILLS = new StringBuilder(
      " select s.ID id, s.NAME name , s.SCORE score, s.IS_ACTIVE isActive from skills s ");

}
