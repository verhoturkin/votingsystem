[![Codacy Badge](https://api.codacy.com/project/badge/Grade/53a402e5f60749d3a4f8580156429c65)](https://www.codacy.com/app/verhoturkin/votingsystem?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=verhoturkin/votingsystem&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/verhoturkin/votingsystem.svg?branch=master)](https://travis-ci.org/verhoturkin/votingsystem)

# Simple voting system
## Task
Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) **without frontend**.
Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
    - If it is before 11:00 we assume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed
    
Each restaurant provides new menu each day.

## Install
Clone project:
```console
git clone https://github.com/verhoturkin/votingsystem.git
```
Deploy:
```console
$ mvn clean package
$ mvn cargo:run
```
Note that you should have to [install JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) and [Maven](https://maven.apache.org/install.html) as prerequisite.

## API v1
### Authorization
Use http basic authorization.
Built-in profiles:
```console
Admin: admin@gmail.com : admin
User1: user1@yandex.ru : password
User2: user2@yandex.ru : password
```
### Users management
#### User DTO

Field Name | `type` | Description
--- | --- | ---
id | `integer` | Null when create new
name | `string` | **REQUIRED**, 2-100 chars
email | `string` | **REQUIRED**,  max. 100 chars 
password | `string` | **REQUIRED**,**WRITE-ONLY**, 5 to 32 chars 

#### Get all users
----

| URL | Method | URL Params | Data Params |
|-----|--------|------------|-------------|
| [/api/v1/users](http://localhost:8080/voting/api/v1/users) | GET | - | - | 

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `{ id : 12, name : "Michael Bloom" }`
 * **Error Response:**
  * **Code:** 404 NOT FOUND <br />
    **Content:** `{ error : "User doesn't exist" }`
  OR
  * **Code:** 401 UNAUTHORIZED <br />
    **Content:** `{ error : "You are unauthorized to make this request." }`

* **Sample Call:**

  ```
  ```





