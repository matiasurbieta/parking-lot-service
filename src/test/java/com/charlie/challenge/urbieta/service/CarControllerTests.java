package com.charlie.challenge.urbieta.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.model.VehicleType;
import com.google.gson.Gson;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTests {
  Gson gson = new Gson();

  @Autowired
  private MockMvc mockMvc;

  @Test
  public void contextLoads() throws Exception {
    assertThat(mockMvc).isNotNull();
  }

  @Test
  public void resolveAllCars()
      throws Exception {

    // Given
    this.mockMvc.perform(get("/cars/")).andDo(print()).andExpect(status().isOk())
        //.andExpect(content().string(containsString("[]")));
        .andExpect(jsonPath("$", hasSize(1)));


  }

  @Test
  @Order(1)

  public void createCar()
      throws Exception {
    String name = RandomStringUtils.randomAlphabetic(8);
    Vehicle car = new Vehicle(name, VehicleType.Car);
    String body = gson.toJson(car);
    // Given

    this.mockMvc.perform(put("/cars/").contentType(APPLICATION_JSON)
        .content(body))
        .andExpect(status().isOk()).andReturn();

  }

}
