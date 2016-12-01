package com.ujm.xmltech.validator;

import com.ujm.xmltech.utils.Banks;
import com.ujm.xmltech.exception.UnitaryException;
import iso.std.iso._20022.tech.xsd.pain_008_001.CustomerDirectDebitInitiationV02;
import iso.std.iso._20022.tech.xsd.pain_008_001.DirectDebitTransactionInformation9;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;
import java.math.BigDecimal;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.ujm.xmltech.utils.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 */
public class ValidateTransaction {
    
    private CustomerDirectDebitInitiationV02 stmrDrctDbtInitn;
    
    public ValidateTransaction(CustomerDirectDebitInitiationV02 stmrDrctDbtInitn) {
        this.stmrDrctDbtInitn = stmrDrctDbtInitn;
    }
    
    //General method that call the different Unitary test followings
    public void ValidateTransaction(PaymentInstructionInformation4 pmtInstr) throws UnitaryException, DatatypeConfigurationException{
        IbanExist(stmrDrctDbtInitn);
        LessThan1(pmtInstr);
        BiggerThan10k(pmtInstr);
        CurrencyIsCorrect(pmtInstr);
        CheckIfTransactionIsntHasBeen(pmtInstr);
        CheckIfTransactionIsInMoreThan13Month(pmtInstr);
    }
    
    
        // Check If Iban exist
    private void IbanExist(CustomerDirectDebitInitiationV02 stmrDrctDbtInitn) throws UnitaryException{
        String IBAN= stmrDrctDbtInitn.getGrpHdr().getMsgId();
        String Bank= StringUtils.substring(IBAN, 0, 4);
        if(Banks.checkIfBankExist(Bank)) {
            throw new UnitaryException("RJC000");
        }
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
    private void BiggerThan10k(PaymentInstructionInformation4 pmtInstr) throws UnitaryException {
 
        List<DirectDebitTransactionInformation9> drctDbtTxInf = pmtInstr.getDrctDbtTxInf();
        for(int i=0; i< drctDbtTxInf.size(); i++) {
            DirectDebitTransactionInformation9 d = drctDbtTxInf.get(i);
            BigDecimal amount = d.getInstdAmt().getValue();
            if(amount.intValue()>10000) {
                throw new UnitaryException("RJC002");
            }
        }
    }

    
    
    // Check If the currency is correct
    private void CurrencyIsCorrect(PaymentInstructionInformation4 pmtInstr) throws UnitaryException{
        if(GetBasicInfo.getCurrrency(pmtInstr).compareTo("EUR")!=0) {
            throw new UnitaryException("RCJ003");
        }
    }
    
    
    
    //Check if the transaction isn't in the past
    private void CheckIfTransactionIsntHasBeen(PaymentInstructionInformation4 pmtInstr) throws UnitaryException, DatatypeConfigurationException{       
            if(GetBasicInfo.getTodayDate().compare(GetBasicInfo.getDatePayment(pmtInstr)) ==-1) {
                throw new UnitaryException("RCJ004");
            }
    }
    
    
    //Check that the transaction isn't in more than 13month
    private void CheckIfTransactionIsInMoreThan13Month(PaymentInstructionInformation4 pmtInstr) throws DatatypeConfigurationException, UnitaryException{
        
        //Add 13month to TodayDate
        DatatypeFactory df = DatatypeFactory.newInstance();
        XMLGregorianCalendar today = GetBasicInfo.getTodayDate();
        XMLGregorianCalendar tmpCalendar = (XMLGregorianCalendar) today.clone();
        tmpCalendar.add(df.newDuration("P13M"));
        if(tmpCalendar.compare(GetBasicInfo.getDatePayment(pmtInstr)) ==-1) {
            throw new UnitaryException("RJC005");
        }
    }
    
    
    //Check that the SeqTP isn't RCUR and the date of the payment isn't in less than 2 days
    private void CheckSeqTPIsntRCURandDateLessT2Days(PaymentInstructionInformation4 pmtInstr) throws DatatypeConfigurationException, UnitaryException{
        DatatypeFactory df= DatatypeFactory.newInstance();
        XMLGregorianCalendar today= GetBasicInfo.getTodayDate();
        XMLGregorianCalendar tmpCalendar = (XMLGregorianCalendar) today.clone();
        tmpCalendar.add(df.newDuration("P2D"));
        String seqTP=GetBasicInfo.getSeqTP(pmtInstr);
        if(tmpCalendar.compare(GetBasicInfo.getDatePayment(pmtInstr)) ==-1 && seqTP.compareTo("RCUR")==0) {
           throw new UnitaryException("RJC006");
        }
    }
    
    
    //Check that the SeqTP isn't FRST and the date of the payment isn't in less than 5 days
     private void CheckSeqTPIsntFRSTRandDateLessT5Days(PaymentInstructionInformation4 pmtInstr) throws DatatypeConfigurationException, UnitaryException{
        DatatypeFactory df= DatatypeFactory.newInstance();
        XMLGregorianCalendar today=GetBasicInfo.getTodayDate();
        XMLGregorianCalendar tmpCalendar = (XMLGregorianCalendar) today.clone();
        tmpCalendar.add(df.newDuration("P5D"));
        String seqTP=GetBasicInfo.getSeqTP(pmtInstr);
        if(tmpCalendar.compare(GetBasicInfo.getDatePayment(pmtInstr)) ==-1 && seqTP.compareTo("FRST")==0) {
           throw new UnitaryException("RJC007");
        }
    }
}   