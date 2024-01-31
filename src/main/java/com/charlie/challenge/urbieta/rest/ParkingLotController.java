package com.charlie.challenge.urbieta.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charlie.challenge.urbieta.model.ParkingLot;
import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.service.ParkingLotException;
import com.charlie.challenge.urbieta.service.ParkingLotService;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value="/parkinglot")
public class ParkingLotController {
    
    @Autowired
    ParkingLotService parkingLotService;
    
    @GetMapping(value="/{id}")
    public ParkingLot getLot(@NotNull @PathVariable Long id) {
        return parkingLotService.findById(id);
    }

    
    @PutMapping(value="/")
    public ParkingLot createCart( @RequestBody ParkingLot pLot) {
        return parkingLotService.save(pLot);
    }

   @GetMapping(value="/")
    public List<ParkingLot> listParkingLots() {
       return parkingLotService.findAll();
    } 

    @PostMapping(value="/park/{id}")
    public boolean park(@RequestBody Vehicle vehicle,@PathVariable Long id) throws ParkingLotException {
       return parkingLotService.parkVehicle(vehicle, id);
    } 
    @PostMapping(value="/findLotAndPark")
    public void park(@RequestBody Vehicle vehicle) throws ParkingLotException {
        parkingLotService.findLotAndPark(vehicle);
    } 

    @GetMapping(value="/release/{plate}")
    public void releaseLot(@PathVariable String plate) throws ParkingLotException {
        parkingLotService.releaseLots( plate);
    } 
    @PostMapping(value="/countavailable/")
    public int countAvailable() {
       return  parkingLotService.countAvailable();
    }

    @PostMapping(value="/takenlotsbyvehicle/{plate}")
    public List<ParkingLot> takenLotsByVehicle(@NotBlank @PathVariable String plate)  {
       return  parkingLotService.takenLotsByVehicle(plate);
    }


}