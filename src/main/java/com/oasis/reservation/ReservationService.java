package com.oasis.reservation;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class ReservationService {
 public boolean register(String username, String password) throws SQLException {
  username = clean(username);
  if (username.length() < 3) throw new IllegalArgumentException("Username must contain at least 3 characters.");
  if (!PasswordUtil.isValid(password)) throw new IllegalArgumentException("Password must be at least 8 characters and contain a number.");
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("INSERT INTO app_users(username,password_hash) VALUES (?,?)")) {
   ps.setString(1, username); ps.setString(2, PasswordUtil.hash(password)); ps.executeUpdate(); return true;
  } catch (SQLIntegrityConstraintViolationException e) { return false; }
 }
 public boolean login(String username, String password) throws SQLException {
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT password_hash FROM app_users WHERE username=?")) {
   ps.setString(1, clean(username));
   try (ResultSet rs = ps.executeQuery()) { return rs.next() && rs.getString(1).equals(PasswordUtil.hash(password)); }
  }
 }
 public List<String> getTrains() throws SQLException {
  List<String> out = new ArrayList<>();
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT train_number,train_name,source_station,destination_station FROM trains ORDER BY train_number"); ResultSet rs = ps.executeQuery()) {
   while (rs.next()) out.add(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3) + " → " + rs.getString(4));
  }
  return out;
 }
 public long book(String username, String passengerName, int trainNumber, String travelClass, LocalDate date) throws SQLException {
  if (passengerName == null || passengerName.isBlank()) throw new IllegalArgumentException("Passenger name is required.");
  if (date == null || date.isBefore(LocalDate.now())) throw new IllegalArgumentException("Journey date cannot be in the past.");
  String sql = "INSERT INTO reservations(username,passenger_name,train_number,travel_class,journey_date,source_station,destination_station) " +
   "SELECT ?, ?, train_number, ?, ?, source_station, destination_station FROM trains WHERE train_number=?";
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
   ps.setString(1, clean(username)); ps.setString(2, passengerName.trim()); ps.setString(3, travelClass);
   ps.setDate(4, Date.valueOf(date)); ps.setInt(5, trainNumber);
   if (ps.executeUpdate() == 0) throw new IllegalArgumentException("Please select a valid train.");
   try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) return keys.getLong(1); }
  }
  throw new SQLException("Booking saved but PNR could not be generated.");
 }
 public Reservation findByPnr(long pnr) throws SQLException {
  String sql = "SELECT r.pnr,r.username,r.passenger_name,r.train_number,t.train_name,r.travel_class,r.journey_date,r.source_station,r.destination_station " +
   "FROM reservations r JOIN trains t ON t.train_number=r.train_number WHERE r.pnr=?";
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
   ps.setLong(1, pnr);
   try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
  }
 }
 public List<Reservation> findByUsername(String username) throws SQLException {
  List<Reservation> out = new ArrayList<>();
  String sql = "SELECT r.pnr,r.username,r.passenger_name,r.train_number,t.train_name,r.travel_class,r.journey_date,r.source_station,r.destination_station " +
   "FROM reservations r JOIN trains t ON t.train_number=r.train_number WHERE r.username=? ORDER BY r.pnr DESC";
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
   ps.setString(1, clean(username));
   try (ResultSet rs = ps.executeQuery()) { while (rs.next()) out.add(map(rs)); }
  }
  return out;
 }
 public boolean cancel(long pnr, String username) throws SQLException {
  try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM reservations WHERE pnr=? AND username=?")) {
   ps.setLong(1, pnr); ps.setString(2, clean(username)); return ps.executeUpdate() == 1;
  }
 }
 private static Reservation map(ResultSet rs) throws SQLException {
  return new Reservation(rs.getLong("pnr"), rs.getString("username"), rs.getString("passenger_name"), rs.getInt("train_number"),
   rs.getString("train_name"), rs.getString("travel_class"), rs.getDate("journey_date"), rs.getString("source_station"), rs.getString("destination_station"));
 }
 private static String clean(String s) { return s == null ? "" : s.trim(); }
}
