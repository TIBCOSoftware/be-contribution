package com.tibco.cep.pattern.integ.impl.master;

import com.tibco.cep.pattern.dsl.PatternParserHelper;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.integ.master.LanguageManager;
import com.tibco.cep.runtime.session.RuleServiceProvider;

/*
* Author: Ashwin Jayaprakash / Date: Nov 17, 2009 / Time: 6:34:19 PM
*/
public class DefaultLanguageManager implements LanguageManager {
    public void init(RuleServiceProvider ruleServiceProvider) {
    }

    public Definition parse(String langString) throws Exception {
        PatternParserHelper parserHelper = new PatternParserHelper(langString);

        return parserHelper.parse();
    }

    public Definition validate(Definition parsedDefinition) throws Exception {
        //todo Plug in validation.

        return parsedDefinition;
    }
}
