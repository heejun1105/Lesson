package com.hkit.lessons.enrollment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.member.Member;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

	Page<Enrollment> findByMember(Member member, Pageable pageable);
	Page<Enrollment> findAll(Pageable pageable);
	List<Enrollment> findByMember(Member member);
	Enrollment findById(int id);
	Enrollment findFirstByOrderByIdDesc();
	List<Enrollment> findByMemberAndPaymentState(Member member, String string);
	boolean existsByLessonAndMemberAndOpt(Lesson lesson, Member member, char opt);
	
	boolean existsByMemberAndLesson(Member member, Lesson lesson);
	@Query("SELECT e FROM Enrollment e JOIN FETCH e.lesson WHERE e.member = :member AND e.paymentState = '결제완료'")
	List<Enrollment> findCompletedEnrollmentsWithLessonByMember(@Param("member") Member member);

	@Query("SELECT e FROM Enrollment e JOIN FETCH e.lesson WHERE e.member = :member AND e.paymentState != '결제완료'")
	List<Enrollment> findPendingEnrollmentsWithLessonByMember(@Param("member") Member member);
	
}
                                             