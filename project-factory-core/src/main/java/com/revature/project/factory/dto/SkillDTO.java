package com.revature.project.factory.dto;

import static com.revature.project.factory.util.TypeConversionUtils.toInteger;
import static com.revature.project.factory.util.TypeConversionUtils.toLocalDateTime;
import static com.revature.project.factory.util.TypeConversionUtils.toLong;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.revature.project.factory.util.JsonLocalDateTimeDeserializer;
import com.revature.project.factory.util.JsonLocalDateTimeSerializer;
import com.revature.project.factory.validator.BooleanCheck;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SkillDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "Skill Name must not be empty")
  @Size(min = 1, max = 200, message = "Skill Name length must be lesser than or equals to 200")
  private String name;

  private Integer score;

  @BooleanCheck
  private Boolean isActive = true;

  private SystemUserDTO createdBy;

  @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
  @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
  private LocalDateTime createdOn;

  private SystemUserDTO modifiedBy;

  @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
  @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
  private LocalDateTime modifiedOn;

  private List<ProjectSkillDTO> projectSkills;

  public SkillDTO() {
    // default constructor
  }

  public SkillDTO(Long id) {
    this.id = id;
  }

  // ---------------------------------------------------- GETTER SETTER
  public Long getId() {
    return id;
  }

  public void setId(Object id) {
    this.id = toLong(id);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Object score) {
    this.score = toInteger(score);
  }

  public void setProjectId(Object projectId) {
    if (projectId != null) {
      this.id = Long.parseLong(projectId.toString());
    }
  }

  public SystemUserDTO getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(SystemUserDTO createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Object createdOn) {
    this.createdOn = toLocalDateTime(createdOn);
  }

  public SystemUserDTO getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(SystemUserDTO modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  public LocalDateTime getModifiedOn() {
    return modifiedOn;
  }

  public void setModifiedOn(Object modifiedOn) {
    this.modifiedOn = toLocalDateTime(modifiedOn);
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public List<ProjectSkillDTO> getProjectSkills() {
    return projectSkills;
  }

  public void setInternshipSkills(List<ProjectSkillDTO> projectSkills) {
    this.projectSkills = projectSkills;
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
