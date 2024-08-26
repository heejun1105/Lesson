package com.hkit.lessons;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hkit.lessons.banner.BannerService;
import com.hkit.lessons.cart.Cart;
import com.hkit.lessons.cart.CartService;
import com.hkit.lessons.category.Category;
import com.hkit.lessons.category.CategoryRepository;
import com.hkit.lessons.category.CategoryService;
import com.hkit.lessons.enrollment.Enrollment;
import com.hkit.lessons.enrollment.EnrollmentService;
import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.lesson.LessonService;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberService;
import com.hkit.lessons.options.LessonOption;
import com.hkit.lessons.options.LessonOptionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminsController {
	
	private final MemberService memberService;
	private final LessonService lessonService;
	private final EnrollmentService enrollmentService;
	private final CategoryRepository categoryRepository;
	private final CartService cartService;
	private final LessonOptionService lessonOptionService;
	private final BannerService bannerService;
	private final CategoryService categoryService;
	@GetMapping("")
	public String admin(Model model, @RequestParam(value="page",defaultValue="0") int page, Principal pcp) {
		Member member = this.memberService.getMember(pcp.getName());
		model.addAttribute("member", member);
		Page<Lesson> Lpaging = this.lessonService.getList(page);
	    model.addAttribute("lessons", Lpaging);
	    Page<Member> paging = this.memberService.getList(page);
        model.addAttribute("members", paging);
        List<Category> category = this.categoryRepository.findAll();
        model.addAttribute("category",category);
        List<Cart> cart = cartService.getCart();
		model.addAttribute("cart", cart);
		Page<Enrollment> Epaging = this.enrollmentService.getList(page);
	    model.addAttribute("Enrollment", Epaging);
	    Page<LessonOption> opaging = this.lessonOptionService.getList(page);
	    model.addAttribute("lessonOption",opaging);
	    
	    model.addAttribute("banners", bannerService.getAllBanners());
	    
	    List<Category> categories = categoryService.getAllCategories();
        
        Map<String, List<Category>> categoriesByFirClass = categories.stream()
            .collect(Collectors.groupingBy(Category::getFirClass));
        
        model.addAttribute("categoriesByFirClass", categoriesByFirClass);
        
		return "admin_form";
	}

}
