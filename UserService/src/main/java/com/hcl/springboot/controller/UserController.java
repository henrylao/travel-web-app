package com.hcl.springboot.controller;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.hcl.springboot.controller.http.UserRoute;
import com.hcl.springboot.model.Destination;
import com.hcl.springboot.model.Recommendation;
import com.hcl.springboot.model.Review;
import com.hcl.springboot.model.User;
import com.hcl.springboot.respository.UserRepository;
import com.hcl.springboot.rest.Utility;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: segment the class into handlers for the various HTTP Methods to reduce code footprint
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1")
public class UserController
        extends UserRoute {
    /********************************************************
     * Attributes
     ********************************************************/
    @Autowired
    UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserController.class);
    boolean DEBUG = true;

    /********************************************************
     * REST API GET Methods
     ********************************************************/

    /**
     * GET Method for reading all users in the database reflecting only a collection
     * of fields existent within the User class. Fields persisted by exterior services
     * are not polled to preserve read speed
     *
     * @param userId
     * @return
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String userId) {
        logger.trace("GET method all users");
        try {
            List<User> users = new ArrayList<User>();

            if (userId == null) {
                logger.trace("GET method all users");
                userRepository.findAll().forEach(users::add);
            }

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

    /**
     * GET Method for accessing all the Reviews mapped to the specified User
     * by `user_id`
     *
     * @param user_id
     * @return
     * @throws JSONException
     */
    @GetMapping("/users/{user_id}/reviews")
    public ResponseEntity<List<Review>> getUserAllReviews(@PathVariable("user_id") Integer user_id) throws JSONException {
        logger.trace("=========== START [GET] User's Reviews ============= ");
        Map<String, Object> rv = new HashMap<>();
        Optional<User> userData = userRepository.findById(user_id);

        // TODO: add GET to Review REST API endpoint to query by author == user_id
        String reviewsJsonStr = Utility.getJSONAtEndpoint(
                "http://localhost:%d//%s//%s",
                8085,
                "reviews",
                "all"
        );
        logger.trace("JSON Reviews:" + reviewsJsonStr);

        // convert from JSON string to Java types
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        List<Map<String, String>> reviews = gson.fromJson(reviewsJsonStr, type);
        logger.trace("GSON: " + reviews.toString());
        logger.trace(String.valueOf(userData.get().getUserId()));

        // TODO: reviews needs an endpoint for searching by Author
        // current implementation simply aggregates all reviews and returns
        List<Review> reviewList = new ArrayList<>();
        Review jsonParsed = null;
        String author, uid;
        uid = String.valueOf(user_id);
        for (Map<String, String> r : reviews) {
            if (r.isEmpty()) {
                continue;
            }
            author = r.get("author");

            // check if review's author matches the user's id
            if (author.equals(uid)) {
                logger.trace("Found matching author: " + author);
                jsonParsed = new Review();
                jsonParsed.setAuthor(author);
                jsonParsed.setId(Integer.parseInt(r.get("id")));
                jsonParsed.setDestId(Integer.parseInt(r.get("destId")));
                jsonParsed.setSubject(r.get("subject"));
                jsonParsed.setContent(r.get("content"));
                reviewList.add(jsonParsed);
            }
        }

        logger.trace("Returning " + reviewList.toString());
        logger.trace("=========== END [GET] User's Reviews ============= ");

        if (reviewList.size() > 0) {
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(reviewList, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET Method for accessing the list of Destinations based upon Recommendations
     * associated with the User
     *
     * @param user_id
     * @return
     * @throws JSONException
     */
    @GetMapping("/users/{user_id}/destinations")
    public ResponseEntity<List<Destination>> getUserDestinationsByRecommendation(@PathVariable("user_id") Integer user_id
//            , @PathVariable("dest_id" ) Integer dest_id
    ) throws JSONException {
        logger.trace("=========== START [GET] User's Destinations by Recommendations ============= ");
        ResponseEntity<List<Recommendation>> response = this.getUserAllRecommendations(user_id);
        List<Recommendation> recommendations = response.getBody();
        // sort destinations by rating
        List<Destination> destinations = new ArrayList<>();
        Destination d;
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();


        for (Recommendation r : recommendations) {
//            r.getDestId()
            String jsonObjectStr = Utility.getJSONAtEndpoint(
                    "http://localhost:%d//%s//%s",
                    8100,
                    "destinations",
                    String.format("%d", r.getDestId())
            );
            Map<String, String> destinationMap = gson.fromJson(jsonObjectStr, type);
            logger.trace("GSON: " + destinationMap.toString());
            d = new Destination();
            logger.trace(destinationMap.get("id"));
            // TODO: breaking -- unsure why
//            d.setDestId(Integer.parseInt(destinationMap.get("id")));
            d.setPlace(destinationMap.get("place"));
            d.setCountry(destinationMap.get("country"));
            d.setLatitude(Float.parseFloat(destinationMap.get("latitude")));
            d.setLongitude(Float.parseFloat(destinationMap.get("longitude")));
            d.setInfo(destinationMap.get("info"));
            d.setImage(destinationMap.get("image")); // TODO: this can either be an image hosted somewhere on the cloud

            logger.trace(jsonObjectStr);
            destinations.add(d);
        }

        // TODO: sort based on rating
//        destinations.sort();
//        Collections.sort(destinations, new Comparator<Destination>() {
//            @Override
//            public int compare(Destination o1, Destination o2) {
//                o1.get
//            }
//        })

        logger.trace("=========== END [GET] User's Destinations by Recommendations ============= ");
        if (destinations.size() < 1) {
            return new ResponseEntity<>(destinations, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(destinations, HttpStatus.OK);
        }
    }

    /**
     * GET Method for accessing the list of Recommendations for the User
     *
     * @param user_id
     * @return
     * @throws JSONException
     */
    @GetMapping("/users/{user_id}/recommendations")
    public ResponseEntity<List<Recommendation>> getUserAllRecommendations(@PathVariable("user_id") Integer user_id) throws JSONException {
        logger.trace("=========== START [GET] User's Recommendations ============= ");
        Map<String, Object> rv = new HashMap<>();
        Optional<User> userData = userRepository.findById(user_id);
        String resourceStr = "recommendations";
        // TODO: add GET to Review REST API endpoint to query by author == user_id
        String reviewsJsonStr = Utility.getJSONAtEndpoint(
                "http://localhost:%d//%s//%s",
                8095,
                "recommendations",
                ""
        );
        logger.trace(String.format("JSON %s:", resourceStr) + reviewsJsonStr);

        // convert from JSON string to Java types
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, String>>>() {
        }.getType();
        List<Map<String, String>> reviews = gson.fromJson(reviewsJsonStr, type);
        logger.trace("GSON: " + reviews.toString());
        logger.trace(String.valueOf(userData.get().getUserId()));

        // TODO: reviews needs an endpoint for searching by Author
        // current implementation simply aggregates all reviews and returns
        List<Recommendation> recList = new ArrayList<>();
        Recommendation jsonParsed = null;
        String author, uid;
        uid = String.valueOf(user_id);
        for (Map<String, String> r : reviews) {
            if (r.isEmpty()) {
                continue;
            }
            author = r.get("author");

            // TODO: assumes `author` is the target User
            // check if recommendation is intended for the particular User
            if (author.equals(uid)) {
                logger.trace("Found matching recommendation by uid: " + author);
                jsonParsed = new Recommendation();
                // TODO: this is a bit misleading a recommendation should be described in a
                //  manner that suggests targeting of a particular object -- a recommendaiton
                //  in this particular application context is likely to suggest a system that
                //  generates an output signal (recommendation) based upon the population
                //  User Reviews, can be extended into a User's set of friends if there is a friend system, etc
                jsonParsed.setAuthor(author);
                jsonParsed.setRecommendationId(Long.valueOf(r.get("recommendationId")));
                jsonParsed.setDestId((long) Integer.parseInt(r.get("destId")));
                jsonParsed.setRate(Integer.parseInt(r.get("rate")));
                jsonParsed.setContent(r.get("content"));
                recList.add(jsonParsed);
            }
        }

        logger.trace("Returning " + recList.toString());
        logger.trace("=========== END [GET] User's Recommendations ============= ");

        if (recList.size() > 0) {
            return new ResponseEntity<>(recList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(recList, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * GET Method for reading a specific User from the target database
     *
     * @param user_id
     * @return
     * @throws JSONException
     */
    @GetMapping("/users/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") int user_id) throws JSONException {
        Optional<User> userData = userRepository.findById(user_id);
        return userData.map(
                user -> new ResponseEntity<>(user, HttpStatus.OK)
        ).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }
    /********************************************************
     * REST API POST Methods
     ********************************************************/
    /**
     * POST method for CREATING a User requiring an EMAIL and PASSWORD
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

    /********************************************************
     * REST API POST Methods
     ********************************************************/
    /**
     * UPDATE method for USER entities
     * TODO: add DESTINATION, REVIEW, RECOMMENDATION
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
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

    /********************************************************
     * REST API DELETE Methods
     ********************************************************/
    @DeleteMapping("/users?={id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
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

    /********************************************************
     * Utility Methods
     ********************************************************/

    public boolean isValidPassword(String password) {
        logger.trace("Home method accessed");
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
