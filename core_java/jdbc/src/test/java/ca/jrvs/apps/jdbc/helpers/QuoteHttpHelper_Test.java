package ca.jrvs.apps.jdbc.helpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.jdbc.models.Quote;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QuoteHttpHelper_Test {

    private QuoteHttpHelper quoteHttpHelper;
    @Mock
    private OkHttpClient mockClient;
    @Mock
    private Response mockResponse;
    @Mock
    private ResponseBody mockBody;
    @Mock
    private Call mockCall;

    @Before
    public void setup(){
        quoteHttpHelper = new QuoteHttpHelper("test-api-key", mockClient);
    }

    @Test
    public void fetchQuoteInfo() throws IllegalArgumentException, IOException {

        String mockJson = "{\n" +
                "    \"Global Quote\": {\n"+
                "        \"01. symbol\": \"MSFT\",\n"+
                "        \"02. open\": \"500.25\",\n"+
                "        \"03. high\": \"535.2\",\n"+
                "        \"04. low\": \"473.5\",\n"+
                "        \"05. price\": \"510.50\",\n"+
                "        \"06. volume\": \"7482494\",\n"+
                "        \"07. latest trading day\": \"2025-02-21\",\n"+
                "        \"08. previous close\": \"484.50\",\n"+
                "        \"09. change\": \"26\",\n"+
                "        \"10. change percent\": \"5.3719%\"\n"+
                "    }\n"+
                "}";

        when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);
        when(mockResponse.body()).thenReturn(mockBody);
        when(mockBody.string()).thenReturn(mockJson);

        Quote quote = quoteHttpHelper.fetchQuoteInfo("MSFT");

        assertNotNull(quote);
        assertEquals("MSFT", quote.getTicker());
        assertEquals(500.25, quote.getOpen(), 0.01);
        assertEquals(535.2, quote.getHigh(), 0.01);

    }
}