package com.hkit.lessons.category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByCategoryNum(Long categoryNum);
	Optional<Category> findBySecClass(String secClass);
	
	
	List<Category> findByFirClass(String firClass);

	List<Category> findAll();

	List<Category> findByFirClassContainingOrSecClassContaining(String firClass, String secClass);


	    List<Category> findByFirClassContaining(String firClass);
	    List<Category> findBySecClassContaining(String secClass);
}