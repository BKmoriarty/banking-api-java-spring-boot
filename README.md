## Banking API

![Java](https://img.shields.io/badge/Java-17-orange) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue) ![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3.13-yellow)

Banking API is a demonstration project built to showcase enterprise-level programming and software design skills using **Clean Architecture** and modern technologies. It simulates a basic banking system with user account management, money transfers, and transaction history retrieval, secured with JWT authentication and integrated with RabbitMQ for asynchronous notifications.

### Features

- **User Management**:
  - User registration and login with JWT authentication
  - Role-based access control (User and Admin roles)
- **Account Management**:
  - Create, update, delete, and retrieve bank accounts
  - Fetch all accounts for a user (GET `/accounts/user/{userId}`)
- **Money Transfer**:
  - Transfer funds between accounts with balance and authorization checks
  - Persist transactions to the database
  - Send asynchronous notifications via RabbitMQ (simulated email with logging)
- **Transaction History**:
  - Retrieve transaction history for an account (GET `/transactions/{accountId}`)
  - Retrieve transaction history for a user (GET `/transactions/user/{userId}`)
  - Supports pagination (`page`, `size`), sorting (`sort`), and time filtering (`days`)
- **Security**:
  - JWT-based authentication and authorization
  - Access restriction: Regular users see only their own data; Admins see all data

### Tech Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.4.3
- **Database**: PostgreSQL 16 (via JPA/Hibernate)
- **Message Queue**: RabbitMQ 3.13 (for asynchronous notifications)
- **Security**: Spring Security + JWT
- **Logging**: SLF4J + Logback
- **Build Tool**: Maven
- **Architecture**: Clean Architecture (Domain, Use Cases, Controllers, Repositories)

### Project Structure
```plaintext
banking-api/
├── src/
│   ├── main/
│   │   ├── java/com/teerasak/bankingapi/
│   │   │   ├── config/         # Configuration (Security, RabbitMQ)
│   │   │   ├── controller/     # REST API Endpoints
│   │   │   ├── domain/         # Entities (User, Account, Transaction)
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── repository/     # Data Access Layer (JPA Repositories)
│   │   │   ├── usecase/        # Business Logic
│   │   │   └── BankingApiApplication.java  # Main Application
│   └── resources/
│       ├── application.properties  # Configuration
│       └── logback-spring.xml      # Logging Config
├── pom.xml  # Maven Dependencies
└── README.md
```

### Prerequisites

- **Java 17**: Install JDK 17
- **Maven**: For build and dependency management
- **PostgreSQL**: Run a local database (e.g., `postgres:16` via Docker)
- **RabbitMQ**: Run a local message broker (e.g., `rabbitmq:3-management` via Docker)

### Setup and Running

#### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/banking-api.git
cd banking-api
```

#### 2. Setup Database
- Run PostgreSQL via Docker:
```bash
docker run -d -p 5432:5432 --name banking-db -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=password -e POSTGRES_DB=banking postgres:16
```
- Update `application.properties`
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/banking
spring.datasource.username=admin
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

#### 3. Setup RabbitMQ
- Run RabbitMQ via Docker:
```bash 
docker run -d -p 5672:5672 -p 15672:15672 --name banking-mq rabbitmq:3-management
```
- Update `application.properties`
```bash
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
rabbitmq.queue.name=transfer-notifications
rabbitmq.exchange.name=transfer-exchange
rabbitmq.routing.key=transfer.routing.key
```
#### 4. Build and Run
```bash
mvn clean install
mvn spring-boot:run
```
- The API will be available at `http://localhost:8080`
### API Endpoints
#### Authentication
- POST `/api/v1/auth/register` - Register a new user
```bash
{
    "username": "teerasak",
    "password": "1234",
    "email": "tee@example.com"
}
```
- POST `/api/v1/auth/login` - Login and retrieve JWT token
```bash
{
    "username": "teerasak",
    "password": "1234"
}
```

#### Accounts
- GET `/api/v1/accounts/user/{userId}` - Fetch all accounts for a user
  - Response:
  ```bash
  [
    {   "accountId": 1, 
        "accountNumber": "ACC-1234-5678", 
        "accountName": "Savings", 
        "balance": 500.0
    }
  ]
  ```
#### Transactions
- POST `/api/v1/transactions` - Transfer money
```bash
{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 500.0
}
```
- GET `/api/v1/transactions/{accountId}` - Fetch account transaction history
    - Query: `?page=0&size=10&sort=timestamp,desc&days=30`
    - Response:
    ```bash
    {
        "transactions": [
            {
                "transactionId": 1, 
                "fromAccountId": 1, 
                "toAccountId": 2, 
                "amount": 500.0, 
                "timestamp": "2025-03-11T10:10:52"
            }
        ],
        "totalElements": 1,
        "totalPages": 1,
        "currentPage": 0,
        "pageSize": 10
    }
    ```
- GET `/api/v1/transactions/user/{userId}` - Fetch user transaction history
    - Query: `?page=0&size=10&sort=amount,asc&days=30`

### Design Decisions
- **Clean Architecture**: Separates Domain, Use Cases, and Infrastructure for maintainability and testability
- **JWT Authentication**: Ensures secure access control based on user roles
- **RabbitMQ**: Decouples notification logic from the core system using a message queue
- **Pagination & Sorting**: Enhances flexibility for handling large datasets

### Future Improvements
- Add unit and integration tests with JUnit and Mockito
- Integrate Swagger/OpenAPI for API documentation
- Support multi-tenancy or internationalization