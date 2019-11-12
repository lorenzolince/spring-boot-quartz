/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.quarts.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 *
 * @author lorenzolince
 */
public class SimpleJob implements Job{
 
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
    	
    	System.out.println("##################### TEST JOB ###############");
        
    }
}
