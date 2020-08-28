package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actually saved in DB and Parking table is updated with availability

        Ticket ticket = ticketDAO.getTicket("ABCDEF"); //récupération du ticket via l'immatriculation
        //System.out.println(ticket.getId());//N° de ticket récupéré
        assertEquals(1, ticket.getId());//Compare la valeur attendue à la valeur actuelle
        ParkingSpot parkingSpot = ticket.getParkingSpot();//Récupération de la place de parking associée au ticket
        //System.out.println(parkingSpot.isAvailable());//Vérification de la disponibilité de la place associée
        //assertEquals(false, parkingSpot.isAvailable());//la valeur attendue à la valeur actuelle
        assertFalse(parkingSpot.isAvailable());//ou le précédent assertEquals
    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        try {
            TimeUnit.SECONDS.sleep(1);}
        catch( java.lang.InterruptedException e){
        } //génère 1 slot d'1 sec entre l'instruction précédente et celle qui suit
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database
        Ticket ticket = ticketDAO.getTicket("ABCDEF"); //récupération du ticket via l'immatriculation
        System.out.println(ticket.getInTime());
        System.out.println(ticket.getPrice());//Récupération du prix
        assertNotEquals(0, ticket.getPrice());//Vérification de la positivité du prix
        System.out.println(ticket.getOutTime());
        assertNotNull(ticket.getOutTime());//Vérification que le out time associé est non null

    }

}
