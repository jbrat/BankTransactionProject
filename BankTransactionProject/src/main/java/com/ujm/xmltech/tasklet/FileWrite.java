package com.ujm.xmltech.tasklet;

import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import com.ujm.xmltech.services.TransactionService;
import com.ujm.xmltech.utils.BankSimulationConstants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * class FileWrite to generate a pain008 for the others banks.
 * 
 * @author UJM's students
 */
public class FileWrite implements Tasklet{

    /**
     * Injection service transaction to persist with DAO
     */
    @Autowired
    private TransactionService serviceTransaction;
    
    /*
     * Method execute with the class RepeatStatus.
     */
    @Override
    public RepeatStatus execute(StepContribution step, ChunkContext context) throws Exception {
        //L'object va maintenant etre de scruter en bdd (potentiellement dans une etape precedente) afin de determiner si le fichier a ete totalement traite
        //Si c'est le cas il faut recuperer toutes les informations dont vous avez besoin et boucler dessus via la methode write pour les ecrire dans un fichier
        return RepeatStatus.FINISHED;
    }
        
    public void write(Object item) {
        //Added a random in order to have a different file each time
        String namebank = null;
      
        List<FilePain008> listFilesPain008 = serviceTransaction.getFilesPain008();
        for(FilePain008 filePain008 : listFilesPain008) {

            //get the bank name, from the first 4th letter of the IBAN
            namebank = filePain008.getMsgId().substring(0, 4);
            File file = new File(BankSimulationConstants.OUT_DIRECTORY + "pain008.001.002_"+ namebank + "_" + Math.random() + ".xml");
            OutputStream out;
            try {
                out = new FileOutputStream(file);
                OutputStreamWriter writer = new OutputStreamWriter(out);
                JAXBContext ctx = JAXBContext.newInstance(item.getClass());
                Marshaller marshaller = ctx.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
                //writer file header
                String documentBase = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.008.001.02\">\n";
                writer.write(documentBase);
                //write header item
                writer.write(getXMLFragment(item, "CstmrDrctDbtInitn", marshaller) + "\n");
                
                List<Transaction> listTransactions = serviceTransaction.getTransactionsByFileMsgId(filePain008.getMsgId());
                
                String XMLPAIN008Transactions = "";
                for(Transaction transaction : listTransactions) {
                    // ECRITURE DANS LE FILE XMLPAIN008
                    writer.write("<GrpHdr>\n");
                    
                    /* Balise écriture fileMsgId */
                    writer.write("\n<fileMsgId>\n");
                    String fileMsgId  = transaction.getFileMsgId();
                    writer.write("  " + fileMsgId);
                    writer.write("\n</fileMsgId>\n");
                    
                    /* Balise écriture checksum */
                    writer.write("\n<checksum>\n");
                    BigDecimal checksum = transaction.getChecksum();
                    writer.write("  " + Integer.parseInt(checksum.toString()));
                    writer.write("\n</checksum>\n");
                    
                    writer.write("</GrpHdr>\n");
                    
                    /* Balise écriture id */
                    writer.write("\n<id>\n");
                    long id = transaction.getId();
                    writer.write("  " + (int) id);
                    writer.write("\n</id>\n");
                    
                    /* Balise écriture amount */
                    writer.write("\n<amount>\n");
                    long amount = transaction.getId();
                    writer.write("  " + (int) amount);
                    writer.write("\n</amount>\n");
                                     
                    /* Balise écriture codeErreur */
                    writer.write("\n<codeErreur>\n");
                    String codeerror = transaction.getCodeErreur();
                    writer.write("  " + codeerror);
                    writer.write("\n</codeErreur>\n");
                    
                    /* Balise écriture Currency */
                    writer.write("\n<currency>\n");
                    String currency = transaction.getCurrency();
                    writer.write("  " + currency);
                    writer.write("\n</currency>\n");
                    
                    /* Balise écriture date */
                    writer.write("\n<date>\n");
                    String date = transaction.getDate();
                    writer.write("  " + date);
                    writer.write("\n</date>\n");
                    
                    /* Balise écriture endToEndId */
                    writer.write("\n<endToEndId>\n");
                    String endToEndId  = transaction.getEndToEndId();
                    writer.write("  " + endToEndId);
                    writer.write("\n</endToEndId>\n");
                    
                    /* Balise écriture ibanDebitor */
                    writer.write("\n<ibanDebitor>\n");        
                    String ibanDebitor  = transaction.getIbanDebitor();
                    writer.write("  " + ibanDebitor);
                    writer.write("\n</ibanDebitor>\n");
                    
                    /* Balise écriture slev */
                    writer.write("\n<slev>\n");  
                    String slev = transaction.getSlev();
                    writer.write(slev);
                    writer.write("\n<slev>\n");
                    
                    /* Delete transaction */
                    serviceTransaction.deleteTransaction(transaction.getId());
                }
                writer.write(XMLPAIN008Transactions);
                //write footer
                String documentEnd = "\n</Document>";
                writer.write(documentEnd);

                writer.close();
                out.close();
                
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            } catch (JAXBException e) {
                    e.printStackTrace();
            }
        }
    }

    /**
     * Transform an object into xml string
     * 
     * @param object
     * @param name
     * @param marshaller
     * 
     * @return String fragment
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private String getXMLFragment(Object object, String name, Marshaller marshaller) {
        StringWriter writer = new StringWriter();
        try {
            marshaller.marshal(new JAXBElement(new QName("", name, ""), object.getClass(), object), writer);
        } catch (JAXBException e) {
            return null;
        }
        String originFragment = writer.toString();
        String fragment = originFragment.replaceAll("<" + name + ".*>", "<" + name + ">").replaceAll("<ns2:", "<").replaceAll("</ns2:", "</");
        fragment = fragment.replaceAll("&quot;", "\"").replaceAll("&apos;", "\'");

        return fragment;
    }
}