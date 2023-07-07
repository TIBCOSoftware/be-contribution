package com.tibco.cep.pattern.matcher.model;

import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash / Date: Dec 1, 2009 / Time: 4:41:59 PM
*/
public interface ContextualInfoProvider {
    /**
     * @return The node's contextual time input.
     *         <p/>
     *         If this is the beginning of a group:
     *         <p/>
     *         1) Return your own if you have one
     *         <p/>
     *         2) Return the parent's if you don't have your own.
     *         <p/>
     *         If this is the end of a group - return parent's.
     */
    @Optional
    ExpectedTimeInput getContextualExpectedTimeInput();
}
