package com.ujm.xmltech.dao;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import java.util.List;

/**
 * Transaction DAO to persist in Database
 * 
 * @author UJM's students
 */
public interface TransactionDao {
   
    /**
     * Method to persist a filePain008 in database
     * 
     * @param file 
     */
    void createFile(FilePain008 file);

    /**
     * Method to persist a transaction 
     * 
     * @param transaction
     * 
     * @return the created transaction
     */
    Transaction createTransaction(Transaction transaction);

    /**
     * Method to find a transaction by an ID
     * 
     * @param id
     * 
     * @return the created Transaction
     */
    Transaction findTransactionById(long id);
    
    /**
     * Method to get a list of filePain008
     * 
     * @return List FilePain008 
     */
    List<FilePain008> getFilesPain008();

    /**
     * Method to get a List of transactions by an idBank
     * 
     * @param msgId
     * 
     * @return List Transaction
     */
    List<Transaction> getTransactionsByidBank(String idBank);
    
    /**
     * Method to delete a Transaction from an ID
     * 
     * @param id
     * 
     * @return Transaction
     */
    Transaction deleteTransaction(long id);
}
