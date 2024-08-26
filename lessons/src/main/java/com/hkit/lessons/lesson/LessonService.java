package com.hkit.lessons.lesson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hkit.lessons.DataNotFoundException;
import com.hkit.lessons.category.Category;
import com.hkit.lessons.category.CategoryService;
import com.hkit.lessons.enrollment.EnrollmentRepository;
import com.hkit.lessons.member.Member;
import com.hkit.lessons.pro.Pro;
import com.hkit.lessons.pro.ProService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonService {
	
@Autowired
private final LessonRepository lessonRepository;
private final ProService proService;
private final CategoryService categoryService;
private final EnrollmentRepository enrollmentRepository;
private static final Logger logger = LoggerFactory.getLogger(LessonService.class);
	
	

		private Specification<Lesson> search(String kw, String searchType) {
		    return (root, query, cb) -> {
		        query.distinct(true);
		        Join<Lesson, Category> category = root.join("categoryNum", JoinType.LEFT);
		        Join<Lesson, Pro> pro = root.join("proNum", JoinType.LEFT);
		        Join<Pro, Member> member = pro.join("member", JoinType.LEFT);
		
		        switch (searchType) {
		            case "title":
		                return cb.like(root.get("lname"), "%" + kw + "%");
		            case "instructor":
		            	return cb.equal(member.get("memberName"), kw);
		            case "category":
		                return cb.or(
		                		cb.equal(category.get("firClass"), kw),
		                        cb.equal(category.get("secClass"), kw)
		                );
		            default: // "all"
		                return cb.or(
		                		cb.like(root.get("lname"), "%" + kw + "%"),
		                        cb.equal(member.get("memberName"), kw),
		                        cb.equal(category.get("firClass"), kw),
		                        cb.equal(category.get("secClass"), kw)
		                );
		        }
		    };
		}
		
	 @Value("${file.upload-dir}")
	 private String externalUploadDir;
	
	 public void saveLessonImage(Long lessonNum, MultipartFile file) throws IOException {
	        Lesson lesson = lessonRepository.findById(lessonNum)
	            .orElseThrow(() -> new RuntimeException("Lesson not found"));

	        String fileName = "LESSON" + lessonNum + getFileExtension(file.getOriginalFilename());

	        // 1. 리소스 폴더에 저장
	        String resourceUploadDir = new ClassPathResource("static/lessonimg/").getFile().getAbsolutePath();
	        Path resourcePath = Paths.get(resourceUploadDir + File.separator + fileName);
	        Files.write(resourcePath, file.getBytes());

	        // 2. 외부 저장소에 추가로 저장
	        Path externalUploadPath = Paths.get(externalUploadDir);
	        if (!Files.exists(externalUploadPath)) {
	            Files.createDirectories(externalUploadPath);
	        }
	        Path externalFilePath = externalUploadPath.resolve(fileName);
	        Files.copy(file.getInputStream(), externalFilePath, StandardCopyOption.REPLACE_EXISTING);

	        lesson.setLessonImage(fileName);
	        lessonRepository.save(lesson);
	    }

	    private String getFileExtension(String fileName) {
	        return fileName.substring(fileName.lastIndexOf("."));
	    }


	 public Lesson createFromParams(Map<String, String> allParams) {
	    Lesson lesson = new Lesson();

	    // 안전하게 파라미터 추출
	    String lname = allParams.get("lname");
	    String introduce = allParams.get("introduce");
	    String lessonImage = allParams.get("lessonImage");
	    String proNumStr = allParams.get("proNum");
	    String categoryNumStr = allParams.get("categoryNum");

	    // null 체크 및 기본값 설정
	    lesson.setLname(lname != null ? lname : "");
	    lesson.setIntroduce(introduce != null ? introduce : "");
	    lesson.setLessonImage(lessonImage != null ? lessonImage : "");

	    // proNum 처리
	    if (proNumStr != null && !proNumStr.isEmpty()) {
	        try {
	            Long proNumId = Long.parseLong(proNumStr);
	            Pro proNum = proService.getPro(proNumId);
	            lesson.setProNum(proNum);
	        } catch (NumberFormatException e) {
	            logger.error("Invalid proNum: " + proNumStr, e);
	        }
	    }

	    // categoryNum 처리
	    if (categoryNumStr != null && !categoryNumStr.isEmpty()) {
	        try {
	            Long categoryNumId = Long.parseLong(categoryNumStr);
	            Category categoryNum = categoryService.getCategory(categoryNumId);
	            lesson.setCategoryNum(categoryNum);
	        } catch (NumberFormatException e) {
	            logger.warn("Invalid categoryNum: " + categoryNumStr + ". Setting to null.", e);
	            lesson.setCategoryNum(null);
	        }
	    } else {
	        logger.info("CategoryNum is null or empty. Setting to null.");
	        lesson.setCategoryNum(null);
	    }

	    lesson.setCreateDate(LocalDateTime.now());
	    lesson.setModifyDate(LocalDateTime.now());

	    return lessonRepository.save(lesson);
	}
	
	public Lesson create(String Lname, String Introduce, Pro proNum, Category categoryNum, String lesson_image) {
		
		Lesson lesson = new Lesson();
		
		lesson.setProNum(proNum);
		lesson.setCategoryNum(categoryNum);
		
		lesson.setLname(Lname);
		lesson.setIntroduce(Introduce);
		lesson.setCreateDate(LocalDateTime.now());
		lesson.setModifyDate(LocalDateTime.now());
		lesson.setLessonImage(lesson_image);
		
		return lessonRepository.save(lesson);
	}
	
	public void delete(Lesson lesson) {
		this.lessonRepository.delete(lesson);
	}
	// 레슨 삭제
	
	public void modify(Lesson lesson, String Lname, String Introduce, Category category, String lesson_image) {
		lesson.setLname(Lname);
		lesson.setIntroduce(Introduce);
		lesson.setCategoryNum(category);
		lesson.setModifyDate(LocalDateTime.now());
		lesson.setLessonImage(lesson_image);
		
		
		this.lessonRepository.save(lesson);
		
	}
	// 레슨 정보 수정
	
	public Lesson GetLesson(Long lessonNum) {
		Optional<Lesson> Lesson = this.lessonRepository.findByLessonNum(lessonNum);
		if (Lesson.isPresent()) {
			return Lesson.get();
		}else {
			throw new DataNotFoundException("Lesson not found");
		}
	}
	//레슨 제목으로 찾기
		public Lesson GetLesson(String lname) {
			Optional<Lesson> Lesson = this.lessonRepository.findByLname(lname);
			if (Lesson.isPresent()) {
				return Lesson.get();
			}else {
				throw new DataNotFoundException("Lesson not found");
			}
		}
		
	public Page<Lesson> getList(int page, String kw, String searchType) {
        Pageable pageable = PageRequest.of(page, 10);
        Specification<Lesson> spec = search(kw, searchType);
        return lessonRepository.findAll(spec, pageable);
    }
	
	public Page<Lesson> getList(int page) {
		Pageable pageable = PageRequest.of(page, 5);
		return this.lessonRepository.findAll(pageable);
	}
	
//	public Page<Lesson> getList(int page, String kw) {
//		Pageable pageable = PageRequest.of(page, 5);
//	
//		Specification<Lesson> specification = search(kw);
//		return this.lessonRepository.findAll(specification,pageable);
//	}
//	
	
	 public Lesson updateLesson(Lesson lesson) {
	     if (lesson == null || lesson.getLessonNum() == null) {
	     throw new IllegalArgumentException("유효하지 않은 레슨 정보입니다.");
	      }

	     // 기존 레슨 정보를 데이터베이스에서 조회
	     Lesson existingLesson = lessonRepository.findById(lesson.getLessonNum())
	     .orElseThrow(() -> new EntityNotFoundException("해당 레슨을 찾을 수 없습니다. ID: " + lesson.getLessonNum()));
	     
	     existingLesson.setLname(lesson.getLname());
	     existingLesson.setIntroduce(lesson.getIntroduce());
	     existingLesson.setLessonImage(lesson.getLessonImage());
	     // 필요한 다른 필드들도 여기에 추가...

	     return lessonRepository.save(existingLesson);
	    }
	 
	 public boolean isLessonAlreadyPurchased(Member member, Long lessonNum) {
	        Lesson lesson = lessonRepository.findById(lessonNum)
	                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson number"));
	        return this.enrollmentRepository.existsByMemberAndLesson(member, lesson);
	    }
	
	
}
		
	
	

	
	
