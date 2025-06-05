package ca.jrvs.apps.jdbc.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.sql.Timestamp;
import java.util.Date;

//@JsonRootName("Global Quote")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol",
        "open",
        "high",
        "low",
        "price",
        "volume",
        "latest trading day",
        "previous close",
        "change",
        "change percent"
})

public class Quote {

    @JsonProperty("01. symbol")
    private String ticker;

    @JsonProperty("02. open")
    private double open;

    @JsonProperty("03. high")
    private double high;

    @JsonProperty("04. low")
    private double low;

    @JsonProperty("05. price")
    private double price;

    @JsonProperty("06. volume")
    private int volume;

    @JsonProperty("07. latest trading day")
    private Date latestTradingDay;

    @JsonProperty("08. previous close")
    private double previousClose;

    @JsonProperty("09. change")
    private double change;

    @JsonProperty("10. change percent")
    private String changePercent;

    private Timestamp timestamp; //time when the info was pulled

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Date getLatestTradingDay() {
        return latestTradingDay;
    }

    public void setLatestTradingDay(Date latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------------\n");
        stringBuilder.append("\"Global Quote\": {\n");
        stringBuilder.append("\"01. symbol\": \"").append(ticker).append("\",\n");
        stringBuilder.append("\"02. open\": \"").append(open).append("\",\n");
        stringBuilder.append("\"03. high\": \"").append(high).append("\",\n");
        stringBuilder.append("\"04. low\": \"").append(low).append("\",\n");
        stringBuilder.append("\"05. price\": \"").append(price).append("\",\n");
        stringBuilder.append("\"06. volume\": \"").append(volume).append("\",\n");
        stringBuilder.append("\"07. latest trading day\": \"").append(latestTradingDay).append("\",\n");
        stringBuilder.append("\"08. previous close\": \"").append(previousClose).append("\",\n");
        stringBuilder.append("\"09. change\": \"").append(change).append("\",\n");
        stringBuilder.append("\"10. change percent\": \"").append(changePercent).append("\"\n");
        stringBuilder.append("\"11. Timestamp\": \"").append(timestamp).append("\"\n");
        stringBuilder.append("------------------------\n");
        return stringBuilder.toString();
    }
}