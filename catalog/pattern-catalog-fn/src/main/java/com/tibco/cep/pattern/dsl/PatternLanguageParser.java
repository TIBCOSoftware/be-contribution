package com.tibco.cep.pattern.dsl;

import org.antlr.runtime.TokenStream;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:38:46 PM
*/

/**
 * Extends parser
 */
public class PatternLanguageParser extends patternParser {

    /**
     * Constructor
     *
     * @param p_input input
     */
    public PatternLanguageParser(TokenStream p_input) {
        super(p_input);
    }
}
