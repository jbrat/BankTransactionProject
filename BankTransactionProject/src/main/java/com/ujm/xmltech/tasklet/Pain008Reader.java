package com.ujm.xmltech.tasklet;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.exception.UnitaryException;
import com.ujm.xmltech.services.TransactionService;
import iso.std.iso._20022.tech.xsd.pain_008_001.Document;
import iso.std.iso._20022.tech.xsd.pain_008_001.GroupHeader39;
import iso.std.iso._20022.tech.xsd.pain_008_001.ObjectFactory;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.repeat.RepeatStatus;
import com.ujm.xmltech.utils.BankSimulationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import com.ujm.xmltech.validator.ValidateTransaction;

/**
 * Method to read the pain008 file 
 * 
 * @author UJM' students
 */
public class Pain008Reader implements Tasklet {

    /**
     * Inject the transaction service for interact with DAO
     */
    @Autowired
    private TransactionService serviceTransaction;
    
    /**
     * Validation class for pain008 informations
     */
    private ValidateTransaction validateTransaction;
    
    @Override
    public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
        read((String) arg1.getStepContext().getJobParameters().get("inputFile"));
        return RepeatStatus.FINISHED;
    }

    @SuppressWarnings("rawtypes")
    public Object read(String fileName) throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
      
        JAXBContext jc;
        
        try {
            jc = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller u = jc.createUnmarshaller();
            File f = new File(BankSimulationConstants.WORK_DIRECTORY + fileName);
            FileReader fileReader = new FileReader(f);
            JAXBElement element = (JAXBElement) u.unmarshal(fileReader);
            Document document = (Document) element.getValue();
            GroupHeader39 header = document.getCstmrDrctDbtInitn().getGrpHdr();

            // Save the file ID
            serviceTransaction.createFilePain008(new FilePain008(header.getMsgId()));
            
            Iterator<PaymentInstructionInformation4> it = document.getCstmrDrctDbtInitn().getPmtInf().iterator();
            validateTransaction = new ValidateTransaction(document.getCstmrDrctDbtInitn());
            
            while (it.hasNext()) {
                PaymentInstructionInformation4 transaction = it.next();
                String codeErreur = "RJC111"; // CODE OK
                
                try {
                    validateTransaction.ValidateTransaction(transaction);
                } catch(UnitaryException e) {
                    if(e.getMessage().equals("RJC009")) {
                        throw new Exception("Fichier rejeté, erreur: RJC009");
                    } 
                    
                    codeErreur = e.getMessage();
                }   
                
                serviceTransaction.parseTransaction(transaction, codeErreur);
            }
            
            return document.getCstmrDrctDbtInitn();
            
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RepeatStatus.FINISHED;
      }
  
}
