package com.hanl.etl.api;

import com.typesafe.config.Config;

import java.util.Collection;

/**
 * @author: Hanl
 * @date :2020/3/5
 * @desc:
 */
public interface OperatorBuilder {
    /**
     * Returns the names with which this command can be invoked.
     *
     * The returned set can contain synonyms to enable backwards compatible name changes.
     */
    Collection<String> getNames();

    /**
     * Creates and returns a command rooted at the given morphline JSON <code>config</code>.
     *
     * The command will feed records into <code>child</code>. The command will have
     * <code>parent</code> as it's parent. Additional parameters can be passed via the morphline
     * <code>context</code>.
     */
    Operator build(Config config, Operator parent, Operator child, FlowContext context);
}
