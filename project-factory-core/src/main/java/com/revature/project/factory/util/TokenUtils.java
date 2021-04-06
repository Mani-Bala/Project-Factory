package com.revature.project.factory.util;


import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.dto.TokenDTO;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.exception.UnauthorizedException;

@Component
public class TokenUtils {

  private static final Logger LOGGER = LogManager.getLogger(TokenUtils.class);
  private static final String ISSUER = "RevaturePro";
  private static final String USER_NAME = "email";

  private final String signingKey;
  private final int minutesToLive;
  private final JWTVerifier verifier;

  public TokenUtils() throws UnsupportedEncodingException {
    this.signingKey = PropertiesFileUtils.getValue("security.jwt.signing.key");
    String minToLive = PropertiesFileUtils.getValue("security.jwt.minutes");
    this.minutesToLive = minToLive == null ? 0 : Integer.parseInt(minToLive);
    this.verifier = JWT.require(Algorithm.HMAC256(signingKey)).withIssuer(ISSUER).build();
  }

  public String doGetToken(SystemUserDTO user) throws BusinessServiceException {
    if (Objects.isNull(user) || Objects.isNull(user.getEmailId())) {
      throw new BusinessServiceException("Invalid parameters", null);
    }

    LOGGER.info("Getting into token generate method for the user = {} ", user.getEmailId());
    String token = null;
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.MINUTE, minutesToLive);
    Date expiredOn = calendar.getTime();
    try {
      token = JWT.create().withIssuer(ISSUER) // issuer
          .withJWTId(String.valueOf(user.getId())) // JWT Id
          .withClaim(USER_NAME, user.getEmailId()) // User Name
          .withExpiresAt(expiredOn) // Expired ON
          .sign(Algorithm.HMAC256(signingKey)); // Symmetric key

    } catch (UnsupportedEncodingException e) {
      LOGGER.error(e.getMessage(), e);
      throw new BusinessServiceException("Token Generate fails", e);
    }
    LOGGER.info("Token generated successfully for user = {}", user.getEmailId());
    LOGGER.debug("Token generated successfully for user = {}, token = {} ", user.getEmailId(),
        token);
    return token;
  }

  public DecodedJWT decodeToken(String token) throws UnauthorizedException {
    try {
      return verifier.verify(token);
    } catch (JWTDecodeException e) {
      throw new UnauthorizedException("Invalid token");
    }
  }

  public DecodedJWT decodeWithoutVerification(String token) throws UnauthorizedException {
    try {
      return JWT.decode(token);
    } catch (JWTDecodeException e) {
      throw new UnauthorizedException("Invalid token");
    }
  }

  public TokenDTO getDetailsFromToken(String token, boolean shouldVerify)
      throws UnauthorizedException {

    DecodedJWT decodedJWT = null;
    if (shouldVerify) {
      decodedJWT = decodeToken(token);
    } else {
      decodedJWT = decodeWithoutVerification(token);
    }
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setUserName(decodedJWT.getClaim(USER_NAME).asString());
    tokenDTO.setEmpId(NumberUtils.createLong(decodedJWT.getId()));
    tokenDTO.setExpiresAt(decodedJWT.getExpiresAt());
    return tokenDTO;
  }

}
