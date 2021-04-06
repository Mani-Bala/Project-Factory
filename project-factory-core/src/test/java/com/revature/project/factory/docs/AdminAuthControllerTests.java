package com.revature.project.factory.docs;

import static com.revature.project.factory.constants.TestConstants.ENCRYPTED_TOKEN;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.inject.Provider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.constants.TestConstants;
import com.revature.project.factory.controller.admin.AdminAuthController;
import com.revature.project.factory.controller.config.UserRequest;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.service.admin.AdminAuthService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*", "javax.net.ssl.*",
    "jdk.internal.reflect.*"})
public class AdminAuthControllerTests {
  @Rule
  public final JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation(TestConstants.REST_SNIPPETS_LOCATION);

  @Mock
  private Provider<UserRequest> requestScopedBeanStub;

  @InjectMocks
  private AdminAuthController adminAuthController;

  @Mock
  private AdminAuthService adminAuthService;

  private final ConstraintDescriptions projectConstraints =
      new ConstraintDescriptions(ProjectDTO.class);

  private ObjectMapper objectMapper = new ObjectMapper();
  private MockMvc mockMvc;

  @Before
  public void setUp() throws UnsupportedEncodingException {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(adminAuthController)
        .apply(documentationConfiguration(this.restDocumentation).uris()
            .withScheme(TestConstants.SCHEME).withHost(TestConstants.DOMAIN_NAME)
            .withPort(TestConstants.PORT))
        .build();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    UserRequest userRequest = new UserRequest();
    userRequest.setEmpId(1L);
    userRequest.setEncryptedToken(ENCRYPTED_TOKEN);
    when(requestScopedBeanStub.get()).thenReturn(userRequest);
  }

  @Test
  public void doLogin() throws Exception {
    SystemUserDTO systemUser = new SystemUserDTO();
    systemUser.setEmailId(getSystemUser().getEmailId());
    systemUser.setPassword(getSystemUser().getPassword());
    systemUser.setIsActive(null);
    Mockito.when(adminAuthService.doAuthenticateAdminLogin(Mockito.any()))
        .thenReturn(getSystemUser());
    this.mockMvc
        .perform(post("/login").contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(systemUser)))
        .andExpect(status().isOk()).andDo(print()).andDo(
            document("login", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
                relaxedRequestFields(
                    fieldWithPath("emailId").type(STRING).description("E-mail Id of the user")
                        .attributes(key("required").value(true)),
                    fieldWithPath("password").type(STRING).description("Password for the user")
                        .attributes(key("required").value(true)))));
  }

  @Test
  public void doForgotPassword() throws Exception {
    SystemUserDTO systemUser = new SystemUserDTO();
    systemUser.setEmailId(getSystemUser().getEmailId());
    systemUser.setIsActive(null);
    Mockito.doNothing().when(adminAuthService).doForgotPassword(Mockito.anyString());
    this.mockMvc
        .perform(post("/forgot-password").contentType(APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(systemUser)))
        .andExpect(status().isOk()).andDo(print()).andDo(
            MockMvcRestDocumentation.document("forgot-password", preprocessRequest(prettyPrint()),
                relaxedRequestFields(fieldWithPath("emailId").type(STRING)
                    .description("Existing Email Id to request the password")
                    .attributes(key("required").value(true)))));
  }

  @Test
  public void doResetPassword() throws Exception {
    SystemUserDTO systemUser = new SystemUserDTO();
    systemUser.setId(getSystemUser().getId());
    systemUser.setPassword(getSystemUser().getPassword());
    systemUser.setIsActive(null);
    Mockito.doNothing().when(adminAuthService).resetPassword(Mockito.any());
    this.mockMvc
        .perform(put("/secure/reset-password").contentType(APPLICATION_JSON)
            .header(AppConstants.ENCRYPTED_TOKEN_HEADER, TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE)
            .content(this.objectMapper.writeValueAsString(systemUser)))
        .andExpect(status().isOk()).andDo(print())
        .andDo(MockMvcRestDocumentation.document("reset-password", preprocessRequest(prettyPrint()),
            relaxedRequestFields(
                fieldWithPath("id").type(NUMBER).description("Id of the user")
                    .attributes(key("required").value(true)),
                fieldWithPath("password").type(STRING).description("New password for reset")
                    .attributes(key("required").value(true)))));
  }

  private SystemUserDTO getSystemUser() {
    SystemUserDTO systemUserDTO = new SystemUserDTO();
    systemUserDTO.setId(2);
    systemUserDTO.setEmailId("user@yopmail.com");
    systemUserDTO.setFirstName("user");
    systemUserDTO.setLastName("name");
    systemUserDTO.setIsActive(true);
    systemUserDTO.setCreatedBy(1L);
    systemUserDTO.setCreatedOn(LocalDateTime.now());
    systemUserDTO.setEncryptedLoginToken("aJDdsAHcnkaIJVDAhbsdSFBKJdjbcBJCDsbcjkSBCDCbdI");
    systemUserDTO.setPassword("Pass123$");
    systemUserDTO.setDisplayFullName("user name");
    systemUserDTO.setTokenExpTime(new Date());
    systemUserDTO.setCreatedOn(LocalDateTime.now());
    return systemUserDTO;
  }
}
