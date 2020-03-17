package com.hanl.etl.base;

import com.hanl.etl.api.RecordWrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hanl
 * @date :2020/3/6
 * @desc:
 */
public class MapRecordWrapper extends RecordWrapper {

    private Map<String, Object> record;

    public MapRecordWrapper() {

        record = new HashMap<>();
    }

    public MapRecordWrapper(Map<String, Object> record) {
        
        this.record = record;
    }

    @Override
    public List getArray(String key) {
        return Collections.singletonList( record.get(key));
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

    }

    @Override
    public String toString() {
        return "MapRecordWrapper{" +
                "record=" + record.toString() +
                '}';
    }
}
