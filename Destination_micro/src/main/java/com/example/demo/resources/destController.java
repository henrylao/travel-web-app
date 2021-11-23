package com.example.demo.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.*;

//ESTABLISH A MONGODB DATABASE
@RestController
public class destController {
	@Autowired
	private DestRepository destRepo;
	//Create methods
	//INCREMENT ID
//	@PostMapping("/add")
//	public void addDestination(@RequestParam String country, @RequestParam int lat, @RequestParam int lon, @RequestParam String info, @RequestParam String place) {
//		Destination myDest = new Destination();
//		myDest.setPlace(place);
//		myDest.setCountry(country);
//		myDest.setLatitude(lat);
//		myDest.setLongitude(lon);
//		myDest.setInfo(info);
//		destRepo.save(myDest);
//		//Image is optional so
//	}
	@PostMapping("/add")
	public Destination addDestination(@RequestBody Destination myDest) {
		return destRepo.save(myDest);
	}
	//Read methods
	@GetMapping("/destination/{id}")
	public Destination getDestination(@PathVariable("id") int id) {
		//1. Look thru database for whatever the id is
		//2. Return a destination from ID
		Destination myDest = destRepo.findById(id).get();
		return myDest;
	}
	@GetMapping("/destinations")
	//Returns the landing page or in this case a list of destination objects
	public Iterable<Destination> getDestinations() {
		return destRepo.findAll();
	}
	//Update methods
		/*
		private String place;
		private String country;
		private float latitude;
		private float longitude;
		private String info;
		private String image;
		 */
	//FIX UPDATE
	@PutMapping("/updatecountry/{id}")
	public void updateDest(@PathVariable("id") int id, @RequestParam String country) {
		Destination myDest = getDestination(id);
//		System.out.println(myDest.getCountry());
		myDest.setCountry(country);
		destRepo.save(myDest);
		//Optional parameters here
	}
	//Delete Methods
	@PostMapping("/delete/{id}")
	public void deleteDest() {
		
	}
	@PostMapping("/deleteAll")
	//Delete the entire MongoDB repo
	public String deleteAll() {
		destRepo.deleteAll();
		return "All destinations deleted";
	}
}
