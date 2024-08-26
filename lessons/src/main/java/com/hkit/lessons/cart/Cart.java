package com.hkit.lessons.cart;

import java.time.LocalDateTime;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.member.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "cart")
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long listNum;
	
	private char opt;
	
	@ManyToOne
	@JoinColumn(name="memberNum")
	private Member member; 

	@ManyToOne
	@JoinColumn(name="lessonNum")
	private Lesson lesson;
	
	private LocalDateTime createDate;
}
