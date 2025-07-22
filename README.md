# ✨ Simple API and Frontend Application

Link to challenge: [here](https://github.com/hmcts/dts-developer-challenge/blob/master/README.md)

## 📝 Overview

This repository contains a simple Task Management system for HMCTS caseworkers. It consists of:

* **Backend API**: A Spring Boot service exposing RESTful endpoints under `/api/v1/tasks`.
* **Frontend Application**: A JS single-page app that interacts with the backend and visually displays tasks, including a connection-status indicator.

## 📦 Tech Stack

* **Backend**

    * Java 21, Spring Boot v3.5.0
    * Gradle build system
    * H2 in-memory database (default) or PostgreSQL
    * Jakarta Validation for request DTOs
* **Frontend**

    * HTML, CSS, JS with Axios
    * Runs via a static file server (e.g., `python3 -m http.server 6969`)
    * No external frameworks

## 🔖 Versioning

All API routes are versioned under **`v1`** (e.g., `/api/v1/tasks`) to allow future compatibility and non-breaking changes.

---

## 🛠 Backend – API Endpoints

Base URL: `http://localhost:4000/api/v1/tasks`

| HTTP Method | Endpoint       | Description                             |
| ----------- | -------------- | --------------------------------------- |
| `POST`      | `/`            | Create a new task                       |
| `GET`       | `/`            | Retrieve all tasks (sorted by due date) |
| `GET`       | `/{id}`        | Retrieve a single task by its ID        |
| `PUT`       | `/{id}`        | Update title, description, or due date  |
| `PUT`       | `/{id}/status` | Update only the task’s status           |
| `DELETE`    | `/{id}`        | Delete a task by its ID                 |
| `DELETE`    | `/delete-all`  | Delete *all* tasks                      |

### Validation & Error Handling

* Request DTOs are validated with Jakarta Validation.
* Custom exceptions:

    * `TaskNotFoundException` → 404 Not Found
    * `InvalidStatusException` → 400 Bad Request
* Global exception handler returns JSON payloads for frontend consumption.

---

## 🌐 Frontend Application

**File structure**:

* `index.html` – main UI
* `index.css` – styling
* `index.js` – API calls using Axios and DOM manipulation
* `icon.png` – favicon and logo

**Features**:

* Create, view, update, and delete tasks via forms and buttons
* Connection status polling every **20 seconds**

    * A small dot in the header turns **green** when the backend is reachable and **red** when not
* Automatic refresh of the task list after modifications

### 🚀 Running the Frontend

1. Navigate to the `FRONT-END/` directory:

   ```bash
   cd FRONT-END
   ```
2. Launch a static server on port `6969`:

   ```bash
   python3 -m http.server 6969
   ```
3. Open your browser at `http://localhost:6969`

> **Note**: This is a custom JS implementation, not using the provided starter template.

---

## 🗄️ Database Configuration

The backend can run with either:

* **H2 In-Memory Database** (default): no setup required
* **PostgreSQL**:
    1. In `application.yml`, set the active profile:

       ```yaml
       spring:
         profiles:
           active: postgresql
       ```
    2. Provide your PostgreSQL credentials in `application-postgresql.yml` or via environment variables.

---

## ▶️ Running the Application

### Backend

1. Build and run with Gradle:

   ```bash
   ./gradlew bootRun
   ```
2. Service listens on `http://localhost:4000` (configured via server.port in application.yml).

### Frontend

See [Frontend Application](#-frontend-application).

---

## 📄 Original Repositories

* [Backend Starter Repo](https://github.com/hmcts/hmcts-dev-test-backend)
* [Frontend Starter Repo](https://github.com/hmcts/hmcts-dev-test-frontend)

---
## 🔧 Future Enhancements

### Looking ahead, here are the improvements and features I plan to implement:

* Integrate HMCTS Frontend Template: Migrate my custom JS/CSS app into the official HMCTS frontend framework for consistency and maintainability.

* Version 2 with HATEOAS: Introduce a v2 API layer leveraging HATEOAS to make endpoints truly RESTful and self-descriptive.


