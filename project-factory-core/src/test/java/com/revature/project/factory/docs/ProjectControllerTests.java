package com.revature.project.factory.docs;

import static com.revature.project.factory.constants.TestConstants.ENCRYPTED_TOKEN;
import static com.revature.project.factory.constants.TestConstants.REQUIRED;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project.factory.constant.AppConstants;
import com.revature.project.factory.constants.TestConstants;
import com.revature.project.factory.controller.admin.ProjectController;
import com.revature.project.factory.controller.config.UserRequest;
import com.revature.project.factory.dto.CategoryDTO;
import com.revature.project.factory.dto.MetadataDTO;
import com.revature.project.factory.dto.ProjectDTO;
import com.revature.project.factory.dto.ProjectSkillDTO;
import com.revature.project.factory.dto.SkillDTO;
import com.revature.project.factory.dto.SystemUserDTO;
import com.revature.project.factory.service.admin.ProjectService;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*",
    "org.w3c.dom.*", "com.sun.org.apache.xalan.*", "javax.activation.*", "javax.net.ssl.*",
    "jdk.internal.reflect.*"})
public class ProjectControllerTests {
  @Rule
  public final JUnitRestDocumentation restDocumentation =
      new JUnitRestDocumentation(TestConstants.REST_SNIPPETS_LOCATION);

  @Mock
  private Provider<UserRequest> requestScopedBeanStub;

  @InjectMocks
  private ProjectController projectController;
  @Mock
  private ProjectService projectService;

  private final ConstraintDescriptions projectConstraints =
      new ConstraintDescriptions(ProjectDTO.class);

  private ObjectMapper objectMapper = new ObjectMapper();
  private MockMvc mockMvc;

  @Before
  public void setUp() throws UnsupportedEncodingException {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(projectController)
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
  public void getAllProjects() throws Exception {
    when(projectService.doGetAllProjects(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(),
        Mockito.anyString(), Mockito.anyBoolean())).thenReturn(getProjectDTOListForPagination());

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("page", "1");
    params.add("size", "10");
    params.add("orderBy", "name");
    params.add("sortOrder", "desc");
    params.add("status", "true");

    this.mockMvc
        .perform(
            get("/secure/projects").params(params)
                .header(AppConstants.ENCRYPTED_TOKEN_HEADER,
                    TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk()).andDo(print())
        .andDo(document("get-projects", preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestParameters(
                parameterWithName("page").description(
                    "No of the page by which projects to be fetched")
                    .attributes(key("type").value(NUMBER), key(REQUIRED).value(false)),
                parameterWithName("size")
                    .description("Size of the page data by which projects to be fetched")
                    .attributes(key("type").value(NUMBER), key(REQUIRED).value(false)),
                parameterWithName("orderBy").description(
                    "Order by of the column name by which projects to be fetched. Allowed field are: {id}, {name}.")
                    .attributes(key("type").value(STRING), key(REQUIRED).value(false)),
                parameterWithName("sortOrder").description(
                    "Sort order of the column name by which projects to be fetched. Possible input: asc or desc.")
                    .attributes(key("type").value(STRING), key(REQUIRED).value(false)),
                parameterWithName("status")
                    .description("Status of the projects by which it has to be fetched")
                    .attributes(key("type").value(BOOLEAN), key(REQUIRED).value(false)))));
  }

  @Test
  public void getProjectById() throws Exception {
    when(projectService.doGetProjectById(1L)).thenReturn(getProjectDTO());
    this.mockMvc
        .perform(get("/secure/project/" + "{projectId}", 1L).contentType(APPLICATION_JSON)
            .header(AppConstants.ENCRYPTED_TOKEN_HEADER, TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE))
        .andExpect(status().isOk()).andDo(print())
        .andDo(MockMvcRestDocumentation.document("get-project", preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("projectId")
                .attributes(key("type").value("Number"))
                .description("Id of the Project to be retrieved"))));
  }

  @Test
  public void doSaveProjectExpectSuccess() throws Exception {
    Mockito.doNothing().when(projectService).doSaveProject(Mockito.any());
    this.mockMvc
        .perform(post("/secure/project").contentType(APPLICATION_JSON)
            .header(AppConstants.ENCRYPTED_TOKEN_HEADER, TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE)
            .content(this.objectMapper.writeValueAsString(getProjectDTOForSave())))
        .andExpect(status().isOk()).andDo(print())
        .andDo(document("save-project", preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            relaxedRequestFields(
                fieldWithPath("name").type(STRING).description("Name of the Project").attributes(
                    key("constraints")
                        .value(projectConstraints.descriptionsForProperty("projectName")),
                    key("required").value(true)),
                fieldWithPath("category.id").type(NUMBER).description("category id of the Project")
                    .attributes(key("required").value(true)),
                fieldWithPath("description").type(STRING).description("Project description")
                    .attributes(
                        key("constraints")
                            .value(projectConstraints.descriptionsForProperty("description")),
                        key("required").value(false)),
                fieldWithPath("projectSkills[].skill.id").type(NUMBER)
                    .description("skill id of Project").attributes(key("required").value(false)),
                fieldWithPath("projectSkills[].score").type(NUMBER)
                    .description("Project skill score").attributes(key("required").value(false)))));
  }

  @Test
  public void deleteProjectExpectSuccess() throws Exception {
    Mockito.doNothing().when(projectService).doDeleteProject(Mockito.anyLong());
    this.mockMvc
        .perform(RestDocumentationRequestBuilders.delete("/secure/project/" + "{projectId}", 1L)
            .contentType(APPLICATION_JSON)
            .header(AppConstants.ENCRYPTED_TOKEN_HEADER, TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE))
        .andExpect(status().isOk()).andDo(print())
        .andDo(MockMvcRestDocumentation.document("delete-project", preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("projectId")
                .attributes(key("type").value("Number"))
                .description("Id of the Project to be deleted"))));
  }

  @Test
  public void activateProjectExpectSuccess() throws Exception {
    Mockito.doNothing().when(projectService).doActivateOrDeactivateProject(Mockito.anyLong(),
        Mockito.anyBoolean());
    this.mockMvc
        .perform(RestDocumentationRequestBuilders.patch("/secure/project/" + "{projectId}", 1L)
            .param("status", "true").contentType(APPLICATION_JSON)
            .header(AppConstants.ENCRYPTED_TOKEN_HEADER, TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE))
        .andExpect(status().isOk()).andDo(print())
        .andDo(MockMvcRestDocumentation.document("activate-project",
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()),
            RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("projectId")
                .attributes(key("type").value("Number"))
                .description("Id of the Project to be activated")),
            requestParameters(
                parameterWithName("status").description("status of the project to be changed")
                    .attributes(key("required").value(true), key("type").value("String")))));
  }

  @Test
  public void getProjectMetaData() throws Exception {
    when(projectService.getMetadata(Mockito.anyBoolean())).thenReturn(getMetadataDTO());
    this.mockMvc
        .perform(get("/secure/metadata").contentType(APPLICATION_JSON)
            .header(AppConstants.ENCRYPTED_TOKEN_HEADER, TestConstants.ENCRYPTED_TOKEN_MOCK_VALUE))
        .andExpect(status().isOk()).andDo(print())
        .andDo(MockMvcRestDocumentation.document("get-projectMetadata",
            preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint())));
  }

  private List<ProjectDTO> getProjectDTOListForPagination() {
    List<ProjectDTO> projectDTOS = new ArrayList<>();
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setId(1);
    projectDTO.setIsActive(true);
    projectDTO.setEmployeeName("user name");
    projectDTO.setName("Project-1");
    projectDTO.setCategoryName("Java Category");
    projectDTOS.add(projectDTO);
    projectDTO = new ProjectDTO();
    projectDTO.setId(2);
    projectDTO.setIsActive(true);
    projectDTO.setEmployeeName("user name");
    projectDTO.setName("Project-2");
    projectDTO.setCategoryName("Java Category");
    projectDTOS.add(projectDTO);
    return projectDTOS;
  }

  private ProjectDTO getProjectDTO() {
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setId(1);
    projectDTO.setName("Project Name");
    projectDTO.setDescription("Project Description");
    projectDTO.setIsActive(true);
    projectDTO.setCategory(getCategoryDTO());
    projectDTO.setCreatedBy(getSystemUserDTO());
    projectDTO.setCreatedOn(LocalDateTime.now().minusDays(3));
    projectDTO.setModifiedBy(getSystemUserDTO());
    projectDTO.setModifiedOn(LocalDateTime.now());
    projectDTO.setProjectSkills(getProjectSkillDTOList());
    return projectDTO;
  }

  private SystemUserDTO getSystemUserDTO() {
    SystemUserDTO systemUserDTO = new SystemUserDTO();
    systemUserDTO.setId(2);
    systemUserDTO.setEmailId("user@yopmail.com");
    systemUserDTO.setFirstName("user");
    systemUserDTO.setLastName("name");
    systemUserDTO.setIsActive(true);
    systemUserDTO.setCreatedBy(1L);
    systemUserDTO.setCreatedOn(LocalDateTime.now());
    return systemUserDTO;
  }

  private MetadataDTO getMetadataDTO() {
    MetadataDTO metadataDTO = new MetadataDTO();
    CategoryDTO categoryDTO = getCategoryDTO();
    categoryDTO.setCreatedOn(null);
    categoryDTO.setCreatedBy(null);
    metadataDTO.setCategoryDTOS(Collections.singletonList(categoryDTO));
    SkillDTO skillDTO = getSkillDTO();
    skillDTO.setCreatedOn(null);
    skillDTO.setCreatedBy(null);
    metadataDTO.setSkillDTOS(Collections.singletonList(skillDTO));
    return metadataDTO;
  }

  private CategoryDTO getCategoryDTO() {
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(1);
    categoryDTO.setName("category");
    categoryDTO.setIsActive(true);
    categoryDTO.setCreatedBy(getSystemUserDTO());
    categoryDTO.setCreatedOn(LocalDateTime.now());
    return categoryDTO;
  }

  private List<ProjectSkillDTO> getProjectSkillDTOList() {
    List<ProjectSkillDTO> projectSkillDTOS = new ArrayList<>();
    projectSkillDTOS.add(getProjectSkillDTO());
    return projectSkillDTOS;
  }

  private ProjectSkillDTO getProjectSkillDTO() {
    ProjectSkillDTO projectSkillDTO = new ProjectSkillDTO();
    projectSkillDTO.setId(1);
    projectSkillDTO.setScore(70);
    projectSkillDTO.setSkill(getSkillDTO());
    return projectSkillDTO;
  }

  private SkillDTO getSkillDTO() {
    SkillDTO skillDTO = new SkillDTO();
    skillDTO.setId(1);
    skillDTO.setName("skill");
    skillDTO.setScore(80);
    skillDTO.setIsActive(true);
    skillDTO.setCreatedBy(getSystemUserDTO());
    skillDTO.setCreatedOn(LocalDateTime.now().minusWeeks(1));
    return skillDTO;
  }

  private ProjectDTO getProjectDTOForSave() {
    ProjectDTO projectDTO = new ProjectDTO();
    projectDTO.setName("project-1");
    projectDTO.setDescription("description");
    projectDTO.setIsActive(null);
    CategoryDTO categoryDTO = new CategoryDTO();
    categoryDTO.setId(1);
    categoryDTO.setName("category");
    categoryDTO.setIsActive(null);
    projectDTO.setCategory(categoryDTO);
    List<ProjectSkillDTO> projectSkillDTOList = new ArrayList<>();
    ProjectSkillDTO projectSkillDTO = new ProjectSkillDTO();
    SkillDTO skillDTO = new SkillDTO(1L);
    skillDTO.setIsActive(null);
    projectSkillDTO.setScore(85);
    projectSkillDTO.setSkill(skillDTO);
    projectSkillDTOList.add(projectSkillDTO);
    projectDTO.setProjectSkills(projectSkillDTOList);
    return projectDTO;
  }
}
