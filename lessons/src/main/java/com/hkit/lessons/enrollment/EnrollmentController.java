package com.hkit.lessons.enrollment;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class EnrollmentController {
	
	private final EnrollmentService enrollmentService;
	private final LessonService lessonService;
	private final MemberService memberService;
	
@GetMapping("/create")
public String create(){
return "redirect:/order/create";
}

@PostMapping("/create")
public String enroll(@RequestParam("lessonNum") String lessonNum,
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

        // 중복 등록 확인 (같은 수업, 같은 회원, 같은 옵션)
        if (enrollmentService.isAlreadyEnrolled(lesson, member, opt)) {
            redirectAttributes.addFlashAttribute("error", "이미 동일한 옵션으로 등록된 수업입니다.");
            return getRedirectUrl();
        }

        enrollmentService.create(lesson, member, opt);
        redirectAttributes.addFlashAttribute("message", "수업에 등록되었습니다.");

    } catch (NumberFormatException e) {
        redirectAttributes.addFlashAttribute("error", "잘못된 수업 번호 또는 회원 번호입니다.");
        return getRedirectUrl();
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "수업 등록 중 오류가 발생했습니다: " + e.getMessage());
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


@PreAuthorize("isAuthenticated()")
@PostMapping("/pay/{id}")
public String pay(Model model, @PathVariable("id") int id, RedirectAttributes redirectAttributes) {
    Enrollment enrollment = enrollmentService.getEnrollment(id);
    
    // 이미 결제 완료 상태인지 확인
    if ("결제완료".equals(enrollment.getPaymentState())) {
        redirectAttributes.addFlashAttribute("message", "이미 결제된 강의입니다.");
        return getRedirectUrl();
    }
    
    String price = enrollmentService.getEnrollmentPrice(enrollment);
    model.addAttribute("enrollment", enrollment);
    model.addAttribute("price", price);
    return "payment";
}

@PreAuthorize("isAuthenticated()")
@GetMapping("/success")
public String paymentSuccess(@RequestParam("orderId") String orderId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
    // orderId에서 enrollment ID 추출 (예: "abcXXXabc" 형식에서 XXX 추출)
    String enrollmentId = orderId.substring(3, orderId.length() - 3);

    // Enrollment 업데이트
    int id = Integer.parseInt(enrollmentId);
    Enrollment enrollment = enrollmentService.getEnrollment(id);
    
    // 이미 결제 완료 상태인지 다시 한 번 확인
    if ("결제완료".equals(enrollment.getPaymentState())) {
        redirectAttributes.addFlashAttribute("message", "이미 결제된 강의입니다.");
        return getRedirectUrl();
    }
    
    enrollment.setPaymentState("결제완료");
    enrollmentService.saveEnrollment(enrollment); // 변경사항 저장

    String referer = request.getHeader("Referer");
    if (referer == null || !referer.contains(request.getServerName())) {
        referer = "/"; // 기본 페이지 URL
    }
    model.addAttribute("previousUrl", referer);

    return getRedirectUrl1();
}


private String getRedirectUrl1() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        return "redirect:/admins";
    }
    return "success";
}

@PreAuthorize("isAuthenticated()")
@GetMapping("/fail")
public String paymentFail(HttpServletRequest request, Model model) {
    String referer = request.getHeader("Referer");
    if (referer == null || !referer.contains(request.getServerName())) {
        referer = "/";  // 기본 페이지 URL
    }
    model.addAttribute("previousUrl", referer);

    return "fail";
}

}
