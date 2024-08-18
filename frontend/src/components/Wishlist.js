import React, { useState, useEffect, useCallback } from "react";
import { Link } from "react-router-dom";
import "./Wishlist.css";
import PopularGamesImg from "./PopularGamesImg";
import RemoveWishlistedGame from "./RemoveWishlistedGame";
import AddToWishlistButton from "./AddToWishlistButton";

const Wishlist = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [games, setGames] = useState([]);
  const [update, setUpdate] = useState(false);
  const [loggedInEmail, setLoggedInEmail] = useState("");

  useEffect(() => {
    const email = localStorage.getItem("loggedInEmail");
    if (email) {
      setLoggedInEmail(email);
    }
  }, []);

  const fetchWishlistGames = useCallback(() => {
    if (!loggedInEmail) return;

    fetch(`http://localhost:8080/user?email=${encodeURIComponent(loggedInEmail)}`)
      .then((response1) => {
        if (!response1.ok) {
          throw new Error("Network response was not ok.");
        }
        return response1.json();
      })
      .then((data1) => {
        const wishlistId = data1.wishlist.id;
        const url = `http://localhost:8080/wishlist/${wishlistId}/games`;
        return fetch(url, {
          headers: {
            Accept: "application/json",
          },
        });
      })
      .then((response2) => {
        if (!response2.ok) {
          throw new Error("Network response was not ok.");
        }
        return response2.json();
      })
      .then((data2) => {
        setGames(data2);
      })
      .catch((error) => {
        console.log("There was a problem fetching the wishlist: " + error);
      });
  }, [loggedInEmail]);

  useEffect(() => {
    console.log("Fetching games due to update state change");
    fetchWishlistGames();
  }, [fetchWishlistGames, update]);

  const toggleMenu = () => {
    setIsOpen((prevIsOpen) => !prevIsOpen);
  };

  const handleUpdate = () => {
    console.log("Handle update triggered");
    setUpdate((prev) => !prev);
  };

  return (
    <div className="wishlist-games">
      <div className={`wishlist-container ${isOpen ? "is-open" : ""}`}>
        <ul className="wishlist">
          <p className="wishlist-title">Wishlist</p>
          {games.map((game) => (
            <li key={game.itadId}>
              <Link to={`/GameInfoPage/${game.itadId}`}>
                <PopularGamesImg game={game} />
                <h4>{game.title}</h4>
              </Link>
              <RemoveWishlistedGame gameToRemove={game} onGameRemoval={handleUpdate} />
              <AddToWishlistButton gameToAdd={game} onGameAddition={handleUpdate} />
            </li>
          ))}
        </ul>
      </div>
      <div
        className={`hamburger no-user-select ${isOpen ? "active" : ""}`}
        onClick={toggleMenu}
      >
        <span className="bar"></span>
        <span className="bar"></span>
      </div>
    </div>
  );
};

export default Wishlist;