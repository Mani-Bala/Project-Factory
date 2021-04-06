package com.revature.project.factory.data.access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.project.factory.data.access.exception.DataAccessException;
import com.revature.project.factory.data.access.exception.DataSourceOperationFailedException;
import com.revature.project.factory.data.access.exception.DuplicateRecordException;
import com.revature.project.factory.data.access.exception.ReferentialIntegrityException;

/**
 * <p>
 * Name: DataModifierHibernateImpl<br>
 * Description: This implementation is based on hibernate and this is used for the following DML
 * operation Insert , Update , Delete This uses hibernate session which is injected through spring
 * container and also the transaction managed by spring container.
 * </p>
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = DataAccessException.class)
public class DataModifierHibernateImpl implements DataModifier, Serializable {

  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(DataModifierHibernateImpl.class);
  private static final String OBJECT_SAVE_FAILS = "Object save failed";
  private static final String OBJECT_UPDATE_FAILS = "Object update failed";
  private static final String DUPLICATE_RECORD_EXCEPTION = "Duplicate Record Exception";
  private static final String FOREIGN_KEY_EXCEPTION = "Foreign Key Reference Exception";
  private static final String DATA_UPDATION_FAILS = "Data updation failed. No of records : {}";
  private SessionFactory sessionFactory;

  @Autowired
  public DataModifierHibernateImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public <T> void insert(T type) throws DataAccessException {
    try {
      logger.debug("Preparing to save object.");
      Session session = sessionFactory.getCurrentSession();
      Object id = session.save(type);
      session.flush();
      logger.debug("Object saved successfully. with id : {} ", id);
    } catch (ConstraintViolationException cvException) {
      logger.error("", cvException);
      throw new DuplicateRecordException(DUPLICATE_RECORD_EXCEPTION, cvException);
    } catch (Exception hibernateException) {
      logger.error(OBJECT_SAVE_FAILS, hibernateException);
      throw new DataSourceOperationFailedException(hibernateException.getMessage(),
          hibernateException);
    }
  }

  @Override
  public <T> void update(T type) throws DataAccessException {
    try {
      logger.debug("Preparing to update the object");
      Session session = sessionFactory.getCurrentSession();
      session.clear();
      session.saveOrUpdate(type);
      session.flush();
      logger.debug("Object updated successfully.");
    } catch (PersistenceException cvException) {
      logger.error(OBJECT_UPDATE_FAILS, cvException);
      throw new DuplicateRecordException(DUPLICATE_RECORD_EXCEPTION, cvException);
    } catch (Exception exception) {
      logger.error(OBJECT_UPDATE_FAILS, exception);
      throw new DataSourceOperationFailedException(exception.getMessage(), exception);
    }
  }

  @Override
  public void executeSQLQuery(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException {
    try {
      logger.debug("Executing the sql query : {}, parameters : {} ", queryString,
          getJsonString(queryParameters));
      Session session = sessionFactory.getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        NativeQuery query = session.createNativeQuery(queryString);
        for (QueryParameter<?> queryParameter : queryParameters) {
          if (queryParameter.getValue() != null
              && (queryParameter.getValue().getClass().equals(List.class)
                  || queryParameter.getValue().getClass().equals(ArrayList.class))) {
            List<?> parameter = (List<?>) queryParameter.getValue();
            query.setParameterList(queryParameter.getName(), parameter);
          } else {
            query.setParameter(queryParameter.getName(), queryParameter.getValue());
          }
        }
        query.executeUpdate();
      }
      logger.debug("Executed the sql query : {},parameters : {}, no of rows affected : {} ",
          queryString, getJsonString(queryParameters));
    } catch (ConstraintViolationException cvException) {
      logger.error("SQL Query execution failed. query : {} ", queryString, cvException);
      throw new ReferentialIntegrityException(FOREIGN_KEY_EXCEPTION, cvException);
    } catch (HibernateException hibernateException) {
      logger.error("SQL Query executed successfully. query : {} ", queryString, hibernateException);
      throw new DataSourceOperationFailedException(hibernateException.getMessage());
    } catch (Exception exception) {
      logger.error("SQL Query execution failed. query : {} ", queryString, exception);
      throw new DataSourceOperationFailedException(exception.getMessage());
    }
  }

  private String getJsonString(Object obj) throws JsonProcessingException {
    ObjectWriter ow = new ObjectMapper().writer();
    ow.withDefaultPrettyPrinter();
    return ow.writeValueAsString(obj);
  }

  @Override
  public <E> boolean saveOrUpdateBulk(List<E> objects) throws DataAccessException {
    boolean result = false;
    try {
      logger.debug("Preparing to update the objects. no of objects : {}", objects.size());
      Session session = sessionFactory.getCurrentSession();
      for (int i = 0; i < objects.size(); i++) {
        Object type = objects.get(i);
        session.saveOrUpdate(type);
        logger.debug("Object updated successfully. {}/{}", i + 1, objects.size());
        if (i % 20 == 0) {
          session.flush();
          session.clear();
        }
      }
      session.flush();
      logger.debug("Objects updated successfully, no of objects : {}", objects.size());
      logger.info("Data updated successfully. No of records : {}", objects.size());
    } catch (ConstraintViolationException cvException) {
      logger.error(DATA_UPDATION_FAILS, objects.size(), cvException);
      throw new DuplicateRecordException(DUPLICATE_RECORD_EXCEPTION, cvException);
    } catch (HibernateException hibernateException) {
      logger.error(DATA_UPDATION_FAILS, objects.size(), hibernateException);
      throw new DataSourceOperationFailedException(hibernateException.getMessage(),
          hibernateException);
    } catch (Exception exception) {
      logger.error(DATA_UPDATION_FAILS, objects.size(), exception);
      throw new DataSourceOperationFailedException(exception.getMessage(), exception);
    }
    return result;
  }
}
