package com.charlie.challenge.urbieta.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.junit4.SpringRunner;
import com.charlie.challenge.urbieta.model.ParkingLot;
import com.charlie.challenge.urbieta.model.ParkingLotType;
import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.model.VehicleType;
import com.charlie.challenge.urbieta.repository.ParkingLotRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ParkingServiceTests {
   
     
    @SpyBean

    ParkingLotService parkingLotService;
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    CarService carService;

    @PersistenceContext
    private EntityManager entityManager;

    private int exceptionThrownCount;

    @Test
    public void basicParkingLotCreation() {
        try {
            parkingLotService.save(null);
            fail();

        } catch (Exception e) {
            assertTrue(e.getMessage().contains("must not be null"));
        }

        try {
            parkingLotService.save(new ParkingLot(null, null));
            fail();

        } catch (Exception e) {
            assertTrue(e.getMessage().contains("must not be null"));
        }

        try {
            parkingLotService.save(new ParkingLot((long) 12, ParkingLotType.Compact));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)

    public void park_car() {
        Vehicle car = new Vehicle("123", VehicleType.Car);
        try {
            parkingLotService.findLotAndPark(car);
            assertEquals(24, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail(e.getMessage());

        }

        try {
            parkingLotService.findLotAndPark(car);
            fail();
        } catch (ParkingLotException e) {
            assertEquals(24, parkingLotService.countAvailable());

        }

        try {
            parkingLotService.releaseLots(car.getPlate());
            assertEquals(25, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail(e.getMessage());

        }

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)

    public void park_motocycle() {
        Vehicle moto = new Vehicle("123", VehicleType.Motocycle);
        try {
            parkingLotService.findLotAndPark(moto);
            assertEquals(24, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail(e.getMessage());

        }

        try {
            parkingLotService.findLotAndPark(moto);
            fail();
        } catch (ParkingLotException e) {
            assertEquals(24, parkingLotService.countAvailable());

        }

        try {
            parkingLotService.releaseLots(moto.getPlate());
            assertEquals(25, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail(e.getMessage());

        }

    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)

    public void park_van() {
        // save one van and check if the number of lots is reduced
        try {
            parkingLotService.findLotAndPark(new Vehicle("van123", VehicleType.Van));
            assertEquals(22, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail();
        }
        // save 2 additional vans
        try {
            parkingLotService.findLotAndPark(new Vehicle("van1", VehicleType.Van));
            parkingLotService.findLotAndPark(new Vehicle("van2", VehicleType.Van));

            assertEquals(16, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail();
        }

        // save 1 van but should fail because there are no enough lots
        try {
            parkingLotService.findLotAndPark(new Vehicle("van3", VehicleType.Van));

            fail();
        } catch (ParkingLotException e) {
        }

        try {
            parkingLotService.releaseLots("van2");
            assertEquals(19, parkingLotService.countAvailable());
        } catch (ParkingLotException e) {
            fail();

        }

    }
    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    public void shouldAssignVehicle_withOptimisticLockingHandling() throws InterruptedException, ParkingLotException {
        
         final List<Vehicle> vehicles = Arrays.asList(new Vehicle("car",VehicleType.Car),new Vehicle("van",VehicleType.Van));

        // given
        List<ParkingLot> parkingLots = parkingLotService.findAll();
        parkingLots.forEach((parkingLot)->assertEquals(0, parkingLot.getVersion()));
        
        final ExecutorService executor = Executors.newFixedThreadPool(vehicles.size());


        vehicles.forEach((vehicle)-> {
            executor.execute(() -> {
                try {
                    parkingLotService.parkVehicle(vehicle, 0L);
                } catch (Exception e) {
                    // there should  be an data Exception because object versioning
                   flagException();
                }
            });
        });

        executor.shutdown();
        assertTrue(executor.awaitTermination(1, TimeUnit.MINUTES));
        ParkingLot takenLot = parkingLotRepository.findById(0L).get();
         System.out.println(takenLot);

        
         final Long pLotVersion = takenLot.getVersion();
         final Vehicle vehicle=takenLot.getVehicle();
         assertAll(
                 () -> assertEquals(1, pLotVersion), //
                 () -> assertNotEquals(null,vehicle),
                 () -> assertEquals(1,exceptionThrownCount ),
                () -> verify(parkingLotService, times(2)).parkVehicle(org.mockito.ArgumentMatchers.any(),org.mockito.ArgumentMatchers.anyLong())

                );
        
    }
    /**
     * Let's track how many exceptions where thrown
     */
    private void flagException() {
       exceptionThrownCount+= 1;
    }


    


}
