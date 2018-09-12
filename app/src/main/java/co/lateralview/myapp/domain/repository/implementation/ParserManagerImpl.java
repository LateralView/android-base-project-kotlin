package co.lateralview.myapp.domain.repository.implementation;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import co.lateralview.myapp.domain.repository.interfaces.ParserManager;

public class ParserManagerImpl implements ParserManager {
    private Gson mGson;

    public ParserManagerImpl(Gson gson) {
        mGson = gson;
    }

    public String toJson(Object object) {
        return mGson.toJson(object);
    }

    public <T> T fromJson(String json, Class<T> type) {
        return mGson.fromJson(json, type);
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        return mGson.fromJson(json, type);
    }
}
