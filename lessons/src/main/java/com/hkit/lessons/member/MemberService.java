package com.hkit.lessons.member;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.hkit.lessons.DataNotFoundException;
import com.hkit.lessons.pro.Pro;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	
	
	@Autowired
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	
	@Value("${file.upload-dir}")
    private String externalUploadDir;
	
	public String getMemberNameByProNum(Long proNum) {
	    return memberRepository.findMemberNameByProNum(proNum);
	}
	
	public Member getMemberByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).orElse(null);
    }
	
	public Member getMember1(String memberId) {
        return memberRepository.findByMemberId(memberId)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with memberId: " + memberId));
    }
	
	public Member getMember(String memberId) {
		Optional<Member> Member = this.memberRepository.findByMemberId(memberId);
		if (Member.isPresent()) {
			return Member.get();
		}else {
			throw new DataNotFoundException("Member not found");
		}
	}
	public void saveProfileImage(Long memberNum, MultipartFile file) throws IOException {
        Member member = memberRepository.findById(memberNum)
            .orElseThrow(() -> new RuntimeException("Member not found"));

        String fileName = "profile" + memberNum + getFileExtension(file.getOriginalFilename());

        // 1. 기존 방식대로 리소스 폴더에 저장
        String resourceUploadDir = new ClassPathResource("static/uploads/").getFile().getAbsolutePath();
        Path resourcePath = Paths.get(resourceUploadDir + File.separator + fileName);
        Files.write(resourcePath, file.getBytes());

        // 2. 외부 저장소에 추가로 저장
        Path externalUploadPath = Paths.get(externalUploadDir);
        if (!Files.exists(externalUploadPath)) {
            Files.createDirectories(externalUploadPath);
        }
        Path externalFilePath = externalUploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), externalFilePath, StandardCopyOption.REPLACE_EXISTING);

        member.setProfileImage(fileName);
        memberRepository.save(member);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
	
    public boolean isMemberIdAvailable(String memberId) {
        return !memberRepository.existsByMemberId(memberId);
    }
    
	public Page<Member> getList(int page){
		Pageable pageable = PageRequest.of(page, 10);
		return this.memberRepository.findAll(pageable);
	}
	
	public Member create(String memberId, String email, String password, String phone, String memberName,
			String address, LocalDate birth,String gender,String profileImage) {
		
		Member member = new Member();
		
		member.setMemberId(memberId);
		member.setPassword(passwordEncoder.encode(password));
		member.setBirth(birth);
		member.setPhone(phone);
		member.setMemberName(memberName);
		member.setEmail(email);
		member.setGender(gender);
		member.setAddress(address);
		member.setProfileImage(profileImage);
		this.memberRepository.save(member);
		
		return member;
	}
	public boolean checkCurrentPassword(String memberId, String currentPassword) {
	    Member member = getMember(memberId);
	    return passwordEncoder.matches(currentPassword, member.getPassword());
	}
	
	@Transactional
	public void changePassword(String memberId, String currentPassword, String newPassword) throws BadCredentialsException {
	    Member member = memberRepository.findByMemberId(memberId)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

	    if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
	        throw new BadCredentialsException("현재 비밀번호가 올바르지 않습니다.");
	    }

	    member.setPassword(passwordEncoder.encode(newPassword));
	    memberRepository.save(member);
	}
	
	
	
	public Member getMemberNum(Long memberNum) {
		Optional<Member> Member = this.memberRepository.findByMemberNum(memberNum);
		if (Member.isPresent()) {
			return Member.get();
		}else {
			throw new DataNotFoundException("Member not found");
		}
	}
	
	public Member getMember(Long memberNum) {
		Optional<Member> Member = this.memberRepository.findByMemberNum(memberNum);
		if (Member.isPresent()) {
			return Member.get();
		}else {
			throw new DataNotFoundException("Member not found");
		}
	}
	
	public void modify(Member member, String email, String phone, String memberName, 
			String address, LocalDate birth, String gender, Pro pro) {

		member.setEmail(email);
		
		member.setPhone(phone);
		member.setMemberName(memberName);
		member.setAddress(address);
		member.setBirth(birth);
		member.setProNum(pro);
		member.setGender(gender);

		this.memberRepository.save(member);

	}
	// 회원정보 수정

	public void insertPronum(Member member, Pro pro) {



		member.setProNum(pro);

		this.memberRepository.save(member);

	}
	
	public void delete(Member member) {
		this.memberRepository.delete(member);
	}

	private final List<String> adminIds = Arrays.asList("admin");

    public boolean isAdmin(Member member) {
        return adminIds.contains(member.getMemberId());
    }

	

	
	
	// 회원정보 삭제

}

