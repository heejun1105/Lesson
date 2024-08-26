package com.hkit.lessons.banner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BannerService {
	
    @Autowired
    private BannerRepository bannerRepository;

    
    @Value("${file.upload-dir}")
    private String uploadPath;

    public void saveBanner(MultipartFile file, String link, int num) throws IOException {
        String fileName = "banner" + num + getFileExtension(file.getOriginalFilename());
        
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Banner banner = bannerRepository.findByNum(num).orElse(new Banner());
        banner.setImageName(fileName);
        banner.setLink(link);
        banner.setNum(num);
        
        bannerRepository.save(banner);
    }


    public List<Banner> getAllBanners() {
        return bannerRepository.findAllByOrderByNumAsc();
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    
    public Banner getBannerByNum(int num) {
        return bannerRepository.findByNum(num).orElse(null);
    }
}