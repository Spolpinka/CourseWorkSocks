package pro.sky.coursework.courseworksocks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.coursework.courseworksocks.model.Colors;
import pro.sky.coursework.courseworksocks.model.Sizes;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.services.SocksService;

import java.util.Collection;

@RestController
@RequestMapping("/socks")
@Tag(name = "Носки", description = "Все операции с носками (добавление, правка, удаление)")
public class SocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    //что выдаст прога, если это будет не Json
    @Operation(
            summary = "Добавление носков на склад",
            description = "берем Json и добавляем в базу носков (на склад)"
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "socks",
                            description = "объекты sock в формате JSON которые будут добавлены в базу",
                            content = {
                                    @Content(
                                            array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                                    )
                            }

                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Носки успешно добавлены",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Добавление не удалось"
                    )
            }

    )
    @PutMapping(value = "/addSocks")
    public ResponseEntity<Void> addSocks(@RequestBody Sock... socks) {
        socksService.addSocks(socks);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Забор носков со склада",
            description = "берем Json и забираем оттуда указанное количество носков"
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "socks",
                            description = "объекты sock в формате JSON которые будут добавлены в базу",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                                    )
                            }

                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Носки успешно забраны",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Забор не удался"
                    )
            }

    )
    @PostMapping(value = "/pickUpSocks", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> pickUpSocks(@RequestBody Sock... socks) {
        String s = socksService.pickUpSocks(socks);
        if (!s.equals("не найдено")) {
            return ResponseEntity.ok(s);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Получение списка всех носков",
            description = "Получение списка всей базы носков со склада"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список носков успешно получен",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = Sock.class))
                                    )
                            }
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Список носков пуст / не получен"
                    )
            }

    )

    @GetMapping
    public ResponseEntity<Collection<Sock>> getAllSocks() {
        Collection<Sock> socks = socksService.getAllSocks();
        if (!socks.isEmpty()) {
            return ResponseEntity.ok(socks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/concreteSocks")
    @Operation(
            summary = "Получение списка носков по параметрам",
            description = "Задаем параметры цвет, размер, минимально и максимальное содержание хлопка, " +
                    "и получаем список носков, удовлетворяющих этим признакам."
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "color",
                            description = "Цвет носков",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Colors.class)
                                    )
                            }

                    ),
                    @Parameter(
                            name = "size",
                            description = "размер носков от 26 - 46.5",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Sizes.class)
                                    )
                            }

                    ),
                    @Parameter(
                            name = "cottonMin",
                            description = "минимальное значение содержания хлопка 0 - 100"
                    ),
                    @Parameter(
                            name = "cottonMin",
                            description = "максимальное значение содержания хлопка 0 - 100"
                    )
            }
    )
    public ResponseEntity<Collection<Sock>> getConcreteSocks(@RequestParam String color,
                                                             @RequestParam float size,
                                                             @RequestParam int cottonMin,
                                                             @RequestParam int cottonMax) {

        Collection<Sock> socks = socksService.getSocks(color, size, cottonMin, cottonMax);
        if (socks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(socks);
        }
    }

    @DeleteMapping("/delete")
    @Operation(
            summary = "Списание некондиции",
            description = "Списываем (уничтожаем) бракованные носки"
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "socks",
                            description = "объекты sock в формате JSON которые будут списаны со склада",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = Sock.class)
                                    )
                            }

                    )
            }
    )
    public ResponseEntity<Void> deleteSocks(@RequestBody Sock... socks) {
        if (socksService.deleteSocks(socks)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
