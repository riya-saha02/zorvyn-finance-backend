# Finance Data Processing & Access Control Backend

A robust Spring Boot backend system designed for managing financial records with strict role-based access control (RBAC). Developed as part of the **Zorvyn SDE Internship Assessment**.

---

## 🚀 Project Overview
This system serves as the core engine for a finance dashboard. It manages user authentication, role-based permissions, and financial transaction processing. The architecture follows a clean, layered approach (Controller-Service-Repository) to ensure maintainability and scalability.

## 🛠️ Tech Stack
* **Java**: 17 (JDK)
* **Framework**: Spring Boot 3.4.2
* **Security**: Spring Security 6 with **JWT (JSON Web Token)**
* **Database**: H2 In-Memory Database (for easy assessment evaluation)
* **ORM**: Spring Data JPA / Hibernate
* **Documentation**: SpringDoc OpenAPI (Swagger UI)

---

## 🚦 Getting Started

### Prerequisites
* Java 17 or higher
* Maven 3.6+

### Installation & Run
1.  **Clone the repository**:
    ```bash
    git clone [your-repo-link]
    ```
2.  **Build and Run**:
    Open the project in IntelliJ IDEA and run `ExpenseTrackerApplication.java`, or use the terminal:
    ```bash
    mvn spring-boot:run
    ```
3.  **Access the Interactive API Documentation (Swagger)**:
    Once the app starts, open your browser to:
    `http://localhost:8082/swagger-ui/index.html`

---

## 🔒 Access Control Logic
The system implements strict **Role-Based Access Control (RBAC)** to meet the assessment requirements:

| Role | Permissions |
| :--- | :--- |
| **ADMIN** | Full Management: Create, Read, Update, and Delete all records and users. |
| **ANALYST** | Insight Access: View all records and access aggregated dashboard summaries. |
| **VIEWER** | Read-Only: Can only view the list of transaction entries. |

---

## 📊 API Endpoints

### 1. Authentication
* `POST /api/auth/register`: Publicly create a new user and assign a role.
* `POST /api/auth/login`: Authenticate and receive a JWT Bearer Token.

### 2. Financial Transactions
* `GET /api/transactions`: View all entries (Accessible by all roles).
* `POST /api/transactions`: Create a new record (**Admin Only**).
* `PUT /api/transactions/{id}`: Update an existing record (**Admin Only**).
* `DELETE /api/transactions/{id}`: Remove a record (**Admin Only**).

### 3. Dashboard Summary
* `GET /api/dashboard/summary`: Returns aggregated data: Total Income, Total Expenses, Net Balance, and Category-wise totals (**Admin/Analyst Only**).

---

## 🗄️ Database & Testing
* **H2 Console**: Visit `http://localhost:8082/h2-console` to view live tables.
    * **JDBC URL**: `jdbc:h2:mem:testdb`
    * **User**: `sa` | **Password**: `password`
* **Validation**: All inputs are validated using `jakarta.validation` to ensure data integrity (e.g., non-negative amounts, required categories).
* **Testing**: The project includes a `test.http` file for rapid API testing within IntelliJ IDEA.

---

## 💡 Key Design Decisions
1.  **Security**: Chose **JWT** for statelessness, making the backend more scalable for potential frontend integration.
2.  **Persistence**: Utilized **H2 In-Memory** to allow reviewers to run the project instantly without configuring a local MySQL/PostgreSQL instance.
3.  **Documentation**: Integrated **Swagger UI** to provide a professional, interactive way for evaluators to test the API without needing external tools like Postman.