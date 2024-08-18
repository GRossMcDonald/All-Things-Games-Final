import React from "react";
import PopularGames from "./PopularGames";
import "./Homepage.css";

const Homepage = () => {
  return (
    <div className="home-page-container">
      <h1>All Things Games</h1>
      <PopularGames />
    </div>
  );
};

export default Homepage;
