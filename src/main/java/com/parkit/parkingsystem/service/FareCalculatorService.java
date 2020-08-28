package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import static com.parkit.parkingsystem.constants.Fare.FREE_TIME;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }
        
        //On remplace les 2 instructions ci-dessous en commentaire par les 2 d'après
        //et passer le type de la variable de int --> long

        //int inHour = ticket.getInTime().getHours();
        //int outHour = ticket.getOutTime().getHours();

        long inHour = ticket.getInTime().getTime();
        long outHour = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct

        //Le type de la variable duration passe à double plutôt que long afin de
        //prendre en compte les décimales

        double duration = (outHour - inHour) / 3600000d;//on divise par 3600000d pour millisecondes --> hour


        if(duration <= FREE_TIME){

            duration = 0;
        }

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unknown Parking Type");
        }
    }
}