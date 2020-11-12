package com.example.demo.repository;

import com.example.demo.model.UserRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRedisRepository extends CrudRepository<UserRedis, String> {
}
