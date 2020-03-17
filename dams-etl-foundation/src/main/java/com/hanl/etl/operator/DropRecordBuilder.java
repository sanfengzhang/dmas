package com.hanl.etl.operator;

import com.hanl.etl.api.*;
import com.typesafe.config.Config;

import java.util.Collection;
import java.util.Collections;

/**
 * Command that silently consumes records without ever emitting any record - think /dev/null.
 */
public final class DropRecordBuilder implements OperatorBuilder {

    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("dropRecord");
    }

    @Override
    public Operator build(Config config, Operator parent, Operator child, FlowContext context) {
        if (config == null) {
            return new DevNull(parent);
        } else {
            return new DropRecord(this, config, parent, child, context);
        }
    }


    private static final class DevNull<T extends RecordWrapper> implements Operator<T> {

        private Operator parent;

        public DevNull(Operator parent) {
            this.parent = parent;
        }

        @Override
        public Operator getParent() {
            return parent;
        }

        @Override
        public void notify(RecordWrapper notification) {
        }

        @Override
        public boolean process(RecordWrapper record) {
            return true;
        }

    }


    private static final class DropRecord<T extends RecordWrapper> extends AbstractOperator<T> {

        public DropRecord(OperatorBuilder builder, Config config, Operator parent, Operator child, FlowContext context) {
            super(builder, config, parent, child, context);
        }

        @Override
        protected boolean doProcess(RecordWrapper record) {
            return true;
        }

    }

}
