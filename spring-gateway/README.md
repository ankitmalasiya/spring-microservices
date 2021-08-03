<artifactId>spring-cloud-starter-gateway</artifactId>

With above dependency, this application will act as gateway for other services.
User will access all the services from one url/port only and the actual end points will 
be mapped to the services. 

In microservices architecture, users access services using gateway. Routes with uris are
defined in application yml gateway > routes.

http://localhost:8999/cars
http://localhost:8999/customers
