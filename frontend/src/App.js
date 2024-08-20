import React from "react";
import "./App.css";
import { useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import GameInfoPage from "./components/GameInfoPage";
import Homepage from "./components/Homepage";
import Navbar from "./components/Navbar";
import SearchPage from "./components/SearchPage";
import AccountRegistration from "./components/AccountRegistration";
import LoginPage from "./components/LoginPage";
import WishlistGamesTemp from "./components/WishlistGamesTemp";
import About from "./components/About";
import { checkAndUpdatePrices } from "./components/CheckPrices";

function App() {
  useEffect(() => {
    const interval = setInterval(() => {
      const loggedInEmail = localStorage.getItem("loggedInEmail");

      console.log(loggedInEmail);

      // eslint-disable-next-line eqeqeq
      if (loggedInEmail !== null) {
        checkAndUpdatePrices();
      }
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <Router>
      <div>
        <Navbar />
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/GameInfoPage/:itadId" element={<GameInfoPage />} />
          <Route path="/SearchPage" element={<SearchPage />} />
          <Route path="/LoginPage" element={<LoginPage />} />
          <Route path="/WishlistGamesTemp" element={<WishlistGamesTemp />} />
          <Route
            path="/AccountRegistration"
            element={<AccountRegistration />}
          />
          <Route path="/About" element={<About />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
