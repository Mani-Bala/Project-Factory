package com.revature.project.factory.dao;

import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.model.Employee;

public interface AdminDAO {

  /**
   * Get System user by user name
   *
   * @param email
   * @return
   * @throws DataServiceException
   */
  Employee getUserByEmail(String email) throws DataRetrievalFailedException;

  /**
   * Get System user by user name
   *
   * @param email
   * @return
   * @throws DataRetrievalFailedException
   */
  Employee getUserById(Long userId) throws DataRetrievalFailedException;

  /**
   * Update employee
   *
   * @param employee
   * @throws DataUpdationFailedException
   */
  void updateEmployee(Employee employee) throws DataUpdationFailedException;
}
