package ca.jrvs.apps.jdbc;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ca.jrvs.apps.jdbc.controller.StockQuoteController;
import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.helpers.DatabaseConnectionManager;
//import ca.jrvs.apps.jdbc.helpers.PropertyLoader;
import ca.jrvs.apps.jdbc.helpers.QuoteHttpHelper;
//import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.service.PositionService;
import ca.jrvs.apps.jdbc.service.QuoteService;
import okhttp3.OkHttpClient;

public class Main {
    public static void main(String[] args) {
        Map<String, String> properties = new HashMap<>();

        try(BufferedReader br = new BufferedReader(new FileReader("src/main/resources/properties.txt"))) {
        String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(":");
                properties.put(tokens[0], tokens[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{
            Class.forName(properties.get("db-class"));
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient();
//        String url = "jdbc:postgresql://" + properties.get("server") +":"+  properties.get("port") + "/" + properties.get("database");
//        try (Connection connection = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))){
        DatabaseConnectionManager dbconman = new DatabaseConnectionManager(properties.get("server"), properties.get("database"), properties.get("username"), properties.get("password"));
        try (Connection connection = dbconman.getConnection()){
            QuoteDao qRepo = new QuoteDao(connection);
            PositionDao pRepo = new PositionDao(connection);
            QuoteHttpHelper rCon = new QuoteHttpHelper(properties.get("api-key"), okHttpClient);
            QuoteService sQuote = new QuoteService(qRepo, rCon);
            PositionService sPos = new PositionService(pRepo, qRepo);
            StockQuoteController con = new StockQuoteController(sQuote, sPos);
            con.initClient();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
