package com.example.demo.resources;

import com.example.demo.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.*;

@RestController
public class destController {
	Logger logger = LoggerFactory.getLogger(destController.class);
	@Autowired
	private DestRepository destRepo;

	@PostMapping("/add")
	public Destination addDestination(@RequestBody Destination myDest) {
		logger.trace("Add method accessed");
		//ERROR CHECKS:
		/*
		 * 1) Check for distinct lat, lon
		 */
		return destRepo.save(myDest);
	}
	//Read methods
	@GetMapping("/destination/{id}")
	public Destination getDestination(@PathVariable("id") int id)
	throws DestinationNotFoundException{
		logger.trace("Get a destination method accessed");
		//Destination myDest = destRepo.findById(id).get();
		return destRepo.findById(id)
				.orElseThrow(() -> new DestinationNotFoundException(id));
	}
	@GetMapping("/destinations")
	//Returns the landing page or in this case a list of destination objects
	public Iterable<Destination> getDestinations() {
		logger.trace("Get all destinations method accessed");
		return destRepo.findAll();
	}

	@PutMapping("/updatecountry/{id}")
	public void updateDest(@PathVariable("id") int id, @RequestBody String country) 
	throws DestinationNotFoundException{
		//return destRepo.findById(id);
		logger.trace("Update country method accessed");
		Destination myDest = destRepo.findById(id)
				.orElseThrow(() -> new DestinationNotFoundException(id));
		
		myDest.setCountry(country);
		
		destRepo.save(myDest);
		//Optional parameters here
	}
	//Delete Methods
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteDest(@PathVariable("id") int id) 
	throws DestinationNotFoundException{
		logger.trace("Delete by id method accessed");
		Destination myDest = destRepo.findById(id)
				.orElseThrow(() -> new DestinationNotFoundException(id));
		
		destRepo.delete(myDest);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("/deleteAll")
	//Delete the entire MongoDB repo
	public String deleteAll() {
		logger.trace("Delete all country method accessed");
		destRepo.deleteAll();
		return "All destinations deleted";
	}
}
