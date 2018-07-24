package com.example.act.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @Author: Kris
 * @Description:
 * @Date: 7/23/2018 2:25 PM
 */
public class TestDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("处理类");
    }
}
