package com.revature.project.factory.config;

import static com.revature.project.factory.constant.AppConstants.EMP_ID_HEADER;
import static com.revature.project.factory.service.responser.HttpStatusResponse.setHttpResponse;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.dao.AdminDAO;
import com.revature.project.factory.model.Employee;
import com.revature.project.factory.service.exception.UnauthorizedException;
import com.revature.project.factory.service.responser.HttpStatusResponse;
import com.revature.project.factory.util.ResponseUtils;

/**
 * <p>
 * Name: VideoInterceptor<br>
 * Description: Which is used to handle the video related request and response
 * </p>
 */
@Component
class ProjectInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private AdminDAO adminDAO;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if (!HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
      Long empId = NumberUtils.createLong(request.getHeader(EMP_ID_HEADER));
      if (Objects.nonNull(empId)) {
        try {
          Employee employee = adminDAO.getUserById(empId);
          if (Objects.nonNull(employee)) {
            return true;
          } else {
            throw new UnauthorizedException(AppConstants.TOKEN_VERIFICATION_FAILED);
          }
        } catch (Exception e) {
          ResponseUtils.setResponse(response, e.getMessage(), HttpStatus.UNAUTHORIZED);
          return false;
        }
      }
      ResponseUtils.setResponse(response, AppConstants.TOKEN_REQUIRED, HttpStatus.BAD_REQUEST);

    }
    return true;
  }

  /**
   * This method is use to converts a {@link HttpStatusResponse} to JSON and writes to the
   * HttpServletResponse.
   */
  private void setResponse(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(UNAUTHORIZED.value());
    HttpStatusResponse httpStatusResponse = setHttpResponse(UNAUTHORIZED.value(),
        AppConstants.ACCESS_DENIED_TO_THE_RESOURCE, null, null);
    String res = new Gson().toJson(httpStatusResponse);
    response.getWriter().println(res);
  }
}
