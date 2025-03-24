package ca.jrvs.apps.jdbc.dao;

import ca.jrvs.apps.jdbc.models.Position;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionDao implements CrudDao<Position, String>{
    private static Logger logger = LoggerFactory.getLogger(PositionDao.class);
    private final Connection connection;
    private static final String INSERT = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?)";
    private static final String GET_ONE = "SELECT symbol, number_of_shares, value_paid FROM position WHERE symbol = ?";
    private static final String Get_ALL = "SELECT symbol, number_of_shares, value_paid FROM position";
    private static final String DELETE_ONE = "DELETE FROM position WHERE symbol = ?";
    private static final String DELETE_ALL = "DELETE FROM position";
    private static final String UPDATE = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";

    public PositionDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        if (entity == null) {
            logger.warn("Null position being saved in PositionDao");
            throw new IllegalArgumentException("entity cannot be null");
        }
        return findById(entity.getTicker()).isPresent() ? update(entity) : create(entity);
    }

    private Position create(Position entity) {
    logger.info("Creating new position ");
    try(PreparedStatement statement = this.connection.prepareStatement(INSERT)) {
        statement.setString(1, entity.getTicker());
        statement.setInt(2, entity.getNumOfShares());
        statement.setDouble(3, entity.getValuePaid());
        statement.execute();
        //logger.info("Successfully created new position: {}", entity.getTicker());
        return entity;
    } catch (SQLException e){
        logger.error("Error creating new position ", e);
        throw new RuntimeException(e);
    }
    }

    private Position update(Position entity) {
        logger.info("Updating existing position: {}", entity.getTicker());
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE)) {
            statement.setInt(1, entity.getNumOfShares());
            statement.setDouble(2, entity.getValuePaid());
            statement.setString(3, entity.getTicker());
            statement.execute();
            //logger.info("Successfully updated existing position: {}", entity.getTicker());
            return entity;
        }catch (SQLException e){
            logger.error("Error updating existing position ", e);
            throw new RuntimeException("Error updating position", e);
        }
    }

    @Override
    public Optional<Position> findById(String id) throws IllegalArgumentException {
        //s = s.toUpperCase();
        logger.info("Finding position by id: {}", id);
        Position position = null;
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE)) {
        statement.setString(1, id);
        ResultSet rs = statement.executeQuery();
        if(rs.next()) {
            position = new Position();
            position.setTicker(rs.getString("symbol"));
            position.setNumOfShares(rs.getInt("number_of_shares"));
            position.setValuePaid(rs.getDouble("value_paid"));
            //logger.info("Successfully found position: {}", position);
            return Optional.of(position);
        }
//        else {
//            logger.info("No position found for id: {}", id);}
        }
        catch (SQLException e){
            logger.error("Error finding position by id", e);
            throw new RuntimeException("Error finding position", e);
        }
        return Optional.ofNullable(position);
    }

    @Override
    public Iterable<Position> findAll() {
        logger.info("Finding all positions");
        List<Position> positionList = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(Get_ALL)) {
        ResultSet rs = statement.executeQuery();
        while(rs.next()) {
            Position position = new Position();
            position.setTicker(rs.getString("symbol"));
            position.setNumOfShares(rs.getInt("number_of_shares"));
            position.setValuePaid(rs.getDouble("value_paid"));
            positionList.add(position);
        }
        }catch (SQLException e){
            logger.error("Error finding all positions", e);
            throw new RuntimeException("Error finding positions", e);
        }
        return positionList;
    }

    @Override
    public void deleteById(String s) throws IllegalArgumentException {
        logger.info("Deleting position by id: {}", s);
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_ONE)) {
        statement.setString(1, s);
        statement.execute();
        logger.info("Successfully deleted position: {}", s);
        } catch (SQLException e){
            logger.error("Error deleting position by id", e);
            throw new RuntimeException("Error deleting position", e);
        }
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all positions");
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE_ALL)) {
            statement.execute();
            logger.info("Successfully deleted all positions");
        }catch (SQLException e){
            logger.error("Error deleting all positions", e);
            throw new RuntimeException("Error deleting positions", e);
        }
    }

//    public boolean existsByTicker(String ticker) {
//        return findById(ticker).isPresent();
//    }
}

