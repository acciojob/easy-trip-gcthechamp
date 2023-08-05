package com.driver.repository;

import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.*;


@Repository
public class AirportRepository {

    //airport database
    HashMap<String, Airport> airportDb = new HashMap<>();

    //flight database
    HashMap<Integer, Flight> flightDb = new HashMap<>();

    //flight vs no. of people
    HashMap<Integer, List<Passenger>> flightPassengers = new HashMap<>();

    //passenger database
    HashMap<Integer, Passenger> PassengerDb = new HashMap<>();

    //passenger vs flights booked
    HashMap<Integer, List<Flight>> flightsBooked = new HashMap<>();

    //flight vs its total revenue
    HashMap<Integer, Integer> flightRevenue = new HashMap<>();

    //passenger vs revenue due to him
    HashMap<Integer,Integer> RevenueByPassenger = new HashMap<>();

    public void addAirport(Airport airport) {
        airportDb.put(airport.getAirportName(),airport);
    }


    public String getLargestAirportName() {
        TreeMap<String, Airport> sortedAirportList = new TreeMap<>();
        sortedAirportList.putAll(airportDb);
        int maxTerminals = 0;
        String largestAirportName = "";
        for(String airportName : sortedAirportList.keySet())
        {
            int terminals = sortedAirportList.get(airportName).getNoOfTerminals();
            if(maxTerminals < terminals)
            {
                maxTerminals = terminals;
                largestAirportName = airportName;
            }
        }
        return largestAirportName;
    }

    public List<Flight> getFlightsBetweenTwoCities(City fromCity, City toCity)
    {
        List<Flight> flightList = new ArrayList<>();
        for(Flight flight : flightDb.values())
        {
            if(flight.getFromCity() == fromCity && flight.getToCity() == toCity)
                flightList.add(flight);
        }
        return flightList;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        int count = 0;
        City city = airportDb.get(airportName).getCity();
        for(Flight flight : flightDb.values()) {
            if (flight.getFlightDate() == date && (flight.getFromCity() == city || flight.getToCity() == city))
                count+= flightPassengers.get(flight.getFlightId()).size();
        }
        return count;
    }

    public boolean containsPassenger(List<Passenger> passengerList, Integer passengerId)
    {
        for(Passenger passenger : passengerList)
        {
            if(passenger.getPassengerId() == passengerId)
                return true;
        }
        return false;
    }

    public int calculateFlightFare(Integer flightId) {
        int numOfPeopleBookedAlready = flightPassengers.get(flightId).size();
        int flightFare = 3000 + numOfPeopleBookedAlready*50;
        return flightFare;
    }

    public String bookATicket(Integer flightId, Integer passengerId) {
        int numOfPeopleBookedAlready = flightPassengers.get(flightId).size();
        if(numOfPeopleBookedAlready > flightDb.get(flightId).getMaxCapacity())
            return "FAILURE";

        List<Flight> flightList = flightsBooked.get(passengerId);

        for(Flight flight : flightList)
        {
            if(flight.getFlightId() == flightId)
                return "FAILURE";
        }


        List<Passenger> peopleInFlightAlready = flightPassengers.getOrDefault(flightId,new ArrayList<>());

        Passenger newPassenger = PassengerDb.get(passengerId);

        int revenueAdded = calculateFlightFare(flightId);

        RevenueByPassenger.put(passengerId, revenueAdded);

        peopleInFlightAlready.add(newPassenger);

        flightPassengers.put(flightId, peopleInFlightAlready);

        flightRevenue.put(flightId, flightRevenue.getOrDefault(flightId,0) + revenueAdded);





        List<Flight> alreadyBookedFlights = flightsBooked.getOrDefault(passengerId, new ArrayList<>());

        Flight newFlight = flightDb.get(flightId);

        alreadyBookedFlights.add(newFlight);

        flightsBooked.put(passengerId, alreadyBookedFlights);

        return "SUCCESS";
    }

    public String cancelATicket(Integer flightId, Integer passengerId) {

        if(!flightDb.containsKey(flightId))
            return "FAILURE";

        boolean isFlightBooked = false;

        List<Flight> flightList = flightsBooked.getOrDefault(passengerId, new ArrayList<>());

        if(flightList.size() == 0)
            return "FAILURE";

        Flight flightToCancel = null;

        for(Flight flight : flightList)
        {
            if(flight.getFlightId() == flightId)
            {
                isFlightBooked = true;
                flightToCancel = flight;
                break;
            }
        }

        if(isFlightBooked == false)
            return "FAILURE";

        flightList.remove(flightToCancel);

        flightsBooked.put(passengerId,flightList);

        List<Passenger> passengerList = flightPassengers.get(flightToCancel);

        Passenger passengerToRemove = PassengerDb.get(passengerId);

        passengerList.remove(passengerToRemove);

        flightPassengers.put(flightToCancel.getFlightId(),passengerList);

        int revenueLost = RevenueByPassenger.get(passengerId);

        flightRevenue.put(flightId, flightRevenue.get(flightId) - revenueLost);

        return "SUCCESS";
   }

    public int countOfBookingsDoneByPassengerAllCombined(Integer passengerId) {

        return flightsBooked.get(passengerId).size();
    }


    public String addFlight(Flight flight) {

        flightDb.put(flight.getFlightId(), flight);
        return "SUCCESS";
    }

    public String getAirportNameFromFlightId(Integer flightId) {
        if(!flightDb.containsKey(flightId))
            return null;

        City city = flightDb.get(flightId).getFromCity();

        for(Airport airport : airportDb.values())
        {
            if(airport.getCity() == city)
                return airport.getAirportName();
        }
        return null;
    }

    public int calculateRevenueOfAFlight(Integer flightId) {
        return flightRevenue.get(flightId);
    }

    public String addPassenger(Passenger passenger) {
        PassengerDb.put(passenger.getPassengerId(),passenger);
        return "SUCCESS";
    }
}
