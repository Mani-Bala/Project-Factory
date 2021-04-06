package com.revature.project.factory.dto;

import java.util.List;

public class MetadataDTO {
  public List<CategoryDTO> categoryDTOS;
  public List<SkillDTO> skillDTOS;

  public List<CategoryDTO> getCategoryDTOS() {
    return categoryDTOS;
  }

  public void setCategoryDTOS(List<CategoryDTO> categoryDTOS) {
    this.categoryDTOS = categoryDTOS;
  }

  public List<SkillDTO> getSkillDTOS() {
    return skillDTOS;
  }

  public void setSkillDTOS(List<SkillDTO> skillDTOS) {
    this.skillDTOS = skillDTOS;
  }
}
