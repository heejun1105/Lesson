package com.hkit.lessons.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hkit.lessons.DataNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

	private final CategoryRepository categoryRepository;

	//카테고리생성
	public Category create(String firClass, String secClass) {

		Category category = new Category();

		category.setFirClass(firClass);
		category.setSecClass(secClass);

		return categoryRepository.save(category);
	}

	public Category getCategory(Long categoryNum) {
		Optional<Category> Category = this.categoryRepository.findByCategoryNum(categoryNum);
		if (Category.isPresent()) {
			return Category.get();
		}else {
			throw new DataNotFoundException("Category not found");
		}
	}

	//카테고리수정
	public void modify(Category category, String firClass, String secClass) {

		category.setFirClass(firClass);
		category.setSecClass(secClass);
	}

	//카테고리 삭제
	public void delete(Category category) {
		this.categoryRepository.delete(category);
	}
	
	 public Map<String, List<String>> getAllCategoriesGrouped() {
	        List<Category> allCategories = categoryRepository.findAll();
	        return allCategories.stream()
	            .collect(Collectors.groupingBy(
	                Category::getFirClass,
	                Collectors.mapping(Category::getSecClass, Collectors.toList())
	            ));
	    }
	 
	 public List<Category> getAllCategories() {
		 return this.categoryRepository.findAll();
	    }

	 public List<Category> getCategories(String keyword) {
	        return this.categoryRepository.findByFirClassContainingOrSecClassContaining(keyword, keyword);
	    }

	 public List<Category> getCategories1(String keyword) {
	        List<Category> byFirClass = this.categoryRepository.findByFirClassContaining(keyword);
	        if (!byFirClass.isEmpty()) {
	            return byFirClass;
	        }
	        List<Category> bySecClass = this.categoryRepository.findBySecClassContaining(keyword);
	        if (!bySecClass.isEmpty()) {
	            String firClass = bySecClass.get(0).getFirClass();
	            return this.categoryRepository.findByFirClassContaining(firClass);
	        }
	        return new ArrayList<>();
	    }

	 public Map<String, List<Category>> getCategories2(String keyword) {
	        List<Category> categories = this.categoryRepository.findByFirClassContaining(keyword);
	        if (categories.isEmpty()) {
	            List<Category> secClassCategories = this.categoryRepository.findBySecClassContaining(keyword);
	            if (!secClassCategories.isEmpty()) {
	                // secClass에 해당하는 항목이 있으면, 그 항목의 firClass에 속한 모든 항목을 가져옵니다.
	                String firClass = secClassCategories.get(0).getFirClass();
	                categories = this.categoryRepository.findByFirClassContaining(firClass);
	            }
	        }
	        if (categories.isEmpty()) {
	            // 키워드에 해당하는 카테고리가 없으면 모든 카테고리를 가져옵니다.
	            categories = this.categoryRepository.findAll();
	            return Map.of("전체 카테고리", categories.stream()
	                    .map(Category::getFirClass)
	                    .distinct()
	                    .map(firClass -> {
	                        Category c = new Category();
	                        c.setFirClass(firClass);
	                        c.setSecClass(firClass);
	                        return c;
	                    })
	                    .collect(Collectors.toList()));
	        }
	        return categories.stream().collect(Collectors.groupingBy(Category::getFirClass));
	    }

	 public Map<String, Object> getCategories3(String keyword) {
	        List<Category> categories = this.categoryRepository.findByFirClassContaining(keyword);
	        if (categories.isEmpty()) {
	            List<Category> secClassCategories = this.categoryRepository.findBySecClassContaining(keyword);
	            if (!secClassCategories.isEmpty()) {
	                String firClass = secClassCategories.get(0).getFirClass();
	                categories = this.categoryRepository.findByFirClassContaining(firClass);
	            }
	        }

	        Map<String, Object> result = new HashMap<>();
	        if (categories.isEmpty()) {
	            result.put("전체 카테고리", getAllFirClasses());
	        } else {
	            result.put(categories.get(0).getFirClass(), categories);
	            result.put("otherFirClasses", getOtherFirClasses(categories.get(0).getFirClass()));
	        }

	        return result;
	    }

	    private List<String> getAllFirClasses() {
	        return this.categoryRepository.findAll().stream()
	                .map(Category::getFirClass)
	                .distinct()
	                .collect(Collectors.toList());
	    }

	    private List<String> getOtherFirClasses(String currentFirClass) {
	        return this.categoryRepository.findAll().stream()
	                .map(Category::getFirClass)
	                .distinct()
	                .filter(firClass -> !firClass.equals(currentFirClass))
	                .collect(Collectors.toList());
	    }
	    
}