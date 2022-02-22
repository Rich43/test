package com.example.test.controller;

import com.example.test.dto.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final Logger log = getLogger(CustomerControllerIT.class);

    @BeforeEach
    public void setUp() throws Exception {
        log.info("start setUp");
        final var mvcResult = mockMvc.perform(get("/customer")).andReturn();
        final var contentAsString = mvcResult.getResponse().getContentAsString();
        final var customers = new ObjectMapper().readValue(contentAsString, Customer[].class);
        for (final var customer : customers) {
            log.info("setUp: delete customer: {}", customer);
            mockMvc.perform(delete("/customer/" + customer.id())).andDo(print());
        }
        log.info("end setUp");
    }

    @Test
    public void getAllCustomersShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[]"));
    }

    @Test
    public void whenCreatingACustomerGetAllCustomersShouldReturnOneCustomer() throws Exception {
        mockMvc.perform(
                        post("/customer")
                                .content("{\"firstname\":\"John\",\"surname\":\"Doe\"}")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"id\":1,\"firstname\":\"John\",\"surname\":\"Doe\"}"));
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[{\"id\":1,\"firstname\":\"John\",\"surname\":\"Doe\"}]"));
    }

    @Test
    public void whenCreatingACustomerGetAllCustomersShouldReturnOneCustomerAndThenAfterDeletingGetAllCustomersShouldReturnZeroCustomers() throws Exception {
        mockMvc.perform(
                        post("/customer")
                                .content("{\"firstname\":\"John\",\"surname\":\"Doe\"}")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"id\":1,\"firstname\":\"John\",\"surname\":\"Doe\"}"));
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[{\"id\":1,\"firstname\":\"John\",\"surname\":\"Doe\"}]"));
        mockMvc.perform(delete("/customer/1")).andDo(print());
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[]"));
    }


    @Test
    public void whenCreatingATwoCustomersGetAllCustomersAndDeletingTheFirstOneAndCreatingANewOneCorrectIdShouldBeUsed() throws Exception {
        mockMvc.perform(
                        post("/customer")
                                .content("{\"firstname\":\"John\",\"surname\":\"Doe\"}")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"id\":1,\"firstname\":\"John\",\"surname\":\"Doe\"}"));
        mockMvc.perform(
                        post("/customer")
                                .content("{\"firstname\":\"Jane\",\"surname\":\"Smith\"}")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"id\":2,\"firstname\":\"Jane\",\"surname\":\"Smith\"}"));
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[{\"id\":1,\"firstname\":\"John\",\"surname\":\"Doe\"}, {\"id\":2,\"firstname\":\"Jane\",\"surname\":\"Smith\"}]"));
        mockMvc.perform(delete("/customer/1")).andDo(print());
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[{\"id\":2,\"firstname\":\"Jane\",\"surname\":\"Smith\"}]"));
        mockMvc.perform(
                        post("/customer")
                                .content("{\"firstname\":\"Jack\",\"surname\":\"Bloggs\"}")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{\"id\":3,\"firstname\":\"Jack\",\"surname\":\"Bloggs\"}"));
        mockMvc.perform(get("/customer"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("[{\"id\":2,\"firstname\":\"Jane\",\"surname\":\"Smith\"}, {\"id\":3,\"firstname\":\"Jack\",\"surname\":\"Bloggs\"}]"));
    }
}
