package co.lateralview.myapp.domain.repository.interfaces;

import java.lang.reflect.Type;

public interface SharedPreferencesManager {

    boolean saveBlocking(String key, String value);

    String getString(String key);

    String getString(String key, String defaultValue);

    int getInt(String key, int defaultValue);

    <T> T get(String key, Class<T> type);

    <T> T get(String key, Type type);

    void clear();
}
