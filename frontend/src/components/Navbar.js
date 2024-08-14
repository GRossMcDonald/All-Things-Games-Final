import React, { useEffect, useState } from "react";
import SearchForGames from "./SearchForGames";
import { Link } from "react-router-dom";
import Wishlist from "./Wishlist";
import About from "./About";
import "./Navbar.css";

function NavBar() {
  const [loggedInEmail, setLoggedInEmail] = useState("");
  useEffect(() => {
    const email = localStorage.getItem("loggedInEmail");
    setLoggedInEmail(email);
    console.log(email);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("loggedInEmail");
    setLoggedInEmail("");
  };

  return (
    <div className="navbar-wrapper">
      <nav className="navbar">
        <div className="homelink-container">
          <Link to="/">
            <p>All Things Games</p>
          </Link>
        </div>
        <div className="search-container">
          <SearchForGames />
        </div>
        <div id="user-greeting">
          <Link to="/WishlistGamesTemp">
            {loggedInEmail && <p>Welcome {loggedInEmail}!</p>}
          </Link>
        </div>

        <div>
          {loggedInEmail ? (
            <Link to="/" onClick={handleLogout}>
              Logout
            </Link>
          ) : (
            <Link to="/LoginPage">Login</Link>
          )}
        </div>
        <div>
          <Wishlist />
        </div>
        <div>
          <Link to="/About">About us</Link>
        </div>
      </nav>
    </div>
  );
}

export default NavBar;
