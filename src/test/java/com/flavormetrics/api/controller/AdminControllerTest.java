package com.flavormetrics.api.controller;

import com.flavormetrics.api.entity.Email;
import com.flavormetrics.api.entity.user.impl.Nutritionist;
import com.flavormetrics.api.entity.user.impl.RegularUser;
import com.flavormetrics.api.model.user.UserDto;
import com.flavormetrics.api.model.user.impl.NutritionistDto;
import com.flavormetrics.api.model.user.impl.RegularUserDto;
import com.flavormetrics.api.service.UserService;
import com.flavormetrics.api.util.ModelConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.flavormetrics.api.model.Data.body;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "mock", roles = "ADMIN")
    void getAllRegularUsers_WithAdminRole() throws Exception {
        final RegularUser user = new RegularUser("mockpassword", new Email("mock@email"));
        final List<RegularUserDto> mockUsers = List.of(
                ModelConverter.toRegularUserDto(user)
        );
        when(userService.getAllRegularUsers()).thenReturn(body(mockUsers));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/regular")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "mock", roles = "USER")
    void getAllRegularUsers_WithRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/regular")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "mock", roles = "ADMIN")
    void getAllUsers_WithAdminRole() throws Exception {
        final RegularUser user = new RegularUser("mockpassword", new Email("mock@email"));
        final List<UserDto> mockUsers = List.of(
                ModelConverter.toRegularUserDto(user)
        );
        when(userService.getAllUsers()).thenReturn(body(mockUsers));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/all")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "mock", roles = "USER")
    void getAllUsers_WithRoleUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/all")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "mock", roles = "ADMIN")
    void getAllNutritionistUsers_WithAdminRole() throws Exception {
        final Nutritionist user = new Nutritionist("mockpassword", new Email("mock@email"));
        final List<NutritionistDto> mockUsers = List.of(
                ModelConverter.toNutritionistDto(user)
        );
        when(userService.getAllNutritionistUsers()).thenReturn(body(mockUsers));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/nutritionist")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "mock", roles = "NUTRITIONIST")
    void getAllNutritionistUsers_WithRoleNutritionist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/nutritionist")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}