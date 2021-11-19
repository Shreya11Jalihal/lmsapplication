package com.eruditeminds.lms.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eruditeminds.lms.model.Course;
import com.eruditeminds.lms.service.CourseService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CourseController {

	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	private CourseService courseService;

	/*
	 * End Point to get all the Courses
	 * 
	 * @return List of courses
	 */
	@GetMapping(path = "/api/courses")
	public List<Course> getAllCourses(@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		if (startDate != null && endDate != null) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate ipstartDate = null;
			LocalDate ipendDate = null;
			try {
				ipstartDate = LocalDate.parse(startDate, formatter);
				ipendDate = LocalDate.parse(endDate, formatter);
				return courseService.getAllCoursesForACertainPeriod(ipstartDate, ipendDate);
			} catch (DateTimeParseException e) {
				logger.error("could not parse the input dates");
				throw new DateTimeParseException("", endDate, 0);
			}
		} else
			return courseService.getAllCourses();
	}

	/*
	 * save a new Course to the collection of courses
	 * 
	 * @Param course Course
	 * 
	 * @return ResponseEntity
	 */
	@PostMapping("/api/courses")
	public ResponseEntity<String> createCourse(@RequestBody Course course) {

		courseService.saveCourse(course);

		return new ResponseEntity<>("Successfully Saved the course", HttpStatus.CREATED);
	}

	/*
	 * update an existing Course
	 * 
	 * @Param course Course
	 * 
	 * @PathVariable courseId long
	 * 
	 * @return ResponseEntity
	 */
	@PutMapping("/api/courses/{courseId}")
	public ResponseEntity<String> updateCourse(@RequestBody Course course, @PathVariable("courseId") long courseId) {
		courseService.updateCourse(course, courseId);
		return new ResponseEntity<>("Successfully updated the course in the collection", HttpStatus.OK);

	}
}
