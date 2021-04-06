package com.revature.project.factory.dto;

import static com.revature.project.factory.util.TypeConversionUtils.toInteger;
import static com.revature.project.factory.util.TypeConversionUtils.toLong;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProjectSkillDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @JsonIgnore
  private ProjectDTO project;

  private SkillDTO skill;

  private Integer score;

  public Long getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = toLong(id);
  }

  public SkillDTO getSkill() {
    return skill;
  }

  public void setSkill(SkillDTO skill) {
    this.skill = skill;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Object score) {
    this.score = toInteger(score);
  }

  public ProjectDTO getProject() {
    return project;
  }

  public void setProject(ProjectDTO project) {
    this.project = project;
  }

  // --------------------------------------------------- Hash code Override

  @Override
  public int hashCode() {
    Integer result = null;
    if (getId() != null) {
      result = Integer.parseInt(getId().toString());
    } else {
      result = new String().hashCode();
    }
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    boolean result = false;
    if (obj != null && this.hashCode() == obj.hashCode()) {
      result = true;
    }
    return result;
  }

}
