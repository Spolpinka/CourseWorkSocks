package pro.sky.coursework.courseworksocks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.coursework.courseworksocks.services.FilesService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/files")
@Tag(name = "Файлы (базы данных)", description = "Работа с файлами, содержащими базы данных носков на складе.")
public class FilesController {
    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    @PutMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Импорт базы данных носков",
            description = "Полное замещение существующей базы носков из предоставляемого файла"
    )
    @Parameters(
            value = {
                    @Parameter(
                            name = "file",
                            style = ParameterStyle.DEEPOBJECT,
                            description = "файл, содержащий базу носков в формате JSON"
                    )
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                      responseCode = "200",
                      description = "база загружена успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "переданы ошибочные данные / неверный формат данных"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "ошибка, не зависящая от пользователя"
                    )
            }
    )
    public ResponseEntity<Void> uploadSocksBase(@RequestParam MultipartFile file) {
        if (filesService.uploadSocksFile(file)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Выгрузка актуальной базы носков",
            description = "Вся база носков выгружается в том виде, в котором она хранится в программе"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "база выгружена успешно"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "база данных отсутствует"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "ошибка, не зависящая от пользователя"
                    )
            }
    )
    public ResponseEntity<InputStreamResource> downloadDataFile() throws FileNotFoundException {
        File file = filesService.getSocksFile();
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Socks.json\"")
                    .contentLength(file.length())
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
