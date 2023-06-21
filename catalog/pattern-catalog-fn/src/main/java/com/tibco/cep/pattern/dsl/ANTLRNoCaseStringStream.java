package com.tibco.cep.pattern.dsl;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:39:40 PM
*/

/**
 * Allows upper and lower case commands
 */
public class ANTLRNoCaseStringStream extends ANTLRStringStream {

    public ANTLRNoCaseStringStream(String p_input) {
        super(p_input);
    }

    public int LA(int p_i) {
        if (p_i == 0) {
            return 0; // undefined
        }
        if (p_i < 0) {
            p_i++; // e.g., translate LA(-1) to use offset 0
        }

        if ((p + p_i - 1) >= n) {

            return CharStream.EOF;
        }
        return Character.toLowerCase(data[p + p_i - 1]);
    }
}
