package pro.sky.coursework.courseworksocks.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Tag(name = "POJO транзакций", description = "Простой класс транзакций")
public class Transaction {
    @NonNull
    @Schema(description = "Тип транзакции", anyOf = TypeOfTransaction.class)
    private TypeOfTransaction type;
    @NonNull
    @Schema(description = "дата, фиксируется автоматически", implementation = LocalDate.class)
    private String localDate;
    @NonNull
    @Schema(description = "Количество носков", minimum = "0")
    private int quantity;

    @NonNull
    @Schema(description = "Размер носков")
    private Sizes size;

    @NonNull
    @Schema(description = "Процентное содержание хлопка", minimum = "0", maximum = "100", format = "000")
    private int composition;

    @NonNull
    @Schema(description = "Цвет носков", anyOf = Colors.class)
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
