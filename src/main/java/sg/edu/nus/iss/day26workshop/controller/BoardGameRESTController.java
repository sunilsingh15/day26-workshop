package sg.edu.nus.iss.day26workshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.day26workshop.model.Game;
import sg.edu.nus.iss.day26workshop.service.BoardGameService;

@RestController
@RequestMapping
public class BoardGameRESTController {

    @Autowired
    private BoardGameService service;

    @GetMapping(path = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> allGames(@RequestParam(defaultValue = "25") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {

        List<Game> gameList = service.getAllGames(limit, offset);
        JsonArrayBuilder builder = Json.createArrayBuilder();

        for (Game game : gameList) {
            JsonObject gameObj = Json.createObjectBuilder()
                    .add("game_id", game.getId())
                    .add("name", game.getName())
                    .build();

            builder.add(gameObj);
        }

        JsonObject gamesJson = Json.createObjectBuilder()
                .add("games", builder.build())
                .add("offset", offset)
                .add("limit", limit)
                .add("total", service.getGamesCount())
                .add("timestamp", LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<>(gamesJson.toString(), HttpStatus.OK);
    }

    @GetMapping(path = "/games/rank", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> allGamesByRank(@RequestParam(defaultValue = "25") Integer limit,
            @RequestParam(defaultValue = "0") Integer offset) {

        List<Game> gameList = service.getAllGamesByRank(limit, offset);
        JsonArrayBuilder builder = Json.createArrayBuilder();

        for (Game game : gameList) {
            JsonObject gameObj = Json.createObjectBuilder()
                    .add("game_id", game.getId())
                    .add("name", game.getName())
                    .build();

            builder.add(gameObj);
        }

        JsonObject gamesByRankJson = Json.createObjectBuilder()
                .add("games", builder.build())
                .add("offset", offset)
                .add("limit", limit)
                .add("total", service.getGamesCount())
                .add("timestamp", LocalDateTime.now().toString())
                .build();

        return new ResponseEntity<String>(gamesByRankJson.toString(), HttpStatus.OK);
    }

    @GetMapping(path = "/game/{gameID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> gameByID(@PathVariable String gameID) {
        try {
            Integer id = Integer.parseInt(gameID);
            Document game = service.getGameByID(id);

            if (game == null) {
                JsonObject errorJson = Json.createObjectBuilder()
                        .add("error", "No game found with ID: " + id)
                        .build();
                return new ResponseEntity<>(errorJson.toString(), HttpStatus.NOT_FOUND);
            }

            double average = service.getSumofRatingsByID(id) / service.getCommentsCountByID(id);

            JsonObject gameJson = Json.createObjectBuilder()
                    .add("game_id", game.getInteger("gid"))
                    .add("name", game.getString("name"))
                    .add("year", game.getInteger("year"))
                    .add("ranking", game.getInteger("ranking"))
                    .add("average", average)
                    .add("users_rated", game.getInteger("users_rated"))
                    .add("url", game.getString("url"))
                    .add("thumbnail", game.getString("image"))
                    .add("timestamp", LocalDateTime.now().toString())
                    .build();

            return new ResponseEntity<>(gameJson.toString(), HttpStatus.OK);
        } catch (NumberFormatException e) {
            JsonObject errorJson = Json.createObjectBuilder()
                    .add("error", "Invalid game ID: " + gameID)
                    .build();
            return new ResponseEntity<>(errorJson.toString(), HttpStatus.BAD_REQUEST);
        }
    }

}
