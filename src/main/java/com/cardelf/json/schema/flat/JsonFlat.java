package com.cardelf.json.schema.flat;

import com.cardelf.json.exception.JsonFlatException;
import com.cardelf.json.schema.node.JsonNodeType;
import com.cardelf.json.schema.node.JsonNodeTypeUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.internal.ParseContextImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * The type Json flat.
 *
 * @author bluecrush
 */
public class JsonFlat {
    /**
     * PATH 模式拼接符
     */
    private static final String PATH_MODE_SPLICING = ".";
    /**
     * 初始化行宽
     */
    private static final int INIT_ROW_WIDTH = 128;
    /**
     * 打平时key的模式
     */
    private FlatModel flatModel = FlatModel.SIMPLE;
    /**
     * keyName
     */
    private Set<String> keys;

    private JsonFlat(FlatModel flatModel, Set<String> keys) {
        this.flatModel = flatModel;
        this.keys = keys;

    }


    private JsonFlat(Set<String> keys) {
        this.keys = keys;
    }

    /**
     * New instance json flat.
     *
     * @return the json flat
     */
    public static JsonFlat newInstance() {
        return jsonFlat = new JsonFlat(new HashSet<>(INIT_ROW_WIDTH));
    }

    /**
     * New instance json flat.
     *
     * @param flatModel the flat model
     * @return the json flat
     */
    public static JsonFlat newInstance(FlatModel flatModel) {
        return jsonFlat = new JsonFlat(flatModel, new HashSet<>(INIT_ROW_WIDTH));
    }

    /**
     * 获取key名称
     *
     * @return the keys
     */
    public static Set<String> getKeys() {
        return jsonFlat.keys;
    }

    /**
     * Flat list.
     *
     * @param jsonData the json data
     * @return the list
     */
    public static List<Map<String, Object>> flat(String jsonData) throws JsonFlatException {
        final ParseContextImpl parseContext = new ParseContextImpl();
        final DocumentContext parse = parseContext.parse(jsonData);
        Object json = parse.json();
        return jsonFlat.flat(json);
    }

    /**
     * Flat list.
     *
     * @param json the json
     * @return the list
     */
    public List<Map<String, Object>> flat(Object json) throws JsonFlatException {
        Map<String, Object> oldRow = new HashMap<>(INIT_ROW_WIDTH);
        Map<String, Object> newRow = new HashMap<>(INIT_ROW_WIDTH);
        String rootVar = "$";
        // 置空keyNameSet
        if (this.keys != null) {
            this.keys.clear();
        } else {
            this.keys = new HashSet<>(INIT_ROW_WIDTH);
        }
        // 初始化行高
        int initRowHeight = 256;
        List<Map<String, Object>> maps = new ArrayList<>(initRowHeight);
        // 判断传入的json的类型
        return getMaps(json, oldRow, newRow, rootVar, maps);
    }


    private List<Map<String, Object>> getMaps(Object json, Map<String, Object> oldRow, Map<String, Object> newRow, String rootVar, List<Map<String, Object>> maps) throws JsonFlatException {
        AtomicInteger arrayNodeCount = new AtomicInteger(0);
        Map<String, Object> lastRow = flat(json, newRow, oldRow, rootVar, maps, arrayNodeCount);
        if (arrayNodeCount.get() == 0) {
            maps.add(lastRow);
        }
        return maps;
    }

    /**
     * json排序 1：array 2：基本类型
     */
    private Map<String, Object> orderJson(Map<String, Object> jsonObject) throws RuntimeException, JsonFlatException {
        AtomicInteger brotherOfArrayNodeCount = new AtomicInteger(0);
        Map<String, Object> orderJson = new HashMap<>(32);
        jsonObject.entrySet()
                .stream()
                .sorted((o1, o2) -> {
                    Object value1 = o1.getValue();
                    Object value2 = o2.getValue();
                    JsonNodeType nodeType1 = JsonNodeTypeUtil.recognitionDataType(value1);
                    if (JsonNodeType.ARRAY == nodeType1) {
                        brotherOfArrayNodeCount.set(brotherOfArrayNodeCount.get() + 1);
                    }
                    // 首次检测类型是object时判断其内部是否有数组
                    if (JsonNodeType.OBJECT == nodeType1 && isInnerArray(JsonNodeTypeUtil.castMap(value1, String.class, Object.class))) {
                        nodeType1 = JsonNodeType.ARRAY;
                    }
                    JsonNodeType nodeType2 = JsonNodeTypeUtil.recognitionDataType(value2);
                    if (JsonNodeType.OBJECT == nodeType2 && isInnerArray(JsonNodeTypeUtil.castMap(value2, String.class, Object.class))) {
                        nodeType2 = JsonNodeType.ARRAY;
                    }
                    return nodeType1.getWeight() - nodeType2.getWeight();
                })
                .forEach(objectEntry -> orderJson.put(objectEntry.getKey(), objectEntry.getValue()));
        // 兄弟节点中存在多个数据
        if (brotherOfArrayNodeCount.get() > 1) {
            throw new JsonFlatException("Object数据中存在兄弟节点都是数组的情况，打平将会没有意义");
        }
        return orderJson;
    }

    private static JsonFlat jsonFlat = newInstance();

    /**
     * 是否存在内部数组
     */
    private boolean isInnerArray(Map<String, Object> jsonObject) {
        AtomicBoolean result = new AtomicBoolean(false);
        jsonObject.forEach((s, o) -> {
            JsonNodeType type = JsonNodeTypeUtil.recognitionDataType(o);
            if (JsonNodeType.ARRAY == type) {
                (JsonNodeTypeUtil.castList(o, Object.class)).forEach(o1 -> {
                    result.set(true);
                    return;
                });
            }
        });
        return result.get();
    }

    private Map<String, Object> flat(Object json, Map<String, Object> curRow, Map<String, Object> oldRow, String parentPath, List<Map<String, Object>> tempData, AtomicInteger arrayNodeCount) throws JsonFlatException {
        curRow.putAll(oldRow);
        if (json instanceof Map) {
            Map<String, Object> jsonObject = orderJson(JsonNodeTypeUtil.castMap(json, String.class, Object.class));
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                Object value = entry.getValue();
                String entryKey = entry.getKey();
                // 类型识别
                JsonNodeType type = JsonNodeTypeUtil.recognitionDataType(value);
                String subPath = (entryKey != null && entryKey.length() > 0) ? parentPath + PATH_MODE_SPLICING + entryKey : parentPath;
                switch (type) {
                    case OBJECT:
                    case ARRAY:
                        // 数组
                        curRow.putAll(flat(value, new HashMap<>(INIT_ROW_WIDTH), curRow, subPath, tempData, arrayNodeCount));
                        break;
                    default:
                        // 基本类型
                        String keyName;
                        if (flatModel == FlatModel.SIMPLE) {
                            keyName = (entryKey != null && entryKey.length() > 0) ? entryKey : parentPath.substring(parentPath.lastIndexOf(PATH_MODE_SPLICING) + 1);
                        } else {
                            keyName = subPath;
                        }
                        curRow.put(keyName, value);
                        // 添加keyName
                        if (this.keys != null) {
                            this.keys.add(keyName);
                        }
                        break;
                }
            }
        } else if (json instanceof List) {
            // 存在数组
            List<Object> jsonArray = JsonNodeTypeUtil.castList(json, Object.class);
            arrayNodeCount.set(arrayNodeCount.get() + 1);
            for (Object value : jsonArray) {
                JsonNodeType type = JsonNodeTypeUtil.recognitionDataType(value);
                String subPath = parentPath + "[*]";
                switch (type) {
                    // 对象
                    case OBJECT:
                        tempData.add(flat(value, new HashMap<>(INIT_ROW_WIDTH), curRow, subPath, tempData, arrayNodeCount));
                        if (isInnerArray(JsonNodeTypeUtil.castMap(value, String.class, Object.class))) {
                            tempData.remove(tempData.size() - 1);
                        }
                        break;
                    // 数组
                    case ARRAY:
                        curRow.putAll(flat(value, new HashMap<>(INIT_ROW_WIDTH), curRow, subPath, tempData, arrayNodeCount));
                        break;
                    // 基本类型
                    default:
                        // 基本类型数组当作来处理，如 [1,2,3],节点名取父级名称
                        Map<String, Object> item = new HashMap<>(1);
                        item.put("", value);
                        tempData.add(flat(item, new HashMap<>(INIT_ROW_WIDTH), curRow, subPath, tempData, arrayNodeCount));
                        if (isInnerArray(item)) {
                            tempData.remove(tempData.size() - 1);
                        }
                }
            }
        }
        return curRow;
    }
}
