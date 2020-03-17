package com.hanl.etl.operator.builder;

import com.hanl.etl.api.FlowContext;
import com.hanl.etl.api.Operator;
import com.hanl.etl.api.RecordWrapper;
import com.hanl.etl.operator.Flow;
import com.typesafe.config.Config;
import com.hanl.etl.api.OperatorBuilder;

import java.util.Collection;
import java.util.Collections;

/**
 * Factory to create morphline pipe instances.
 */
public final class FlowBuilder implements OperatorBuilder {

    @Override
    public Collection<String> getNames() {
        return Collections.singletonList("Flow");
    }

    @Override
    public Operator build(Config config, Operator parent, Operator child, FlowContext context) {
        return new Flow(this, config, (parent != null ? parent : new RootCommand()), child, context);
    }



    private static final class RootCommand<T extends RecordWrapper> implements Operator<T> {

        @Override
        public Operator getParent() {
            return null;
        }

        @Override
        public void notify(RecordWrapper notification) {
            throw new UnsupportedOperationException("Root command should be invisible and must not be called");
        }

        @Override
        public boolean process(RecordWrapper record) {
            throw new UnsupportedOperationException("Root command should be invisible and must not be called");
        }

    }
}
