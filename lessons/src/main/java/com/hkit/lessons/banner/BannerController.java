package com.hkit.lessons.banner;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/banner")
public class BannerController {
    @Autowired
    private BannerService bannerService;

    @GetMapping
    public String bannerManagementPage(Model model) {
        model.addAttribute("banners", bannerService.getAllBanners());
        return "redirect:/admins";
    }

    @PostMapping
    public String uploadBanner(@RequestParam("file") MultipartFile file,
                               @RequestParam("link") String link,
                               @RequestParam("num") int num) throws IOException {
        bannerService.saveBanner(file, link, num);
        return "redirect:/admins";
    }
    
}