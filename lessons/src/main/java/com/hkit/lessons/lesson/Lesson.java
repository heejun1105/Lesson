package com.hkit.lessons.lesson;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.hkit.lessons.cart.Cart;
import com.hkit.lessons.category.Category;
import com.hkit.lessons.enrollment.Enrollment;
import com.hkit.lessons.options.LessonOption;
import com.hkit.lessons.pro.Pro;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "lesson")
public class Lesson {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long lessonNum;
	
	@Column(nullable = false, length = 50)
    private String lname;
	
	
	private String lessonImage;
	
	private String introduce;
	
	private LocalDateTime createDate;
	
	private LocalDateTime modifyDate;
	
	@ManyToOne
    @JoinColumn(name = "categoryNum")
    private Category categoryNum;
	
	@OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cart> carts = new ArrayList<>();
	

	@OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Enrollment> enrollments = new ArrayList<>();
	
	@ManyToOne
    @JoinColumn(name = "proNum")
    private Pro proNum;
	
	
	@OneToMany(mappedBy = "lessonNum", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonOption> lessonOptions = new ArrayList<>();
	
}