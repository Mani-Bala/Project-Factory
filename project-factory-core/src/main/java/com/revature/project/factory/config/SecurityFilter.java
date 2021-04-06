package com.revature.project.factory.config;

import static com.revature.project.factory.constant.AppConstants.EMP_ID_HEADER;
import static com.revature.project.factory.constant.AppConstants.ENCRYPTED_TOKEN_HEADER;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.dto.TokenDTO;
import com.revature.project.factory.service.exception.EncryptionFailsException;
import com.revature.project.factory.util.AESTokenEncryption;
import com.revature.project.factory.util.ResponseUtils;
import com.revature.project.factory.util.TokenUtils;

@Component
public class SecurityFilter implements Filter {

  @Autowired
  private TokenUtils tokenUtils;

  @Override
  public void destroy() {
    // Nothing for destroy.
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(req);
    String token;
    String encryptedToken = req.getHeader(ENCRYPTED_TOKEN_HEADER);
    if (Objects.nonNull(encryptedToken)) {
      try {
        token = AESTokenEncryption.decrypt(encryptedToken);
        TokenDTO tokenDTO = tokenUtils.getDetailsFromToken(token, true);
        AESTokenEncryption.decrypt(encryptedToken);
        mutableRequest.putHeader(ENCRYPTED_TOKEN_HEADER, encryptedToken);
        mutableRequest.putHeader(EMP_ID_HEADER, tokenDTO.getEmpId().toString());
      } catch (EncryptionFailsException e) {
        ResponseUtils.setResponse(res, AppConstants.INVALID_TOKEN, HttpStatus.UNAUTHORIZED);
      } catch (Exception e) {
        ResponseUtils.setResponse(res, e.getMessage(), HttpStatus.UNAUTHORIZED);
      }
    }
    chain.doFilter(mutableRequest, response);
  }

  @Override
  public void init(FilterConfig filterConfig) {
    // Nothing for initialization.
  }
}
