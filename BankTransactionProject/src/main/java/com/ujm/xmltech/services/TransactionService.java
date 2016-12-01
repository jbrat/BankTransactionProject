package com.ujm.xmltech.services;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;
import java.util.List;

/**
 *
 * @author julien
 */
public interface TransactionService {
   
    void createFilePain008(FilePain008 file);
    
    Transaction getTransactionById(long id);
    
    void parseTransaction(PaymentInstructionInformation4 transaction008, String codeErreur);
    
    List<FilePain008> getFilesPain008();
    
    List<Transaction> getTransactionsByFileMsgId(String msgId);
    
    Transaction deleteTransaction(long id);
    
}
