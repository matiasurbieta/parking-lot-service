package com.charlie.challenge.urbieta.model;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 
 * Domain model for parking lot

 */
@Entity
@Data
public class ParkingLot implements Serializable {

    @Id
    private Long id;
   
    @NotNull
    private ParkingLotType type;


    @ManyToOne
    private Vehicle vehicle;

    // ... additional members, often include @OneToMany mappings

     ParkingLot() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    public ParkingLot(Long id,ParkingLotType type) {
        this.id=id;
        this.type = type;
    }

  

}