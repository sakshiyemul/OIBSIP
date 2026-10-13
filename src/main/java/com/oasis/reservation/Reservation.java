package com.oasis.reservation;
import java.sql.Date;
public record Reservation(long pnr, String username, String passengerName, int trainNumber, String trainName,
 String travelClass, Date journeyDate, String sourceStation, String destinationStation) {
 @Override public String toString() {
  return "PNR: " + pnr + "\\nPassenger: " + passengerName + "\\nTrain: " + trainNumber + " - " + trainName +
   "\\nClass: " + travelClass + "\\nJourney date: " + journeyDate + "\\nRoute: " + sourceStation + " → " + destinationStation;
 }
}
