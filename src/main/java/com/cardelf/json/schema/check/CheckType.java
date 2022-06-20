package com.cardelf.json.schema.check;

/**
 * The enum CheckType.
 */
enum CheckType {
    /**
     * 必须存在节点
     */
    REQUIRED("required"),
    /**
     * 节点不应该存在
     */
    NOT_REQUIRED("notRequired"),
    /**
     * 节点类型校验
     */
    TYPE("type"),
    /**
     * 字符型正则校验
     */
    REGEX("regex");

    CheckType(String value) {
    }

}
