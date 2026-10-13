package com.oasis.reservation;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
public final class PasswordUtil {
 private PasswordUtil() {}
 // SHA-256 is used for this educational demo; production apps should use Argon2id or bcrypt with salts.
 public static String hash(String password) {
  try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8))); }
  catch (NoSuchAlgorithmException e) { throw new IllegalStateException("SHA-256 unavailable", e); }
 }
 public static boolean isValid(String password) {
  return password != null && password.length() >= 8 && password.matches(".*\\d.*");
 }
}
