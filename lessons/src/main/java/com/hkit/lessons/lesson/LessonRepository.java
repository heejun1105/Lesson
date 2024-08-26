package com.hkit.lessons.lesson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hkit.lessons.member.Member;

import java.util.List;
import java.util.Optional;


public interface LessonRepository extends JpaRepository<Lesson, Long> {
	Optional<Lesson> findByLessonNum(Long lessonNum);
	Page<Lesson> findAll(Pageable pageable);
	Optional<Lesson> findByLname(String lname);
	//레슨이름 검색
	List<Lesson> findByLnameLike(String lname);
	//레슨이름 부분검색
	
	Page<Lesson> findAll(Specification<Lesson> specification,Pageable pageable);
}
