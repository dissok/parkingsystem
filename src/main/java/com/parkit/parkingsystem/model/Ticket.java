package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.dao.TicketDAO;
//import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.Calendar;
import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;


    //Définition des fonctions qui nous permettront de définir si récc ou pas
    private Boolean reccuringVehicle = false;
    //Getter
    public Boolean getReccuringVehicle() {
        //TicketDAO ticketPremier = new TicketDAO();
        //return ticketPremier.getLastTickets(vehicleRegNumber);
        return this.reccuringVehicle;
    }
    //Setter
    public void setReccuringVehicle(Boolean reccuringVehicle) {
        this.reccuringVehicle = reccuringVehicle;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }
}
