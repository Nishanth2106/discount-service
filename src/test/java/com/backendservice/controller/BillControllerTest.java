package com.backendservice.controller;

import com.backendservice.model.*;
import com.backendservice.service.DiscountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillController.class)
class BillControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private DiscountService discountService;
    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }
    @Test
    void testCalculateNetPayableAmount_whenValidBillDetailsGiven() throws Exception {
        User user = new User();
        user.setUserType(UserType.EMPLOYEE);
        Item item1 = new Item(1, "Item1", ItemType.NON_GROCERY, 100);
        Bill bill = new Bill(1, user, List.of(item1));

        when(discountService.calculateNetPayableAmount(any(Bill.class))).thenReturn(90.0);

        MvcResult result = mvc.perform(post("/calculateNetPayableAmount").content(convertObjectToJsonBytes(bill))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk()).andReturn();
        double content = Double.parseDouble(result.getResponse().getContentAsString());
        assertEquals(90.0,content);
    }

    @Test
    void testCalculateNetPayableAmount_whenDiscountServiceFails() throws Exception {
        MvcResult result = mvc.perform(post("/calculateNetPayableAmount")
                        .content(convertObjectToJsonBytes(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest()).andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    private static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsBytes(object);
    }
}
