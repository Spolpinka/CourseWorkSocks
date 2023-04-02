package pro.sky.coursework.courseworksocks.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Transaction;
import pro.sky.coursework.courseworksocks.services.FilesService;
import pro.sky.coursework.courseworksocks.services.TransactionService;

import javax.annotation.PostConstruct;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
            transactions.put(transId++, transaction);
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Collection<Transaction> getAllTransactions() {
        return transactions.values();
    }

    private void readFromFile() {
        try {
            DataFile dataFile = new ObjectMapper().readValue(filesService.readTransactions(), new TypeReference<DataFile>() {
            });
            transactions = dataFile.transactions;
            transId = dataFile.lastId;
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
    }

    private void saveToFile() {
        try {
            DataFile dataFile = new DataFile(transId, transactions);
            String s = new ObjectMapper().writeValueAsString(dataFile);
            filesService.saveTransactionFile(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DataFile {
        private int lastId;
        private Map<Integer, Transaction> transactions;
    }
}
