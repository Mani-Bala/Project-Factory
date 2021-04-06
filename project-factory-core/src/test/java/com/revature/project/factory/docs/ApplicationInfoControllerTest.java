package com.revature.project.factory.docs;

import static com.revature.project.factory.controller.constant.RestURIConstants.STATUS_INFO_ENDPOINT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.revature.project.factory.constants.TestConstants;
import com.revature.project.factory.controller.admin.ApplicationInfoController;
import com.revature.project.factory.util.VersionPropertiesFileUtils;

/**
 * <p>
 * Name: ApplicationInfoControllerTest<br>
 * Description: Which is used to handle the project-factory health check rest end point
 * documentation
 * </p>
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*"})
public class ApplicationInfoControllerTest {

  @Rule
  public final JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation("target/generated-snippets");

  @InjectMocks
  private ApplicationInfoController applicationInfoController;

  @Mock
  private VersionPropertiesFileUtils versionPropertiesFileUtils;

  private MockMvc mockMvc;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(applicationInfoController)
        .apply(documentationConfiguration(this.restDocumentation).uris()
            .withScheme(TestConstants.SCHEME).withHost(TestConstants.DOMAIN_NAME)
            .withPort(TestConstants.PORT))
        .build();

    PowerMockito.when(versionPropertiesFileUtils.getValue("spring.application.name"))
        .thenReturn("project-factory");
    PowerMockito.when(versionPropertiesFileUtils.getValue("git.branch.name"))
        .thenReturn("origin/Dev");
    PowerMockito.when(versionPropertiesFileUtils.getValue("git.revision"))
        .thenReturn("94015a5e6da0d0f7d8ffe3c39516926bdeda3e98");
    PowerMockito.when(versionPropertiesFileUtils.getValue("app.build.time"))
        .thenReturn("2019-08-29T14:17:51.311");
    PowerMockito.when(versionPropertiesFileUtils.getValue("app.version")).thenReturn("32");
    PowerMockito.when(versionPropertiesFileUtils.getValue("app.release")).thenReturn("1.0");
  }

  /**
   * To generate rest doc for project-factory health check end point
   */
  @Test
  public void getAppHealthStatusExpectSuccess() throws Exception {
    this.mockMvc.perform(get(STATUS_INFO_ENDPOINT).contentType(APPLICATION_JSON))
        .andExpect(status().isOk()).andDo(print()).andDo(document("project-factory-getHealthStatus",
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }
}
