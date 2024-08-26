package com.hkit.lessons.enrollment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.options.LessonOption;
import com.hkit.lessons.options.LessonOptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
	
	@Autowired
	private final EnrollmentRepository enrollmentRepository;
	private final LessonOptionRepository lessonOptionRepository;
	
	public Enrollment getEnrollment(int id) {
		return enrollmentRepository.findById(id);
	}
	
	public Enrollment getfinalOrder() {
		return enrollmentRepository.findFirstByOrderByIdDesc();
	}
	
	public Page<Enrollment> getList(int page){
		Pageable pageable = PageRequest.of(page, 10);
		return enrollmentRepository.findAll(pageable);
	}
	
	public Page<Enrollment> orderList(Member member, int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return enrollmentRepository.findByMember(member, pageable);
	}
	
	public List<Enrollment> orderList(Member member){
		return enrollmentRepository.findByMember(member);
	}

	public void create(Lesson lessonNum, Member memberNum,char opt) {
    	Enrollment enrollment = new Enrollment();

    	enrollment.setLesson(lessonNum);
    	enrollment.setMember(memberNum);
    	enrollment.setEnrollDate(LocalDateTime.now());
    	enrollment.setPaymentState("결제 전");
    	enrollment.setOpt(opt);
    	enrollmentRepository.save(enrollment);
    }
	
	
	public void modify(Enrollment enrollment) {
		enrollmentRepository.save(enrollment);
	}
		
	public void delete(Enrollment enrollment) {
		enrollmentRepository.delete(enrollment);
	}
	
	public Enrollment saveEnrollment(Enrollment enrollment) {
		return enrollmentRepository.save(enrollment);
	}
	
	public List<Enrollment> getCompletedEnrollmentsByMember(Member member) {
	    return enrollmentRepository.findCompletedEnrollmentsWithLessonByMember(member);
	}

	public List<Enrollment> getPendingEnrollmentsByMember(Member member) {
	    return enrollmentRepository.findPendingEnrollmentsWithLessonByMember(member);
	}
	
	
	public String getEnrollmentPrice(Enrollment enrollment) {
        Lesson lesson = enrollment.getLesson();
        char opt = enrollment.getOpt();
        
        // 해당 레슨의 모든 옵션을 가져옵니다.
        List<LessonOption> options = lessonOptionRepository.findByLessonNum(lesson);
        
        // 옵션 중에서 enrollment의 opt와 일치하는 것을 찾습니다.
        for (LessonOption option : options) {
            if (option.getLoption() == opt) {
                return option.getPrice();
            }
        }
        
        // 일치하는 옵션이 없을 경우
        return "가격 정보 없음";
    }
	
	public boolean isAlreadyEnrolled(Lesson lesson, Member member, char opt) {
        return enrollmentRepository.existsByLessonAndMemberAndOpt(lesson, member, opt);
    }

}

