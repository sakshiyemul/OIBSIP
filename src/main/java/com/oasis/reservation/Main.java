
package com.oasis.reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Main {

 private final ReservationService service = new ReservationService();
 private JFrame frame;
 private String currentUser;

 public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> new Main().showLogin());
 }

 private void setup(String title, int w, int h) {
  if (frame != null) {
   frame.dispose();
  }

  frame = new JFrame(title);
  frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  frame.setSize(w, h);
  frame.setLocationRelativeTo(null);
 }

 private JPanel panel() {
  JPanel p = new JPanel(new GridBagLayout());
  p.setBorder(new EmptyBorder(22, 26, 22, 26));
  p.setBackground(new Color(245, 248, 252));
  return p;
 }

 private JLabel heading(String s) {
  JLabel l = new JLabel(s);
  l.setFont(new Font("SansSerif", Font.BOLD, 23));
  l.setForeground(new Color(25, 65, 115));
  return l;
 }

 private JButton button(String s) {
  JButton b = new JButton(s);
  b.setBackground(new Color(28, 105, 190));
  b.setForeground(Color.WHITE);
  b.setFocusPainted(false);
  b.setFont(new Font("SansSerif", Font.BOLD, 12));
  return b;
 }

 private JTextField field(int n) {
  return new JTextField(n);
 }

 // ---------------- LOGIN SCREEN ----------------

 private void showLogin() {
  setup("OIBSIP | Online Reservation System", 500, 350);

  JPanel p = panel();
  GridBagConstraints g = new GridBagConstraints();
  g.insets = new Insets(8, 8, 8, 8);
  g.fill = GridBagConstraints.HORIZONTAL;

  g.gridx = 0;
  g.gridy = 0;
  g.gridwidth = 2;
  p.add(heading("Online Reservation System"), g);

  g.gridy++;
  JLabel subtitle = new JLabel("Sign in to book and manage reservations.");
  p.add(subtitle, g);

  g.gridwidth = 1;
  g.gridy++;
  g.gridx = 0;
  p.add(new JLabel("Username"), g);

  JTextField user = field(18);
  g.gridx = 1;
  p.add(user, g);

  g.gridy++;
  g.gridx = 0;
  p.add(new JLabel("Password"), g);

  JPasswordField pass = new JPasswordField(18);
  g.gridx = 1;
  p.add(pass, g);

  JButton login = button("Login");
  JButton register = button("Create Account");

  g.gridy++;
  g.gridx = 0;
  p.add(login, g);

  g.gridx = 1;
  p.add(register, g);

  login.addActionListener(e -> {
   String username = user.getText().trim();
   String password = new String(pass.getPassword());

   db(() -> {
    if (service.login(username, password)) {
     currentUser = username;
     showDashboard();
    } else {
     error("Invalid username or password.");
    }
   });
  });

  register.addActionListener(e -> showRegister());

  frame.setContentPane(p);
  frame.setVisible(true);
 }

 // ---------------- REGISTRATION SCREEN ----------------

 private void showRegister() {
  setup("OIBSIP | Create Account", 500, 390);

  JPanel p = panel();
  GridBagConstraints g = new GridBagConstraints();
  g.insets = new Insets(8, 8, 8, 8);
  g.fill = GridBagConstraints.HORIZONTAL;

  g.gridx = 0;
  g.gridy = 0;
  g.gridwidth = 2;
  p.add(heading("Create Your Account"), g);

  g.gridy++;
  p.add(new JLabel("Register to use the reservation system."), g);

  g.gridwidth = 1;
  g.gridy++;
  g.gridx = 0;
  p.add(new JLabel("Username"), g);

  JTextField user = field(18);
  g.gridx = 1;
  p.add(user, g);

  g.gridy++;
  g.gridx = 0;
  p.add(new JLabel("Password"), g);

  JPasswordField pass = new JPasswordField(18);
  g.gridx = 1;
  p.add(pass, g);

  g.gridy++;
  g.gridx = 0;
  p.add(new JLabel("Confirm Password"), g);

  JPasswordField confirm = new JPasswordField(18);
  g.gridx = 1;
  p.add(confirm, g);

  JButton back = button("Back to Login");
  JButton create = button("Create Account");

  g.gridy++;
  g.gridx = 0;
  p.add(back, g);

  g.gridx = 1;
  p.add(create, g);

  back.addActionListener(e -> showLogin());

  create.addActionListener(e -> {
   String username = user.getText().trim();
   String password = new String(pass.getPassword());
   String confirmPassword = new String(confirm.getPassword());

   if (!password.equals(confirmPassword)) {
    error("Passwords do not match.");
    return;
   }

   db(() -> {
    try {
     if (service.register(username, password)) {
      info("Account created successfully. Please log in.");
      showLogin();
     } else {
      error("That username is already registered.");
     }
    } catch (IllegalArgumentException ex) {
     error(ex.getMessage());
    }
   });
  });

  frame.setContentPane(p);
  frame.setVisible(true);
 }

 // ---------------- DASHBOARD ----------------

 private void showDashboard() {
  setup("OIBSIP | Dashboard", 620, 410);

  JPanel p = new JPanel(new BorderLayout(12, 12));
  p.setBorder(new EmptyBorder(24, 28, 24, 28));

  JPanel top = new JPanel(new BorderLayout());
  top.add(heading("Welcome, " + currentUser), BorderLayout.WEST);

  JButton logout = button("Logout");

  logout.addActionListener(e -> {
   currentUser = null;
   showLogin();
  });

  top.add(logout, BorderLayout.EAST);
  p.add(top, BorderLayout.NORTH);

  JPanel actions = new JPanel(new GridLayout(3, 1, 10, 10));

  JButton book = button("Book a Ticket");
  JButton view = button("View My Reservations");
  JButton cancel = button("Cancel Reservation");

  actions.add(book);
  actions.add(view);
  actions.add(cancel);

  p.add(actions, BorderLayout.CENTER);

  JTextArea note = new JTextArea(
          "Features\n"
                  + "• Registration and login\n"
                  + "• Train list with route details\n"
                  + "• Automatic PNR generation\n"
                  + "• View and cancel your own bookings"
  );

  note.setEditable(false);
  note.setOpaque(false);
  p.add(note, BorderLayout.SOUTH);

  book.addActionListener(e -> showBooking());
  view.addActionListener(e -> showReservations());
  cancel.addActionListener(e -> cancelReservation());

  frame.setContentPane(p);
  frame.setVisible(true);
 }

 // ---------------- BOOKING SCREEN ----------------

 private void showBooking() {
  db(() -> {
   List<String> trains = service.getTrains();

   if (trains.isEmpty()) {
    error("No trains found. Run database/schema.sql.");
    return;
   }

   setup("Book a Ticket", 550, 400);

   JPanel p = panel();
   GridBagConstraints g = new GridBagConstraints();
   g.insets = new Insets(7, 7, 7, 7);
   g.fill = GridBagConstraints.HORIZONTAL;

   g.gridx = 0;
   g.gridy = 0;
   g.gridwidth = 2;
   p.add(heading("New Reservation"), g);

   g.gridwidth = 1;
   g.gridy++;
   g.gridx = 0;
   p.add(new JLabel("Passenger name"), g);

   JTextField name = field(22);
   g.gridx = 1;
   p.add(name, g);

   g.gridy++;
   g.gridx = 0;
   p.add(new JLabel("Train"), g);

   JComboBox<String> train =
           new JComboBox<>(trains.toArray(String[]::new));
   g.gridx = 1;
   p.add(train, g);

   g.gridy++;
   g.gridx = 0;
   p.add(new JLabel("Class"), g);

   JComboBox<String> cls = new JComboBox<>(
           new String[]{
                   "Sleeper",
                   "AC 3 Tier",
                   "AC 2 Tier",
                   "AC First Class"
           }
   );

   g.gridx = 1;
   p.add(cls, g);

   g.gridy++;
   g.gridx = 0;
   p.add(new JLabel("Journey date (YYYY-MM-DD)"), g);

   JTextField date = field(22);
   date.setText(LocalDate.now().plusDays(1).toString());

   g.gridx = 1;
   p.add(date, g);

   JButton back = button("Back");
   JButton confirm = button("Confirm Booking");

   g.gridy++;
   g.gridx = 0;
   p.add(back, g);

   g.gridx = 1;
   p.add(confirm, g);

   back.addActionListener(e -> showDashboard());

   confirm.addActionListener(e -> {
    try {
     String selectedTrain = (String) train.getSelectedItem();

     if (selectedTrain == null) {
      error("Please select a train.");
      return;
     }

     int number = Integer.parseInt(
             selectedTrain.split("\\s*\\|")[0].trim()
     );

     long pnr = service.book(
             currentUser,
             name.getText(),
             number,
             (String) cls.getSelectedItem(),
             LocalDate.parse(date.getText().trim())
     );

     info("Booking successful! Your PNR is: " + pnr);
     showDashboard();

    } catch (DateTimeParseException ex) {
     error("Enter the date in YYYY-MM-DD format.");
    } catch (IllegalArgumentException | SQLException ex) {
     error(ex.getMessage());
    }
   });

   frame.setContentPane(p);
   frame.setVisible(true);
  });
 }

 // ---------------- VIEW RESERVATIONS ----------------

 private void showReservations() {
  db(() -> {
   List<Reservation> list = service.findByUsername(currentUser);

   JTextArea area = new JTextArea();
   area.setEditable(false);
   area.setFont(new Font("Monospaced", Font.PLAIN, 13));

   if (list.isEmpty()) {
    area.setText("You have no reservations yet.");
   } else {
    StringBuilder s = new StringBuilder();

    for (Reservation r : list) {
     s.append(r)
             .append("\n\n----------------------\n\n");
    }

    area.setText(s.toString());
   }

   JScrollPane scroll = new JScrollPane(area);
   scroll.setPreferredSize(new Dimension(530, 310));

   JOptionPane.showMessageDialog(
           frame,
           scroll,
           "My Reservations",
           JOptionPane.INFORMATION_MESSAGE
   );
  });
 }

 // ---------------- CANCEL RESERVATION ----------------

 private void cancelReservation() {
  String input = JOptionPane.showInputDialog(
          frame,
          "Enter your PNR number:"
  );

  if (input == null || input.isBlank()) {
   return;
  }

  try {
   long pnr = Long.parseLong(input.trim());

   db(() -> {
    Reservation r = service.findByPnr(pnr);

    if (r == null || !r.username().equals(currentUser)) {
     error("Reservation not found for your account.");
     return;
    }

    int choice = JOptionPane.showConfirmDialog(
            frame,
            r + "\n\nCancel this reservation?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {
     if (service.cancel(pnr, currentUser)) {
      info("Reservation cancelled successfully.");
     } else {
      error("Cancellation failed.");
     }
    }
   });

  } catch (NumberFormatException ex) {
   error("PNR must be numeric.");
  }
 }

 // ---------------- DATABASE / MESSAGES ----------------

 private void db(Action action) {
  try {
   action.run();
  } catch (SQLException ex) {
   error(
           "Database error: " + ex.getMessage()
                   + "\nCheck MySQL and run database/schema.sql."
   );
  } catch (Exception ex) {
   error("Something went wrong: " + ex.getMessage());
  }
 }

 private void info(String s) {
  JOptionPane.showMessageDialog(
          frame,
          s,
          "Information",
          JOptionPane.INFORMATION_MESSAGE
  );
 }

 private void error(String s) {
  JOptionPane.showMessageDialog(
          frame,
          s,
          "Please check",
          JOptionPane.ERROR_MESSAGE
  );
 }

 @FunctionalInterface
 private interface Action {
  void run() throws Exception;
 }
}
