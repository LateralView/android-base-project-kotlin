package co.lateralview.myapp.domain.repository.interfaces;

import java.lang.reflect.Type;

public interface ParserManager {
    String toJson(Object object);

    <T> T fromJson(String json, Class<T> type);

    <T> T fromJson(String json, Type type);
}
