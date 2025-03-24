package ca.jrvs.apps.jdbc.helpers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonParser {

    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
            throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        if (!includeNullValues) {
            m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if (prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return m.writeValueAsString(object);
    }

//    public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//
//        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
//        return (T) mapper.readValue(json, clazz);
//    }
    public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
        ObjectMapper m = new ObjectMapper();
        // Read the JSON and convert it into a tree of JsonNode objects
        JsonNode rootNode = m.readTree(json);
        // Extract the "Global Quote" root node and then map the tree to the clazz object
        JsonNode quoteNode = rootNode.get("Global Quote");
        return (T) m.treeToValue(quoteNode, clazz);
}
}
