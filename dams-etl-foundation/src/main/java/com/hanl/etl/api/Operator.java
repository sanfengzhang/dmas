package com.hanl.etl.api;

/**
 * @author: Hanl
 * @date :2020/3/5
 * @desc:
 */
public interface Operator<T extends RecordWrapper> {


    default  void notify(T notification){}


    default boolean process(RecordWrapper record){
        return true;
    }

    /**
     * Returns the parent of this command. The parent of a command A is the command B that passes
     * records to A. A is the child of B.
     */
    default  Operator getParent(){

        return null;
    }
}
