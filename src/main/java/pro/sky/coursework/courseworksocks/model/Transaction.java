package pro.sky.coursework.courseworksocks.model;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "POJO транзакций", description = "Простой класс транзакций")
public class Transaction {

    private TypeOfTransaction type;
    private String localDate;

    private int quantity;

    private Sizes size;

    private int composition;

    private Colors color;

    public Transaction(TypeOfTransaction type, int quantity, Sizes size, int composition, Colors color) {
        this.type = type;
        this.localDate = LocalDate.now().getDayOfMonth() + ":"
                + LocalDate.now().getMonth() + ":"
                + LocalDate.now().getYear();
        this.quantity = quantity;
        this.size = size;
        this.composition = composition;
        this.color = color;
    }
}
