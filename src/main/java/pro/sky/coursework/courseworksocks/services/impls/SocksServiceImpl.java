package pro.sky.coursework.courseworksocks.services.impls;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Colors;
import pro.sky.coursework.courseworksocks.model.Sock;
import pro.sky.coursework.courseworksocks.model.Transaction;
import pro.sky.coursework.courseworksocks.model.TypeOfTransaction;
import pro.sky.coursework.courseworksocks.services.FilesService;
import pro.sky.coursework.courseworksocks.services.SocksService;
import pro.sky.coursework.courseworksocks.services.TransactionService;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Service

public class SocksServiceImpl implements SocksService {

    private final FilesService filesService;
    private final TransactionService transactionService;
    private static Map<Integer, Sock> socks = new HashMap<>();
    private static int id = 0;

    public SocksServiceImpl(FilesService filesService, TransactionService transactionService) {
        this.filesService = filesService;
        this.transactionService = transactionService;
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
                if (socks.containsValue(newSocks[i])) {
                    int resultId = searchForIdInSocks(newSocks[i]);//Id уже заведенных носков
                    int oldQuantity = socks.get(resultId).getQuantity();//для фиксации старого значения количества
                    newSocks[i].setQuantity(newSocks[i].getQuantity() + oldQuantity);
                    socks.put(resultId, newSocks[i]);
                } else {
                    socks.put(id++, newSocks[i]);
                }
                //сохраняем транзакцию
                transactionService.addTransaction(new Transaction(TypeOfTransaction.ADD, newSocks[i].getQuantity(),
                        newSocks[i].getSize(), newSocks[i].getComposition(), newSocks[i].getColor()));
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
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < takenSocks.length; i++) {
                int takenId = searchForIdInSocks(takenSocks[i]);
                Sock existingSock = socks.get(takenId);
                //если такие носки есть и их количество не меньше запрошенного
                if (socks.containsValue(takenSocks[i])
                        && existingSock.getQuantity() >= takenSocks[i].getQuantity()) {
                    existingSock.setQuantity(existingSock.getQuantity() - takenSocks[i].getQuantity());
                    //добавляем транзакцию
                    if (transactionService.addTransaction(new Transaction(TypeOfTransaction.PICK_UP, takenSocks[i].getQuantity(),
                            takenSocks[i].getSize(), takenSocks[i].getComposition(), takenSocks[i].getColor()))) {
                        System.out.println("Транзакция отправлена на добавление!");
                    } else {
                        System.out.println("Транзакция не прошла на добавление!");
                    }
                } else {
                    isEnough = false;
                    answer.append("На складе недостаточно носков: ")
                            .append(takenSocks[i].getColor()).append("; ")
                            .append("содержание хлопка - ")
                            .append(takenSocks[i].getComposition()).append("%; ")
                            .append("размер носков ")
                            .append(takenSocks[i].getSize().getValue()).append("\n")
                            .append("Запрошенное количество - ")
                            .append(takenSocks[i].getQuantity()).append(", ")
                            .append("в наличии на складе - ").append(existingSock.getQuantity()).append("\n");
                }
            }
            if (!isEnough) {
                //если недостаточно - посылаем ответ, чего именно недостаточно
                return answer.toString();
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

    @Override
    public Collection<Sock> getSocks(String color, float size, int cottonMin, int cottonMax) {
        Collection<Sock> result = new HashSet<>();
        for (Sock sock :
                socks.values()) {
            if (sock.getColor() == Colors.valueOf(color.toUpperCase()) &&
                    sock.getSize().getValue() == size &&
                    sock.getComposition() >= cottonMin &&
                    sock.getComposition() <= cottonMax) {
                result.add(sock);
            }
        }
        return result;
    }

    @Override
    public boolean deleteSocks(Sock[] deletedSocks) {
        for (Sock deletedSock :
                deletedSocks) {
            if (socks.containsValue(deletedSock)) {
                Sock neededSock = socks.get(searchForIdInSocks(deletedSock));
                int newQuantity = neededSock.getQuantity() - deletedSock.getQuantity();
                if (newQuantity >= 0) {
                    neededSock.setQuantity(newQuantity);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private void readFromFile() {
        try {
            //распознаем из файла объект класса DataFile, в котором записаны наши данные
            DataFile dataFile = new ObjectMapper().readValue(filesService.readSocks(), new TypeReference<>() {
            });
            //берем из объекта коллекцию носков и последний id
            socks = dataFile.socks;
            id = dataFile.lastId++;
        } catch (JsonProcessingException e) {
            e.printStackTrace();

        }
    }

    private void saveToFile() {
        try {
            DataFile dataFile = new DataFile(id, socks);
            String s = new ObjectMapper().writeValueAsString(dataFile);
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DataFile {
        private int lastId;
        private Map<Integer, Sock> socks;
    }
}
