package com.example.demo.resources;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.models.Destination;
import org.springframework.stereotype.Repository;

@Repository
public interface DestRepository extends JpaRepository<Destination,Integer>{

}
