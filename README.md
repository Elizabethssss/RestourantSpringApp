# RestaurantSpringApp

### Functionality

Restaurant system provides next functionality:

* Login and authorization for users
* Looking through menu 
* Checking dishes and their ingredients
* Ordering lunches at a certain time
* Viewing all orders
* Invoicing bills due to orders
* Admin can manage passenger's orders (accept or not)

### Technology stack

* Java 8
* Maven
* Spring Boot
* Spring Security
* HTML
* CSS
* Thymeleaf
* Lombok
* Apache Tomcat
* log4j
* MySQL
* Mockito
* Junit

### Deployment requirements

* Java 1.8 or later
* Maven 3.0 or later


## Installation

### Clone and build
First, clone the project from github:
```
$ git clone https://github.com/Elizabethssss/RestourantSpringApp
```
Then, install it:
```
$ cd RestourantSpringApp
$ mvn clean install
```

### Configure mysql
* change credentials at (__src/main/resources/application.properties__) to your own
* use provided mysqldump at (__src/main/resources/database/schema.sql__) before first run of application


### Run application
* Set up project as web project
* Enable maven auto-import
* Set up project Java SDK
* Run RestaurantSpringAppApplication class