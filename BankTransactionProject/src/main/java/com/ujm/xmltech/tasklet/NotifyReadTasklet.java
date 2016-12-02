package com.ujm.xmltech.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Class which use to notify bank when the application start to read a file
 * 
 * @author UJM's Students
 */
public class NotifyReadTasklet implements Tasklet{

    @Override
    public RepeatStatus execute(StepContribution sc, ChunkContext cc) throws Exception {
        System.out.println("NOTIFICATION READ FILE BANK");
        return null;
    }
    
}
