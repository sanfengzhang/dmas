package com.hanl.etl.api;

import com.google.common.base.Preconditions;

/**
 * Command that is sandwiched between two other commands, chaining the two other commands together.
 */
final class Connector<T extends RecordWrapper> implements Operator<T> {

    private Operator parent;
    private Operator child;
    private final boolean ignoreNotifications;

    public Connector(boolean ignoreNotifications) {
        this.ignoreNotifications = ignoreNotifications;
    }

    @Override
    public Operator getParent() {
        return parent;
    }

    public void setParent(Operator parent) {
        Preconditions.checkNotNull(parent);
        this.parent = parent;
    }

    public void setChild(Operator child) {
        Preconditions.checkNotNull(child);
        this.child = child;
    }

    @Override
    public void notify(T notification) {
        Preconditions.checkNotNull(notification);
        Preconditions.checkNotNull(parent);
        Preconditions.checkNotNull(child);
        if (!ignoreNotifications) {
            child.notify(notification);
        }
    }

    @Override
    public boolean process(RecordWrapper record) {
        Preconditions.checkNotNull(record);
        return child.process(record);
    }

}
