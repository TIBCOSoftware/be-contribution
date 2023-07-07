package com.tibco.cep.pattern.impl.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.integ.impl.master.DefaultCorrelationIdExtractor;
import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.dsl.PatternDeploymentDef;
import com.tibco.cep.pattern.matcher.impl.master.DefaultTransitionGuardClosure;
import com.tibco.cep.pattern.matcher.impl.master.ReflectionTransitionGuardSetCreator;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;
import com.tibco.cep.util.annotation.LogCategory;

/*
* Author: Ashwin Jayaprakash Date: Aug 26, 2009 Time: 1:46:34 PM
*/

public class Helper {
    public static <T> ReflectionEventDescriptor<T> $eventDescriptor(Class<T> type, String id) {
        return new ReflectionEventDescriptor<T>(type, $id(id));
    }

    public static Id $sourceId(EventDescriptor eventDescriptor) {
        return eventDescriptor.getResourceId();
    }

    public static <T> SimpleEventSource<T> $eventSource(EventDescriptor<T> eventDescriptor) {
        return new SimpleEventSource<T>(eventDescriptor);
    }

    public static Date $date(int date, int month, int year) {
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.set(year, month, date, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Id $id(String string) {
        return new DefaultId(string);
    }

    public static PatternDef $pattern(PatternDeploymentDef deployment) {
        return deployment.createPattern();
    }

    public static DefaultCorrelationIdExtractor $corrIdExtractor(String fieldName) {
        return new DefaultCorrelationIdExtractor(fieldName);
    }

    public static <K extends TransitionGuardClosure> ReflectionTransitionGuardSetCreator<K> $guardSetCreator() {
        return new ReflectionTransitionGuardSetCreator<K>();
    }

    public static TransitionGuardClosure $guardClosure(String id) {
        return new DefaultTransitionGuardClosure(id);
    }

    public static void $sleep(long millis) {
        if (Flags.DEBUG) {
            System.out.println("Sleeping for [" + millis + "] millis.");
        }

        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (Flags.DEBUG) {
            System.out.println("Awoke.");
        }
    }

    /**
     * @param jobs Removes the element from here once the {@link Future#get()} succeeds.
     * @return The number of tasks that completed. If everything succeeds, then this number should be the same as the
     *         input collection's size.
     */
    public static int $waitForAll(Collection<? extends Future> jobs) {
        int count = 0;

        for (Iterator<? extends Future> iterator = jobs.iterator(); iterator.hasNext();) {
            Future future = iterator.next();

            for (; ;) {
                try {
                    Object result = future.get();

                    iterator.remove();

                    count++;

                    break;
                }
                catch (InterruptedException ie) {
                }
                catch (CancellationException ce) {
                    iterator.remove();

                    break;
                }
                catch (ExecutionException ee) {
                    iterator.remove();

                    break;
                }
            }
        }

        return count;
    }

    /**
     * @param resourceProvider
     * @param clazz            Annotated with {@link LogCategory}.
     * @return
     */
    public static Logger $logger(ResourceProvider resourceProvider, Class clazz) {
        return com.tibco.cep.util.Helper.$logger(resourceProvider, clazz);
    }
}
