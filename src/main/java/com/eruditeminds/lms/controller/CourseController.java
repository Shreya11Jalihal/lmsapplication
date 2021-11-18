package com.eruditeminds.lms.controller;

import java.sql.Timestamp;
import java.time.Instant;
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
import org.springframework.web.bind.annotation.RestController;

import com.eruditeminds.lms.exception.ValidationException;
import com.eruditeminds.lms.model.Course;
import com.eruditeminds.lms.service.CourseService;

@CrossOrigin(origins = { "http://localhost:8083", "http://localhost:9002"}, maxAge = 3600)
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
	public List<Course> getAllCourses() {
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
		if (course != null && course.getAvailableDates().size() != 0) {
			course.getAvailableDates().forEach(t -> {
				if (t.getTimestamp().getTime() < (Timestamp.from(Instant.now()).getTime())) {
					logger.error("Validation failed Timestamp is invalid.");
					throw new ValidationException("Initial validation for the schedule Dates failed");
				}
				
			});
			courseService.saveCourse(course);
		} 
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
