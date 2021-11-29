package com.eruditeminds.lms.repo;

import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.eruditeminds.lms.dao.model.CourseDao;
import com.eruditeminds.lms.dao.model.ScheduleDao;
import com.eruditeminds.lms.repository.CourseRepository;
@DataJpaTest
 class CourseRepoTest {

	Set<ScheduleDao> schedules = new HashSet<ScheduleDao>();
	Timestamp timeStamp1= null;
	Timestamp timeStamp2= null;
	Set<Timestamp> timeStamps= new HashSet<Timestamp>();

	@Mock
	private CourseRepository courseRepository;

	@BeforeEach
	void Setup() {	
		ScheduleDao scheduleDao1 = ScheduleDao.builder().scheduleId(Long.valueOf(1)).timestamp(Timestamp.valueOf("2022-09-11 09:01:15")).slots(Long.valueOf(5)).build();
		ScheduleDao scheduleDao2 = ScheduleDao.builder().scheduleId(Long.valueOf(2)).timestamp(Timestamp.valueOf("2024-09-11 09:01:15")).slots(Long.valueOf(6)).build();
		schedules.add(scheduleDao1);
		schedules.add(scheduleDao2);
		
		timeStamp1= Timestamp.valueOf("2022-09-11 09:01:15");
		timeStamp2= Timestamp.valueOf("2024-09-11 09:01:15");
		timeStamps.add(timeStamp1);
		timeStamps.add(timeStamp2);
	}

	@Test
	 void saveCourseTest() {
		CourseDao courseDao = CourseDao.builder().name("Java").instructor("Michael Porsche")
				.price(BigDecimal.valueOf(234.5)).availableDates(schedules).build();
		when(courseRepository.save(courseDao)).thenReturn(courseDao);

		Assertions.assertThat(courseDao.getCourseId()).isNotNull();
	}

	@Test
	 void getListOfCourses() {
		List<CourseDao> courseDaos = Arrays.asList(
				CourseDao.builder().name("Java").instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
						.availableDates(schedules).build(),
				CourseDao.builder().name("Python").instructor("Sheela Bantle").price(BigDecimal.valueOf(455.5))
						.availableDates(schedules).build());
		when(courseRepository.findAll()).thenReturn(courseDaos);
		List<CourseDao> retrievedCourseDaos = courseRepository.findAll();
		Assertions.assertThat(retrievedCourseDaos.size()).isGreaterThan(0);
	}

	@Test
	 void testGetSchedulesByNameAndInstructor() {
		CourseDao courseDao = CourseDao.builder().name("AWS").instructor("Akash Gupta").price(BigDecimal.valueOf(345.6)).availableDates(schedules).build();
		when(courseRepository.save(courseDao)).thenReturn(courseDao);
		when(courseRepository.findByNameAndInstructor("AWS", "Akash Gupta")).thenReturn(timeStamps);
		Set<Timestamp> retrievedSchedules = courseRepository.findByNameAndInstructor("AWS", "Akash Gupta");
		Assertions.assertThat(retrievedSchedules.size()).isEqualTo(2);
	}
	
	

}
