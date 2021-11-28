package com.eruditeminds.lms.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eruditeminds.lms.dao.model.CourseDao;
import com.eruditeminds.lms.exception.ResourceNotFoundException;
import com.eruditeminds.lms.exception.ValidationException;
import com.eruditeminds.lms.mapper.CourseMapper;
import com.eruditeminds.lms.model.Course;
import com.eruditeminds.lms.repository.CourseRepository;

@Service
public class CourseService {

	private static final Logger logger = LoggerFactory.getLogger(CourseService.class);

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	CourseMapper courseMapper;

	public List<Course> getAllCourses() {
		List<CourseDao> courseDaos = courseRepository.findAll();
		List<Course> courses = courseMapper.convertToCollectionCourse(courseDaos);
		if (courseDaos.isEmpty())
			return Collections.emptyList();
		logger.info("list of courses retrieved successfully,count: " + courses.size());
		return courses;
	}

	/*
	 * Method to save a course in a collection of Courses
	 * 
	 * @param course Course
	 * 
	 * @return course of type Course
	 * 
	 */

	public Course saveCourse(Course course) {
		Set<Timestamp> timeStamps = new HashSet<>();
		if (course != null && !course.getAvailableDates().isEmpty()) {
			initValidateTimestamp(course);
		}
		 courseRepository.findByNameAndInstructor(course.getName(), course.getInstructor());
		CourseDao courseDao = null;
		if (timeStamps.isEmpty()) {
			courseDao = courseMapper.convertToDao(course);
			courseRepository.save(courseDao);
		} else {
			course.getAvailableDates().forEach(t1 -> {
				timeStamps.forEach(t -> {
					if (t.getYear() == t1.getTimestamp().getYear() && t.getMonth() == t1.getTimestamp().getMonth()
							&& t.getDate() == t1.getTimestamp().getDate())
						throw new ValidationException("The Course: " + course.getName() + "already exists");
				});
			});
			courseDao = courseMapper.convertToDao(course);
			courseRepository.save(courseDao);
			
		}
		logger.info("Saved the course Details successfully in the database");
		return course;
	}

	/*
	 * Method to update a Course
	 * 
	 * @Param course Course
	 * 
	 * @Param courseId long
	 * 
	 * @return course Course
	 * 
	 */
	
	public Course updateCourse(Course course, long courseId) {
		if (course != null && !course.getAvailableDates().isEmpty())
			initValidateTimestamp(course);

		Optional<CourseDao> optionalCourseDao = courseRepository.findById(courseId);
		if (optionalCourseDao.isPresent()) {
			CourseDao courseDao = courseMapper.convertToDao(course);
			courseDao.setCourseId(courseId);
			
			courseRepository.save(courseDao);
			logger.info("Course with Id " + courseId + " updated successfully.");
		} else
			throw new ResourceNotFoundException("Course with courseId " + courseId + "not found");
		logger.error("Resource with Id " + courseId + " not found");
		return course;
	}

	/*
	 * method for initial validation of Timestamp
	 * 
	 * @Param course Course
	 *
	 */
	public static void initValidateTimestamp(Course course) {
		course.getAvailableDates().forEach(t -> {
			if (t.getTimestamp().getTime() < (Timestamp.from(Instant.now()).getTime())) {
				logger.error("Validation failed Timestamp is invalid.");
				throw new ValidationException("Initial validation for the schedule Dates failed");
			}

		});
	}

	/*
	 * Get Courses for a time period range
	 * 
	 * @Param startDate String
	 * 
	 * @Param endDate String
	 *
	 * List<Course> course
	 */
	public List<Course> getAllCoursesForACertainPeriod(LocalDate startDate, LocalDate endDate) {
	List<CourseDao> courseDaos = courseRepository.findForACertainPeriod(Date.valueOf(startDate), Date.valueOf(endDate));
		logger.info("Courses for the requested period returned. size" + courseDaos.size());
	return courseMapper.convertToCollectionCourse(courseDaos);
		
	}

	public void deleteCourse(long courseId) {
		Optional<CourseDao> courseDao= courseRepository.findById(courseId);
		if(courseDao.isPresent())	
		courseRepository.deleteById(courseId);
	}

}
