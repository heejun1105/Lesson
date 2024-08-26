package com.hkit.lessons.category;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateForm {

	@NotEmpty(message = "대분류는 필수 항목입니다.")
	private String firClass;

	@NotEmpty(message = "소분류는 필수 항목입니다.")
	private String secClass;

}