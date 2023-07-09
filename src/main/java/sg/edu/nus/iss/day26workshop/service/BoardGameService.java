package sg.edu.nus.iss.day26workshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day26workshop.model.Game;
import sg.edu.nus.iss.day26workshop.repository.BoardGameRepository;

@Service
public class BoardGameService {

    @Autowired
    private BoardGameRepository repository;

    public List<Game> getAllGames(Integer limit, Integer offset) {
        return repository.getAllGames(limit, offset);
    }

    public long getGamesCount() {
        return repository.getGamesCount();
    }

}
