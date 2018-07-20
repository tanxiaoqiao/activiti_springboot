package com.example.act.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Kris
 * @Description:
 * @Date: 7/19/2018 3:18 PM
 */

@RestController
@RequestMapping("/test")
public class ActivityController {

    @Autowired
    ActivityConsumerServiceImpl activityConsumerService;

    /**
     * 流程demo
     * @return
     */
    @RequestMapping(value = "/activitiDemo", method = RequestMethod.GET)
        public boolean startActivityDemo () {
            return activityConsumerService.startActivityDemo();
        }


    @RequestMapping(value = "/first", method = RequestMethod.GET)
    public void first () {
      activityConsumerService.first();
    }
}

