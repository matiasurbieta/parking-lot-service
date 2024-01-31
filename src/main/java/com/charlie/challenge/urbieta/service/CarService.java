package com.charlie.challenge.urbieta.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.repository.VehicleRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class CarService {
    @Autowired
    VehicleRepository carRepository;

    public Vehicle save(@Valid @NotNull Vehicle car) {

        return this.carRepository.save(car);
    }

    public List<Vehicle> findAll() {
        return this.carRepository.findAll();
    }

    public Vehicle findById(String car) {
        return this.carRepository.getReferenceById(car);
    }

    public void delete(String plate) {
        carRepository.deleteById(plate);
    }

}
