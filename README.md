# FlavorMetrics API

FlavorMetrics is a robust RESTful API built with Spring Boot 3 that allows users to manage recipes, provide ratings, and interact through a secure and role-based authorization system.
This API is designed with clean architecture principles, DTO separation, and data validation, making it scalable and easy to maintain.

---

## 🚀 Features

- ✅ User registration and authentication (JWT-based)
- ✅ Role-based access control (ADMIN / USER)
- ✅ Recipe CRUD operations
- ✅ Rating system for recipes
- ✅ Profile and email management
- ✅ Ownership filtering and data projection via DTOs
- ✅ Exception handling with meaningful HTTP responses
---

## 🧱 Tech Stack

| Technology      | Purpose                                 |
|-----------------|-----------------------------------------|
| Java 21         | Core language                           |
| Spring Boot 3   | Application framework                   |
| Spring Security | JWT-based authentication                |
| Hibernate (JPA) | ORM and persistence                     |
| PostgreSQL      | Relational database                     |
| Maven           | Build and dependency management         |
| Mockito         | Mocking dependencies and service layers |
| JUnit 5         | Unit testing framework                  |
| AssertJ         | Fluent and expressive test assertions   |

---

## 🗂️ Domain Model Overview

### Entities

- **User**
  - First name, last name, email (wrapped in a value object)
  - Roles (authorities)
  - Profile, ratings, and owned recipes


- **Recipe**
  - Name, description, difficulty, dietary preferences
  - Linked to an owner (user)
  - Can be rated by other users


- **Rating**
  - Linked to a user and a recipe
  - Score from 1 to 5
  - DTO projection supports score and ownership


- **Profile**
  - Profile picture URL
  - Bio / description
  - Linked one-to-one with a User


- **Email**
  - Wrapper for user email address
  - Allows future extensibility (e.g. verified flag, change history)
  - Embedded or referenced from User


- **Authority**
  - Defines user roles (e.g. ROLE_USER, ROLE_ADMIN)
  - Used by Spring Security for access control
  - Mapped many-to-many with Users


- **Recipe**
  - Name, description
  - Difficulty level (enum)
  - Dietary preferences (e.g. vegan, gluten-free)
  - Linked to an owner (User)
  - Can be rated by other users
  - Contains multiple ingredients, tags, and allergy warnings

* Rating
  * Score from 1 to 5
  * Linked to a user and a recipe
  * Used in DTO projections to show ownership and rating details


* Ingredient
  * Name, quantity, unit
  * Associated with a Recipe
  * Cascade persisted with the parent Recipe


* Allergy
  * Common allergens (e.g. peanuts, dairy)
  * Linked to Recipes to help filter based on user sensitivities
  * Cascade persisted with the parent Recipe


* Tag
  * Free-form keywords or categories (e.g. "keto", "quick", "Italian")
  * Used for organizing and searching Recipes
  * Cascade persisted with the parent Recipe

---

## 📦 DTOs

- `UserDto`: Public view of a user
- `RegisterResponse`: Returned after user registration
- `RatingDto`: Maps rating with user and recipe
- `RatingWithScore`: Projection used in user mappings
- `RecipeDto`: Lightweight representation of a recipe
- `RecipeByOwner`: Groups recipes by a specific owner
- `ProfileDto`: Public view of a user's profile
- `LoginResponse`: Returned after user authentication
- `DataWithPaginations`: Groups recipes by a specific page-size and page-number

---

## 🔐 Authentication

- JWT token issued after login
- Roles and authorities used to restrict endpoints
- Email-based user identification
- Endpoints protected via custom security filter

---

## 🧪 Testing

- Unit tests for services, mappers
- Integration tests for repositories
- Assertions via AssertJ and Mockito Assertions

To run tests and generate coverage:

```bash
mvn clean test
```

## 📦 Build & Run
### Prerequisites

Java 21

Maven 3.8+

Docker & Docker Compose

PostgreSQL (local or container)

### !!! Don't skip this
Create a .env file on the root folder of the project with properties:

* SPRING_DATASOURCE_URL=jdbc:postgresql://<localhost | postgres>:5432/<your_db>
* SPRING_DATASOURCE_USERNAME=<your_db_username>
* SPRING_DATASOURCE_PASSWORD=<your_db_password>
* POSTGRES_DB=<your_db>
* POSTGRES_USER=<your_db_username>
* POSTGRES_PASSWORD=<your_db_password>
* JWT_SECURITY_KEY=<your_super_secret_key> (min 32 characters)
* IMAGE_KIT_URL=<your_imagekit_url>
* IMAGE_KIT_PRIVATE_KEY=<your_imagekit_private_key>
* IMAGE_KIT_PUBLIC_KEY=<your_imagekit_public_key>

### ▶️ Option 1: Run with Maven

```bash
mvn clean install
```

```bash
mvn spring-boot:run
```
App will be available at: http://localhost:8080

### 🐳 Option 2: Run with Docker Compose
You can spin up the Spring Boot app + PostgreSQL using Docker Compose.

Run with Docker Compose:

```bash
docker-compose up --build
```

This will start:

1. [ ] PostgreSQL on port 5432
2. [ ] 
3. [ ] Spring Boot app on port 8080

Access the app: http://localhost:8080
