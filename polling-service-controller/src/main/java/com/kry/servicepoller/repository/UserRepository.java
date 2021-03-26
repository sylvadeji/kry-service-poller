package com.kry.servicepoller.repository;

import com.kry.servicepoller.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
