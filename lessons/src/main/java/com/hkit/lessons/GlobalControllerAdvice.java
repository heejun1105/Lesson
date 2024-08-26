package com.hkit.lessons;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hkit.lessons.category.Category;
import com.hkit.lessons.category.CategoryService;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberService;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private MemberService memberService;
    
    @Autowired
    private CategoryService categoryService;  // 카테고리 서비스 추가
    
    @ModelAttribute("profileImageName")
    public String getProfileImageName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            Member member = memberService.getMemberByMemberId(username);
            if (member != null) {
                return member.getProfileImage();
            }
        }
        return "profile0.jpg";
    }
    
    @ModelAttribute("categories")
    public Map<String, List<String>> getCategories() {
        return categoryService.getAllCategoriesGrouped();
    }
    
    @ModelAttribute("allCategories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
    @ModelAttribute("isAdmin")
    public boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String memberId = auth.getName();
            Member member = memberService.getMemberByMemberId(memberId);
            if (member != null) {
                return memberService.isAdmin(member);
            }
        }
        return false;
    }
}