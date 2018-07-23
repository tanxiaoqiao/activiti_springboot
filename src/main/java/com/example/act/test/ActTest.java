package com.example.act.test;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
        for (int i = 0; i < 10; i++) {
            Group group = is.newGroup("" + i);
            group.setName("Group_" + i);
            group.setType("TYPE_" + i);
            is.saveGroup(group);
        }

    }

    public void getList() {
        List<Group> list = processEngine.getIdentityService().createGroupQuery().list();
        list.forEach(l -> System.out.println(l.getName() + "--"));

    }

    //部署流程
    public void third() {
        //RepositoryService rs = processEngine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("processes/first.bpmn").deploy();
        runtimeService.startProcessInstanceByKey("myProcess_1");

    }


    //候选人 持有者 代理人(assign)
    public void four() {
        String taskId = UUID.randomUUID().toString();
        Task task = taskService.newTask(taskId);
        task.setName("ceshi");
        TaskService ts = processEngine.getTaskService();
        String userId = UUID.randomUUID().toString();
        User user = processEngine.getIdentityService().newUser(userId);
        ts.saveTask(task);
        user.setFirstName("test");

        //设置候选人 可多个
        ts.addCandidateUser(taskId, userId);


        List<Task> list = ts.createTaskQuery().taskCandidateUser(userId).list();
        list.forEach(l -> System.out.println("该用户有的task" + l.getName()));

        //设置持有者  只能有一个  任务分配给了谁
        String owner = UUID.randomUUID().toString();
        User own = processEngine.getIdentityService().newUser(owner);
        own.setFirstName("owner");
        ts.setOwner(taskId, owner);

        //设置代理人  代被分配的人 一个任务只能有一个代理人
        String assign = UUID.randomUUID().toString();
        User assigner = processEngine.getIdentityService().newUser(owner);
        assigner.setFirstName("owner");
        ts.claim(taskId, assign);
    }


    public void five() {
        Task task = taskService.newTask(UUID.randomUUID().toString());
        task.setName("任务方法");
        taskService.saveTask(task);
        taskService.setVariable(task.getId(), "var", "hello");
    }


    //设置任务参数
    public void six() {
        Task task = taskService.newTask(UUID.randomUUID().toString());
        task.setName("var方法");
        taskService.saveTask(task);
        Person p = new Person();
        p.setName("person1");
        taskService.setVariable(task.getId(), "person1", p);
        System.out.println(taskService.getVariable(task.getId(), "person1", Person.class));
    }

    public void seven() {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/first.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例 注意是流程定义的id
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println(task.getName() + "111111111");
        taskService.complete(task.getId());
        Task task1 = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("当前任务：" + task1.getName());

    }


    //多分支流
    public void eight() {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/multi.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());
    }

    //多分支流
    public void nine() {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/nine.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());

        Execution exe = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId()).onlyChildExecutions()
                .singleResult();
        System.out.println(exe.getId()+"++++"+exe.getActivityId());
        //执行流往下走
        runtimeService.trigger(exe.getId());
        exe = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId()).onlyChildExecutions().singleResult();
        System.out.println("当前" + exe.getActivityId());
    }


}
