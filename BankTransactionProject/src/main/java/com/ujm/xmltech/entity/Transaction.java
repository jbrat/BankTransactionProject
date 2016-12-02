package com.ujm.xmltech.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class model to persist the Transactions in database
 * 
 * @author UJM's students
 */
@Entity
public class Transaction implements Serializable {

    private static final long serialVersionUID = 8315057757268890401L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long amount;

    private String endToEndId;
    
    private String ibanDebitor;
    
    private String ibanCreditor;

    private String date;
    
    private String slev;
    
    private BigDecimal checksum;
    
    private String fileMsgId;
    
    private String currency;
    
    private String codeErreur;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getEndToEndId() {
        return endToEndId;
    }

    public void setEndToEndId(String endToEndId) {
        this.endToEndId = endToEndId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getFileMsgId() {
        return fileMsgId;
    }

    public void setFileMsgId(String fileMsgId) {
        this.fileMsgId = fileMsgId;
    }

    public String getIbanDebitor() {
        return ibanDebitor;
    }

    public void setIbanDebitor(String ibanDebitor) {
        this.ibanDebitor = ibanDebitor;
    }
    
    public String getIbanCreditor() {
        return ibanCreditor;
    }

    public void setIbanCreditor(String ibanCreditor) {
        this.ibanCreditor = ibanCreditor;
    }

    public String getSlev() {
        return slev;
    }

    public void setSlev(String slev) {
        this.slev = slev;
    }

    public BigDecimal getChecksum() {
        return checksum;
    }

    public void setChecksum(BigDecimal checksum) {
        this.checksum = checksum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    } 
    
    public String getCodeErreur() {
        return codeErreur;
    }

    public void setCodeErreur(String codeErreur) {
        this.codeErreur = codeErreur;
    }
}
