package com.eruditeminds.lms.dao.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class CourseDao {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="COURSEID")
	@EqualsAndHashCode.Include 
	private long courseId;
	
	
	@Column(name="NAME")
	private String name;
	
	
	@Column(name="INSTRUCTOR")
	private String instructor;
	
	
	@Column(name="PRICE")
	private BigDecimal price;
	
	
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
	@JsonIgnoreProperties
	private Set<ScheduleDao> availableDates = new HashSet<ScheduleDao>();
	

	public CourseDao(String name, String instructor, BigDecimal price, Set<ScheduleDao> availableDates) {
		super();
		this.name = name;
		this.instructor = instructor;
		this.price = price;
		this.availableDates = availableDates;
	}
	
	

}
