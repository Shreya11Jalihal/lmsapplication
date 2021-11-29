package com.eruditeminds.lms.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;


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
 class CourseServiceTest {

	Set<ScheduleDao> listScheduleDao = new HashSet<ScheduleDao>();
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
	void Setup() throws Exception {
		
		ScheduleDao scheduleDao1 = ScheduleDao.builder().scheduleId(Long.valueOf(1)).timestamp(Timestamp.valueOf("2022-09-11 09:01:15")).slots(Long.valueOf(5)).build();
		ScheduleDao scheduleDao2 = ScheduleDao.builder().scheduleId(Long.valueOf(2)).timestamp(Timestamp.valueOf("2024-09-11 09:01:15")).slots(Long.valueOf(6)).build();
		listScheduleDao.add(scheduleDao1);
		listScheduleDao.add(scheduleDao2);
		
		Schedule schedule1 = Schedule.builder().scheduleId(Long.valueOf(1)).timestamp(Timestamp.valueOf("2022-09-11 09:01:15")).slots(Long.valueOf(5)).build();
		Schedule schedule2 = Schedule.builder().scheduleId(Long.valueOf(2)).timestamp(Timestamp.valueOf("2024-09-11 09:01:15")).slots(Long.valueOf(6)).build();
		listSchedule.add(schedule1);
		listSchedule.add(schedule2);
			
			
			timeStamp1= Timestamp.valueOf("2022-09-11 09:01:15");
			timeStamp2= Timestamp.valueOf("2024-09-11 09:01:15");
			timeStamps.add(timeStamp1);
			timeStamps.add(timeStamp2);
		}
	

	@Test
	 void testGetAllCourses() {

		List<CourseDao> courseDaos = Arrays.asList(
				CourseDao.builder().name("Java").instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
						.availableDates(listScheduleDao).build(),
				CourseDao.builder().name("Python").instructor("Sheela Bantle").price(BigDecimal.valueOf(455.5))
						.availableDates(listScheduleDao).build());
		List<Course> courses = Arrays.asList(
				Course.builder().name("Java").instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
						.availableDates(listSchedule).build(),
				Course.builder().name("Python").instructor("Sheela Bantle").price(BigDecimal.valueOf(455.5))
						.availableDates(listSchedule).build());
		when(courseRepository.findAll()).thenReturn(courseDaos);
		when(courseMapper.convertToCollectionCourse(courseDaos)).thenReturn(courses);

		List<Course> coursesRetrieved = courseService.getAllCourses();
		assertEquals(2, coursesRetrieved.size());
		assertFalse(coursesRetrieved.isEmpty());
		assertNotNull(coursesRetrieved.get(0).getCourseId());
		verify(courseRepository, times(1)).findAll();

	}

	@Test
	 void testCreateCourse() {
		
		Course course = Course.builder().name("AWS").instructor("Akash Gupta").price( BigDecimal.valueOf(345.6)).availableDates(listSchedule).build();
		when(courseService.saveCourse(course)).thenReturn(course);
		CourseDao courseDao = CourseDao.builder().name("AWS").instructor("Akash Gupta").price( BigDecimal.valueOf(345.6)).availableDates(listScheduleDao).build();
		when(courseMapper.convertToDao(course)).thenReturn(courseDao);

		Course savedCourse= courseService.saveCourse(course);
		assertEquals(savedCourse.getCourseId(),course.getCourseId());
		assertEquals(savedCourse.getName(),course.getName());
		verify(courseRepository, times(1)).save(courseMapper.convertToDao(course));
	}

	@Test
	 void testValidationException() {
		when(courseMapper.convertToCollectionScheduleModel(listScheduleDao)).thenReturn(listSchedule);
		Course course = Course.builder().name("AWS").instructor("Akash Gupta").price( BigDecimal.valueOf(345.6)).availableDates(listSchedule).build();
		when(courseRepository.findByNameAndInstructor("AWS", "Akash Gupta")).thenReturn(timeStamps);
		assertThrows(ValidationException.class,
				() -> courseService.saveCourse(course));
	}

	@Test
	 void testUpdateCourse() {
		
		//given
		CourseDao courseDao = CourseDao.builder().name("AWS").instructor("Stewart").price( BigDecimal.valueOf(145.6)).availableDates(listScheduleDao).build();
		Course course = Course.builder().name("AWS").instructor("Akash Gupta").price( BigDecimal.valueOf(345.6)).availableDates(listSchedule).build();
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
	 void testResourceDoesNotExistsException() {
		when(courseRepository.findById(Long.valueOf(2))).thenReturn(Optional.empty());
		Course course = Course.builder().name("AWS").instructor("Akash Gupta").price( BigDecimal.valueOf(345.6)).availableDates(listSchedule).build();
		assertThrows(ResourceNotFoundException.class,
				() -> courseService.updateCourse(course, 2));
	}
	
	@Test
	 void testDeleteCourse() {
		Optional<CourseDao> optionalCourseDao = Optional.of(CourseDao.builder().name("AWS").instructor("Stewart").price( BigDecimal.valueOf(145.6)).availableDates(listScheduleDao).build());
		when(courseRepository.findById(Long.valueOf(1))).thenReturn(optionalCourseDao);
		doNothing().when(courseRepository).deleteById(1L);
		
		courseService.deleteCourse(1L);
		verify(courseRepository, times(1)).deleteById(1L);
		
	}
	
}
