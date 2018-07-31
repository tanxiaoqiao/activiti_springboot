package com.example.act.test;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * @Author: Kris
 * @Description:
 * @Date: 7/31/2018 10:19 AM
 */
public class NydeleteTest implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        System.out.println("ny补偿");
    }
}
