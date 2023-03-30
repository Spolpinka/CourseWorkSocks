package pro.sky.coursework.courseworksocks.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.services.FilesService;
import pro.sky.coursework.courseworksocks.services.SocksService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service

public class SocksServiceImpl implements SocksService {

    private final FilesService filesService;
    //@JsonDeserialize
    private static Map<Integer, Sock> socks = new TreeMap<>();
    private static int id = 0;

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
                //проверяем, есть ли уже такие носки в базе
                if (socks.containsValue(newSocks[i])){
                    int resultId = searchForIdInSocks(newSocks[i]);//Id уже заведенных носков
                    int oldQuantity = socks.get(resultId).getQuantity();//для фиксации старого значения количества

                    newSocks[i].setQuantity(newSocks[i].getQuantity() + oldQuantity);
                    socks.put(resultId, newSocks[i]);
                } else {
                    socks.put(id++, newSocks[i]);
                }

                //если Sock является кллючом, но для этого надо дописывать десериализатор, потому что, видите ли, не можем мы воспринимать кастомные объекты как ключ
                /*Integer x = socks.putIfAbsent(newSocks[i], newSocks[i].getQuantity());
                if (x != null) {
                    socks.replace(newSocks[i], x + newSocks[i].getQuantity());
                }*/
            }
            saveToFile();
            return true;
        }
        return false;
    }

    @Override
    public String pickUpSocks(Sock[] takenSocks) {
        if (takenSocks != null) {
            //проверяем наличие
            boolean isEnough = true;
            String answer = "";
            for (int i = 0; i < takenSocks.length; i++) {
                //если такие носки есть и их количество не меньше запрошенного
                int takenId = searchForIdInSocks(takenSocks[i]);
                Sock existingSock = socks.get(takenId);
                if (socks.containsValue(takenSocks)
                        && existingSock.getQuantity() >= takenSocks[i].getQuantity()) {
                    existingSock.setQuantity(existingSock.getQuantity() - takenSocks[i].getQuantity());
                } else {
                    isEnough = false;
                    answer += "На складе недостаточно носков: " + takenSocks[i].getColor() + "; "
                            + "содержание хлопка - " + takenSocks[i].getComposition() + "%; "
                            + "размер носков " + takenSocks[i].getSize().getValue() + "\n"
                            + "Запрошенное количество - " + takenSocks[i].getQuantity() + ", "
                            + "в наличии на складе - " + existingSock.getQuantity() + "\n";
                }
            }
            if (!isEnough) {
                //если недостаточно - посылаем ответ, чего именно недостаточно
                return answer;
            } else {
                saveToFile();
                return "все носки забраны успешно";
            }
        } else {
            return "false";
        }
    }

    @Override
    public Collection<Sock> getAllSocks() {
        return socks.values();
    }

    private void readFromFile() {
        try {
            socks = new ObjectMapper().readValue(filesService.readSocks(), new TypeReference<TreeMap<Integer, Sock>>() {
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

    private int searchForIdInSocks(Sock sock) {
        for (Map.Entry<Integer, Sock> entry : socks.entrySet()) {
            if (entry.getValue().equals(sock)) {
                return entry.getKey();
            }
        }
        return 0;
    }
}
