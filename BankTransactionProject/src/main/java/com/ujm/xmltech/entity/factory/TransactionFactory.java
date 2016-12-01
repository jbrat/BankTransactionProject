package com.ujm.xmltech.entity.factory;

import com.ujm.xmltech.entity.Transaction;
import com.ujm.xmltech.utils.GetBasicInfo;
import iso.std.iso._20022.tech.xsd.pain_008_001.DirectDebitTransactionInformation9;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;

/**
 *
 * @author julien
 */
public class TransactionFactory {
    
    public static Transaction createTransaction(String file, 
            DirectDebitTransactionInformation9 infoTransaction, 
            PaymentInstructionInformation4 transaction008) {
        
        Transaction newTransaction = new Transaction();
        newTransaction.setFileMsgId(file);
        newTransaction.setAmount(infoTransaction.getInstdAmt().getValue().longValue());
        newTransaction.setEndToEndId(infoTransaction.getPmtId().getEndToEndId());
        newTransaction.setCurrency(infoTransaction.getInstdAmt().getCcy());
        newTransaction.setDate(infoTransaction.getDrctDbtTx().getMndtRltdInf().getDtOfSgntr().toGregorianCalendar().getTime().toGMTString());
        newTransaction.setIbanCreditor(GetBasicInfo.getCreditorIban(transaction008));
        newTransaction.setIbanDebitor(GetBasicInfo.getDebitorIban(transaction008));
        newTransaction.setChecksum(GetBasicInfo.getChecksumTransaction(transaction008));
        
        return newTransaction;
    }
}
