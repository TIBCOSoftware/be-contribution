package com.tibco.cep.pattern.matcher.master;

import com.tibco.cep.common.resource.Recoverable;

/*
* Author: Ashwin Jayaprakash Date: Sep 10, 2009 Time: 3:07:19 PM
*/

/**
 * A marker interface to identify a transition-guard. This should be unique inside a pattern
 * instance. Each pattern instance will each have a separate instance of this object, but would mean
 * the same across all such patterns.
 */
public interface TransitionGuardClosure extends Recoverable<TransitionGuardClosure> {
}
