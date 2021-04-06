package com.revature.project.factory.service;

import static com.revature.project.factory.constants.TestConstants.ENCRYPTED_TOKEN;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;

import javax.inject.Provider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.project.factory.controller.config.UserRequest;
import com.revature.project.factory.dao.AdminDAO;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.dto.TokenDTO;
import com.revature.project.factory.exception.UnprocessableEntityException;
import com.revature.project.factory.model.Employee;
import com.revature.project.factory.service.admin.impl.AdminAuthServiceImpl;
import com.revature.project.factory.service.exception.AuthenticationFailsException;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.EncryptionFailsException;
import com.revature.project.factory.service.exception.UnauthorizedException;
import com.revature.project.factory.util.AESTokenEncryption;
import com.revature.project.factory.util.EmailUtils;
import com.revature.project.factory.util.PasswordEncryptionUtils;
import com.revature.project.factory.util.PropertiesFileUtils;
import com.revature.project.factory.util.TokenUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AESTokenEncryption.class, PropertiesFileUtils.class, EmailUtils.class})
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*", "javax.net.ssl.*",
    "jdk.internal.reflect.*"})
public class AdminAuthServiceImplTest {
  @InjectMocks
  private AdminAuthServiceImpl adminAuthService;
  @Mock
  private AdminDAO adminDAO;

  @Mock
  private TokenUtils tokenUtils;

  @Mock
  private Provider<UserRequest> requestScopedBeanStub;

  private UserRequest userRequest;

  private PasswordEncryptionUtils passwordEncryptionUtils;

  @Mock
  DecodedJWT decodedJWT;

  @Before
  public void init() {
    PowerMockito.mockStatic(PropertiesFileUtils.class);
    PowerMockito.mockStatic(EmailUtils.class);
    Mockito.when(PropertiesFileUtils.getValue(Mockito.anyString())).thenReturn("key");
    PowerMockito.mockStatic(AESTokenEncryption.class);
    passwordEncryptionUtils = mock(PasswordEncryptionUtils.class);
    setMock(passwordEncryptionUtils);
  }

  private void setMock(PasswordEncryptionUtils mock) {
    try {
      Field instance = PasswordEncryptionUtils.class.getDeclaredField("passwordEncryptionInstance");
      instance.setAccessible(true);
      instance.set(instance, mock);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void when_doAuthenticateAdminLogin_Expect_Success()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException, EncryptionFailsException {
    when(adminDAO.getUserByEmail(anyString())).thenReturn(getEmployee());
    when(tokenUtils.doGetToken(Mockito.any())).thenReturn(ENCRYPTED_TOKEN);
    Mockito.when(passwordEncryptionUtils.authenticate(anyString(), any(), any())).thenReturn(true);
    Mockito.when(decodedJWT.getExpiresAt()).thenReturn(new Date());
    when(tokenUtils.decodeToken(Mockito.any())).thenReturn(decodedJWT);
    Mockito.when(PropertiesFileUtils.getValue(anyString())).thenReturn("key");
    Mockito.when(AESTokenEncryption.encrypt(anyString())).thenReturn("key");
    SystemUserDTO systemUserDTO = adminAuthService.doAuthenticateAdminLogin(getSystemUserDTO());
    Assert.assertNotNull(systemUserDTO);
  }

  @Test(expected = UnprocessableEntityException.class)
  public void when_doAuthenticateAdminLogin_Expect_UnprocessableEntityExceptionWithNullPwd()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException {
    SystemUserDTO systemUserDTO = getSystemUserDTO();
    systemUserDTO.setPassword(null);
    adminAuthService.doAuthenticateAdminLogin(systemUserDTO);
  }

  @Test(expected = AuthenticationFailsException.class)
  public void when_doAuthenticateAdminLogin_Expect_UnprocessableEntityException()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException {
    when(adminDAO.getUserByEmail(anyString())).thenReturn(null);
    adminAuthService.doAuthenticateAdminLogin(getSystemUserDTO());
  }

  @Test(expected = AuthenticationFailsException.class)
  public void when_doAuthenticateAdminLogin_Expect_AuthenticationFailsException()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException {
    when(adminDAO.getUserByEmail(anyString())).thenReturn(getEmployee());
    Mockito.when(passwordEncryptionUtils.authenticate(anyString(), any(), any())).thenReturn(false);
    adminAuthService.doAuthenticateAdminLogin(getSystemUserDTO());
  }

  @Test(expected = AuthenticationFailsException.class)
  public void when_doAuthenticateAdminLogin_Expect_AuthenticationFailsExceptionWithInactiveEmployee()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException {
    Employee employee = getEmployee();
    employee.setIsActive(false);
    when(adminDAO.getUserByEmail(anyString())).thenReturn(employee);
    Mockito.when(passwordEncryptionUtils.authenticate(anyString(), any(), any())).thenReturn(true);
    adminAuthService.doAuthenticateAdminLogin(getSystemUserDTO());
  }

  @Test(expected = BusinessServiceException.class)
  public void when_doAuthenticateAdminLogin_Expect_BusinessServiceException()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException, EncryptionFailsException {
    when(adminDAO.getUserByEmail(anyString())).thenThrow(DataRetrievalFailedException.class);
    adminAuthService.doAuthenticateAdminLogin(getSystemUserDTO());
  }

  @Test
  public void when_doDecodeToken_Expect_Success()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException, EncryptionFailsException {
    Mockito.when(AESTokenEncryption.decrypt(anyString())).thenReturn("key");
    Mockito.when(tokenUtils.getDetailsFromToken(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(getTokenDTO());
    Mockito.when(adminDAO.getUserById(Mockito.anyLong())).thenReturn(getEmployee());
    TokenDTO tokenDTO = adminAuthService.doDecodeToken(ENCRYPTED_TOKEN);
    Assert.assertNotNull(tokenDTO);
  }

  @Test(expected = UnauthorizedException.class)
  public void when_doDecodeToken_Expect_UnauthorizedException()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException, EncryptionFailsException {
    Mockito.when(AESTokenEncryption.decrypt(anyString())).thenReturn("key");
    Mockito.when(tokenUtils.getDetailsFromToken(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(getTokenDTO());
    Mockito.when(adminDAO.getUserById(Mockito.anyLong())).thenReturn(null);
    adminAuthService.doDecodeToken(ENCRYPTED_TOKEN);
  }

  @Test(expected = EncryptionFailsException.class)
  public void when_doDecodeToken_Expect_EncryptionFailsException()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException, EncryptionFailsException {
    Mockito.when(AESTokenEncryption.decrypt(anyString())).thenThrow(EncryptionFailsException.class);
    adminAuthService.doDecodeToken(ENCRYPTED_TOKEN);
  }

  @Test(expected = BusinessServiceException.class)
  public void when_doDecodeToken_Expect_DataServiceException()
      throws DataRetrievalFailedException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException, AuthenticationFailsException, EncryptionFailsException {
    Mockito.when(AESTokenEncryption.decrypt(anyString())).thenReturn("key");
    Mockito.when(tokenUtils.getDetailsFromToken(Mockito.anyString(), Mockito.anyBoolean()))
        .thenReturn(getTokenDTO());
    Mockito.when(adminDAO.getUserById(Mockito.anyLong()))
        .thenThrow(DataRetrievalFailedException.class);
    adminAuthService.doDecodeToken(ENCRYPTED_TOKEN);
  }

  @Test
  public void when_doForgotPassword_Expect_Success() throws Exception {
    Mockito.when(adminDAO.getUserByEmail(Mockito.anyString())).thenReturn(getEmployee());
    Mockito.when(passwordEncryptionUtils.getEncryptedPassword(anyString(), any()))
        .thenReturn("pwd".getBytes());
    Mockito.when(passwordEncryptionUtils.generateSalt()).thenReturn("pwd salt".getBytes());
    PowerMockito.doNothing().when(EmailUtils.class, "sendEmail", any());
    adminAuthService.doForgotPassword("user@yopmail.com");
    Assert.assertEquals("user@yopmail.com", getEmployee().getEmailId());
  }

  @Test(expected = UnprocessableEntityException.class)
  public void when_doForgotPassword_Expect_UnprocessableEntityException() throws Exception {
    adminAuthService.doForgotPassword("user");
  }

  @Test(expected = UnauthorizedException.class)
  public void when_doForgotPassword_Expect_UnauthorizedException() throws Exception {
    Mockito.when(adminDAO.getUserByEmail(Mockito.anyString())).thenReturn(null);
    adminAuthService.doForgotPassword("user@yopmail.com");
  }

  @Test(expected = UnauthorizedException.class)
  public void when_doForgotPassword_Expect_UnauthorizedExceptionWithInactiveEmployee()
      throws Exception {
    Employee employee = getEmployee();
    employee.setIsActive(false);
    Mockito.when(adminDAO.getUserByEmail(Mockito.anyString())).thenReturn(employee);
    adminAuthService.doForgotPassword("user@yopmail.com");
  }

  @Test(expected = BusinessServiceException.class)
  public void when_doForgotPassword_Expect_BusinessServiceException() throws Exception {
    Mockito.when(adminDAO.getUserByEmail(Mockito.anyString()))
        .thenThrow(DataRetrievalFailedException.class);
    adminAuthService.doForgotPassword("user@yopmail.com");
  }

  @Test
  public void when_doResetPassword_Expect_Success() throws Exception {
    Mockito.when(adminDAO.getUserById(Mockito.anyLong())).thenReturn(getEmployee());
    Mockito.when(passwordEncryptionUtils.getEncryptedPassword(anyString(), any()))
        .thenReturn("pwd".getBytes());
    Mockito.when(passwordEncryptionUtils.generateSalt()).thenReturn("pwd salt".getBytes());
    PowerMockito.doNothing().when(adminDAO).updateEmployee(Mockito.any());
    adminAuthService.resetPassword(getSystemUserDTO());
    Assert.assertEquals("pwd", getSystemUserDTO().getPassword());
  }

  @Test(expected = BusinessConflictException.class)
  public void when_doResetPassword_Expect_BusinessConflictException() throws Exception {
    Mockito.when(adminDAO.getUserById(Mockito.anyLong())).thenReturn(null);
    adminAuthService.resetPassword(getSystemUserDTO());
  }

  @Test(expected = BusinessServiceException.class)
  public void when_doResetPassword_Expect_BusinessServiceException() throws Exception {
    Mockito.when(adminDAO.getUserById(Mockito.anyLong()))
        .thenThrow(DataRetrievalFailedException.class);
    adminAuthService.resetPassword(getSystemUserDTO());
  }

  private Employee getEmployee() {
    Employee employee = new Employee();
    employee.setId(2L);
    employee.setEmailId("user@yopmail.com");
    employee.setFirstName("user");
    employee.setLastName("name");
    employee.setIsActive(true);
    employee.setCreatedBy(1L);
    employee.setCreatedOn(LocalDateTime.now());
    employee.setPasswordResetRequest(true);
    return employee;
  }

  private SystemUserDTO getSystemUserDTO() {
    SystemUserDTO systemUserDTO = new SystemUserDTO();
    systemUserDTO.setEncryptedLoginToken(ENCRYPTED_TOKEN);
    systemUserDTO.setId(2);
    systemUserDTO.setEmailId("user@yopmail.com");
    systemUserDTO.setFirstName("user");
    systemUserDTO.setLastName("name");
    systemUserDTO.setIsActive(true);
    systemUserDTO.setCreatedBy(1L);
    systemUserDTO.setPassword("pwd");
    systemUserDTO.setCreatedOn(LocalDateTime.now());
    return systemUserDTO;
  }

  private TokenDTO getTokenDTO() {
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setExpiresAt(new Date());
    tokenDTO.setUserName("user name");
    tokenDTO.setEncryptedToken(ENCRYPTED_TOKEN);
    tokenDTO.setEmpId(1);
    return tokenDTO;
  }

}
