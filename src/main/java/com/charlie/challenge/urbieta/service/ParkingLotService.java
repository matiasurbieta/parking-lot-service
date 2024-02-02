package com.charlie.challenge.urbieta.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.charlie.challenge.urbieta.model.ParkingLot;
import com.charlie.challenge.urbieta.model.ParkingLotType;
import com.charlie.challenge.urbieta.model.Vehicle;
import com.charlie.challenge.urbieta.repository.VehicleRepository;
import com.charlie.challenge.urbieta.repository.ParkingLotRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
@Validated
public class ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository;

    @Autowired
    VehicleRepository vehicleRepository;

    /**
     * 
     * Saves a Parking lot in the database
     * 
     * @param parkingLot
     * @return
     */
    public ParkingLot save(@Valid @NotNull ParkingLot parkingLot) {

        return this.parkingLotRepository.save(parkingLot);
    }

    /**
     * Finds all parking lot no matter their condition
     * 
     * @return
     */
    public List<ParkingLot> findAll() {
        return this.parkingLotRepository.findAll();
    }

    /**
     * Parks a vehicle in a given parking lot.
     * It checkes whether or not the lot exists or it is in use.
     * 
     * @param vehicle
     * @param parkingLotId
     * @return
     * @throws ParkingLotException
     */

    public boolean parkVehicle(@Valid @NotNull Vehicle vehicle, @NotNull Long parkingLotId) throws ParkingLotException {
        Optional<ParkingLot> pLotReturn = parkingLotRepository.findById(parkingLotId);

        if (pLotReturn.isEmpty()) {
            throw new ParkingLotException("Parking lot does not exists:" + parkingLotId);
        } else {
            ParkingLot pLot = pLotReturn.get();
            return this.parkVehicle(vehicle, pLot);
        }

    }

    /**
     * Parks a vehicle in the lot passed as parameter.
     * When there is a new vehicle, it is saved in the database
     * 
     * 
     * @param vehicle
     * @param pLot
     * @return
     * @throws ParkingLotException
     */

    boolean parkVehicle(@Valid @NotNull Vehicle vehicle, ParkingLot pLot) throws ParkingLotException {
        
        if (pLot.getVehicle() != null) {
            throw new ParkingLotException("Parking lot is already in use by:" + pLot.getVehicle().getPlate());
        }
        // find or create the vehicle
        Optional<Vehicle> vehicleById = vehicleRepository.findById(vehicle.getPlate());
        if (vehicleById.isEmpty()) {
            vehicleRepository.save(vehicle);
        }
        pLot.setVehicle(vehicle);
        // saving the lot status
        parkingLotRepository.save(pLot);
        return true;

    }

    public ParkingLot findById(@NotNull Long id) {
        return parkingLotRepository.getReferenceById(id);
    }

    /**
     * 
     * Releases all lots taken by a vehicle
     * 
     * @param id
     * @throws ParkingLotException
     */
    public void releaseLots(@NotBlank String plate) throws ParkingLotException {
         List<ParkingLot> lots = parkingLotRepository.findAllByVehiclePlate(plate);

        if (lots.isEmpty()) {
            throw new ParkingLotException("Vehicle is not parked");
        } else {

            //
            for (ParkingLot parkingLot : lots) {
                parkingLot.setVehicle(null);
                parkingLotRepository.save(parkingLot);
            }
           

        }
    }

    /**
     * 
     * counts all lots without any vehicle parked
     * 
     * @return
     */
    public int countAvailable() {
        return parkingLotRepository.countByVehicleIsNull();
    }

    /**
     * Find all lots that are being used by a vehicle
     * 
     * @param plate
     * @return
     */
    public List<ParkingLot> takenLotsByVehicle(@NotBlank String plate) {
        return parkingLotRepository.findAllByVehiclePlate(plate);
    }

    public void findLotAndPark(Vehicle vehicle) throws ParkingLotException {
        int numberOfTakenLots = this.takenLotsByVehicle(vehicle.getPlate()).size();
        if (numberOfTakenLots!=0) {
            throw new ParkingLotException("the vehicle is already parked");
        }
        switch (vehicle.getType()) {
            // treating car and motocycle in the same way.
            case Motocycle:
                parkCarOrMotocycle(vehicle);
                break;
            case Car:
                parkCarOrMotocycle(vehicle);
                break;

            case Van:
                parkVan(vehicle);
                break;

            default:

                throw new IllegalStateException("Vehicle type must not be null");
        }

    }

    /**
     * 
     * Parks a van in the required RegularLots
     * 
     * @param vehicle
     * @throws ParkingLotException
     */
    private void parkVan(Vehicle vehicle) throws ParkingLotException {
        List<ParkingLot> parkingLots = parkingLotRepository.findByTypeAndVehicleNull(ParkingLotType.Regular);
        // checks if the minimun number of slots are available
        if (parkingLots.size() >= vehicle.getType().requiredLots()) {

            for (int i = 0; i < vehicle.getType().requiredLots(); i++) {
                ParkingLot parkingLot = parkingLots.get(i);
                this.parkVehicle(vehicle, parkingLot);

            }
        } else {
            throw new ParkingLotException("No enough lots aviailable. There are %d  but %d are required."
                    .formatted(parkingLots.size(), vehicle.getType().requiredLots()));
        }
    }

    /**
     * 
     * Parks a vehicle on a Copact or Regular lot
     * 
     * @param vehicle
     * @throws ParkingLotException
     */
    private void parkCarOrMotocycle(Vehicle vehicle) throws ParkingLotException {
        ParkingLot parkingLot = parkingLotRepository.findFirstByTypeInAndVehicleIsNull(Arrays.asList(ParkingLotType.Compact,ParkingLotType.Regular) );
        if (parkingLot == null) {
            throw new ParkingLotException("No lot aviailable");
        } else {
            this.parkVehicle(vehicle, parkingLot);
        }
    }
}
