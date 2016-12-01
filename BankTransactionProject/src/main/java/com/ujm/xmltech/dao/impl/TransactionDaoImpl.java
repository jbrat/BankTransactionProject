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

@Repository("TransactionDao")
public class TransactionDaoImpl implements TransactionDao {

    @PersistenceContext(unitName = BankSimulationConstants.PERSISTENCE_UNIT)
    protected EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, value = BankSimulationConstants.TRANSACTION_MANAGER)
    public Transaction createTransaction(Transaction transaction) {
        entityManager.persist(transaction);
        entityManager.flush();
        return transaction;
    }

    @Override
    public Transaction findTransactionById(long id) {
        Query query = entityManager.createQuery("SELECT t FROM Transaction t WHERE t.id = :id").setParameter("id", id);
        return (Transaction) query.getSingleResult();
    }

    @Override
    public FilePain008 findFileByName(String msgId) {
        Query query = entityManager.createQuery("SELECT f FROM FilePain008 f WHERE f.MsgId = :MsgId").setParameter("MsgId", msgId);
        try {
            return (FilePain008) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, value = BankSimulationConstants.TRANSACTION_MANAGER)
    public void createFile(FilePain008 file) {
        entityManager.persist(file);
    }
    
    @Override
    public List<FilePain008> getFilesPain008() {
        Query query = entityManager.createQuery("SELECT * FROM FilePain008 f WHERE f.msgId <> :MsgId").setParameter("MsgId", BankSimulationConstants.MY_BANK_IDENTIFIER);
        return query.getResultList();
    }
    
    @Override
    public Transaction deleteTransaction(long id) {
        Query query = entityManager.createQuery("DELETE FROM Transaction where id = :id").setParameter("id", id);
        return (Transaction) query.getResultList();
    }

    @Override
    public List<Transaction> getTransactionsByFileMsgId(String msgId) {
        Query query = entityManager.createQuery("SELECT * FROM Transaction WHERE fileMsgId = :MsgId").setParameter("MsgId", msgId);
        return query.getResultList();
    }
}

