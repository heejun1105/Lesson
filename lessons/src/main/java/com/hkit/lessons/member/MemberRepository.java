package com.hkit.lessons.member;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>{
	Optional<Member> findByMemberId(String memberId);
	Optional<Member> findByMemberNum(Long memberNum);
	Page<Member> findAll(Pageable pageable);
	boolean existsByMemberId(String memberId);
	
	@Query("SELECT m.memberName FROM Member m WHERE m.proNum.proNum = :proNum")
	String findMemberNameByProNum(@Param("proNum") Long proNum);
}
