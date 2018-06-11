package com.cham.hazelcastJet.controller;

import com.cham.hazelcastJet.service.HazelcastJetEmployeeService;
import domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class StreamController {

    @Autowired
    private HazelcastJetEmployeeService hazelcastJetEmployeeService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Flux<Employee> streamEmp() {
        System.out.println("Inside StreamController.streamEmp..");
        return hazelcastJetEmployeeService.getStream();
    }
}
