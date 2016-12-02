package com.ujm.xmltech.dao.impl;

import com.ujm.xmltech.dao.TransactionDao;
import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import com.ujm.xmltech.utils.BankSimulationConstants;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO Implementation for repository TransactionDAO
 * 
 * @author UJM's students
 */
@Repository("TransactionDao")
public class TransactionDaoImpl implements TransactionDao {

    @PersistenceContext(unitName = BankSimulationConstants.PERSISTENCE_UNIT)
    protected EntityManager entityManager;
    
    /**
     * Method to persist a transaction 
     * 
     * @param transaction
     * 
     * @return the created transaction
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, value = BankSimulationConstants.TRANSACTION_MANAGER)
    public Transaction createTransaction(Transaction transaction) {
        entityManager.persist(transaction);
        entityManager.flush();
        return transaction;
    }

    /**
     * Method to find a transaction by an ID
     * 
     * @param id
     * 
     * @return the created Transaction
     */
    @Override
    public Transaction findTransactionById(long id) {
        Query query = entityManager.createQuery("SELECT t FROM Transaction t WHERE t.id = :id").setParameter("id", id);
        return (Transaction) query.getSingleResult();
    }

    /**
     * Method to persist a filePain008 in database
     * 
     * @param file 
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, value = BankSimulationConstants.TRANSACTION_MANAGER)
    public void createFile(FilePain008 file) {
        entityManager.persist(file);
    }
    
    /**
     * Method to get a list of filePain008
     * 
     * @return List FilePain008 
     */
    @Override
    public List<FilePain008> getFilesPain008() {
        Query query = entityManager.createQuery("SELECT * FROM FilePain008 f WHERE f.msgId <> :MsgId").setParameter("MsgId", BankSimulationConstants.MY_BANK_IDENTIFIER);
        return query.getResultList();
    }
    
        
    /**
     * Method to delete a Transaction from an ID
     * 
     * @param id
     * 
     * @return Transaction
     */
    @Override
    public Transaction deleteTransaction(long id) {
        Query query = entityManager.createQuery("DELETE FROM Transaction where id = :id").setParameter("id", id);
        return (Transaction) query.getResultList();
    }

    /**
     * Method to get a List of transactions by a file idBank
     * 
     * @param idBank
     * 
     * @return List Transaction
     */
    @Override
    public List<Transaction> getTransactionsByidBank(String idBank) {
        Query query = entityManager.createQuery("SELECT * FROM Transaction WHERE idBank = :idBank").setParameter("idBank", idBank);
        return query.getResultList();
    }
}

