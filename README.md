# Project Title: URL Shortener
This project is an implementation of a URL Shortener in order to manage and minimize the length of long urls.  A URL shortener is a service that translates long URLs to short abbreviated alternatives and offers URL redirections.
## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
#### Prerequisites
1. Knowledge of JavaSE and JavaFX
1. Understanding of MySQL, JAX-RS and Web servlets
#### Installing and Configuration
1. Install the IDE of your choice to run Java Application.
1. Setup a new dynamic Web application project or import this project.
1. Setup the glassfish or relevant server.
1. Include all the required Jar files with the project.
1. The Main.java file is the main file of the project and will be used to start the URL shortening application.
1. After the program is running, enter the url you want to shorten.
1. The shortened web url will then be inserted into the database for which a user can also check dynamic dashboard including how many times the shortened url has been accessed.
##### Running
###### Javafx Frontend
Run the Main.java file and enter long url in order to shorten it
###### Web Service
The application will be running on localhost:8080/
Enter the valid short url in order to be redirected to the actual page
###### Rest Service
Access all the entries - localhost:8080/UrlShortener/rest/service
Access with respect to Id - localhost:8080/UrlShortener/rest/service/{EnterId}
#### Built with
This application is built using:
1. Javafx
1. MySQL
1. Web Servlets
1. JAX-RS
1. Bootstrap
#### Versioning
Version 1.0
#### Author
* Shaleem John - Application Developer