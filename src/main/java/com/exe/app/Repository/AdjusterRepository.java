package com.exe.app.Repository;

import com.exe.app.entity.Adjuster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdjusterRepository extends JpaRepository<Adjuster, Long> {
    boolean existsByEmail(String email);
}
