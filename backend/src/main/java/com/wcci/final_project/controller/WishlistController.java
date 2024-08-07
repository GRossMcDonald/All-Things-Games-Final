package com.wcci.final_project.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcci.final_project.dto.GamePayload;
import com.wcci.final_project.dto.WishlistPayload;
import com.wcci.final_project.entity.Game;
import com.wcci.final_project.entity.Review;
import com.wcci.final_project.entity.User;
import com.wcci.final_project.entity.Wishlist;
import com.wcci.final_project.service.GameService;
import com.wcci.final_project.service.UserService;
import com.wcci.final_project.service.WishlistService;
import com.wcci.final_project.service.ReviewService;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ReviewService reviewService;



    @PostMapping
    public ResponseEntity<Wishlist> addWishlist(@RequestBody WishlistPayload wishlistPayload) {
        Wishlist wishlist = new Wishlist();

        List<Long> wishlistGameIds = wishlistPayload.getGameIds();
        Long wishlistUserId = wishlistPayload.getUserId();

        List<Game> wishlistGames = new ArrayList<>();

        for (Long wishlistGameId : wishlistGameIds) {
            Game wishlistGame = gameService.findGameById(wishlistGameId);
            if (wishlistGame != null)
                wishlistGames.add(wishlistGame);
        }

        wishlist.setGames(wishlistGames);

        User wishlistUser = userService.findUserById(wishlistUserId);

        if (wishlistUser != null) {
            wishlist.setUser(wishlistUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return new ResponseEntity<>(wishlistService.createWishlist(wishlist), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable Long id) {
        Wishlist foundWishlist = wishlistService.findWishlistById(id);

        if (foundWishlist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(foundWishlist);
    }

    @PostMapping("/{id}/add-game")
    public ResponseEntity<Wishlist> addGameToWishlist(@PathVariable Long id, @RequestBody GamePayload gamePayload) throws IOException {
        String newGameItadId = gamePayload.getItadId();
        Wishlist existingWishlist = wishlistService.findWishlistById(id);

        List<Game> gamesInDatabase = gameService.getAllGames();
        List<Game> gamesInExistingWishlist = existingWishlist.getGames();
        List<Game> againGamesInExistingWishlist = new ArrayList<>();

        for (Game gameInExistingWishlist : gamesInExistingWishlist) {
            againGamesInExistingWishlist.add(gameInExistingWishlist);
        }

        boolean isGameAlreadyInWishlist = false;
        boolean isGameAlreadyInDatabase = false;

        for (Game gameInExistingWishlist : againGamesInExistingWishlist) {
            String gameInExistingWishlistItadId = gameInExistingWishlist.getItadId();

            if (gameInExistingWishlistItadId.equals(newGameItadId)) {
                isGameAlreadyInWishlist = true;
                break;
            }
        }

        Game databaseGame = new Game();

        if (!isGameAlreadyInWishlist) {
            for (Game gameInDatabase : gamesInDatabase) {
                String gameInDatabaseItadId = gameInDatabase.getItadId();

                if (gameInDatabaseItadId.equals(newGameItadId)) {
                    isGameAlreadyInDatabase = true;
                    databaseGame = gameInDatabase;
                    break;
                }
            }
        }

        if (isGameAlreadyInDatabase) {
            againGamesInExistingWishlist.add(databaseGame);
        } else {
            Game game = new Game();

            if (newGameItadId != null) game.setItadId(newGameItadId);

            



            List<Long> gameReviewIds = gamePayload.getGameReviewIds();

            if (gameReviewIds != null && !gameReviewIds.isEmpty()) {
                List<Review> gameReviews = new ArrayList<>();

                for (Long gameReviewId : gameReviewIds) {
                    Review gameReview = reviewService.findReviewById(gameReviewId);

                    if (gameReview != null)
                        gameReviews.add(gameReview);
                }

                game.setReviews(gameReviews);
            }

            Game newGame = gameService.saveGame(game);
            againGamesInExistingWishlist.add(newGame);
            existingWishlist.setGames(againGamesInExistingWishlist);
        }

        return ResponseEntity.ok(wishlistService.updateWishlist(existingWishlist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Wishlist> removeGameFromWishlist(@PathVariable Long id, @RequestBody Long gameId) {
        Wishlist existingWishlist = wishlistService.findWishlistById(id);

        if (existingWishlist == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        List<Game> wishlistGames = existingWishlist.getGames();
        List<Game> againGamesInExistingWishlist = new ArrayList<>();

        againGamesInExistingWishlist.addAll(wishlistGames);

        int index = 0;

        for (Game wishlistGame : againGamesInExistingWishlist) {
            Long wishlistGameId = wishlistGame.getId();
            if (wishlistGameId == gameId) {
                Game game = gameService.findGameById(wishlistGameId);

                if (game != null) {
                    againGamesInExistingWishlist.remove(index);
                    break;
                }

            } else {
                index++;
            }
        }

        existingWishlist.setGames(againGamesInExistingWishlist);

        return ResponseEntity.ok(wishlistService.updateWishlist(existingWishlist));
    }
}