package com.charlie.challenge.urbieta.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.service.CarService;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping(value="/cars")
public class CarController {
    
    @Autowired
    CarService carService;
    

    @GetMapping(value="/{car}")
    public Vehicle getCar(@Nonnull @PathVariable String car) {
        return carService.findById(car);
    }

    @DeleteMapping(value="/{car}") 
    public void deleteCar(@NotBlank @PathVariable String car) {
         carService.delete(car);
    }
    @PutMapping(value="/")
    public Vehicle createCart( @RequestBody Vehicle car) {
        return carService.save(car);
    }

   @GetMapping(value="/")
    public List<Vehicle> listCars() {

        
        return carService.findAll();
    } 

}