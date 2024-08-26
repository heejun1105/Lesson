package com.hkit.lessons.pro;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hkit.lessons.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProService {

	private final ProRepository proRepository;

	public Page<Pro> getList(int page){
		Pageable pageable = PageRequest.of(page, 10);
		return this.proRepository.findAll(pageable);
	}

	public Pro create(String introduce) {
		Pro pro = new Pro();

		pro.setIntroduce(introduce);
		pro.setCreateDate(LocalDateTime.now());

		return this.proRepository.save(pro);
	}

	public Pro getPro(Long proNum) {
		Optional<Pro> Pro = this.proRepository.findById(proNum);
		if (Pro.isPresent()){
			return Pro.get();
		}else {
			throw new DataNotFoundException("Pro not found");
		}

	}

	public void modify(Pro pro, String introduce) {
		pro.setIntroduce(introduce);


		this.proRepository.save(pro);
	}

	public void delete(Pro pro) {
		this.proRepository.delete(pro);
	}
}