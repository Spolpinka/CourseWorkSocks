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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.services.SocksService;

import java.util.Collection;

@RestController
@RequestMapping("/socks")
@Tag(name = "Носки", description = "Все операции с носками (добавление, правка, удаление")
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

        /*if (socksService.addSocks(socks)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }*/
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
    public ResponseEntity<String> pickUpSocks(Sock... socks) {
        String s = socksService.pickUpSocks(socks);
        if (!s.equals("не найдено")) {
            return ResponseEntity.ok(s);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Collection<Sock>> getAllSocks() {
        Collection<Sock> socks = socksService.getAllSocks();
        if (!socks.isEmpty()) {
            return ResponseEntity.ok(socks);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
