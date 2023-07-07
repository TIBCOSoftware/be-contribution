package com.tibco.cep.pattern.common.store;

import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash / Date: Nov 3, 2009 / Time: 5:02:11 PM
*/
public interface Storage {
    //todo Incomplete interface

    /*
    todo - Recovery

    What does it mean to recover?
    What version gets recovered?
    What are checkpoints?

    Just use BDB for this?
    */


    void write();

    void read();

    @Optional
    void checkpoint();

    public static interface Stats {
        long pendingChanges();
    }
}
