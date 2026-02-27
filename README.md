# RAG API – Chat Storage Microservice

This is a Spring Boot REST API built to manage chat histories for RAG-based systems. It features secure session management, message persistence with pagination, and a fully containerized environment.

##  Key Features
- Session Management:- Create, rename, delete, and mark sessions as favorite.
- Message Storage:- Save messages with sender details and retrieved context.
- Security:- Protected by API Key Authentication (Custom Filter).
- Scalability:- Built-in Pagination for message history retrieval.
- Monitoring:- Health check endpoints using Spring Actuator.
- Database Management:- Dockerized PostgreSQL with pgAdmin for easy browsing.

## Tech Stack
- Language:- Java 21
- Framework:- Spring Boot 3.x
- Security:- Spring Security (API Key based)
- Data:- Spring Data JPA & PostgreSQL
- Documentation:- Swagger / OpenAPI
- Monitoring:- Spring Actuator (Health Checks)
- DevOps:- Docker & Docker Compose

##  Project Architecture Flow

- Client → Spring Boot API (Auth Filter) → Service Layer → JPA Repository → PostgreSQL (Docker)

##  Prerequisites
- Before running the project, ensure you have the following installed:
* Docker & Docker Compose

## Available APIs
  - Chat Session APIs

      ** Create Session **
        - Endpoint: http://localhost:8081/sessions
        - Methor: POST
        - Description: Create a new chat session for a user.
        - Response: Session details.
       
      ** Get Session by ID **
        - Endpoint: http://localhost:8081/sessions/{id}
        - Methor: GET
        - Description: Retrieve a single chat session by its ID.
        - Response: Session details.

      ** Get Sessions by User **
        - Endpoint: http://localhost:8081/sessions/users/{userId}
        - Methor: GET
        - Description: Retrieve all sessions for a given user.
        - Response: List of sessions.

      ** Update Session **
        - Endpoint: http://localhost:8081/sessions/{id}
        - Methor: PATCH
        - Description: Update the session title or status.
        - Response: Updated session details.

      ** Delete Session **
        - Endpoint: http://localhost:8081/sessions/{id}
        - Methor: DELETE
        - Description: Delete a session by ID.
        - Response: Success/failure status..
    
  - Chat Message APIs
      ** Create Message **
        - Endpoint: http://localhost:8081/sessions/{sessionId}/messages
        - Methor: POST
        - Description: Add a new message to a session.
        - Response: Return message object.

       ** Get Messages **
        - Endpoint: http://localhost:8081/sessions/{sessionId}/messages?page=0&size=20
        - Methor: GET
        - Description: Retrieve messages for a session with pagination.
        - Response: Paginated list of messages.
        -  Query Params:
            - page – page number (default 0)
            - size – page size (default 20)

       ** Delete Message **
        - Endpoint: http://localhost:8081//messages/{messageId}
        - Methor: DELETE
        - Description: Delete a message by ID.
        - Response: Success/failure status..

                        ##  Setup & Installation

## 1. Clone the Repository

- git clone https://github.com/majidkhana030-gif/rag-chat-storage-service.git
- cd <project-folder-name>

## 2. Configuration
- Copy the .env.example file to create a new .env file.
- Update the values in .env (like API_KEY) as per your requirement.

## 3. Run using Docker
- Run below command to build java appliation and start the PostgreSQL and pgAdmin containers.
- docker-compose up --build

  - if facing issue during build, run below command for manually pulling images
    - docker pull maven:3.9.4-eclipse-temurin-21
    - docker pull eclipse-temurin:21-jdk
    - docker pull postgres:15
    - docker pull dpage/pgadmin4:latest

## 4. Access & Monitoring
- API Documentation (Swagger)
   - http://localhost:8081/swagger-ui/index.html
- Health Monitoring
   - http://localhost:8081/actuator/health

## 5. Database Management (pgAdmin)
Browse PostgreSQL data:
- URL: http://localhost:8080
- Login Email: admin@example.com
- Login Password: admin
