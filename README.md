# com.enviro.assessment.junior.xolanimbambo
Java OOP investment system for a junior developer assessment. Models investors and portfolios with business rules handled in a service layer, demonstrating clean architecture using encapsulation and enums.
# Enviro365 Investments — Withdrawal Notice System

A full-stack application built for the eTalente Junior Developer Assessment (June 2026). Allows investors to view their portfolios, submit withdrawal notices against business rules, view withdrawal history, and export statements as CSV.

**Author:** Xolani Mbambo
**Package:** `com.enviro.assessment.junior.xolanimbambo`

---

## Tech Stack

- **Backend:** Spring Boot 3.5.16, Java 21, Maven, Spring Data JPA, H2 (in-memory database), Bean Validation
- **Frontend:** React 19 (Vite), Axios, React Router
- **Testing:** JUnit 5, Mockito

---

## Project Structure

```
com.enviro.assessment.junior.xolanimbambo/
├── src/main/java/com/enviro/assessment/junior/xolanimbambo/
│   ├── controller/       REST endpoints (Investor, Portfolio, Withdrawal)
│   ├── service/          Business logic interfaces + implementations
│   ├── repository/       Spring Data JPA repositories
│   ├── model/            JPA entities (Investor, InvestmentProduct, WithdrawalNotice)
│   ├── model/enums/      ProductType, WithdrawalStatus
│   ├── dto/              Request/response DTOs
│   ├── exception/        Custom exceptions + global exception handler
│   └── config/           CORS configuration
├── src/test/java/...     Unit tests
├── frontend/
│   └── src/
│       ├── pages/        Dashboard page (portfolio, withdrawal form, history)
│       ├── services/     API client (axios)
│       └── App.jsx        Routing + layout shell
└── README.md
```

---

## Business Rules Implemented

1. **Retirement withdrawals** are only allowed if the investor's age is **greater than 65**.
2. A withdrawal **must not exceed** the available balance on the product.
3. A withdrawal **must not exceed 90%** of the available balance on the product.
4. Investors must be **at least 18 years old** to be registered on the platform.

All rules are enforced server-side in the service layer and covered by automated unit tests (see `WithdrawalServiceImplTest.java`).

---

## Setup Instructions

### Prerequisites
- Java 21 (JDK)
- Node.js 18+ and npm
- Maven (or use the included Maven Wrapper `mvnw.cmd` / `mvnw` — no separate install needed)

### Backend

```bash
# From the project root
.\mvnw.cmd spring-boot:run
```

The API starts on **http://localhost:8080**. The H2 in-memory database is automatically seeded with sample data on startup (see `src/main/resources/data.sql`) — 3 sample investors with investment products, so the app is usable immediately without manual data entry.

H2 console (for inspecting the database directly) is available at **http://localhost:8080/h2-console** while the app is running (JDBC URL: `jdbc:h2:mem:enviro365db`, username: `sa`, no password).

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The app opens on **http://localhost:5173** and automatically connects to the backend at `localhost:8080`. Both servers must be running simultaneously for the app to function.

### Running Tests

```bash
.\mvnw.cmd test
```

Runs all unit tests, including the full suite of withdrawal business rule tests.

---

## API Documentation

### Investors

| Method | Endpoint | Description |
|--------|----------|--------------|
| `POST` | `/api/investors` | Create a new investor |
| `GET` | `/api/investors` | List all investors |
| `GET` | `/api/investors/{id}` | Get a single investor |
| `PUT` | `/api/investors/{id}` | Update an investor |
| `DELETE` | `/api/investors/{id}` | Delete an investor |
| `GET` | `/api/investors/{id}/portfolio` | Get an investor's portfolio (details + products) |

**Example — create investor:**
```json
POST /api/investors
{
  "firstName": "Sipho",
  "lastName": "Zulu",
  "idNumber": "9001015800087",
  "email": "sipho.zulu@example.com",
  "dateOfBirth": "1990-01-01"
}
```

### Withdrawals

| Method | Endpoint | Description |
|--------|----------|--------------|
| `POST` | `/api/withdrawals` | Submit a withdrawal notice |
| `GET` | `/api/withdrawals/investor/{investorId}` | Get withdrawal history for an investor |
| `GET` | `/api/withdrawals/investor/{investorId}/export` | Download withdrawal history as CSV |

**Example — submit withdrawal:**
```json
POST /api/withdrawals
{
  "productId": 2,
  "amount": 5000,
  "reason": "Home repairs"
}
```

**Example — error response** (business rule violation):
```json
{
  "timestamp": "2026-07-02 08:14:45",
  "status": 422,
  "error": "Unprocessable Entity",
  "message": "Withdrawal amount (44000) exceeds 90% of the available balance. Maximum allowed withdrawal is 40500.00.",
  "path": "/api/withdrawals",
  "details": null
}
```

---

## Advanced Requirements Implemented

- ✅ **Global exception handling** — `GlobalExceptionHandler` with `@RestControllerAdvice`, consistent `ApiError` response shape across all endpoints
- ✅ **DTO layer** — all endpoints use request/response DTOs, never expose JPA entities directly
- ✅ **Input validation** — Bean Validation (`@NotNull`, `@NotBlank`, `@Email`, `@DecimalMin`, `@Past`) on all request DTOs
- ✅ **Unit tests** — full coverage of the three core withdrawal business rules using JUnit 5 + Mockito

---

## AI Usage Disclosure

AI tools (Claude, Anthropic) were used throughout this project's development for:
- Scaffolding boilerplate (entities, DTOs, repositories) following patterns I specified
- Debugging assistance (e.g. diagnosing a PowerShell/curl quoting issue during manual API testing, a stale Maven build causing a bean-not-found error, and an H2 auto-increment collision with seed data)
- Guidance on Spring Boot/JPA/React best practices (e.g. why `BigDecimal` is used for monetary values instead of `double`, transaction boundaries, DTO vs entity separation)

I reviewed, tested, and understand all code in this repository, and am able to explain any design decision or line of code on request, including the reasoning behind the business rule validation logic, the exception handling architecture, and the frontend state management approach.

---

## Screenshots

See the `/Screenshots` folder for:
- Portfolio dashboard
- Withdrawal form (success and validation error states)
- Withdrawal history table
- CSV export output
- H2 console showing seeded data

---

## Known Limitations / Future Improvements

- No authentication/login — investor selection is via a dropdown for demo purposes, matching assessment scope
- CSV export filtering is currently by investor only; could be extended to filter by status/date range
- No pagination on withdrawal history (fine for demo data volume, would need it at scale)