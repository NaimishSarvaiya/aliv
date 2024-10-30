package com.iotsmartaliv.model.booking;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MetadataDeserializer implements JsonDeserializer<Map<String, String>> {

    @Override
    public Map<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<String, String> metadataMap = new HashMap<>();

        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                metadataMap.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
        return metadataMap;
    }
}