package backend.Marine.units.controller;

import backend.Marine.units.dto.UserDTO;
import backend.Marine.units.entity.Role;
import backend.Marine.units.model.ErrorAPI;
import backend.Marine.units.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        // userService = mock(UserService.class);
    }

    @Test
    @WithMockUser
    public void should_return_userDTO_when_get_by_correct_id() throws Exception {
        // given
        int id = 1;
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();


        // then
        UserDTO user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<UserDTO>() {
        });
        Assertions.assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("dragon.marcel@o2.pl", user.getEmail());

    }

    @Test
    @WithMockUser
    public void should_return_error_when_get_userDTO_by_incorrect_id() throws Exception {
        // given
        int id = 10;

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(404)).andReturn();

        // then
        ErrorAPI error = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<ErrorAPI>() {
        });
        Assertions.assertNotNull(error);
        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        assertEquals("User not found by id: 10", error.getMessage());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testSetEnabledUserWithUserRole() throws Exception {
        //given
        int userId = 1;
        boolean enabled = true;

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/{id}/{enabled}", userId, enabled)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(403));

        //then
        Mockito.verify(userService, never()).setEnabled(userId, enabled);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testSetEnabledUserWithAdminRole() throws Exception {
        //given
        int userId = 1;
        boolean enabled = true;

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/{id}/{enabled}", userId, enabled)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));

        //then
        Mockito.verify(userService, times(1)).setEnabled(userId, enabled);
    }

    @Test
    @WithMockUser()
    public void should_return_all_users() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        //then
        List<Role> roles = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Role>>() {
        });
        String role = roles.get(0).getAuthority();
        assertEquals("ROLE_ADMIN", role);
        assertThat(roles, Matchers.hasSize(2));

    }

}
