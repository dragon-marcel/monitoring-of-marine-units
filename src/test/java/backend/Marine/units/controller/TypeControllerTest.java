package backend.Marine.units.controller;

import backend.Marine.units.entity.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
@Transactional
public class TypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_get_all_type_of_ships() throws Exception {
        // given

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
              //  .andExpect(MockMvcResultMatchers.jsonPath("$id", Matchers.contains(1))
                .andReturn();

        // then
        List<Type> shipTypes = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),new TypeReference<List<Type>>() {});
        Assertions.assertFalse(shipTypes.isEmpty());
        assertEquals(shipTypes.get(24).getDescription(),"High speed craft (HSC), Hazardous category C");
        assertEquals(shipTypes.get(24).getId(),(Integer)43);
        assertThat(shipTypes, Matchers.hasSize(81));
    }
}