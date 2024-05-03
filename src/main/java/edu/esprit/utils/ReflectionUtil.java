package edu.esprit.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    /**
     * Gets the field names for a given class of any object.
     * @param object An instance of any class to analyze.
     * @return A list of field names of the class of the given object.
     */
    public static List<String> getFieldNamesFromObject(Object object) {
        List<String> fieldNames = new ArrayList<>();
        Class<?> clazz = object.getClass();

        // Retrieve all fields from the class and its superclasses.
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // Optionally filter out fields based on modifiers or other criteria
                if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) { // Exclude static fields
                    fieldNames.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass(); // Move to the superclass to get all fields
        }
        return fieldNames;
    }

    /**
     * Gets the field names from a list of objects.
     * @param objects A list of objects from which to extract field names.
     * @return A list of field names, assumed all objects are of the same type.
     */
    public static List<String> getFieldNamesFromList(List<?> objects) {
        if (objects == null || objects.isEmpty()) {
            return new ArrayList<>();
        }
        return getFieldNamesFromObject(objects.get(0)); // Just use the first element to determine field names
    }

}
