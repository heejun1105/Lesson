package com.hkit.lessons.cart;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hkit.lessons.lesson.Lesson;
import com.hkit.lessons.member.Member;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

	List<Cart> findAll();

	Cart findByListNum(int ListNum);

	void deleteByListNum(int ListNum);
	
	List<Cart> findByMember(Member member);
	
	Optional<Cart> findByListNum(Long listNum);
	

}