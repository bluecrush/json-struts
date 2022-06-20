package com.cardelf.json.schema.extract;

import com.cardelf.json.exception.JsonStructureException;
import com.cardelf.json.schema.JsonSchema;
import com.cardelf.json.schema.JsonSchemaBuilder;
import org.junit.Test;

import java.util.List;

public class JsonNodeVarExtractorTest {
    @Test
    public void testAutoVariableRecognition() throws JsonStructureException {
        String json = "{\n" +
                "  \"Document\": {\n" +
                "    \"PDA\": {\n" +
                "      \"PD01\": [\n" +
                "        {\n" +
                "          \"PD01B\": {\n" +
                "            \"PD01BD01\": \"1\",\n" +
                "            \"PD01BR01\": \"2023-06-06\",\n" +
                "            \"PD01BR04\": \"null\",\n" +
                "            \"PD01BJ01\": \"2019-01-13\",\n" +
                "            \"PD01BR02\": \"2019-01-13\",\n" +
                "            \"PD01BJ02\": \"1,000\",\n" +
                "            \"PD01BD03\": \"1\",\n" +
                "            \"PD01BD04\": \"N\",\n" +
                "            \"PD01BR03\": \"2019-01-13\"\n" +
                "          },\n" +
                "          \"PD01D\": {\n" +
                "            \"PD01DR01\": \"2018-0-12\",\n" +
                "            \"PD01DR02\": \"2019-0-12\",\n" +
                "            \"PD01DH\": [\n" +
                "              {\n" +
                "                \"PD01DR03\": \"2018-05\",\n" +
                "                \"PD01DD01\": \"M\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"PD01DR03\": \"2018-05\",\n" +
                "                \"PD01DD01\": \"M\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JsonSchemaBuilder jsonSchemaBuilder = new JsonSchemaBuilder();
        final JsonSchema jsonSchema = jsonSchemaBuilder.build(json);
        JsonNodeVarExtractor jsonVariableRecogniser = new JsonNodeVarExtractor(jsonSchema);
        List<JsonSchema> variables = jsonVariableRecogniser.autoVariableRecognition();
        System.out.println("变量个数：" + variables.size());
        variables.forEach(variable -> System.out.println(variable.getTitle() + "\t" + variable.getJsonPath() + "\t" + variable.getType()));
    }
}