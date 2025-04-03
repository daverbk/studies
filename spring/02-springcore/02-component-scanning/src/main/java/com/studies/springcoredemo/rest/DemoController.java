package com.studies.springcoredemo.rest;

import com.studies.util.Coach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    private Coach coach;

    // @Autowired is optional for a class with one constructor
    @Autowired
    public DemoController(Coach coach) {
        this.coach = coach;
    }

    @GetMapping("/dailyworkout")
    public String getDailyWorkout() {
        return coach.getDailyWorkout();
    }
}
