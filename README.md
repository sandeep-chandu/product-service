# product-service mricroservice

### Stack used:
   *  Java 8
   *  Springboot 2.7.9
   *  Junit5 and Mockito frameworks for unit and integration testing
   *  Maven build tool
   *  H2 database
   *  Open API Swagger for API specification
   *  Auth0 platform for Authentication and Authorisation. [Auth0](https://auth0.com/)
   *  GitHub actions for Continous Itegration
   *  DockerHub container registry
   *  Docker for containerisation
   
## How to run and test?

### Using docker image from DockerHub
Make sure you have docker installed on the machine.
  1.  Open cmd or temrnial and run the following command to pull the image from DockerHub
  
    docker pull sandeepchandu/product-service-1.0
  
  2.  Run the image using docker run command below, wait for the application to get deployed.
  
    docker run -p9999:8080 sandeepchandu/product-service-1.0
    
  3.  Access swagger at http://localhost:9999/swagger-ui/index.html
  
  4.  To access API end points you need to fetch the token, invoke the below Curl command to get the same
      
       **Read only token**: can perform GET operations only 
       ``` 
       curl --request POST --url https://dev-l0bv5x78cpcytc8k.us.auth0.com/oauth/token  --header 'content-type: application/json' --data '{"client_id":"5tptNmrATV6fHNZKDxyCmTpVnYx0CYiM","client_secret":"jZAdYzWEqcL-aE8Iq2qTpr5YSm6ZdtIgLlAHvGiRa-7E-YG79zXLTK_jyABXxnwn","audience":"https://cake-store/","grant_type":"client_credentials"}' 
       ```
       
       **Read & Write token**: can invoke GET, POST, PATCH, PUT and DELETE operations on /products endpoint 
       ``` 
       curl --request POST --url https://dev-l0bv5x78cpcytc8k.us.auth0.com/oauth/token --header 'content-type: application/json' --data '{"client_id":"uFuAR7howxGo5Rwb3k7j7zBEQ6HGzwwB","client_secret":"gjy6B7fRuHvF7_bsAyk71OeYq70keuvY2W_tDr3zICe60DjiEhHqG2h3rM4gXNR6","audience":"https://cake-store/","grant_type":"client_credentials"}' 
        ```
        Copy the value of access_token from the response.
  5. Once the you have access token store/update it in the Authorize section of swagger and invoke the APIs to test.
  
### Checkout code, build and test using maven
  1.  Clone this repo (master) branch
  
  2.  Navigate to /product-service folder in the terminal and run the below command
  
      ```
        mvn spring-boot:run
      ```
     
  3.  Access swagger at http://localhost:8080/swagger-ui/index.html
  
  4.  Inorder to access API you need Bearer token, to get same refer to step 4 in previous method (Using docker image from DockerHub).
  
  5. Once the you have access token store/update it in the Authorize section of swagger and invoke the APIs to test.


### Checkout code, build build docker image and run the image
  You can build a docker image and run it as well.
  
Thanks!
