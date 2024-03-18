PROJECT IS STILL UNDER DEVELOPMENT!

# What to Watch

This is a Spring based web app with REST API written in Java, that allows users to manage their movie lists (in future
also tv shows).

## Table of Contents

* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#Features)
* [Project Status](#project-status)
* [Movie Recommendation Module Summary](#movie-recommendation-module-summary)

## General Information

- It is my second project.
- The purpose of the project is to learn new technologies, mainly Spring Boot.
- Project architecture focused on ports and adapters.
- Application works on Hibernate, tests are ran on H2 database.
- Integrates with external API.
- The project implements the Command Query Responsibility Segregation pattern.

## Technologies Used

- Java - version 17.0.5
- Maven
- Spring Boot
- Spring Security
- REST
- Hibernate
- Lombok
- GIT

## Features

- Add new movies to the list
- Update existing movie details.
- Remove movies from the list.
- Assigning covers to movies.
- Adding comments.
- Adding tags and searching movies by them.
- Saves cover files in "files" folder in project's directory.
- Recommending movies based on configuration using external API.

## Project Status

Project is still under development. I am currently using it to learn new technologies and gain practice in coding.

## Testing the app:

Repositories are filled with data at application start while using profile "test".
In order to test you can just import the file from httprequests folder to postman. File has all
endpoints handled in the app.

## Information about the data:

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

## Recommendation Configuration

- Entity
- Responsible for storing user-specific movie genre preferences.
- Configurations are uniquely identified by a configuration ID and associated with a user ID.
- Enables the update of genre preferences.

## Recommended Movies

- Entity
- Stores a list of recommended movies for a user, along with relevant metadata.
- Each set of recommended movies is identified by a unique ID.
- Includes creation and end intervals to manage the validity period of recommendations.

## Recommendation Configuration Manager

- Manages operations related to recommendation configurations, such as creation, update, and retrieval.
- Facilitates access to user-specific configurations and provides a list of all users with configurations.

## Recommendation Service

- Orchestrates the recommendation process by interfacing with the configuration manager and an external movie
  information API.
- Recommends popular movies and generates personalized recommendations based on user preferences.
- Utilizes a clock to determine the recommendation interval and checks if recommendations have been made during the
  current period.

## Movie Information API

- Implements the MovieInfoApi interface to interact with external movie API (currently The Movie Database).
- Retrieves popular movies, movie genres, and movies by specified genres.
- Maps genre names to corresponding TMDB API IDs.