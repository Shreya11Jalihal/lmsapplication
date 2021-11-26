package com.eruditeminds.lms.model;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Schedule {

	@JsonProperty("scheduleId")
	private long scheduleId;

	@JsonProperty("timestamp")
	private Timestamp timestamp;

	@JsonProperty("slots")
	private long slots;
	

}
