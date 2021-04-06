package com.revature.project.factory.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.revature.project.factory.dao.AdminDAO;
import com.revature.project.factory.dao.constant.DataConstants;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dao.query.AdminQuery;
import com.revature.project.factory.data.access.DataModifier;
import com.revature.project.factory.data.access.DataRetriever;
import com.revature.project.factory.data.access.QueryParameter;
import com.revature.project.factory.data.access.exception.DataAccessException;
import com.revature.project.factory.model.Employee;

@Repository
public class AdminDAOImpl implements AdminDAO {
  private static final Logger LOGGER = LogManager.getLogger(AdminDAOImpl.class);

  @Autowired
  private DataRetriever dataRetriever;

  @Autowired
  private DataModifier dataModifier;

  @Override
  public Employee getUserByEmail(String email) throws DataRetrievalFailedException {
    try {
      List<QueryParameter<?>> queryParameters = new ArrayList<>();
      queryParameters.add(new QueryParameter<>("emailId", email));
      return dataRetriever.retrieveObjectByHQL(AdminQuery.GET_USER_BY_EMAIL, queryParameters);
    } catch (DataAccessException e) {
      LOGGER.error(DataConstants.DATA_RETRIEVAL_FAILED, e);
      throw new DataRetrievalFailedException(DataConstants.DATA_RETRIEVAL_FAILED, e);
    }
  }

  @Override
  public Employee getUserById(Long userId) throws DataRetrievalFailedException {
    try {
      List<QueryParameter<?>> queryParameters = new ArrayList<>();
      queryParameters.add(new QueryParameter<>("userId", userId));
      return dataRetriever.retrieveObjectByHQL(AdminQuery.GET_USER_BY_ID, queryParameters);
    } catch (DataAccessException e) {
      LOGGER.error(DataConstants.DATA_RETRIEVAL_FAILED, e);
      throw new DataRetrievalFailedException(DataConstants.DATA_RETRIEVAL_FAILED, e);
    }
  }

  @Override
  public void updateEmployee(Employee employee) throws DataUpdationFailedException {
    try {
      dataModifier.update(employee);
    } catch (DataAccessException e) {
      LOGGER.error(DataConstants.UPDATE_EMPLOYEE_FAILED, e);
      throw new DataUpdationFailedException(DataConstants.UPDATE_EMPLOYEE_FAILED, e);
    }
  }
}
