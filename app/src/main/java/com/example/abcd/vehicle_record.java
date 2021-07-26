package com.example.abcd;

public class vehicle_record {
    String plate_number,owner_name,owner_father,owner_city,Make_Name,Model,Color,Vehicle_Price,Registration_Date,Chassis_Number,Engine_Number;
    public  vehicle_record(){}

    public vehicle_record(String plate_number, String owner_name, String owner_father, String owner_city, String make_Name, String model, String color, String vehicle_Price, String registration_Date, String chassis_Number, String engine_Number) {
        this.plate_number = plate_number;
        this.owner_name = owner_name;
        this.owner_father = owner_father;
        this.owner_city = owner_city;
        Make_Name = make_Name;
        Model = model;
        Color = color;
        Vehicle_Price = vehicle_Price;
        Registration_Date = registration_Date;
        Chassis_Number = chassis_Number;
        Engine_Number = engine_Number;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getOwner_father() {
        return owner_father;
    }

    public void setOwner_father(String owner_father) {
        this.owner_father = owner_father;
    }

    public String getOwner_city() {
        return owner_city;
    }

    public void setOwner_city(String owner_city) {
        this.owner_city = owner_city;
    }

    public String getMake_Name() {
        return Make_Name;
    }

    public void setMake_Name(String make_Name) {
        Make_Name = make_Name;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    public String getVehicle_Price() {
        return Vehicle_Price;
    }

    public void setVehicle_Price(String vehicle_Price) {
        Vehicle_Price = vehicle_Price;
    }

    public String getRegistration_Date() {
        return Registration_Date;
    }

    public void setRegistration_Date(String registration_Date) {
        Registration_Date = registration_Date;
    }

    public String getChassis_Number() {
        return Chassis_Number;
    }

    public void setChassis_Number(String chassis_Number) {
        Chassis_Number = chassis_Number;
    }

    public String getEngine_Number() {
        return Engine_Number;
    }

    public void setEngine_Number(String engine_Number) {
        Engine_Number = engine_Number;
    }
}
