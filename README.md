# Introduction

This is Charlie's Take-Home Assessment 

# Task
Your task is to design a parking lot service using object-oriented principles. The starter project includes an in memory db and a few other dependencies. Please spend no more than four hours. This ensures fairness and consistency in the evaluation.
# Assumptions
1. The parking lot has 25 total spaces
2. The parking lot can hold motorcycles, cars and vans
3. The parking lot has motorcycle spots, compact car spots and regular spots
4. A car can park in a single compact spot, or a regular spot
5. A van can park, but it will take up 3 regular spots (for simplicity we don't need to make
sure these spots are beside each other)
# Acceptance Criteria
1. Service endpoints
    1. Park vehicle
    2. Vehicle leaves parking lot
    3. Find how many spots are remaining
    4. Check if all parking spots are taken for a given vehicle type

#  Solution

As it was required to get this resolved in 4hrs. I've made the following decisions:
- Parking lots are created on app boostrapping.
- No parking lot and car hierarchies were defined because there was no requirement supporting that design decission.
- Car visit concept was not defined relating a parking lot with a cat because there was no requirement for it. A parking lot gets an vehicle assigned
- Spring Boot:  No need of response entity for tweaking the API response
- No high level databases management was adopted for sake of simplicity like Flyway and Liquibase.


# Running the tests

To run test, you can use gradle:

`./gradlew test`

# Checking source code quality

First run SonarQube server

`docker run  --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest`

Update the default password to a new one

`curl -u admin:admin -X POST "http://localhost:9000/api/users/change_password?login=admin&previousPassword=admin&password=newAdminPassword"`

Then run sonnar on the source code

`./gradlew sonar`

Finally you can check the project health at:
`[http://localhost:9000/](http://localhost:9000/dashboard?id=assessment%3Aparking-lot-service)`

using `admin` and `newAdminPassword` as credentials

# To run  the app

`./gradlew bootRun

# Swagger console

The swagger console is availabnle at `http://127.0.0.1:8080/swagger-ui/index.html`


## Manual test

1. Park vehicle

```
curl -X 'POST' \
  'http://127.0.0.1:8080/parkinglot/findLotAndPark' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "plate": "van1",
  "type": "Car"
}'
```



2. Find how many spots are remaining

```
curl -X 'POST' \
  'http://127.0.0.1:8080/parkinglot/countavailable/' \
  -H 'accept: */*' \
  -d ''

```

3. Check if all parking spots are taken for a given vehicle type

```
curl -X 'POST' \
  'http://127.0.0.1:8080/parkinglot/takenlotsbyvehicle/van1' \
  -H 'accept: */*' \
  -d ''

  ```

4. Vehicle leaves parking lot

```
curl -X 'GET' \
  'http://127.0.0.1:8080/parkinglot/release/van1' \
  -H 'accept: */*'
  ```

2. Find how many spots are remaining

```
curl -X 'POST' \
  'http://127.0.0.1:8080/parkinglot/countavailable/' \
  -H 'accept: */*' \
  -d ''

```
