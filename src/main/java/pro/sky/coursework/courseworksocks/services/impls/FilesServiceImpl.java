package pro.sky.coursework.courseworksocks.services.impls;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.coursework.courseworksocks.services.FilesService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FilesServiceImpl implements FilesService {
    @Value("${path.to.files}")
    private String filesPath;

    @Value("${name.of.socks.file}")
    private String socksFile;

    @Value("${name.of.operations.file}")
    private String operationsFile;


    @Override
    public boolean saveSocksFile(String json) {
        try{
            cleanSocksFile();
            Files.writeString(Path.of(filesPath, socksFile), json);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File getSocksFile() {
        return new File(filesPath + "/" + socksFile);
    }

    @Override
    public String readSocks() {
        try {
            return Files.readString(Path.of(filesPath, socksFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //метод для очистки файла Socks
    @Override
    public boolean cleanSocksFile() {
        try {
            Files.deleteIfExists(Path.of(filesPath, socksFile));
            Files.createFile(Path.of(filesPath, socksFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //метод для очистки файла операций
    @Override
    public boolean cleanOperationsFile() {
        try {
            Files.deleteIfExists(Path.of(filesPath, operationsFile));
            Files.createFile(Path.of(filesPath, operationsFile));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Path createTempFile(String suffix) {
        try {
            return Files.createTempFile(Path.of(filesPath, socksFile), "tempFile", suffix);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public boolean uploadSocksFile(MultipartFile file) {
        if (cleanSocksFile()) {
            System.out.println("Очистка прошла успешно");
        } else {
            System.out.println("Что-то с очисткой не задалось...");
        }
        File dataFile = getSocksFile();
        try (FileOutputStream fos = new FileOutputStream(dataFile)) {
            IOUtils.copy(file.getInputStream(), fos);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
