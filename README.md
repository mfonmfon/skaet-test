# USSD Banking Application

A scalable, **modular monolithic** USSD banking application built with Spring Boot that allows users to create accounts, deposit, withdraw, and check balances.

## Architecture

This application follows a **Modular Monolithic Architecture** with clear separation of concerns organized into independent, loosely-coupled modules.

### Modules

1. **User Module** - User registration and management
2. **Account Module** - Account operations (deposit, withdraw, balance)
3. **Transaction Module** - Transaction recording and history
4. **USSD Module** - USSD protocol handling and menu navigation
5. **Wallet Module** - Wallet management (future expansion)
6. **Common Module** - Shared exceptions and utilities

### Module Structure

Each module contains:
- **Domain Models** (entities)
- **Repositories** (data access)
- **Services** (business logic with interfaces)
- **DTOs** (data transfer objects)
- **Controllers** (where applicable)

### Key Benefits

- **Clear Boundaries**: Each module is self-contained
- **Loose Coupling**: Modules communicate through interfaces
- **Easy Testing**: Mock dependencies at module boundaries
- **Scalable**: Can evolve into microservices
- **Maintainable**: Easy to locate and modify code

## Features

- User Registration (phone number, name, PIN)
- Account Creation with unique account number
- Check Balance
- Deposit Money
- Withdraw Money
- Transaction History
- Session Management
- Exception Handling
- Transaction Tracking

## Technology Stack

- Java 21
- Spring Boot 4.0.0
- Spring Data JPA
- MySQL Database
- H2 Database (for testing)
- Maven
- Lombok

## Database Schema

### Users Table
- id, phoneNumber, fullName, pin, createdAt, updatedAt

### Accounts Table
- id, accountNumber, balance, userId, createdAt, updatedAt

### Transactions Table
- id, transactionReference, accountNumber, type, amount, balanceBefore, balanceAfter, description, createdAt

### USSD Sessions Table
- id, sessionId, phoneNumber, currentMenu, sessionData, createdAt, expiresAt

## Setup Instructions

### Prerequisites
- Java 21
- Maven
- MySQL Server

### Configuration

1. Update database credentials in `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ussd_banking?createDatabaseIfNotExist=true
    username: your_username
    password: your_password
```

### Running the Application

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### USSD Endpoint
```
POST /api/ussd
Content-Type: application/json

{
  "sessionId": "unique-session-id",
  "phoneNumber": "+1234567890",
  "text": "1*100",
  "serviceCode": "*123#"
}
```

### Health Check
```
GET /api/ussd/health
```

## USSD Flow

### New User Registration
```
1. Dial *123#
2. Enter full name
3. Enter 4-digit PIN
4. Receive account number
```

### Existing User Menu
```
1. Check Balance
2. Deposit
3. Withdraw
4. Transaction History
```

## USSD Response Format

- `CON` prefix: Continue session (user needs to provide more input)
- `END` prefix: End session (final response)

## Scalability Features

1. **Stateless Design**: Session data stored in database, not in memory
2. **Transaction Management**: ACID compliance with @Transactional
3. **Repository Pattern**: Easy to switch data sources
4. **Service Layer Separation**: Business logic isolated from controllers
5. **Exception Handling**: Centralized error handling
6. **Connection Pooling**: Built-in with Spring Boot
7. **Actuator Endpoints**: Health checks and metrics for monitoring

## Security Considerations

- PIN storage (consider hashing in production)
- Input validation
- SQL injection prevention (JPA)
- Session expiration (5 minutes)
- Transaction isolation

## Future Enhancements

- PIN encryption/hashing
- Transfer between accounts
- Mini-statement via SMS
- Multi-language support
- Rate limiting
- Audit logging
- Redis for session management
- Microservices architecture
- API Gateway integration

## Testing

```bash
# Run tests
mvn test
```

## Monitoring

Access actuator endpoints:
- Health: `http://localhost:8080/actuator/health`
- Metrics: `http://localhost:8080/actuator/metrics`
- Info: `http://localhost:8080/actuator/info`

## License

MIT License
