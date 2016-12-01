package com.ujm.xmltech.dao;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;

/**
 *
 * @author julien
 */
public interface TransactionDao {
   
    void createFile(FilePain008 file);

    Transaction createTransaction(Transaction transaction);

    FilePain008 findFileByName(String name);

    Transaction findTransactionById(long id);
}
