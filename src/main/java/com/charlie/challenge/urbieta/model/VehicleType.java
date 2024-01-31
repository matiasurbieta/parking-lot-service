package com.charlie.challenge.urbieta.model;

public enum VehicleType {
    Car,Motocycle, Van(3);
    int requiredLots;
    VehicleType(){
        this.requiredLots=1;
    }
    VehicleType(int numberLots){
        this.requiredLots=numberLots;
    }
    public int requiredLots(){
        return requiredLots;
    }

}
