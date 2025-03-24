package ca.jrvs.apps.jdbc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.jdbc.helpers.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import ca.jrvs.apps.jdbc.helpers.QuoteHttpHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PositionService_IntTest {

    private static PositionService positionService;
    private static PositionDao positionDao;
    private static QuoteDao quoteDao;
    private static Connection connection;
    private static QuoteService quoteService;

    @BeforeClass
    public static void setUp() throws SQLException {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        connection = dcm.getConnection();
        connection.setAutoCommit(false);
        quoteDao = new QuoteDao(connection);
        positionDao = new PositionDao(connection);
        OkHttpClient client = new OkHttpClient();
        String apiKey = "0663c3ccf5mshcc8e09c1439bb25p19cd67jsnaa913948a975";
        QuoteHttpHelper httpHelper = new QuoteHttpHelper(apiKey, client);
        positionService = new PositionService(positionDao, quoteDao);
        quoteService = new QuoteService(quoteDao, httpHelper);
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        if(connection != null)
            connection.close();
    }

    @After
    public void rollbackTransaction() throws SQLException {
        connection.rollback();
    }

    @Test
    public void buyTest1(){
        quoteService.fetchQuoteDataFromAPI("MSFT");
        positionService.buy("MSFT", 0, 0);
        Position before = positionDao.findById("MSFT").get();
        Position position = positionService.buy("MSFT", 100, 400.0);

        assertEquals("MSFT", position.getTicker());
        assertEquals(before.getNumOfShares() + 100, position.getNumOfShares());
        assertEquals(before.getValuePaid() + 400, position.getValuePaid(), 0.01);

        Position position2 = positionService.buy("MSFT", 100, 400.0);

        assertEquals(before.getNumOfShares() + 200, position2.getNumOfShares());
        assertEquals(before.getValuePaid() + 800, position2.getValuePaid(), 0.01);
    }

    @Test(expected = RuntimeException.class)
    public void buyTest2(){
        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI("PLTR");
        int volume = quote.get().getVolume();
        double price = quote.get().getPrice();

        positionService.buy("PLTR", volume + 1 , price);
    }

    @Test(expected = RuntimeException.class)
    public void buyTest3(){
        quoteService.fetchQuoteDataFromAPI("Non existent symbol");

        positionService.buy("Non existent symbol", 1, 1);
    }

    @Test
    public void sellTest1(){
        quoteService.fetchQuoteDataFromAPI("NVDA");

        Position position = positionService.buy("NVDA", 20, 120.0);
        assertTrue(positionDao.findById("NVDA").isPresent());
        assertEquals(20, position.getNumOfShares());
        assertEquals(120, position.getValuePaid(), 0.01);

        positionService.sell("NVDA");
        assertTrue(positionDao.findById("NVDA").isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void sellTest2(){
        quoteService.fetchQuoteDataFromAPI("Non existent symbol");

        positionService.sell("Non existent symbol");
    }
}