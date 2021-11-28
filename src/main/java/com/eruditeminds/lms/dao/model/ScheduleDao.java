package com.eruditeminds.lms.dao.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long scheduleId;
	
	@Column(name="scheduleDate")
	@NotNull
	private Timestamp timestamp;
	
	@Column(name= "slots")
	private long slots;
	

}
