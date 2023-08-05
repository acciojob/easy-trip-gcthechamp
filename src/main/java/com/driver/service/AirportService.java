package com.driver.service;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AirportService {


    AirportRepository robj = new AirportRepository();

    public void addAirport(Airport airport) {
        robj.addAirport(airport);
    }


    public String getLargestAirportName() {
        return robj.getLargestAirportName();
    }

    public double getShortestDurationOfPossibleBetweenTwoCities(City fromCity, City toCity) {
        List<Flight> flights = robj.getFlightsBetweenTwoCities(fromCity, toCity);
        if(flights.size() == 0)
            return -1;


        double minDuration = Double.MAX_VALUE;
        for(Flight flight : flights)
            {
                if(flight.getDuration() < minDuration)
                {
                    minDuration = flight.getDuration();
                }
            }
        return minDuration;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {

        return robj.getNumberOfPeopleOn(date,airportName);
    }

    public int calculateFlightFare(Integer flightId) {

        return robj.calculateFlightFare(flightId);
    }

    public String bookATicket(Integer flightId, Integer passengerId) {

        return robj.bookATicket(flightId, passengerId);
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        return robj.cancelATicket(flightId, passengerId);
    }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        return robj.countOfBookingsDoneByPassengerAllCombined(passengerId);
    }

    public String addFlight(Flight flight) {

        return robj.addFlight(flight);
    }

    public String getAirportNameFromFlightId(Integer flightId) {

        return robj.getAirportNameFromFlightId(flightId);
    }

    public int calculateRevenueOfAFlight(Integer flightId) {

        return robj.calculateRevenueOfAFlight(flightId);
    }

    public String addPassenger(Passenger passenger) {

        return robj.addPassenger(passenger);
    }
}
