package com.revature.project.factory.dto;

import static com.revature.project.factory.util.TypeConversionUtils.toLocalDateTime;
import static com.revature.project.factory.util.TypeConversionUtils.toLong;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.revature.project.factory.util.JsonLocalDateTimeDeserializer;
import com.revature.project.factory.util.JsonLocalDateTimeSerializer;
import com.revature.project.factory.validator.BooleanCheck;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CategoryDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  @NotBlank(message = "Category Name must not be empty")
  @Size(min = 1, max = 200, message = "Category Name length must be lesser than or equals to 200")
  private String name;

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

  public CategoryDTO() {
    // default constructor
  }

  public CategoryDTO(Object id) {
    this.id = toLong(id);
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setCategoryId(Object id) {
    if (id != null) {
      this.id = Long.parseLong(id.toString());
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
