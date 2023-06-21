package com.tibco.cep.pattern.matcher.impl.dsl;

import com.tibco.cep.pattern.matcher.dsl.InputDefLB;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInputDef;

/*
* Author: Ashwin Jayaprakash Date: Jul 24, 2009 Time: 11:23:58 AM
*/
public class DefaultInputDefLB extends SimpleListBuilder<DefaultInputDef>
        implements InputDefLB<DefaultInputDef> {
    public DefaultInputDefLB() {
    }

    public DefaultInputDefLB(DefaultInputDef firstItem) {
        super(firstItem);
    }

    @Override
    public DefaultInputDefLB add(DefaultInputDef defaultInputDef) {
        super.add(defaultInputDef);

        return this;
    }
}
