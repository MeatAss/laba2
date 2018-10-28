package com.example.laba2.repository;

import com.example.laba2.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<User, Long> {
}
