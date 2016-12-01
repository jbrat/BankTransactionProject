package com.ujm.xmltech.validator;

import com.ujm.xmltech.utils.Banks;
import com.ujm.xmltech.exception.UnitaryException;
import iso.std.iso._20022.tech.xsd.pain_008_001.CustomerDirectDebitInitiationV02;
import iso.std.iso._20022.tech.xsd.pain_008_001.DirectDebitTransactionInformation9;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 *
 */
public class ValidateTransaction {
    
    private CustomerDirectDebitInitiationV02 stmrDrctDbtInitn;
    
    public ValidateTransaction(CustomerDirectDebitInitiationV02 stmrDrctDbtInitn) {
        this.stmrDrctDbtInitn = stmrDrctDbtInitn;
    }
    
    //General method that call the different Unitary test followings
    public void ValidateTransaction(PaymentInstructionInformation4 pmtInstr) throws UnitaryException{
        LessThan1(pmtInstr);
        BiggerThan1(pmtInstr);
        IbanExist(stmrDrctDbtInitn);
    
    }
    
    //Amount <1, reject transaction
    private void LessThan1(PaymentInstructionInformation4 pmtInstr) throws UnitaryException {

        List<DirectDebitTransactionInformation9> drctDbtTxInf = pmtInstr.getDrctDbtTxInf();
        for(int i=0; i< drctDbtTxInf.size(); i++) {
            DirectDebitTransactionInformation9 d = drctDbtTxInf.get(i);
            BigDecimal amount = d.getInstdAmt().getValue();
            if(amount.intValue()<1) {
                throw new UnitaryException("RJC001");
            }
        }
    }
    
    
      
    //Amount > 10 000, reject transaction
    private void BiggerThan1(PaymentInstructionInformation4 pmtInstr) throws UnitaryException {
 
        List<DirectDebitTransactionInformation9> drctDbtTxInf = pmtInstr.getDrctDbtTxInf();
        for(int i=0; i< drctDbtTxInf.size(); i++) {
            DirectDebitTransactionInformation9 d = drctDbtTxInf.get(i);
            BigDecimal amount = d.getInstdAmt().getValue();
            if(amount.intValue()>10000) {
                throw new UnitaryException("RJC002");
            }
        }
    }

    
    
    // Check If Iban exist
    private void IbanExist(CustomerDirectDebitInitiationV02 stmrDrctDbtInitn) throws UnitaryException{
        String IBAN= stmrDrctDbtInitn.getGrpHdr().getMsgId();
        String Bank= StringUtils.substring(IBAN, 0, 4);
        if(Banks.checkIfBankExist(Bank)) {
            throw new UnitaryException("RJC002");
        }
    }
    
}   
