package backend.Marine.units.controller;

import backend.Marine.units.entity.AuthRequest;
import backend.Marine.units.entity.AuthResponse;
import backend.Marine.units.jwt.JWTTokenFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@Transactional
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    JWTTokenFilter jwtTokenFilter;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_return_auth_token() throws Exception {
        // given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("admin");
        authRequest.setUsername("admin");

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        // then
        AuthResponse auth = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<AuthResponse>() {
        });
        String token = auth.getToken();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtTokenFilter.getUsernamePasswordAuthenticationToken(token);
        String principal = usernamePasswordAuthenticationToken.getPrincipal().toString();
        Collection<GrantedAuthority> authorities = usernamePasswordAuthenticationToken.getAuthorities();
        GrantedAuthority grantedAuthority = authorities.stream().findFirst().get();

        Assertions.assertNotNull(token);
        assertEquals("admin", principal);
        assertEquals("ROLE_ADMIN", grantedAuthority.getAuthority());
        assertThat(authorities, Matchers.hasSize(1));
    }

    @Test
    public void should_return_401_when_incorret_auth() throws Exception {
        // given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setPassword("admin");
        authRequest.setUsername("adminn");

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/auth")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(authRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(401)).andReturn();

        // then
        JSONObject error = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<JSONObject>() {
        });
        String message = error.getAsString("message");
        String status = error.getAsString("status");

        Assertions.assertNotNull(error);
        assertEquals("UNAUTHORIZED", status);
        assertEquals("Error,incorrect password or username", message);
    }
}
