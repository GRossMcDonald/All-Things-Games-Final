package com.wcci.final_project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wcci.final_project.dto.UserPayload;
import com.wcci.final_project.entity.Review;
import com.wcci.final_project.entity.User;
import com.wcci.final_project.entity.Wishlist;
import com.wcci.final_project.service.ReviewService;
import com.wcci.final_project.service.UserService;
import com.wcci.final_project.service.WishlistService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserPayload userPayload) {
        User user = new User();

        List<Long> userReviewIds = userPayload.getReviewIds();
        Long userWishlistId = userPayload.getWishlistId();

        if (userWishlistId != null) {
            Wishlist userWishlist = wishlistService.findWishlistById(userWishlistId);

            if (!(userWishlist == null))
                user.setWishlist(userWishlist);
        }

        user.setEmail(userPayload.getEmail());

        if (!(userReviewIds == null)) {
            List<Review> userReviews = new ArrayList<>();
            for (Long reviewId : userReviewIds) {
                Review review = reviewService.findReviewById(reviewId);
                if (!(review == null))
                    userReviews.add(review);
            }
            user.setReviews(userReviews);
        }

        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        Optional<User> optionalFoundUser = userService.findUserByEmail(email);
        if (optionalFoundUser.isPresent()) {
            return ResponseEntity.ok(optionalFoundUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User foundUser = userService.findUserById(id);
        if (foundUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(foundUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();

        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserPayload userPayload) {
        User existingUser = userService.findUserById(id);

        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Long> userReviewIds = userPayload.getReviewIds();
        Long userWishlistId = userPayload.getWishlistId();

        if (userWishlistId != null) {
            Wishlist userWishlist = wishlistService.findWishlistById(userWishlistId);

            if (userWishlist != null)
                existingUser.setWishlist(userWishlist);
        }

        existingUser.setEmail(userPayload.getEmail());

        if (userReviewIds != null) {
            List<Review> userReviews = new ArrayList<>();
            for (Long reviewId : userReviewIds) {
                Review review = reviewService.findReviewById(reviewId);
                if (!(review == null))
                    userReviews.add(review);
            }

            existingUser.setReviews(userReviews);
        }

        return new ResponseEntity<>(userService.updateUser(existingUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);

        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long id) {
        User user = userService.findUserById(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<Review> reviews = user.getReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/{id}/wishlist")
    public ResponseEntity<Wishlist> getWishlistByUserId(@PathVariable Long id) {
        User user = userService.findUserById(id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Wishlist wishlist = user.getWishlist();
        return ResponseEntity.ok(wishlist);
    }
}