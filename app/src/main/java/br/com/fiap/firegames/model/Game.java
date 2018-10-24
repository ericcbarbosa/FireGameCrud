package br.com.fiap.firegames.model;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private String id;
    private String name;
    private String developer;
    private String releaseDate;

    public Game() {
    }

    public Game(String name, String developer, String releaseDate) {
        this.name = name;
        this.developer = developer;
        this.releaseDate = releaseDate;
    }

    public Game(String id, String name, String developer, String releaseDate) {
        this.id = id;
        this.name = name;
        this.developer = developer;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", this.name);
        result.put("developer", this.developer);
        result.put("releaseDate", this.releaseDate);

        return result;
    }

}
