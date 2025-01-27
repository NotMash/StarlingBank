import React, { useState, useEffect } from 'react';
import './RoundUp.css';
import {
    fetchSavingsGoals,
    fetchAccounts,
    withdrawFromSavings,
    createSavingsGoal,
    doRoundUpTransfer,
} from '../services/api';


const CustomModal = ({
    isOpen,
    onClose,
    onConfirm,
    title,
    message,
    confirmText = 'Confirm',
    cancelText = 'Cancel',
    inputLabel,
    inputType = 'text', // Default input type
}) => {
    const [inputValue, setInputValue] = useState('');

    if (!isOpen) return null;

    return (
        <div className="modal-overlay">
            <div className="modal">
                <h2 className="modal-title">{title}</h2>
                <p className="modal-text">{message}</p>
                {inputLabel && (
                    <div className="modal-input-container">
                        <label>{inputLabel}</label>
                        <input
                            type={inputType}
                            value={inputValue}
                            onChange={(e) => setInputValue(e.target.value)}
                            className="modal-input"
                            step={inputType === 'number' ? '0.01' : undefined}
                            min={inputType === 'number' ? '0' : undefined}
                        />
                    </div>
                )}
                <div className="modal-actions">
                    <button className="modal-cancel" onClick={onClose}>
                        {cancelText}
                    </button>
                    <button
                        className="modal-confirm"
                        onClick={() => onConfirm(inputValue)}
                    >
                        {confirmText}
                    </button>
                </div>
            </div>
        </div>
    );
};

const ProgressBar = ({ saved, target }) => {
    const percentage = Math.min(
        Math.round((saved / target) * 100),
        100
    );

    return (
        <div className="progress-bar-container">
            <div
                className="progress-bar"
                style={{
                    width: `${percentage}%`,
                    backgroundColor: 'rgb(78, 70, 220)',
                }}
            />
        </div>
    );
};

const RoundUpSection = ({ roundUpAmount, message }) => {
    const [modalConfig, setModalConfig] = useState({});
    const [savingsGoals, setSavingsGoals] = useState([]);
    const [accountUid, setAccountUid] = useState('');
    const [loadingGoals, setLoadingGoals] = useState(true);
    const [selectedGoalUid, setSelectedGoalUid] = useState('');

    useEffect(() => {
        const loadData = async () => {
            try {
                const accounts = await fetchAccounts();
                if (accounts && accounts.length > 0) {
                    setAccountUid(accounts[0].accountUid);
                }

                const goals = await fetchSavingsGoals();
                if (goals && goals.length > 0) {
                    setSavingsGoals(goals);
                    setSelectedGoalUid(goals[0].savingsGoalUid);
                } else {
                    setModalConfig({
                        isOpen: true,
                        title: 'No Savings Goals',
                        message: 'No savings goals found. Would you like to create one?',
                        onConfirm: handleCreateSavingsGoal,
                        confirmText: 'Create Goal',
                    });
                }
            } catch (error) {
                setModalConfig({
                    isOpen: true,
                    title: 'Error',
                    message: error.message,
                });
            } finally {
                setLoadingGoals(false);
            }
        };

        loadData();
    }, []);

    const handleCreateSavingsGoal = async (name, targetAmount) => {
        try {
            name = name || await new Promise(resolve => {
                setModalConfig({
                    isOpen: true,
                    title: 'New Savings Goal',
                    message: 'Enter the name of your savings goal',
                    inputLabel: 'Goal Name: ',
                    inputType: 'text',
                    onConfirm: resolve,
                });
            });

            if (!name) return;

            targetAmount = targetAmount || await new Promise(resolve => {
                setModalConfig({
                    isOpen: true,
                    title: 'Savings Goal Target',
                    message: 'Enter the target amount (in £)',
                    inputLabel: 'Target Amount: ',
                    inputType: 'number',
                    onConfirm: resolve,
                });
            });

            if (!targetAmount || isNaN(targetAmount) || Number(targetAmount) <= 0) {
                alert('Invalid amount. Please enter a positive number.');
                return;
            }

            await createSavingsGoal(name, targetAmount);
            const updatedGoals = await fetchSavingsGoals();
            setSavingsGoals(updatedGoals);
            setSelectedGoalUid(updatedGoals[0].savingsGoalUid);
            setModalConfig({ isOpen: false });
        } catch (error) {
            setModalConfig({
                isOpen: true,
                title: 'Error',
                message: `Failed to create savings goal: ${error.message}`,
            });
        }
    };

    const handleWithdraw = async (goalUid, amount) => {
        try {
            const goal = savingsGoals.find(g => g.savingsGoalUid === goalUid);
            const totalSaved = goal.totalSaved?.minorUnits ? goal.totalSaved.minorUnits / 100 : 0;

            if (!amount || isNaN(amount) || Number(amount) <= 0) {
                alert('Invalid withdrawal amount.');
                return;
            }

            if (Number(amount) > totalSaved) {
                alert(`Insufficient funds. Max withdrawal: £${totalSaved.toFixed(2)}`);
                return;
            }

            await withdrawFromSavings(goalUid, amount);
            const updatedGoals = await fetchSavingsGoals();
            setSavingsGoals(updatedGoals);
            setModalConfig({ isOpen: false });
        } catch (error) {
            setModalConfig({
                isOpen: true,
                title: 'Withdrawal Error',
                message: error.message,
            });
        }
    };

    const handleTransferClick = async () => {
        if (!selectedGoalUid) {
            alert('Please select a savings goal.');
            return;
        }

        try {
            await doRoundUpTransfer(accountUid, selectedGoalUid, roundUpAmount);
            const updatedGoals = await fetchSavingsGoals();
            setSavingsGoals(updatedGoals);
            alert(`£${roundUpAmount.toFixed(2)} transferred successfully!`);
        } catch (error) {
            alert(`Transfer failed: ${error.message}`);
        }
    };

    return (
        <div className="round-up-section">
            <CustomModal
                isOpen={modalConfig.isOpen}
                onClose={() => setModalConfig({ isOpen: false })}
                onConfirm={modalConfig.onConfirm || (() => setModalConfig({ isOpen: false }))}
                title={modalConfig.title}
                message={modalConfig.message}
                confirmText={modalConfig.confirmText}
                inputLabel={modalConfig.inputLabel}
                inputType={modalConfig.inputType} // Pass inputType here
            />



            <p>Round-Up Amount: {roundUpAmount !== null ? `£${roundUpAmount}` : 'Loading...'}</p>

            {savingsGoals.length > 0 && (
                <div className="savings-goal-selector">
                    <label htmlFor="goal-select">Select Savings Goal:</label>
                    <select
                        id="goal-select"
                        value={selectedGoalUid}
                        onChange={(e) => setSelectedGoalUid(e.target.value)}
                    >
                        {savingsGoals.map((goal) => (
                            <option key={goal.savingsGoalUid} value={goal.savingsGoalUid}>
                                {goal.name}
                            </option>
                        ))}
                    </select>
                </div>
            )}

            <button
                className="round-up-button"
                onClick={handleTransferClick}
                disabled={loadingGoals || savingsGoals.length === 0}
            >
                Perform Round-Up Transfer
            </button>

            <div className="savings-goals-section">
                <h3 className="savings-goals-title">Savings Goals</h3>
                {savingsGoals.map(goal => {
                    const saved = goal.totalSaved?.minorUnits / 100 || 0;
                    const target = goal.target?.minorUnits / 100 || 0;

                    return (
                        <div key={goal.savingsGoalUid} className="savings-goal-item">
                            <div className="goal-header">
                                <h4>{goal.name}</h4>
                                <p className="goal-amount">
                                    £{saved.toFixed(2)} / £{target.toFixed(2)}
                                </p>
                            </div>
                            <ProgressBar saved={saved} target={target} />
                            <button
                                className="withdraw-button"
                                onClick={() => {
                                    setModalConfig({
                                        isOpen: true,
                                        title: `Withdraw from ${goal.name}`,
                                        message: `Available: £${saved.toFixed(2)}`,
                                        inputLabel: 'Withdrawal Amount: ',
                                        inputType: 'number',
                                        onConfirm: (amount) => handleWithdraw(goal.savingsGoalUid, amount),
                                    });
                                }}
                            >
                                Withdraw
                            </button>
                        </div>
                    );
                })}

                <button
                    className="create-goal-button"
                    onClick={() => handleCreateSavingsGoal()}
                >
                    Create New Savings Goal
                </button>
            </div>
        </div>
    );
};

export default RoundUpSection;
