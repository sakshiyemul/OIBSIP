package com.oasis.reservation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Database {
 private Database() {}
 public static Connection getConnection() throws SQLException {
  String url = Config.get("db.url"), user = Config.get("db.username"), pass = Config.get("db.password");
  if (pass.isBlank() || pass.equals("CHANGE_ME")) throw new SQLException("Set your MySQL password in application.properties first.");
  return DriverManager.getConnection(url, user, pass);
 }
}
