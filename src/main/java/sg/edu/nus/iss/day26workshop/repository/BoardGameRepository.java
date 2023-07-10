package sg.edu.nus.iss.day26workshop.repository;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.day26workshop.model.Game;

@Repository
public class BoardGameRepository {

    @Autowired
    private MongoTemplate template;

    public List<Game> getAllGames(Integer limit, Integer offset) {
        Criteria criteria = Criteria.where("");
        Query query = Query.query(criteria)
                .limit(limit)
                .skip(offset)
                .with(Sort.by("gid"));

        List<Document> documents = template.find(query, Document.class, "games");
        List<Game> gamesList = new ArrayList<>();

        for (Document document : documents) {
            Game game = new Game(document.getInteger("gid"), document.getString("name"));
            gamesList.add(game);
        }

        return gamesList;
    }

    public List<Game> getAllGamesByRank(Integer limit, Integer offset) {
        Criteria criteria = Criteria.where("");
        Query query = Query.query(criteria)
                .limit(limit)
                .skip(offset)
                .with(Sort.by("ranking"));

        List<Document> documents = template.find(query, Document.class, "games");
        List<Game> gamesList = new ArrayList<>();

        for (Document document : documents) {
            Game game = new Game(document.getInteger("gid"), document.getString("name"));
            gamesList.add(game);
        }

        return gamesList;
    }

    public long getGamesCount() {
        Criteria criteria = Criteria.where("");
        Query query = Query.query(criteria);
        return template.count(query, Document.class, "games");
    }

}
