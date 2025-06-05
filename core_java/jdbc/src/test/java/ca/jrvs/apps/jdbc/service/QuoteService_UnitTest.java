package ca.jrvs.apps.jdbc.service;

import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.helpers.QuoteHttpHelper;
import ca.jrvs.apps.jdbc.models.Quote;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QuoteService_UnitTest {
    private QuoteService quoteService;
    @Mock
    private QuoteDao mockQuoteDao;
    @Mock
    private QuoteHttpHelper mockQuoteHttpHelper;

    @Before
    public void setup() {
        quoteService = new QuoteService(mockQuoteDao, mockQuoteHttpHelper);
    }

    @Test
    public void getQuote() {
        Quote mockQuote = new Quote();
        mockQuote.setTicker("WFC");
        mockQuote.setOpen(500);

        when(mockQuoteHttpHelper.fetchQuoteInfo("WFC")).thenReturn(mockQuote);
        when(mockQuoteDao.save(mockQuote)).thenReturn(mockQuote);

        Optional <Quote> testQuote = quoteService.fetchQuoteDataFromAPI(mockQuote.getTicker());

        assertTrue(testQuote.isPresent());
        assertEquals(500, testQuote.get().getOpen(),0.01);
    }

    @Test
    public void fetchQuoteDataFromAPI() {
        Quote mockQuote = new Quote();
        mockQuote.setTicker(null);

        when(mockQuoteHttpHelper.fetchQuoteInfo("Invalid Symbol")).thenReturn(mockQuote);

        Optional<Quote> testQuote = quoteService.fetchQuoteDataFromAPI("Invalid Symbol");

        assertFalse(testQuote.isPresent());
        verify(mockQuoteDao, never()).save(mockQuote);
    }
}
