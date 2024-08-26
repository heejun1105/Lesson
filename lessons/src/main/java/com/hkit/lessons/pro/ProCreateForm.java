package com.hkit.lessons.pro;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DynamicInsert
public class ProCreateForm {

	@NotEmpty(message = "자기소개는 필수 항목입니다.")
	private String introduce;

	@DateTimeFormat
	private LocalDateTime createDate;

	private Long memberNum;
}