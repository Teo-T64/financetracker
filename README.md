# Personal Finance Tracking API 
> Backend service for the `financetrackingwebapp`

This is a RESTful API designed to manage personal transactions, custom categories, and user profiles.

---

##  Database Architecture
The database is designed to handle multi-tenant data, ensuring users only access their own financial records.

### Entity Relationship Diagram (ERD)
<img width="1321" height="817" alt="FinanceTracker" src="https://github.com/user-attachments/assets/2e067765-d358-4bb8-ae59-7ab0e36998ba" />

**Key Logic:**
* **Precise Currency:** Uses `Decimal(38,2)` to avoid floating-point rounding errors common in financial applications.
* **Category Mapping:** Transactions are linked to both a `Profile` and a `Category` for granular filtering.
* **User Isolation:** All queries are scoped by `profile_id` for security.

---

## Features
* **Authentication:** Secure registration and login using JWT (JSON Web Tokens).
* **Transaction Management:** Full CRUD operations for Expenses and Incomes.
* **Custom Categorization:** Users can create and update their own categories with custom icons.
* **Financial Overviews:** Logic to calculate total balance, monthly spending, and income streams as well as filtering the transactions by given features(start/end date, sort ordering, sort fields).
* **Data Validation:** Prevents negative transaction amounts and ensures category ownership.

---

## Tech Stack
* **Language:** Java
* **Framework:** SpringBoot
* **Database:** PostgreSQL (production Database), MySQL(local database)
* **Security:** Spring Security Configuration
---

## Key API Endpoints 

| Method | Endpoint | Description | Auth |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | Create a new account | No |
| `GET` | `/api/v1/categories` | List all user categories | Yes |
| `POST` | `/api/v1/expenses` | Create a new expense | Yes |
| `DELETE` | `/api/v1/expenses/${id}` | Delete a certain expense | Yes |

---

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone [https://github.com/Teo-T64/financetrackingapi.git](https://github.com/Teo-T64/financetrackingapi.git)
   cd financetrackingapi
