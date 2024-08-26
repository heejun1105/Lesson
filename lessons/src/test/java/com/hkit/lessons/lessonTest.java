package com.hkit.lessons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.apache.catalina.util.ServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.lesson.LessonController;
import com.hkit.lessons.lesson.LessonRepository;

@SpringBootTest
public class lessonTest {
	
	@Autowired
	private LessonRepository lessonRepository;
	private LessonController lessonController;
	
//	@Test
//	void testCreate() {
//		Lesson l1 = new Lesson();
//		
//		l1.setLname("보컬교육");
//		l1.setPrice(88000);
//		l1.setIntroduce("보컬 발성에 대한 기초 교육 과정입니다.");
//		this.lessonRepository.save(l1);
//	}
//	


	//@Test
	void lessonDelete() {
		assertEquals(3, this.lessonRepository.count());
        Optional<Lesson> ol = this.lessonRepository.findByLessonNum(1L);
        assertTrue(ol.isPresent());
        Lesson l = ol.get();
        this.lessonRepository.delete(l);
        assertEquals(2, this.lessonRepository.count());
	}
	@Test
	void version(){
		
		System.out.println("자바 버전 :"+System.getProperty("java.version"));

		String tomcatVersion = ServerInfo.getServerNumber();
		System.out.println("Tomcat version: " + tomcatVersion);
	}
}
