package com.tibco.cep.pattern.matcher.impl.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.master.TimeSource;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.ExpectedTimeInput;
import com.tibco.cep.util.annotation.Optional;
import com.tibco.cep.util.annotation.ReadOnly;

/*
* Author: Ashwin Jayaprakash Date: Jun 30, 2009 Time: 1:49:41 PM
*/
public class DefaultExpectedTimeInput extends DefaultExpectedInput implements ExpectedTimeInput {
    protected Id uniqueId;

    protected long expectationRecordedAtMillis;

    protected long expectedTimeOffsetMillis;

    protected HashSet<ExpectedInput> contextualInputs;

    protected transient TimeOutHint timeOutHint;

    public DefaultExpectedTimeInput() {
    }

    /**
     * Has to be an instance of {@link TimeSource}.
     *
     * @param source
     */
    @Override
    public void setSource(Source source) {
        TimeSource timeSource = (TimeSource) source;

        super.setSource(timeSource);
    }

    @Override
    public TimeSource getSource() {
        return (TimeSource) super.getSource();
    }

    public Id getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Id uniqueId) {
        this.uniqueId = uniqueId;
    }

    public long getExpectationRecordedAtMillis() {
        return expectationRecordedAtMillis;
    }

    public void setExpectationRecordedAtMillis(long expectationRecordedAtMillis) {
        this.expectationRecordedAtMillis = expectationRecordedAtMillis;
    }

    public long getExpectedTimeOffsetMillis() {
        return expectedTimeOffsetMillis;
    }

    public void setExpectedTimeOffsetMillis(long expectedTimeOffsetMillis) {
        this.expectedTimeOffsetMillis = expectedTimeOffsetMillis;
    }

    //--------------

    public void addContextualInput(ExpectedInput expectedInput) {
        if (contextualInputs == null) {
            contextualInputs = new HashSet<ExpectedInput>();
        }

        contextualInputs.add(expectedInput);
    }

    public void removeContextualInput(ExpectedInput expectedInput) {
        contextualInputs.remove(expectedInput);

        if (contextualInputs.isEmpty()) {
            contextualInputs = null;
        }
    }

    public boolean hasContextualInputs() {
        return (contextualInputs != null && contextualInputs.size() > 0);
    }

    @ReadOnly
    @Optional
    public Set<? extends ExpectedInput> getContextualInputs() {
        return contextualInputs;
    }

    @Optional
    public Set<? extends ExpectedInput> removeContextualInputs() {
        HashSet<ExpectedInput> copy = contextualInputs;

        contextualInputs = null;

        return copy;
    }

    //--------------

    public TimeOutHint getTimeOutHint() {
        return timeOutHint;
    }

    public void setTimeOutHint(TimeOutHint timeOutHint) {
        this.timeOutHint = timeOutHint;
    }

    //--------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultExpectedTimeInput)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        DefaultExpectedTimeInput that = (DefaultExpectedTimeInput) o;

        if (expectationRecordedAtMillis != that.expectationRecordedAtMillis) {
            return false;
        }
        if (expectedTimeOffsetMillis != that.expectedTimeOffsetMillis) {
            return false;
        }
        if (!uniqueId.equals(that.uniqueId)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();

        result = 31 * result + uniqueId.hashCode();

        result = 31 * result +
                (int) (expectationRecordedAtMillis ^ (expectationRecordedAtMillis >>> 32));

        result = 31 * result + (int) (expectedTimeOffsetMillis ^ (expectedTimeOffsetMillis >>> 32));

        return result;
    }

    @Override
    public String toString() {
        return "{" + getClass().getSimpleName()
                + "{Driver instance Id: " + driverInstanceId
                + ", Source: " + source
                + ", Destination chain: " + destinationChain
                + ", CorrelationId: " + uniqueId
                + ", ExpectationRecordedAtMillis: " + expectationRecordedAtMillis
                + ", ExpectedTimeOffsetMillis: " + expectedTimeOffsetMillis
                + "}}";

    }

    //--------------

    @Override
    public DefaultExpectedTimeInput recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        return (DefaultExpectedTimeInput) super.recover(resourceProvider, params);
    }

    //--------------

    public static class DefaultTimeOutHint implements TimeOutHint {
        protected Collection<? extends ExpectedInput> contextualInputs;

        protected Collection<? extends ExpectedInput> nonContextualInputs;

        public DefaultTimeOutHint(Collection<? extends ExpectedInput> contextualInputs,
                                  Collection<? extends ExpectedInput> nonContextualInputs) {
            this.contextualInputs = contextualInputs;
            this.nonContextualInputs = nonContextualInputs;
        }

        @Optional
        public Collection<? extends ExpectedInput> getContextualInputs() {
            return contextualInputs;
        }

        @Optional
        public Collection<? extends ExpectedInput> getNonContextualInputs() {
            return nonContextualInputs;
        }

        public boolean hasContextualInputs() {
            return (contextualInputs != null && contextualInputs.size() > 0);
        }

        public boolean hasNonContextualInputs() {
            return (nonContextualInputs != null && nonContextualInputs.size() > 0);
        }
    }
}
