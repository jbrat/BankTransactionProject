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
 * Class Validation for input pain008 informations
 * 
 * @author UJM's students
 */
public class ValidateTransaction {
    
    private CustomerDirectDebitInitiationV02 stmrDrctDbtInitn;
    
    public ValidateTransaction(CustomerDirectDebitInitiationV02 stmrDrctDbtInitn) {
        this.stmrDrctDbtInitn = stmrDrctDbtInitn;
    }
    
    /**
     * General method that call the different Unitary test followings
     * 
     * @param pmtInstr
     * 
     * @throws UnitaryException
     * @throws DatatypeConfigurationException 
     */
    public void ValidateTransaction(PaymentInstructionInformation4 pmtInstr) throws UnitaryException, DatatypeConfigurationException{
        ControlChecksumFile();
        IbanExist(stmrDrctDbtInitn);
        LessThan1(pmtInstr);
        BiggerThan10k(pmtInstr);
        CurrencyIsCorrect(pmtInstr);
        CheckIfTransactionIsntHasBeen(pmtInstr);
        CheckIfTransactionIsInMoreThan13Month(pmtInstr);
        ControlChecksumTransaction(pmtInstr);
        CheckSeqTPIsntFRSTRandDateLessT5Days(pmtInstr);
        CheckSeqTPIsntRCURandDateLessT2Days(pmtInstr);
    }
    
    
    /**
     * Method to check if iban exist
     * 
     * @param stmrDrctDbtInitn
     * 
     * @throws UnitaryException 
     */
    private void IbanExist(CustomerDirectDebitInitiationV02 stmrDrctDbtInitn) throws UnitaryException{
        String IBAN= stmrDrctDbtInitn.getGrpHdr().getMsgId();
        String Bank= StringUtils.substring(IBAN, 0, 4);
        if(Banks.checkIfBankExist(Bank)) {
            throw new UnitaryException("RJC000");
        }
    }
    
    /**
     * Method to reject transaction when amount < 1
     * 
     * @param pmtInstr
     * 
     * @throws UnitaryException 
     */
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
    
    /**
     * Method to reject transaction when amount > 10000
     * 
     * @param pmtInstr
     * 
     * @throws UnitaryException 
     */
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

    /**
     * Method to check If the currency is correct
     * 
     * @param pmtInstr
     * 
     * @throws UnitaryException 
     */
    private void CurrencyIsCorrect(PaymentInstructionInformation4 pmtInstr) throws UnitaryException{
        if(GetBasicInfo.getCurrrency(pmtInstr).compareTo("EUR")!=0) {
            throw new UnitaryException("RCJ003");
        }
    }
    
    /**
     * Method to check if the transaction isn't in the pasts
     * 
     * @param pmtInstr
     * 
     * @throws UnitaryException
     * @throws DatatypeConfigurationException 
     */
    private void CheckIfTransactionIsntHasBeen(PaymentInstructionInformation4 pmtInstr) throws UnitaryException, DatatypeConfigurationException{       
        if(GetBasicInfo.getTodayDate().compare(GetBasicInfo.getDatePayment(pmtInstr)) ==-1) {
            throw new UnitaryException("RCJ004");
        }
    }
    
    /**
     * Method to check that the transaction isn't in more than 13month
     * 
     * @param pmtInstr
     * 
     * @throws DatatypeConfigurationException
     * @throws UnitaryException 
     */
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
    
    /**
     * method to check that the SeqTP isn't RCUR and the date of the payment isn't in less than 2 day
     * 
     * @param pmtInstr
     * 
     * @throws DatatypeConfigurationException
     * @throws UnitaryException 
     */
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
    
    
    /**
     * Method to check that the SeqTP isn't FRST and the date of the payment isn't in less than 5 days
     * 
     * @param pmtInstr
     * 
     * @throws DatatypeConfigurationException
     * @throws UnitaryException 
     */
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
     
    /**
     * Method to check the checksum of the File
     * 
     * @throws UnitaryException 
     */
    private void ControlChecksumFile() throws UnitaryException{
        BigDecimal totalamount = GetBasicInfo.getAllPayments(stmrDrctDbtInitn);
        BigDecimal checksumF= GetBasicInfo.getChecksumFile(stmrDrctDbtInitn);
        if(totalamount.compareTo(checksumF) != 0) {
            throw new UnitaryException("RJC009");
        }
    }
     
     
    /**
     * Method to check the checksum of one transaction
     * 
     * @param pmtInstr
     * 
     * @throws UnitaryException 
     */
    private void ControlChecksumTransaction(PaymentInstructionInformation4 pmtInstr) throws UnitaryException{
        BigDecimal amount=GetBasicInfo.getPaymentsTransaction(pmtInstr);
        BigDecimal checksumT=GetBasicInfo.getChecksumTransaction(pmtInstr);
        if(amount.compareTo(checksumT) != 0) {
           throw new UnitaryException("RJC010");
        }
   }  
}   