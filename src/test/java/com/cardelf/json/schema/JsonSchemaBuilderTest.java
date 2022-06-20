package com.cardelf.json.schema;

import com.cardelf.json.exception.JsonStructureException;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class JsonSchemaBuilderTest {


    @Test
    public void getStringJsonSchema1() throws JsonStructureException {
        String json = "{\n" +
                "  \"sites\": {\n" +
                "    \"site\": [\n" +
                "      {\n" +
                "        \"id\": \"1\",\n" +
                "        \"name\": \"菜鸟教程\",\n" +
                "        \"url\": \"www.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"2\",\n" +
                "        \"name\": \"菜鸟工具\",\n" +
                "        \"url\": \"c.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"3\",\n" +
                "        \"name\": \"Google\",\n" +
                "        \"url\": \"www.google.com\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        JsonSchemaBuilder jsonSchemaBuilder = new JsonSchemaBuilder();
        String jsonSchema = jsonSchemaBuilder.build(json).toJsonString();
        System.out.println(jsonSchema);
    }

    @Test
    public void getJsonSchemaEntities1() throws JsonStructureException {
        String json = "{\n" +
                "  \"sites\": {\n" +
                "    \"site\": [\n" +
                "      {\n" +
                "        \"id\": \"1\",\n" +
                "        \"name\": \"菜鸟教程\",\n" +
                "        \"url\": \"www.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"2\",\n" +
                "        \"name\": \"菜鸟工具\",\n" +
                "        \"url\": \"c.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"3\",\n" +
                "        \"name\": \"Google\",\n" +
                "        \"url\": \"www.google.com\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  \"array\": [\n" +
                "    [\n" +
                "      [\n" +
                "        [\n" +
                "          {\n" +
                "            \"name\": \"qqq\"\n" +
                "          }\n" +
                "        ]\n" +
                "      ]\n" +
                "    ]\n" +
                "  ]\n" +
                "}";
        JsonSchemaBuilder jsonSchemaBuilder = new JsonSchemaBuilder();
        Map<String, JsonSchema> jsonSchemaEntities = jsonSchemaBuilder.build(json).toJsonSchemaEntityMap();
        System.out.println("数量：" + jsonSchemaEntities.size());
        jsonSchemaEntities.forEach((s, jsonSchemaEntity) -> {
            System.out.println(jsonSchemaEntity.getId() + "\t" + jsonSchemaEntity.getJsonPath());
        });
    }

    @Test
    public void getJsonSchemaEntities3() throws JsonStructureException {
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
        Map<String, JsonSchema> jsonSchemaEntities = jsonSchemaBuilder.build(json).toJsonSchemaEntityMap();
        System.out.println("数量：" + jsonSchemaEntities.size());
        jsonSchemaEntities.forEach((s, jsonSchemaEntity) -> {
            System.out.println(jsonSchemaEntity.getId() + "\t" + jsonSchemaEntity.getJsonPath());
        });
    }

    @Test
    public void getJsonSchemaEntities2() throws JsonStructureException {
        String json = "{\n" +
                "  \"Document\": {\n" +
                "    \"PDA\": {\n" +
                "      \"PD02\":[\n" +
                "        {\"PD02A\":\"888\"},\n" +
                "        {\"PD02A\":\"888\"},\n" +
                "        {\"PD02A\":\"888\"}\n" +
                "        ],\n" +
                "      \"PD03\": \"999\",\n" +
                "      \"PD01\": [\n" +
                "        {\n" +
                "          \"PD01A\": {\n" +
                "            \"PD01AI01\": \"666\",\n" +
                "            \"PD01AD01\": \"D1\",\n" +
                "            \"PD01AD02\": \"16\",\n" +
                "            \"PD01AI02\": \"HAS3GD213F2\",\n" +
                "            \"PD01AI03\": \"4561\",\n" +
                "            \"PD01AI04\": \"456\",\n" +
                "            \"PD01AD03\": \"11\",\n" +
                "            \"PD01AR01\": \"2014-06-06\",\n" +
                "            \"PD01AD04\": \"CNY\",\n" +
                "            \"PD01AJ01\": \"10000\",\n" +
                "            \"PD01AJ02\": \"85\",\n" +
                "            \"PD01AJ03\": \"33000\",\n" +
                "            \"PD01AR02\": \"2023-06-06\",\n" +
                "            \"PD01AD05\": \"12\",\n" +
                "            \"PD01AD06\": \"3\",\n" +
                "            \"PD01AS01\": \"12\",\n" +
                "            \"PD01AD07\": \"3\",\n" +
                "            \"PD01AD08\": \"1\",\n" +
                "            \"PD01AD09\": \"1\",\n" +
                "            \"PD01AD10\": \"0\"\n" +
                "          },\n" +
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
                "          \"PD01C\": {\n" +
                "            \"PD01CR01\": \"201912\",\n" +
                "            \"PD01CD01\": \"1\",\n" +
                "            \"PD01CJ01\": \"10000\",\n" +
                "            \"PD01CJ02\": \"0\",\n" +
                "            \"PD01CJ03\": \"0\",\n" +
                "            \"PD01CD02\": \"1\",\n" +
                "            \"PD01CS01\": \"0\",\n" +
                "            \"PD01CR02\": \"2019-03-10\",\n" +
                "            \"PD01CJ04\": \"0\",\n" +
                "            \"PD01CJ05\": \"0\",\n" +
                "            \"PD01CR03\": \"2019-03-26\",\n" +
                "            \"PD01CS02\": \"0\",\n" +
                "            \"PD01CJ06\": \"0\",\n" +
                "            \"PD01CJ07\": \"0\",\n" +
                "            \"PD01CJ08\": \"0\",\n" +
                "            \"PD01CJ09\": \"0\",\n" +
                "            \"PD01CJ10\": \"0\",\n" +
                "            \"PD01CJ11\": \"0\",\n" +
                "            \"PD01CJ12\": \"0\",\n" +
                "            \"PD01CJ13\": \"0\",\n" +
                "            \"PD01CJ14\": \"0\",\n" +
                "            \"PD01CJ15\": \"0\",\n" +
                "            \"PD01CR04\": \"2019-03-10\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        JsonSchemaBuilder jsonSchemaBuilder = new JsonSchemaBuilder();
        List<JsonSchema> jsonSchemaEntities = jsonSchemaBuilder.build(json).toLeafJsonSchemaEntityList();
        System.out.println("数量：" + jsonSchemaEntities.size());
        jsonSchemaEntities.forEach(jsonSchemaEntity -> {
            System.out.println(jsonSchemaEntity.getId() + "\t" + jsonSchemaEntity.getJsonPath());
        });
    }
}