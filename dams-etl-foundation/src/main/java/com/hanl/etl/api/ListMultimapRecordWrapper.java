package com.hanl.etl.api;

import java.util.List;

/**
 * @author: Hanl
 * @date :2020/3/6
 * @desc:
 */
public class ListMultimapRecordWrapper extends RecordWrapper {

    private ListMultimapRecord record;

    public ListMultimapRecordWrapper(ListMultimapRecord record) {
        this.record = record;
    }

    @Override
    public List getArray(String key) {

        return record.get(key);
    }

    @Override
    public Object get(String key) {

        return record.get(key);
    }

    @Override
    public void put(String key, Object value) {

        record.put(key, value);
    }

    @Override
    public void putAll(String key, Object value) {

        record.getFields().putAll(key, (Iterable<?>) value);
    }
}
