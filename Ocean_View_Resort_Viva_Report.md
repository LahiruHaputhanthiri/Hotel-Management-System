# Ocean View Resort Management System - Viva Report

## 1. Project Information
**Project Title:** Ocean View Resort Management System
**Developer:** Lahiru Haputhanthiri
**Project Type:** Degree-Level Software Engineering Project (Full-Stack Web Application)
**GitHub Repository:** `LahiruHaputhanthiri/Hotel-Management-System`

---

## 2. Abstract
The **Ocean View Resort Management System** is a comprehensive, full-stack web application designed to digitalize and streamline resort operations. Built to replace manual administrative tasks, this secure and scalable solution facilitates guest registration, room allocation, reservations, billing, and administrative management. It seamlessly connects guest-facing booking workflows with dynamic administrator-facing dashboards to reduce booking conflicts and maximize resort efficiency.

---

## 3. Problem Statement & Objectives
**Problem:** Traditional resort management relies heavily on manual ledgers or decentralized spreadsheets, leading to booking overlaps, delayed responses, data loss, and inefficient resource allocation.
**Objectives:**
- To develop a centralized web-based platform for room reservations.
- To implement secure role-based access control for Guests, Admins, and Super Admins.
- To provide real-time availability checking and step-by-step booking workflows.
- To design an N-Tier software architecture ensuring scalability, clean code, and maintainability.

---

## 4. Key Features & Modules

### 4.1 Guest-Facing Features
- **Authentication:** Secure user registration, login, and email verification.
- **Reservation Workflow:** Real-time room search, step-by-step booking functionality, and booking confirmations.
- **User Dashboard:** Profile management, booking history viewing, reservation cancellation, and invoice downloads.

### 4.2 Administrator & Super Admin Features
- **Centralized Dashboard:** Real-time insights into resort occupancy and revenue.
- **Reservation Management:** View, update, and manage the status of guest bookings.
- **Room Management:** dynamically manage room details, capacity, pricing, and availability statuses.
- **System Administration:** Super Admin capabilities to manage system users and elevate staff roles.

---

## 5. Technology Stack

### 5.1 Frontend (Presentation Tier)
- **Languages:** HTML5, CSS3, JavaScript (ES6+).
- **Design Philosophy:** Custom responsive UI featuring modern "Glassmorphism" aesthetics for a premium user experience.

### 5.2 Backend (Controller & Service Tiers)
- **Core Technology:** Java 17+
- **Enterprise Framework:** Jakarta EE
- **Web Components:** Servlets 6.0 and JSP 3.1
- **Email Services:** JavaMail API for notifications and booking confirmations.

### 5.3 Database (Data Access Tier)
- **Database Engine:** MySQL 8.0+
- **Integration:** Custom DAO (Data Access Object) implementations.

---

## 6. System Architecture
The application strictly follows an **N-Tier Architecture**, ensuring separation of concerns:
1. **Presentation Tier:** JSP files, CSS, and JS responsible for rendering the UI.
2. **Controller Tier:** Java Servlets that intercept HTTP requests, enforce security, and route application flow.
3. **Service Tier:** Encapsulates core business logic, including booking validations, pricing calculations, and workflow management.
4. **DAO Tier:** Manages structured database communication and SQL execution securely.
5. **Integration Tier:** Connects the core system with external nodes like MySQL databases and SMTP email servers.

---

## 7. Security & Best Practices implementation
- **Authentication Filters:** Enforces logical role validation before allowing access to secure routes (`AuthFilter`).
- **CSRF Protection:** Form submissions are guarded against Cross-Site Request Forgery.
- **Cryptography:** Secure password hashing utilities.
- **Session Management:** Secure HTTP-only cookies and session tracking.
- **Defensive Programming:** Null handling, safe Enum mapping, and input sanitization to prevent SQL injection.

---

## 8. Academic Learning Outcomes Demonstrated
This system practically demonstrates mastery over:
- Object-Oriented Analysis and Design (OOAD).
- UML System Modelling (Use Case, Class, Sequence Diagrams).
- Secure Full-Stack Web Development.
- Implementation of the DRY (Don't Repeat Yourself) principle and clean code utilities.
- Software testing, documentation, and Git-based version control.

---

## 9. Future Enhancements
- Integration of a third-party Online Payment Gateway (e.g., Stripe, PayPal).
- API-based frontend separation (e.g., using React/Next.js).
- Advanced automated report generation and role-based audit logging.
- SMS notification integration for instant guest updates.
- Interactive room image gallery and guest review/rating module.

---

**End of Report**
