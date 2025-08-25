# Bookstore Inventory Management System

A comprehensive, enterprise-grade REST API for managing bookstore inventory with advanced search capabilities, role-based access control, and scalable architecture.

## Architecture Overview

### Technology Stack
- **Framework**: Spring Boot 3.2.0 (Java 17)
- **Database**: PostgreSQL 15 with JPA/Hibernate
- **Security**: Spring Security with JWT authentication
- **Documentation**: OpenAPI 3 (Swagger)
- **Containerization**: Docker with multi-stage builds
- **Testing**: JUnit 5 with Testcontainers

### Design Principles
- **Layered Architecture**: Clear separation of concerns (Controller → Service → Repository)
- **Domain-Driven Design**: Rich domain models with business logic
- **RESTful API**: Standard HTTP methods and status codes
- **Security First**: Role-based access control (RBAC)
- **Scalability**: Optimized database queries and pagination
- **Maintainability**: Comprehensive documentation and testing

## Key Features

### Core Functionality
- **Book CRUD Operations**: Create, read, update, and delete books
- **Advanced Search**: Multi-criteria search with pagination
- **Inventory Management**: Stock tracking and low stock alerts
- **Author & Genre Management**: Flexible categorization system
- **User Management**: Role-based access control

### Security Features
- **JWT Authentication**: Stateless token-based authentication
- **Role-Based Access**: Admin and User roles with different permissions
- **Input Validation**: Comprehensive data validation and sanitization
- **Audit Trail**: Automatic timestamp and version tracking

### API Features
- **Pagination**: Efficient handling of large datasets
- **Filtering**: Multiple search criteria and sorting options
- **Documentation**: Interactive Swagger UI
- **Error Handling**: Consistent error responses and status codes

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL 15 (if running locally)

## Quick Start

### Option 1: Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone https://github.com/yumahalme/bookstoreManagment.git
   cd BookstoreHometask
   ```

2. **Start the application**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - API: http://localhost:8080/api/v1
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - Database: localhost:5432 (postgres/password)
   - pgAdmin: http://localhost:5050 (admin@bookstore.com/admin)

### Option 2: Local Development

1. **Set up PostgreSQL database**
   ```sql
   CREATE DATABASE bookstore_inventory;
   CREATE USER postgres WITH PASSWORD 'password';
   GRANT ALL PRIVILEGES ON DATABASE bookstore_inventory TO postgres;
   ```

2. **Configure environment variables**
   ```bash
   export DB_USERNAME=postgres
   export DB_PASSWORD=password
   export DB_HOST=localhost
   export DB_PORT=5432
   export DB_NAME=bookstore_inventory
   export JWT_SECRET=your-secret-key-here
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## Authentication

### Default Users
- **Admin**: username: `admin`, password: `password`
- **User**: username: `user`, password: `password`

### JWT Token Usage
```bash
# Login to get token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Use token in subsequent requests
curl -H "Authorization: Bearer <your-jwt-token>" \
  http://localhost:8080/api/v1/books
```

## API Endpoints

### Authentication
- `POST /api/v1/auth/login` - User login and JWT token generation
- `GET /api/v1/auth/health` - Authentication service health check

### Book Management
- `POST /api/v1/books` - Create a new book (Admin only)
- `GET /api/v1/books` - Get all books with pagination
- `GET /api/v1/books/{id}` - Get book by ID
- `PUT /api/v1/books/{id}` - Update book (Admin only)
- `DELETE /api/v1/books/{id}` - Delete book (Admin only)
- `POST /api/v1/books/search` - Advanced search with criteria

### Search & Filtering
- `GET /api/v1/books/genre/{genreName}` - Books by genre
- `GET /api/v1/books/author/{authorName}` - Books by author
- `GET /api/v1/books/publisher/{publisher}` - Books by publisher
- `GET /api/v1/books/price-range` - Books by price range
- `GET /api/v1/books/year-range` - Books by publication year

### Inventory Management
- `GET /api/v1/books/low-stock` - Books with low stock (Admin only)
- `GET /api/v1/books/in-stock` - Books currently in stock
- `GET /api/v1/books/out-of-stock` - Books out of stock (Admin only)
- `PUT /api/v1/books/{id}/stock` - Update stock quantity (Admin only)

### Statistics & Reports
- `GET /api/v1/books/statistics` - Inventory statistics (Admin only)

## Database Schema

### Core Entities
- **Book**: Central entity with title, ISBN, price, stock, etc.
- **Author**: Book authors with biography and contact information
- **Genre**: Book categories with hierarchical support
- **User**: System users with authentication details
- **Role**: User roles for access control

### Key Relationships
- Books ↔ Authors: Many-to-many
- Books ↔ Genres: Many-to-many
- Users ↔ Roles: Many-to-many

### Database Indexes
- Primary keys on all entities
- ISBN uniqueness constraint
- Search indexes on title, author, genre
- Performance indexes on price, stock, year

## Testing

### Running Tests
```bash
# Unit tests
mvn test

# Integration tests with Testcontainers
mvn verify

# Test coverage report
mvn jacoco:report
```

### Test Strategy
- **Unit Tests**: Service layer business logic
- **Integration Tests**: Repository layer with Testcontainers
- **API Tests**: Controller endpoints with Spring Security Test
- **Database Tests**: Real PostgreSQL instance in containers

## Deployment

### Production Considerations
- **Environment Variables**: Secure configuration management
- **Database**: Production PostgreSQL with proper backup strategy
- **Security**: Strong JWT secrets and HTTPS
- **Monitoring**: Health checks and metrics endpoints
- **Scaling**: Horizontal scaling with load balancers

### Docker Production
```bash
# Build production image
docker build -t bookstore:latest .

# Run with production environment
docker run -d \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-db-host \
  -e JWT_SECRET=your-production-secret \
  bookstore:latest
```

## Configuration

### Application Properties
```yaml
# Database
spring.datasource.url: jdbc:postgresql://localhost:5432/bookstore_inventory
spring.datasource.username: ${DB_USERNAME:postgres}
spring.datasource.password: ${DB_PASSWORD:password}

# JWT
jwt.secret: ${JWT_SECRET:your-secret-key}
jwt.expiration: ${JWT_EXPIRATION:86400000}

# API
server.servlet.context-path: /api/v1
server.port: ${SERVER_PORT:8080}
```

### Environment-Specific Configs
- **dev**: Development with detailed logging
- **docker**: Containerized environment
- **prod**: Production with minimal logging

## Monitoring & Health

### Actuator Endpoints
- `/actuator/health` - Application health status
- `/actuator/info` - Application information
- `/actuator/metrics` - Performance metrics

### Health Checks
- Database connectivity
- Application responsiveness
- Memory and disk usage

## Security Features

### Authentication
- JWT token-based authentication
- Configurable token expiration
- Secure password storage with BCrypt

### Authorization
- Role-based access control (RBAC)
- Method-level security with @PreAuthorize
- Resource-level permissions

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- XSS protection

## Future Enhancements

### Planned Features
- **E-commerce Integration**: Shopping cart and order management
- **Advanced Analytics**: Sales reports and trend analysis
- **Multi-language Support**: Internationalization
- **API Rate Limiting**: Request throttling
- **WebSocket Support**: Real-time inventory updates
- **Mobile App**: React Native companion app

### Scalability Improvements
- **Caching**: Redis integration for performance
- **Message Queues**: Async processing with RabbitMQ
- **Microservices**: Service decomposition
- **Kubernetes**: Container orchestration

## Contributing

### Development Setup
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

### Code Standards
- Follow Java coding conventions
- Add comprehensive documentation
- Include unit tests for new features
- Update API documentation


---

**Built with Spring Boot and modern Java technologies**
