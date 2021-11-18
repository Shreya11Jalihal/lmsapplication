package com.eruditeminds.lms.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eruditeminds.lms.dao.model.CourseDao;
import com.eruditeminds.lms.dao.model.ScheduleDao;
import com.eruditeminds.lms.model.Course;
import com.eruditeminds.lms.model.Schedule;

/**
 * Mapper class to map CourseDao and Model beans
 * 
 * @author Shreya S Jalihal
 */
@Component
public class CourseMapper {

	public Course convertToModel(CourseDao courseDao) {
		return Course.builder().name(courseDao.getName()).courseId(courseDao.getCourseId()).price(courseDao.getPrice())
				.instructor(courseDao.getInstructor())
				.availableDates(convertToCollectionScheduleModel(courseDao.getAvailableDates())).build();
	}

	public CourseDao convertToDao(Course course) {
		return CourseDao.builder().courseId(course.getCourseId()).name(course.getName())
				.instructor(course.getInstructor())
				.availableDates(convertToCollectionScheduleDao(course.getAvailableDates())).price(course.getPrice())
				.build();

	}

	public Set<Schedule> convertToCollectionScheduleModel(Set<ScheduleDao> scheduleDaos) {
		if (scheduleDaos != null && !scheduleDaos.isEmpty())

			return scheduleDaos.stream().map(temp -> {
				return Schedule.builder().scheduleId(temp.getScheduleId()).timestamp(temp.getTimestamp())
						.slots(temp.getSlots()).build();
			}).collect(Collectors.toSet());

		return Collections.emptySet();

	}

	public Set<ScheduleDao> convertToCollectionScheduleDao(Set<Schedule> schedules) {
		if (schedules != null && !schedules.isEmpty())

			return schedules.stream().map(temp -> {
				return ScheduleDao.builder().scheduleId(temp.getScheduleId()).timestamp(temp.getTimestamp())
						.slots(temp.getSlots()).build();
			}).collect(Collectors.toSet());

		return Collections.emptySet();

	}

	public List<Course> convertToCollectionCourse(List<CourseDao> courseDaos) {
		List<Course> courses = new ArrayList<Course>();
		if (courseDaos.size() != 0 && !courseDaos.isEmpty()) {
			for (CourseDao courseDao : courseDaos) {
				courses.add(convertToModel(courseDao));
				
			}
			return courses;
		}
		return Collections.emptyList();

	}

	public List<CourseDao> convertToCollectionCourseDao(List<Course> courses) {
		List<CourseDao> courseDaos = new ArrayList<CourseDao>();
		if (courses.size() !=0 && !courses.isEmpty()) {
			for (Course course : courses) {
				courseDaos.add(convertToDao(course));
			}
			return courseDaos;
		}
		return Collections.emptyList();

	}

}
