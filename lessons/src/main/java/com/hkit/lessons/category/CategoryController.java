package com.hkit.lessons.category;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hkit.lessons.lesson.Lesson;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	//사용안함
	@GetMapping("/create")
	public String createForm(CategoryCreateForm categoryCreateForm) {

	    return "cateCreForm";
	}

	@PostMapping("/create")
	public String create(CategoryCreateForm categoryCreateForm) {

	    categoryService.create(categoryCreateForm.getFirClass(),
	    categoryCreateForm.getSecClass());

	    return "redirect:/admins";
	}
	
	@GetMapping("/delete/{categoryNum}")
	public String lessonDelete(Principal pcp, @PathVariable("categoryNum") Long categoryNum) {
		Category category = categoryService.getCategory(categoryNum);
		categoryService.delete(category);
		return "redirect:/admins";
	}
	
	//사용안함
	 @GetMapping("/modify/{categoryNum}")
	    public String lessonModify(@PathVariable("categoryNum") Long categoryNum, Model model) {
		 Category category = categoryService.getCategory(categoryNum);
	    	model.addAttribute("category", category);
	    	return "category_info";
	   }

	
	   @PostMapping("/modify/{categoryNum}")
	   public String lessonmodify(@PathVariable("categoryNum") Long categoryNum, @ModelAttribute Category category) {
	    	
	    Category modifycategory = categoryService.getCategory(categoryNum);
	    categoryService.modify(modifycategory,
	    		category.getFirClass(),
	    		category.getSecClass()
	  	);
	    	return "redirect:/admins";
	
	    }
	

	}
