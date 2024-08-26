package com.hkit.lessons.enrollment;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "member_num")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lesson_num")
    private Lesson lesson;

    @Column(nullable = false)
    private LocalDateTime enrollDate;
    
    private char opt;

    @ColumnDefault("'결제 전'")
	private String paymentState;

   
}