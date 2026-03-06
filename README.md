# Ocean View Resort Management System

A full-stack web application developed to digitalize and streamline resort operations such as guest registration, room allocation, reservations, payments, billing, and administrative management.

## Project Overview

The **Ocean View Resort Management System** was developed as a degree-level software engineering project to replace manual resort administration with a secure, scalable, and user-friendly web-based solution. The system supports both **guest-facing services** and **administrator-facing operations**, helping reduce booking conflicts, improve service efficiency, and provide better visibility into resort performance.

The application is designed around a layered architecture and follows good software engineering practices such as separation of concerns, reusable utility components, secure authentication, session management, and structured database access.

## Key Features

### Guest Features
- Secure user registration and login
- Email verification and booking confirmation
- Search available rooms in real time
- Step-by-step reservation workflow
- View booking history
- Cancel reservations
- Manage personal profile
- Download invoice or booking-related documents

### Administrator Features
- Centralized admin dashboard
- View reservation records
- Update reservation status
- Manage room details and availability
- Monitor occupancy and revenue-related information
- Manage system users and staff roles (Super Admin)

## Technology Stack

### Frontend
- HTML5
- CSS3
- JavaScript (ES6+)
- Custom responsive glassmorphism-based UI

### Backend
- Java 17+
- Jakarta EE
- Servlets 6.0
- JSP 3.1

### Database
- MySQL 8.0+

### Security and Utilities
- CSRF protection
- Secure session management with cookies
- Password hashing through utility-based implementation
- Authentication and authorization filters
- JavaMail API for notifications and verification

## System Architecture

The project follows an **N-Tier Architecture** to keep the codebase maintainable and scalable.

- **Presentation Tier** – JSP, JavaScript, and CSS for rendering the user interface
- **Controller Tier** – Servlets for handling requests and application flow
- **Service Tier** – Business logic such as booking validation, billing, and email handling
- **DAO Tier** – Database communication and SQL execution
- **Integration Tier** – MySQL database and email service integration

## Core Modules

- User Authentication Module
- Room Management Module
- Reservation Management Module
- Payment and Billing Module
- Dashboard and Analytics Module
- Profile and Booking History Module
- Email Notification Module

## UML Diagrams

The project documentation includes the following diagrams:
- Use Case Diagram
- Class Diagram
- Sequence Diagram

> You can place your exported UML images inside a folder such as `docs/images/` and update the README with embedded image links if needed.

## Project Structure

```text
src/main/java/com/oceanview/controller   -> Servlet controllers
src/main/java/com/oceanview/service      -> Business logic services
src/main/java/com/oceanview/dao          -> Data access objects
src/main/java/com/oceanview/model        -> Domain models, enums, POJOs
src/main/webapp/WEB-INF/views            -> JSP view templates
src/main/webapp/css                      -> Stylesheets
src/main/webapp/js                       -> JavaScript assets
```

## Installation and Setup

### Prerequisites
- Java JDK 17 or higher
- MySQL Server 8.0 or higher
- Apache Tomcat 10.x or another Jakarta EE compatible servlet container
- IDE such as IntelliJ IDEA or Eclipse

### Setup Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/LahiruHaputhanthiri/Hotel-Management-System.git
   ```
2. Open the project in your IDE.
3. Configure the project server runtime with Tomcat.
4. Create the MySQL database for the project.
5. Import the SQL schema and seed data if available.
6. Update database connection settings in the project configuration files.
7. Configure email credentials for JavaMail if email verification is enabled.
8. Build and deploy the project to the server.
9. Run the application in the browser.

## Sample User Roles

- **Guest** – searches rooms, creates reservations, views booking history
- **Admin** – manages rooms and reservations
- **Super Admin** – manages administrative users and system-level controls

## Design Principles Applied

- Clean code and reusable utilities
- DRY principle
- Layered separation of concerns
- Secure authentication flow
- Defensive null handling and safe enum mapping
- Responsive and user-friendly interface design

## Academic Context

This system was developed as a **graduate/degree-level academic project** to demonstrate practical understanding of:
- Object-oriented analysis and design
- Full-stack web development
- Database design and SQL integration
- Secure software engineering practices
- UML-based system modelling
- Testing and software documentation
- Version control using Git and GitHub

## GitHub Repository

This project is hosted on GitHub:

**Repository:**
`LahiruHaputhanthiri/Hotel-Management-System`

**GitHub Link:**
```text
https://github.com/LahiruHaputhanthiri/Hotel-Management-System
```

## Future Improvements

- Online payment gateway integration
- Advanced report generation
- Role-based audit logging
- Room image gallery management
- SMS notifications
- API-based frontend integration
- Review and rating system for guests

## Author

**Project:** Ocean View Resort Management System  
**Developer:** Lahiru Haputhanthiri

## License

This project is currently provided for academic and demonstration purposes.
