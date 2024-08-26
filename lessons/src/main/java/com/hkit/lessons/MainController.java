package com.hkit.lessons;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hkit.lessons.banner.Banner;
import com.hkit.lessons.banner.BannerService;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.member.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
public class MainController {
    
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    private final MemberService memberService;
    private final BannerService bannerService;

    @GetMapping("/")
    public String getHomePage(Model model, Principal principal) {
        try {
            Banner banner4 = bannerService.getBannerByNum(4);
            Banner banner5 = bannerService.getBannerByNum(5);
            
            if (banner4 != null) {
                model.addAttribute("banner4", banner4);
                logger.info("Banner 4 loaded: {}", banner4);
            } else {
                logger.warn("Banner 4 not found");
            }
            
            if (banner5 != null) {
                model.addAttribute("banner5", banner5);
                logger.info("Banner 5 loaded: {}", banner5);
            } else {
                logger.warn("Banner 5 not found");
            }

            if (principal != null) {
                Member member = memberService.getMember(principal.getName());  
                String profileImage = member.getProfileImage();
                model.addAttribute("imageName", profileImage);
                logger.info("User logged in: {}, Profile Image: {}", principal.getName(), profileImage);
            } else {
                model.addAttribute("imageName", "profile0.jpg");
                logger.info("Anonymous user, using default profile image");
            }

            model.addAttribute("banners", bannerService.getAllBanners());
            logger.info("All banners loaded successfully");

        } catch (DataAccessException e) {
            logger.error("Error accessing data: ", e);
            // 에러 페이지로 리다이렉트하거나 에러 메시지를 모델에 추가할 수 있습니다.
            model.addAttribute("error", "데이터를 불러오는 중 오류가 발생했습니다.");
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            model.addAttribute("error", "예기치 않은 오류가 발생했습니다.");
        }

        return "index";
    }
}