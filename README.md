#(LMS) Learning_Management_System 
This repository contains dockerized springBoot application for Learning Management System. It contains APIs to store, retrieve courses and its relevant schedules.

## Introduction
LMS (Learning Management System) is an application developed in SpringBoot and java to maintain Courses their Schedules and booking using H2 Database. It is a back-end application and provides apis to execute CRUD operations on Courses.

## Technologies
-Spring Boot <br />
-JDK 8 <br />
-H2 Database <br />
-Swagger UI <br />
-Docker <br />

## APIs

**Application health check:** <br />
/actuator/health 

**Courses:**

POST - /api/courses -> Create and save a course. <br />
GET -  /api/courses -> Get all courses. <br />
UPDATE - /api/courses/{courseId} -> Update a course <br />

## Launch
Steps to run the application
Step 1: Download the docker-compose.yaml file from source Repo: https://github.com/Shreya11Jalihal/lmsapplication 
Step 2: Run docker-compose up
