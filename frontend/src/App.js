import React, { useState } from "react";
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
import { fetchLoggedInUsersWishlist } from "./components/LoggedInUserData";

function App() {
  const [loggedInUsersWishlist, setLoggedInUsersWishlist] = useState([]);

  async function fetchWishlist(params) {
    setLoggedInUsersWishlist(await fetchLoggedInUsersWishlist());
  }

  useEffect(() => {
    setInterval(() => {
      fetchWishlist();

      console.log("User's wishlist size: ", loggedInUsersWishlist.length);

      // eslint-disable-next-line eqeqeq
      if (loggedInUsersWishlist.length > 0) {
        checkAndUpdatePrices();
      }
    }, 3000);
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
