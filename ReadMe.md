# Round-Up Feature for Starling Customers

<img width="1439" alt="Screenshot 2025-03-20 at 14 30 28" src="https://github.com/user-attachments/assets/bbd7de12-8785-4dda-9d25-d1730ee433eb" />
<img width="1439" alt="Screenshot 2025-03-20 at 14 31 04" src="https://github.com/user-attachments/assets/024fbd5f-169b-4c98-a1aa-cac5e3ef9c09" />

## Overview
Note: I have removed some functionality for the saving goal to prevent cheating for future applicants for this tech test.

Despite facing a personal setback with an ankle injury during the development period, the project was completed on schedule.

This project implements a "round-up" feature for Starling customers using the public developer API. The feature takes all the transactions in a given week, rounds them up to the nearest pound, and transfers the rounded-up amount into a savings goal. This helps customers save for future adventures.

## Tech Stack
- Backend: Java with Spring Boot
- Frontend: React

## Project Structure

### Backend

#### Main Application
The main application class is `RoundChallengeIbrahimApplication`. It sets up the Spring Boot application and provides a `RestTemplate` bean for making API calls.

#### Controllers
- `RetrieveTransactionController`: Handles requests to fetch transactions
- `RoundUpController`: Handles requests to calculate the round-up amount and transfer it to a savings goal

#### Services
- `TransactionFetchService`: Fetches transactions from the Starling API
- `RoundUpService`: Calculates the round-up amount and handles the transfer to savings goals

#### DTOs
- `AccountResponse`: Represents the response for account details
- `FeedItemsResponse`: Represents the response for transaction feed items
- `SavingsGoal`: Represents a savings goal

#### Tests
- `AccountServiceTest`: Tests for the account service
- `RoundChallengeIbrahimApplicationTests`: Tests for the main application
- `RoundUpServiceTest`: Tests for the round-up service
- `TransactionFetchServiceTest`: Tests for the transaction fetch service

### Frontend

#### Components
- `RoundUpSection`: Displays the round-up amount and provides a button to perform the transfer
- `TransactionsList`: Displays a list of transactions with filtering and pagination
- `AccountList`: Displays a list of accounts

#### Main Dashboard
The main dashboard component is `Dashboard`. It integrates the different sections and handles the periodic fetching of data.

#### Services
`api.js`: Contains functions to interact with the backend API

## Configuration

### Important Setup
Before running the application, you must configure your Starling API token:
1. Navigate to `src/main/resources/application.properties`
2. Insert your Starling API token in this file
   
## Running the Project

### Backend
1. Navigate to the project root directory
2. Build the project using Gradle:
```bash
./gradlew build
```
3. Run the Spring Boot application:
```bash
./gradlew bootRun
```

### Frontend
1. Navigate to the frontend directory
2. Install the dependencies:
```bash
npm install
```
3. Start the development server:
```bash
npm start
```
4. Open http://localhost:3000 to view the application in your browser

## API Endpoints

### Backend
- `GET /transactions`: Fetches transactions for the last 7 days
- `GET /round-up/amount`: Calculates the round-up amount for the last 7 days
- `POST /round-up/transfer`: Transfers the round-up amount to a savings goal
- `GET /accounts`: Fetches account details
- `GET /savings-goals`: Fetches savings goals
- `POST /savings-goals`: Creates a new savings goal

### Frontend
- `/dashboard`: Displays account balances, round-up amount, and recent transactions
- `/round-up`: Displays the round-up amount and provides a button to perform the transfer
- `/transactions`: Displays a list of transactions with filtering and pagination
- `/accounts`: Displays a list of accounts





## Author
Ibrahim
