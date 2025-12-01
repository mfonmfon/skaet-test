package com.skaet.ussdapp.usermodule.domain.repository;
import com.skaet.ussdapp.usermodule.domain.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

}
