package com.cardelf.json.schema.flat;

import com.cardelf.json.constant.JsonNodeConstants;
import com.cardelf.json.schema.JsonSchema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Json flator.
 *
 * @author bluecrush
 */
public class JsonFlator {
    /**
     * The Json schema.
     */
    private JsonSchema jsonSchema;

    /**
     * The Flat model.
     */
    private FlatModel flatModel;

    /**
     * Instantiates a new Json flator.
     *
     * @param jsonSchema the json schema
     */
    public JsonFlator(JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    /**
     * The Column info.
     */
    private List<JsonSchema> columnInfo;

    /**
     * Preprocess.
     */
    private void preprocess() {
        // 进行节点排序，获取叶子节点列表
    }


    private List<JsonSchema> orderLeafNode(JsonSchema root) {
        // 进行节点排序，获取叶子节点列表
        return jsonSchema.toJsonSchemaEntityList()
                .stream()
                .filter(node -> node
                        .getType()
                        .isBasicType())
                .sorted(Comparator.comparingInt(o -> o.getJsonPath().split(JsonNodeConstants.NODE_JSONPATH_ARRAY).length))
                .sorted(Comparator.comparingInt(o -> o.getType().getWeight()))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> flat(Object json) {
        List<Map<String, Object>> list = new ArrayList<>(128);
        this.columnInfo.forEach(jsonSchema -> {

        });
        return list;
    }


    /**
     * Gets column name.
     *
     * @param jsonSchema the json schema
     * @return the column name
     */
    private String getColumnName(JsonSchema jsonSchema) {
        if (flatModel == FlatModel.SIMPLE) {
            return jsonSchema.getTitle();
        } else {
            return jsonSchema.getId();
        }
    }

}
