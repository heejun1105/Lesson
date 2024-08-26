package com.hkit.lessons.member;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hkit.lessons.enrollment.Enrollment;
import com.hkit.lessons.enrollment.EnrollmentService;
import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.options.LessonOption;
import com.hkit.lessons.options.LessonOptionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
	
	private final MemberRepository memberRepsitory;
	
	private final MemberService memberService;
	private final EnrollmentService enrollmentService;
	private final LessonOptionService lessonOptionService;
	
	@GetMapping("/layout")
    public String layout() {
    	return "layout";
    }
	
	@GetMapping("/admins")
	public String admin() {
		return "admin_form";
	}
	
	@GetMapping("/login")
    public String login() {
    	return "login_form";
    }
	
	@GetMapping("/signup")
    public String signup(MemberCreateForm memberCreateForm) {
        return "signup_form";
    }
	
	//id check
    @GetMapping("/checkId")
    public ResponseEntity<String> checkId(@RequestParam("memberId") String memberId) {
        boolean isAvailable = memberService.isMemberIdAvailable(memberId);
        if (isAvailable) {
            return ResponseEntity.ok("available");
        } else {
            return ResponseEntity.ok("unavailable");
        }
    }
    
	@PostMapping("/upload")
	public String uploadProfileImage(@RequestParam("memberNum") Long memberNum,
	                                 @RequestParam("file") MultipartFile file,
	                                 RedirectAttributes redirectAttributes) {
	    try {
	        memberService.saveProfileImage(memberNum, file);
	        redirectAttributes.addFlashAttribute("message", "프로필 이미지가 성공적으로 업로드되었습니다.");
	        return "redirect:/member/mypage";
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "업로드 중 오류가 발생했습니다: " + e.getMessage());
	        return "redirect:/member/mypage";
	    }
	}
	 

    @PostMapping("/signup")
    public String signup(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
    	
        if (bindingResult.hasErrors()) {
        	
        	return "signup_form";
        }

        if (!memberCreateForm.getPassword1().equals(memberCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
        
        try {
        	memberService.create(memberCreateForm.getMemberId(), memberCreateForm.getEmail(),
        			memberCreateForm.getPassword1(), memberCreateForm.getPhone(),
        			memberCreateForm.getMemberName(),  memberCreateForm.getAddress(),
        			memberCreateForm.getBirth(),memberCreateForm.getGender(),"profile0.jpg");
        			
        			
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
       
        return "redirect:/";
    }
    
    
    
    private static class EnrollmentWithOptionDTO {
        private Enrollment enrollment;
        private LessonOption lessonOption;

        public EnrollmentWithOptionDTO(Enrollment enrollment, LessonOption lessonOption) {
            this.enrollment = enrollment;
            this.lessonOption = lessonOption;
        }

        // getters and setters
        public Enrollment getEnrollment() { return enrollment; }
        public void setEnrollment(Enrollment enrollment) { this.enrollment = enrollment; }
        public LessonOption getLessonOption() { return lessonOption; }
        public void setLessonOption(LessonOption lessonOption) { this.lessonOption = lessonOption; }
    }
    
    @GetMapping("/mypage")
    public String myPage(Model model, Principal pcp) {

        String member1 = pcp.getName();
        Member member = memberService.getMember(member1);
        List<Enrollment> completedEnrollments = enrollmentService.getCompletedEnrollmentsByMember(member);
        List<Enrollment> pendingEnrollments = enrollmentService.getPendingEnrollmentsByMember(member);
        List<EnrollmentWithOptionDTO> enrollmentsWithOptions = new ArrayList<>();

        for (Enrollment enrollment : completedEnrollments) {
            processEnrollment(enrollment, enrollmentsWithOptions);
        }

        for (Enrollment enrollment : pendingEnrollments) {
            processEnrollment(enrollment, enrollmentsWithOptions);
        }

        model.addAttribute("member", member);
        model.addAttribute("enrollmentsWithOptions", enrollmentsWithOptions);

        return "mypage";
    }

    private void processEnrollment(Enrollment enrollment, List<EnrollmentWithOptionDTO> enrollmentsWithOptions) {
        Lesson lesson = enrollment.getLesson();
        char opt = enrollment.getOpt();
        Optional<LessonOption> matchingOption = lessonOptionService.findByLessonAndOption(lesson, opt);

        enrollmentsWithOptions.add(new EnrollmentWithOptionDTO(enrollment, matchingOption.orElse(null)));
    }
    
	@GetMapping("/delete/{memberId}")
	public String memberDelete(Principal pcp, @PathVariable("memberId") String memberId) {
		Member member = memberService.getMember(memberId);
//		if(!lesson.get().equals(pcp.getName())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한 없음!");
//		}  // !lesson 뒤에 getMemberId 추가
		memberService.delete(member);
		return getRedirectUrl();
		
	}
	
	//삭제시 이동용
	private String getRedirectUrl() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
	        return "redirect:/admins";
	    }
	    return "redirect:/";
	}
	
	
	
    @GetMapping("/modify/{memberId}")
    public String memberModify(@PathVariable("memberId") String memberId, Model model) {
    	Member member = memberService.getMember(memberId);
    	model.addAttribute("member", member);
   	return "member_info";
    }
    
    @PostMapping("/modify/{memberId}")
    public String modify(@PathVariable("memberId") String memberId,
                         @ModelAttribute Member member,
                         @RequestParam(required = false) String currentPassword,
                         @RequestParam(required = false) String newPassword,
                         @RequestParam(required = false) String confirmPassword,
                         RedirectAttributes redirectAttributes) {

        boolean isAdmin = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            isAdmin = true;
        }

        try {
            Member modifyMember = memberService.getMember(memberId);

            // 기존 정보 수정
            memberService.modify(modifyMember,
                    member.getEmail(),
                    member.getPhone(),
                    member.getMemberName(),
                    member.getAddress(),
                    member.getBirth(),
                    member.getGender(),
                    modifyMember.getProNum());

            // 비밀번호 변경 로직
            if (currentPassword != null && newPassword != null && confirmPassword != null) {
                if (!newPassword.equals(confirmPassword)) {
                    redirectAttributes.addFlashAttribute("error", "새 비밀번호가 일치하지 않습니다.");
                    return isAdmin ? "redirect:/admins" : "redirect:/member/modify/" + memberId;
                } else {
                    try {
                        memberService.changePassword(memberId, currentPassword, newPassword);
                        redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
                    } catch (BadCredentialsException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        return isAdmin ? "redirect:/admins" : "redirect:/member/modify/" + memberId;
                    }
                }
            }

            redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
            
            // 성공 시 리다이렉트
            return isAdmin ? "redirect:/admins" : "redirect:/member/mypage";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            
            // 오류 발생 시 리다이렉트
            return isAdmin ? "redirect:/admins" : "redirect:/member/modify/" + memberId;
        }
    }
}
		
    

