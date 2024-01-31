package com.charlie.challenge.urbieta;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.charlie.challenge.urbieta.model.ParkingLot;
import com.charlie.challenge.urbieta.model.ParkingLotType;
import com.charlie.challenge.urbieta.service.ParkingLotService;
/**
 * Initializes database with 25 lots if there are not available
 */

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ParkingLotService parkingLotService;

    

    public void run(ApplicationArguments args) {
        if (parkingLotService.findAll().isEmpty()) {
            //Creating 10 Compact Lots
            for (long i = 0; i < 10; i++) {
                parkingLotService.save(new ParkingLot( i,ParkingLotType.Compact));
            }
            //Creating 10 Regular Lots
            for (long i = 10; i < 20; i++) {
                parkingLotService.save(new ParkingLot( i,ParkingLotType.Regular));
            }
             //Creating 10 Motocyle Lots
             for (long i = 20; i < 25; i++) {
                parkingLotService.save(new ParkingLot( i,ParkingLotType.Motorcycle));
            }
            assert(parkingLotService.findAll().size()==25);
        }
        

}
}