package com.revature.project.factory.service.admin.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.dao.AdminDAO;
import com.revature.project.factory.dao.exception.DataRetrievalFailedException;
import com.revature.project.factory.dao.exception.DataUpdationFailedException;
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.dto.TokenDTO;
import com.revature.project.factory.exception.UnprocessableEntityException;
import com.revature.project.factory.mail.EmailWrapper;
import com.revature.project.factory.model.Employee;
import com.revature.project.factory.service.admin.AdminAuthService;
import com.revature.project.factory.service.constant.BusinessConstants;
import com.revature.project.factory.service.exception.AuthenticationFailsException;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.EncryptionFailsException;
import com.revature.project.factory.service.exception.UnauthorizedException;
import com.revature.project.factory.util.AESTokenEncryption;
import com.revature.project.factory.util.EmailUtils;
import com.revature.project.factory.util.PasswordEncryptionUtils;
import com.revature.project.factory.util.PropertiesFileUtils;
import com.revature.project.factory.util.RandomKeyGenerator;
import com.revature.project.factory.util.TokenUtils;
import com.revature.project.factory.util.ValidationUtil;

@Service
public class AdminAuthServiceImpl implements AdminAuthService {

  private static final Logger LOGGER = LogManager.getLogger(AdminAuthServiceImpl.class);

  @Autowired
  private AdminDAO adminDAO;

  @Autowired
  private TokenUtils tokenUtils;

  @Override
  public SystemUserDTO doAuthenticateAdminLogin(SystemUserDTO systemUser)
      throws AuthenticationFailsException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException {
    try {
      if (Objects.isNull(systemUser) || Objects.isNull(systemUser.getEmailId())
          || Objects.isNull(systemUser.getPassword())) {
        throw new UnprocessableEntityException(BusinessConstants.MANDATORY_FIELDS_MISSING);
      }
      Employee employee = adminDAO.getUserByEmail(systemUser.getEmailId());

      if (Objects.isNull(employee) || Objects.isNull(employee.getId())) {
        throw new AuthenticationFailsException(BusinessConstants.INCORRECT_USERNAME_PWD);
      }
      boolean isInactiveUser = !employee.getIsActive();
      if (!PasswordEncryptionUtils.getInstance().authenticate(systemUser.getPassword(),
          employee.getPasswordEncrypt(), employee.getPasswordSalt())) {
        throw new AuthenticationFailsException(BusinessConstants.INCORRECT_USERNAME_PWD);
      } else if (isInactiveUser) {
        throw new AuthenticationFailsException(BusinessConstants.USER_DEACTIVATED);
      }
      String fullName =
          (Objects.nonNull(employee.getFirstName()) ? employee.getFirstName().trim() + " " : "")
              + (Objects.nonNull(employee.getLastName()) ? employee.getLastName().trim() : "");
      employee.setDisplayFullName(fullName);
      ModelMapper modelMapper = new ModelMapper();
      SystemUserDTO user = modelMapper.map(employee, SystemUserDTO.class);
      getSystemUser(user);
      return user;
    } catch (DataRetrievalFailedException e) {
      throw new BusinessServiceException(e.getMessage(), e);
    } catch (AuthenticationFailsException | EncryptionFailsException e) {
      LOGGER.error(e.getMessage(), e);
      throw new AuthenticationFailsException(e.getMessage(), e);
    }
  }

  /**
   * This method is used to generate the token {@link String} and set it in the given
   * {@link SystemUserDTO}
   *
   * @param user
   * @throws BusinessServiceException
   * @throws EncryptionFailsException
   * @throws DataRetrievalFailedException
   */
  private void getSystemUser(SystemUserDTO user)
      throws BusinessServiceException, EncryptionFailsException, UnauthorizedException {
    user.setLoginToken(tokenUtils.doGetToken(user));
    DecodedJWT decodedJWT = tokenUtils.decodeToken(user.getLoginToken());
    user.setTokenExpTime(decodedJWT.getExpiresAt());
    String encryptedToken = AESTokenEncryption.encrypt(user.getLoginToken());
    user.setEncryptedLoginToken(encryptedToken);
  }

  @Override
  public TokenDTO doDecodeToken(String encryptedToken)
      throws EncryptionFailsException, BusinessServiceException, UnauthorizedException {
    String token;
    TokenDTO tokenDTO = null;
    try {
      token = AESTokenEncryption.decrypt(encryptedToken);
      tokenDTO = tokenUtils.getDetailsFromToken(token, true);
      Employee employee = adminDAO.getUserById(tokenDTO.getEmpId());
      if (Objects.nonNull(employee)) {
        return tokenDTO;
      } else {
        throw new UnauthorizedException(BusinessConstants.TOKEN_VERIFICATION_FAILED);
      }
    } catch (EncryptionFailsException e) {
      LOGGER.error(e.getMessage(), e);
      throw new EncryptionFailsException(AppConstants.INVALID_TOKEN);
    } catch (UnauthorizedException e) {
      throw new UnauthorizedException(BusinessConstants.TOKEN_VERIFICATION_FAILED, e);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new BusinessServiceException(BusinessConstants.TOKEN_VERIFICATION_FAILED, e);
    }

  }

  @Override
  public void doForgotPassword(String email)
      throws BusinessServiceException, UnauthorizedException, UnprocessableEntityException {
    try {
      ValidationUtil.validate("Email", email).required().matches(BusinessConstants.MAIL_PATTERN);
      Employee employee = adminDAO.getUserByEmail(email);

      if (Objects.isNull(employee)) {
        throw new UnauthorizedException(BusinessConstants.INVALID_USER);
      } else if (!employee.getIsActive()) {
        throw new UnauthorizedException(BusinessConstants.USER_DEACTIVATED);
      }
      employee.setPasswordResetRequest(true);
      employee.setPassword(RandomKeyGenerator.getRandomVerificationCode());
      employee.setPasswordSalt(PasswordEncryptionUtils.getInstance().generateSalt());
      employee.setPasswordEncrypt(PasswordEncryptionUtils.getInstance()
          .getEncryptedPassword(employee.getPassword(), employee.getPasswordSalt()));
      LOGGER.info(employee.getPassword());
      String fullName =
          (Objects.nonNull(employee.getFirstName()) ? employee.getFirstName().trim() : "")
              + (Objects.nonNull(employee.getLastName()) ? employee.getLastName().trim() : "");
      employee.setDisplayFullName(fullName);
      adminDAO.updateEmployee(employee);
      /* Email Logic START */
      EmailWrapper emailWrapper = new EmailWrapper();
      List<String> toAddress = new ArrayList<>();
      if (Objects.nonNull(employee.getEmailId())) {
        toAddress.add(employee.getEmailId());
      }
      emailWrapper.setToAddress(toAddress);
      emailWrapper.setToName(fullName);
      emailWrapper.setFromName("RevaturePro");
      emailWrapper.setSubject("RevaturePro | Password reset instructions for your account");
      emailWrapper.setTemplateName("t001-forgotPswFromAdmin.ftl");
      Map<String, Object> templateVars = new HashMap<>();
      templateVars.put("systemUser", employee);
      templateVars.put("linkUrl", PropertiesFileUtils.getValue("project.app.url"));
      templateVars.put("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
      templateVars.put("regardsUrl", PropertiesFileUtils.getValue("email.regards.url"));
      templateVars.put("subject", "RevaturePro | Password reset instructions for your account");
      emailWrapper.setTemplateVars(templateVars);
      EmailUtils.sendEmail(emailWrapper);
      /* Email Logic END */

    } catch (DataRetrievalFailedException e) {
      throw new BusinessServiceException(e.getMessage(), e);
    } catch (UnauthorizedException e) {
      throw new UnauthorizedException(e.getMessage(), e);
    } catch (UnprocessableEntityException e) {
      throw new UnprocessableEntityException(e.getMessage(), e);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      throw new BusinessServiceException(BusinessConstants.SOMETHING_WENT_WRONG, e);
    }
  }

  @Override
  public void resetPassword(SystemUserDTO systemUser)
      throws BusinessServiceException, BusinessConflictException {
    try {
      Employee employee = adminDAO.getUserById(systemUser.getId());
      if (Objects.nonNull(employee)) {
        employee.setPassword(systemUser.getPassword());
        employee.setPasswordSalt(PasswordEncryptionUtils.getInstance().generateSalt());
        employee.setPasswordEncrypt(PasswordEncryptionUtils.getInstance()
            .getEncryptedPassword(employee.getPassword(), employee.getPasswordSalt()));
        if (employee.getPasswordResetRequest()) {
          employee.setPasswordResetRequest(false);
        }
        adminDAO.updateEmployee(employee);
      } else {
        throw new BusinessConflictException(BusinessConstants.INVALID_USER, null);
      }
    } catch (DataRetrievalFailedException | DataUpdationFailedException e) {
      throw new BusinessServiceException(e.getMessage(), null);
    }
  }

}
