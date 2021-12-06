package com.example.demo.exceptions;

public class DestinationNotFoundException extends Exception{
	private int id;
	public DestinationNotFoundException(int id) {
		super(String.format("Destination not found with id: '%s'", id));
	}
}
