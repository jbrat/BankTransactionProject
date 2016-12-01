package com.ujm.xmltech.dao;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import java.util.List;

/**
 *
 * @author julien
 */
public interface TransactionDao {
   
    void createFile(FilePain008 file);

    Transaction createTransaction(Transaction transaction);

    FilePain008 findFileByName(String name);

    Transaction findTransactionById(long id);
    
    List<FilePain008> getFilesPain008();

    List<Transaction> getTransactionsByFileMsgId(String msgId);
    
    Transaction deleteTransaction(long id);
}
