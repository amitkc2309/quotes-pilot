# quotes-pilot
This application can be used for generating and managing english quotes. 
The application allows authenticated users to search for quotes over a public API and can save and manage those quotes in DB. 
Application's Backend is written in Spring-boot and Frontend is designed in react-js and it uses MySql as DB.

## Running the Application
- Install Docker
- Download [docker-compose.yml](https://github.com/amitkc2309/quotes-pilot/blob/main/docker/docker-compose.yml)
- Start application in your local by running command `docker compose up -d`. The command should be run on the same path where docker-compose.yml is present.
- Once docker has downloaded all the required images, the application will be running on `http://localhost:3000/`
- Register a new user using UI and then you can start using the application.

## Running Application in Development Mode
You can play with the code, make changes and quickly generate your own image of the application.
- Git clone the application in your local.
- Go to path [docker-compose.onlymysql.yml](https://github.com/amitkc2309/quotes-pilot/blob/main/docker/docker-compose.onlymysql.yml) and run `docker compose -f docker-compose.onlymysql.yml up -d`. It will start MySql container.
- Import [Java backend project](https://github.com/amitkc2309/quotes-pilot/tree/main/backend) in an IDE like IntelliJ or Eclipse.
- Make changes in the code as per your need.
- Provide your own docker registery path in pom.xml (inside `<repository>` section of `<artifactId>dockerfile-maven-plugin</artifactId>`)
- run `mvn package`, It will generate deployables and also create a new docker image for your backend code in local.
- Now for the front end, import [Front-end code](https://github.com/amitkc2309/quotes-pilot/tree/main/frontend_web/quote-app) in your favourite IDE like VS-Code.
- Make changes and run `npm start` to start the application on `http://localhost:3000/` to test.
- Go to [front-end docker file](https://github.com/amitkc2309/quotes-pilot/blob/main/frontend_web/quote-app/Dockerfile) and run  `docker build -t <your-docker-registery>/quotes-pilot-frontend`. It will create a new docker image for your frontend code in local.
- Now, go to [docker-compose.yml](https://github.com/amitkc2309/quotes-pilot/blob/main/docker/docker-compose.yml) and provide your own docker-registery name as the value of `image` variable.
- Start application in your local by running command `docker compose up -d`.

## Authentication
All Spring-boot REST end-points are protected by JWT token using spring-security. If you are runing Java backend project only and want to test it using tools 
like postman, then following is how you should access these end-points: 
- A new User can be registered at end-point `/register` (POST request) (http://localhost:5000/register/). This path does not require any authentication. Request body should look 
like this-
  ```
    {
        "name": "ram",
        "password": "ram"
    }
  ```
- Login can be done at '/login' (POST request). Request body should look like this-
  ```
    {
        "name": "ram",
        "password": "ram"
    }
    ```
   If login is successful then code will return an JWT token.
- Use JWT token returned as the response of `/login` request, in the headers for all subsequent requests. The token shoule be sent as a Bearer token in Authorization header-
`Authorization` : `Bearer ${JWT token}` 

## Available REST end-points.
For all of the Http request listed below, you must add Authorization token as explained before.
- Adding a quote: `http://localhost:5000/quote/add-quote/` (POST request). Request body-
```
{
        "id": 1,
        "text": "Example quote",
        "author": "myself",
        "tags": [
            "testTag"
        ]
    }
```
- Searching for a quote:`http://localhost:5000/quote/search?query=<search_text>` (GET request)
- Get All gaved Tags for logged-in User:`http://localhost:5000/quote/tags`. 
- Search quotes by Tag for logged-in User:`http://localhost:5000/quote/search-by-tag?tag=<search_text>`.
- Remove quote for logged-in user: `http://localhost:5000/remove-quote/{id}`.
- Get a random quote: `http://localhost:5000/quote/random`.

## Others
- You can change the port on which backend java application is running by changing value of `server.port` in application.properties file. Currently it is set as 5000 
to be easily runnable on AWS. If you decide to change it then same should be done in `config.json` file of front-end project.
- application.properties also lists the MySql credentials and DB name. This should be same as given in docker-compose file of MySql service.
