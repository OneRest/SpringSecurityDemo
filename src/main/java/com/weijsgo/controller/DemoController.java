package com.weijsgo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/security")
    public String demo (){

        return "OK security_custom";
    }

}
