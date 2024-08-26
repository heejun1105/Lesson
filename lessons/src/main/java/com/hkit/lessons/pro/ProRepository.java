package com.hkit.lessons.pro;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProRepository extends JpaRepository<Pro, Long> {
	Optional<Pro> findById(Long proNum);
}