package com.example.act.test;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Kris
 * @Description:
 * @Date: 7/20/2018 2:57 PM
 */
@Service
public class ActTest {

    @Autowired
    ProcessEngine processEngine;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

    @Autowired
    RepositoryService repositoryService;

    public void first() {
        IdentityService is = processEngine.getIdentityService();
        for (int i=0;i<10;i++) {
            Group group = is.newGroup(""+i);
            group.setName("Group_"+i);
            group.setType("TYPE_"+i);
            is.saveGroup(group);
        }

    }

    public void getList(){
        List<Group> list = processEngine.getIdentityService().createGroupQuery().list();
        list.forEach(l-> System.out.println(l.getName()+"--"));

    }

    //部署流程
    public void third(){
        RepositoryService rs = processEngine.getRepositoryService();
        DeploymentBuilder deployment = rs.createDeployment();
        deployment.addClasspathResource("processes/first.bpmn");
        deployment.deploy();
    }


}
