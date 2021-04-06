package com.revature.project.factory.controller.admin;

import static java.util.Optional.ofNullable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.project.factory.controller.constant.ControllerConstants;
import com.revature.project.factory.controller.constant.RestURIConstants;
import com.revature.project.factory.dto.ApplicationInfoDTO;
import com.revature.project.factory.service.responser.HttpStatusResponse;
import com.revature.project.factory.util.ResponseUtils;
import com.revature.project.factory.util.VersionPropertiesFileUtils;

/**
 * <p>
 * Name: ApplicationInfoController<br>
 * Description: Which is used to handle the skill related crud operation rest end point
 * </p>
 */
@RestController
public class ApplicationInfoController {

  private VersionPropertiesFileUtils versionPropertiesFileUtils;

  @Autowired
  public ApplicationInfoController(VersionPropertiesFileUtils versionPropertiesFileUtils) {
    this.versionPropertiesFileUtils = versionPropertiesFileUtils;
  }

  /**
   * Gathers build related information like
   * <ul>
   * <li><b>applicationName</b> - Name of application which is built</li>
   * <li><b>branch</b> - Branch from which code is fetched</li>
   * <li><b>gitHash</b> - Unique identifier for the code commit</li>
   * <li><b>buildTime</b> - Date and time in which build is generated</li>
   * <li><b>appVersion</b> - Version of application which is built</li>
   * </ul>
   * 
   * @return {@link HttpStatusResponse}<{@link ApplicationInfoDTO}>
   */
  @GetMapping(value = RestURIConstants.STATUS_INFO_ENDPOINT)
  public ResponseEntity<HttpStatusResponse> getApplicationInfo() {
    ApplicationInfoDTO applicationInfoDTO = new ApplicationInfoDTO();
    applicationInfoDTO
        .setAppName(ofNullable(versionPropertiesFileUtils.getValue("spring.application.name"))
            .filter(StringUtils::isNotBlank).orElse(null));
    applicationInfoDTO
        .setGitBranch(ofNullable(versionPropertiesFileUtils.getValue("git.branch.name"))
            .filter(StringUtils::isNotBlank).orElse(null));
    applicationInfoDTO.setGitHash(ofNullable(versionPropertiesFileUtils.getValue("git.revision"))
        .filter(StringUtils::isNotBlank).orElse(null));
    applicationInfoDTO
        .setBuildTime(ofNullable(versionPropertiesFileUtils.getValue("app.build.time"))
            .filter(StringUtils::isNotBlank).orElse(null));
    applicationInfoDTO.setBuildNumber(ofNullable(versionPropertiesFileUtils.getValue("app.version"))
        .filter(StringUtils::isNotBlank).orElse(null));
    applicationInfoDTO.setVersion(ofNullable(versionPropertiesFileUtils.getValue("app.release"))
        .filter(StringUtils::isNotBlank).orElse(null));

    return ResponseUtils.prepareSuccessResponse(ControllerConstants.DATA_RETRIEVAL_SUCCESS_MESSAGE,
        applicationInfoDTO, null);
  }
}
