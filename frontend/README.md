
# 📈 Trading Frontend Application

## 📝 Introduction

This is a full-stack trading application built using **React** (frontend) and **Node.js/Express** (backend). The app allows users to manage traders and track real-time stock quotes.

### 🔍 Key Areas
- **Trader Dashboard** – Manage trader data.
- **Quotes Page** – Display real-time stock quotes.
- **Trader Account Page** – View trader details and manage account balances.

### ✅ Key Features
- **Trader Management**: Add, delete, and view traders (with name, email, DOB, country, and account balance).
- **Account Transactions**: Deposit or withdraw money for each trader.
- **Quote Display**: Pulls real-time stock data from Alpha Vantage (via RapidAPI), cached for better performance.

### ⚙️ Technologies Used
- **Frontend**: React, Axios, SCSS, Ant Design, Font Awesome
- **Backend**: Node.js, Express
- **API**: Alpha Vantage via RapidAPI
- **Caching**: Manual in-memory cache (5-minute refresh)

---

## 🚀 Quick Start

### 📥 Clone the Project

```bash
git clone https://github.com/jarviscanada/jarvis_data_eng_PiyushSethi.git
cd jarvis_data_eng_PiyushSethi
```

### 🔧 Backend Setup

```bash
cd frontend/backend-trader-quotes
npm install
node index.js  # Starts backend on http://localhost:8080
```

### 💻 Frontend Setup

```bash
cd ../trading-ui
npm install
npm start  # Starts frontend on http://localhost:3000
```

Visit `http://localhost:3000` to use the app.

---

## 🏗️ Implementation

### 🧠 Backend

The backend provides a RESTful API to support trader and quote functionality.

#### 📌 Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/traders` | Fetch all traders |
| `GET` | `/dashboard/traders/:id` | Get a trader by ID |
| `POST` | `/trader/:firstName/:lastName/:dob/:country/:email` | Add a new trader |
| `DELETE` | `/trader/:id` | Delete a trader |
| `PUT` | `/dashboard/trader/deposit/:id/amount/:amount` | Deposit amount to trader |
| `PUT` | `/dashboard/trader/withdraw/:id/amount/:amount` | Withdraw amount from trader |
| `GET` | `/quotes/dailyList` | Get real-time quotes (5-minute in-memory cache) |

> Quotes are fetched from Alpha Vantage using Axios and cached using JavaScript variables.

---

### 🎨 Frontend

The frontend is built with React and styled using Ant Design and SCSS.

#### 🧩 Pages & Features

- **Dashboard Page**: Add, delete, and view trader list.
- **Trader Account Page**: Deposit or withdraw funds and view trader profile.
- **Quotes Page**: Display daily stock quotes for symbols like AAPL, GOOGL, AMZN, TSLA, MSFT.

---

## 🧪 Test

The application was manually tested to verify functionality across pages and API interactions.

### ✅ Testing Scenarios

- **CRUD Operations**:
  - Successfully added and deleted traders.
  - Verified backend data changes accordingly.

- **Account Transactions**:
  - Performed deposits and withdrawals.
  - Checked balance updates and error messages on invalid input.

- **Quotes API**:
  - Ensured quotes fetch correctly from Alpha Vantage.
  - Verified caching logic works (data doesn't refetch within 5 minutes).

- **UI Testing**:
  - Navigated between Dashboard, Trader Account, and Quotes pages.
  - Validated form inputs and error handling.

---

## 🚀 Deployment

- Code is hosted on [GitHub](https://github.com/jarviscanada/jarvis_data_eng_PiyushSethi).
- Backend is ready to be containerized using Docker (optional enhancement).
- Frontend can be deployed via Netlify, Vercel, or served via NGINX.

---

## 🔧 Improvements

Here are a few enhancements that could be added to improve the app:

1. **Authentication & Authorization**  
   Add login/logout with role-based access (admin/trader).

2. **Real-Time Quote Updates**  
   Use WebSockets or periodic polling to auto-refresh quote data.

3. **Improved Caching**  
   Replace manual in-memory cache with Redis for persistence and multi-user support.

4. **Mobile Responsiveness**  
   Optimize layout and components for smaller screens.

5. **Testing Framework**  
   Implement unit and integration tests using Jest, React Testing Library, and Supertest.

6. **Dockerized Deployment**  
   Add Docker and Docker Compose for smoother deployment across environments.

---

## 📎 Acknowledgements

- [Alpha Vantage API](https://rapidapi.com/alphavantage/api/alpha-vantage)
- [Ant Design](https://ant.design/)
- [Font Awesome](https://fontawesome.com/)
