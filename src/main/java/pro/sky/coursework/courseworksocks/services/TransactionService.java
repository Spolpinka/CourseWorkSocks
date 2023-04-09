package pro.sky.coursework.courseworksocks.services;

import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.model.Transaction;

import java.util.Collection;

@Service
public interface TransactionService {
    boolean addTransaction(Transaction transaction);

    Collection<Transaction> getAllTransactions();
}
