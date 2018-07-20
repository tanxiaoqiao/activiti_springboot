package com.example.act;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ActApplicationTests {

    @Test
    public void createTableTest(){
        //表不存在的话创建表
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResource("activiti.xml")
                .buildProcessEngine();
        System.out.println("------processEngine:" + processEngine);

    }

    @Test
    public void tt(){

    }


}
