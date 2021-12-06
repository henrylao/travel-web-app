package com.hcl.springboot.user.model;
/*
 * 1) Create a class for Destination in order for object creation
 * 2) Create getters and setters
 * 3) Make sure we can add 
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Destination {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//TO-DO: INCREMENT THE ID FOR MONGODB
	private int id;
	private String place;
	private String country;
	private float latitude;
	private float longitude;
	private String info;
	private String image;
	
	public Destination() {
		
	}
	
	public Destination(int destId, String place, String country, float latitude, float longitude, String info,
                       String image) {
		super();
		this.id = destId;
		this.place = place;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
		this.info = info;
		this.image = image;
	}
	
	public int getDestId() {
		return id;
	}
	public void setDestId(int destId) {
		this.id = destId;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}

/*
 * destId
 * place
 * country
 * latitude
 * longitude
 * info
 * image
 */
