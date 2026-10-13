# OIBSIP Java Task 1 — Online Reservation System

Java Swing desktop application using JDBC and MySQL for the Oasis Infobyte Java Development track.

## Features
- User registration and login
- SHA-256 password hash storage for this educational demo
- Username uniqueness and password validation (8+ characters, at least one digit)
- Train list with route details
- Ticket reservation with automatically generated PNR
- Journey date validation
- View only the logged-in user's reservations
- PNR lookup and cancellation restricted to the booking user
- Prepared statements and try-with-resources for database access

## Tech stack
Java 21, Swing, JDBC, MySQL 8+, Maven, MySQL Connector/J.

## Setup
1. Install JDK 21, Maven 3.9+, and MySQL Server.
2. Open `database/schema.sql` in MySQL Workbench and execute it.
3. Edit `src/main/resources/application.properties` and set your MySQL password.
4. From the project directory run:
   ```bash
   mvn clean compile
   mvn exec:java -Dexec.mainClass=com.oasis.reservation.Main
   ```
   You can also open the folder in IntelliJ, reload Maven, and run `com.oasis.reservation.Main`.

## Demo flow
Register → log in → Book a Ticket → note PNR → View My Reservations → Cancel Reservation → log out.

Sample trains are fictional demo data and are not live railway schedules. This is not an official railway booking service.

## Security note
SHA-256 is used only as a basic educational hash. For production applications, use salted password hashing such as Argon2id or bcrypt. Never commit real database credentials to a public repository.

## Submission checklist
- [ ] Configure MySQL and run the application locally
- [ ] Test registration, login, booking, viewing and cancellation
- [ ] Capture screenshots and record a functioning demo video
- [ ] First 2 seconds of video show full name, assigned track and task title
- [ ] Push to public repository `OIBSIP` under `Java-Task1-OnlineReservationSystem/`
- [ ] Post demo to LinkedIn, tag Oasis Infobyte and add `#oasisinfobyte`
- [ ] Leave substantive comments on two other interns' demo videos
- [ ] Submit repository link through the official form by 15 November 2026
