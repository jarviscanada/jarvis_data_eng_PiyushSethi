package ca.jrvs.apps.jdbc.service;

import ca.jrvs.apps.jdbc.helpers.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
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

import static org.junit.Assert.*;


public class QuoteService_IntTest {

    private static QuoteService quoteService;
    private static QuoteDao quoteDao;
    private static Connection connection;

    @BeforeClass
    public static void setUp() throws SQLException {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "stock_quote",
                "postgres", "password");
        connection = dcm.getConnection();
        connection.setAutoCommit(false);
        quoteDao = new QuoteDao(connection);
        OkHttpClient client = new OkHttpClient();
        String apiKey = "0663c3ccf5mshcc8e09c1439bb25p19cd67jsnaa913948a975";
        QuoteHttpHelper httpHelper = new QuoteHttpHelper(apiKey, client);
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
    public void testFetchQuoteDataFromAPIValidTicker() {
        String validTicker = "TSLA";

        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(validTicker);

        // Check if data is fetched and saved
        assertTrue("Quote fetched and saved.", quote.isPresent());

        // Verify quote is saved to db
        Optional<Quote> savedQuote = quoteDao.findById(validTicker);
        assertNotNull("Quote should be saved in the database.", savedQuote);
        assertEquals(validTicker, savedQuote.get().getTicker());
    }

    @Test
    public void testFetchQuoteDataFromAPIInvalidTicker() {
        String invalidTicker = "INVALIDTICKER";

        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(invalidTicker);

        // Check if data is not fetched due to invalid ticker
        assertFalse("Quote should not be fetched for invalid ticker.", quote.isPresent());

        // Verify quote is not saved to db
        Optional<Quote> savedQuote = quoteDao.findById(invalidTicker);
        assertFalse("Quote should not be saved in the database for invalid ticker.", savedQuote.isPresent());
    }
}