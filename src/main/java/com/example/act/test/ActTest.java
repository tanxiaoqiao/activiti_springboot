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

    @Autowired
    ManagementService managementService;

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
        //todo 部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/multi.bpmn").deploy();
        //todo 流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //todo 启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());
    }

    //多分支流，有receive Task的时候，调用trigger往下执行
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
        System.out.println(exe.getId() + "++++" + exe.getActivityId());
        //todo 执行流往下走
        runtimeService.trigger(exe.getId());
        exe = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId()).onlyChildExecutions().singleResult();
        System.out.println("当前" + exe.getActivityId());
    }


    //捕获事件  流程停在此处等待外部发送信号，流程才会继续往下走  注意时间标签signal要自己添加在bpmn文件
    //抛出事件 无需外部通知，流程继续往下走
    public void ten() {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/ten.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());

        Execution exe = runtimeService.createExecutionQuery()
                .processInstanceId(pi.getId()).onlyChildExecutions()
                .singleResult();
        System.out.println(exe.getId() + "++++" + exe.getActivityId());
        //todo 执行触发通知，signal标签里面的id
        runtimeService.signalEventReceived("test");
        //消息事件的通知
        // runtimeService.messageEventReceived("test",exe.getId());
        exe = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).onlyChildExecutions().singleResult();
        //todo 已经到下一个节点
        System.out.println(exe.getId() + "++++" + exe.getActivityId());

    }

    //ru_job表一般事件 ru_execution看到processInstance   主要看bpmn
    public void eleven() {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/eleven.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());

    }

    //定时任务 主要看bpmn
    public void twelve() {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/twelve.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());

    }

    //中止任务 主要看bpmn
    public void othree() throws InterruptedException {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/twelve.bpmn").deploy();
        //流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        System.out.println(pi.getId());
        Thread.sleep(10000);
        //中止
        runtimeService.suspendProcessInstanceById(pi.getId());
        Thread.sleep(10000);
        //再次启动
        runtimeService.activateProcessInstanceById(pi.getId());
        //todo job任务的查询都用managementService
    }

    //定时任务
    public void ofour() throws InterruptedException {
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("view/定时事件.bpmn").deploy();
        long count = runtimeService.createProcessInstanceQuery().count();
        System.out.println("启动前流程数量" + count);
        Thread.sleep(700000);
        count = runtimeService.createProcessInstanceQuery().count();
        System.out.println("启动HOU流程数量" + count);
    }


    //错误开始事件
    public void  a(){
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/a.bpmn").deploy();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());



    }

    //错误结束时间
    public void  b(){
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/b.bpmn").deploy();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());

    }

    //终止结束事件
    public void  c(){
        //部署流程
        Deployment deploy = repositoryService.createDeployment().addClasspathResource("processes/c.bpmn").deploy();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
        //启动流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(pd.getId());
        long count = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).count();
        System.out.println("当前任务数量："+count);

        List<Task> list = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
        list.forEach(l->{
            if (l.getName().equals("UserTask1")){
                taskService.complete(l.getId());
            }
        });

        System.out.println("==============end");
        ProcessInstance pid = runtimeService.createProcessInstanceQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("当前还有事件数量："+pid);

    }
    public void  d(){

    }
    public void  e(){

    }
    public void  f(){

    }
    public void  g(){

    }
    public void  h(){

    }
    public void  i(){

    }
    public void  j(){

    }



}
