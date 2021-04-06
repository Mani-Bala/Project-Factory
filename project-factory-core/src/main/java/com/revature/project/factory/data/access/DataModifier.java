package com.revature.project.factory.data.access;

import java.util.List;

import com.revature.project.factory.data.access.exception.DataAccessException;

public interface DataModifier {

  <T> void insert(T type) throws DataAccessException;

  <T> void update(T type) throws DataAccessException;

  void executeSQLQuery(String queryString, List<QueryParameter<?>> queryParameters)
      throws DataAccessException;

  <E> boolean saveOrUpdateBulk(List<E> objects) throws DataAccessException;
}
