package com.revature.project.factory.dao;


import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dao.impl.AdminDAOImpl;
import com.revature.project.factory.data.access.DataModifier;
import com.revature.project.factory.data.access.DataRetriever;
import com.revature.project.factory.data.access.exception.DataAccessException;
import com.revature.project.factory.model.Employee;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*"})
public class AdminDAOImplTest {

  private static final String DATA_FETCH_FAILED = "Data fetch failed.";

  @InjectMocks
  private AdminDAOImpl adminDAO;

  @Mock
  private DataRetriever dataRetriever;

  @Mock
  private DataModifier dataModifier;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  /**
   * This method is used to test the get user by email. expect success
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test
  public void whenDoGetUserByEmailGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveObjectByHQL(Mockito.anyString(), Mockito.anyList()))
        .thenReturn(getEmployee());
    Employee employee = adminDAO.getUserByEmail(getEmployee().getEmailId());
    Assert.assertEquals(getEmployee().getEmailId(), employee.getEmailId());
  }

  /**
   * This method is used to test the get user by email. expect failure
   *
   * @throws DataRetrievalFailedException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenDoGetUserByEmailGetFailure()
      throws DataRetrievalFailedException, DataAccessException {

    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataRetriever)
        .retrieveObjectByHQL(Mockito.anyString(), Mockito.anyList());
    adminDAO.getUserByEmail(getEmployee().getEmailId());
  }

  /**
   * This method is used to test the get user by id. expect success
   *
   * @throws DataServiceException if DAO service fail.
   */
  @Test
  public void whenDoGetUserByIdGetSuccess()
      throws DataRetrievalFailedException, DataAccessException {
    Mockito.when(dataRetriever.retrieveObjectByHQL(Mockito.anyString(), Mockito.anyList()))
        .thenReturn(getEmployee());
    Employee employee = adminDAO.getUserById(getEmployee().getId());
    Assert.assertEquals(getEmployee().getEmailId(), employee.getEmailId());
  }

  /**
   * This method is used to test the get user by id. expect failure
   *
   * @throws DataServiceException if DAO service fail.
   */
  @Test(expected = DataRetrievalFailedException.class)
  public void whenDoGetUserByIdGetFailure()
      throws DataRetrievalFailedException, DataAccessException {

    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataRetriever)
        .retrieveObjectByHQL(Mockito.anyString(), Mockito.anyList());
    adminDAO.getUserById(getEmployee().getId());
  }

  /**
   * This method is used to test the update employee. expect success
   *
   * @throws DataServiceException if DAO service fail.
   */
  @Test
  public void whenDoUpdateEmployeeGetSuccess()
      throws DataUpdationFailedException, DataAccessException {
    Mockito.doNothing().when(dataModifier).update(Mockito.any());
    adminDAO.updateEmployee(getEmployee());
    Mockito.verify(dataModifier, Mockito.times(1)).update(Mockito.any());
  }

  /**
   * This method is used to test the get user by id. expect failure
   *
   * @throws DataServiceException if DAO service fail.
   */
  @Test(expected = DataUpdationFailedException.class)
  public void whenDoUpdateEmployeeGetFailure()
      throws DataUpdationFailedException, DataAccessException {

    Mockito.doThrow(new DataAccessException(DATA_FETCH_FAILED) {}).when(dataModifier)
        .update(Mockito.any());
    adminDAO.updateEmployee(getEmployee());
  }


  private Employee getEmployee() {
    Employee employee = new Employee();
    employee.setId(2);
    employee.setEmailId("user@yopmail.com");
    employee.setFirstName("user");
    employee.setLastName("name");
    employee.setIsActive(true);
    employee.setCreatedBy(1L);
    employee.setCreatedOn(LocalDateTime.now());
    return employee;
  }

}
