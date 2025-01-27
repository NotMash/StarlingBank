import React from 'react';
import './AccountList.css';

const formatCurrency = (minorUnits) => `£${(minorUnits / 100).toFixed(2)}`;

const AccountList = ({ accounts }) => {
    return (
        <div className="account-list">
            {accounts.map((account) => (
                <div key={account.accountUid} className="account-item">
                    <div className="account-details">
                        <p className="account-name">{account.name}</p>
                        <p className="account-type">{account.accountType}</p>
                    </div>
                    <div className="account-balance">
                        <p className="account-balance-total text-starling-blue">
                            {formatCurrency(account.balance.effectiveBalance.minorUnits)}
                        </p>
                        <div className="account-balance-details">
                            <span className="account-balance-cleared text-gray-500">
                                Cleared: {formatCurrency(account.balance.clearedBalance.minorUnits)}
                            </span>
                            <span className="account-balance-pending text-gray-500">
                                Pending: {formatCurrency(account.balance.pendingTransactions.minorUnits)}
                            </span>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default AccountList;