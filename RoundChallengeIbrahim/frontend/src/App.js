import React from "react";
import Dashboard from "./Dashboard";
import logo from "./images/img.png";
import IdentitySection from "./components/Identity";
import './App.css';

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <div className="logo-container">
                    <img
                        src={logo}
                        alt="logo"
                        style={{
                            width: "200px",
                            height: "auto",
                            objectFit: "contain",
                        }}
                        className="App-logo"
                    />
                </div>
                <IdentitySection />
            </header>

            <main>
                <Dashboard />
            </main>
        </div>
    );
}

export default App;
