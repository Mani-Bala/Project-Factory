package com.revature.project.factory.dao;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import com.revature.project.factory.dao.exception.DataDeletionFailedException;
import com.revature.project.factory.dao.exception.DataInsertionFailedException;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dao.impl.ProjectDAOImpl;
import com.revature.project.factory.data.access.DataModifier;
import com.revature.project.factory.data.access.DataRetriever;
import com.revature.project.factory.data.access.exception.DataAccessException;
import com.revature.project.factory.data.access.exception.ReferentialIntegrityException;
import com.revature.project.factory.dto.CategoryDTO;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.dto.SkillDTO;
import com.revature.project.factory.model.Project;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*"})
public class ProjectDAOImplTest {

  private static final String DATA_FETCH_FAILED = "Data fetch failed.";
  private static final String PROJECT_NAME = "Sample Project";

  @InjectMocks
  private ProjectDAOImpl projectDAO;

  @Mock
  private DataRetriever dataRetriever;

  @Mock
  private DataModifier dataModifier;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * This method is used to test the get project by id. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetProjectByIdGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveObjectByHQL(Mockito.anyString(), Mockito.anyList()))
        .thenReturn(getProject());
    Project project = projectDAO.getProjectById(1L);
    Assert.assertNotNull(project);
  }

  /**
   * This method is used to test the get project by id. expect failure
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenDoGetProjectByIdGetFailure()
      throws DataRetrievalFailedException, DataAccessException {

    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataRetriever)
        .retrieveObjectByHQL(Mockito.anyString(), Mockito.anyList());
    projectDAO.getProjectById(1L);
  }

  /**
   * This method is used to test the get projects. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetAllProjectsGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any()))
        .thenReturn(Collections.singletonList(getProjectDTOList()));
    Mockito
        .when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(), Mockito.anyList(),
            Mockito.any(), Mockito.any()))
        .thenReturn(Collections.singletonList(getProjectDTOList()));
    List<ProjectDTO> response = projectDAO.doGetAllProjects(1, 2, "id", "DESC", true);
    Assert.assertEquals(response.size(), getProjectDTOList().size());
  }

  /**
   * This method is used to test the get projects. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetAllProjectsWithOrderBYSortOrderGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any())).thenReturn(Collections.singletonList(getProjectDTO()));
    List<ProjectDTO> response = projectDAO.doGetAllProjects(null, null, "id", "ASC", false);
    Assert.assertEquals(response.size(), getProjectDTOList().size());
  }

  /**
   * This method is used to test the get projects. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetAllProjectsWithPageNoSizeNoGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito
        .when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(), Mockito.anyList(),
            Mockito.any(), Mockito.any()))
        .thenReturn(Collections.singletonList(getProjectDTOList()));
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any()))
        .thenReturn(Collections.singletonList(getProjectDTOList()));
    List<ProjectDTO> response = projectDAO.doGetAllProjects(1, 1, null, null, false);
    Assert.assertEquals(response.size(), getProjectDTOList().size());
  }

  /**
   * This method is used to test the get projects. expect failure
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenDoGetAllProjectsGetFailure()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataRetriever)
        .retrieveBySQLResultTransformer(Mockito.anyString(), Mockito.anyList(), Mockito.any());
    projectDAO.doGetAllProjects(null, null, "id", "ASC", false);
  }

  /**
   * This method is used to test the activate or deactivate project. expect success
   *
   * @throws DataUpdationFailedException if DAO service fail.
   */
  @Test
  public void whenDoActivateOrDeactivateProjectGetSuccess()
      throws DataUpdationFailedException, DataAccessException {
    Mockito.doNothing().when(dataModifier).executeSQLQuery(Mockito.anyString(), Mockito.anyList());
    projectDAO.activateOrDeactivateProject(1L, true, 1L);
    ArgumentCaptor<String> projectCapture = ArgumentCaptor.forClass(String.class);
    Mockito.verify(dataModifier).executeSQLQuery(Mockito.anyString(), Mockito.anyList());
    Assert.assertNotNull(projectCapture);
  }

  /**
   * This method is used to test the activate or deactivate project. expect failure
   *
   * @throws DataUpdationFailedException if DAO service fail.
   */
  @Test(expected = DataUpdationFailedException.class)
  public void whenDoActivateOrDeactivateProjectGetFailure()
      throws DataUpdationFailedException, DataAccessException {
    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataModifier)
        .executeSQLQuery(Mockito.anyString(), Mockito.anyList());
    projectDAO.activateOrDeactivateProject(1L, true, 1L);
  }

  /**
   * This method is used to test the get skills method,expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenGetSkillsSuccess() throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any())).thenReturn(Collections.singletonList(getSkillsDTO()));
    List<SkillDTO> skills = projectDAO.doGetSkills(true);
    Assert.assertNotNull(skills);
  }

  /**
   * This method is used to test the get skills method,expect DataServiceException
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenGetSkillsFailed() throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any())).thenThrow(DataAccessException.class);
    projectDAO.doGetSkills(true);
  }

  /**
   * This method is used to test the get in-active categories. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetAllCategoriesGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    List<CategoryDTO> categories = new ArrayList<>();
    categories.add(getCategoryDTO());
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any())).thenReturn(Collections.singletonList(getCategoryDTO()));
    List<CategoryDTO> response = projectDAO.doGetCategories(true);
    Assert.assertEquals(response.size(), categories.size());
  }

  /**
   * This method is used to test the get in-active categories. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetAllCategoriesGetSuccessWithOutStatus()
      throws DataRetrievalFailedException, DataAccessException {
    List<CategoryDTO> categories = new ArrayList<>();
    categories.add(getCategoryDTO());
    Mockito.when(dataRetriever.retrieveBySQLResultTransformer(Mockito.anyString(),
        Mockito.anyList(), Mockito.any())).thenReturn(Collections.singletonList(getCategoryDTO()));
    List<CategoryDTO> response = projectDAO.doGetCategories(null);
    Assert.assertEquals(response.size(), categories.size());
  }

  /**
   * This method is used to test the get in-active categories. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenDoGetAllCategories_Expect_Failure()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataRetriever)
        .retrieveBySQLResultTransformer(Mockito.anyString(), Mockito.anyList(), Mockito.any());
    projectDAO.doGetCategories(false);
  }

  /**
   * This method is used to test the get skills method,expect success
   *
   * @throws DataDeletionFailedException if DAO service fail.
   */
  @Test
  public void whenDoDeleteProjectSuccess() throws DataDeletionFailedException, DataAccessException {
    Mockito.doNothing().when(dataModifier).executeSQLQuery(Mockito.anyString(), Mockito.anyList());
    projectDAO.doDeleteProject(1L);
    Mockito.verify(dataModifier, Mockito.times(2)).executeSQLQuery(Mockito.anyString(),
        Mockito.anyList());
  }

  /**
   * This method is used to test the get skills method,expect DataServiceException
   *
   * @throws DataDeletionFailedException if DAO service fail.
   */
  @Test(expected = DataDeletionFailedException.class)
  public void whenDoDeleteProjectExpectDataServiceException()
      throws DataDeletionFailedException, DataAccessException {
    Mockito.doThrow(DataAccessException.class).when(dataModifier)
        .executeSQLQuery(Mockito.anyString(), Mockito.anyList());
    projectDAO.doDeleteProject(1L);
  }

  /**
   * This method is used to test the get skills method,expect ReferentialIntegrityException
   *
   * @throws DataDeletionFailedException if DAO service fail.
   */
  @Test(expected = DataDeletionFailedException.class)
  public void whenDoDeleteProjectExpectReferentialIntegrityException()
      throws DataDeletionFailedException, DataAccessException {
    Mockito.doThrow(ReferentialIntegrityException.class).when(dataModifier)
        .executeSQLQuery(Mockito.anyString(), Mockito.anyList());
    projectDAO.doDeleteProject(1L);
  }

  /**
   * This method is used to test the save project objects method,expect success
   *
   * @throws DataInsertionFailedException if DAO service fail.
   */
  @Test
  public void whenDoSaveObjectsSuccess() throws DataInsertionFailedException, DataAccessException {
    Mockito.when(dataModifier.saveOrUpdateBulk(Mockito.anyList())).thenReturn(true);
    projectDAO.saveObjects(Collections.singletonList(getProject()));
    Mockito.verify(dataModifier, Mockito.times(1)).saveOrUpdateBulk(Mockito.anyList());
  }

  /**
   * This method is used to test the save project objects method,expect DataServiceException
   *
   * @throws DataInsertionFailedException if DAO service fail.
   */
  @Test(expected = DataInsertionFailedException.class)
  public void whenDoSaveObjectsExpectDataServiceException()
      throws DataInsertionFailedException, DataAccessException {
    Mockito.when(dataModifier.saveOrUpdateBulk(Mockito.anyList()))
        .thenThrow(DataAccessException.class);
    projectDAO.saveObjects(Collections.singletonList(getProject()));
  }

  /**
   * This method is used to test the check unique project name method,expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoCheckUniqueProjectNameSuccessResponseTrue()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQL(Mockito.anyString(), Mockito.anyList()))
        .thenReturn(Collections.singletonList(1));
    Boolean isExist = projectDAO.checkUniqueProjectName("project name");
    Assert.assertTrue(isExist);
  }

  /**
   * This method is used to test the check unique project name method,expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoCheckUniqueProjectNameSuccessResponseFalse()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQL(Mockito.anyString(), Mockito.anyList()))
        .thenReturn(new ArrayList<>());
    Boolean isExist = projectDAO.checkUniqueProjectName("project name");
    Assert.assertFalse(isExist);
  }

  /**
   * This method is used to test the check unique project name,expect DataServiceException
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenDoCheckUniqueProjectNameExpectDataServiceException()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveBySQL(Mockito.anyString(), Mockito.anyList()))
        .thenThrow(DataAccessException.class);
    projectDAO.checkUniqueProjectName("project name");
  }

  private Project getProject() {
    Project project = new Project();
    project.setId(1L);
    project.setName(PROJECT_NAME);
    project.setIsActive(true);
    return project;
  }

  private ProjectDTO getProjectDTO() {
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setId(1L);
    projectDTO.setName(PROJECT_NAME);
    projectDTO.setIsActive(true);
    return projectDTO;
  }

  private List<ProjectDTO> getProjectDTOList() {
    List<ProjectDTO> projectDTOS = new ArrayList<>();
    projectDTOS.add(getProjectDTO());
    return projectDTOS;
  }

  private SkillDTO getSkillDTO() {
    SkillDTO skillDTO = new SkillDTO();
    skillDTO.setId(1L);
    skillDTO.setName("skill");
    skillDTO.setScore(1);
    return skillDTO;
  }

  private List<SkillDTO> getSkillsDTO() {
    List<SkillDTO> skillDTOList = new ArrayList<>();
    skillDTOList.add(getSkillDTO());
    return skillDTOList;
  }

  private CategoryDTO getCategoryDTO() {
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(1L);
    categoryDTO.setName("category");
    return categoryDTO;
  }

}
