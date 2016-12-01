/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author julien
 */

@Service("TransactionService")
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionDao transactionDAO;
    
    @Override
    public Transaction getTransactionById(long id) {
        return (Transaction)transactionDAO.findTransactionById(id);
    }
    
    private String filePain008;
    
    @Override
    public void createFilePain008(FilePain008 file) {
        this.filePain008 = file.getMsgId();
        transactionDAO.createFile(file);
    }
    
    /**
     * Method to parse a transaction into model transaction and save it
     * 
     * @param transaction008 
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

    @Override
    public List<FilePain008> getFilesPain008() {
        return transactionDAO.getFilesPain008();
    }

    @Override
    public List<Transaction> getTransactionsByFileMsgId(String msgId) {
       return transactionDAO.getTransactionsByFileMsgId(msgId);
    }

    @Override
    public Transaction deleteTransaction(long id) {
        return transactionDAO.deleteTransaction(id);
    }

  
    
}
