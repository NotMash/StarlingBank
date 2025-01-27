import React, { useState, useMemo } from 'react';
import './TransactionsList.css';

// Existing formatting functions remain the same

const TransactionsList = ({ transactions }) => {
    const formatAmountWithCurrency = (amount, currency) => {
        if (isNaN(amount)) return "N/A";
        const formattedAmount = (amount / 100).toFixed(2);
        if (currency === 'GBP') return `£${formattedAmount}`;
        if (currency === 'USD') return `$${formattedAmount}`;
        return `${formattedAmount}`; 
    };

    // Format date and time
    const formatDateTime = (dateString) => {
        if (!dateString) return "N/A";
        try {
            const date = new Date(dateString);
            return date.toLocaleString('en-GB', {
                day: 'numeric',
                month: 'short',
                year: 'numeric',
                hour: '2-digit',
                minute: '2-digit',
                hour12: false,
            });
        } catch (error) {
            console.error('Date parsing error:', error);
            return "Invalid Date";
        }
    };

    const [currentPage, setCurrentPage] = useState(1);
    const [filterDirection, setFilterDirection] = useState('ALL');
    const [searchTerm, setSearchTerm] = useState('');
    const [dateFilter, setDateFilter] = useState('');
    const itemsPerPage = 10;

    // Enhanced filtering logic
    const filteredTransactions = useMemo(() => {
        return transactions.filter(transaction => {
            const matchesDirection = filterDirection === 'ALL' ||
                transaction.direction === filterDirection;

            const matchesSearch =
                transaction.counterPartyName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                formatAmountWithCurrency(transaction.amount, transaction.currency).includes(searchTerm) ||
                formatDateTime(transaction.transactionTime).toLowerCase().includes(searchTerm.toLowerCase());

            const matchesDate = !dateFilter ||
                new Date(transaction.transactionTime).toISOString().split('T')[0] === dateFilter;

            return matchesDirection && matchesSearch && matchesDate;
        });
    }, [transactions, filterDirection, searchTerm, dateFilter]);

    // Paginate transactions
    const paginatedTransactions = useMemo(() => {
        const startIndex = (currentPage - 1) * itemsPerPage;
        return filteredTransactions.slice(startIndex, startIndex + itemsPerPage);
    }, [filteredTransactions, currentPage]);

    // Calculate total pages
    const totalPages = Math.ceil(filteredTransactions.length / itemsPerPage);

    return (
        <div className="transaction-list">
            <div className="transaction-filters">
                <input
                    type="text"
                    placeholder="Search transactions"
                    value={searchTerm}
                    onChange={(e) => {
                        setSearchTerm(e.target.value);
                        setCurrentPage(1);
                    }}
                    className="search-input"
                />
                <input
                    type="date"
                    value={dateFilter}
                    onChange={(e) => {
                        setDateFilter(e.target.value);
                        setCurrentPage(1);
                    }}
                    className="date-filter"
                />
                <select
                    value={filterDirection}
                    onChange={(e) => {
                        setFilterDirection(e.target.value);
                        setCurrentPage(1);
                    }}
                    className="direction-filter"
                >
                    <option value="ALL">All Transactions</option>
                    <option value="IN">Incoming</option>
                    <option value="OUT">Outgoing</option>
                </select>
            </div>

            {filteredTransactions.length > 0 ? (
                <>
                    <table className="transaction-table">
                        <thead>
                            <tr>
                                <th>Transaction</th>
                                <th>Amount</th>
                                <th>Currency</th>
                                <th>Direction</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            {paginatedTransactions.map((transaction, index) => (
                                <tr key={transaction.id || index}>
                                    <td className="text-gray-700">{transaction.counterPartyName ?? "Unknown"}</td>
                                    <td className="text-gray-900 font-medium">
                                        {formatAmountWithCurrency(transaction.amount, transaction.currency)}
                                    </td>
                                    <td className="text-gray-700">
                                        {transaction.currency ?? "N/A"}
                                    </td>
                                    <td>
                                        <span className={`transaction-direction ${transaction.direction === 'IN' ? 'in' : 'out'}`}>
                                            {transaction.direction ?? "N/A"}
                                        </span>
                                    </td>
                                    <td className="text-gray-500">{formatDateTime(transaction.transactionTime)}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>

                    <div className="pagination">
                        <button
                            onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                            disabled={currentPage === 1}
                        >
                            Previous
                        </button>
                        <span>{currentPage} of {totalPages}</span>
                        <button
                            onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                            disabled={currentPage === totalPages}
                        >
                            Next
                        </button>
                    </div>
                </>
            ) : (
                <p className="no-transactions text-gray-500">No transactions found.</p>
            )}
        </div>
    );
};

export default TransactionsList;