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

    @RequestMapping("/four")
    public void four() {
        actTest.four();
    }


    @RequestMapping("/five")
    public void five() {
        actTest.five();
    }


    @RequestMapping("/seven")
    public void seven() {
        actTest.seven();
    }


    @RequestMapping("/eight")
    public void eight() {
        actTest.eight();
    }


    @RequestMapping("/nine")
    public void nine() {
        actTest.nine();
    }

    @RequestMapping("/ten")
    public void ten() {
        actTest.ten();
    }


    @RequestMapping("/eleven")
    public void eleven() {
        actTest.eleven();
    }


    @RequestMapping("/twelve")
    public void twelve() {
        actTest.eleven();
    }


    @RequestMapping("/othree")
    public void othree() throws InterruptedException {
        actTest.othree();
    }


    @RequestMapping("/ofour")
    public void ofour() throws InterruptedException {
        actTest.ofour();
    }

    @RequestMapping("/a")
    public void a() throws InterruptedException {
        actTest.a();
    }

    @RequestMapping("/b")
    public void b() throws InterruptedException {
        actTest.b();
    }
    @RequestMapping("/c")
    public void c() throws InterruptedException {
        actTest.c();
    }
    @RequestMapping("/d")
    public void d() throws InterruptedException {
        actTest.d();
    }
    @RequestMapping("/e")
    public void e() throws InterruptedException {
        actTest.e();
    }
    @RequestMapping("/f")
    public void f() throws InterruptedException {
        actTest.f();
    }
    @RequestMapping("/g")
    public void g() throws InterruptedException {
        actTest.g();
    }
    @RequestMapping("/h")
    public void h() throws InterruptedException {
        actTest.h();
    }

    @RequestMapping("/i")
    public void i() throws InterruptedException {
        actTest.i();
    }
    @RequestMapping("/j")
    public void j() throws InterruptedException {
        actTest.j();
    }





}
