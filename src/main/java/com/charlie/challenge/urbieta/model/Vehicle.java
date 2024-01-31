package com.charlie.challenge.urbieta.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Vehicle implements Serializable {

   
    @Id
    @Column(nullable = false)
    @NotBlank
    private String plate;


    @NotNull
    private VehicleType type;

    // ... additional members, often include @OneToMany mappings

     Vehicle() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public Vehicle(String plate,VehicleType type) {
        this.plate = plate;
        this.type = type;
    }

  

}