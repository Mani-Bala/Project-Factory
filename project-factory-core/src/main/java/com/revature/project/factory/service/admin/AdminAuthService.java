package com.revature.project.factory.service.admin;

import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.dto.TokenDTO;
import com.revature.project.factory.exception.UnprocessableEntityException;
import com.revature.project.factory.service.exception.AuthenticationFailsException;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.EncryptionFailsException;
import com.revature.project.factory.service.exception.UnauthorizedException;

public interface AdminAuthService {
  /**
   * Authenticates the provided login credentials and returns the system user object along with the
   * token details needed for accessing the microservices
   *
   * @param systemUser
   * @return {@link SystemUserDTO}
   * @throws AuthenticationFailsException
   * @throws BusinessServiceException
   * @throws UnauthorizedException
   */
  SystemUserDTO doAuthenticateAdminLogin(SystemUserDTO systemUser)
      throws AuthenticationFailsException, BusinessServiceException, UnauthorizedException,
      UnprocessableEntityException;

  /**
   * Used to verify the token and decodes the payload of the JWT token
   *
   * @return {@link TokenDTO}
   * @throws EncryptionFailsException
   * @throws BusinessServiceException
   * @throws UnauthorizedException
   */
  TokenDTO doDecodeToken(String encryptedToken)
      throws BusinessServiceException, EncryptionFailsException, UnauthorizedException;

  /**
   * Forgot password service resets the password and salt and sends mail notification to the
   * employees mail regarding the change
   *
   * @param email
   * @throws UnauthorizedException
   */
  void doForgotPassword(String email)
      throws BusinessServiceException, UnauthorizedException, UnprocessableEntityException;

  void resetPassword(SystemUserDTO systemUser)
      throws BusinessConflictException, BusinessServiceException;
}
