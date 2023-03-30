package pro.sky.coursework.courseworksocks.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.services.FilesService;
import pro.sky.coursework.courseworksocks.services.SocksService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {

    private final FilesService filesService;

    private static Map<Sock, Integer> socks = new HashMap<>();

    public SocksServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        readFromFile();
    }

    //добавление. Вопрос: именно здесь прога увидит, что в массиве нет объектов sock? И значит ли это, что массив null?
    @Override
    public boolean addSocks(Sock[] newSocks) {
        if (newSocks != null) {
            for (int i = 0; i < newSocks.length; i++) {
                Integer x = socks.putIfAbsent(newSocks[i], newSocks[i].getQuantity());
                if (x != null) {
                    socks.replace(newSocks[i], x + newSocks[i].getQuantity());
                }
            }
            saveToFile();
            return true;
        }
        return true;
    }

    @Override
    public String pickUpSocks(Sock[] takenSocks) {
        if (takenSocks != null) {
            //проверяем наличие
            boolean isEnough = true;
            String answer = "";
            for (int i = 0; i < takenSocks.length; i++) {
                if (!socks.containsKey(takenSocks[i])
                        || socks.get(takenSocks[i]) < takenSocks[i].getQuantity()) {
                    isEnough = false;
                    answer += "На складе недостаточно носков: " + takenSocks[i].getColor() + "; "
                            + "содержание хлопка - " + takenSocks[i].getComposition() + "%; "
                            + "размер носков " + takenSocks[i].getSize().getValue() + "\n"
                            + "Запрошенное количество - " + takenSocks[i].getQuantity() + ", "
                            + "в наличии на складе - " + socks.get(takenSocks[i]) + "\n";
                }
            }
            if (!isEnough) {
                //если недостаточно - посылаем ответ, чего именно недостаточно
                return answer;
            } else {
                for (int i = 0; i < takenSocks.length; i++) {
                    int newQuantity = socks.get(takenSocks[i]) - takenSocks[i].getQuantity();
                    socks.replace(takenSocks[i], socks.get(takenSocks), takenSocks[i].getQuantity());
                }
                saveToFile();
            }
        } else {
            return "false";
        }
        return "";
    }

    @Override
    public Collection<Sock> getAllSocks() {
        return socks.keySet();
    }

    private void readFromFile() {
        try {
            socks = new ObjectMapper().readValue(filesService.readSocks(), new TypeReference<HashMap<Sock, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
    }

    private void saveToFile() {
        try {
            String s = new ObjectMapper().writeValueAsString(socks);
            filesService.saveSocksFile(s);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
