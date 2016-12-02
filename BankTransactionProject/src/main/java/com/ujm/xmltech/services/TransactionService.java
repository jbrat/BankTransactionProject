package com.ujm.xmltech.services;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;
import java.util.List;

/**
 * Class Servide for transaction with DAO
 * 
 * @author UJM's students
 */
public interface TransactionService {
   
    /**
     * Method to create a filePain008 in DAO
     * 
     * @param file 
     */
    void createFilePain008(FilePain008 file);
    
    /**
     * Method to get a transaction by Id from DAO
     * 
     * @param id
     * 
     * @return Transaction
     */
    Transaction getTransactionById(long id);
    
    /**
     * Method to parse a Transaction with a pain008 input informations
     * 
     * @param transaction008
     * @param codeErreur 
     */
    void parseTransaction(PaymentInstructionInformation4 transaction008, String codeErreur);
    
    /**
     * Method to get the files Pain008 from DAO
     * 
     * @return List FilePain008
     */
    List<FilePain008> getFilesPain008();
    
    /**
     * Method to get a list of Transactions by an MsgId
     * 
     * @param msgId
     * 
     * @return List Transaction
     */
    List<Transaction> getTransactionsByFileMsgId(String msgId);
    
    
    /**
     * Method to delete a transaction from DAO with an ID
     * 
     * @param id
     * 
     * @return deleted Transaction 
     */
    Transaction deleteTransaction(long id);
    
}
