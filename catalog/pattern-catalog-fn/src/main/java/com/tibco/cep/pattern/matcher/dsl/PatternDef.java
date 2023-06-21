package com.tibco.cep.pattern.matcher.dsl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.matcher.model.GroupBoundaryStart;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.matcher.model.Node;
import com.tibco.cep.util.annotation.Optional;

/*
* Author: Ashwin Jayaprakash Date: Jul 20, 2009 Time: 5:40:59 PM
*/
public interface PatternDef<I extends InputDef, P extends PatternDef, N extends Node> {
    N[] getFirstNodes();

    N[] getLastNodes();

    /**
     * @return <code>null</code> or a list of all the transition-closures that are in use in all the
     *         nested patterns and in the current instance.
     */
    @Optional
    List<? extends TransitionGuardClosure> getTransitionGuardClosures();

    /**
     * @return All the sibling chold groups contained in this pattern definition.
     */
    @Optional
    List<? extends GroupBoundaryStart> getChildGroups();

    //--------------

    P startsWith(I input);

    /**
     * @param input
     * @param closure Gets called before making the actual transition.
     * @return
     */
    P startsWith(I input, TransitionGuardClosure closure);

    P startsWith(P pattern);

    P startsWithAnyOne(InputDefLB<I> inputs);

    /**
     * @param inputs
     * @param closure Gets called before making the actual transition.
     * @return
     */
    P startsWithAnyOne(InputDefLB<I> inputs, TransitionGuardClosure closure);

    P startsWithAnyOne(PatternDefLB<P> patterns);

    P startsWithAll(InputDefLB<I> inputs);

    /**
     * @param inputs
     * @param closure Gets called before making the actual transition for each and every item in the
     *                list.
     * @return
     */
    P startsWithAll(InputDefLB<I> inputs, TransitionGuardClosure closure);

    P startsWithAll(PatternDefLB<P> patterns);

    /**
     * @param input
     * @param min
     * @param max
     * @return
     */
    P startsWith(I input, int min, int max);

    /**
     * @param input
     * @param min
     * @param max
     * @param closure
     * @return
     */
    P startsWith(I input, int min, int max, TransitionGuardClosure closure);

    P startsWith(P pattern, int min, int max);

    //-------------

    P then(I input, TransitionGuardClosure closure);

    P then(I input);

    P then(P pattern);

    /*
    Why a ListBuilder?

    First because Genericised arrays don't work in Java.

    So, Var-args on the Genericised parameter will not work. Which means a simple
    thenAnyOne(I... inputs) and thenAnyOne(P... patterns) will throw a ClassCastException because
    if someone is using the "erased" API then InputDef[] cannot be cast to I[]. It starts from here.

    To overcome this, I switched to a ListBuilder. But ListBuilder<I> and ListBuilder<P> both result
    in the same type due to erasure. So, the 2 methods will not compile. They have to use different
    class types. Aarrgh...
    */

    P thenAnyOne(InputDefLB<I> inputs);

    P thenAnyOne(InputDefLB<I> inputs, TransitionGuardClosure closure);

    P thenAnyOne(PatternDefLB<P> patterns);

    P thenAll(InputDefLB<I> inputs);

    P thenAll(InputDefLB<I> inputs, TransitionGuardClosure closure);

    P thenAll(PatternDefLB<P> patterns);

    //-------------

    P then(I input, int min, int max);

    P then(I input, int min, int max, TransitionGuardClosure closure);

    P then(P pattern, int min, int max);

    //-------------

    /**
     * If there is a step after this, and the expected input occurs within the time specified, then
     * the pattern will move to the next step even before the specified end time.
     *
     * @param delay
     * @param delayUnit
     * @param input
     * @return
     */
    P thenWithin(long delay, TimeUnit delayUnit, I input);

    P thenWithin(long delay, TimeUnit delayUnit, I input, TransitionGuardClosure closure);

    /**
     * If there is a step after this, and the expected pattern occurs within the time specified,
     * then the pattern will move to the next step even before the specified end time.
     *
     * @param delay
     * @param delayUnit
     * @param pattern
     * @return
     */
    P thenWithin(long delay, TimeUnit delayUnit, P pattern);

    /**
     * If there is a step after this, and the expected input occurs within the time specified, then
     * the pattern will not move to the next step until after the specified end time.
     *
     * @param delay
     * @param delayUnit
     * @param input
     * @return
     */
    P thenDuring(long delay, TimeUnit delayUnit, I input);

    P thenDuring(long delay, TimeUnit delayUnit, I input, TransitionGuardClosure closure);

    /**
     * If there is a step after this, and the expected pattern occurs within the time specified,
     * then the pattern will not move to the next step until after the specified end time.
     *
     * @param delay
     * @param delayUnit
     * @param pattern
     * @return
     */
    P thenDuring(long delay, TimeUnit delayUnit, P pattern);

    /**
     * Similar to {@link #thenDuring(long, TimeUnit, InputDef)} but does not expect any input
     * between the specified start and end times.
     *
     * @param delay
     * @param delayUnit
     * @return
     */
    P thenAfter(long delay, TimeUnit delayUnit);
}
