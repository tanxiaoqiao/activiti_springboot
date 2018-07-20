package com.example.act.controller;

import com.example.act.test.ActTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kris
 * @Description:
 * @Date: 7/20/2018 2:58 PM
 */
@RestController
public class TestController {


    @Autowired
    ActTest actTest;

    @RequestMapping("/first")
    public void first() {
        actTest.first();
    }
    @RequestMapping("/second")
    public void secount() {
        actTest.getList();
    }

    @RequestMapping("/third")
    public void third() {
        actTest.third();
    }


}
