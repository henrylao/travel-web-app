package com.hcl.recommendservice.controller;

import com.hcl.recommendservice.exception.RecommendationNotFoundException;
import com.hcl.recommendservice.model.Recommendation;
import com.hcl.recommendservice.repository.RecommendationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
public class RecommendationController {
    Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @Autowired
    RecommendationRepository recommendationRepository;

    @RequestMapping("/")
    public String Index() {
        logger.trace("Home method accessed");
        return "Recommendation Page";
    }

    //Get all recommendations
    @GetMapping("/recommendations")
    public List<Recommendation> getAllRecommendations() {
        logger.trace("get-all method accessed");
        try {
            return recommendationRepository.findAll();
        } catch (Exception e) {
            logger.trace("Failed to get list of recommendations, see exception log");
            throw e;
        }
    }

    //Get single recommendation by Id
    //troubleshoot better logging for this request, or just switch to Evan's implementation
    @GetMapping("/recommendations/{id}")
    public Recommendation getRecommendationById(@PathVariable(value = "id") Long recommendationId)
            throws RecommendationNotFoundException {
        logger.trace("get-by-id method accessed");
        return recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));
    }

    //Create a recommendation
    @PostMapping("/recommendations")
    public Recommendation createRecommendation(@Valid @RequestBody Recommendation recommendation) {
        logger.trace("Add method accessed");
        return recommendationRepository.save(recommendation);
    }

    //Update recommendation
    @PutMapping("/recommendations/{id}")
    public Recommendation updateRecommendationContent(@PathVariable(value = "id") Long recommendationId,
                                                      @Valid @RequestBody Recommendation recDetails) throws RecommendationNotFoundException {
        logger.trace("update-by-id method accessed");
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));

        recommendation.setDestId(recDetails.getDestId());
        recommendation.setContent(recDetails.getContent());
        recommendation.setRate(recDetails.getRate());

        Recommendation updatedRecommendation = recommendationRepository.save(recommendation);
        logger.trace("Successfully updated recommendation #: " + recommendationId);

        return updatedRecommendation;

    }

    //Delete a recommendation
    @DeleteMapping("/recommendations/{id}")
    public ResponseEntity<?> deleteRecommendation(@PathVariable(value = "id") Long recommendationId) throws RecommendationNotFoundException {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));
        recommendationRepository.delete(recommendation);

        return ResponseEntity.ok().build();
    }


}
