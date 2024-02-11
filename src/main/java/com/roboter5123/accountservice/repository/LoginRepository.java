package com.roboter5123.accountservice.repository;
import com.roboter5123.accountservice.repository.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LoginRepository extends JpaRepository<Login, UUID> {

    @Query("select l from Login l where l.account.email = :email and l.createdOn < :createdOn")
    List<Login> findByEmailAndCreatedBefore(@Param("email") String email, @Param("createdOn") LocalDateTime createdOn);

}