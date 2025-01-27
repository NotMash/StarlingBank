const BASE_URL = "http://localhost:8080"; // Backend URL

export const fetchTransactions = async () => {
    try {
        const response = await fetch(`${BASE_URL}/transactions`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error("Failed to fetch transactions");
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        return null;
    }
};

export const fetchAccounts = async () => {
    try {
        const response = await fetch(`${BASE_URL}/api/v1/accounts`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error("Failed to fetch accounts");
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        return null;
    }
};

/**
 * Fetch the total round-up amount for the last 7 days.
 */
export const getRoundUpAmount = async () => {
    try {
        const response = await fetch(`${BASE_URL}/round-up/amount`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error("Failed to fetch round-up amount");
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        return null;
    }
};

export const doRoundUpTransfer = async (accountUid, savingsGoalUid, amount) => {
    try {
        const response = await fetch(`${BASE_URL}/round-up/transfer?savingsGoalUid=${encodeURIComponent(savingsGoalUid)}&amount=${encodeURIComponent(amount)}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                accountUid,
                defaultCategory: '',
                currency: '',
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to perform round-up transfer');
        }

        return await response.text();
    } catch (error) {
        console.error(error);
        return null;
    }
};

/**
 * Fetch all savings goals with their details.
 */
export const fetchSavingsGoals = async () => {
    try {
        const response = await fetch(`${BASE_URL}/round-up/savings-goals`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || "Failed to fetch savings goals");
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        return null;
    }
};

/**
 * Withdraw an amount from a specific savings goal.
 * @param {string} savingsGoalUid - The UID of the savings goal.
 * @param {number} amount - The amount to withdraw.
 */
export const withdrawFromSavings = async (savingsGoalUid, amount) => {
    try {
        const response = await fetch(
            `${BASE_URL}/round-up/withdraw?savingsGoalUid=${encodeURIComponent(savingsGoalUid)}&amount=${encodeURIComponent(amount)}`,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            }
        );

        if (!response.ok) {
            throw new Error("Failed to withdraw from savings goal");
        }

        return await response.text();
    } catch (error) {
        console.error(error);
        return null;
    }
};

/**
 * Create a new savings goal.
 * @param {string} name - The name of the savings goal.
 * @param {number} targetAmount - The target amount for the savings goal.
 */
export const createSavingsGoal = async (name, targetAmount) => {
    try {
        const response = await fetch(`${BASE_URL}/round-up/savings-goal?name=${encodeURIComponent(name)}&targetAmount=${encodeURIComponent(targetAmount)}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error("Failed to create savings goal");
        }

        return await response.text();
    } catch (error) {
        console.error(error);
        return null;
    }
};

/**
 * Fetch the user identity details.
 */
export const fetchUserIdentity = async () => {
    try {
        const response = await fetch(`${BASE_URL}/api/v1/accounts/identity/individual`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error("Failed to fetch user identity");
        }

        return await response.json();
    } catch (error) {
        console.error(error);
        return null;
    }
};


export const startPeriodicFetch = (callback, interval = 5000) => {
    const fetchData = async () => {
        try {
            const [
                accounts,
                roundUp,
                transactions,
                savingsGoals
            ] = await Promise.all([
                fetchAccounts(),
                getRoundUpAmount(),
                fetchTransactions(),
                fetchSavingsGoals()
            ]);

            callback({
                accounts,
                roundUp,
                transactions,
                savingsGoals
            });
        } catch (error) {
            console.error("Periodic fetch error:", error);
        }
    };

    fetchData();
    const intervalId = setInterval(fetchData, interval);
    return () => clearInterval(intervalId);
};
