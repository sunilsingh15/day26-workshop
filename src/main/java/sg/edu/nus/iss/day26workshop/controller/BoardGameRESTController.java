package sg.edu.nus.iss.day26workshop.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

}
