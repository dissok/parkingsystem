package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
    public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            //when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

            //Ajout: Si le type de véhicule est demandé, on retourne 1 (pour CAR)
            //when(inputReaderUtil.readSelection()).thenReturn(1);

            //Ajout: Si demande d'une nouvelle place de parking pour CAR, on retourne la place N°1
            //when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
            Ticket ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");

            //when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
            //when(ticketDAO.saveTicket(any(Ticket.class))).thenReturn(true);
            //when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);


            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void processExitingVehicleTest(){

        try {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60*60*1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCDEF");

        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);


        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

            //when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }

    }



    //Rajout du test sur le process d'entrée des véhicules
    @Test
    public void processIncomingVehicleTest(){

        //Ajout: Si le type de véhicule est demandé, on retourne 1 (pour CAR)
        when(inputReaderUtil.readSelection()).thenReturn(1);

        //Ajout: Si demande d'une nouvelle place de parking pour CAR, on retourne la place N°1
        when(parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR)).thenReturn(1);

        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));
    }


    @Test
    public void knowIfCustomerIsRecurring(){

        //Ajout:
        TicketDAO ticketDAO1 = new TicketDAO();

        //Lancer la méthode directement et vérifier que le résultat est false
        //Insérer 2 lignes avec prix > 0 et un délai de moins de 7 jours et vérifier que
        //le résultat est true.

        //Définition de l'immatriculation du véhicule
        Ticket ticket1 = new Ticket();
        ticket1.setVehicleRegNumber("ED-133-SK");

        //Définition de j-5 (heure d'entrée il y a 5 jours) (exple: Si entrée --> x heures)
        Date inTime5DaysBefore = new Date();
        inTime5DaysBefore.setTime(System.currentTimeMillis()-(5*24*3600*1000));

        //Définition de l'heure de sortie il ya 5 jours (exple: sortie --> x+2 heures)
        Date outTime5DaysBefore = new Date();
        //outTime5DaysBefore.setTime(System.currentTimeMillis()-(5*24*3600*1000+2*3600*1000));
        outTime5DaysBefore.setTime(inTime5DaysBefore.getTime()+(2*3600*1000));

        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        //Création d'un ticket pour il y a 5 jours (en entrée et sortie après 2h)
        ticket1.setInTime(inTime5DaysBefore);
        ticket1.setOutTime(outTime5DaysBefore);
        ticket1.setParkingSpot(parkingSpot);
        ticketDAO1.saveTicket(ticket1);




        //Définition pour j-6 avec écart d'1 heure cette fois
        Ticket ticket2 = new Ticket();
        ticket2.setVehicleRegNumber("ED-133-SK");

        Date inTime6DaysBefore = new Date();
        inTime6DaysBefore.setTime(System.currentTimeMillis()-(6*24*3600*1000));

        Date outTime6DaysBefore = new Date();
        //outTime6DaysBefore.setTime(System.currentTimeMillis()-(6*24*3600*1000+3600*1000));
        outTime6DaysBefore.setTime(inTime6DaysBefore.getTime()+(3600*1000));

        ParkingSpot parkingSpot1 = new ParkingSpot(1, ParkingType.CAR,false);

        ticket2.setInTime(inTime6DaysBefore);
        ticket2.setOutTime(outTime6DaysBefore);
        ticket2.setParkingSpot(parkingSpot1);
        ticketDAO1.saveTicket(ticket2);



        //Définition pour j
        Ticket ticket3= new Ticket();
        ticket3.setVehicleRegNumber("ED-133-SK");

        Date inTime = new Date();
        Date outTime = new Date();
        inTime.setTime( System.currentTimeMillis() - (  20 * 60 * 1000) );

        ParkingSpot parkingSpot2 = new ParkingSpot(1, ParkingType.CAR,false);

        ticket3.setInTime(inTime);
        ticket3.setOutTime(outTime);
        ticket3.setParkingSpot(parkingSpot2);
        ticketDAO1.saveTicket(ticket3);


        //Calcul du tarif

        boolean daoDAO = ticketDAO1.getLastTickets("ED-133-SK");
        System.out.println("customerIsItRecurring" + " ?" + " :" + daoDAO);
        assertEquals(true, daoDAO);

    }



}
