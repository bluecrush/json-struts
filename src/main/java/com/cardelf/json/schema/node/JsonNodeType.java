package com.cardelf.json.schema.node;

/**
 * The enum Variable type.
 *
 * @author bluecrush
 */
public enum JsonNodeType {
    /**
     * 未知类型
     */
    NULL("null", true, 0),

    /**
     * 整型数字
     */
    INTEGER("integer", true, 1),
    /**
     * 长整型数字
     */
    LONG("long", true, 2),
    /**
     * 浮点型数值
     */
    NUMBER("number", true, 3),
    /**
     * 布尔类型
     */
    BOOLEAN("boolean", true, 4),

    /**
     * 字符串
     */
    STRING("string", true, 5),

    /**
     *
     * 数组类型
     */
    ARRAY("array", false, 7),

    /**
     * object类型
     */
    OBJECT("object", false, 6),

    /**
     * 表类型
     */
    TABLE("table", false, 8);


    private final String value;
    private final boolean basicType;
    /**
     * 排序
     */
    private final int weight;


    /**
     * Instantiates a new Variable type.
     *
     * @param value     the value
     * @param basicType the basic type
     * @param weight    the weight
     */
    JsonNodeType(String value, boolean basicType, int weight) {
        this.value = value;
        this.basicType = basicType;
        this.weight = weight;
    }

    /**
     * Is basic type boolean.
     *
     * @return the boolean
     */
    public boolean isBasicType() {
        return basicType;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return value;
    }
}
