package com.exe.app.Repository;

import com.exe.app.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByDoc(String doc);
    boolean existsByEmail(String email);
}
