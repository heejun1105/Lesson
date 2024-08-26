package com.hkit.lessons.cart;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.lesson.LessonService;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberService;

import lombok.RequiredArgsConstructor;



@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;
	private final CartRepository cartRepository;
	private final LessonService lessonService;
	private final MemberService memberService;
	
	@GetMapping("/cartList")
	public String showCart(Model model, Principal principal) {
	    // 현재 로그인한 사용자의 정보를 가져옵니다.
	    String memberId = principal.getName();
	    Member member = memberService.getMember1(memberId);
	    
	    // 현재 사용자의 장바구니 항목만 가져옵니다.
	    List<Cart> carts = cartService.getCartByMember(member);
	    model.addAttribute("carts", carts);
	    return "cartList";
	}
	
	@GetMapping("/add")
	public String addToCart(){
	return "redirect:/cart/add";
}

	
	@PostMapping("/add")
	public String addToCart(@RequestParam("lessonNum") String lessonNum, 
	                        @RequestParam("memberNum") String memberNum,
	                        @RequestParam("opt") char opt,
	                        RedirectAttributes redirectAttributes) {
	    try {
	        Long lessonId = Long.parseLong(lessonNum);
	        Long memberId = Long.parseLong(memberNum);
	        
	        Lesson lesson = lessonService.GetLesson(lessonId);
	        Member member = memberService.getMemberNum(memberId);
	        
	        if (lesson == null || member == null) {
	            redirectAttributes.addFlashAttribute("error", "수업 또는 회원을 찾을 수 없습니다.");
	            return getRedirectUrl();
	        }
	        
	        cartService.addToCart(lesson, member, opt);
	        redirectAttributes.addFlashAttribute("message", "장바구니에 추가되었습니다.");
	        
	    } catch (NumberFormatException e) {
	        redirectAttributes.addFlashAttribute("error", "잘못된 수업 번호 또는 회원 번호입니다.");
	        return getRedirectUrl();
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "장바구니 추가 중 오류가 발생했습니다: " + e.getMessage());
	        return getRedirectUrl();
	    }

	    return getRedirectUrl();
	}
	private String getRedirectUrl() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
	        return "redirect:/admins";
	    }
	    return "redirect:/";
	}
	
	
	 @GetMapping("/delete/{listNum}")
		public String lessonDelete(Principal pcp, @PathVariable("listNum") Long listNum) {
		 
			Cart cart = cartService.GetCart(listNum);
			cartService.delete(cart);
			
			return "redirect:/cart/cartList";
			
		}
	 
}