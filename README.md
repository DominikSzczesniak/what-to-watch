PROJECT IS STILL UNDER DEVELOPMENT!

# What to Watch

This is a Java-based web app using Spring and REST API. It helps users manage their movie lists and provides 
recommendations based on their preferences from an external API.

## General Information

- The purpose of the project is to learn new technologies, mainly Spring Boot.
- Project architecture focused on ports and adapters.
- High coverage with unit tests and integration tests
- Application works on Hibernate, tests are ran on H2 database.
- Facade design pattern.
- Integrates with external API.
- The project implements the Command Query Responsibility Segregation pattern.

## Technologies Used

![image](https://img.shields.io/badge/17-Java-orange?style=for-the-badge) &nbsp;
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring) &nbsp;
![image](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white) &nbsp;<br />
![image](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white) &nbsp;
![Postgres](	https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white) &nbsp;
![image](https://img.shields.io/badge/GIT-E44C30?style=for-the-badge&logo=git&logoColor=white) &nbsp;

## Features

- Adding new movies to the list
- Updating existing movie details.
- Removing movies from the list.
- Assigning covers to movies.
- Adding comments.
- Adding tags and searching movies by them.
- Saves cover files in "files" folder in project's directory.
- Scheduled recommendation of movies based on configuration using external API.

## Testing the app:

Repositories are filled with data at application start while using profile "test".
In order to test you can just import the file from httprequests folder to postman. File has all
endpoints handled in the app.

### Information about the data:

Created user's credentials are:

**Username: Dominik** 

**Password: 123**

User's repository is filled with 10 movies, 2 of them already moved to watchedMovies.

# Movie Recommendation Module Summary

The Movie Recommendation Module is a component within the movies app
designed to enhance user experience by providing personalized movie
recommendations based on user preferences. The module incorporates
several key classes to manage recommendation configurations, retrieve
movie information from an external API, and store recommended movies for users.
The module is designed to be extensible and configurable, allowing to integrate
additional features or adapt it to different movie information APIs.

## Recommendation Configuration Manager

- Manages operations related to recommendation configurations, such as creation, update, and retrieval.
- Helps users access their personalized settings and provides a list of all users with configurations.

## Movie Information API

- Implements the MovieInfoApi interface to interact with external movie API (currently The Movie Database).
- Retrieves popular movies, movie genres, and movies by specified genres.
- Maps genre names to corresponding API IDs.

## Recommendation Service

- Coordinates the recommendation procedure by linking with both the configuration manager and an external movie information API.
- Recommends popular movies and generates personalized recommendations based on user preferences with the use of 
  scheduler.
- Utilizes a clock to determine the recommendation interval and checks if recommendations have been made during the
  current period.