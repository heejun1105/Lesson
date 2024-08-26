package com.hkit.lessons.member;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.hkit.lessons.cart.Cart;
import com.hkit.lessons.enrollment.Enrollment;
import com.hkit.lessons.pro.Pro;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberNum;
	
	@Column(unique = true)
	private String memberId;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private LocalDate birth;
	
	@Column(nullable = false)
	private String phone;
	
	@Column(nullable = false)
	private String memberName;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private String address;
	
	@OneToOne
	@JoinColumn(name = "proNum")
	@OnDelete(action = OnDeleteAction.SET_NULL)
	private Pro proNum;

	@ColumnDefault("'profile0.jpg'")
	private String profileImage;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Cart> carts = new ArrayList<>();
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Enrollment> enrollments = new ArrayList<>();
	
}

