package pro.sky.coursework.courseworksocks.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.model.Transaction;
import pro.sky.coursework.courseworksocks.services.FilesService;
import pro.sky.coursework.courseworksocks.services.TransactionService;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final FilesService filesService;
    private Map<Integer, Transaction> transactions = new HashMap<>();
    private static int transId;

    public TransactionServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }
    @Override
    public boolean addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.put(transId, transaction);
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    private void readFromFile() {
        try {
            transactions = new ObjectMapper().readValue(filesService.readTransactions(), new TypeReference<Map<Integer, Transaction>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
    }

    private void saveToFile() {
        try {
            String s = new ObjectMapper().writeValueAsString(transactions);
            filesService.saveTransactionFile(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
