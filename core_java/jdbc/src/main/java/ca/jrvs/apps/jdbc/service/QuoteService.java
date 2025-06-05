package ca.jrvs.apps.jdbc.service;

import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.helpers.QuoteHttpHelper;
import ca.jrvs.apps.jdbc.models.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class QuoteService {

    private final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
        this.dao = dao;
        this.httpHelper = httpHelper;
    }

    /**
     * Fetches latest quote data from endpoint
     * @param ticker
     * @return Latest quote information or empty optional if ticker symbol not found
     */
    public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        logger.info("Fetching quote data from API...");
        Quote quote = httpHelper.fetchQuoteInfo(ticker);
        if(quote.getTicker() != null){
            dao.save(quote);
        } else {
            return Optional.empty();
        }
        return Optional.of(quote);
    }
}