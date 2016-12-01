/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ujm.xmltech.tasklet;

import com.ujm.xmltech.sockets.FileSender;
import com.ujm.xmltech.utils.BankSimulationConstants;
import java.io.File;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * Class to send the pain008 file 
 *  
 * @author UJM's students
 */
public class SendPain008 implements Tasklet{

    @Override
    public RepeatStatus execute(StepContribution sc, ChunkContext cc) throws Exception {
        
        File folder = new File(BankSimulationConstants.IN_DIRECTORY);
        for (File file : folder.listFiles()) {
            FileSender fileSender = new FileSender(file);
        }
        
        return RepeatStatus.FINISHED;
    }
    
}
