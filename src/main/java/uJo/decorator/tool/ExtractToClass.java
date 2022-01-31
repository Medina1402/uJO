package uJo.decorator.tool;

import uJo.decorator.type.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Abraham Medina Carrillo <https://github.com/medina1402>
 */
public class ExtractToClass {
    /**
     *
     * @param clazz
     * @return
     */
    public static String TableName(Class<?> clazz) {
        return ((Table) clazz.getDeclaredAnnotations()[0]).value();
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static String[] ColumnsNames(Class<?> clazz) {
        List<String> names = new ArrayList<>();

        for (LinkedHashMap<String, LinkedHashMap<String, String>> hashmap: AnnotationsDeclared(clazz)) {
            names.add(hashmap.get("Column").get("value"));
        }

        String[] lisToArrayNames = new String[names.size()];
        lisToArrayNames = names.toArray(lisToArrayNames);
        return lisToArrayNames;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static LinkedHashMap<String, LinkedHashMap<String, String>> FieldsDeclared(Class<?> clazz) {
        LinkedHashMap<String, LinkedHashMap<String, String>> fields = new LinkedHashMap<>();

        for (LinkedHashMap<String, LinkedHashMap<String, String>> hashmap: AnnotationsDeclared(clazz)) {
            for (Map.Entry<String, LinkedHashMap<String, String>> item: hashmap.entrySet()) {
                if (!item.getKey().equalsIgnoreCase("Column")) {
                    item.getValue().put("type", item.getKey());
                    fields.put(hashmap.get("Column").get("value"), item.getValue());
                }
            }
        }

        return fields;
    }

    /**
     *
     * @param clazz
     * @return
     */
    public static ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> AnnotationsDeclared(Class<?> clazz) {
        ArrayList<LinkedHashMap<String, LinkedHashMap<String, String>>> data = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            LinkedHashMap<String, LinkedHashMap<String, String>> hashMap = new LinkedHashMap<>();

            for (Annotation annotation: field.getDeclaredAnnotations()) {
                String tempAnnotation = annotation.toString().split(annotation.annotationType().getSimpleName())[1];
                LinkedHashMap<String, String> hashMapProps = new LinkedHashMap<>();

                for(String values: tempAnnotation.substring(1, tempAnnotation.length()-1).split(",")) {
                    String[] value = values.split("=");
                    hashMapProps.put(value[0].trim(), value[1]);
                }

                hashMap.put(annotation.annotationType().getSimpleName(), hashMapProps);
            }

            data.add(hashMap);
        }

        return data;
    }
}
