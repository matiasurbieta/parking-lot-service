package com.charlie.challenge.urbieta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.charlie.challenge.urbieta.model.Vehicle;
/**
 * Vehicle's repository
 */
public interface VehicleRepository extends JpaRepository<Vehicle, String> {



}
