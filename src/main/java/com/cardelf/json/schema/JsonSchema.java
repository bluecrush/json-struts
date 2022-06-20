package com.cardelf.json.schema;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cardelf.json.constant.JsonNodeConstants;
import com.cardelf.json.schema.node.JsonNodeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Json schema entity.
 *
 * @author bluecrush
 */
public class JsonSchema {
    /**
     * 节点ID
     */
    @JSONField(serialize = false)
    private String id;

    /**
     * 节点名称
     */
    @JSONField(ordinal = 1)
    private String title;

    /**
     * 节点类型
     */
    @JSONField(ordinal = 2, serialzeFeatures = SerializerFeature.WriteEnumUsingToString)
    private JsonNodeType type;

    /**
     * 节点描述
     */
    @JSONField(ordinal = 3)
    private String description;

    /**
     * 校验正则，只有末梢节点才会有pattern
     */
    @JSONField(ordinal = 4)
    private String pattern;

    /**
     * 子节点
     */
    @JSONField(serialize = false)
    private Set<String> subNodeIds;

    /**
     * 子节点
     */
    @JSONField(ordinal = 5)
    private HashSet<String> required;


    /**
     * 父节点ID
     */
    @JSONField(serialize = false)
    private String parentId;

    /**
     * 缺省值
     */
    private String defaultValue;

    /**
     * jsonPath路径
     */
    @JSONField(serialize = false)
    private String jsonPath;

    /**
     * 当前节点所处的深度，跟节点是0
     */
    @JSONField(serialize = false)
    private int depth;

    /**
     * 数据样例
     */
    @JSONField(serialize = false)
    private Object examples;

    /**
     * 子节点
     */
    @JSONField(ordinal = 6)
    private HashMap<String, JsonSchema> properties;

    /**
     * 非属性节点（数组类属性）
     */
    @JSONField(ordinal = 7)
    private JsonSchema items;

    /**
     * Instantiates a new Json schema entity.
     */
    public JsonSchema() {
    }


    /**
     * Instantiates a new Json schema entity.
     *
     * @param id           the id
     * @param title        the title
     * @param type         the type
     * @param description  the description
     * @param pattern      the pattern
     * @param subNodeIds   the sub node ids
     * @param parentId     the parent id
     * @param defaultValue the default value
     * @param jsonPath     the json path
     * @param examples     the examples
     */
    public JsonSchema(String id, String title, JsonNodeType type, String description, String pattern, Set<String> subNodeIds, String parentId
            , String defaultValue, String jsonPath, Object examples) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.description = description;
        this.pattern = pattern;
        this.subNodeIds = subNodeIds;
        this.parentId = parentId;
        this.defaultValue = defaultValue;
        this.jsonPath = jsonPath;
        this.examples = examples;
    }

    /**
     * 对象拷贝
     *
     * @return json schema entity
     */
    public JsonSchema copy() {
        return getJsonSchemaEntity();
    }

    /**
     * Gets json schema entity.
     *
     * @return the json schema entity
     */
    private JsonSchema getJsonSchemaEntity() {
        JsonSchema newSchema = new JsonSchema();
        newSchema.setId(this.id);
        newSchema.setType(this.type);
        newSchema.setTitle(this.title);
        newSchema.setJsonPath(this.jsonPath);
        newSchema.setParentId(this.parentId);
        newSchema.setDescription(this.description);
        newSchema.setExamples(this.examples);
        newSchema.setDefaultValue(this.defaultValue);
        newSchema.setSubNodeIds(this.subNodeIds);
        newSchema.setProperties(this.properties);
        return newSchema;
    }

    /**
     * Gets required.
     *
     * @return the required
     */
    public HashSet<String> getRequired() {
        return this.required;
    }

    /**
     * Sets required.
     *
     * @param required the required
     */
    public void setRequired(HashSet<String> required) {
        this.required = required;
    }

    /**
     * Add required.
     *
     * @param required the required
     */
    public void addRequired(String required) {
        if (required == null) {
            return;
        }
        if (this.required == null) {
            this.required = new LinkedHashSet<>();
        }
        this.required.add(required);
    }

    /**
     * Add sub node ids.
     *
     * @param subNodeIds the sub node ids
     */
    public void addSubNodeIds(String subNodeIds) {
        if (this.subNodeIds == null) {
            this.subNodeIds = new LinkedHashSet<>();
        }
        this.subNodeIds.add(subNodeIds);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public JsonNodeType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(JsonNodeType type) {
        this.type = type;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets sub node ids.
     *
     * @return the sub node ids
     */
    public Set<String> getSubNodeIds() {
        return subNodeIds;
    }

    /**
     * Sets sub node ids.
     *
     * @param subNodeIds the sub node ids
     */
    public void setSubNodeIds(Set<String> subNodeIds) {
        this.subNodeIds = subNodeIds;
    }

    /**
     * Gets parent id.
     *
     * @return the parent id
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * Sets parent id.
     *
     * @param parentId the parent id
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Gets default value.
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets default value.
     *
     * @param defaultValue the default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets examples.
     *
     * @return the examples
     */
    public Object getExamples() {
        return examples;
    }

    /**
     * Sets examples.
     *
     * @param examples the examples
     */
    public void setExamples(Object examples) {
        this.examples = examples;
    }

    /**
     * Gets pattern.
     *
     * @return the pattern
     */
    public String getPattern() {
        // 设置默认正则（只有string类型进行设置）
        if (JsonNodeType.STRING == type) {
            return JsonNodeConstants.NODE_PATTEN_STRING;
        }
        return null;
    }

    /**
     * Sets pattern.
     *
     * @param pattern the pattern
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Gets json path.
     *
     * @return the json path
     */
    public String getJsonPath() {
        return jsonPath;
    }

    /**
     * Sets json path.
     *
     * @param jsonPath the json path
     */
    public void setJsonPath(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    /**
     * Gets properties.
     *
     * @return the properties
     */
    public HashMap<String, JsonSchema> getProperties() {
        return properties;
    }

    /**
     * Sets properties.
     *
     * @param properties the properties
     */
    public void setProperties(HashMap<String, JsonSchema> properties) {
        this.properties = properties;
    }

    /**
     * 添加属性节点
     *
     * @param subNodeSchema the sub node schema
     */
    public void addProperties(JsonSchema subNodeSchema) {
        if (this.properties == null) {
            this.properties = new LinkedHashMap<>();
        }
        this.properties.put(subNodeSchema.title, subNodeSchema);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JsonSchema that = (JsonSchema) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Gets items.
     *
     * @return the items
     */
    public JsonSchema getItems() {
        return items;
    }

    /**
     * Sets items.
     *
     * @param items the items
     */
    public void setItems(JsonSchema items) {
        this.items = items;
    }

    /**
     * To json schema entity list.
     *
     * @return the list
     */
    public List<JsonSchema> toJsonSchemaEntityList() {
        final Map<String, JsonSchema> schemaEntityHashMap = new HashMap<>(20);
        convertList(this, schemaEntityHashMap);
        return new ArrayList<>(schemaEntityHashMap.values());
    }

    /**
     * To json schema entity map map.
     *
     * @return the map
     */
    public Map<String, JsonSchema> toJsonSchemaEntityMap() {
        final Map<String, JsonSchema> schemaEntityHashMap = new HashMap<>(20);
        convertList(this, schemaEntityHashMap);
        return schemaEntityHashMap;
    }

    /**
     * Gets json schema entity ids.
     *
     * @return the json schema entity ids
     */
    @JSONField(serialize = false)
    public List<String> getJsonSchemaEntityIds() {
        final Map<String, JsonSchema> schemaEntityHashMap = new HashMap<>(20);
        convertList(this, schemaEntityHashMap);
        return new ArrayList<>(schemaEntityHashMap.keySet());
    }

    /**
     * To leaf json schema entity list.
     *
     * @return the list
     */
    public List<JsonSchema> toLeafJsonSchemaEntityList() {
        return this.toJsonSchemaEntityList()
                .stream()
                .filter(jsonSchemaEntity ->
                        jsonSchemaEntity
                                .getType()
                                .isBasicType())
                .collect(Collectors.toList());
    }

    /**
     * Convert list.
     *
     * @param jsonSchema          the json schema
     * @param jsonSchemaEntityMap the json schema entity map
     */
    private void convertList(JsonSchema jsonSchema, Map<String, JsonSchema> jsonSchemaEntityMap) {
        switch (jsonSchema.type) {
            case OBJECT:
                jsonSchemaEntityMap.put(jsonSchema.id, jsonSchema);
                jsonSchema.getProperties().values().forEach(subNode -> {
                    jsonSchemaEntityMap.put(subNode.id, subNode);
                    convertList(subNode, jsonSchemaEntityMap);
                });
                break;
            case ARRAY:
                final JsonSchema subNode = jsonSchema.getItems();
                jsonSchemaEntityMap.put(subNode.id, subNode);
                convertList(subNode, jsonSchemaEntityMap);
                break;
            default:
                break;
        }
    }


    /**
     * Gets depth.
     *
     * @return the depth
     */
    public int getDepth() {
        return this.depth;
    }

    /**
     * Sets depth.
     *
     * @param depth the depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * To json string string.
     *
     * @return the string
     */
    public String toJsonString() {
        return JSONObject.toJSONString(this, true);
    }

    @Override
    public String toString() {
        return "JsonSchema{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", pattern='" + pattern + '\'' +
                ", subNodeIds=" + subNodeIds +
                ", required=" + required +
                ", parentId='" + parentId + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", jsonPath='" + jsonPath + '\'' +
                ", examples=" + examples +
                ", properties=" + properties +
                ", items=" + items +
                '}';
    }

    /**
     * Node slimming json schema entity.
     *
     * @return the json schema entity
     */
    public JsonSchema nodeSlimming() {
        JsonSchema jsonSchema;
        jsonSchema = this.copy();
        jsonSchema.properties = null;
        jsonSchema.items = null;
        return jsonSchema;
    }

    /**
     * Gets sub node.
     *
     * @param subNodeId the sub node id
     * @return the sub node
     */
    public JsonSchema getSubNode(String subNodeId) {
        if (this.properties != null) {
            return this.properties.get(subNodeId);
        } else {
            if (this.items != null && this.items.properties != null) {
                return this.items.properties.get(subNodeId);
            } else {
                return this.items;
            }
        }
    }
}
