package com.revature.project.factory.data.access;

import java.util.List;

import com.revature.project.factory.data.access.exception.DataAccessException;

public interface DataRetriever {

  <E> List<E> retrieveBySQLResultTransformer(String queryString,
      List<QueryParameter<?>> queryParameters, Class<?> t) throws DataAccessException;

  <E> List<E> retrieveBySQLResultTransformer(String queryString,
      List<QueryParameter<?>> queryParameters, Class<?> t, QueryProperties queryProperties)
      throws DataAccessException;

  <E> List<E> retrieveByHQL(String queryString) throws DataAccessException;

  <E> List<E> retrieveByHQL(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException;

  <E> List<E> retrieveByHQL(String queryString, QueryProperties queryProperties)
      throws DataAccessException;

  <E> List<E> retrieveBySQLResultTransformer(String query, Class<?> t) throws DataAccessException;

  <T> T retrieveObjectByHQL(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException;

  <E> List<E> retrieveBySQL(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException;

}
