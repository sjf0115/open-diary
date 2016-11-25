package com.sjf.open.json.adapter;

import java.lang.reflect.Type;
import java.util.List;

import avro.shaded.com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Created by xiaosi on 16-11-16.
 */
public class ListAdapter implements JsonSerializer<List>, JsonDeserializer<List> {

    public List deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List list = Lists.newArrayList();

        return list;
    }

    public JsonElement serialize(List src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();

        return jsonArray;
    }

}
