package ca.jrvs.apps.jdbc.helpers;

import static ca.jrvs.apps.jdbc.helpers.JsonParser.toObjectFromJson;
import ca.jrvs.apps.jdbc.models.Quote;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelper {

    private String apiKey;
    private OkHttpClient client;
    private static Logger logger = LoggerFactory.getLogger(QuoteHttpHelper.class);

    /**
     * Fetch latest quote data from Alpha Vantage endpoint
     * @param symbol
     * @return Quote with latest data
     * @throws IllegalArgumentException - if no data was found for the given symbol
     */
    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        Request request = new Request.Builder()
                .url("https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="
                        + symbol + "&datatype=json")
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", "alpha-vantage.p.rapidapi.com")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String responseJson = response.body().string();
            Quote quote = toObjectFromJson(responseJson, Quote.class);
            quote.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
            logger.info(quote.toString());
            return quote;
        } catch (JsonMappingException e){
            logger.error(e.getMessage(), e);
        } catch (JsonProcessingException e){
            logger.error(e.getMessage(), e);
        } catch (IOException e){
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public QuoteHttpHelper(String apiKey, OkHttpClient client) {
        this.apiKey = apiKey;
        this.client = client;
    }

}