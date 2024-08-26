package com.hkit.lessons.banner;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByOrderByNumAsc();

    Optional<Banner> findByNum(int num);
}