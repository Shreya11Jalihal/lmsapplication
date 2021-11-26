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
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
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
	@NotNull
	private String name;
	
	
	@Column(name="INSTRUCTOR")
	@NotNull
	private String instructor;
	
	
	@Column(name="PRICE")
	@NotNull
	private BigDecimal price;
	
	
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH},fetch = FetchType.LAZY)
	private Set<ScheduleDao> availableDates = new HashSet<ScheduleDao>();
	

}
