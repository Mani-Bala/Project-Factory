package com.revature.project.factory.dto;

import static com.revature.project.factory.util.TypeConversionUtils.toBool;
import static com.revature.project.factory.util.TypeConversionUtils.toLocalDateTime;
import static com.revature.project.factory.util.TypeConversionUtils.toLong;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.revature.project.factory.util.JsonLocalDateTimeDeserializer;
import com.revature.project.factory.util.JsonLocalDateTimeSerializer;
import com.revature.project.factory.validator.BooleanCheck;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProjectDTO {
  private Long id;

  @NotBlank(message = "Project Name must not be empty")
  @Size(min = 1, max = 200, message = "Project Name length must be lesser than or equals to 200")
  private String name;

  @Valid
  @NotNull(message = "Category must not be null")
  private CategoryDTO category;

  @Size(min = 0, max = 8000, message = "description length must be lesser than or equals to 8000")
  private String description;

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
  private List<ProjectSkillDTO> removedSkills;

  private String categoryName;

  private String employeeName;

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

  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Object active) {
    isActive = toBool(active);
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

  public List<ProjectSkillDTO> getProjectSkills() {
    return projectSkills;
  }

  public void setProjectSkills(List<ProjectSkillDTO> projectSkills) {
    this.projectSkills = projectSkills;
  }

  public List<ProjectSkillDTO> getRemovedSkills() {
    return removedSkills;
  }

  public void setRemovedSkills(List<ProjectSkillDTO> removedSkills) {
    this.removedSkills = removedSkills;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }
}
