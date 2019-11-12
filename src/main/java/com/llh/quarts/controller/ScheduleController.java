/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.quarts.controller;

import com.llh.quarts.service.JobManager;
import com.llh.quarts.job.SimpleJob;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author lorenzolince
 */
@RestController
@RequestMapping(name = "Scheduled", value = "/api/scheduled")
public class ScheduleController {

       @Autowired
       private JobManager jobManager;
    private static final String NAME_ = "PRINCIPAL";

   @RequestMapping(path = "/", method = RequestMethod.GET)
    public void scheduleJob() {
         HashMap<String, String> param = new HashMap<>();
           jobManager.addJob(NAME_, SimpleJob.class, NAME_, NAME_, LocalDateTime.now(), LocalDateTime.now(), param);
     }
   

   
}
