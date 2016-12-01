package com.ujm.xmltech.dao.impl;

import com.ujm.xmltech.dao.TransactionDao;
import com.ujm.xmltech.entity.FilePain008;
import com.ujm.xmltech.entity.Transaction;
import com.ujm.xmltech.utils.BankSimulationConstants;
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
}

