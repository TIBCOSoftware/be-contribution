package com.tibco.cep.pattern.subscriber.master;

import java.util.Collection;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.service.Service;

/*
* Author: Ashwin Jayaprakash Date: Aug 17, 2009 Time: 11:30:06 AM
*/
public interface SubscriptionCaretakerRegistry<C extends SubscriptionCaretaker> extends Service {
    /**
     * @param caretaker
     * @return <code>false</code> if there is already an existing deployment.
     */
    boolean addCaretaker(C caretaker);

    /**
     * @param id From {@link SubscriptionCaretaker#getId()}.
     * @return
     */
    C getCaretaker(Id id);

    Collection<C> getCaretakers();

    void removeCaretaker(C caretaker);
}
