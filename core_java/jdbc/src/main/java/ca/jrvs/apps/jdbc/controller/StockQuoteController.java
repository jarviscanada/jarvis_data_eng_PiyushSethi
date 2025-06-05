package ca.jrvs.apps.jdbc.controller;

import ca.jrvs.apps.jdbc.models.Position;
import ca.jrvs.apps.jdbc.models.Quote;
import ca.jrvs.apps.jdbc.service.PositionService;
import ca.jrvs.apps.jdbc.service.QuoteService;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {
    private QuoteService quoteService;
    private PositionService positionService;
    public StockQuoteController(QuoteService quoteService, PositionService positionService) {
        this.quoteService = quoteService;
        this.positionService = positionService;
    }
    public void initClient(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Welcome to stock quote service. Please enter your choice: ");
            System.out.println("1. View position");
            System.out.println("2. View quote");
            System.out.println("3. Buy shares");
            System.out.println("4. Sell shares");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewPosition(scanner);
                    break;
                case 2:
                    viewQuote(scanner);
                    break;
                case 3:
                    buyShares(scanner);
                    break;
                case 4:
                    sellShares(scanner);
                    break;
                case 5:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
    private void viewPosition(Scanner scanner) {
        System.out.println("Please enter the stock ticker to view the position: ");
        String ticker = scanner.next();
        Position position = positionService.viewPos(ticker);
        System.out.println("Position: " + position);
    }

    private void viewQuote(Scanner scanner) {
        System.out.println("Please enter the stock ticker to view the quote: ");
        String ticker = scanner.next();
        Optional <Quote> quote = quoteService.fetchQuoteDataFromAPI(ticker);
        if (quote.isPresent()) {
            System.out.println("Quote: " + quote.get());
        } else {
            System.out.println("No quote found. This is an invalid input, please try again.");
        }
    }

    private void buyShares(Scanner scanner) {
        System.out.println("Please enter the stock ticker to purchase: ");
        String ticker = scanner.next();

        System.out.println("Please enter the number of shares to buy: ");
        int shares = scanner.nextInt();

        System.out.println("Please enter the price per share: ");
        double pricePerShare = scanner.nextDouble();

        try {
            Position position = positionService.buy(ticker, shares, pricePerShare);
            System.out.println("Successfully purchased " + position);
        } catch (Exception e) {
            System.out.println("Something went wrong while buying shares. Please try again." + e.getMessage());
        }
    }
    private void sellShares(Scanner scanner) {
        System.out.println("Please enter the stock ticker to sell: ");
        String ticker = scanner.next();
        try {
            positionService.sell(ticker);
            System.out.println("Successfully sold shares " + ticker);
        }
        catch (Exception e) {
            System.out.println("Something went wrong while selling shares. Please try again." + e.getMessage());
        }
    }
}
