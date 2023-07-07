package com.tibco.cep.pattern.matcher.response;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedList;

import com.tibco.cep.pattern.matcher.master.Input;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.ExpectedInput;
import com.tibco.cep.pattern.matcher.model.Node;

/*
* Author: Ashwin Jayaprakash Date: Jun 30, 2009 Time: 6:25:09 PM
*/

/**
 * At least one field should be non-<code>null</code>.
 */
public class UnexpectedOccurrence implements Failure {
    protected Input trigger;

    protected Source source;

    protected Node occurredAt;

    protected Exception trace;

    protected Collection<? extends ExpectedInput> expectations;

    /**
     * At least one parameter must be non-<code>null</code>.
     *
     * @param trigger
     * @param source
     * @param trace
     */
    public UnexpectedOccurrence(Input trigger, Source source, Exception trace) {
        this(trigger, source, null, trace, (Collection<? extends ExpectedInput>) null);
    }

    /**
     * At least one parameter must be non-<code>null</code>.
     *
     * @param trigger
     * @param occurredAt
     * @param trace
     * @param expectations
     */
    public UnexpectedOccurrence(Input trigger, Node occurredAt, Exception trace,
                                Collection<? extends ExpectedInput> expectations) {
        this(trigger, null, occurredAt, trace, expectations);
    }

    /**
     * At least one parameter must be non-<code>null</code>.
     *
     * @param trigger
     * @param occurredAt
     * @param trace
     * @param expectations
     */
    public UnexpectedOccurrence(Input trigger, Node occurredAt, Exception trace,
                                ExpectedInput... expectations) {
        this(trigger, null, occurredAt, trace, extractList(expectations));
    }

    /**
     * At least one parameter must be non-<code>null</code>.
     *
     * @param trigger
     * @param source
     * @param occurredAt
     * @param trace
     * @param expectations
     */
    public UnexpectedOccurrence(Input trigger, Source source, Node occurredAt, Exception trace,
                                ExpectedInput... expectations) {
        this(trigger, source, occurredAt, trace, extractList(expectations));
    }

    /**
     * At least one parameter must be non-<code>null</code>.
     *
     * @param trigger
     * @param source
     * @param occurredAt
     * @param trace
     * @param expectations
     */
    public UnexpectedOccurrence(Input trigger, Source source, Node occurredAt, Exception trace,
                                Collection<? extends ExpectedInput> expectations) {
        this.trigger = trigger;
        this.source = source;
        this.occurredAt = occurredAt;
        this.trace = trace;
        this.expectations = expectations;
    }

    private static LinkedList<ExpectedInput> extractList(ExpectedInput[] expectations) {
        LinkedList<ExpectedInput> list = null;

        if (expectations.length > 0) {
            list = new LinkedList<ExpectedInput>();

            for (ExpectedInput expectation : expectations) {
                list.add(expectation);
            }
        }

        return list;
    }

    public Input getTrigger() {
        return trigger;
    }

    public Node getOccurredAt() {
        return occurredAt;
    }

    public Collection<? extends ExpectedInput> getExpectations() {
        return expectations;
    }

    public Exception getTrace() {
        return trace;
    }

    //-------------

    @Override
    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(baos);

        pw.println();

        pw.print("[    ");
        pw.print(getClass().getSimpleName());
        pw.println("    ]");

        if (trigger != null) {
            pw.print(" > Trigger: ");
            pw.println(trigger);
        }

        if (source != null) {
            pw.print(" > Source: ");
            pw.println(source);
        }

        if (expectations != null) {
            pw.println(" > All expectations: ");

            for (ExpectedInput expectation : expectations) {
                pw.print(" > Expectation: ");
                pw.println(expectation);
            }
        }

        if (occurredAt != null) {
            pw.print(" > Occurred at: ");
            pw.println(occurredAt);
        }

        if (trace != null) {
            pw.print(" > Trace: ");
            trace.printStackTrace(pw);
        }

        pw.flush();
        pw.close();

        return baos.toString();
    }
}
