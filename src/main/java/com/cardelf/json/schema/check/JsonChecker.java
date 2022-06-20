package com.cardelf.json.schema.check;


import com.cardelf.json.constant.JsonNodeConstants;
import com.cardelf.json.exception.JsonStructureException;
import com.cardelf.json.schema.JsonSchema;
import com.cardelf.json.schema.JsonSchemaBuilder;
import com.cardelf.json.schema.node.JsonNodeType;
import com.cardelf.json.schema.node.JsonNodeTypeUtil;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.internal.ParseContextImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * The type Json format checker.
 *
 * @author bluecrush
 */
public class JsonChecker {
    /**
     * The Json schema.
     */
    private JsonSchema jsonschema;


    /**
     * The Target json schema entity id list.
     */
    private List<String> targetJsonSchemaEntityIdList;
    /**
     * The Parse conf.
     */
    private final Configuration parseConf = getConfiguration()
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
    /**
     * The Path conf.
     */
    private final Configuration pathConf = getConfiguration()
            .addOptions(Option.AS_PATH_LIST)
            .addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .addOptions(Option.ALWAYS_RETURN_LIST);

    /**
     * Gets configuration.
     *
     * @return the configuration
     */
    private static Configuration getConfiguration() {
        return Configuration.defaultConfiguration();
    }

    /**
     * The Check feature.
     */
    private CheckMode checkMode;


    /**
     * The Parse context.
     */
    private final ParseContextImpl parseContext = new ParseContextImpl();

    /**
     * Instantiates a new Json format checker.
     *
     * @param jsonschema the json schema
     */
    public JsonChecker(JsonSchema jsonschema) {
        this.init(jsonschema, CheckMode.LAZY_MODE);
    }

    /**
     * Instantiates a new Json format checker.
     *
     * @param jsonschema the json schema
     * @param checkMode  the nodeCheck feature
     */
    public JsonChecker(JsonSchema jsonschema, CheckMode checkMode) {
        this.init(jsonschema, checkMode);
    }


    /**
     * Instantiates a new Json format checker.
     *
     * @param jsonSchema the json schema
     */
    public JsonChecker(String jsonSchema) {
        final DocumentContext parse = parseContext.parse(jsonSchema);
        this.jsonschema = parse.json();
        this.init(jsonschema, CheckMode.LAZY_MODE);

    }

    private void init(JsonSchema jsonschema, CheckMode checkMode) {
        this.jsonschema = jsonschema;
        this.targetJsonSchemaEntityIdList = this.jsonschema.getJsonSchemaEntityIds();
        this.checkMode = checkMode;
    }

    /**
     * Instantiates a new Json format checker.
     *
     * @param jsonSchema the json schema
     * @param checkMode  the nodeCheck feature
     */
    public JsonChecker(String jsonSchema, CheckMode checkMode) {
        final DocumentContext parse = parseContext.parse(jsonSchema);
        this.jsonschema = parse.json();
        this.targetJsonSchemaEntityIdList = this.jsonschema.getJsonSchemaEntityIds();
        this.checkMode = checkMode;
    }


    /**
     * Gets json nodeCheck report.
     *
     * @param json the json
     * @return the json nodeCheck report
     * @throws JsonStructureException the json structure exception
     */
    public JsonCheckReport nodeCheck(String json) throws JsonStructureException {
        return nodeCheck((Object) json);
    }

    /**
     * Node check json check report.
     *
     * @param json the json
     * @return the json check report
     * @throws JsonStructureException the json structure exception
     */
    public JsonCheckReport nodeCheck(Object json) throws JsonStructureException {
        DocumentContext parseDocument;
        DocumentContext pathDocument;
        try {
            parseDocument = JsonPath.using(parseConf).parse(json);
            pathDocument = JsonPath.using(pathConf).parse(json);
        } catch (Exception e) {
            throw new JsonStructureException("Not a JSON structure", e);
        }
        JsonCheckReport jsonCheckReport = new JsonCheckReport();
        nodeCheck(this.jsonschema, parseDocument, pathDocument, jsonCheckReport, "$");
        // 开始PRECISION_MODE校验
        if (CheckMode.PRECISION_MODE == this.checkMode) {
            final JsonCheckReport checkReport = this.structureCheck(json);
            jsonCheckReport.addReportMessages(checkReport.getReportMessages());
        }
        return jsonCheckReport;
    }

    /**
     * 结构强校验
     *
     * @param json the json
     * @return the json nodeCheck report
     * @throws JsonStructureException the json parse exception
     */
    private JsonCheckReport structureCheck(Object json) throws JsonStructureException {
        JsonCheckReport jsonCheckReport = new JsonCheckReport();
        final JsonSchemaBuilder jsonSchemaBuilder = new JsonSchemaBuilder();
        final JsonNodeType type = JsonNodeTypeUtil.recognitionDataType(json);
        JsonSchema builder;
        if (type == JsonNodeType.STRING) {
            builder = jsonSchemaBuilder.build(json.toString());
        } else {
            builder = jsonSchemaBuilder.build(json);
        }
        final Map<String, JsonSchema> jsonSchemaEntities = builder.toJsonSchemaEntityMap();
        final List<String> currentJsonSchemaEntityIds = builder.getJsonSchemaEntityIds();
        for (String current : currentJsonSchemaEntityIds) {
            if (!targetJsonSchemaEntityIdList.contains(current)) {
                final JsonSchema jsonSchema = jsonSchemaEntities.get(current);
                final String jsonPath = jsonSchema.getJsonPath();
                final String pattern = jsonSchema.getPattern();
                final Object examples = jsonSchema.getExamples();
                final ReportMessage reportMessage = new ReportMessage(jsonPath, JsonNodeType.NULL, jsonSchema.getType(), CheckType.NOT_REQUIRED,
                        pattern, examples);
                jsonCheckReport.addReportMessages(reportMessage);
            }
        }
        return jsonCheckReport;
    }


    /**
     * Check.
     *
     * @param jsonSchema      the json schema entity
     * @param parseDocument   the parse document
     * @param pathDocument    the path document
     * @param jsonCheckReport the json nodeCheck report
     * @param parentJsonPath  the parent json path
     */
    private void nodeCheck(JsonSchema jsonSchema, DocumentContext parseDocument, DocumentContext pathDocument, JsonCheckReport jsonCheckReport, String parentJsonPath) {
        // 处理由于递归造成LAZY模式下多检测的问题，提前做一步判断
        if (CheckMode.LAZY_MODE == this.checkMode && !jsonCheckReport.isSuccess()) {
            return;
        }
        JsonNodeType parentType = jsonSchema.getType();
        // 1.获取当前节点的必输
        HashSet<String> required;
        Collection<JsonSchema> subSchemaEntities;
        switch (parentType) {
            case OBJECT:
                required = jsonSchema.getRequired();
                subSchemaEntities = jsonSchema.getProperties().values();
                break;
            case ARRAY:
                JsonSchema items = jsonSchema.getItems();
                required = items.getRequired();
                // 如果是[1,2,3,4] 则把该节点本身放进去
                subSchemaEntities = items.getProperties() != null ? items.getProperties().values() : Collections.singleton(items);
                break;
            default:
                return;
        }
        // 2.开始校验
        for (JsonSchema subSchemaEntity : subSchemaEntities) {
            // 当前校验的节点是否必须存在
            JsonNodeType targetType = subSchemaEntity.getType();
            String subJsonPath = getSubJsonPath(parentJsonPath, parentType, targetType, subSchemaEntity.getTitle());
            List<String> paths;
            boolean result;
            try {
                paths = pathDocument.read(subJsonPath);
            } catch (Exception e) {
                ReportMessage reportMessage = new ReportMessage(subJsonPath, targetType, null, CheckType.REQUIRED, subSchemaEntity.getPattern(), null);
                jsonCheckReport.addReportMessages(reportMessage);
                // 检测校验模式
                if (CheckMode.LAZY_MODE == this.checkMode) {
                    return;
                } else {
                    continue;
                }
            }
            String pattern = subSchemaEntity.getPattern();
            assert paths != null;
            for (String path : paths) {
                Object value = parseDocument.read(path);
                JsonNodeType currentType = JsonNodeTypeUtil.recognitionDataType(value);
                CheckType keyword = null;
                // 1.必输校验
                if (required != null) {
                    keyword = CheckType.REQUIRED;
                    result = validationRequired(required, subSchemaEntity.getTitle(), value);
                } else {
                    result = true;
                }
                // 2.类型校验
                if (result) {
                    keyword = CheckType.TYPE;
                    result = validationType(targetType, currentType);
                }
                // 3.正则校验
                if (result && JsonNodeType.STRING == targetType) {
                    keyword = CheckType.REGEX;
                    result = validationRegex(pattern, value);
                }
                // 4.最终结果
                if (!result) {
                    ReportMessage reportMessage = new ReportMessage(path, targetType, currentType, keyword, pattern, value);
                    jsonCheckReport.addReportMessages(reportMessage);
                    // 检测校验模式
                    if (CheckMode.LAZY_MODE == this.checkMode) {
                        return;
                    }
                }
            }
            if (!subSchemaEntity.getType().isBasicType()) {
                String nextSubJsonPath = getSubJsonPath(parentJsonPath, parentType, subSchemaEntity.getType(), subSchemaEntity.getTitle());
                nodeCheck(subSchemaEntity, parseDocument, pathDocument, jsonCheckReport, nextSubJsonPath);
            }
        }
    }

    /**
     * Gets sub json path.
     *
     * @param parentJsonPath the parent json path
     * @param parentType     the parent type
     * @param currentType    the current type
     * @param title          the title
     * @return the sub json path
     */
    private String getSubJsonPath(String parentJsonPath, JsonNodeType parentType, JsonNodeType currentType, String title) {
        String subJsonPath;
        if (JsonNodeType.ARRAY == parentType && title != null) {
            subJsonPath = parentJsonPath + JsonNodeConstants.NODE_JSONPATH_SPLICE + JsonNodeConstants.NODE_JSONPATH_ARRAY + JsonNodeConstants.NODE_JSONPATH_SPLICE + title;
        } else {
            subJsonPath = parentJsonPath + JsonNodeConstants.NODE_JSONPATH_SPLICE + (title == null ? "*" : title);
        }
        return subJsonPath;
    }

    /**
     * Gets sub id.
     *
     * @param parentId the parent id
     * @param title    the title
     * @return the sub id
     */
    private String getSubId(String parentId, String title) {
        String subId;
        if (parentId == null) {
            subId = "#";
        } else {
            subId = parentId + JsonNodeConstants.NODE_ID_SPLICE + title;
        }
        return subId;
    }


    /**
     * Validation type boolean.
     *
     * @param targetType  the target type
     * @param currentType the current type
     * @return the boolean
     */
    private boolean validationType(JsonNodeType targetType, JsonNodeType currentType) {
        return targetType == currentType;
    }


    /**
     * Validation required boolean.
     *
     * @param requiredSet the required to be set
     * @param title       the title
     * @param value       the value
     * @return the boolean
     */
    private boolean validationRequired(HashSet<String> requiredSet, String title, Object value) {
        return requiredSet.contains(title) && value != null;
    }


    /**
     * Validation regex boolean.
     *
     * @param regex the regex
     * @param value the value
     * @return the boolean
     */
    private boolean validationRegex(String regex, Object value) {
        return String.valueOf(value).matches(regex);
    }
}
