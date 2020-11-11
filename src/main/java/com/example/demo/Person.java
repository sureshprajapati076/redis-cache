package com.example.demo;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RedisHash("Student")
public class Person implements Serializable {
    @Id
    private String email;
    private Set<String> jwts=new HashSet<>();

    public Person() {
    }

    public Person(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getJwts() {
        return jwts;
    }

    public void setJwts(Set<String> jwts) {
        this.jwts = jwts;
    }
}
