package com.example.act.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @Author: Kris
 * @Description:
 * @Date: 7/23/2018 2:25 PM
 */
public class TestError implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("抛出异常啦");
        //开始抛出事件  就是ActTes.a（）需要开启
        //throw new BpmnError("error");
    }
}
