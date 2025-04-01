package ca.jrvs.apps.jdbc.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PositionService_UnitTest {

    private PositionService positionService;
    @Mock
    private QuoteDao mockQuoteDao;
    @Mock
    private PositionDao mockPositionDao;

    @Before
    public void setUp(){
        positionService = new PositionService(mockPositionDao, mockQuoteDao);
    }

    @Test
    public void buyTest1(){
        Quote mockQuote = new Quote();
        mockQuote.setTicker("AMD");
        mockQuote.setPrice(300.0);
        mockQuote.setVolume(4000);

        Position mockPosition = new Position();
        mockPosition.setTicker("AMD");
        mockPosition.setNumOfShares(150);
        mockPosition.setValuePaid(45000);

        when(mockQuoteDao.findById("AMD")).thenReturn(Optional.of(mockQuote));
        when(mockPositionDao.findById("AMD")).thenReturn(Optional.of(mockPosition));

        Position testPosition = positionService.buy("AMD", 150, 300.0);

        assertEquals("AMD", testPosition.getTicker());
        assertEquals(300, testPosition.getNumOfShares());
        assertEquals(45300, testPosition.getValuePaid(), 0.01);
        verify(mockPositionDao).save(mockPosition);
    }

    @Test(expected = RuntimeException.class)
    public void buyTest_InvalidShares_Volume(){
        Quote mockQuote = new Quote();
        mockQuote.setTicker("MSFT");
        mockQuote.setPrice(400.0);
        mockQuote.setVolume(1000);

        Position mockPosition = new Position();
        mockPosition.setTicker("MSFT");
        mockPosition.setNumOfShares(100);
        mockPosition.setValuePaid(40000);

        when(mockQuoteDao.findById("MSFT")).thenReturn(Optional.of(mockQuote));
        when(mockPositionDao.findById("MSFT")).thenReturn(Optional.of(mockPosition));

        positionService.buy("MSFT", 2000, 400.0);

        verify(mockPositionDao, never()).save(mockPosition);
    }

    @Test(expected = RuntimeException.class)
    public void buyTest_NonExistentTicker(){
        Position mockPosition = new Position();
        mockPosition.setTicker("Non existent ticker");
        mockPosition.setNumOfShares(100);
        mockPosition.setValuePaid(40000);

        when(mockQuoteDao.findById("Non existent ticker")).thenReturn(Optional.empty());

        positionService.buy("Non existent ticker", 100, 400);

        verify(mockPositionDao, never()).save(mockPosition);
    }

    @Test
    public void sellTest1(){
        Position mockPosition = new Position();
        mockPosition.setTicker("AMD");
        mockPosition.setNumOfShares(150);
        mockPosition.setValuePaid(45000);

        when(mockPositionDao.findById("AMD")).thenReturn(Optional.of(mockPosition));

        positionService.sell("AMD");

        verify(mockPositionDao).deleteById("AMD");
    }

    @Test(expected = RuntimeException.class)
    public void sellTest2(){
        Position mockPosition = new Position();
        mockPosition.setTicker("Non existent symbol");
        mockPosition.setNumOfShares(100);
        mockPosition.setValuePaid(40000);

        when(mockPositionDao.findById("Non existent symbol")).thenReturn(Optional.empty());

        positionService.sell("Non existent symbol");

        verify(mockPositionDao, never()).deleteById("Non existent symbol");
    }
}