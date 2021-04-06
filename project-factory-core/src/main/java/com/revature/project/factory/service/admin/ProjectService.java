package com.revature.project.factory.service.admin;

import java.util.List;

import com.revature.project.factory.dto.MetadataDTO;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.exception.HttpClientException;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.MetadataRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectDeletionFailedException;
import com.revature.project.factory.service.exception.ProjectInsertionFailedException;
import com.revature.project.factory.service.exception.ProjectRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectUpdationFailedException;


public interface ProjectService {

  /**
   * To get available projects with/without pagination
   *
   * @param pageNo - No of the page by which projects has to be fetched
   * @param sizeNo - Size of the page data by which projects has to be fetched
   * @param orderBy - Order by of the column name by which projects has to be fetched
   * @param sortOrder - Sort order of the column name by which projects has to be fetched
   * @param status - Status of the project by which projects has to be fetched
   * @return List of {@link ProjectDTO}
   * @throws BusinessServiceException if business logic fail {@link BusinessServiceException}
   * @throws BusinessConflictException if Field validation fail {@link BusinessConflictException}
   */
  List<ProjectDTO> doGetAllProjects(Integer pageNo, Integer sizeNo, String orderBy,
      String sortOrder, Boolean status) throws BusinessServiceException, BusinessConflictException;

  /**
   * To save project details
   *
   * @param projectDTO - used to save project details
   * @throws BusinessServiceException if business logic fail {@link BusinessServiceException}
   * @throws HttpClientException if business validation fail {@link HttpClientException}
   */
  void doSaveProject(ProjectDTO projectDTO)
      throws ProjectInsertionFailedException, BusinessConflictException;

  /**
   * delete project by project id
   *
   * @param projectId - Id of the project by which project has to be deleted
   * @throws ProjectDeletionFailedException if business logic fail
   *         {@link ProjectDeletionFailedException}
   * @throws BusinessConflictException if business validation fail {@link BusinessConflictException}
   */
  void doDeleteProject(Long projectId)
      throws ProjectDeletionFailedException, BusinessConflictException;

  /**
   *
   * activate or deactivate the Project by project id
   *
   * @param projectId - Id of the project which has to be activated or deactivated
   * @param status - status of the project which has to be updated
   * @throws ProjectUpdationFailedException if data layer logic fail
   *         {@link ProjectUpdationFailedException}
   * @throws BusinessConflictException if business validation fail {@link BusinessConflictException}
   */
  void doActivateOrDeactivateProject(Long projectId, Boolean status)
      throws ProjectUpdationFailedException, BusinessConflictException;

  /**
   *
   * Get project by project id
   *
   * @param projectId - Id of the project for which project details has to be fetched
   * @return {@link Object} of{@link ProjectDTO}
   * @throws ProjectRetrievalFailedException if data layer logic fail
   *         {@link ProjectRetrievalFailedException}
   * @throws BusinessConflictException if business validation fail {@link BusinessConflictException}
   */
  ProjectDTO doGetProjectById(Long projectId)
      throws ProjectRetrievalFailedException, BusinessConflictException;

  /**
   * To get metadata for project
   *
   * @param status
   * @return Object of {@link MetadataDTO}
   * @throws MetadataRetrievalFailedException
   */
  MetadataDTO getMetadata(Boolean status) throws MetadataRetrievalFailedException;
}
