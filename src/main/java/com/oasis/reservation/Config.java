package com.oasis.reservation;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public final class Config {
 private static final Properties P = new Properties();
 static {
  try (InputStream in = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
   if (in == null) throw new IllegalStateException("application.properties not found");
   P.load(in);
  } catch (IOException e) { throw new ExceptionInInitializerError(e); }
 }
 private Config() {}
 public static String get(String key) {
  String v = System.getenv("OIBSIP_" + key.toUpperCase().replace('.', '_'));
  if (v == null || v.isBlank()) v = P.getProperty(key);
  return v == null ? "" : v.trim();
 }
}
