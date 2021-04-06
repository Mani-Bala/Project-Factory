package com.revature.project.factory.data.access;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.project.factory.data.access.exception.DataAccessException;
import com.revature.project.factory.data.access.exception.DataSourceOperationFailedException;

/**
 * <p>
 * Name: DataRetrieverHibernateImpl<br>
 * Description: This implementation is based on hibernate and this is used for the Select DML
 * operation This uses hibernate session which is injected through spring container.Transactions
 * managed by spring container.
 * </p>
 */
@Repository
@Transactional(propagation = Propagation.REQUIRED, readOnly = false,
    rollbackFor = DataAccessException.class)
public class DataRetrieverHibernateImpl implements DataRetriever, Serializable {

  private static final ToIntFunction<List> GET_SIZE = list -> list != null ? list.size() : 0;

  private static final long serialVersionUID = 1L;
  private static final String QUERY_RETRIEVED_RESULT = "Query retrieved the result from database.";
  private static final String RETRIEVAL_FAILS = "Datasource Retrieval Failed.";

  private static Logger logger = LogManager.getLogger(DataRetrieverHibernateImpl.class);

  private SessionFactory sessionFactory;

  @Autowired
  public DataRetrieverHibernateImpl(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  @Override
  public <E> List<E> retrieveBySQLResultTransformer(String queryString,
      List<QueryParameter<?>> queryParameters, Class<?> t) throws DataAccessException {
    List<E> objects = null;
    try {
      logger.debug("Retrieving the result based on the sql query : {} , parameters : {}",
          queryString, getJsonString(queryParameters));

      Session session = getSessionFactory().getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query<E> query = session.createNativeQuery(queryString);
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
        if (Objects.nonNull(t)) {
          query.setResultTransformer(Transformers.aliasToBean(t));
        }
        objects = query.list();
        logger.debug(
            "Retrieved the result based on the sql query : {} ,"
                + " parameters : {}, size of data : {} ",
            queryString, getJsonString(queryParameters), GET_SIZE.applyAsInt(objects));
      }
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    logger.info(QUERY_RETRIEVED_RESULT);
    return objects;
  }


  @Override
  public <E> List<E> retrieveByHQL(String queryString) throws DataAccessException {
    List<E> objects = null;
    try {
      logger.debug("Retrieving the result based on the query : {}", queryString);

      Session session = getSessionFactory().getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query<E> query = session.createQuery(queryString);
        objects = query.list();
      }

      logger.debug("Retrieved the result based on the query : {} and size of data : {}",
          queryString, GET_SIZE.applyAsInt(objects));
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(QUERY_RETRIEVED_RESULT, exception);
    }
    return objects;
  }

  @Override
  public <E> List<E> retrieveByHQL(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException {
    List<E> objects = null;
    try {
      logger.debug("Retrieving the result based on the query : {} , with the parameters : {}",
          queryString, getJsonString(queryParameters));

      Session session = getSessionFactory().getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query query = session.createQuery(queryString);
        setQueryParameters(query, queryParameters);
        objects = query.list();
      }

      logger.debug("Retrieved the result based on the query : {} , with the parameters : {}",
          queryString, getJsonString(queryParameters), GET_SIZE.applyAsInt(objects));
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    return objects;
  }

  @Override
  public <E> List<E> retrieveByHQL(String queryString, QueryProperties queryProperties)
      throws DataAccessException {
    List<E> objects = null;
    try {
      logger.debug("Retrieving the result based on the query : {} , with the query properties : {}",
          queryString, getJsonString(queryProperties));

      Session session = getSessionFactory().getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query query = session.createQuery(queryString);
        if (Objects.nonNull(queryProperties)) {
          if (Objects.nonNull(queryProperties.getFirstResult())) {
            query.setFirstResult(queryProperties.getFirstResult());
          }
          if (Objects.nonNull(queryProperties.getMaxResults())) {
            query.setMaxResults(queryProperties.getMaxResults());
          }
          if (Objects.nonNull(queryProperties.getFetchSize())) {
            query.setFetchSize(queryProperties.getFetchSize());
          }
        }
        objects = query.list();
      }

      logger.debug(
          "Retrieved the result based on the query : {} , "
              + "	with the query properties : {}, size of data : {}",
          queryString, getJsonString(queryProperties), GET_SIZE.applyAsInt(objects));
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    return objects;
  }

  @Override
  public <E> List<E> retrieveBySQLResultTransformer(String queryString, Class<?> t)
      throws DataAccessException {
    List<E> list = null;
    try {
      logger.debug("Retrieving the result based on the sql query : {} ", queryString);
      Session session = getSessionFactory().getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query<E> query = session.createSQLQuery(queryString);
        if (Objects.nonNull(t)) {
          query.setResultTransformer(Transformers.aliasToBean(t));
        }
        list = query.list();
      }

      logger.debug("Retrieved the result based on the sql query : {} , size of data : {}",
          queryString, GET_SIZE.applyAsInt(list));
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    return list;
  }

  @Override
  public <T> T retrieveObjectByHQL(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException {
    T object = null;
    try {
      logger.debug("Retrieving the object based on the query : {} , with the parameters : {}",
          queryString, getJsonString(queryParameters));

      Session session = sessionFactory.getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query query = session.createQuery(queryString);
        setQueryParameters(queryParameters, query);
        object = (T) query.uniqueResult();
      }
      if (Objects.nonNull(object)) {
        logger.debug(
            "Data retrieved successfully, based on the query : {} , with the parameters : {}",
            queryString, getJsonString(queryParameters));
      } else {
        logger.debug("Retrieved empty data, based on the query : {} , with the parameters : {}",
            queryString, getJsonString(queryParameters));
      }
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    return object;
  }

  @Override
  public <E> List<E> retrieveBySQLResultTransformer(String queryString,
      List<QueryParameter<?>> queryParameters, Class<?> t, QueryProperties queryProperties)
      throws DataAccessException {
    List<E> objects = null;
    try {
      logger.debug(
          "Retrieving the result based on the sql query : {}  ,"
              + " with the parameters : {}, start limit : {}, no of records : {}",
          queryString, getJsonString(queryParameters), queryProperties.getFirstResult(),
          queryProperties.getMaxResults());

      Session session = getSessionFactory().getCurrentSession();
      if (StringUtils.isNotBlank(queryString)) {
        Query query = session.createSQLQuery(queryString);
        setQueryParameters(queryParameters, query);
        if (Objects.nonNull(queryProperties.getFirstResult())) {
          query.setFirstResult(queryProperties.getFirstResult());
        }
        if (Objects.nonNull(queryProperties.getMaxResults())) {
          query.setMaxResults(queryProperties.getMaxResults());
        }
        if (Objects.nonNull(queryProperties.getFetchSize())) {
          query.setFetchSize(queryProperties.getFetchSize());
        }
        query.setResultTransformer(Transformers.aliasToBean(t));
        objects = query.list();
        logger.debug(
            "Retrieved the result based on the sql query : {} , with the parameters : {} , start limit : {},"
                + " no of records : {}, size of data : {} ",
            queryString, getJsonString(queryParameters), queryProperties.getFirstResult(),
            queryProperties.getMaxResults(), GET_SIZE.applyAsInt(objects));
      }
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    return objects;
  }

  @Override
  public <E> List<E> retrieveBySQL(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException {
    List<E> objects = null;
    try {
      logger.debug("Retrieving the result based on the sql query : %s , with the parameters : {}",
          queryString, getJsonString(queryParameters));

      Session session = getSessionFactory().getCurrentSession();
      if (queryString != null) {
        Query<E> query = session.createNativeQuery(queryString);
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
        objects = query.list();
      }
      logger.debug(
          "Retrieved the result based on the sql query : {} , with the parameters : {}, size of data : %d",
          queryString, getJsonString(queryParameters), GET_SIZE.applyAsInt(objects));
    } catch (Exception exception) {
      logger.error(exception.getMessage(), exception);
      throw new DataSourceOperationFailedException(RETRIEVAL_FAILS, exception);
    }
    logger.info(QUERY_RETRIEVED_RESULT);
    return objects;
  }

  // ----------------- Root Unique Fetch ------------------------[END]
  private String getJsonString(Object obj) throws JsonProcessingException {
    ObjectWriter ow = new ObjectMapper().writer();
    ow.withDefaultPrettyPrinter();
    return ow.writeValueAsString(obj);
  }

  /**
   * Assister method to DataRetriever and DataModifier implementations. This method handles adding
   * the values of many {@link QueryParameter}s to the Hibernate {@link Query}.
   *
   * @param query - Query to be executed
   * @param parameters - Parameters which required to execute query
   */
  private void setQueryParameters(Query<?> query, Collection<QueryParameter<?>> parameters) {
    for (QueryParameter<?> parameter : parameters) {
      String name = parameter.getName();
      Object value = parameter.getValue();
      if (value instanceof Collection) {
        query.setParameterList(name, (Collection<?>) value);
      } else {
        query.setParameter(name, value);
      }
    }
  }

  private void setQueryParameters(List<QueryParameter<?>> queryParameters, Query query) {
    for (QueryParameter<?> queryParameter : queryParameters) {
      if (queryParameter.getValue().getClass().equals(List.class)
          || queryParameter.getValue().getClass().equals(ArrayList.class)) {
        List<?> parameter = (List<?>) queryParameter.getValue();
        query.setParameterList(queryParameter.getName(), parameter);
      } else {
        query.setParameter(queryParameter.getName(), queryParameter.getValue());
      }
    }
  }
}
