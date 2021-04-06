package com.revature.project.factory.dao;

import java.util.List;

import com.revature.project.factory.dao.exception.DataDeletionFailedException;
import com.revature.project.factory.dao.exception.DataInsertionFailedException;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dto.CategoryDTO;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.dto.SkillDTO;
import com.revature.project.factory.model.Project;

public interface ProjectDAO {

  /**
   *
   * Get project by project id
   *
   * @param projectId - Id of the project for which project details has to be fetched
   * @return {@link Object} of{@link Project}
   * @throws DataRetrievalFailedException if data layer logic fail
   *         {@link DataRetrievalFailedException}
   */
  Project getProjectById(Long projectId) throws DataRetrievalFailedException;

  /**
   *
   * Get all projects with pagination
   *
   * @param pageNo - No of the page by which project has to be fetched
   * @param sizeNo - Size of the page data by which project has to be fetched
   * @param orderBy - Order by of the column name by which project has to be fetched
   * @param sortOrder - Sort order of the column name by which projects has to be fetched
   * @param status - Status of the project by which projects has to be fetched
   * @return List of {@link ProjectDTO}
   * @throws DataServiceException if data layer logic fail {@link DataRetrievalFailedException}
   */
  List<ProjectDTO> doGetAllProjects(Integer pageNo, Integer sizeNo, String orderBy,
      String sortOrder, Boolean status) throws DataRetrievalFailedException;

  /**
   * To save a project details
   *
   * @param saveList
   * @throws DataServiceException if data layer logic fail {@link DataInsertionFailedException}
   */
  void saveObjects(List<Object> saveList) throws DataInsertionFailedException;

  /**
   *
   * Activate or deactivate the Project by project id
   *
   * @param projectId - Id of the project which has to be activated or deactivated
   * @param status - status of the project which has to be updated
   * @param employeeId - id of the employee who activated or deactivated the project
   * @throws DataUpdationFailedException if data layer logic fail
   *         {@link DataUpdationFailedException}
   */
  void activateOrDeactivateProject(Long projectId, Boolean status, Long employeeId)
      throws DataUpdationFailedException;

  /**
   *
   * To check project name exists by project name
   *
   * @param projectName - name of the project to verify the existence
   * @return Boolean - if project exists then true else false
   * @throws DataServiceException if data layer logic fail {@link DataRetrievalFailedException}
   */
  Boolean checkUniqueProjectName(String projectName) throws DataRetrievalFailedException;

  /**
   *
   * Delete the Project by project id
   *
   * @param projectId - Id of the project which has to be deleted
   * @throws DataDeletionFailedException if data layer logic fail
   *         {@link DataDeletionFailedException}
   */
  void doDeleteProject(Long projectId) throws DataDeletionFailedException;

  /**
   * Get categories
   *
   * @param status
   * @return List of {@link CategoryDTO}
   * @throws DataRetrievalFailedException
   */
  List<CategoryDTO> doGetCategories(Boolean status) throws DataRetrievalFailedException;

  /**
   * Get skills
   *
   * @param status
   * @return List of {@link SkillDTO}
   * @throws DataRetrievalFailedException
   */
  List<SkillDTO> doGetSkills(Boolean status) throws DataRetrievalFailedException;
}
