package pro.sky.coursework.courseworksocks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Транзакции", description = "Операции с транзакциями (пока выгрузка всех транзакций)")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping()
    @Operation(
            summary = "Получение всех транзакций",
            description = "Выводим все транзакции, имеющиеся в базе"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Транзакции успешно получены",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Transaction.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Ответ отсутствует"
                    )
            }

    )
    public ResponseEntity<Collection<Transaction>> getAllTransactions() {
        if (!transactionService.getAllTransactions().isEmpty()) {
            return ResponseEntity.ok(transactionService.getAllTransactions());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
