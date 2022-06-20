package com.cardelf.json.schema.check;


import com.cardelf.json.schema.JsonSchema;
import com.cardelf.json.schema.JsonSchemaBuilder;
import org.junit.Before;
import org.junit.Test;

public class JsonCheckerTest {
    private String target;

    private String current;

    @Before
    public void init() {
        target = "{\n" +
                "  \"sites\": {\n" +
                "    \"site\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"菜鸟教程\",\n" +
                "        \"url\": \"www.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"2\",\n" +
                "        \"name\": \"菜鸟工具\",\n" +
                "        \"url\": \"c.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"Google\",\n" +
                "        \"url\": \"www.google.com\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"array\":[1,2,\"3\"]\n" +
                "  }\n" +
                "}";
        current = "{\n" +
                "  \"sites\": {\n" +
                "    \"site\": [\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"name\": \"菜鸟教程\",\n" +
                "        \"url\": \"www.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": \"2\",\n" +
                "        \"name\": \"菜鸟工具\",\n" +
                "        \"url\": \"c.runoob.com\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"name\": \"Google\",\n" +
                "        \"url\": \"www.google.com\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"array\":[1,2,\"3\"]\n" +
                "  }\n" +
                "}";
    }

    @Test
    public void getJsonCheckReport() {
        try {
            final JsonSchemaBuilder jsonSchemaBuilder = new JsonSchemaBuilder();
            final JsonSchema jsonSchema = jsonSchemaBuilder.build(target);

            System.out.println(jsonSchema.toJsonString());
            JsonChecker jsonChecker = new JsonChecker(jsonSchema, CheckMode.PRECISION_MODE);

            for (int i = 0; i < 100; i++) {
                final long start = System.currentTimeMillis();
                final JsonCheckReport jsonCheckReport = jsonChecker.nodeCheck(current);
                System.out.println(jsonCheckReport.toString());
                final long end = System.currentTimeMillis();
                final long l = end - start;
                System.out.printf("耗时:%d毫秒", l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkJsonStyle() {
    }

    @Test
    public void validationRequired() {
    }
}