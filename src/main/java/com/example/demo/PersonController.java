package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/add")
    public Person addPerson( @RequestBody Jwt jwt){
        Person person1=personRepository.findById(jwt.getEmail()).orElse(new Person(jwt.getEmail()));
        person1.getJwts().add(jwt.getJwt());
        return personRepository.save(person1);
    }

    @GetMapping("/get/{email}")
    public Person getById(@PathVariable String email){
        return personRepository.findById(email).orElseGet(Person::new);
    }


}
