package ru.akvine.fitstats.utils;

import com.google.common.base.Preconditions;
import ru.akvine.fitstats.exceptions.util.ReflectionException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
    public static <T> List<String> getFieldNames(Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "class is null");
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }

    public static <T> List<String> getFieldsValues(T object) {
        Preconditions.checkNotNull(object, "object is null");
        List<String> values = new ArrayList<>();

        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                values.add(value == null ? null : String.valueOf(value));
            } catch (IllegalAccessException e) {
                throw new ReflectionException("Can't get fields values from object. Reflection error = Illegal Access");
            }
        }

        return values;
    }
}
