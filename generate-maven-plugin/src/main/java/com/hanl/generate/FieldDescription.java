package com.hanl.generate;

/**
 * @author: Hanl
 * @date :2020/3/11
 * @desc:
 */
public class FieldDescription {

    protected String name;

    protected String fieldType;

    public String getName() {
        return name;
    }

    public String getFieldType() {
        return fieldType;
    }

    @Override
    public String toString() {
        return "FiledDescription{" +
                "name='" + name + '\'' +
                ", fieldType='" + fieldType + '\'' +
                '}';
    }
}
