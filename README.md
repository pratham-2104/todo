# Todo Backend (Quarkus + PostgreSQL + Liquibase)

A simple, robust Todo CRUD backend built with Quarkus, PostgreSQL, Panache ORM, and Liquibase database migrations (YAML format).

---

## Prerequisites

- **Java**: 25 (or compatible JDK)
- **Docker & Docker Compose**: To run the PostgreSQL database container

---

## Quickstart

### 1. Start PostgreSQL with Docker Compose

Start the PostgreSQL container defined in `compose.yaml`:

```bash
docker compose up -d
```

To verify the container is running and healthy:

```bash
docker compose ps
```

- **Database**: `tododb`
- **Username**: `todo`
- **Password**: `todo_password`
- **Port**: `5432`

### 2. Run the Application in Dev Mode

Start Quarkus in dev mode (Liquibase migrations run automatically at startup):

```bash
./gradlew quarkusDev
```

The server starts at `http://localhost:8080`.

---

## API Endpoints

Base URL: `http://localhost:8080/api/todos`

| Method | Endpoint | Description | Status Code |
|---|---|---|---|
| `POST` | `/api/todos` | Create a new todo | `201 Created` |
| `GET` | `/api/todos` | Get all todos (optional `?completed=true/false`) | `200 OK` |
| `GET` | `/api/todos/{id}` | Get todo by ID | `200 OK` / `404 Not Found` |
| `PUT` | `/api/todos/{id}` | Update title, description, and status | `200 OK` / `404 Not Found` |
| `DELETE` | `/api/todos/{id}` | Delete a todo | `204 No Content` / `404 Not Found` |

---

## Example Requests

### 1. Create a Todo
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, and bread"
  }'
```

### 2. Get All Todos
```bash
# Get all
curl -X GET http://localhost:8080/api/todos

# Filter by completion status
curl -X GET "http://localhost:8080/api/todos?completed=false"
```

### 3. Get Todo by ID
```bash
curl -X GET http://localhost:8080/api/todos/1
```

### 4. Update a Todo
```bash
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, bread, and fruits",
    "completed": true
  }'
```

### 5. Delete a Todo
```bash
curl -X DELETE http://localhost:8080/api/todos/1
```

---

## Liquibase Migrations

- Master changelog: `src/main/resources/db/changeLog.yaml`
- Changeset files: `src/main/resources/db/changes/`
  - `001-create-todos-table.yaml`: Creates the `todos` table with primary key, timestamps, and indexes.

---

## Project Structure

```
├── compose.yaml
├── build.gradle.kts
├── src
│   ├── main
│   │   ├── java/com/dyventech/todo
│   │   │   ├── dto
│   │   │   │   ├── CreateTodoRequest.java
│   │   │   │   ├── UpdateTodoRequest.java
│   │   │   │   └── TodoResponse.java
│   │   │   ├── entity
│   │   │   │   └── Todo.java
│   │   │   ├── exception
│   │   │   │   ├── TodoNotFoundException.java
│   │   │   │   └── TodoNotFoundExceptionMapper.java
│   │   │   ├── repository
│   │   │   │   └── TodoRepository.java
│   │   │   ├── resource
│   │   │   │   └── TodoResource.java
│   │   │   └── service
│   │   │       └── TodoService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── db
│   │           ├── changeLog.yaml
│   │           └── changes
│   │               └── 001-create-todos-table.yaml
│   └── test
│       └── java/com/dyventech/todo/service
│           └── TodoServiceTest.java
```
