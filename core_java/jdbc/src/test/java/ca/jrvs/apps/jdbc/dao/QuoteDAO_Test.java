package ca.jrvs.apps.jdbc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.jdbc.models.Quote;
import ca.jrvs.apps.jdbc.helpers.DatabaseConnectionManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuoteDAO_Test {
    private QuoteDao quoteDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
        connection = dcm.getConnection();
        quoteDao = new QuoteDao(connection);
        quoteDao.setC(connection);
    }

    // Helper method to create a Quote object
    private Quote createTestQuote(String ticker) {
        Quote newQuote = new Quote();
        newQuote.setTicker(ticker);
        newQuote.setOpen(550.50);
        newQuote.setHigh(560.50);
        newQuote.setLow(540.00);
        newQuote.setPrice(555.50);
        newQuote.setVolume(800000);
        newQuote.setLatestTradingDay(new Timestamp(Instant.now().toEpochMilli()));
        newQuote.setPreviousClose(545.50);
        newQuote.setChange(10.00);
        newQuote.setChangePercent("1.44%");
        newQuote.setTimestamp(Timestamp.from(Instant.now()));
        return newQuote;
    }

    @Test
    void save() {
        Quote newQuote = createTestQuote("AAML");
        Quote savedQuote = quoteDao.save(newQuote);

        assertNotNull(savedQuote);
        assertEquals("AAML", savedQuote.getTicker());
        assertEquals(555.50, savedQuote.getPrice(), 0.01);
    }

    @Test
    void create() {
        Quote newQuote = createTestQuote("MSFT");
        Quote savedQuote = quoteDao.save(newQuote);

        assertNotNull(savedQuote);
        assertEquals("MSFT", savedQuote.getTicker());
        assertEquals(555.50, savedQuote.getPrice(), 0.01);
    }

    @Test
    void update() {
        Quote newQuote = createTestQuote("AAML");
        quoteDao.save(newQuote);

        newQuote.setPrice(150.00);
        newQuote.setChange(5.00);

        Quote updatedQuote = quoteDao.save(newQuote);

        assertNotNull(updatedQuote);
        assertEquals("AAML", updatedQuote.getTicker());
        assertEquals(150.00, updatedQuote.getPrice(), 0.01);
        assertEquals(5.00, updatedQuote.getChange(), 0.01);
    }

    @Test
    void findById() {
        Optional<Quote> foundQuote = quoteDao.findById("MSFT");

        assertTrue(quoteDao.findById(foundQuote.get().getTicker()).isPresent());
        assertFalse(quoteDao.findById("Non existent symbol").isPresent());
    }

    @Test
    void findAll() {
        Quote newQuote1 = createTestQuote("AMZN");
        Quote newQuote2 = createTestQuote("NFLX");

        quoteDao.save(newQuote1);
        quoteDao.save(newQuote2);

        Iterable<Quote> quotes = quoteDao.findAll();

        assertNotNull(quotes);
        assertTrue(quotes.iterator().hasNext());
    }

    @Test
    void deleteById() {
        Quote newQuote = createTestQuote("ABC");
        quoteDao.save(newQuote);

        quoteDao.deleteById("ABC");

        Optional<Quote> foundQuote = quoteDao.findById("ABC");
        assertFalse(foundQuote.isPresent());
    }

    @Test
    void deleteAll() {
        Quote newQuote1 = createTestQuote("GOOG");
        Quote newQuote2 = createTestQuote("AAPL");

        quoteDao.save(newQuote1);
        quoteDao.save(newQuote2);

        quoteDao.deleteAll();

        Iterable<Quote> quotes = quoteDao.findAll();
        assertFalse(quotes.iterator().hasNext());
    }

}