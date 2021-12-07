package com.hcl.destinationservice.resources;

import com.hcl.destinationservice.exceptions.DestinationNotFoundException;
import com.hcl.destinationservice.models.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/destinations")
public class DestinationController {
	Logger logger = LoggerFactory.getLogger(DestinationController.class);
	@Autowired
	private DestinationRepository destRepo;

//	@PostMapping("/add")
//	public Destination addDestination(@RequestBody Destination myDest) {
//		logger.trace("Add method accessed");
//		//ERROR CHECKS:
//		/*
//		 * 1) Check for distinct lat, lon
//		 */
//		return destRepo.save(myDest);
//	}
	@RequestMapping("/")
	public String Index() {
		logger.trace("Home method accessed");
	return "Destinatnons Page";
}
	//Read methods
	@GetMapping("/{id}")
	public Destination getDestinationById(@PathVariable("id") int id)
	throws DestinationNotFoundException{
		logger.trace("Get a destination method accessed");
		//Destination myDest = destRepo.findById(id).get();
		return destRepo.findById(id)
				.orElseThrow(() -> new DestinationNotFoundException(id));
	}
	@GetMapping("/all")
	//Returns the landing page or in this case a list of destination objects
	public Iterable<Destination> getDestinations() {
		logger.trace("Get all destinations method accessed");
		return destRepo.findAll();
	}

	@PutMapping("/{id}/country/update")
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
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteDest(@PathVariable("id") int id) 
	throws DestinationNotFoundException{
		logger.trace("Delete by id method accessed");
		Destination myDest = destRepo.findById(id)
				.orElseThrow(() -> new DestinationNotFoundException(id));
		
		destRepo.delete(myDest);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("/all")
	//Delete the entire MongoDB repo
	public String deleteAll() {
		logger.trace("Delete all country method accessed");
		destRepo.deleteAll();
		return "All destinations deleted";
	}
}
