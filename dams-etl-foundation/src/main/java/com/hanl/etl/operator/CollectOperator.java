package com.hanl.etl.operator;

import com.google.common.base.Preconditions;
import com.hanl.etl.api.Operator;
import com.hanl.etl.api.RecordWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Hanl
 * @date :2020/3/6
 * @desc:
 */
public class CollectOperator<T extends RecordWrapper> implements Operator<T> {


    /**
     * 在使用Foreach循环的时候对Array的效率比较低
     */
    private List<RecordWrapper> results = new ArrayList<>();

    public CollectOperator() {

    }

    public CollectOperator(List<RecordWrapper> recordWrapperList) {

        this.results = recordWrapperList;
    }

    public List<RecordWrapper> getRecords() {

        return results;
    }

    public void reset() {
        results.clear();
    }


    @Override
    public void notify(T notification) {

    }

    @Override
    public boolean process(RecordWrapper record) {
        Preconditions.checkNotNull(record);
        results.add(record);
        return true;
    }

    @Override
    public Operator getParent() {
        return null;
    }
}
