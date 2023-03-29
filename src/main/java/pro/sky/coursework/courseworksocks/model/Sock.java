package pro.sky.coursework.courseworksocks.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.awt.Color;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Sock {
    @NonNull
    @Schema(description = "Цвет носков (Enum)")
    private Colors color;
    @NonNull
    @Schema(description = "Размер носков (Enum)")
    private Sizes size;
    @NotNull
    @Schema(description = "Состав носков (% хлопка в составе), только целые числа")
    private int composition;
//необязательное поле для создания сущности
    @NonNull
    @Schema(description = "Количество пар носков")
    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sock sock = (Sock) o;
        return composition == sock.composition && color == sock.color && size == sock.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, size, composition);
    }
}
