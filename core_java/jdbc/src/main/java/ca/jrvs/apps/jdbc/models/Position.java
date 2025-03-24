package ca.jrvs.apps.jdbc.models;

public class Position {

    private String ticker; //id
    private int numOfShares;
    private double valuePaid; //total amount paid for shares

    public String getTicker() {

        return ticker;
    }

    public void setTicker(String ticker) {

        this.ticker = ticker;
    }

    public int getNumOfShares() {

        return numOfShares;
    }

    public void setNumOfShares(int numOfShares) {

        this.numOfShares = numOfShares;
    }

    public double getValuePaid() {

        return valuePaid;
    }

    public void setValuePaid(double valuePaid) {

        this.valuePaid = valuePaid;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Position\n");
        stringBuilder.append("symbol: ").append(ticker).append("\n");
        stringBuilder.append("number of shares: ").append(numOfShares).append("\n");
        stringBuilder.append("value paid: ").append(valuePaid).append("\n");
        return stringBuilder.toString();
    }
}