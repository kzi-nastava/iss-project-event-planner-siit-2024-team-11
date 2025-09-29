package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.eventy.EventyApplication;
import org.example.eventy.users.dtos.LoginDTO;
import org.example.eventy.users.dtos.UserTokenState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EventyApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BudgetControllerTest {

    private String jwtToken;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setupTestData() throws Exception {
        login("ves@gmail.com");
    }

    @AfterAll
    void tearDownTestData() throws Exception {
        logout();
    }

    private void login(String email) throws Exception {
        LoginDTO loginDTO = new LoginDTO(email, "admin");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/authentication/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        UserTokenState tokenState = objectMapper.readValue(responseContent, UserTokenState.class);
        jwtToken = tokenState.getAccessToken();
    }

    private void logout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/authentication/logout")
                        .header("Authorization", "Bearer " + jwtToken)  // Include token
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        jwtToken = null;
    }

    @Test
    @Transactional
    void getBudget_AllValid_ReturnsBudget() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/budget/3")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void getBudget_EventWhichIsNotYours_ReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/budget/2")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void createBudgetItem_AllValid_ReturnsBudgetItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/budget/3/item/3")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isCreated());
    }

    @Test
    @Transactional
    void createBudgetItem_EventWhichIsNotYours_ReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/budget/2/item/1")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void createBudgetItem_SolutionCategoryIDInvalid_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/budget/3/item/5")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void removeBudgetItem_AllValid_ReturnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/budget/3/item/4")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void removeBudgetItem_EventWhichIsNotYours_ReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/budget/2/item/1")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void removeBudgetItem_BudgetItemHasBudgetedItems_ReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/budget/3/item/3")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void updateBudgetItemFunds_AllValid_ReturnsBudgetItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/budget/item/3")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isOk());
    }

    @Test
    @Transactional
    void updateBudgetItemFunds_EventWhichIsNotYours_ReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/budget/item/1")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void updateBudgetItemFunds_BudgetItemNotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/budget/item/9")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(1000.0))
        ).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void removeBudgetItemSolution_AllValid_ReturnsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/budget/item/3/solution/6")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    void removeBudgetItemSolution_EventWhichIsNotYours_ReturnsForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/budget/item/1/solution/6")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isForbidden());
    }

    @Test
    @Transactional
    void removeBudgetItemSolution_BudgetItemSolutionNotFound_ReturnsNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/budget/item/3/solution/1")
                .header("Authorization", "Bearer " + jwtToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isNotFound());
    }
}
