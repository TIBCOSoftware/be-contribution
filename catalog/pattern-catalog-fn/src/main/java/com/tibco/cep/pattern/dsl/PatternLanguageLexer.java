package com.tibco.cep.pattern.dsl;

import org.antlr.runtime.CharStream;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:42:41 PM
*/

/**
 * Extends lexer
 */
public class PatternLanguageLexer extends patternLexer {

    ExceptionHandler adminException;
    patternLexer lexer;

    /**
     * Constructor
     */
    public PatternLanguageLexer() {
    }

    /**
     * Constructor
     *
     * @param lexer lexer
     */
    public PatternLanguageLexer(patternLexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Constructor
     *
     * @param input input
     */
    public PatternLanguageLexer(CharStream input) {
        super(input);
        //this.adminException = adminException;
    }

    /**
     * Returns input source
     *
     * @return input
     */
    public CharStream getInputSource() {
        return lexer.getCharStream();
    }
}
