package com.eruditeminds.lms.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.eruditeminds.lms.mapper.CourseMapper;
import com.eruditeminds.lms.model.Course;
import com.eruditeminds.lms.model.Schedule;
import com.eruditeminds.lms.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

	Timestamp timestamp1 = null;
	Timestamp timestamp2 = null;
	Set<Schedule> schedules = new HashSet<Schedule>();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CourseService courseService;

	@Mock
	private CourseMapper courseMapper;

	private JacksonTester<Course> jsonCourse;

	@BeforeEach
	void Setup() {
		JacksonTester.initFields(this, new ObjectMapper());

		Schedule schedule1 = Schedule.builder().scheduleId(Long.valueOf(1))
				.timestamp(Timestamp.valueOf("2022-09-11 09:01:15")).slots(5).build();
		Schedule schedule2 = Schedule.builder().scheduleId(Long.valueOf(2))
				.timestamp(Timestamp.valueOf("2021-10-01 09:01:15")).slots(6).build();
		schedules.add(schedule1);
		schedules.add(schedule2);

	}

	@Test
	 void canCreateANewCourse() throws Exception {
		Course course = Course.builder().courseId(Long.valueOf(1)).name("Java").instructor("Michael Porsche")
				.price(BigDecimal.valueOf(234.5)).availableDates(schedules).build();
		// given
		when(courseService.saveCourse(course)).thenReturn(course);

		// when
		MockHttpServletResponse response = mockMvc
				.perform(
						MockMvcRequestBuilders.post("/api/courses").contentType(MediaType.APPLICATION_JSON)
								.content(jsonCourse.write(Course.builder().courseId(Long.valueOf(1)).name("Java")
										.instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
										.availableDates(schedules).build()).getJson()))
				.andReturn().getResponse();
		// then
		Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
	}

	@Test
	 void testGetAllCourses() throws Exception {
		List<Course> courses = Arrays.asList(
				Course.builder().courseId(Long.valueOf(1)).name("Java").instructor("Michael Porsche")
						.price(BigDecimal.valueOf(234.5)).availableDates(schedules).build(),
				Course.builder().courseId(Long.valueOf(2)).name("Python").instructor("Sheela Bantle")
						.price(BigDecimal.valueOf(455.5)).availableDates(schedules).build());

		when(courseService.getAllCourses()).thenReturn(courses);

		MockHttpServletResponse response = mockMvc
				.perform(MockMvcRequestBuilders.get("/api/courses").accept(MediaType.APPLICATION_JSON)).andReturn()
				.getResponse();

		Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

	}

	@Test
	 void testupdateCourse() throws Exception {

		Course course = Course.builder().name("Java").instructor("Michael Porsche").price(BigDecimal.valueOf(234.5))
				.availableDates(schedules).build();
		Course updatedCourse = Course.builder().name("Java").instructor("George").price(BigDecimal.valueOf(274.5))
				.availableDates(schedules).build();

		when(courseService.updateCourse(course, 2)).thenReturn(updatedCourse);

		MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/2")
				.contentType(MediaType.APPLICATION_JSON).content(jsonCourse.write(course).getJson())).andReturn()
				.getResponse();

		Assertions.assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		Assertions.assertThat(response.getContentAsString())
				.contains("Successfully updated the course in the collection");

	}
	
	

}
