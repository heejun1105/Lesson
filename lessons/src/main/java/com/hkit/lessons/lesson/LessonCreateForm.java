package com.hkit.lessons.lesson;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.hkit.lessons.category.Category;
import com.hkit.lessons.pro.Pro;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LessonCreateForm {
	
	@NotEmpty(message = "강의 제목은 필수 항목입니다.")
	private String lname;
	
	private Pro proNum;

	private Category categoryNum;
	
	@NotEmpty(message = "강의 내용은 필수 항목입니다.")
	private String introduce;
	
	@DateTimeFormat
	private LocalDateTime createTime;
	private LocalDateTime modifyTime;
	
	private String lesson_image;
}
