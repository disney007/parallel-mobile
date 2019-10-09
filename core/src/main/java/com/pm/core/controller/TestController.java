package com.pm.core.controller;

import com.pm.core.service.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    UtilService utilService;

    @GetMapping("/hello")
    public long sayHello() {
        return utilService.getNextDeviceId();
    }
}
