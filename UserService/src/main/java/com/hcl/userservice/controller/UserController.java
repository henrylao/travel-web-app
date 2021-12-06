package com.hcl.userservice.controller;

//import com.hcl.userservice.respository.UserRepository;

import com.hcl.userservice.model.Destination;
import com.hcl.userservice.model.Recommendation;
import com.hcl.userservice.model.Review;
import com.hcl.userservice.model.User;
import com.hcl.userservice.respository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    boolean DEBUG = true;
    @Autowired
    UserRepository userRepository;

    public boolean isValidPassword(String password) {
        logger.trace("Home method accessed");
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String userId) {
        logger.trace("GET method all users");
        try {
            List<User> users = new ArrayList<User>();

            if (userId == null) {
                logger.trace("GET method all users");
                userRepository.findAll().forEach(users::add);
            }
//            else
//                userRepository.findByTitleContaining(title).forEach(users::add);

            if (users.isEmpty()) {
                logger.trace("GET method all users -- empty");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            logger.trace("GET method all users");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") long user_id) {
        Optional<User> userData = userRepository.findById(user_id);

        return userData.map(
                user -> new ResponseEntity<>(user, HttpStatus.OK)
        ).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * CREATE method for a User requiring an EMAIL and PASSWORD
     *
     * @return
     */
    @PostMapping("/users/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.trace("POST create a new user");
        try {
            if (userRepository.existsByEmail(user.getEmail())) {
                logger.error("POST create a new user: existing email");
                return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
            }

            if (!isValidPassword(user.getPassword()) && !DEBUG) {
                logger.error("POST create a new user: invalid password");
                return new ResponseEntity<>(null, HttpStatus.PRECONDITION_FAILED);
            }
            User newUser = new User(
                    user.getEmail(),
                    user.getPassword(),
                    user.isAdmin(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName());
            System.out.println(newUser);
            User _user = userRepository
                    .save(
                            new User(
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.isAdmin(),
                                    user.getUsername(),
                                    user.getFirstName(),
                                    user.getLastName()
                            ));

            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("POST create a new user: error unknown");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * UPDATE method for USER entities
     * TODO: add DESTINATION, REVIEW, RECOMMENDATION
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);
        logger.trace("PUT update on existing user");
        if (userData.isPresent()) {
            User _user = userData.get();
            // MINIMUM
            _user.setAdmin(user.isAdmin());
            _user.setEmail(user.getEmail());
            _user.setPassword(user.getPassword());
            _user.setUsername(user.getUsername());

            // STRETCH
            System.out.println(user.getFirstName());
            _user.setFirstName(user.getFirstName());
            _user.setLastName(user.getLastName());
//            System.out.println(_user);
//			_user.setDestination(user.getDestination());
//			_user.setReviews(user.getReviews());
//			_user.setRecommendations(user.getRecommendations());

//			_user.setTitle(user.getTitle());
//			_user.setDescription(user.getDescription());
//			_user.setPublished(user.isPublished());
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            logger.trace("PUT update on existing user: could not find user");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users?={id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        logger.trace("DELETE update on existing user");
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("PUT update on existing user");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        logger.trace("PUT update on existing user");
        try {
            logger.trace("PUT update on existing user");
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("DELETE update on existing user");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Method for ping a particular service at a specific port for testing
     *
     * @param port
     * @param resourceName
     * @return
     */
    private HttpEntity<? extends Object> pingHelper(Integer port, String resourceName) {
        logger.trace("PUT update on existing user");
        try {
            // TODO: NOTE endpoint formatting is subject to change, this is for basic integration testing
            String uri = String.format("http://localhost:%d/api/%s", port, resourceName);
            System.out.println("Pinged: " + uri);
            RestTemplate restTemplate = new RestTemplate();
            Object result = null;
            if (resourceName.equals("review")) {
                logger.trace("PUT update on existing user");

                result = (Review) restTemplate.getForObject(uri, Review.class);
            } else if (resourceName.equals("destination")) {
                logger.trace("PUT update on existing user");

                result = (Destination) restTemplate.getForObject(uri, Destination.class);
            } else if (resourceName.equals("recommendation")) {
                logger.trace("PUT update on existing user");

                result = (Recommendation) restTemplate.getForObject(uri, Recommendation.class);
            }

            if (result == null) {
                logger.trace("PUT update on existing user");


                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            } else {
                logger.trace("PUT update on existing user");

                return new ResponseEntity<>(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.trace("PUT update on existing user");

            System.out.println(e);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // TODO: add REST API call to REVIEW, RECOMMENDATION, DESTINATION for aggregation
    // TODO: migrate the following test pings to a JUNIT test
    // TODO: perform local testing with the `uscities.csv` file
    // TODO: perform local testing using `_beans.xml` when defining classes instead of statically typed classes in model
    // TODO: add tests for USER class
    // TODO: migrate to a MYSQL/POSTGRESQL database from H2

    @GetMapping("/users/reviews/test")

    public ResponseEntity<HttpStatus> pingReviewService() {
        logger.trace("PUT update on existing user");

        // TODO: add REVIEW endpoint
        return (ResponseEntity<HttpStatus>) pingHelper(8081, "review");
    }

    @GetMapping("/users/destinations/test")
    public ResponseEntity<HttpStatus> pingDestinationService() {
        logger.trace("PUT update on existing user");

        // TODO: add DESTINATION endpoint
        return (ResponseEntity<HttpStatus>) pingHelper(8082, "destination");
    }

    @GetMapping("/users/recommendations/test")
    public ResponseEntity<HttpStatus> pingRecommendService() {
        logger.trace("PUT update on existing user");

        // TODO: add RECOMMENDATION endpoint
        return (ResponseEntity<HttpStatus>) pingHelper(8083, "recommendation");
    }

}
