import { useState, useEffect } from "react";
import { fetchLoggedInUsersWishlistedGames } from "./LoggedInUserData";
import { Link } from "react-router-dom";
import PopularGamesImg from "./PopularGamesImg";
import { checkAndUpdatePrices } from "./CheckPrices";
import RemoveWishlistedGame from "./RemoveWishlistedGame";

function WishlistGamesTemp() {
  const [wishlistGames, setWishlistGames] = useState([]);
  const loggedInEmail = localStorage.getItem("loggedInEmail");
  const [update, setUpdate] = useState(false);

  useEffect(() => {
    const fetchLoggedInUserData = async () => {
      try {
        const loggedInUsersWishlistedGames =
          await fetchLoggedInUsersWishlistedGames();
        setWishlistGames(loggedInUsersWishlistedGames);
      } catch (error) {
        console.log(
          "There was a problem fetching the logged-in user's wishlisted games: " +
            error
        );
      }
    };

    fetchLoggedInUserData();
  }, [update]);

  useEffect(() => {
    if (wishlistGames.length > 0) {
      checkAndUpdatePrices();
    }
  }, [wishlistGames]);

  wishlistGames.forEach((wishlistGame) => {
    const wishlistedGameTitle = wishlistGame.title;
    const wishlistedGameItadId = wishlistGame.itadId;
    console.log(
      "Displaying ",
      { wishlistedGameTitle },
      " with ITAD ID: ~",
      { wishlistedGameItadId },
      "~"
    );
  });

  const handleUpdate = () => {
    console.log("Handle update triggered");
    setUpdate((prev) => !prev);
  };

  return (
    <div>
      <div>
        <h3>{loggedInEmail}'s Wishlisted Games</h3>
      </div>
      <ul>
        {wishlistGames.map((wishlistGame) => (
          <li key={wishlistGame.id}>
            <Link to={`/GameInfoPage/${wishlistGame.itadId}`}>
              <PopularGamesImg game={wishlistGame} />
              <h3>{wishlistGame.title}</h3>
            </Link>
            <RemoveWishlistedGame
              gameToRemove={wishlistGame}
              onGameRemoval={handleUpdate}
            />
            <h4>Best Price</h4>
            <div>
              <span>$</span>
              <span id={`game-price-${wishlistGame.itadId}`}>
                {wishlistGame.priceWhenAdded}
              </span>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default WishlistGamesTemp;
