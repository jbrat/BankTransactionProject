package com.ujm.xmltech.services.impl;

import com.ujm.xmltech.dao.TransactionDao;
import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import com.ujm.xmltech.entity.factory.TransactionFactory;
import com.ujm.xmltech.services.TransactionService;
import iso.std.iso._20022.tech.xsd.pain_008_001.DirectDebitTransactionInformation9;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implement class for Transaction Service
 * 
 * @author UJM's students
 */
@Service("TransactionService")
public class TransactionServiceImpl implements TransactionService {

    /**
     * Injection of the DAO transaction service
     */
    @Autowired
    private TransactionDao transactionDAO;

    private String filePain008;
    
    /**
     * Method to get a transaction by Id from DAO
     * 
     * @param id
     * 
     * @return Transaction
     */
    @Override
    public Transaction getTransactionById(long id) {
        return (Transaction)transactionDAO.findTransactionById(id);
    }
    
    /**
     * Method to create a filePain008 in DAO
     * 
     * @param file 
     */
    @Override
    public void createFilePain008(FilePain008 file) {
        this.filePain008 = file.getMsgId();
        transactionDAO.createFile(file);
    }
    
    /**
     * Method to parse a Transaction with a pain008 input informations
     * 
     * @param transaction008
     * @param codeErreur 
     */
    @Override
    public void parseTransaction(PaymentInstructionInformation4 transaction008, String codeErreur) {
        
        Iterator<DirectDebitTransactionInformation9> it2 = transaction008.getDrctDbtTxInf().iterator();
        
        while(it2.hasNext()) {
            DirectDebitTransactionInformation9 infoTransaction = it2.next();
            Transaction newTransaction = TransactionFactory.createTransaction(filePain008, infoTransaction, transaction008);
            newTransaction.setCodeErreur(codeErreur);
            
            transactionDAO.createTransaction(newTransaction);
            
            System.out.println("TRANSACTION OK :"+newTransaction.getId());
        }
    }  

    /**
     * Method to get the files Pain008 from DAO
     * 
     * @return List FilePain008
     */
    @Override
    public List<FilePain008> getFilesPain008() {
        return transactionDAO.getFilesPain008();
    }

    /**
     * Method to get a list of Transactions by an MsgId
     * 
     * @param msgId
     * 
     * @return List Transaction
     */
    @Override
    public List<Transaction> getTransactionsByFileMsgId(String msgId) {
        return transactionDAO.getTransactionsByFileMsgId(msgId);
    }

    /**
     * Method to delete a transaction from DAO with an ID
     * 
     * @param id
     * 
     * @return deleted Transaction 
     */
    @Override
    public Transaction deleteTransaction(long id) {
        return transactionDAO.deleteTransaction(id);
    }
}
