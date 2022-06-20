package com.cardelf.json.schema.extract;

import com.cardelf.json.constant.JsonNodeConstants;
import com.cardelf.json.schema.JsonSchema;
import com.cardelf.json.schema.node.JsonNodeType;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JSON变量识别
 *
 * @author bluecrush
 */
public class JsonNodeVarExtractor {

    /**
     * JsonSchemaRegister注册器
     */
    private JsonSchema jsonSchema;

    /**
     * Instantiates a new Json node var extractor.
     *
     * @param jsonSchema the register
     */
    public JsonNodeVarExtractor(JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
    }


    /**
     * Auto variable recognition hash set.
     *
     * @return the hash set
     */
    public List<JsonSchema> autoVariableRecognition() {
        Map<String, JsonSchema> jsonSchemaEntities = this.jsonSchema.toJsonSchemaEntityMap();
        List<JsonSchema> allSchema = this.jsonSchema.toJsonSchemaEntityList();
        // 基本类型
        List<JsonSchema> base = allSchema
                .stream()
                // 可用末梢节点包含：required=null 并且节点类型非object和数组（主要考虑空数组和空对象）
                .filter(jsonSchemaEntity -> jsonSchemaEntity.getType().isBasicType()
                        && !jsonSchemaEntity.getJsonPath().contains(JsonNodeConstants.NODE_JSONPATH_ARRAY)
                )
                .collect(Collectors.toList());
        allSchema.removeAll(base);
        // 2.table类型节点（特殊的数组节点）
        HashSet<JsonSchema> table = allSchema
                .stream()
                // 可用末梢节点包含：required=null 并且节点类型非object和数组（主要考虑空数组和空对象）
                .filter(jsonSchemaEntity -> jsonSchemaEntity.getRequired() == null &&
                        jsonSchemaEntity.getTitle() != null &&
                        (jsonSchemaEntity.getType() != JsonNodeType.OBJECT &&
                                jsonSchemaEntity.getType() != JsonNodeType.ARRAY)
                )
                // 获取父级节点
                .map(jsonSchemaEntity -> jsonSchemaEntities.get(jsonSchemaEntity.getParentId()))
                // 过滤父节点中包含数组子节点的父节点
                .filter(jsonSchemaEntity -> {
                    Set<String> subNodeIds = jsonSchemaEntity.getSubNodeIds();
                    HashSet<Boolean> tmp = new HashSet<>(2);
                    subNodeIds.forEach(requiredId -> {
                        JsonNodeType type;
                        type = jsonSchemaEntities.get(requiredId).getType();
                        tmp.add((type == JsonNodeType.STRING) || (type == JsonNodeType.INTEGER) || (type == JsonNodeType.NUMBER));
                    });
                    boolean contains = jsonSchemaEntity.getJsonPath().contains(JsonNodeConstants.NODE_JSONPATH_ARRAY);
                    return tmp.size() == 1 && tmp.contains(Boolean.TRUE) && contains;
                })
                .map(schema -> {
                    schema = schema.copy();
                    schema.setType(JsonNodeType.TABLE);
                    Set<String> subNodeIds = schema.getSubNodeIds();
                    for (String subNodeId : subNodeIds) {
                        if (JsonNodeType.OBJECT == jsonSchemaEntities.get(subNodeId).getType()) {
                            String jsonPath = schema.getJsonPath() + JsonNodeConstants.NODE_JSONPATH_ARRAY;
                            schema.setJsonPath(jsonPath);
                            break;
                        }
                    }
                    return schema.nodeSlimming();
                })
                .collect(Collectors.toCollection(HashSet::new));

        // 3.筛选object节点
        List<JsonSchema> object = allSchema
                .stream()
                .filter(jsonSchemaEntity ->
                        jsonSchemaEntity.getType() == JsonNodeType.OBJECT
                                && jsonSchemaEntity.getJsonPath() != null
                                && !jsonSchemaEntity.getJsonPath().contains(JsonNodeConstants.NODE_JSONPATH_ARRAY)
                )
                .map(JsonSchema::nodeSlimming)
                .collect(Collectors.toList());

        // 数组
        LinkedHashSet<JsonSchema> array = allSchema
                .stream()
                .filter(jsonSchemaEntity ->
                        jsonSchemaEntity.getJsonPath() != null
                                && jsonSchemaEntity.getJsonPath().contains(JsonNodeConstants.NODE_JSONPATH_ARRAY) &&
                                !jsonSchemaEntity.getJsonPath().endsWith(JsonNodeConstants.NODE_JSONPATH_ARRAY + JsonNodeConstants.NODE_JSONPATH_SPLICE + JsonNodeConstants.NODE_ARRAY_ITEMS)
                                && !table.contains(jsonSchemaEntity)
                ).map(jsonSchema -> {
                    jsonSchema = jsonSchema.copy();
                    final String suffix = "[*]";
                    if (jsonSchema.getJsonPath().endsWith(suffix)) {
                        jsonSchema.setJsonPath(jsonSchema.getJsonPath().substring(0, jsonSchema.getJsonPath().lastIndexOf(suffix)));
                    }
                    jsonSchema.setType(JsonNodeType.ARRAY);
                    return jsonSchema.nodeSlimming();
                })
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // 4.合并
        base.addAll(table);
        base.addAll(object);
        base.addAll(array);
        return base;
    }
}
