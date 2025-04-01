package ca.jrvs.apps.jdbc.service;

import ca.jrvs.apps.jdbc.dao.PositionDao;
import ca.jrvs.apps.jdbc.dao.QuoteDao;
import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class PositionService {

    private final Logger logger = LoggerFactory.getLogger(PositionService.class);
    private PositionDao positionDao;
    private QuoteDao quoteDao;

    public PositionService(PositionDao positionDao, QuoteDao quoteDao) {
        this.positionDao = positionDao;
        this.quoteDao = quoteDao;
    }

    /**
     * Processes a buy order and updates the database accordingly
     * @param ticker
     * @param numberOfShares
     * @param price
     * @return The position in our database after processing the buy
     */
    public Position buy(String ticker, int numberOfShares, double price) {
        logger.info("Buy position for ticker: " + ticker);
        try {
            Optional<Quote> quote = quoteDao.findById(ticker);
            Optional<Position> op_position = positionDao.findById(ticker);
//            if (quote.isPresent())
//                if(quote.get().getVolume() < numberOfShares) {
//                    throw new IllegalArgumentException("The quote volume must be less than or equal to the number of shares");
//                }
//                else {
//                    throw new IllegalArgumentException("The stock ticker must be valid. Check the symbol");
//                }
            // Check if symbol is valid
            if (!quote.isPresent()){
                throw new IllegalArgumentException("Invalid stock symbol. Please try again.");
            }
            // Check if available volume is more than number of shares
            if (quote.get().getVolume() < numberOfShares) {
                throw new IllegalArgumentException("Insufficient volume or invalid number of shares ");
            }

//            Position position;
//            if (op_position.isPresent()) {
//                position = op_position.get();
//                position.setNumOfShares(op_position.get().getNumOfShares()+numberOfShares);
//                position.setValuePaid(op_position.get().getValuePaid()+ price *numberOfShares);
//            } else {
//                position = new Position();
//                position.setTicker(ticker.toUpperCase());
//                position.setNumOfShares(numberOfShares);
//                position.setValuePaid(price * numberOfShares);
//            }
//            positionDao.save(position);
//            return position;
//        } catch (Exception e){
//            logger.error("Error when trying to buy stock", e);
//            throw new RuntimeException("Exception when trying to buy stock", e);
//        }
            Position position;
            if (op_position.isPresent()) {
                position = op_position.get();
            } else {
                position = new Position();
            }
            position.setTicker(ticker);
            position.setNumOfShares(position.getNumOfShares() + numberOfShares);
            position.setValuePaid(position.getValuePaid() + price);
            positionDao.save(position);

            // Retrieve and return updated position
            Optional<Position> pos = positionDao.findById(ticker);
            return pos.get();
        } catch (Exception e) {
            logger.error("Error occurred while buying stock. ", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Sells all shares of the given ticker symbol
     * @param ticker
     */
    public void sell(String ticker) {
        logger.info("Sell position for ticker: " + ticker);
        try {
            Optional<Position> position = positionDao.findById(ticker);
            if (position.isPresent()) {
                positionDao.deleteById(ticker);
            } else {
                throw new IllegalArgumentException("The stock ticker must be valid. Check to see if you own " + ticker);
            }
        } catch (Exception e) {
            logger.error("Exception when trying to sell stock ", e);
            throw new RuntimeException("Exception when trying to sell stock ", e);
        }
    }

    /**
     *
     * @param ticker
     * @return The position on your portfolio
     */

    public Position viewPos(String ticker) {
        logger.info("View position for ticker: " + ticker);
        //return (List<Position>) positionDao.findAll();
        try {
            Optional<Position> position = positionDao.findById(ticker);
            if (position.isPresent()) {
                return position.get();
            }else throw new IllegalArgumentException("The stock ticker must be valid and owned by you" + ticker);
        }
        catch (Exception e){
            logger.error("Exception when trying to view stock", e);
            throw new RuntimeException(e);
        }
    }
}