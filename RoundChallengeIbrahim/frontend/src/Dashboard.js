import React, { useState, useEffect } from 'react';
import { startPeriodicFetch, doRoundUpTransfer } from './services/api.js';
import AccountList from './components/AccountList';
import RoundUpSection from './components/RoundUpSection';
import TransactionsList from './components/TransactionsList';
import './Dashboard.css';

const Dashboard = () => {
   const [roundUpAmount, setRoundUpAmount] = useState(null);
   const [message, setMessage] = useState('');
   const [transactions, setTransactions] = useState([]);
   const [accounts, setAccounts] = useState([]);
   const [savingsGoals, setSavingsGoals] = useState([]);

   useEffect(() => {
       const stopPeriodicFetch = startPeriodicFetch(({
           accounts,
           roundUp,
           transactions,
           savingsGoals
       }) => {
           if (accounts) setAccounts(accounts);
           if (roundUp !== null) setRoundUpAmount(roundUp);
           if (transactions) setTransactions(transactions);
           if (savingsGoals) setSavingsGoals(savingsGoals);
       });

       return () => stopPeriodicFetch();
   }, []);

   const handleRoundUpTransfer = async () => {
       try {
           const selectedGoal = savingsGoals[0];
           const account = accounts[0];

           if (!selectedGoal || !account) {
               setMessage('No savings goal or account available');
               return;
           }

           const response = await doRoundUpTransfer(
               account.accountUid,
               selectedGoal.savingsGoalUid,
               roundUpAmount
           );

           setMessage(response || 'Round-up transfer successful!');
       } catch (error) {
           console.error("Failed to transfer round-up amount:", error);
           setMessage('Transfer failed');
       }
   };

   return (
       <div className="dashboard-container">
           <div className="dashboard">
               <h2 className="dashboard-title">Account Balances</h2>
               <AccountList accounts={accounts} />

               <h2 className="dashboard-title">Round-Up</h2>
               <RoundUpSection
                   roundUpAmount={roundUpAmount}
                   onTransfer={handleRoundUpTransfer}
                   message={message}
               />

               <h2 className="dashboard-title">Recent Transactions</h2>
               <TransactionsList
                   transactions={transactions}
               />
           </div>
       </div>
   );
};

export default Dashboard;