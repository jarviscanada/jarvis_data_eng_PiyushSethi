package ca.jrvs.apps.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import ca.jrvs.apps.jdbc.models.Quote;

public class QuoteDao implements CrudDao<Quote, String> {
    private static Logger log = LoggerFactory.getLogger(QuoteDao.class);
    private Connection c;

    private static final String GET_BY_ID = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote WHERE symbol = ?";
    private static final String INSERT = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        /*"ON CONFLICT (ticker) DO UPDATE SET open = EXCLUDED.open, high = EXCLUDED.high, low = EXCLUDED.low, price = EXCLUDED.price, volume = EXCLUDED.volume, latest_trading_day = EXCLUDED.latest_trading_day, previous_close = EXCLUDED.previous_close, change = EXCLUDED.change, change_percent = EXCLUDED.change_percent, timestamp = EXCLUDED.timestamp;"*/
     private static final String GET_ALL = "SELECT symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp FROM quote";
     private static final String UPDATE = "UPDATE quote set open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
    private static final String DELETE_BY_ID = "DELETE FROM quote WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM quote";

    public QuoteDao(Connection c) {
        this.c = c;
    }

    @Override
    public Quote save(Quote entity) {
//    if (findById(entity.getTicker()).isPresent()){
//        update(entity);
//    } else {
//        create(entity);
//    }
//    return entity;
//    }
      if(entity == null) {
      log.warn("Null entity passed to save()");
      throw new IllegalArgumentException("Cannot pass null entity to save()");
      }
      return findById(entity.getTicker()).isPresent() ? update(entity) : create(entity);
    }

    private Quote create(Quote entity) {
        log.info("Creating quote {}", entity);
        try(PreparedStatement statement = this.c.prepareStatement(INSERT)) {
        statement.setString(1, entity.getTicker());
        statement.setDouble(2, entity.getOpen());
        statement.setDouble(3, entity.getHigh());
        statement.setDouble(4, entity.getLow());
        statement.setDouble(5, entity.getPrice());
        statement.setInt(6, entity.getVolume());
        statement.setDate(7, new java.sql.Date(entity.getLatestTradingDay().getTime()));
        statement.setDouble(8, entity.getPreviousClose());
        statement.setDouble(9, entity.getChange());
        statement.setString(10, entity.getChangePercent());
        statement.setTimestamp(11, new java.sql.Timestamp(entity.getTimestamp().getTime()));
        statement.execute();
        return entity;
        }catch (SQLException e){
            log.error("Error creating quote",e);
            throw new RuntimeException("Error creating Quote",e);
        }
    }

    private Quote update(Quote entity) {
    try(PreparedStatement statement = this.c.prepareStatement(UPDATE)) {
    statement.setDouble(1, entity.getOpen());
    statement.setDouble(2, entity.getHigh());
    statement.setDouble(3, entity.getLow());
    statement.setDouble(4, entity.getPrice());
    statement.setInt(5, entity.getVolume());
    statement.setDate(6, new java.sql.Date(entity.getLatestTradingDay().getTime()));
    statement.setDouble(7, entity.getPreviousClose());
    statement.setDouble(8, entity.getChange());
    statement.setString(9, entity.getChangePercent());
    statement.setTimestamp(10, new java.sql.Timestamp(entity.getTimestamp().getTime()));
    statement.setString(11, entity.getTicker());
    statement.execute();
    return entity;
    }catch (SQLException e){
        throw new RuntimeException("Error updating Quote",e);
    }
    }

    @Override
    public Optional<Quote> findById(String s) throws IllegalArgumentException {
        Quote quote = null;
        try(PreparedStatement statement = this.c.prepareStatement(GET_BY_ID)) {
        statement.setString(1, s);
        try(ResultSet resultSet = statement.executeQuery()) {
            while(resultSet.next()) {
                quote = new Quote();
                quote.setTicker(resultSet.getString("symbol"));
                quote.setOpen(resultSet.getDouble("open"));
                quote.setHigh(resultSet.getDouble("high"));
                quote.setLow(resultSet.getDouble("low"));
                quote.setPrice(resultSet.getDouble("price"));
                quote.setVolume(resultSet.getInt("volume"));
                quote.setLatestTradingDay(resultSet.getDate("latest_trading_day"));
                quote.setPreviousClose(resultSet.getDouble("previous_close"));
                quote.setChange(resultSet.getDouble("change"));
                quote.setChangePercent(resultSet.getString("change_percent"));
                quote.setTimestamp(resultSet.getTimestamp("timestamp"));
            }
        }
        } catch (SQLException e){
            log.error("Error getting quote by id",e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(quote);
    }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        log.info("Getting all quotes");
        try(PreparedStatement statement = this.c.prepareStatement(GET_ALL)) {
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            Quote quote = new Quote();
            quote.setTicker(resultSet.getString("symbol"));
            quote.setOpen(resultSet.getDouble("open"));
            quote.setHigh(resultSet.getDouble("high"));
            quote.setLow(resultSet.getDouble("low"));
            quote.setPrice(resultSet.getDouble("price"));
            quote.setVolume(resultSet.getInt("volume"));
            quote.setLatestTradingDay(resultSet.getDate("latest_trading_day"));
            quote.setPreviousClose(resultSet.getDouble("previous_close"));
            quote.setChange(resultSet.getDouble("change"));
            quote.setChangePercent(resultSet.getString("change_percent"));
            quote.setTimestamp(resultSet.getTimestamp("timestamp"));
            quotes.add(quote);
        }
        } catch (SQLException e) {
            log.error("Error getting all quotes",e);
            throw new RuntimeException(e);
        }
        return quotes;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_BY_ID)) {
        statement.setString(1, s);
        statement.execute();
    } catch (SQLException e){
        throw new RuntimeException("Error deleting quote by id",e);
    }
    }

    @Override
    public void deleteAll() {
        log.info("Deleting all quotes");
    try(PreparedStatement statement = this.c.prepareStatement(DELETE_ALL)) {
        statement.execute();
    } catch (SQLException e){
        log.error("Error getting all quotes",e);
        throw new RuntimeException("Error deleting all quotes",e);
    }
    }

//    private boolean exists(String ticker) {
//        return findById(ticker).isPresent();
//    }
    public Connection getC() {
        return c;
    }

    public void setC(Connection c) {
        this.c = c;
    }
}
