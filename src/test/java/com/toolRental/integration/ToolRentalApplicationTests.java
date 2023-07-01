package com.toolRental.integration;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.toolRental.model.RentalAgreement;
import com.toolRental.model.RentalRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureMockMvc
public class ToolRentalApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void should_retrieve_one_tool() throws Exception {
        this.mockMvc.perform(get("/tools/fetch/{toolCode}", "CHNS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("CHNS"))
                .andExpect(jsonPath("$.label").value("Chainsaw"))
                .andExpect(jsonPath("$.brand").value("Stihl"));
    }

    @Test
    public void tool_rental_high_discount() throws Exception {
        RentalRequest rentalRequest = new RentalRequest("JAKR", 5, 101, LocalDate.parse("2015-09-03"));
        this.mockMvc.perform(post("/tools/requestRental").contentType(MediaType.APPLICATION_JSON).content(rentalRequest.toString()))
                .andDo(print())
                .andExpect(status().is(400));
        
    }

    @Test
    public void tool_rental_happy_path1() throws Exception {
        RentalRequest rentalRequest = new RentalRequest("LADW", 3, 10, LocalDate.parse("2020-07-02"));
        this.mockMvc.perform(post("/tools/requestRental").contentType(MediaType.APPLICATION_JSON).content(rentalRequest.toString()))
                .andDo(print())
                .andExpect(status().is(200));
        this.mockMvc.perform(get("/tools/fetch/{toolCode}", "LADW"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.rentalDays").value(3));
    }

    @Test
    public void tool_rental_happy_path2() throws Exception {
        RentalRequest rentalRequest = new RentalRequest("CHNS", 5, 25, LocalDate.parse("2015-07-02"));
        this.mockMvc.perform(post("/tools/requestRental").contentType(MediaType.APPLICATION_JSON).content(rentalRequest.toString()))
                .andDo(print())
                .andExpect(status().is(200));
        this.mockMvc.perform(get("/tools/fetch/{toolCode}", "CHNS"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.rentalDays").value(5));
    }

    @Test
    public void tool_rental_happy_path3() throws Exception {
        RentalRequest rentalRequest = new RentalRequest("JAKD", 6, 0, LocalDate.parse("2015-09-03"));
        this.mockMvc.perform(post("/tools/requestRental").contentType(MediaType.APPLICATION_JSON).content(rentalRequest.toString()))
                .andDo(print())
                .andExpect(status().is(200));
        this.mockMvc.perform(get("/tools/fetch/{toolCode}", "JAKD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.rentalDays").value(5));
    }

    @Test
    public void tool_rental_happy_path4() throws Exception {
        RentalRequest rentalRequest = new RentalRequest("JAKR", 9, 0, LocalDate.parse("2015-07-02"));
        this.mockMvc.perform(post("/tools/requestRental").contentType(MediaType.APPLICATION_JSON).content(rentalRequest.toString()))
                .andDo(print())
                .andExpect(status().is(200));
        this.mockMvc.perform(get("/tools/fetch/{toolCode}", "JAKR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.rentalDays").value(5));
    }

    @Test
    public void tool_rental_happy_path5() throws Exception {
        RentalRequest rentalRequest = new RentalRequest("JAKR", 4, 50, LocalDate.parse("2020-07-02"));
        this.mockMvc.perform(post("/tools/requestRental").contentType(MediaType.APPLICATION_JSON).content(rentalRequest.toString()))
                .andDo(print())
                .andExpect(status().is(200));
        this.mockMvc.perform(get("/tools/fetch/{toolCode}", "JAKR"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.rentalDays").value(4));
    }
}