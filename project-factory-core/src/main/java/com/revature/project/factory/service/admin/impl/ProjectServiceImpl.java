package com.revature.project.factory.service.admin.impl;

import static com.revature.project.factory.service.constant.BusinessConstants.ASC;
import static com.revature.project.factory.service.constant.BusinessConstants.DESC;
import static com.revature.project.factory.service.constant.BusinessConstants.ID;
import static com.revature.project.factory.service.constant.BusinessConstants.NAME;
import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Provider;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.project.factory.controller.config.UserRequest;
import com.revature.project.factory.dao.ProjectDAO;
import com.revature.project.factory.dao.exception.DataDeletionFailedException;
import com.revature.project.factory.dao.exception.DataInsertionFailedException;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dto.CategoryDTO;
import com.revature.project.factory.dto.MetadataDTO;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.dto.ProjectSkillDTO;
import com.revature.project.factory.dto.SkillDTO;
import com.revature.project.factory.model.Category;
import com.revature.project.factory.model.Employee;
import com.revature.project.factory.model.Project;
import com.revature.project.factory.model.ProjectSkill;
import com.revature.project.factory.model.Skill;
import com.revature.project.factory.service.admin.ProjectService;
import com.revature.project.factory.service.constant.BusinessConstants;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.MetadataRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectDeletionFailedException;
import com.revature.project.factory.service.exception.ProjectInsertionFailedException;
import com.revature.project.factory.service.exception.ProjectRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectUpdationFailedException;
import com.revature.project.factory.util.CalendarUtils;

@Service
public class ProjectServiceImpl implements ProjectService {
  private static final Logger LOGGER = LogManager.getLogger(ProjectServiceImpl.class);

  private Provider<UserRequest> requestScopedBean;
  private ProjectDAO projectDAO;

  @Autowired
  public ProjectServiceImpl(Provider<UserRequest> requestScopedBean, ProjectDAO projectDAO) {
    this.requestScopedBean = requestScopedBean;
    this.projectDAO = projectDAO;
  }

  @Override
  public List<ProjectDTO> doGetAllProjects(Integer pageNo, Integer sizeNo, String orderBy,
      String sortOrder, Boolean status) throws BusinessConflictException, BusinessServiceException {
    List<ProjectDTO> projectDTOS = null;
    try {
      // Validate filter option
      validateFilterOption(pageNo, sizeNo, orderBy, sortOrder);
      projectDTOS = projectDAO.doGetAllProjects(pageNo, sizeNo, orderBy, sortOrder, status);
    } catch (DataRetrievalFailedException e) {
      throw new BusinessServiceException(e.getMessage(), e);
    }
    return projectDTOS;
  }

  @Override
  public void doSaveProject(ProjectDTO projectDTO)
      throws ProjectInsertionFailedException, BusinessConflictException {
    try {
      List<Object> saveObjects = new ArrayList<>();
      boolean isNameExists = validateProjectDetails(projectDTO);
      if (isNameExists) {
        throw new BusinessConflictException(
            "That name is already used by another project. Please try another name", null);
      }
      Project project = createProject(projectDTO);
      saveObjects.add(project);
      for (ProjectSkillDTO projectSkillDTO : projectDTO.getProjectSkills()) {
        saveObjects.add(buildProjectSkill(projectSkillDTO, project));
      }
      projectDAO.saveObjects(saveObjects);
    } catch (DataRetrievalFailedException | DataInsertionFailedException e) {
      throw new ProjectInsertionFailedException(e.getMessage(), e);
    }
  }

  private Project createProject(ProjectDTO projectDTO) {
    Project project = new Project();
    Employee employee = new Employee(requestScopedBean.get().getEmpId());
    project.setCreatedBy(employee);
    project.setCreatedOn(CalendarUtils.getNowInUTC());
    project.setIsActive(true);
    project.setName(projectDTO.getName());
    project.setDescription(projectDTO.getDescription());
    project.setDescription(projectDTO.getDescription());
    Category category = new Category();
    category.setId(projectDTO.getCategory().getId());
    project.setCategory(category);
    return project;
  }

  private ProjectSkill buildProjectSkill(ProjectSkillDTO projectSkillDTO, Project project) {
    ProjectSkill projectSkill = new ProjectSkill();
    projectSkill.setId(projectSkillDTO.getId());
    projectSkill.setProject(project);
    projectSkill.setScore(projectSkillDTO.getScore());
    Skill skill = new Skill(projectSkillDTO.getSkill().getId());
    projectSkill.setSkill(skill);
    return projectSkill;
  }

  @Override
  public void doDeleteProject(Long id)
      throws ProjectDeletionFailedException, BusinessConflictException {
    try {
      validateProjectId(id);
      doGetProjectById(id);
      projectDAO.doDeleteProject(id);
    } catch (DataDeletionFailedException | ProjectRetrievalFailedException e) {
      throw new ProjectDeletionFailedException(e.getMessage(), e);
    }
  }

  @Override
  public void doActivateOrDeactivateProject(Long id, Boolean status)
      throws ProjectUpdationFailedException, BusinessConflictException {
    try {
      validateProjectId(id);
      ProjectDTO projectDTO = doGetProjectById(id);
      if (Objects.nonNull(projectDTO)) {
        if (Objects.equals(projectDTO.getIsActive(), status)) {
          boolean sts = status;
          throw new BusinessConflictException(
              "Project is already " + (sts ? "activated" : "deactivated"), null);
        }
        projectDAO.activateOrDeactivateProject(id, status, requestScopedBean.get().getEmpId());
      }
    } catch (DataUpdationFailedException | ProjectRetrievalFailedException e) {
      throw new ProjectUpdationFailedException(e.getMessage(), e);
    }
  }

  @Override
  public ProjectDTO doGetProjectById(Long id)
      throws ProjectRetrievalFailedException, BusinessConflictException {
    try {
      Project project = projectDAO.getProjectById(id);
      if (Objects.isNull(project)) {
        throw new BusinessConflictException(
            String.format(BusinessConstants.PROJECT_NOT_FOUND_FOR_THE_GIVEN_ID, id), null);
      }
      ModelMapper modelMapper = new ModelMapper();
      modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
      return modelMapper.map(project, ProjectDTO.class);
    } catch (DataRetrievalFailedException e) {
      throw new ProjectRetrievalFailedException(e.getMessage(), e);
    }
  }

  @Override
  public MetadataDTO getMetadata(Boolean status) throws MetadataRetrievalFailedException {
    MetadataDTO metadataDTO = new MetadataDTO();
    try {
      List<CategoryDTO> categories = projectDAO.doGetCategories(status);
      metadataDTO.setCategoryDTOS(categories);
      List<SkillDTO> skills = projectDAO.doGetSkills(status);
      metadataDTO.setSkillDTOS(skills);
      return metadataDTO;
    } catch (DataRetrievalFailedException e) {
      throw new MetadataRetrievalFailedException(e.getMessage(), e);
    }
  }

  private void validateProjectId(Long id) throws BusinessConflictException {
    if (Objects.isNull(id) || id <= 0) {
      throw new BusinessConflictException(BusinessConstants.INVALID_PROJECT_ID, null);
    }
  }

  private boolean validateProjectDetails(ProjectDTO projectDTO)
      throws DataRetrievalFailedException, BusinessConflictException {
    Boolean nameExistance = projectDAO.checkUniqueProjectName(projectDTO.getName());
    if (BooleanUtils.isTrue(nameExistance)) {
      return true;
    }
    if (CollectionUtils.isEmpty(projectDTO.getProjectSkills())) {
      throw new BusinessConflictException("Aleast one skill is required", null);
    }
    Map<SkillDTO, List<ProjectSkillDTO>> duplicateSkills = projectDTO.getProjectSkills().stream()
        .collect(Collectors.groupingBy(ProjectSkillDTO::getSkill));
    if (duplicateSkills.values().stream().anyMatch(duplicates -> duplicates.size() > 1)) {
      throw new BusinessConflictException("Duplicate skills cannot be added", null);
    }
    if (projectDTO.getProjectSkills().stream().filter(skill -> Objects.nonNull(skill.getScore()))
        .anyMatch(skill -> skill.getScore() <= 0)) {
      throw new BusinessConflictException("Skill score cannot be negative", null);
    }
    return false;
  }

  private void validateFilterOption(Integer pageNo, Integer sizeNo, String orderBy,
      String sortOrder) throws BusinessConflictException {
    if (Objects.nonNull(pageNo) && isNull(sizeNo)) {
      throw new BusinessConflictException(BusinessConstants.INVALID_FILTER_OPTIONS, null);
    }
    if (Objects.nonNull(sizeNo) && isNull(pageNo)) {
      throw new BusinessConflictException(BusinessConstants.INVALID_FILTER_OPTIONS, null);
    }
    List<String> columns = Arrays.asList(ID, NAME);
    if (StringUtils.isNotBlank(orderBy)
        && columns.stream().noneMatch(column -> column.equalsIgnoreCase(orderBy))) {
      throw new BusinessConflictException(BusinessConstants.INVALID_FILTER_OPTIONS, null);
    }
    if (StringUtils.isNotBlank(sortOrder)
        && !(sortOrder.equalsIgnoreCase(ASC) || sortOrder.equalsIgnoreCase(DESC))) {
      throw new BusinessConflictException(BusinessConstants.INVALID_FILTER_OPTIONS, null);
    }
    if (StringUtils.isNotBlank(sortOrder) && StringUtils.isBlank(orderBy)) {
      throw new BusinessConflictException(BusinessConstants.INVALID_FILTER_OPTIONS, null);
    }
    if (StringUtils.isNotBlank(orderBy) && StringUtils.isBlank(sortOrder)) {
      throw new BusinessConflictException(BusinessConstants.INVALID_FILTER_OPTIONS, null);
    }
  }
}
