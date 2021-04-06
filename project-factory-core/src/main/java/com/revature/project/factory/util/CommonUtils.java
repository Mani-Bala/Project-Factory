package com.revature.project.factory.util;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;

import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.controller.config.UserRequest;

public class CommonUtils {

  private CommonUtils() {}

  public static void setRequestValues(HttpServletRequest request,
      Provider<UserRequest> requestScopedBean) {
    requestScopedBean.get()
        .setEmpId(Optional
            .ofNullable(NumberUtils.createLong(request.getHeader(AppConstants.EMP_ID_HEADER)))
            .map(Long::valueOf).orElse(null));
    requestScopedBean.get().setEncryptedToken(
        Optional.ofNullable(request.getHeader(AppConstants.ENCRYPTED_TOKEN_HEADER)).orElse(null));
  }

  public static boolean patternValidator(String value, String patternString) {
    Pattern pattern = Pattern.compile(patternString);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

}
