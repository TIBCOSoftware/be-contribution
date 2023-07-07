package com.tibco.cep.pattern.matcher.response;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jun 30, 2009 Time: 6:16:51 PM
*/
public class MultipleFailures extends UnexpectedOccurrence {
    protected Collection<Failure> failures;

    public MultipleFailures(Input trigger, Node occurredAt, Exception trace,
                            Collection<Failure> failures) {
        super(trigger, occurredAt, trace);

        this.failures = failures;
    }

    public Collection<Failure> getFailures() {
        return failures;
    }

    //-------------

    @Override
    public String toString() {
        String superString = super.toString();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        pw.print(superString);

        if (failures != null && failures.size() > 0) {
            pw.println(" > All failures: ");

            for (Failure failure : failures) {
                pw.print(" > Failure: ");
                pw.println(failure);
            }
        }

        pw.flush();
        pw.close();

        return baos.toString();
    }
}
