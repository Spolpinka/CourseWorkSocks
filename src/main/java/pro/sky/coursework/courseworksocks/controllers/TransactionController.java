package pro.sky.coursework.courseworksocks.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.coursework.courseworksocks.model.Transaction;
import pro.sky.coursework.courseworksocks.services.TransactionService;

import java.util.Collection;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Транзакции", description = "Операции с транзакциями")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    public ResponseEntity<Collection<Transaction>> getAllTransactions() {
        if (!transactionService.getAllTransactions().isEmpty()) {
            return ResponseEntity.ok(transactionService.getAllTransactions());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
