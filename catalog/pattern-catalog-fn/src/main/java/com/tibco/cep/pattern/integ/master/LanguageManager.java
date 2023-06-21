package com.tibco.cep.pattern.integ.master;

import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/*
* Author: Ashwin Jayaprakash / Date: Nov 17, 2009 / Time: 6:29:06 PM
*/
public interface LanguageManager {
    void init(RuleServiceProvider ruleServiceProvider);

    Definition parse(String langString) throws Exception;

    Definition validate(Definition parsedDefinition) throws Exception;
}
