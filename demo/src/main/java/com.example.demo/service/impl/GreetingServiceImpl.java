package com.example.demo.service.impl;

import com.example.demo.service.GreetingService;
import org.springframework.stereotype.Service;


@Service
public class GreetingServiceImpl implements GreetingService {

    @Override
    public String getGreeting() {
        return "Hello World";
    }

    @Override
    public String getContent() { return "Experimenting with data types, operators, and basic functions." ;}

}
