package com.revature.project.factory.model;

import static com.revature.project.factory.util.TypeConversionUtils.toLocalDateTime;
import static com.revature.project.factory.util.TypeConversionUtils.toLong;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.revature.project.factory.util.JsonLocalDateTimeDeserializer;
import com.revature.project.factory.util.JsonLocalDateTimeSerializer;

@Entity
@Table(name = "categories")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class Category implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "IS_ACTIVE")
  private Boolean isActive;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "CREATED_BY")
  private Employee createdBy;

  @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
  @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
  @Column(name = "CREATED_ON")
  private LocalDateTime createdOn;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "UPDATED_BY")
  private Employee modifiedBy;

  @JsonSerialize(using = JsonLocalDateTimeSerializer.class)
  @JsonDeserialize(using = JsonLocalDateTimeDeserializer.class)
  @Column(name = "UPDATED_ON")
  private LocalDateTime modifiedOn;

  public Category() {
    // default constructor
  }

  public Category(Long id) {
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setProjectId(Object projectId) {
    if (projectId != null) {
      this.id = Long.parseLong(projectId.toString());
    }
  }

  public Employee getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(Employee createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Object createdOn) {
    this.createdOn = toLocalDateTime(createdOn);
  }

  public Employee getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(Employee modifiedBy) {
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
