package com.ujm.xmltech.utils;
import iso.std.iso._20022.tech.xsd.pain_008_001.CustomerDirectDebitInitiationV02;
import iso.std.iso._20022.tech.xsd.pain_008_001.DirectDebitTransactionInformation9;
import iso.std.iso._20022.tech.xsd.pain_008_001.PaymentInstructionInformation4;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.joda.time.DateTime;

/**
 * A class for getting basic infos from the pain.008 such as IBANS
 * 
 * @author UJM's students
 */
public class GetBasicInfo {
   
    /**
    * Get the Creditor's IBAN from process
    * 
     * @param pmtInstr
    * @return the creditor IBAN
    */    
    public static String getCreditorIban(PaymentInstructionInformation4 pmtInstr) {
        return pmtInstr.getCdtrAgtAcct().getId().getIBAN();
    }      
    
    /**
    * Get the Debitors' IBAN from process
    * 
     * @param pmtInstr
     * 
    * @return the debitors' IBANs
    */   
    public static String getDebitorIban(PaymentInstructionInformation4 pmtInstr) {
        return fillDebitorsIbans(pmtInstr).get(0);
    } 
      
    /**
     * Fill the Debitors' IBANs from process
     * 
     * @see SetDebitorsIbans
     */
    private static List<String> fillDebitorsIbans (PaymentInstructionInformation4 pmtInstr) {
        List<String> ibans_debitor = new ArrayList<String>();
        List<DirectDebitTransactionInformation9> drctDbtTxInf = pmtInstr.getDrctDbtTxInf();
        //browse the different directDebitTransactionInformations for getting Ibans of debitors
        for(int i=0; i< drctDbtTxInf.size(); i++) {
            DirectDebitTransactionInformation9 d = drctDbtTxInf.get(i);
            //get the Different Ibans of debitors
            ibans_debitor.add(d.getDbtrAcct().getId().getIBAN());
        }   
        return ibans_debitor;
    }
    
    /**
     * Method to get the checkSum of a transaction
     * 
     * @param pmtInstr
     * 
     * @return Bigdecimal CheckSum
     */
    public static BigDecimal getChecksumTransaction(PaymentInstructionInformation4 pmtInstr) {
        return pmtInstr.getCtrlSum();
    }
    
    /**
     * Method to get the checkSum of the total pain008 file
     * 
     * @param ctmrDrctDbtInitn
     * 
     * @return BigDecimal checksum
     */
    public static BigDecimal getChecksumFile(CustomerDirectDebitInitiationV02 ctmrDrctDbtInitn) {
        return ctmrDrctDbtInitn.getGrpHdr().getCtrlSum();   
    }
    
    /**
     * Method to get the Currency of the Transaction
     * 
     * @param pmtInstr 
     * 
     * @return ccy the currency
     */
    public static String getCurrrency(PaymentInstructionInformation4 pmtInstr){
        return pmtInstr.getDrctDbtTxInf().get(1).getInstdAmt().getCcy();
    }
    
    /**
     * Method to get the payment date.
     * 
     * @param pmtInstr 
     * 
     * @return the date of the payment
     */
    public static XMLGregorianCalendar getDatePayment(PaymentInstructionInformation4 pmtInstr){
        return pmtInstr.getReqdColltnDt();
    }
    
    /**
     * Method to get today date.
     * 
     * @return ccy the currency
     */ 
    public static XMLGregorianCalendar getTodayDate() throws DatatypeConfigurationException{
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(new DateTime().toGregorianCalendar());
        }
    
    /**
     * Method to get the SeqTp
     * 
     * @param pmtInstr
     * 
     * @return Seqtp in a string
     */ 
    public static String getSeqTP(PaymentInstructionInformation4 pmtInstr){
        return pmtInstr.getPmtTpInf().getSeqTp().toString();
    }
    
    /**
     * Method to get mandatory information
     * 
     * @param pmtInstr
     * 
     * @return String mandatory
     */
    public static String getMandatory(PaymentInstructionInformation4 pmtInstr){
        return pmtInstr.getPmtMtd().toString();
    }
}
