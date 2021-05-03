# ServiceOrderManager
 Just a personal project
 
<br>Ps.: Unitary tests unavaible because they have not been finalized, check the pull request on the link below: 
<br>https://github.com/gustavnienkotter/ServiceOrderManager/pull/1
 
# How to configure API:
Its simple, just set the database connection in resources/application.yml, the Spring will boot and create the admin user if then not exists (I will made something better to create the admin in the future).
If API successful up, you can access the Swagger by https://server:port/context-path/swagger-ui and login with the admin (credentials are below), now you can use all of the API

# Important:
Access Swagger to tests the endpoints (use https://server:port/context-path/swagger-ui, eg https://localhost:8080/swagger-ui);
<br>You must be logged to use the endpoints;
<br>Only the admin can create new Users;
<br>Default Admin credentials:
- username: admin
- password: admin

# Project structure:
 
 /Config:
  - Has all Spring security configurations, line 28 was commented for tests, but this is a important line to protect for cross site requests.
 
 /Controller:
  - Is where all classes that receive the requests and call their proper method. I wanna (I don't know at this moment if it is possible) put a Default controller, where will get all in common methods like create, update and delete.
  - In each method in the controllers has notes for Swagger quickly explaining what each method does, waits for and returns. I wanna use Enums to better the code visualization and avoid future bugs/errors on this Swagger annotations (I tried but I had some problems to change, so I left the texts hardcoded). 
 
 /dto:
  - I separeted then in subfolder because Service Order will use more than one DTO, I used more than one to facilitate validations.
 
 /enums:
  - I put some unused Enums (Ex.: SwaggerTagsEnum) to future implementations, Explained on Controller
 
 /exception:
  - Classes to handle the messages of expected exceptions;
 
 /handler:
  - Where are the ExceptionHandler;
 /model
  - Where are all the models
  
  /projectModel
   - I created projection models to control what the response will return, without it, the response responses were returning fragile data (eg all user information);
 
 /repository
  - Where the communication to database where made
  - I wanna put a default repository (I don't now if its possible) because has some querys in common in each repository
 
 /service
  - Where are all the business rules
  - Some methods has the @Transactional Annotation, I put that to rollback if a problem occurs
  - I wanna create a defaultService, to implements methods in common, like save, create, delete, update and the builders
  - The builders methods (eg serviceOrderbuilder, serviceOrderProjectionBuilder) is importants to convert Entitys to Projections or DTO to Entity.
 
 /util
  - Has all util methods, in the moment I use just DateUtil

# Request flow

Send the Json to API > Spring converts Json to DTO and send the DTO to Controller > Controller send to Service > Service convert DTO to Entity with the builder > Service handles the Entity according to the business rule > Service send the Entity to Repository to save > repository returns to Controller a Projection > Spring convert Projection to JSON and return then.

# Used libs:
Java 11

Spring (version: 2.4.5):
 - Security
 - DevTools
 - Web
 - JPA

Lombok

SpringDoc(version: 1.5.8) (Contains Swagger and others stuffs to future implements)
 - Swagger
