package com.revature.project.factory.service;

import static com.revature.project.factory.constants.TestConstants.DATA_ACCESS_FAILED;
import static com.revature.project.factory.constants.TestConstants.DATA_RETRIEVAL_FAILED;
import static com.revature.project.factory.constants.TestConstants.ENCRYPTED_TOKEN;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

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
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.model.Category;
import com.revature.project.factory.model.Project;
import com.revature.project.factory.service.admin.impl.ProjectServiceImpl;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.MetadataRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectDeletionFailedException;
import com.revature.project.factory.service.exception.ProjectInsertionFailedException;
import com.revature.project.factory.service.exception.ProjectRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectUpdationFailedException;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*", "javax.net.ssl.*",
    "jdk.internal.reflect.*"})
public class ProjectServiceImplTest {
  @InjectMocks
  private ProjectServiceImpl projectServiceImpl;
  @Mock
  private ProjectDAO projectDAO;

  @Mock
  private Provider<UserRequest> requestScopedBeanStub;

  private UserRequest userRequest;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userRequest = new UserRequest();
    userRequest.setEmpId(1L);
    userRequest.setEncryptedToken(ENCRYPTED_TOKEN);
    when(requestScopedBeanStub.get()).thenReturn(userRequest);
  }

  @Test
  public void when_doGetAllInternships_Expect_Success()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    when(projectDAO.doGetAllProjects(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
        Mockito.anyString(), Mockito.anyBoolean())).thenReturn(getProjectDTOListForPagination());
    List<ProjectDTO> projectDTOList = projectServiceImpl.doGetAllProjects(1, 1, "id", "ASC", true);
    Assert.assertNotNull(projectDTOList);
  }

  @Test(expected = BusinessServiceException.class)
  public void when_doGetAllInternships_Expect_Failure()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    Mockito.doThrow(new DataRetrievalFailedException("Retieval failed")).when(projectDAO)
        .doGetAllProjects(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
            Mockito.anyString(), Mockito.anyBoolean());
    projectServiceImpl.doGetAllProjects(1, 1, "id", "ASC", true);
  }

  @Test
  public void when_doGetAllProjects_Expect_Success_withOrderBySortOrder()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    when(projectDAO.doGetAllProjects(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
        Mockito.anyString(), Mockito.anyBoolean())).thenReturn(getProjectDTOListForPagination());
    List<ProjectDTO> projectDTOList =
        projectServiceImpl.doGetAllProjects(null, null, "id", "ASC", false);
    Assert.assertNotNull(projectDTOList);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetAllInternships_Expect_HttpClientExceptionWithNullPageNO()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    projectServiceImpl.doGetAllProjects(null, 1, "id", "ASC", true);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetAllInternships_Expect_HttpClientExceptionWithNullSizeNO()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    projectServiceImpl.doGetAllProjects(1, null, "id", "ASC", true);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetAllInternships_Expect_HttpClientExceptionWithNullOrderBy()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    projectServiceImpl.doGetAllProjects(1, 1, null, "ASC", true);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetAllInternships_Expect_HttpClientExceptionWithNullSortOrder()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    projectServiceImpl.doGetAllProjects(1, 1, "id", null, true);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetAllInternships_Expect_HttpClientExceptionWithInvalidOderBy()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    projectServiceImpl.doGetAllProjects(1, 1, "status", "ASC", true);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetAllInternships_Expect_HttpClientExceptionWithInvalidSortOder()
      throws DataRetrievalFailedException, BusinessServiceException, BusinessConflictException {
    projectServiceImpl.doGetAllProjects(1, 1, "id", "ASCENDING", true);
  }

  @Test
  public void when_doSaveProject_Expect_Success() throws DataRetrievalFailedException,
      BusinessConflictException, ProjectInsertionFailedException {
    Mockito.when(projectDAO.checkUniqueProjectName(Mockito.anyString())).thenReturn(false);
    projectServiceImpl.doSaveProject(getProjectDTO());
    Assert.assertNotNull(getProjectDTO());
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doSaveProject_Expect_HttpClientException() throws DataRetrievalFailedException,
      BusinessConflictException, ProjectInsertionFailedException {
    Mockito.when(projectDAO.checkUniqueProjectName(Mockito.anyString())).thenReturn(true);
    projectServiceImpl.doSaveProject(getProjectDTO());
  }

  @Test(expected = ProjectInsertionFailedException.class)
  public void when_doSaveProject_Expect_Exception() throws DataRetrievalFailedException,
      BusinessConflictException, ProjectInsertionFailedException, DataInsertionFailedException {
    Mockito.when(projectDAO.checkUniqueProjectName(Mockito.anyString())).thenReturn(false);
    Mockito.doThrow(DataInsertionFailedException.class).when(projectDAO)
        .saveObjects(Mockito.anyList());
    projectServiceImpl.doSaveProject(getProjectDTO());
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doSaveProject_Expect_DuplicateException() throws DataRetrievalFailedException,
      BusinessConflictException, ProjectInsertionFailedException {
    ProjectDTO projectDTO = getProjectDTO();
    projectDTO.getProjectSkills().add(getProjectSkillDTO());
    Mockito.when(projectDAO.checkUniqueProjectName(Mockito.anyString())).thenReturn(false);
    projectServiceImpl.doSaveProject(projectDTO);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doSaveProject_Expect_Exception_WithZeroSkills()
      throws DataRetrievalFailedException, BusinessConflictException,
      ProjectInsertionFailedException {
    ProjectDTO projectDTO = getProjectDTO();
    projectDTO.setProjectSkills(null);
    Mockito.when(projectDAO.checkUniqueProjectName(Mockito.anyString())).thenReturn(false);
    projectServiceImpl.doSaveProject(projectDTO);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doSaveProject_Expect_Exception_WithSkillNegativeScore()
      throws DataRetrievalFailedException, BusinessConflictException,
      ProjectInsertionFailedException {
    ProjectDTO projectDTO = getProjectDTO();
    projectDTO.getProjectSkills().get(0).setScore(-1);
    Mockito.when(projectDAO.checkUniqueProjectName(Mockito.anyString())).thenReturn(false);
    projectServiceImpl.doSaveProject(projectDTO);
  }

  @Test
  public void when_doDeleteProject_Expect_Success() throws DataDeletionFailedException,
      BusinessConflictException, ProjectDeletionFailedException, DataRetrievalFailedException {
    Mockito.doNothing().when(projectDAO).doDeleteProject(Mockito.anyLong());
    when(projectDAO.getProjectById(Mockito.anyLong())).thenReturn(getProject());
    projectServiceImpl.doDeleteProject(1L);
    ArgumentCaptor<Long> projectCapture = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(projectDAO).doDeleteProject(projectCapture.capture());
    Assert.assertNotNull(projectCapture);
  }

  @Test(expected = ProjectDeletionFailedException.class)
  public void when_doDeleteProject_Expect_Failure() throws DataDeletionFailedException,
      BusinessConflictException, ProjectDeletionFailedException, DataRetrievalFailedException {
    Mockito.doThrow(new DataDeletionFailedException(DATA_ACCESS_FAILED, new Exception()))
        .when(projectDAO).doDeleteProject(Mockito.anyLong());
    when(projectDAO.getProjectById(Mockito.anyLong())).thenReturn(getProject());
    projectServiceImpl.doDeleteProject(1L);
  }

  @Test(expected = ProjectDeletionFailedException.class)
  public void when_doDeleteProject_Expect_Exception() throws DataDeletionFailedException,
      BusinessConflictException, ProjectDeletionFailedException, DataRetrievalFailedException {
    Mockito.doThrow(new DataDeletionFailedException(DATA_RETRIEVAL_FAILED, new Exception()))
        .when(projectDAO).doDeleteProject(Mockito.anyLong());
    when(projectDAO.getProjectById(Mockito.anyLong())).thenReturn(getProject());
    projectServiceImpl.doDeleteProject(1L);
  }

  @Test
  public void when_doActivateOrDeactivateProject_Expect_Sucess() throws DataUpdationFailedException,
      BusinessConflictException, ProjectUpdationFailedException, DataRetrievalFailedException {
    Mockito.doNothing().when(projectDAO).activateOrDeactivateProject(Mockito.anyLong(),
        Mockito.anyBoolean(), Mockito.anyLong());
    Mockito.when(projectDAO.getProjectById(anyLong())).thenReturn(getProject());
    projectServiceImpl.doActivateOrDeactivateProject(1L, false);
    ArgumentCaptor<Long> projectCapture = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(projectDAO).activateOrDeactivateProject(Mockito.anyLong(), Mockito.anyBoolean(),
        Mockito.anyLong());
    Assert.assertNotNull(projectCapture);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doActivateOrDeactivateProject_Expect_ConflictException()
      throws ProjectUpdationFailedException, BusinessConflictException,
      DataRetrievalFailedException {
    Project project = getProject();
    project.setIsActive(true);
    Mockito.when(projectDAO.getProjectById(anyLong())).thenReturn(project);
    projectServiceImpl.doActivateOrDeactivateProject(1L, true);
  }

  @Test(expected = ProjectUpdationFailedException.class)
  public void when_doActivateOrDeactivateProject_Expect_Failure()
      throws ProjectUpdationFailedException, BusinessConflictException, DataUpdationFailedException,
      DataRetrievalFailedException {
    doThrow(new DataUpdationFailedException(DATA_RETRIEVAL_FAILED)).when(projectDAO)
        .activateOrDeactivateProject(Mockito.anyLong(), Mockito.anyBoolean(), Mockito.anyLong());
    Mockito.when(projectDAO.getProjectById(anyLong())).thenReturn(getProject());
    projectServiceImpl.doActivateOrDeactivateProject(1L, false);
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doActivateOrDeactivateProject_Expect_FailureWithNullId()
      throws ProjectUpdationFailedException, BusinessConflictException {
    projectServiceImpl.doActivateOrDeactivateProject(null, false);
  }

  @Test
  public void when_doGetProjectById_Expect_Success() throws BusinessConflictException,
      DataRetrievalFailedException, ProjectRetrievalFailedException {
    when(projectDAO.getProjectById(1L)).thenReturn(getProject());
    ProjectDTO project = projectServiceImpl.doGetProjectById(1L);
    Assert.assertEquals(getProject().getId(), project.getId());
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doGetProjectById_Expect_HttpClientException() throws BusinessConflictException,
      ProjectRetrievalFailedException, DataRetrievalFailedException {
    when(projectDAO.getProjectById(1L)).thenReturn(null);
    projectServiceImpl.doGetProjectById(1L);
  }

  @Test(expected = ProjectRetrievalFailedException.class)
  public void when_doGetProjectById_Expect_Failure() throws ProjectRetrievalFailedException,
      BusinessConflictException, DataRetrievalFailedException {
    doThrow(new DataRetrievalFailedException(DATA_RETRIEVAL_FAILED)).when(projectDAO)
        .getProjectById(1L);
    projectServiceImpl.doGetProjectById(1L);
  }

  @Test
  public void when_doGetMetadata_Expect_Success()
      throws DataRetrievalFailedException, MetadataRetrievalFailedException {
    when(projectDAO.doGetSkills(true)).thenReturn(Collections.singletonList(getSkillDTO()));
    when(projectDAO.doGetCategories(true)).thenReturn(Collections.singletonList(getCategoryDTO()));
    MetadataDTO metadataDTO = projectServiceImpl.getMetadata(true);
    Assert.assertNotNull(metadataDTO);
  }

  @Test(expected = MetadataRetrievalFailedException.class)
  public void when_doGetMetadata_Expect_Failure()
      throws DataRetrievalFailedException, MetadataRetrievalFailedException {
    doThrow(new DataRetrievalFailedException(DATA_RETRIEVAL_FAILED)).when(projectDAO)
        .doGetCategories(Mockito.anyBoolean());
    projectServiceImpl.getMetadata(false);
  }

  private List<ProjectDTO> getProjectDTOListForPagination() {
    List<ProjectDTO> projectDTOS = new ArrayList<>();
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setId(1);
    projectDTO.setIsActive(true);
    projectDTO.setEmployeeName("user name");
    projectDTO.setName("Project-1");
    projectDTO.setCategoryName("Java Category");
    projectDTOS.add(projectDTO);
    projectDTO = new ProjectDTO();
    projectDTO.setId(2);
    projectDTO.setIsActive(true);
    projectDTO.setEmployeeName("user name");
    projectDTO.setName("Project-2");
    projectDTO.setCategoryName("Java Category");
    projectDTOS.add(projectDTO);
    return projectDTOS;
  }

  private ProjectDTO getProjectDTO() {
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setId(1);
    projectDTO.setName("Project Name");
    projectDTO.setDescription("Project Description");
    projectDTO.setIsActive(true);
    projectDTO.setCategory(getCategoryDTO());
    projectDTO.setCreatedBy(getSystemUserDTO());
    projectDTO.setCreatedOn(LocalDateTime.now().minusDays(3));
    projectDTO.setModifiedBy(getSystemUserDTO());
    projectDTO.setModifiedOn(LocalDateTime.now());
    projectDTO.setProjectSkills(getProjectSkillDTOList());
    return projectDTO;
  }

  private SystemUserDTO getSystemUserDTO() {
    SystemUserDTO systemUserDTO = new SystemUserDTO();
    systemUserDTO.setId(2L);
    systemUserDTO.setEmailId("user@yopmail.com");
    systemUserDTO.setFirstName("user");
    systemUserDTO.setLastName("name");
    systemUserDTO.setIsActive(true);
    systemUserDTO.setCreatedBy(1L);
    systemUserDTO.setCreatedOn(LocalDateTime.now());
    return systemUserDTO;
  }

  private CategoryDTO getCategoryDTO() {
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(1);
    categoryDTO.setName("category");
    categoryDTO.setIsActive(true);
    categoryDTO.setCreatedBy(getSystemUserDTO());
    categoryDTO.setCreatedOn(LocalDateTime.now());
    return categoryDTO;
  }

  private List<ProjectSkillDTO> getProjectSkillDTOList() {
    List<ProjectSkillDTO> projectSkillDTOS = new ArrayList<>();
    projectSkillDTOS.add(getProjectSkillDTO());
    return projectSkillDTOS;
  }

  private ProjectSkillDTO getProjectSkillDTO() {
    ProjectSkillDTO projectSkillDTO = new ProjectSkillDTO();
    projectSkillDTO.setId(1);
    projectSkillDTO.setScore(70);
    projectSkillDTO.setSkill(getSkillDTO());
    return projectSkillDTO;
  }

  private SkillDTO getSkillDTO() {
    SkillDTO skillDTO = new SkillDTO();
    skillDTO.setId(1);
    skillDTO.setName("skill");
    skillDTO.setScore(80);
    skillDTO.setIsActive(true);
    skillDTO.setCreatedBy(getSystemUserDTO());
    skillDTO.setCreatedOn(LocalDateTime.now().minusWeeks(1));
    return skillDTO;
  }

  private Project getProject() {
    Project project = new Project();
    project.setName("project-1");
    project.setDescription("description");
    project.setIsActive(null);
    Category category = new Category();
    category.setId(1);
    category.setName("category");
    category.setIsActive(null);
    project.setCategory(category);
    return project;
  }
}
