package com.hkit.lessons;


import java.time.LocalDateTime;
import java.util.Optional;

import java.time.LocalDate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.lesson.LessonRepository;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminsService {
	private final LessonRepository lessonRepository;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	public Lesson create(String Lname, String Introduce, String lessonImage) {
		
		Lesson lesson = new Lesson();
		
		lesson.setLname(Lname);
		lesson.setLessonImage(lessonImage);
		lesson.setIntroduce(Introduce);
		lesson.setCreateDate(LocalDateTime.now());
		lesson.setModifyDate(LocalDateTime.now());
		
		return lessonRepository.save(lesson);
	}
	
	
	public Lesson GetLesson(Long lessonNum) {
		Optional<Lesson> Lesson = this.lessonRepository.findById(lessonNum);
		if (Lesson.isPresent()) {
			return Lesson.get();
		}else {
			throw new DataNotFoundException("Lesson not found");
		}
	}
	
	
	
	

	public Member create(String memberId, String email, String password, String phone, String memberName,
			String address, LocalDate birth,String gender) {
		
		Member member = new Member();
		
		member.setMemberId(memberId);
		member.setPassword(passwordEncoder.encode(password));
		member.setBirth(birth);
		member.setPhone(phone);
		member.setMemberName(memberName);
		member.setEmail(email);
		member.setGender(gender);
		member.setAddress(address);
		
		this.memberRepository.save(member);
		
		return member;
	} 
	
	public Member getMember(String memberId) {
		Optional<Member> Member = this.memberRepository.findByMemberId(memberId);
		if (Member.isPresent()) {
			return Member.get();
		}else {
			throw new DataNotFoundException("Member not found");
		}
	}
}
