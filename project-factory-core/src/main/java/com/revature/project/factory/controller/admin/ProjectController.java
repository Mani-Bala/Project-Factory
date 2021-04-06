package com.revature.project.factory.controller.admin;


import static com.revature.project.factory.controller.constant.ControllerConstants.DATA_DELETION_SUCCESS_MESSAGE;
import static com.revature.project.factory.controller.constant.ControllerConstants.DATA_RETRIEVAL_SUCCESS_MESSAGE;
import static com.revature.project.factory.controller.constant.ControllerConstants.NO_RECORDS_FOUND_ERROR_MESSAGE;
import static com.revature.project.factory.controller.constant.RestURIConstants.GET_PROJECTS;
import static com.revature.project.factory.controller.constant.RestURIConstants.METADATA;
import static com.revature.project.factory.controller.constant.RestURIConstants.PROJECT;
import static com.revature.project.factory.controller.constant.RestURIConstants.PROJECT_BY_ID;
import static com.revature.project.factory.util.ResponseUtils.prepareConflictResponse;
import static com.revature.project.factory.util.ResponseUtils.prepareInternalServerErrorResponse;
import static com.revature.project.factory.util.ResponseUtils.prepareNoRecordsFoundResponse;
import static com.revature.project.factory.util.ResponseUtils.prepareSuccessResponse;
import static java.util.Optional.ofNullable;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revature.project.factory.controller.config.UserRequest;
import com.revature.project.factory.controller.constant.ControllerConstants;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.service.admin.ProjectService;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.MetadataRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectDeletionFailedException;
import com.revature.project.factory.service.exception.ProjectInsertionFailedException;
import com.revature.project.factory.service.exception.ProjectRetrievalFailedException;
import com.revature.project.factory.service.exception.ProjectUpdationFailedException;
import com.revature.project.factory.service.responser.HttpStatusResponse;
import com.revature.project.factory.util.CommonUtils;

@RestController
public class ProjectController {

  private Provider<UserRequest> requestScopedBean;
  private ProjectService projectService;

  @Autowired
  public ProjectController(Provider<UserRequest> requestScopedBean, ProjectService projectService) {
    this.requestScopedBean = requestScopedBean;
    this.projectService = projectService;
  }

  @ModelAttribute
  private void setRequestValues(HttpServletRequest request) {
    CommonUtils.setRequestValues(request, requestScopedBean);
  }

  /**
   * To get all projects based on organization
   *
   * @param pageNo - No of the page by which projects has to be fetched
   * @param sizeNo - Size of the page data by which projects has to be fetched
   * @param orderBy - Order by of the column name by which projects has to be fetched
   * @param sortOrder - Sort order of the column name by which projects has to be fetched
   * @param status - Status of the project by which projects has to be fetched
   * @return {@link ResponseEntity} - which include status code, message and response data
   * @throws BusinessServiceException if business logic fails {@link BusinessServiceException}
   */
  @GetMapping(value = GET_PROJECTS, produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doGetProjects(
      @RequestParam(value = "page", required = false) Integer pageNo,
      @RequestParam(value = "size", required = false) Integer sizeNo,
      @RequestParam(value = "orderBy", required = false) String orderBy,
      @RequestParam(value = "sortOrder", required = false) String sortOrder,
      @RequestParam(value = "status", required = false) Boolean status) {
    try {
      return ofNullable(projectService.doGetAllProjects(pageNo, sizeNo, orderBy, sortOrder, status))
          .filter(CollectionUtils::isNotEmpty)
          .map(project -> prepareSuccessResponse(DATA_RETRIEVAL_SUCCESS_MESSAGE, project, null))
          .orElse(prepareNoRecordsFoundResponse(NO_RECORDS_FOUND_ERROR_MESSAGE));
    } catch (BusinessConflictException e) {
      return prepareConflictResponse(e.getMessage(), null);
    } catch (BusinessServiceException e) {
      return prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * To get a project by id
   *
   * @param projectId - Id of the project by which project details has to be fetched
   * @return {@link ResponseEntity} - which include status code, message and response data
   */
  @GetMapping(value = PROJECT_BY_ID, produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doGetProjectById(
      @PathVariable(name = "projectId") Long projectId) {
    try {
      return ofNullable(projectService.doGetProjectById(projectId))
          .map(project -> prepareSuccessResponse(DATA_RETRIEVAL_SUCCESS_MESSAGE, project, null))
          .orElse(prepareNoRecordsFoundResponse(NO_RECORDS_FOUND_ERROR_MESSAGE));
    } catch (BusinessConflictException e) {
      return prepareConflictResponse(e.getMessage(), null);
    } catch (ProjectRetrievalFailedException e) {
      return prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * To save project
   *
   * @return {@link ResponseEntity} - which include status code, message and response data
   */
  @PostMapping(value = PROJECT, produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doSaveProject(
      @Valid @RequestBody ProjectDTO projectDTO) {
    try {
      projectService.doSaveProject(projectDTO);
      return prepareSuccessResponse(ControllerConstants.DATA_INSERT_SUCCESS, null, null);
    } catch (BusinessConflictException e) {
      return prepareConflictResponse(e.getMessage(), null);
    } catch (ProjectInsertionFailedException e) {
      return prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * To delete a project
   *
   * @return {@link ResponseEntity} - which include status code, message and response data
   */
  @DeleteMapping(value = PROJECT_BY_ID, produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doDeleteProject(
      @PathVariable(name = "projectId") Long projectId) {
    try {
      projectService.doDeleteProject(projectId);
      return prepareSuccessResponse(DATA_DELETION_SUCCESS_MESSAGE, null, null);
    } catch (BusinessConflictException e) {
      return prepareConflictResponse(e.getMessage(), null);
    } catch (ProjectDeletionFailedException e) {
      return prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * To activate or deactivate a project
   *
   * @return {@link ResponseEntity} - which include status code, message and response data
   */
  @PatchMapping(value = PROJECT_BY_ID, produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doActivateOrDeactivateProject(
      @PathVariable(name = "projectId") Long projectId,
      @RequestParam(name = "status") Boolean status) {
    try {
      projectService.doActivateOrDeactivateProject(projectId, status);
      return prepareSuccessResponse(ControllerConstants.DATA_UPDATE_SUCCESS, null, null);
    } catch (BusinessConflictException e) {
      return prepareConflictResponse(e.getMessage(), null);
    } catch (ProjectUpdationFailedException e) {
      return prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * To get projects metadata based on metadata status
   *
   * @return {@link ResponseEntity} - which include status code, message and response data
   */
  @GetMapping(value = METADATA, produces = APPLICATION_JSON_VALUE,
      consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doGetMetadata(
      @RequestParam(name = "status", required = false) Boolean status) {
    try {
      return ofNullable(projectService.getMetadata(true))
          .map(metadata -> prepareSuccessResponse(DATA_RETRIEVAL_SUCCESS_MESSAGE, metadata, null))
          .orElse(prepareNoRecordsFoundResponse(NO_RECORDS_FOUND_ERROR_MESSAGE));
    } catch (MetadataRetrievalFailedException e) {
      return prepareInternalServerErrorResponse(e.getMessage());
    }
  }



}
