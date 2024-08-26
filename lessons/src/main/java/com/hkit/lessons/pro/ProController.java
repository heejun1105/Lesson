package com.hkit.lessons.pro;

import java.security.Principal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberCreateForm;
import com.hkit.lessons.member.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@Controller
@RequiredArgsConstructor
@RequestMapping("/pro")
public class ProController {

	private final ProService proService;
	private final MemberService memberService;



	@GetMapping("/procre_form")
	public String crePro(@RequestParam("memberNum") Long memberNum,ProCreateForm proCreateForm, Principal principal, Model model) {
		Member member = memberService.getMember(principal.getName());
    	model.addAttribute("member", member);
    	model.addAttribute("memberNum", memberNum);

		return "procre_form";
	}


	@PostMapping("/procre_form")
	public String crePro(@Valid ProCreateForm proCreateForm, BindingResult bindingResult, 
	                     @RequestParam("memberNum") Long memberNum) {

	    Member member = this.memberService.getMember(memberNum);

	    if(bindingResult.hasErrors()) {
	        return "procre_form";
	    }

	    try {
	        Pro pro = proService.create(proCreateForm.getIntroduce());
	        memberService.insertPronum(member, pro);
	    } catch(DataIntegrityViolationException e) {
	        e.printStackTrace();
	        bindingResult.reject("procreFailed", "이미 등록된 전문가입니다.");
	        return "procre_form";
	    } catch(Exception e) {
	        e.printStackTrace();
	        bindingResult.reject("procreFailed", e.getMessage());
	        return "procre_form";
	    }

	    return "redirect:/";
	}
	
	@GetMapping("/propage")
	public String getProPage(Model model, Principal pcp, RedirectAttributes redirectAttributes) {
	    try {
	        Member member = memberService.getMember(pcp.getName());
	        Pro pro = member.getProNum();
	        if (pro == null) {
	            redirectAttributes.addFlashAttribute("error", "프로 회원만 접근 가능합니다.");
	            return "redirect:/member/mypage";  // 일반 마이페이지로 리다이렉트
	        }
	        model.addAttribute("pro", pro);
	        model.addAttribute("member", member);
	        return "propage";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "페이지 로딩 중 오류가 발생했습니다.");
	        return "redirect:/member/mypage";
	    }
	}
}