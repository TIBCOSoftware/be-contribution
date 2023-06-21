package com.tibco.cep.pattern.matcher.impl.dsl;

import com.tibco.cep.pattern.matcher.dsl.PatternDefLB;

/*
* Author: Ashwin Jayaprakash Date: Jul 24, 2009 Time: 11:25:00 AM
*/
public class DefaultPatternDefLB extends SimpleListBuilder<DefaultPatternDef>
        implements PatternDefLB<DefaultPatternDef> {
    public DefaultPatternDefLB() {
    }

    public DefaultPatternDefLB(DefaultPatternDef firstItem) {
        super(firstItem);
    }

    @Override
    public DefaultPatternDefLB add(DefaultPatternDef defaultPatternDef) {
        super.add(defaultPatternDef);

        return this;
    }
}
