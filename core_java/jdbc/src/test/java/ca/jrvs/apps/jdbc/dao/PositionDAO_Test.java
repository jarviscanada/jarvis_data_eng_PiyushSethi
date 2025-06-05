package ca.jrvs.apps.jdbc.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.jdbc.helpers.DatabaseConnectionManager;
import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PositionDAO_Test {

    private static Connection connection;
    private static PositionDao positionDao;
    private static QuoteDao quoteDao;
    private static Quote quote;
    private Position position;

    @BeforeClass
    public static void setUp(){
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "stock_quote", "postgres", "password");
        try{
            connection = dcm.getConnection();
            connection.setAutoCommit(false);
            positionDao = new PositionDao(connection);
            quoteDao = new QuoteDao(connection);

            quote = new Quote();
            quote.setTicker("MSFT");
            quote.setOpen(424.01);
            quote.setHigh(435.2);
            quote.setLow(423.5);
            quote.setPrice(434.56);
            quote.setVolume(35647805);
            quote.setLatestTradingDay(new Date(System.currentTimeMillis()));
            quote.setPreviousClose(444.06);
            quote.setChange((-9.5));
            quote.setChangePercent("-2.1394%");
            quote.setTimestamp(new Timestamp(System.currentTimeMillis()));
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        if(connection != null){
            connection.close();
        }
    }

    @Before
    public void setUpPosition(){
        position = new Position();
        position.setTicker("MSFT");
        position.setNumOfShares(100);
        position.setValuePaid(20000.0);
    }

    @After
    public void rollBackTransaction() throws SQLException{
        connection.rollback();
    }

    @Test
    public void save(){
        quoteDao.save(quote);
        Position testPosition = positionDao.save(position);
        assertNotNull(testPosition);
        assertEquals("MSFT", testPosition.getTicker());
        assertEquals(100, testPosition.getNumOfShares());
        assertEquals(20000.0, testPosition.getValuePaid(), 0.01);

        testPosition.setNumOfShares(150);
        testPosition.setValuePaid(30000.0);
        Position testPosition2 = positionDao.save(testPosition);

        assertEquals(150, testPosition2.getNumOfShares());
        assertEquals(30000.0, testPosition2.getValuePaid(), 0.01);
    }

    @Test
    public void findById(){
        quoteDao.save(quote);
        positionDao.save(position);

        assertTrue(positionDao.findById(position.getTicker()).isPresent());
        assertFalse(positionDao.findById("Non existent symbol").isPresent());
    }

    @Test
    public void findAll(){
        quoteDao.save(quote);
        Quote quote2 = new Quote();
        quote2.setTicker("GOOG");
        quote2.setOpen(199.76);
        quote2.setHigh(203.24);
        quote2.setLow(199.47);
        quote2.setPrice(202.63);
        quote2.setVolume(17970000);
        quote2.setLatestTradingDay(new Date(System.currentTimeMillis()));
        quote2.setPreviousClose(197.18);
        quote2.setChange(5.45);
        quote2.setChangePercent("2.7640%");
        quote2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        quoteDao.save(quote2);

        Position position2 = new Position();
        position2.setTicker("GOOG");
        position2.setNumOfShares(200);
        position2.setValuePaid(30000);
        positionDao.save(position);
        positionDao.save(position2);

        List<Position> positionList = (List<Position>) positionDao.findAll();
        assertNotNull(positionList);
        assertTrue(positionList.size() > 1);
    }

    @Test
    public void deletebyId(){
        quoteDao.save(quote);
        positionDao.save(position);
        positionDao.deleteById(position.getTicker());

        assertFalse(positionDao.findById(position.getTicker()).isPresent());
    }

    @Test
    public void deleteAll(){
        quoteDao.save(quote);
        Quote quote2 = new Quote();
        quote2.setTicker("GOOG");
        quote2.setOpen(199.76);
        quote2.setHigh(203.24);
        quote2.setLow(199.47);
        quote2.setPrice(202.63);
        quote2.setVolume(17970000);
        quote2.setLatestTradingDay(new Date(System.currentTimeMillis()));
        quote2.setPreviousClose(197.18);
        quote2.setChange(5.45);
        quote2.setChangePercent("2.7640%");
        quote2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        quoteDao.save(quote2);

        Position position2 = new Position();
        position2.setTicker("GOOG");
        position2.setNumOfShares(200);
        position2.setValuePaid(30000);
        positionDao.save(position);
        positionDao.save(position2);
        positionDao.deleteAll();

        List<Position> positionList = (List<Position>) positionDao.findAll();

        assertTrue(positionList.isEmpty());
    }
}

