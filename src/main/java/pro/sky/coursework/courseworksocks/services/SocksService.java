package pro.sky.coursework.courseworksocks.services;

import org.springframework.stereotype.Service;
import pro.sky.coursework.courseworksocks.model.Sock;

import java.util.Collection;

@Service
public interface SocksService {
    boolean addSocks(Sock[] socks);

    String pickUpSocks(Sock[] takenSocks);

    Collection<Sock> getAllSocks();

    Collection<Sock> getSocks(String color, float size, int cottonMin, int cottonMax);

    boolean deleteSocks(Sock[] deletedSocks);
}
