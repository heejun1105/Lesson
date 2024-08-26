package com.hkit.lessons.options;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hkit.lessons.DataNotFoundException;
import com.hkit.lessons.lesson.Lesson;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonOptionService {

	private final LessonOptionRepository lessonOptionRepository;

	public LessonOption create(char lOption, boolean needs, boolean place, String onetime, String weekdate,
			String totaldate, Lesson lessonNum, String price, String info) {

		LessonOption lessonOption = new LessonOption();

		lessonOption.setLoption(lOption);
		lessonOption.setNeeds(needs);
		lessonOption.setPlace(place);
		lessonOption.setOnetime(onetime);
		lessonOption.setWeekdate(weekdate);
		lessonOption.setTotaldate(totaldate);
		lessonOption.setPrice(price);
		lessonOption.setLessonNum(lessonNum);
		lessonOption.setInfo(info);

		return lessonOptionRepository.save(lessonOption);
	}

	public LessonOption createFromParams(Map<String, String> params, Lesson lesson, char optionType) {
		LessonOption lessonOption = new LessonOption();

		lessonOption.setLoption(optionType);
		lessonOption.setNeeds(Boolean.parseBoolean(params.get("needs_" + optionType)));
		lessonOption.setPlace(Boolean.parseBoolean(params.get("place_" + optionType)));
		lessonOption.setOnetime(params.get("onetime_" + optionType));
		lessonOption.setWeekdate(params.get("weekdate_" + optionType));
		lessonOption.setTotaldate(params.get("totaldate_" + optionType));
		lessonOption.setPrice(params.get("price_" + optionType));
		lessonOption.setInfo(params.get("info_" + optionType));
		lessonOption.setLessonNum(lesson);

		return lessonOptionRepository.save(lessonOption);
	}

	public void delete(LessonOption option) {
		this.lessonOptionRepository.delete(option);
	}

	public void modify(LessonOption lessonOption, char lOption, boolean needs, boolean place, String onetime,
			String weekdate, String totaldate, String price, String info) {
		lessonOption.setLoption(lOption);
		lessonOption.setNeeds(needs);
		lessonOption.setPlace(place);
		lessonOption.setOnetime(onetime);
		lessonOption.setWeekdate(weekdate);
		lessonOption.setPrice(price);
		lessonOption.setTotaldate(totaldate);
		lessonOption.setInfo(info);

		this.lessonOptionRepository.save(lessonOption);

	}

	/*
	 * public LessonOption findByLessonAndOption(Lesson lesson, char opt) {
	 * LessonOption lessonOption =
	 * lessonOptionRepository.findByLessonNumAndLoption(lesson, opt);
	 * 
	 * return lessonOption; }
	 */

	
	  public Optional<LessonOption>findByLessonAndOption(Lesson lesson, char opt)
	 {
	  
	  return lessonOptionRepository.findByLessonNumAndLoption(lesson, opt); }
 

	public LessonOption GetOption(Long optionNum) {
		Optional<LessonOption> option = this.lessonOptionRepository.findByOptionNum(optionNum);
		if (option.isPresent()) {
			return option.get();
		} else {
			throw new DataNotFoundException("option not found");
		}

	}

	public Page<LessonOption> getList(int page) {
		Pageable pageable = PageRequest.of(page, 10);
		return this.lessonOptionRepository.findAll(pageable);
	}
	
	public Page<LessonOption> GetOption(Lesson lesson, char opt, int page) {
	    Pageable pageable = PageRequest.of(page, 10);
	    return this.lessonOptionRepository.findByLessonNumAndLoption(lesson, opt, pageable);
	}


}

