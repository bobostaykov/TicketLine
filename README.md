# TicketLine

This is a university group project. It represents a web application for selling 
event tickets.

The app runs on localhost. You will need to have NodeJS installed. 
Navigate to *frontend* folder and run `npm install`. Based on the *package.lock* 
file, npm will download all required node_modules to run a Angular application. 
Afterwards, execute `npm install -g @angular/cli` to install the Angular CLI 
globally. To start the frontend, run `ng serve`. To start the backend on Unix, 
run `./mvnw spring-boot:run -Dspring-boot.run.profiles=generateData` in the 
*backend* folder. Then visit `http://localhost:4200/`.

Used technologies:
- Spring Boot
- Angular
- REST
- H2
- JPA
- JUnit
