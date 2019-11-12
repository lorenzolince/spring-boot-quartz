/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.quarts.service;

import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;

import static org.quartz.DateBuilder.dateOf;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;



/**
 *
 * @author lorenzolince
 */
@Component
public class JobManager {
   @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
    private static String JOB_GROUP_NAME = null;
    private static String TRIGGER_GROUP_NAME = null;
    
   @SuppressWarnings("unchecked")
    public void addJob(String jobName, Class cls, String jobGroupName, String triggerName,
                              LocalDateTime startDate, LocalDateTime endDate, HashMap<String, String> param) {
    
    
         try {
        String JOB_GROUP_NAME = jobName;

            JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
             Scheduler sched = schedulerFactoryBean.getScheduler();
             if (!sched.checkExists(jobKey)) {

                JobDetail job = newJob(cls)
                        .withIdentity(jobName, jobGroupName)
                        .build();

                for(Map.Entry<String, String> entry : param.entrySet()) {
                    job.getJobDataMap().put(entry.getKey(), entry.getValue());
                }

                Trigger trigger = null;

                if ("PRINCIPAL".equals(jobName)) {
                    trigger = TriggerBuilder
                            .newTrigger()
                            .withIdentity(jobName, triggerName)
                            .withSchedule(simpleSchedule()
                                    .withIntervalInSeconds(10)
                                    //  .withIntervalInMinutes(10)
                                    .repeatForever())
                            .startAt(dateOf(startDate.getHour(), startDate.getMinute()
                                    , startDate.getSecond(), startDate.getDayOfMonth(), startDate.getMonth().getValue(), startDate.getYear())).build();
                } else {
                    trigger = TriggerBuilder
                            .newTrigger()
                            .withIdentity(jobName, triggerName)
                            .withSchedule(simpleSchedule()
                                    .withIntervalInSeconds(20)
                                    //  .withIntervalInMinutes(10)
                                    .repeatForever())
                            .startAt(dateOf(startDate.getHour(), startDate.getMinute()
                                    , startDate.getSecond(), startDate.getDayOfMonth(), startDate.getMonth().getValue(), startDate.getYear()))
                            .endAt(dateOf(endDate.getHour(), endDate.getMinute()
                                    , endDate.getSecond(), endDate.getDayOfMonth(), endDate.getMonth().getValue(), endDate.getYear())).build();
                }

                 sched.scheduleJob(job, trigger);
                 if (!sched.isShutdown()) {
                     sched.start();
                 }
            }

       } catch (Exception e) {
       }
    }
    @SuppressWarnings("unchecked")
    public void modifyJobTime(String jobName, String time) {
        TriggerKey triggerKey = TriggerKey.triggerKey(
                jobName, TRIGGER_GROUP_NAME);

        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            CronTrigger trigger =(CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                CronScheduleBuilder scheduleBuilder =CronScheduleBuilder.cronSchedule(time);

                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();

                sched.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void modifyJobTime(String triggerName,
                                     String triggerGroupName, String time) {
        TriggerKey triggerKey = TriggerKey.triggerKey(
                triggerName, triggerGroupName);
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                        .cronSchedule(time);

                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder).build();

                sched.resumeTrigger(triggerKey);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void removeJob(String jobName) {

        String TRIGGER_GROUP_NAME = jobName;
        String JOB_GROUP_NAME = jobName;

        TriggerKey triggerKey = TriggerKey.triggerKey(
                jobName, TRIGGER_GROUP_NAME);
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try {
            //Scheduler sched = gSchedulerFactory.getScheduler();
            Scheduler sched = schedulerFactoryBean.getScheduler();
            Trigger trigger = (Trigger) sched.getTrigger(triggerKey);
            if (trigger != null) {
                sched.pauseTrigger(triggerKey);
                sched.unscheduleJob(triggerKey);
                sched.deleteJob(jobKey);
                System.out.println("JOB REMOVED"+jobName);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(
                jobName, triggerGroupName);
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseTrigger(triggerKey);
            sched.unscheduleJob(triggerKey);
            sched.deleteJob(jobKey);
            System.out.println("JOB REMOVED..: "+jobName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void pauseJob(String jobName, String jobGroupName) {
        JobKey jobKey =JobKey.jobKey(jobName, jobName);
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(jobKey);
        } catch (SchedulerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void pauseJob(String jobName) {
        JobKey jobKey =JobKey.jobKey(jobName, JOB_GROUP_NAME);
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    public void startJobs() {
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void shutdownJobs() {
        try {
            Scheduler sched = schedulerFactoryBean.getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
