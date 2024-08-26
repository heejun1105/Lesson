package com.hkit.lessons.lesson;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hkit.lessons.category.Category;
import com.hkit.lessons.category.CategoryService;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberService;
import com.hkit.lessons.options.LessonOption;
import com.hkit.lessons.options.LessonOptionService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonController {

    private static final Logger logger = LoggerFactory.getLogger(LessonController.class);

    private final LessonService lessonService;
    private final MemberService memberService;
    private final LessonOptionService lessonOptionService;
    private final CategoryService categoryService;
    
    
    @GetMapping("/create")
    public String showCreateForm(LessonCreateForm lessonCreateForm, Model model, Principal principal) {
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("member", member);
        return "lesson_form";
    }

    //밑에 것으로 대체
    @PostMapping("/create")
    public String createLesson(LessonCreateForm lessonCreateForm) {
        lessonService.create(lessonCreateForm.getLname(),
                lessonCreateForm.getIntroduce(), lessonCreateForm.getProNum(),
                lessonCreateForm.getCategoryNum(), lessonCreateForm.getLesson_image());

        return "redirect:/";
    }

    @PostMapping("/create-with-options")
    public ResponseEntity<?> createLessonWithOptions(@RequestParam("file") MultipartFile file,
                                                     @RequestParam Map<String, String> allParams) {
        try {
            logger.info("레슨 생성 시작. 파라미터: {}", allParams);

            // 카테고리 번호 확인
            String categoryNumStr = allParams.get("categoryNum");
            if (categoryNumStr == null || categoryNumStr.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "카테고리 번호가 제공되지 않았습니다."
                ));
            }

            // 카테고리 유효성 검사
            try {
                Long categoryNumId = Long.parseLong(categoryNumStr);
                Category category = categoryService.getCategory(categoryNumId);
                if (category == null) {
                    return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "유효하지 않은 카테고리 번호입니다."
                    ));
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "잘못된 카테고리 번호 형식입니다."
                ));
            }

            // 레슨 생성 (기존 메서드 사용)
            Lesson lesson = lessonService.createFromParams(allParams);
            logger.info("레슨 생성 완료. 레슨 ID: {}, 카테고리: {}", lesson.getLessonNum(), lesson.getCategoryNum().getCategoryNum());

            // 이미지 파일 처리
            if (file != null && !file.isEmpty()) {
                lessonService.saveLessonImage(lesson.getLessonNum(), file);
                logger.info("이미지 업로드 완료. 레슨 ID: {}", lesson.getLessonNum());
            }

            // 모든 옵션 처리
            char[] optionTypes = {'b', 's', 'p'};
            for (char optionType : optionTypes) {
                if (allParams.containsKey("needs_" + optionType)) {
                    LessonOption option = lessonOptionService.createFromParams(allParams, lesson, optionType);
                    logger.info("옵션 생성 완료. 옵션 ID: {}, 타입: {}", option.getOptionNum(), optionType);
                }
            }

            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "레슨과 옵션이 성공적으로 생성되었습니다.",
                "lessonNum", lesson.getLessonNum()
            ));
        } catch (Exception e) {
            logger.error("레슨 생성 중 오류 발생", e);
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "레슨 생성 중 오류가 발생했습니다: " + e.getMessage(),
                "exceptionType", e.getClass().getSimpleName()
            ));
        }
    }
    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

	
	@GetMapping("/delete/{lessonNum}")
	public String lessonDelete(Principal pcp, @PathVariable("lessonNum") Long lessonNum) {
		Lesson lesson = lessonService.GetLesson(lessonNum);
		lessonService.delete(lesson);
		return getRedirectUrl();
	}
	
	private String getRedirectUrl() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
	        return "redirect:/admins";
	    }
	    return "redirect:/";
	}
	
	 @GetMapping("/modify/{lessonNum}")
	    public String lessonModify(@PathVariable("lessonNum") Long lessonNum, Model model) {
	    	Lesson lesson = lessonService.GetLesson(lessonNum);
	    	model.addAttribute("lesson", lesson);
	    	return "lesson_info";
	    }

	 //수정시 갈곳지정용
	 private String getRedirectUrl2() {
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    if (auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
		        return "redirect:/admins";
		    }
		    return "redirect:/";
		}
	 
	  @PostMapping("/modify/{lessonNum}")
	   public String lessonmodify(@PathVariable("lessonNum") Long lessonNum, @ModelAttribute Lesson lesson) {
	    	
	    Lesson modifyLesson = lessonService.GetLesson(lessonNum);
	    lessonService.modify(modifyLesson,
	    	lesson.getLname(),
	    	lesson.getIntroduce(),
			lesson.getCategoryNum(),
			lesson.getLessonImage()
	    	);
	    return getRedirectUrl2();
	    }
	    
	   @GetMapping("/lessonList")
	   public String lessonList(Principal pcp, Model model, @RequestParam(value="page",defaultValue="0") int page) {
		   Page<Lesson> Lpaging = this.lessonService.getList(page);
		   model.addAttribute("lessons", Lpaging);
		   return "lessonList";
	 }
	   
	   @Getter
	   @Setter
	   @AllArgsConstructor
	   private static class LessonWithMember {
	       private Lesson lesson;
	       private String memberName;
	    }
	   
	 //페이징 처리 미완료
	   @GetMapping("/List")
	   public String list(Model model, 
	                      @RequestParam(value = "page", defaultValue = "0") int page,
	                      @RequestParam(value = "kw", defaultValue = "") String kw,
	                      @RequestParam(value = "searchType", defaultValue = "all") String searchType) {
	       
	       Page<Lesson> paging = this.lessonService.getList(page, kw, searchType);
	       List<Lesson> lessons = paging.getContent();

	       List<Map<String, Object>> lessonDataList = new ArrayList<>();
	       for (Lesson lesson : lessons) {
	           Map<String, Object> lessonData = new HashMap<>();
	           lessonData.put("lesson", lesson);

	       Page<LessonOption> opPaging = this.lessonOptionService.GetOption(lesson, 'b', page);
	       if (!opPaging.getContent().isEmpty()) {
	           lessonData.put("lessonOption", opPaging.getContent().get(0));
	       }
	       String memberName = memberService.getMemberNameByProNum(lesson.getProNum().getProNum());
           lessonData.put("memberName", memberName);
           
           lessonDataList.add(lessonData);
	       }

	       Map<String, Object> categoryMap = this.categoryService.getCategories3(kw);

	       model.addAttribute("paging", new PageImpl<>(lessonDataList, paging.getPageable(), paging.getTotalElements()));
	       model.addAttribute("kw", kw);
	       model.addAttribute("searchType", searchType);
	       model.addAttribute("categoryMap", categoryMap);
	       return "lesson_cate_List";
	   }
	   
	 //레슨 상세페이지
	   @GetMapping("/detail/{lessonNum}")
	   public String getLesson(Model model, @PathVariable("lessonNum") Long lessonNum, Principal pcp) {
		   boolean isLoggedIn = (pcp != null);
	       model.addAttribute("isLoggedIn", isLoggedIn);

	       if (isLoggedIn) {
	           String member1 = pcp.getName();
	           Member member = this.memberService.getMember(member1);
	           model.addAttribute("member", member);
	       }
	       Lesson lesson = this.lessonService.GetLesson(lessonNum);
	       
	       Optional<LessonOption> lessonOption_b = this.lessonOptionService.findByLessonAndOption(lesson, 'b');
	       Optional<LessonOption> lessonOption_s = this.lessonOptionService.findByLessonAndOption(lesson, 's');
	       Optional<LessonOption> lessonOption_p = this.lessonOptionService.findByLessonAndOption(lesson, 'p');
	       
	       model.addAttribute("lesson", lesson);
	       model.addAttribute("lessonOption_b", lessonOption_b.orElse(null));
	       model.addAttribute("lessonOption_s", lessonOption_s.orElse(null));
	       model.addAttribute("lessonOption_p", lessonOption_p.orElse(null));
	       
//	       boolean alreadyPurchased = this.lessonService.isLessonAlreadyPurchased(member, lessonNum);
//           model.addAttribute("alreadyPurchased", alreadyPurchased);
	       
	       return "lesson_detail";
	   }
}
	

