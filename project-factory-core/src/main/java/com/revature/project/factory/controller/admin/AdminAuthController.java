package com.revature.project.factory.controller.admin;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.project.factory.controller.constant.ControllerConstants;
import com.revature.project.factory.controller.constant.RestURIConstants;
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.exception.UnprocessableEntityException;
import com.revature.project.factory.service.admin.AdminAuthService;
import com.revature.project.factory.service.exception.AuthenticationFailsException;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.UnauthorizedException;
import com.revature.project.factory.service.responser.HttpStatusResponse;
import com.revature.project.factory.util.ResponseUtils;

/**
 * <p>
 * Name: LoginController<br>
 * Description: Which is used to handle the Authenticate user and to access Project enpoints
 * </p>
 */
@RestController
public class AdminAuthController {

  private static final Logger LOGGER = LogManager.getLogger(AdminAuthController.class);

  @Autowired
  private AdminAuthService adminAuthService;

  /**
   * This service authenticates the login credentials with user name and password. The following are
   * the mandatory fields
   * <ul>
   * <li>UserName</li>
   * <li>Password</li>
   * <li>TimeZone</li>
   * </ul>
   *
   * @param systemUser
   * @return {@link SystemUserDTO}
   * @throws Exception
   */
  @PostMapping(value = RestURIConstants.LOGIN_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doLogin(@RequestBody SystemUserDTO systemUser) {
    LOGGER.info("Getting into user login Controller :: userName = {} ", systemUser.getEmailId());
    try {
      SystemUserDTO user = adminAuthService.doAuthenticateAdminLogin(systemUser);
      return ResponseUtils.prepareSuccessResponse(ControllerConstants.AUTHENTICATION_SUCCESS, user,
          null);
    } catch (AuthenticationFailsException | UnauthorizedException
        | UnprocessableEntityException e) {
      return ResponseUtils.prepareUnAuthorizedResponse(e.getMessage());
    } catch (BusinessServiceException e) {
      return ResponseUtils.prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * This endpoint resets the admin password and sends a temporary password as a email notification
   * to the user
   *
   * @param systemUser
   * @return
   */
  @PostMapping(value = RestURIConstants.FORGOT_PASS_WORD_ENDPOINT,
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doForgotPassword(
      @RequestBody SystemUserDTO systemUser) {
    LOGGER.info("Getting into user :: userName = {} ", systemUser.getEmailId());
    try {
      adminAuthService.doForgotPassword(systemUser.getEmailId());
      return ResponseUtils.prepareSuccessResponse(ControllerConstants.PASSWORD_INST_SENT, null,
          null);
    } catch (UnauthorizedException | UnprocessableEntityException e) {
      return ResponseUtils.prepareUnAuthorizedResponse(e.getMessage());
    } catch (BusinessServiceException e) {
      return ResponseUtils.prepareInternalServerErrorResponse(e.getMessage());
    }
  }

  /**
   * This endpoint resets the admin password and sends a temporary password as a email notification
   * to the user
   *
   * @param systemUser
   * @return
   */
  @PutMapping(value = RestURIConstants.RESET_PASSWORD, consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<HttpStatusResponse> doResetPassword(@RequestBody SystemUserDTO systemUser) {
    LOGGER.info("Getting into user :: userId = {} ", systemUser.getId());
    try {
      adminAuthService.resetPassword(systemUser);
      return ResponseUtils.prepareSuccessResponse(ControllerConstants.PASSWORD_RESET_SUCCESS, null,
          null);
    } catch (BusinessServiceException e) {
      return ResponseUtils.prepareInternalServerErrorResponse(e.getMessage());
    } catch (BusinessConflictException e) {
      return ResponseUtils.prepareConflictResponse(e.getMessage(), null);
    }
  }

}
