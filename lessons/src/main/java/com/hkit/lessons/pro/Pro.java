package com.hkit.lessons.pro;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import com.hkit.lessons.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DynamicInsert
public class Pro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long proNum;

	@Column(nullable = false)
	private String introduce;


	private LocalDateTime createDate;
	
	@OneToOne(mappedBy = "proNum")
    private Member member;

    @Column(name = "member_name", insertable = false, updatable = false)
    private String memberName;


}