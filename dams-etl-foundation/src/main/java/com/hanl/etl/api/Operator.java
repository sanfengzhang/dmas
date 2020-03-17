package com.hanl.etl.api;

/**
 * @author: Hanl
 * @date :2020/3/5
 * @desc:
 */
public interface Operator<T extends RecordWrapper> {


    void notify(T notification);


    boolean process(RecordWrapper record);

    /**
     * Returns the parent of this command. The parent of a command A is the command B that passes
     * records to A. A is the child of B.
     */
    Operator getParent();
}
