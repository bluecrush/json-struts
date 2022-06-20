package com.cardelf.json.schema.check;

/**
 * The enum Check mode.
 *
 * @author bluecrush
 */
public enum CheckMode {
    /**
     * 饿汉模式：检测出所有的错误
     */
    HUNGRY_MODE("HUNGRY_MODE", 1),

    /**
     * 懒汉模式：发现错误就停止
     */
    LAZY_MODE("LAZY_MODE", 2),

    /**
     * 精准模式：精准模式下会对比模版和当前json的key是否完全一致，其他模式允许模版格式是当前json的子集（即满足模版即可）
     * 同时此模式的校验耗时也会较长一点
     */
    PRECISION_MODE("PRECISION_MODE", 3);


    /**
     * The Priority.
     */
    private final int priority;

    /**
     * The Value.
     */
    private final String value;

    /**
     * Instantiates a new Check mode.
     *
     * @param value    the value
     * @param priority the priority
     */
    CheckMode(String value, int priority) {
        this.value = value;
        this.priority = priority;
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
     * Gets priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }
}
