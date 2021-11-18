package com.eruditeminds.lms.repository;

import java.sql.Timestamp;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eruditeminds.lms.dao.model.CourseDao;

@Repository
public interface CourseRepository extends JpaRepository<CourseDao, Long> {

	
	//@Query("SELECT a FROM CourseDao c JOIN c.availableDates a WHERE c.name=:name AND c.instructor=:instructor")
	@Query("SELECT a.timestamp FROM CourseDao c JOIN c.availableDates a WHERE c.name=:name AND c.instructor=:instructor")
	public Set<Timestamp> findByNameAndInstructor (@Param("name") String name,@Param("instructor") String instrcutor);
	
	
}
