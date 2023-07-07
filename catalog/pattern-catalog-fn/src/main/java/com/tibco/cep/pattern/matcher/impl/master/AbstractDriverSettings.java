package com.tibco.cep.pattern.matcher.impl.master;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.matcher.master.EndSource;
import com.tibco.cep.pattern.matcher.model.Start;

/*
* Author: Ashwin Jayaprakash Date: Oct 6, 2009 Time: 10:53:06 AM
*/
public class AbstractDriverSettings {
    protected Id driverId;

    protected Start start;

    protected InternalTimeSource timeSource;

    protected EndSource endSource;

    public AbstractDriverSettings() {
    }

    public Id getDriverId() {
        return driverId;
    }

    public void setDriverId(Id driverId) {
        this.driverId = driverId;
    }

    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public InternalTimeSource getTimeSource() {
        return timeSource;
    }

    public void setTimeSource(InternalTimeSource timeSource) {
        this.timeSource = timeSource;
    }

    public EndSource getEndSource() {
        return endSource;
    }

    public void setEndSource(EndSource endSource) {
        this.endSource = endSource;
    }
}
