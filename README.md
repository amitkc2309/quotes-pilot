# quotes-pilot
This application can be used for generating and managing english quotes. 
The application allows authenticated users to search for quotes over a public API and can save and manage those quotes in DB. 
Application's Backend is written in Spring-boot and Frontend is designed in react-js and it uses MySql as DB.

## Running the Application on Docker
- Install Docker
- Download [docker-compose.yml](https://github.com/amitkc2309/quotes-pilot/blob/main/docker/docker-compose.yml)
- Start application in your local by running command `docker compose up -d`. The command should be run on the same path where docker-compose.yml is present.
- Once docker has downloaded all the required images, the application will be running on `http://localhost:3000/`
- Register a new user using UI and then you can start using the application.

## Running the Application on Kubernetes
- Install Docker
- Intall `minikube` , `kubectl` and  `helm`
- start minikube using command `minikube start --driver=docker --insecure-registry true`
- Create namespace for our application on kubernetes using command `kubectl create namespace quotes-pilot`
- We can now deploy application on kubernetes in 2 ways-
  - First method is to use kubectl. Go to `kubernetes/quotes-pilot/templates` folder and run following command-
    - kubectl apply -f mysql-deployment.yaml (wait 2 mins)
    - kubectl apply -f backend-deployment.yaml
    - kubectl apply -f frontend-deployment.yaml
  - Second method is to use `Helm`-
    - Go to `kubernetes/quotes-pilot` and run `helm package quotes-pilot`
    - Install generated packege on kubernetes using command `helm install quotes-pilot quotes-pilot-0.1.0.tgz`
- Open minikube dashboard using command `minikube dashboard`
- Go to namespace `quotes-pilot`. Here you should see the all 3 `services` named `backend`, `frontend` and `mysql` deployed. Pods should be started.
- Run `minikube service frontend --url --namespace=quotes-pilot` to expose frontend on a URL. Open URL on browser to access the application.
- Run `kubectl port-forward service/backend 5000:5000 --namespace quotes-pilot` and then you can start using the App. Here is why we need this command-
  - Kubernetes pods can connect internally using `service` name. For example `backend` is connected to `mysql`.
  - But, We can not connect React front-end to `backend` using service name. Because React is running on browser and browser does not recognise any of the services running on kubernetes i.e it does not know what `backend` means. Meaning from front-end we will connect to backend using backend's URL only. 
  - Here also we have a problem, On local machine kubernetes will not let you connect to `http://localhost:5000` and you will get `ERR_CONNECTION_REFUSED` error while react will try to connect to backend. This error will not happen on production environment because `backend` will have a domain name but on local machine we can port forward `5000` to `backend` service using command `kubectl port-forward service/backend 5000:5000 --namespace quotes-pilot`

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
to be easily runnable on AWS. If you decide to change it then same should be done in `.env` file of front-end project.
- application.properties also lists the MySql credentials and DB name. This should be same as given in docker-compose file of MySql service.
