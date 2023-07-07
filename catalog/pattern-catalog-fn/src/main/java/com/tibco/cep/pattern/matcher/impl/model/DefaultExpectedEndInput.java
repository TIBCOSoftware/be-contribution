package com.tibco.cep.pattern.matcher.impl.model;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.EndSource;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.ExpectedEndInput;

/*
* Author: Ashwin Jayaprakash Date: Jun 30, 2009 Time: 1:49:41 PM
*/
public class DefaultExpectedEndInput extends DefaultExpectedInput implements ExpectedEndInput {
    /**
     * Has to be an instance of {@link EndSource}.
     *
     * @param source
     */
    @Override
    public void setSource(Source source) {
        EndSource endSource = (EndSource) source;

        super.setSource(endSource);
    }

    @Override
    public EndSource getSource() {
        return (EndSource) super.getSource();
    }
    //--------------

    @Override
    public DefaultExpectedEndInput recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return (DefaultExpectedEndInput) super.recover(resourceProvider, params);
    }
}