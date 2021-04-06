package com.revature.project.factory.dao.impl;


import static com.revature.project.factory.dao.constant.DataConstants.ORDER_BY;
import static com.revature.project.factory.dao.constant.DataConstants.PROJECT_ID;
import static com.revature.project.factory.dao.constant.DataConstants.PROJECT_INSERT_FAILED;
import static com.revature.project.factory.dao.constant.DataConstants.PROJECT_RETRIEVAL_FAILED;
import static com.revature.project.factory.dao.constant.DataConstants.SKILL_RETRIEVAL_FAILED;
import static com.revature.project.factory.dao.constant.DataConstants.UPDATE_PROJECT_FAILED;
import static com.revature.project.factory.dao.query.ProjectQuery.ACTIVATE_DEACTIVATE_PROJECT_BY_ID;
import static com.revature.project.factory.dao.query.ProjectQuery.CHECK_UNIQUE_PROJECT_BY_PROJECT_NAME;
import static com.revature.project.factory.dao.query.ProjectQuery.DELETE_PROJECT_BY_PROJECT_ID;
import static com.revature.project.factory.dao.query.ProjectQuery.DELETE_PROJECT_SKILLS_BY_PROJECT_ID;
import static com.revature.project.factory.dao.query.ProjectQuery.GET_PROJECTS;
import static com.revature.project.factory.dao.query.ProjectQuery.GET_PROJECT_BY_ID;
import static com.revature.project.factory.dao.query.ProjectQuery.GET_SKILLS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revature.project.factory.dao.ProjectDAO;
import com.revature.project.factory.dao.constant.DataConstants;
import com.revature.project.factory.dao.exception.DataDeletionFailedException;
import com.revature.project.factory.dao.exception.DataInsertionFailedException;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dao.query.CategoryQueries;
import com.revature.project.factory.data.access.DataModifier;
import com.revature.project.factory.data.access.DataRetriever;
import com.revature.project.factory.data.access.QueryParameter;
import com.revature.project.factory.data.access.QueryProperties;
import com.revature.project.factory.data.access.exception.DataAccessException;
import com.revature.project.factory.dto.CategoryDTO;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.dto.SkillDTO;
import com.revature.project.factory.model.Project;
import com.revature.project.factory.util.CalendarUtils;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

  private static final Logger LOGGER = LogManager.getLogger(ProjectDAOImpl.class);
  private static final String STATUS = "{status}";

  private DataModifier dataModifier;
  private DataRetriever dataRetriever;

  @Autowired
  public ProjectDAOImpl(DataModifier dataModifier, DataRetriever dataRetriever) {
    this.dataModifier = dataModifier;
    this.dataRetriever = dataRetriever;
  }

  @Override
  public Project getProjectById(Long id) throws DataRetrievalFailedException {
    try {
      List<QueryParameter<?>> queryParameters = new ArrayList<>();
      queryParameters.add(new QueryParameter<>("id", id));
      return dataRetriever.retrieveObjectByHQL(GET_PROJECT_BY_ID.toString(), queryParameters);
    } catch (DataAccessException e) {
      LOGGER.error(PROJECT_RETRIEVAL_FAILED + " based on id = {} :: errorMsg = {} ", id,
          e.getMessage(), e);
      throw new DataRetrievalFailedException(PROJECT_RETRIEVAL_FAILED, e);
    }
  }

  @Override
  public List<ProjectDTO> doGetAllProjects(Integer pageNo, Integer sizeNo, String orderBy,
      String sortOrder, Boolean status) throws DataRetrievalFailedException {
    try {
      LOGGER.info("preparing the query to get all projects based on pagination");
      List<QueryParameter<?>> queryParameters = new ArrayList<>();
      StringBuilder query = new StringBuilder(GET_PROJECTS);
      buildQueryRequestByStatus(" AND ", query, status, "p");
      if (Objects.nonNull(pageNo) && Objects.nonNull(sizeNo)) {
        buildQueryRequest(orderBy, sortOrder, query, "p");
        QueryProperties queryProperties = new QueryProperties();
        int start = pageNo > 0 ? pageNo - 1 : pageNo;
        start *= sizeNo;
        queryProperties.setFirstResult(start);
        queryProperties.setMaxResults(sizeNo);
        return dataRetriever.retrieveBySQLResultTransformer(query.toString(), queryParameters,
            ProjectDTO.class, queryProperties);
      } else {
        buildQueryRequest(orderBy, sortOrder, query, "p");
        return dataRetriever.retrieveBySQLResultTransformer(query.toString(), queryParameters,
            ProjectDTO.class);
      }
    } catch (DataAccessException e) {
      LOGGER.error(PROJECT_RETRIEVAL_FAILED, e);
      throw new DataRetrievalFailedException(PROJECT_RETRIEVAL_FAILED, e);
    }
  }

  private void buildQueryRequest(String orderBy, String sortOrder, StringBuilder query,
      String type) {
    if (StringUtils.isNotBlank(sortOrder) && "asc".equalsIgnoreCase(sortOrder)) {
      query.append(ORDER_BY).append(orderBy).append(" ASC ");
    } else if (StringUtils.isNotBlank(sortOrder) && "desc".equalsIgnoreCase(sortOrder)) {
      query.append(ORDER_BY).append(orderBy).append(" DESC ");
    } else {
      query.append(ORDER_BY).append(" ").append(type).append(".id ").append(" ASC ");
    }
  }

  private void buildQueryRequestByStatus(String prefix, StringBuilder query, Boolean status,
      String type) {
    if (BooleanUtils.isTrue(status)) {
      query.append(prefix).append(" ").append(type).append(".IS_ACTIVE=true ");
    }
    if (BooleanUtils.isFalse(status)) {
      query.append(prefix).append(" ").append(type).append(".IS_ACTIVE=false ");
    }
  }

  @Override
  public void saveObjects(List<Object> saveList) throws DataInsertionFailedException {
    try {
      dataModifier.saveOrUpdateBulk(saveList);
    } catch (DataAccessException e) {
      LOGGER.error(PROJECT_INSERT_FAILED, e);
      throw new DataInsertionFailedException(PROJECT_INSERT_FAILED, e);
    }
  }

  @Override
  public void activateOrDeactivateProject(Long id, Boolean status, Long systemUserId)
      throws DataUpdationFailedException {
    try {
      List<QueryParameter<?>> queryParam = new ArrayList<>();
      queryParam.add(new QueryParameter<>("id", id));
      queryParam.add(new QueryParameter<>("status", status));
      queryParam.add(new QueryParameter<>("updatedBy", systemUserId));
      queryParam.add(new QueryParameter<>("updatedOn", CalendarUtils.getNowInUTC()));
      dataModifier.executeSQLQuery(ACTIVATE_DEACTIVATE_PROJECT_BY_ID.toString(), queryParam);
      LOGGER.info("Project with id : {}, status : {}  changed successfully.", id, status);
    } catch (DataAccessException de) {
      LOGGER.error("Project with id : {}, status : {} change failed.", id, status, de);
      throw new DataUpdationFailedException(UPDATE_PROJECT_FAILED, de);
    }
  }

  @Override
  public Boolean checkUniqueProjectName(String projectName) throws DataRetrievalFailedException {
    try {
      List<QueryParameter<?>> queryParam = new ArrayList<>();
      queryParam.add(new QueryParameter<>("projectName", projectName));
      List<BigInteger> ids =
          dataRetriever.retrieveBySQL(CHECK_UNIQUE_PROJECT_BY_PROJECT_NAME.toString(), queryParam);
      return !ids.isEmpty();
    } catch (DataAccessException e) {
      LOGGER.error(PROJECT_RETRIEVAL_FAILED, e);
      throw new DataRetrievalFailedException(PROJECT_RETRIEVAL_FAILED, e);
    }
  }

  @Override
  public void doDeleteProject(Long projectId) throws DataDeletionFailedException {
    try {
      LOGGER.info("Preparing query to delete project by project id");
      List<QueryParameter<?>> queryParameter = new ArrayList<>();
      queryParameter.add(new QueryParameter<>(PROJECT_ID, projectId));
      dataModifier.executeSQLQuery(DELETE_PROJECT_SKILLS_BY_PROJECT_ID.toString(), queryParameter);
      dataModifier.executeSQLQuery(DELETE_PROJECT_BY_PROJECT_ID.toString(), queryParameter);
    } catch (DataAccessException e) {
      LOGGER.error(e.getMessage(), e);
      throw new DataDeletionFailedException(e.getMessage(), e);
    }
  }

  @Override
  public List<CategoryDTO> doGetCategories(Boolean status) throws DataRetrievalFailedException {
    List<QueryParameter<?>> queryParam = new ArrayList<>();
    try {
      String queryString = CategoryQueries.GET_ALL_CATEGORY.toString();
      queryString = buildCategoryQuery(queryString, status);
      return dataRetriever.retrieveBySQLResultTransformer(queryString, queryParam,
          CategoryDTO.class);
    } catch (DataAccessException e) {
      LOGGER.error(DataConstants.CATEGORY_RETRIEVAL_FAILED, e);
      throw new DataRetrievalFailedException(DataConstants.CATEGORY_RETRIEVAL_FAILED, e);
    }
  }

  @Override
  public List<SkillDTO> doGetSkills(Boolean status) throws DataRetrievalFailedException {
    try {
      List<QueryParameter<?>> queryParameters = new ArrayList<>();
      StringBuilder query = new StringBuilder(GET_SKILLS);
      buildQueryRequestByStatus(" where ", query, status, "s");
      query.append(" ORDER BY s.id DESC ");
      return dataRetriever.retrieveBySQLResultTransformer(query.toString(), queryParameters,
          SkillDTO.class);
    } catch (DataAccessException e) {
      LOGGER.error(SKILL_RETRIEVAL_FAILED, e);
      throw new DataRetrievalFailedException(SKILL_RETRIEVAL_FAILED, e);
    }
  }

  private String buildCategoryQuery(String queryString, Boolean status) {
    if (StringUtils.containsIgnoreCase(queryString, STATUS)) {
      if (BooleanUtils.isTrue(status)) {
        queryString = queryString.replace(STATUS, " c.IS_ACTIVE =TRUE ");
      } else if (BooleanUtils.isFalse(status)) {
        queryString = queryString.replace(STATUS, " c.IS_ACTIVE =FALSE ");
      } else {
        queryString = queryString.replace("WHERE {status}", "");
      }
    }
    return queryString;
  }

}
