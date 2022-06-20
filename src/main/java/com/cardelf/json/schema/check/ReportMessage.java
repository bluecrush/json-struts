package com.cardelf.json.schema.check;


import com.cardelf.json.schema.node.JsonNodeType;

/**
 * The type Report message.
 *
 * @author bluecrush
 */
public class ReportMessage {
    /**
     * 校验节点
     */
    private String pointer;

    /**
     * 目标
     */
    private JsonNodeType targetType;

    /**
     * 当前
     */
    private JsonNodeType currentType;


    /**
     * 校验模式
     */
    private CheckType checkMode;


    /**
     * 校验正则
     */
    private String regex;


    /**
     * 当前值
     */
    private Object currentValue;

    /**
     * Instantiates a new Report message.
     *
     * @param pointer      the pointer
     * @param targetType   the target type
     * @param currentType  the current type
     * @param checkMode    the nodeCheck mode
     * @param regex        the regex
     * @param currentValue the current value
     */
    public ReportMessage(String pointer, JsonNodeType targetType, JsonNodeType currentType, CheckType checkMode, String regex, Object currentValue) {
        this.pointer = pointer;
        this.targetType = targetType;
        this.currentType = currentType;
        this.checkMode = checkMode;
        this.regex = regex;
        this.currentValue = currentValue;
    }

    /**
     * Gets pointer.
     *
     * @return the pointer
     */
    public String getPointer() {
        return pointer;
    }

    /**
     * Sets pointer.
     *
     * @param pointer the pointer
     */
    public void setPointer(String pointer) {
        this.pointer = pointer;
    }

    /**
     * Gets target type.
     *
     * @return the target type
     */
    public JsonNodeType getTargetType() {
        return targetType;
    }

    /**
     * Sets target type.
     *
     * @param targetType the target type
     */
    public void setTargetType(JsonNodeType targetType) {
        this.targetType = targetType;
    }

    /**
     * Gets current type.
     *
     * @return the current type
     */
    public JsonNodeType getCurrentType() {
        return currentType;
    }

    /**
     * Sets current type.
     *
     * @param currentType the current type
     */
    public void setCurrentType(JsonNodeType currentType) {
        this.currentType = currentType;
    }

    /**
     * Gets nodeCheck mode.
     *
     * @return the nodeCheck mode
     */
    public CheckType getCheckMode() {
        return checkMode;
    }

    /**
     * Sets nodeCheck mode.
     *
     * @param checkMode the nodeCheck mode
     */
    public void setCheckMode(CheckType checkMode) {
        this.checkMode = checkMode;
    }

    /**
     * Gets regex.
     *
     * @return the regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * Sets regex.
     *
     * @param regex the regex
     */
    public void setRegex(String regex) {
        this.regex = regex;
    }

    /**
     * Gets current value.
     *
     * @return the current value
     */
    public Object getCurrentValue() {
        return currentValue;
    }

    /**
     * Sets current value.
     *
     * @param currentValue the current value
     */
    public void setCurrentValue(Object currentValue) {
        this.currentValue = currentValue;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("ReportMessage:\n");
        msg.append("\tpointer=").append(pointer).append("\n");
        msg.append("\ttargetType=").append(targetType).append("\n");
        msg.append("\tcurrentType=").append(currentType).append("\n");
        msg.append("\tcheckMode=").append(checkMode).append("\n");
        msg.append("\tregex=").append(regex).append("\n");
        msg.append("\tcurrentValue=");
        if (currentType == JsonNodeType.STRING) {
            msg.append("\"").append(currentValue).append("\"").append("\n");
        } else {
            msg.append(currentValue).append("\n");
        }
        if (CheckType.TYPE == checkMode) {
            msg.append("\terrorInfo=Required ").append(targetType).append(" but found ").append(currentType);
        } else if (CheckType.REQUIRED == checkMode) {
            msg.append("\terrorInfo=Required ").append(targetType).append(" ").append(pointer).append(" but not found");
        } else {
            msg.append("\terrorInfo=currentValue '").append(currentValue).append("' Cannot match regular '").append(regex).append("'");
        }

        return msg.toString();
    }
}