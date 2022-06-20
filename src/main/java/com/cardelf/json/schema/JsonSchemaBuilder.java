package com.cardelf.json.schema;

import com.cardelf.json.constant.JsonNodeConstants;
import com.cardelf.json.exception.JsonStructureException;
import com.cardelf.json.schema.node.JsonNodeType;
import com.cardelf.json.schema.node.JsonNodeTypeUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.internal.ParseContextImpl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The type Json schema register.
 *
 * @author bluecrush
 */
public class JsonSchemaBuilder {

    private final ParseContextImpl parseContext = new ParseContextImpl();

    /**
     * Build json schema.
     *
     * @param jsonData the json data
     * @return the json schema
     * @throws JsonStructureException the json structure exception
     */
    public JsonSchema build(String jsonData) throws JsonStructureException {
        final DocumentContext parse = parseContext.parse(jsonData);
        Map<String, Object> json = parse.json();
        return build(json);
    }

    /**
     * Build json schema.
     *
     * @param jsonData the json data
     * @return the json schema
     * @throws JsonStructureException the json structure exception
     */
    public JsonSchema build(Object jsonData) throws JsonStructureException {
        if (jsonData instanceof Map) {
            // root节点
            JsonSchema jsonSchema = new JsonSchema();
            int depth = 0;
            jsonSchema.setDepth(0);
            jsonSchema.setId(JsonNodeConstants.NODE_ID_SPLICE);
            jsonSchema.setDescription("ROOT");
            jsonSchema.setTitle(JsonNodeConstants.NODE_JSONPATH_PREFIX);
            jsonSchema.setJsonPath(JsonNodeConstants.NODE_JSONPATH_PREFIX);
            jsonSchema.setJsonPath(JsonNodeConstants.NODE_JSONPATH_PREFIX);
            jsonSchema.setType(JsonNodeType.OBJECT);
            LinkedHashMap<String, JsonSchema> jsonSchemaEntities = new LinkedHashMap<>(32);
            jsonSchemaEntities.put(jsonSchema.getId(), jsonSchema);
            // 抽取json节点的元信息
            this.buildJsonSchema(JsonNodeTypeUtil.castMap(jsonData, String.class, Object.class), jsonSchema, jsonSchemaEntities);
            // 构建每个节点的子节点数据
            jsonSchemaEntities.values().forEach(schemaEntity -> {
                String parentId = schemaEntity.getParentId();
                if (parentId != null) {
                    JsonSchema parent = jsonSchemaEntities.get(parentId);
                    parent.addRequired(schemaEntity.getTitle());
                    parent.addSubNodeIds(schemaEntity.getId());
                }
            });
            // 构建jsonSchema树
            this.buildJsonSchemaTree(jsonSchemaEntities, jsonSchema);
            return jsonSchema;
        } else if (jsonData instanceof List) {
            throw new JsonStructureException("Unsupported JSON array structure");
        } else {
            throw new JsonStructureException("Not a JSON structure");
        }
    }

    /**
     * 生成schema集，分析json，生成schema集
     *
     * @param jsonObject jsonObject数据
     * @param parentNode 父节点
     * @throws JsonStructureException the json parse exception
     */
    private void buildJsonSchema(Map<String, Object> jsonObject, JsonSchema parentNode, LinkedHashMap<String, JsonSchema> jsonSchemaEntities) throws JsonStructureException {
        final Set<Map.Entry<String, Object>> set = jsonObject.entrySet();
        for (Map.Entry<String, Object> entry : set) {
            Object value = entry.getValue();
            String key = entry.getKey();
            // 类型识别
            JsonNodeType curType = JsonNodeTypeUtil.recognitionDataType(value);
            // 获取已存在的类型进行比较，取优先级最高的,即获取真实类型
            JsonSchema stubNode = this.buildSubNode(key, value, curType, parentNode);
            final JsonNodeType realType;
            if (parentNode.getSubNode(stubNode.getId()) != null) {
                final JsonNodeType hisType = parentNode.getSubNode(stubNode.getId()).getType();
                realType = JsonNodeTypeUtil.typePromote(hisType, curType);
            } else {
                realType = curType;
            }
            stubNode.setType(realType);
            switch (curType) {
                case OBJECT:
                    jsonSchemaEntities.put(stubNode.getId(), stubNode);
                    this.buildJsonSchema(JsonNodeTypeUtil.castMap(value, String.class, Object.class), stubNode, jsonSchemaEntities);
                    break;
                case ARRAY:
                    // 判断是不是
                    handleArray(JsonNodeTypeUtil.castList(value, Object.class), stubNode, jsonSchemaEntities);
                    break;
                default:
                    jsonSchemaEntities.put(stubNode.getId(), stubNode);
                    break;
            }
        }
    }

    /**
     * 处理数组
     *
     * @param value   the value
     * @param subNode the stub node
     * @throws JsonStructureException the json parse exception
     */
    private void handleArray(List<Object> value, JsonSchema subNode, LinkedHashMap<String, JsonSchema> jsonSchemaEntities) throws JsonStructureException {
        JsonNodeType type;
        for (Object o : value) {
            jsonSchemaEntities.put(subNode.getId(), subNode);
            type = JsonNodeTypeUtil.recognitionDataType(o);
            switch (type) {
                case OBJECT:
                    this.buildJsonSchema(JsonNodeTypeUtil.castMap(o, String.class, Object.class), subNode, jsonSchemaEntities);
                    break;
                case ARRAY:
                    subNode = this.buildSubNode(null, value, type, subNode);
                    handleArray(JsonNodeTypeUtil.castList(o, Object.class), subNode, jsonSchemaEntities);
                    break;
                default:
                    Map<String, Object> newObject = new HashMap<>(8);
                    newObject.put(null, o);
                    this.buildJsonSchema(newObject, subNode, jsonSchemaEntities);
                    break;
            }
        }
    }

    /**
     * 构建末梢节点
     *
     * @param key        节点key
     * @param value      节点value
     * @param type       节点类型
     * @param parentNode 父级节点
     * @return JsonSchemaEntity实体对象 json schema entity
     */
    private JsonSchema buildSubNode(String key, Object value, JsonNodeType type, JsonSchema parentNode) {
        JsonSchema stubJsonSchema = new JsonSchema();
        // 默认ID
        String id = getId(key, parentNode);
        stubJsonSchema.setId(id);
        stubJsonSchema.setType(type);
        String jsonPath = getJsonPath(key, type, parentNode);
        stubJsonSchema.setJsonPath(jsonPath);
        stubJsonSchema.setTitle(key);
        stubJsonSchema.setExamples(value);
        // 设置描述信息（非末梢节点不设置）
        if (type.isBasicType()) {
            stubJsonSchema.setDescription(value.toString());
        } else {
            stubJsonSchema.setDescription(key + " node");
        }
        stubJsonSchema.setParentId(parentNode.getId());
        return stubJsonSchema;
    }

    /**
     * Gets json path.
     *
     * @param key        the key
     * @param type       the type
     * @param parentNode the parent node
     * @return the json path
     */
    private String getJsonPath(String key, JsonNodeType type, JsonSchema parentNode) {
        String jsonPath;
        if (JsonNodeType.ARRAY == type) {
            jsonPath = parentNode.getJsonPath() + JsonNodeConstants.NODE_JSONPATH_SPLICE + (key == null ? "" : key) + JsonNodeConstants.NODE_JSONPATH_ARRAY;
        } else {
            jsonPath = parentNode.getJsonPath() + (key == null ? "" : JsonNodeConstants.NODE_JSONPATH_SPLICE + key);
        }
        return jsonPath;
    }

    /**
     * Gets id.
     *
     * @param key        the key
     * @param parentNode the parent node
     * @return the id
     */
    private String getId(String key, JsonSchema parentNode) {
        if (JsonNodeConstants.NODE_ID_SPLICE.equals(parentNode.getId())) {
            return parentNode.getId() + (key == null ? JsonNodeConstants.NODE_ARRAY_ITEMS : key);
        } else {
            return parentNode.getId() + JsonNodeConstants.NODE_ID_SPLICE + (key == null ? JsonNodeConstants.NODE_ARRAY_ITEMS : key);
        }
    }


    /**
     * 构建schema树
     *
     * @param source     源
     * @param parentNode 父节点
     */
    private void buildJsonSchemaTree(Map<String, JsonSchema> source, JsonSchema parentNode) {
        Map<String, JsonSchema> copy = new HashMap<>(source);
        Iterator<Map.Entry<String, JsonSchema>> iterator = copy.entrySet().iterator();
        JsonNodeType parentType = parentNode.getType();
        if (JsonNodeType.ARRAY == parentType) {
            JsonSchema items = new JsonSchema();
            items.setTitle(JsonNodeConstants.NODE_ARRAY_ITEMS);
            items.setRequired(parentNode.getRequired());
            items.setId(getId(JsonNodeConstants.NODE_ARRAY_ITEMS, parentNode));
            items.setDepth(parentNode.getDepth() + 1);
            parentNode.setItems(items);
            parentNode.setRequired(null);
        }
        while (iterator.hasNext()) {
            JsonSchema currentNode = iterator.next().getValue();
            if (currentNode.getParentId() != null && currentNode.getParentId().equals(parentNode.getId())) {
                currentNode.setDepth(parentNode.getDepth() + 1);
                // 数组节点特殊处理，设置Items
                if (JsonNodeType.ARRAY == parentType) {
                    if (currentNode.getTitle() != null) {
                        JsonSchema items = parentNode.getItems();
                        items.setType(JsonNodeType.OBJECT);
                        items.addProperties(currentNode);
                    } else {
                        parentNode.setItems(currentNode);
                    }
                } else {
                    parentNode.addProperties(currentNode);
                }
                // 减少查询次数
                iterator.remove();
                this.buildJsonSchemaTree(copy, currentNode);
            }
        }
    }

}