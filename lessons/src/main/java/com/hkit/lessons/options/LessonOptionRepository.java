package com.hkit.lessons.options;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.hkit.lessons.lesson.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LessonOptionRepository extends JpaRepository<LessonOption, Long> {
    Optional<LessonOption> findByOptionNum(Long optionNum);
    
    // 추가된 메서드
    List<LessonOption> findByLessonNum(Lesson lessonNum);
    
    Optional<LessonOption> findByLessonNumAndLoption(Lesson lessonNum, char loption);
    
    /* LessonOption findByLessonNumAndLoption1(Lesson lessonNum, char loption); */
    
    Page<LessonOption> findByLessonNumAndLoption(Lesson lessonNum, char loption, Pageable pageable);
}