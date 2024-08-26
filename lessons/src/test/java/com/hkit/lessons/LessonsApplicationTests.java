package com.hkit.lessons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hkit.lessons.category.Category;
import com.hkit.lessons.category.CategoryRepository;
import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.lesson.LessonRepository;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberRepository;

@SpringBootTest
class LessonsApplicationTests {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private LessonRepository lr;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Test
	void cateCreate(){
		Category category = new Category();

		category.setFirClass("외국어");
		category.setSecClass("영어");

		this.categoryRepository.save(category);
	}
	@Test
	void delete() {
		assertEquals(3, this.memberRepository.count());
		Optional<Member> om = this.memberRepository.findByMemberId("hkit2");
		assertTrue(om.isPresent());
		Member m = om.get();
		this.memberRepository.delete(m);
		assertEquals(2, this.memberRepository.count());
	}
	
//	@Test
	void modify() {
		Optional<Member> om = this.memberRepository.findByMemberNum(1L);
		assertTrue(om.isPresent());
		Member m = om.get();
		m.setEmail("변경된 이메일");
		this.memberRepository.save(m);
	}
	// 회원정보 수정
	
//	@Test
	void testCreate() {
		Member m1 = new Member();
		Member m2 = new Member();
		Member m3 = new Member();
//		m1.setAddress("대구시 범어동");
//		m1.setBirth(new Date(1995, 11, 5));
//		m1.setEmail("abc@naver.com");
//		m1.setGender("M");
//		m1.setMemberId("hkit");
//		m1.setMemberName("홍길동");
//		m1.setPassword("1234");
//		m1.setPhone("010-1234-5678");
//		
//		this.memberRepository.save(m1);
		
		m3.setAddress("대구시 범물동");
		m3.setBirth(LocalDate.of(1995, 11, 5));
		m3.setEmail("abc3@naver.com");
		m3.setGender("M");
		m3.setMemberId("hkit3");
		m3.setMemberName("박길동");
		m3.setPassword("1234");
		m3.setPhone("010-5678-5678");
		
		this.memberRepository.save(m3);
	}

//		//@Test
//		void testLesson() {
//			Lesson l1 = new Lesson();
//			
//			l1.setLname("강의 제목입니다.");
//			l1.setPrice(39800);
//			l1.setCreateDate(LocalDateTime.now());
//			this.lr.save(l1);
//		} 
	}

