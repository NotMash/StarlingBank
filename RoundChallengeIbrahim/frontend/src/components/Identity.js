import React, { useState, useEffect } from 'react';
import './Identity.css';
import { fetchUserIdentity } from '../services/api';
import secureIcon from '../images/secure1.png';

const IdentitySection = () => {
    const [userIdentity, setUserIdentity] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        const loadIdentity = async () => {
            try {
                const identity = await fetchUserIdentity();
                if (identity) {
                    setUserIdentity(identity);
                } else {
                    throw new Error('Failed to fetch user identity.');
                }
            } catch (error) {
                setErrorMessage(error.message);
            }
        };

        loadIdentity();
    }, []);

    const toggleModal = () => {
        setIsModalOpen(!isModalOpen);
    };

    return (
        <div className="identity-container">
            <div className="identity-toggle" onClick={toggleModal}>
                <img
                    src={secureIcon}
                    alt="Secure Icon"
                    style={{
                        width: "150px",
                        height: "150px",
                        objectFit: "contain",
                    }}
                />
            </div>

            {isModalOpen && (
                <div className="modal-overlay" onClick={toggleModal}>
                    <div
                        className="modal"
                        onClick={(e) => e.stopPropagation()} // Prevent click on modal content from closing it
                    >
                        <h2 className="modal-title">User Identity</h2>
                        <div className="modal-content">
                            {errorMessage ? (
                                <p className="error-message">Error: {errorMessage}</p>
                            ) : userIdentity ? (
                                <div className="identity-details">
                                    <p><strong>Title:</strong> {userIdentity.title}</p>
                                    <p><strong>First Name:</strong> {userIdentity.firstName}</p>
                                    <p><strong>Last Name:</strong> {userIdentity.lastName}</p>
                                    <p><strong>Date of Birth:</strong> {userIdentity.dateOfBirth}</p>
                                    <p><strong>Email:</strong> {userIdentity.email}</p>
                                    <p><strong>Phone:</strong> {userIdentity.phone}</p>
                                </div>
                            ) : (
                                <p>Loading user identity...</p>
                            )}
                        </div>
                        <div className="modal-actions">
                            <button className="modal-close" onClick={toggleModal}>
                                Close
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default IdentitySection;
