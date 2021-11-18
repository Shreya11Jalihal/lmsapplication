package com.eruditeminds.lms.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;

import com.eruditeminds.lms.dao.model.ScheduleDao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

	@JsonProperty("courseId")
	private long courseId;

	@JsonProperty("name")
	private String name;
	
	@JsonProperty("instructor")
	private String instructor;
	
	@JsonProperty("price")
	private BigDecimal price;
	
	@JsonProperty("availableDates")
	@JsonIgnoreProperties
	private Set<Schedule> availableDates = new HashSet<Schedule>();


	public Course(String name, String instructor, BigDecimal price, Set<Schedule> availableDates) {
		super();
		this.name = name;
		this.instructor = instructor;
		this.price = price;
		this.availableDates = availableDates;
	}
	
	
	

}
