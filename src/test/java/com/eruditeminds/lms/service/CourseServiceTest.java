package com.eruditeminds.lms.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.eruditeminds.lms.dao.model.CourseDao;
import com.eruditeminds.lms.dao.model.ScheduleDao;
import com.eruditeminds.lms.exception.ResourceNotFoundException;
import com.eruditeminds.lms.exception.ValidationException;
import com.eruditeminds.lms.mapper.CourseMapper;
import com.eruditeminds.lms.model.Course;
import com.eruditeminds.lms.model.Schedule;
import com.eruditeminds.lms.repository.CourseRepository;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceTest {

	Set<ScheduleDao> schedules = new HashSet<ScheduleDao>();
	Set<Schedule> listSchedule = new HashSet<Schedule>();
	Timestamp timeStamp1= null;
	Timestamp timeStamp2= null;
	Set<Timestamp> timeStamps= new HashSet<Timestamp>();

	@InjectMocks
	private CourseService courseService;

	@Mock
	CourseRepository courseRepository;

	@Mock
	CourseMapper courseMapper;
	

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@BeforeEach
	void Setup()  throws Exception {
			Schedule schedule1 = new Schedule(Timestamp.valueOf("2022-09-11 09:01:15"),5);
			Schedule schedule2 = new Schedule(Timestamp.valueOf("2024-09-11 09:01:15"),6);
			listSchedule.add(schedule1);
			listSchedule.add(schedule2);
			
			ScheduleDao scheduleDao1 = new ScheduleDao(Timestamp.valueOf("2022-09-11 09:01:15"),5);
			ScheduleDao scheduleDao2 = new ScheduleDao(Timestamp.valueOf("2024-09-11 09:01:15"),6);
			schedules.add(scheduleDao1);
			schedules.add(scheduleDao2);
			
			
			Course course1=Course.builder()
						.courseId(1).name("Python").price(BigDecimal.valueOf(234.7)).availableDates(listSchedule).build();
			
			Course course2=Course.builder()
					.courseId(2).name("Java").price(BigDecimal.valueOf(234.7)).availableDates(listSchedule).build();
			
			timeStamp1= Timestamp.valueOf("2022-09-11 09:01:15");
			timeStamp2= Timestamp.valueOf("2024-09-11 09:01:15");
			timeStamps.add(timeStamp1);
			timeStamps.add(timeStamp2);
		}
	

	@Test
	public void testGetAllCourses() {

		List<CourseDao> courseDaos = Arrays.asList(
				CourseDao.builder().name("Java").instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
						.availableDates(schedules).build(),
				CourseDao.builder().name("Python").instructor("Sheela Bantle").price(BigDecimal.valueOf(455.5))
						.availableDates(schedules).build());
		List<Course> courses = Arrays.asList(
				Course.builder().name("Java").instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
						.availableDates(courseMapper.convertToCollectionScheduleModel(schedules)).build(),
				Course.builder().name("Python").instructor("Sheela Bantle").price(BigDecimal.valueOf(455.5))
						.availableDates(courseMapper.convertToCollectionScheduleModel(schedules)).build());
		when(courseRepository.findAll()).thenReturn(courseDaos);
		when(courseMapper.convertToCollectionCourse(courseDaos)).thenReturn(courses);

		List<Course> coursesRetrieved = courseService.getAllCourses();
		assertEquals(2, coursesRetrieved.size());
		assertFalse(coursesRetrieved.isEmpty());
		assertNotNull(coursesRetrieved.get(0).getCourseId());
		verify(courseRepository, times(1)).findAll();

	}

	@Test
	public void testCreateCourse() {
		Course course = new Course("AWS", "Akash Gupta", BigDecimal.valueOf(345.6), courseMapper.convertToCollectionScheduleModel(schedules));
		when(courseService.saveCourse(course)).thenReturn(course);
		CourseDao courseDao = new CourseDao("AWS", "Akash Gupta", BigDecimal.valueOf(345.6), schedules);
		when(courseMapper.convertToDao(course)).thenReturn(courseDao);

		Course savedCourse= courseService.saveCourse(course);
		assertEquals(savedCourse.getCourseId(),course.getCourseId());
		assertEquals(savedCourse.getName(),course.getName());
		verify(courseRepository, times(1)).save(courseMapper.convertToDao(course));
	}

	@Test
	public void testValidationException() {
		when(courseMapper.convertToCollectionScheduleModel(schedules)).thenReturn(listSchedule);
		Course course = new Course("AWS", "Akash Gupta", BigDecimal.valueOf(345.6), courseMapper.convertToCollectionScheduleModel(schedules));
		when(courseRepository.findByNameAndInstructor("AWS", "Akash Gupta")).thenReturn(timeStamps);
		assertThrows(ValidationException.class,
				() -> courseService.saveCourse(course));
	}

	@Test
	public void testUpdateCourse() {
		
		//given
		CourseDao courseDao = new CourseDao("AWS", "Stewart", BigDecimal.valueOf(154.6), schedules);
		Course course = new Course("AWS", "Akash Rathi", BigDecimal.valueOf(145.6), courseMapper.convertToCollectionScheduleModel(schedules));
		when(courseRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(courseDao));
		when(courseMapper.convertToDao(course)).thenReturn(courseDao);
		
		//when
		Course updatedCourse= courseService.updateCourse(course, 1);
		
		//then
		verify(courseRepository, times(1)).save(courseMapper.convertToDao(course));
		assertEquals(course.getName(),updatedCourse.getName());
	    assertEquals(course.getPrice(),updatedCourse.getPrice());

	}

	@Test
	public void testResourceDoesNotExistsException() {
		when(courseRepository.findById(Long.valueOf(1))).thenReturn(null);
		Course course = new Course("AWS", "Akash Gupta", BigDecimal.valueOf(345.6), courseMapper.convertToCollectionScheduleModel(schedules));
		assertThrows(ResourceNotFoundException.class,
				() -> courseService.updateCourse(course, 1));
	}
}
