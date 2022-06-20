package com.cardelf.json.schema.node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Variable type util.
 *
 * @author bluecrush
 */
public class JsonNodeTypeUtil {

    /**
     * Recognition data type variable type.
     *
     * @param object the object
     * @return the variable type
     */
    public static JsonNodeType recognitionDataType(Object object) {
        if (object == null) {
            return JsonNodeType.NULL;
        } else if (object instanceof Integer) {
            return JsonNodeType.INTEGER;
        } else if (object instanceof Long) {
            return JsonNodeType.LONG;
        } else if (object instanceof Number) {
            return JsonNodeType.NUMBER;
        } else if (object instanceof Boolean) {
            return JsonNodeType.BOOLEAN;
        } else if (object instanceof String) {
            return JsonNodeType.STRING;
        } else if (object instanceof List) {
            return JsonNodeType.ARRAY;
        } else {
            return JsonNodeType.OBJECT;
        }
    }


    /**
     * Type promote variable type.
     *
     * @param hisType the history JsonNodeType
     * @param curType the current JsonNodeType
     * @return the variable type
     */
    public static JsonNodeType typePromote(JsonNodeType hisType, JsonNodeType curType) {
        if (hisType == null) {
            hisType = JsonNodeType.NULL;
        }
        if (curType == null) {
            curType = JsonNodeType.NULL;
        }
        final int hisTypeWeight = hisType.getWeight();
        final int curTypeWeight = curType.getWeight();
        return hisTypeWeight > curTypeWeight ? hisType : curType;
    }

    /**
     * Cast list.
     *
     * @param <T>   the type parameter
     * @param obj   the obj
     * @param clazz the clazz
     * @return the list
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        } else {
            throw new RuntimeException("Object '" + obj + "' Failed to convert list");
        }
    }


    /**
     * Cast map.
     *
     * @param <K>    the type parameter
     * @param <V>    the type parameter
     * @param obj    the obj
     * @param kClass the k class
     * @param vClass the v class
     * @return the map
     */
    public static <K, V> Map<K, V> castMap(Object obj, Class<K> kClass, Class<V> vClass) {
        Map<K, V> result = new LinkedHashMap<>();
        if (obj instanceof Map<?, ?>) {
            final Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                result.put(kClass.cast(entry.getKey()), vClass.cast(entry.getValue()));
            }
            return result;
        } else {
            throw new RuntimeException("Object '" + obj + "' Failed to convert Map");
        }
    }
}
