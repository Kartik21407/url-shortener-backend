package com.example.kartik.repo;

import com.example.kartik.model.UrlClick;
import com.example.kartik.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlClickRepository extends JpaRepository<UrlClick, Long> {

    long countByUrlMappingId(Long mappingId);
    void deleteByUrlMapping(UrlMapping urlMapping);
}