package com.charlie.challenge.urbieta.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.model.VehicleType;
@SpringBootTest
@RunWith(SpringRunner.class)
public class CarServiceTests {
    
    @Autowired
    CarService carService;
	@Test
    public void basicCarCreation(){
        try {
            carService.save(null);
            fail();
            
        } catch (Exception e) {
           assertTrue(e.getMessage().contains("must not be null"));
        }

        try {
            carService.save(new Vehicle(null,null));
            fail();
            
        } catch (Exception e) {
           assertTrue(e.getMessage().contains("must not be blank"));
        }


        String plate = RandomStringUtils.randomAlphabetic(8);

        try {
            carService.save(new Vehicle(plate,VehicleType.Car));
            
        } catch (Exception e) {
            fail();
        }
    }
    
}
