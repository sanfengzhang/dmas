package com.hanl.etl.api;

import java.util.List;

/**
 * @author: Hanl
 * @date :2020/3/6
 * @desc:
 */
public abstract class RecordWrapper {

    public abstract List getArray(String key);

    public abstract Object get(String key);

    public abstract void put(String key, Object value);

    public abstract void putAll(String key, Object value);

}
