package com.example.demo.repository;

import com.example.demo.model.UserRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRedisRepository extends CrudRepository<UserRedis, String> {
    List<UserRedis> findAllByEmail(String email);
}
