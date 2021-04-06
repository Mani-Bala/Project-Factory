package com.revature.project.factory.logging;

import static com.revature.project.factory.logging.MDCConstant.AUDIO;
import static com.revature.project.factory.logging.MDCConstant.CLIENT;
import static com.revature.project.factory.logging.MDCConstant.CONTENT_LENGTH;
import static com.revature.project.factory.logging.MDCConstant.CONTENT_TYPE;
import static com.revature.project.factory.logging.MDCConstant.DEFAULT_MAX_PAYLOAD_LENGTH;
import static com.revature.project.factory.logging.MDCConstant.ELAPSED_TIME;
import static com.revature.project.factory.logging.MDCConstant.EMP_ID;
import static com.revature.project.factory.logging.MDCConstant.HOST;
import static com.revature.project.factory.logging.MDCConstant.HTTP_METHOD;
import static com.revature.project.factory.logging.MDCConstant.HTTP_STATUS;
import static com.revature.project.factory.logging.MDCConstant.IMAGE;
import static com.revature.project.factory.logging.MDCConstant.MULTIPART_FORM_DATA;
import static com.revature.project.factory.logging.MDCConstant.ORG_ID;
import static com.revature.project.factory.logging.MDCConstant.PATH;
import static com.revature.project.factory.logging.MDCConstant.PAYLOAD;
import static com.revature.project.factory.logging.MDCConstant.RESPONSE;
import static com.revature.project.factory.logging.MDCConstant.ROLE_CODE;
import static com.revature.project.factory.logging.MDCConstant.VIDEO;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public class MDCFilter extends OncePerRequestFilter {
  private static final Logger LOGGER = LogManager.getLogger(MDCFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    long time = System.currentTimeMillis();
    // HTTP Method
    MDC.put(HTTP_METHOD,
        Optional.ofNullable(request.getMethod()).map(method -> request.getMethod()).orElse(""));
    // Rest URI
    String requestUri =
        Optional.ofNullable(request.getRequestURI()).map(url -> request.getRequestURI()).orElse("");
    requestUri += Optional.ofNullable(request.getQueryString())
        .map(url -> '?' + request.getQueryString()).orElse("");
    MDC.put(PATH, requestUri);
    // Host Name
    MDC.put(HOST, getHeaderValue(request, HOST));
    if (LOGGER.isDebugEnabled()) {
      // Client IP
      MDC.put(CLIENT, Optional.ofNullable(request.getRemoteAddr())
          .map(client -> request.getRemoteAddr()).orElse(""));
      // content-type
      MDC.put(CONTENT_TYPE, getHeaderValue(request, CONTENT_TYPE));
      // content-length
      MDC.put(CONTENT_LENGTH, getHeaderValue(request, CONTENT_LENGTH));
      // org_id
      MDC.put(ORG_ID, getHeaderValue(request, ORG_ID));
      // emp_id
      MDC.put(EMP_ID, getHeaderValue(request, EMP_ID));
      // role_code
      MDC.put(ROLE_CODE, getHeaderValue(request, ROLE_CODE));
    }

    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

    try {
      filterChain.doFilter(wrappedRequest, wrappedResponse);
    } finally {
      // Request Body
      if (!isMultipart(request) && !isBinaryContent(request)) {
        String requestBody = this.getContentAsString(wrappedRequest.getContentAsByteArray(),
            request.getCharacterEncoding());
        MDC.put(PAYLOAD, Optional.ofNullable(requestBody).filter(req -> req.length() > 0)
            .map(reqBody -> requestBody).orElse(""));
      }
      // Response Body
      byte[] buf = wrappedResponse.getContentAsByteArray();
      MDC.put(RESPONSE, getContentAsString(buf, response.getCharacterEncoding()));
      wrappedResponse.copyBodyToResponse();
      // Elapsed Time
      long duration = System.currentTimeMillis() - time;
      MDC.put(ELAPSED_TIME, Optional.of(duration).map(elapsed -> duration + "ms").orElse(""));
      // HTTP Status Code
      MDC.put(HTTP_STATUS, String.valueOf(
          Optional.of(response.getStatus()).map(status -> response.getStatus()).orElse(null)));
      LOGGER.debug("MDC logging end");
      MDC.clear();
    }
  }

  private boolean isMultipart(final HttpServletRequest request) {
    return request.getContentType() != null
        && request.getContentType().startsWith(MULTIPART_FORM_DATA);
  }

  private boolean isBinaryContent(final HttpServletRequest request) {
    if (request.getContentType() == null) {
      return false;
    }
    return request.getContentType().startsWith(IMAGE) || request.getContentType().startsWith(VIDEO)
        || request.getContentType().startsWith(AUDIO);
  }

  private String getContentAsString(byte[] buf, String charsetName) {
    if (buf == null || buf.length == 0)
      return null;
    int length = Math.min(buf.length, DEFAULT_MAX_PAYLOAD_LENGTH);
    try {
      return new String(buf, 0, length, charsetName).trim().replace("\n", "");
    } catch (UnsupportedEncodingException ex) {
      return "Unsupported Encoding";
    }
  }

  private String getHeaderValue(HttpServletRequest request, String headerKey) {
    return Optional.ofNullable(request.getHeader(headerKey))
        .map(contentType -> request.getHeader(headerKey)).orElse("");
  }
}
