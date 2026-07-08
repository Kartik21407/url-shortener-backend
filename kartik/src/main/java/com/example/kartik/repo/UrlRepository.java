package com.example.kartik.repo;
import com.example.kartik.model.UrlMapping;
import com.example.kartik.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortAlias(String shortAlias);
    boolean existsByShortAlias(String shortAlias);
    List<UrlMapping> findByUser(User user);
}