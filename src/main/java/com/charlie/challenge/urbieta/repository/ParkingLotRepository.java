package com.charlie.challenge.urbieta.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.charlie.challenge.urbieta.model.ParkingLot;
import com.charlie.challenge.urbieta.model.ParkingLotType;

import jakarta.validation.constraints.NotBlank;


/**
 * Parking Lot's repository
 */
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    int countByVehicleIsNull();

    List<ParkingLot> findAllByVehiclePlate(@NotBlank String plate);

    ParkingLot findFirstByTypeInAndVehicleIsNull(List<ParkingLotType> list);
    List<ParkingLot> findByTypeAndVehicleNull(ParkingLotType regular);



}
