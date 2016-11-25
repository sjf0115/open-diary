package com.sjf.open.json.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * Created by xiaosi on 16-11-16.
 */
public class DateTimeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {

    public JsonElement serialize(DateTime dateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(dateTime.toString("yyyy-MM-dd"));
    }

    public DateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new DateTime(jsonElement.getAsJsonPrimitive().getAsString());
    }

}
