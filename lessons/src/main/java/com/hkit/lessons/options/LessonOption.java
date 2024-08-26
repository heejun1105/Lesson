package com.hkit.lessons.options;

import java.time.LocalDateTime;

import com.hkit.lessons.category.Category;
import com.hkit.lessons.lesson.Lesson;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class LessonOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long optionNum;

	//b(Basic), s(Standard), p(Professional) 중 하나
	@Column(nullable = false)
    private char loption;

	//준비물제공여부
	@Column(nullable = false)
    private boolean needs;

	//강의실제공여부
	@Column(nullable = false)
    private boolean place;

	//1회당레슨시간
	@Column(nullable = false, length = 50)
    private String onetime;

	//주당강의횟수
	@Column(nullable = false, length = 50)
    private String weekdate;

	//총강의횟수
	@Column(nullable = false, length = 50)
    private String totaldate;

	//가격
	@Column(nullable = false, length = 50)
    private String price;
	
	@Column(nullable = false)
    private String info;

	@ManyToOne
	@JoinColumn(name = "lessonNum") 
	private Lesson lessonNum;

}