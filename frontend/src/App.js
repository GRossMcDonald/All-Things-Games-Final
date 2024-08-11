import React from "react";
import "./App.css";
import { useState } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import GameInfoPage from "./components/GameInfoPage";
import Homepage from "./components/Homepage";
import Navbar from "./components/Navbar";
import SearchForGames from "./components/SearchForGames";
import AccountRegistration from "./components/AccountRegistration";
import LoginPage from "./components/LoginPage";
import Wishlist from "./components/Wishlist";

function App() {
  return (
    <Router>
      <div>
        <Navbar />
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/GameInfoPage/:itadId" element={<GameInfoPage />} />
          <Route path="/search" element={<SearchForGames />} />
          <Route path="/LoginPage" element={<LoginPage />} />
          <Route
            path="/AccountRegistration"
            element={<AccountRegistration />}
          />
          <Route path="/SearchForGames" element={<SearchForGames />} />
          <Route path="/Wishlist" element={<Wishlist />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
