package pro.sky.coursework.courseworksocks.services;

import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Transaction;

@Service
public interface TransactionService {
    boolean addTransaction(Transaction transaction);
}
