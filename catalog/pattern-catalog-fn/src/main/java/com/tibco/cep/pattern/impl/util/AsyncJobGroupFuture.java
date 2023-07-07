package com.tibco.cep.pattern.impl.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
* Author: Ashwin Jayaprakash Date: Sep 1, 2009 Time: 5:58:29 PM
*/
public class AsyncJobGroupFuture<T>
        implements Future<Collection<T>>, AsyncJobCompletionListener<T> {
    protected Collection<AsyncJobMember<T>> jobMembers;

    protected CountDownLatch completedJobCounter;

    /**
     * @param jobMembers If all the jobs are known during construction.
     */
    public AsyncJobGroupFuture(Collection<AsyncJobMember<T>> jobMembers) {
        this.jobMembers = jobMembers;
        this.completedJobCounter = new CountDownLatch(jobMembers.size());
    }

    /**
     * @param expectedJobMemberCount If only the total number of jobs are known but the actual jobs
     *                               are available later (before scheduling). The jobs can be added
     *                               one by one using {@link #getJobMembers()} but their final count
     *                               must be exactly equal to this initial number.
     */
    public AsyncJobGroupFuture(int expectedJobMemberCount) {
        this.jobMembers = new LinkedList<AsyncJobMember<T>>();
        this.completedJobCounter = new CountDownLatch(expectedJobMemberCount);
    }

    /**
     * No runtime checks will ber performed to check the actual number of jobs added against the
     * initial number specified.
     *
     * @param jobMember
     * @see #AsyncJobGroupFuture(int)
     */
    public void addJobMember(AsyncJobMember<T> jobMember) {
        jobMembers.add(jobMember);
    }

    public Collection<AsyncJobMember<T>> getJobMembers() {
        return jobMembers;
    }

    public long getCompletedJobCount() {
        return completedJobCounter.getCount();
    }

    //---------------

    /**
     * This method does nothing.
     *
     * @param mayInterruptIfRunning
     * @return Always <code>false</code>.
     */
    public final boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * This method does nothing.
     *
     * @return Always <code>false</code>.
     */
    public final boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return (jobMembers.size() == completedJobCounter.getCount());
    }

    /**
     * Waits until the {@link #completedJobCounter} reaches 0. See {@link #onSuccess(AsyncJobMember,
     * Object)} and {@link #onFailure(AsyncJobMember, Exception)}.
     *
     * @return Always <code>null</code>.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public Collection<T> get() throws InterruptedException, ExecutionException {
        completedJobCounter.await();

        return null;
    }

    /**
     * Waits until the {@link #completedJobCounter} reaches 0 or the specified time elapses. See
     * {@link #onSuccess(AsyncJobMember, Object)} and {@link #onFailure(AsyncJobMember,
     * Exception)}.
     *
     * @param timeout
     * @param unit
     * @return Always <code>null</code>.
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public Collection<T> get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        completedJobCounter.await(timeout, unit);

        return null;
    }

    //---------------

    /**
     * Calls {@link CountDownLatch#countDown()} on {@link #completedJobCounter}.
     *
     * @param member
     * @param exception
     */
    public void onFailure(AsyncJobMember<T> member, Exception exception) {
        completedJobCounter.countDown();

        if (Flags.DEBUG) {
            exception.printStackTrace();
        }
    }

    /**
     * Calls {@link CountDownLatch#countDown()} on {@link #completedJobCounter}.
     *
     * @param member
     * @param t
     */
    public void onSuccess(AsyncJobMember<T> member, T t) {
        completedJobCounter.countDown();
    }

    //---------------

    /**
     * Calls {@link AsyncJobMember#cleanup()} on all the members and removes items from the {@link
     * #getJobMembers() collection}.
     */
    public void cleanup() {
        for (Iterator<AsyncJobMember<T>> iterator = jobMembers.iterator(); iterator.hasNext();) {
            AsyncJobMember<T> jobMember = iterator.next();

            jobMember.cleanup();

            iterator.remove();
        }

        jobMembers = null;

        completedJobCounter = null;
    }
}
