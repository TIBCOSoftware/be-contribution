package com.tibco.cep.pattern.matcher.impl.dsl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.impl.common.resource.DefaultId;
import com.tibco.cep.pattern.matcher.dsl.InputDefLB;
import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.dsl.PatternDefLB;
import com.tibco.cep.pattern.matcher.impl.master.PlaceholderTimeSource;
import com.tibco.cep.pattern.matcher.impl.model.AbstractNode;
import com.tibco.cep.pattern.matcher.impl.model.DefaultDynGroupEnd;
import com.tibco.cep.pattern.matcher.impl.model.DefaultDynGroupMemberEnd;
import com.tibco.cep.pattern.matcher.impl.model.DefaultDynGroupMemberOwner;
import com.tibco.cep.pattern.matcher.impl.model.DefaultDynGroupMemberStart;
import com.tibco.cep.pattern.matcher.impl.model.DefaultDynGroupStart;
import com.tibco.cep.pattern.matcher.impl.model.DefaultInputDef;
import com.tibco.cep.pattern.matcher.impl.model.DefaultNode;
import com.tibco.cep.pattern.matcher.impl.model.DefaultOccurrenceGroupEnd;
import com.tibco.cep.pattern.matcher.impl.model.DefaultOccurrenceGroupStart;
import com.tibco.cep.pattern.matcher.impl.model.DefaultSilentNode;
import com.tibco.cep.pattern.matcher.impl.model.DefaultTimeInputDef;
import com.tibco.cep.pattern.matcher.impl.model.DefaultTimedGroupStart;
import com.tibco.cep.pattern.matcher.impl.model.OccursAfterTimedGroupEnd;
import com.tibco.cep.pattern.matcher.impl.model.OccursDuringTimedGroupEnd;
import com.tibco.cep.pattern.matcher.impl.model.OccursWithinTimedGroupEnd;
import com.tibco.cep.pattern.matcher.master.TransitionGuardClosure;
import com.tibco.cep.pattern.matcher.model.GroupBoundaryStart;

/*
* Author: Ashwin Jayaprakash Date: Jul 21, 2009 Time: 1:36:35 PM
*/
public class DefaultPatternDef
        implements PatternDef<DefaultInputDef, DefaultPatternDef, AbstractNode> {
    protected AbstractNode[] firstNodes;

    protected AbstractNode[] lastNodes;

    protected boolean firstAndLastSame;

    protected LinkedList<DefaultTimeInputDef> timeInputs;

    protected LinkedList<TransitionGuardClosure> transitionGuardClosures;

    protected LinkedList<GroupBoundaryStart> childGroups;

    public DefaultPatternDef() {
        this.firstAndLastSame = true;
    }

    public AbstractNode[] getFirstNodes() {
        return firstNodes;
    }

    public AbstractNode[] getLastNodes() {
        return lastNodes;
    }

    /**
     * @return <code>true</code> if the {@link #getFirstNodes()} and the {@link #getLastNodes()} are
     *         equal/same.
     */
    public boolean isFirstAndLastSame() {
        return firstAndLastSame;
    }

    /**
     * @return <code>null</code> or a list of all the time-input-defs that are in use in all the
     *         nested patterns and in the current instance.
     */
    public List<DefaultTimeInputDef> getTimeInputs() {
        return timeInputs;
    }

    /**
     * @return <code>null</code> or a list of all the transition-colsures that are in use in all the
     *         nested patterns and in the current instance.
     */
    public List<? extends TransitionGuardClosure> getTransitionGuardClosures() {
        return transitionGuardClosures;
    }

    public List<? extends GroupBoundaryStart> getChildGroups() {
        return childGroups;
    }

    //-------------

    private static AbstractNode[] listToArray(LinkedList<AbstractNode> list) {
        int size = list.size();

        return (size > 0) ? list.toArray(new AbstractNode[size]) : null;
    }

    private void connectToExistingChain(AbstractNode... nodes) {
        if (lastNodes != null) {
            for (AbstractNode lastNode : lastNodes) {
                lastNode.setNext(nodes);
            }
        }

        //-------------

        if (this.firstNodes == null) {
            //This is the beginning. First is same as the last.
            this.firstNodes = nodes;

            firstAndLastSame = true;
        }
        else {
            firstAndLastSame = false;
        }
    }

    /**
     * We do this because running an equals() test aginst the firstNodes every time would be
     * expensive.
     *
     * @param nodes
     */
    private void setAsDifferentLastNodes(AbstractNode... nodes) {
        setAsLastNodes(nodes);

        firstAndLastSame = false;
    }

    private void setAsLastNodes(AbstractNode... nodes) {
        this.lastNodes = nodes;
    }

    //-------------

    public DefaultPatternDef startsWith(DefaultInputDef input) {
        return then(input);
    }

    public DefaultPatternDef startsWith(DefaultInputDef input, TransitionGuardClosure closure) {
        return then(input, closure);
    }

    public DefaultPatternDef startsWith(DefaultPatternDef pattern) {
        return then(pattern);
    }

    public DefaultPatternDef startsWithAnyOne(InputDefLB<DefaultInputDef> inputs) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAnyOneInput(list);
    }

    public DefaultPatternDef startsWithAnyOne(InputDefLB<DefaultInputDef> inputs,
                                              TransitionGuardClosure closure) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAnyOneInput(list, closure);
    }

    public DefaultPatternDef startsWithAnyOne(PatternDefLB<DefaultPatternDef> patterns) {
        List<DefaultPatternDef> list = patterns.toList();

        return thenAnyOnePattern(list);
    }

    public DefaultPatternDef startsWithAll(InputDefLB<DefaultInputDef> inputs) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAllInputs(list);
    }

    public DefaultPatternDef startsWithAll(InputDefLB<DefaultInputDef> inputs,
                                           TransitionGuardClosure closure) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAllInputs(list, closure);
    }

    public DefaultPatternDef startsWithAll(PatternDefLB<DefaultPatternDef> patterns) {
        List<DefaultPatternDef> list = patterns.toList();

        return thenAllPatterns(list);
    }

    public DefaultPatternDef startsWith(DefaultInputDef input, int min, int max) {
        return then(input, min, max);
    }

    public DefaultPatternDef startsWith(DefaultInputDef input, int min, int max,
                                        TransitionGuardClosure closure) {
        return then(input, min, max, closure);
    }

    public DefaultPatternDef startsWith(DefaultPatternDef pattern, int min, int max) {
        return then(pattern, min, max);
    }

    //-------------

    public DefaultPatternDef then(DefaultInputDef input) {
        DefaultNode node = new DefaultNode();
        node.setInputDef(input);

        connectToExistingChain(node);

        setAsLastNodes(node);

        return this;
    }

    public DefaultPatternDef then(DefaultInputDef input, TransitionGuardClosure closure) {
        DefaultSilentNode silentNode = new DefaultSilentNode(closure);

        addTransitionGuardClosure(closure);

        //--------------------

        DefaultNode actualNode = new DefaultNode();
        actualNode.setInputDef(input);
        silentNode.addNext(actualNode);

        //--------------------

        connectToExistingChain(silentNode);

        setAsDifferentLastNodes(actualNode);

        return this;
    }

    public DefaultPatternDef then(DefaultPatternDef pattern) {
        AbstractNode[] theFirstNodes = pattern.getFirstNodes();

        connectToExistingChain(theFirstNodes);

        AbstractNode[] theLastNodes = pattern.getLastNodes();
        if (pattern.isFirstAndLastSame()) {
            setAsLastNodes(theLastNodes);
        }
        else {
            setAsDifferentLastNodes(theLastNodes);
        }

        addTimeInputs(pattern.getTimeInputs());
        addTransitionGuardClosures(pattern.getTransitionGuardClosures());
        addChildGroups(pattern.getChildGroups());

        return this;
    }

    public DefaultPatternDef thenAnyOne(InputDefLB<DefaultInputDef> inputs) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAnyOneInput(list);
    }

    public DefaultPatternDef thenAnyOne(InputDefLB<DefaultInputDef> inputs,
                                        TransitionGuardClosure closure) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAnyOneInput(list, closure);
    }

    public DefaultPatternDef thenAnyOne(PatternDefLB<DefaultPatternDef> patterns) {
        List<DefaultPatternDef> list = patterns.toList();

        return thenAnyOnePattern(list);
    }

    protected DefaultPatternDef thenAnyOneInput(List<DefaultInputDef> inputs) {
        //todo Validate there is no ambiguity/repetition here - ab|ac

        DefaultNode[] nodes = new DefaultNode[inputs.size()];
        int i = 0;
        for (DefaultInputDef input : inputs) {
            nodes[i] = new DefaultNode();
            nodes[i].setInputDef(input);

            i++;
        }

        connectToExistingChain(nodes);

        setAsLastNodes(nodes);

        return this;
    }

    protected DefaultPatternDef thenAnyOneInput(List<DefaultInputDef> inputs,
                                                TransitionGuardClosure closure) {
        //todo Validate there is no ambiguity/repetition here - ab|ac

        DefaultSilentNode silentNode = new DefaultSilentNode(closure);

        addTransitionGuardClosure(closure);

        //--------------------

        DefaultNode[] nodes = new DefaultNode[inputs.size()];
        int i = 0;
        for (DefaultInputDef input : inputs) {
            nodes[i] = new DefaultNode();
            nodes[i].setInputDef(input);

            i++;
        }

        silentNode.setNext(nodes);

        connectToExistingChain(silentNode);

        setAsDifferentLastNodes(nodes);

        return this;
    }

    protected DefaultPatternDef thenAnyOnePattern(List<DefaultPatternDef> patterns) {
        //todo Validate there is no ambiguity/repetition here - (a|b)|(a|b|c)

        LinkedList<AbstractNode> list = new LinkedList<AbstractNode>();
        boolean fIsL = true;

        for (DefaultPatternDef pattern : patterns) {
            AbstractNode[] theFirstNodes = pattern.getFirstNodes();

            for (AbstractNode theFirstNode : theFirstNodes) {
                list.add(theFirstNode);
            }

            fIsL = fIsL && pattern.isFirstAndLastSame();

            addTimeInputs(pattern.getTimeInputs());
            addTransitionGuardClosures(pattern.getTransitionGuardClosures());
            addChildGroups(pattern.getChildGroups());
        }

        AbstractNode[] allTheFirstNodes = listToArray(list);
        connectToExistingChain(allTheFirstNodes);

        list.clear();

        //-------------

        if (fIsL) {
            setAsLastNodes(allTheFirstNodes);
        }
        else {
            for (DefaultPatternDef pattern : patterns) {
                AbstractNode[] theLastNodes = pattern.getLastNodes();

                for (AbstractNode theLastNode : theLastNodes) {
                    list.add(theLastNode);
                }
            }

            AbstractNode[] allTheLastNodes = listToArray(list);
            setAsDifferentLastNodes(allTheLastNodes);
        }

        list.clear();

        return this;
    }

    public DefaultPatternDef thenAll(InputDefLB<DefaultInputDef> inputs) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAllInputs(list);
    }

    public DefaultPatternDef thenAll(InputDefLB<DefaultInputDef> inputs,
                                     TransitionGuardClosure closure) {
        List<DefaultInputDef> list = inputs.toList();

        return thenAllInputs(list, closure);
    }

    protected DefaultPatternDef thenAllInputs(List<DefaultInputDef> inputs) {
        Id groupId = new DefaultId("all-" + System.identityHashCode(inputs));

        LinkedList<DefaultDynGroupMemberOwner> groupMemberOwners =
                new LinkedList<DefaultDynGroupMemberOwner>();
        for (DefaultInputDef input : inputs) {
            DefaultDynGroupMemberStart memberStart = new DefaultDynGroupMemberStart();

            DefaultNode memberNode = new DefaultNode();
            memberNode.setInputDef(input);
            memberStart.addNext(memberNode);

            DefaultDynGroupMemberEnd memberEnd = new DefaultDynGroupMemberEnd();
            memberNode.addNext(memberEnd);

            DefaultDynGroupMemberOwner memberOwner = new DefaultDynGroupMemberOwner();
            memberOwner.setStart(memberStart);
            memberOwner.setEnd(memberEnd);

            groupMemberOwners.add(memberOwner);
        }

        DefaultDynGroupStart dynGroupStart = new DefaultDynGroupStart();
        dynGroupStart.setGroupId(groupId);
        dynGroupStart.setGroupMemberOwners(groupMemberOwners);

        DefaultDynGroupEnd dynGroupEnd = new DefaultDynGroupEnd();
        dynGroupEnd.setGroupId(groupId);
        dynGroupEnd.setGroupStart(dynGroupStart);
        dynGroupStart.addNext(dynGroupEnd);
        dynGroupStart.setGroupEnd(dynGroupEnd);

        //--------------------

        connectToExistingChain(dynGroupStart);
        setAsDifferentLastNodes(dynGroupEnd);
        addChildGroup(dynGroupStart);

        return this;
    }

    protected DefaultPatternDef thenAllInputs(List<DefaultInputDef> inputs,
                                              TransitionGuardClosure closure) {
        addTransitionGuardClosure(closure);

        //--------------------

        Id groupId = new DefaultId("all-" + System.identityHashCode(inputs));

        LinkedList<DefaultDynGroupMemberOwner> groupMemberOwners =
                new LinkedList<DefaultDynGroupMemberOwner>();
        for (DefaultInputDef input : inputs) {
            DefaultDynGroupMemberStart memberStart = new DefaultDynGroupMemberStart();

            DefaultSilentNode silentNode = new DefaultSilentNode(closure);
            memberStart.addNext(silentNode);

            DefaultNode memberNode = new DefaultNode();
            memberNode.setInputDef(input);
            silentNode.addNext(memberNode);

            DefaultDynGroupMemberEnd memberEnd = new DefaultDynGroupMemberEnd();
            memberNode.addNext(memberEnd);

            DefaultDynGroupMemberOwner memberOwner = new DefaultDynGroupMemberOwner();
            memberOwner.setStart(memberStart);
            memberOwner.setEnd(memberEnd);

            groupMemberOwners.add(memberOwner);
        }

        DefaultDynGroupStart dynGroupStart = new DefaultDynGroupStart();
        dynGroupStart.setGroupId(groupId);
        dynGroupStart.setGroupMemberOwners(groupMemberOwners);

        DefaultDynGroupEnd dynGroupEnd = new DefaultDynGroupEnd();
        dynGroupEnd.setGroupId(groupId);
        dynGroupEnd.setGroupStart(dynGroupStart);
        dynGroupStart.addNext(dynGroupEnd);
        dynGroupStart.setGroupEnd(dynGroupEnd);

        //--------------------

        connectToExistingChain(dynGroupStart);
        setAsDifferentLastNodes(dynGroupEnd);
        addChildGroup(dynGroupStart);

        return this;
    }

    public DefaultPatternDef thenAll(PatternDefLB<DefaultPatternDef> patterns) {
        List<DefaultPatternDef> list = patterns.toList();

        return thenAllPatterns(list);
    }

    protected DefaultPatternDef thenAllPatterns(List<DefaultPatternDef> patterns) {
        Id groupId = new DefaultId("all-" + System.identityHashCode(patterns));

        LinkedList<DefaultDynGroupMemberOwner> groupMemberOwners =
                new LinkedList<DefaultDynGroupMemberOwner>();

        for (DefaultPatternDef pattern : patterns) {
            DefaultDynGroupMemberStart memberStart = new DefaultDynGroupMemberStart();

            //--------------------

            AbstractNode[] theFirstNodes = pattern.getFirstNodes();
            memberStart.setNext(theFirstNodes);

            //--------------------

            DefaultDynGroupMemberEnd memberEnd = new DefaultDynGroupMemberEnd();

            AbstractNode[] theLastNodes = pattern.getLastNodes();
            for (AbstractNode theLastNode : theLastNodes) {
                theLastNode.addNext(memberEnd);
            }

            //--------------------

            DefaultDynGroupMemberOwner memberOwner = new DefaultDynGroupMemberOwner();
            memberOwner.setStart(memberStart);
            memberOwner.setEnd(memberEnd);

            groupMemberOwners.add(memberOwner);

            addTimeInputs(pattern.getTimeInputs());
            addTransitionGuardClosures(pattern.getTransitionGuardClosures());
        }

        DefaultDynGroupStart dynGroupStart = new DefaultDynGroupStart();
        dynGroupStart.setGroupId(groupId);
        dynGroupStart.setGroupMemberOwners(groupMemberOwners);

        DefaultDynGroupEnd dynGroupEnd = new DefaultDynGroupEnd();
        dynGroupEnd.setGroupId(groupId);
        dynGroupEnd.setGroupStart(dynGroupStart);
        dynGroupStart.addNext(dynGroupEnd);
        dynGroupStart.setGroupEnd(dynGroupEnd);

        for (DefaultPatternDef pattern : patterns) {
            dynGroupStart.addChildGroups(pattern.getChildGroups());
        }
        addChildGroup(dynGroupStart);

        //--------------------

        connectToExistingChain(dynGroupStart);
        setAsDifferentLastNodes(dynGroupEnd);

        return this;
    }

    //-------------

    public DefaultPatternDef then(DefaultInputDef input, int min, int max) {
        Id groupId = new DefaultId(
                "repeat-{min-" + min + "}-{max-" + max + "}-" + System.identityHashCode(input));

        DefaultOccurrenceGroupStart occurrenceGroupStart = new DefaultOccurrenceGroupStart();
        occurrenceGroupStart.setMinOccurrence(min);
        occurrenceGroupStart.setMaxOccurrence(max);
        occurrenceGroupStart.setGroupId(groupId);

        DefaultNode node = new DefaultNode();
        node.setInputDef(input);
        occurrenceGroupStart.addNext(node);

        DefaultOccurrenceGroupEnd occurrenceGroupEnd = new DefaultOccurrenceGroupEnd();
        occurrenceGroupEnd.setGroupId(groupId);
        occurrenceGroupEnd.setGroupStart(occurrenceGroupStart);
        occurrenceGroupStart.setGroupEnd(occurrenceGroupEnd);
        node.addNext(occurrenceGroupEnd);

        addChildGroup(occurrenceGroupStart);

        //--------------------

        connectToExistingChain(occurrenceGroupStart);
        setAsDifferentLastNodes(occurrenceGroupEnd);

        return this;
    }

    public DefaultPatternDef then(DefaultInputDef input, int min, int max,
                                  TransitionGuardClosure closure) {
        addTransitionGuardClosure(closure);

        //--------------------

        Id groupId = new DefaultId(
                "repeat-{min-" + min + "}-{max-" + max + "}-" + System.identityHashCode(input));

        DefaultOccurrenceGroupStart occurrenceGroupStart = new DefaultOccurrenceGroupStart();
        occurrenceGroupStart.setMinOccurrence(min);
        occurrenceGroupStart.setMaxOccurrence(max);
        occurrenceGroupStart.setGroupId(groupId);

        DefaultSilentNode silentNode = new DefaultSilentNode(closure);
        occurrenceGroupStart.addNext(silentNode);

        DefaultNode node = new DefaultNode();
        node.setInputDef(input);
        silentNode.addNext(node);

        DefaultOccurrenceGroupEnd occurrenceGroupEnd = new DefaultOccurrenceGroupEnd();
        occurrenceGroupEnd.setGroupId(groupId);
        occurrenceGroupEnd.setGroupStart(occurrenceGroupStart);
        occurrenceGroupStart.setGroupEnd(occurrenceGroupEnd);
        node.addNext(occurrenceGroupEnd);

        addChildGroup(occurrenceGroupStart);

        //--------------------

        connectToExistingChain(occurrenceGroupStart);
        setAsDifferentLastNodes(occurrenceGroupEnd);

        return this;
    }

    public DefaultPatternDef then(DefaultPatternDef pattern, int min, int max) {
        Id groupId = new DefaultId(
                "repeat-{min-" + min + "}-{max-" + max + "}-" + System.identityHashCode(pattern));

        DefaultOccurrenceGroupStart occurrenceGroupStart = new DefaultOccurrenceGroupStart();
        occurrenceGroupStart.setMinOccurrence(min);
        occurrenceGroupStart.setMaxOccurrence(max);
        occurrenceGroupStart.setGroupId(groupId);

        //--------------------

        AbstractNode[] theFirstNodes = pattern.getFirstNodes();
        occurrenceGroupStart.setNext(theFirstNodes);

        //--------------------

        DefaultOccurrenceGroupEnd occurrenceGroupEnd = new DefaultOccurrenceGroupEnd();
        occurrenceGroupEnd.setGroupId(groupId);
        occurrenceGroupEnd.setGroupStart(occurrenceGroupStart);
        occurrenceGroupStart.setGroupEnd(occurrenceGroupEnd);

        //--------------------

        AbstractNode[] theLastNodes = pattern.getLastNodes();
        for (AbstractNode theLastNode : theLastNodes) {
            theLastNode.addNext(occurrenceGroupEnd);
        }

        //--------------------

        connectToExistingChain(occurrenceGroupStart);
        setAsDifferentLastNodes(occurrenceGroupEnd);

        addTimeInputs(pattern.getTimeInputs());
        addTransitionGuardClosures(pattern.getTransitionGuardClosures());

        occurrenceGroupStart.addChildGroups(pattern.getChildGroups());
        addChildGroup(occurrenceGroupStart);

        return this;
    }

    public DefaultPatternDef thenWithin(long delay, TimeUnit delayUnit,
                                        DefaultInputDef input) {
        Id groupId = new DefaultId(
                "within-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        System.identityHashCode(input));

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        DefaultNode node = new DefaultNode();
        node.setInputDef(input);
        timedGroupStart.addNext(node);

        //--------------------

        OccursWithinTimedGroupEnd timedGroupEnd = new OccursWithinTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);
        node.addNext(timedGroupEnd);

        //--------------------

        addTimeInput(timeInput);

        addChildGroup(timedGroupStart);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        return this;
    }

    public DefaultPatternDef thenWithin(long delay, TimeUnit delayUnit,
                                        DefaultInputDef input, TransitionGuardClosure closure) {
        addTransitionGuardClosure(closure);

        //--------------------        

        Id groupId = new DefaultId(
                "within-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        System.identityHashCode(input));

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        DefaultSilentNode silentNode = new DefaultSilentNode(closure);
        timedGroupStart.addNext(silentNode);

        DefaultNode node = new DefaultNode();
        node.setInputDef(input);
        silentNode.addNext(node);

        //--------------------

        OccursWithinTimedGroupEnd timedGroupEnd = new OccursWithinTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);
        node.addNext(timedGroupEnd);

        //--------------------

        addTimeInput(timeInput);

        addChildGroup(timedGroupStart);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        return this;
    }

    public DefaultPatternDef thenWithin(long delay, TimeUnit delayUnit,
                                        DefaultPatternDef pattern) {
        Id groupId = new DefaultId(
                "within-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        System.identityHashCode(pattern));

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        AbstractNode[] theFirstNodes = pattern.getFirstNodes();
        timedGroupStart.setNext(theFirstNodes);

        //--------------------

        OccursWithinTimedGroupEnd timedGroupEnd = new OccursWithinTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);

        //--------------------

        AbstractNode[] theLastNodes = pattern.getLastNodes();
        for (AbstractNode theLastNode : theLastNodes) {
            theLastNode.addNext(timedGroupEnd);
        }

        //--------------------

        addTimeInput(timeInput);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        addTimeInputs(pattern.getTimeInputs());
        addTransitionGuardClosures(pattern.getTransitionGuardClosures());

        timedGroupStart.addChildGroups(pattern.getChildGroups());
        addChildGroup(timedGroupStart);

        return this;
    }

    public DefaultPatternDef thenDuring(long delay, TimeUnit delayUnit,
                                        DefaultInputDef input) {
        Id groupId = new DefaultId(
                "during-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        System.identityHashCode(input));

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        DefaultNode node = new DefaultNode();
        node.setInputDef(input);
        timedGroupStart.addNext(node);

        //--------------------

        OccursDuringTimedGroupEnd timedGroupEnd = new OccursDuringTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);
        node.addNext(timedGroupEnd);

        //--------------------

        addTimeInput(timeInput);

        addChildGroup(timedGroupStart);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        return this;
    }

    public DefaultPatternDef thenDuring(long delay, TimeUnit delayUnit, DefaultInputDef input,
                                        TransitionGuardClosure closure) {
        addTransitionGuardClosure(closure);

        //--------------------

        Id groupId = new DefaultId(
                "during-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        System.identityHashCode(input));

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        DefaultSilentNode silentNode = new DefaultSilentNode(closure);
        timedGroupStart.addNext(silentNode);

        DefaultNode node = new DefaultNode();
        node.setInputDef(input);
        silentNode.addNext(node);

        //--------------------

        OccursDuringTimedGroupEnd timedGroupEnd = new OccursDuringTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);
        node.addNext(timedGroupEnd);

        //--------------------

        addTimeInput(timeInput);

        addChildGroup(timedGroupStart);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        return this;
    }

    public DefaultPatternDef thenDuring(long delay, TimeUnit delayUnit,
                                        DefaultPatternDef pattern) {
        Id groupId = new DefaultId(
                "during-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        System.identityHashCode(pattern));

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        AbstractNode[] theFirstNodes = pattern.getFirstNodes();
        timedGroupStart.setNext(theFirstNodes);

        //--------------------

        OccursDuringTimedGroupEnd timedGroupEnd = new OccursDuringTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);

        //--------------------

        AbstractNode[] theLastNodes = pattern.getLastNodes();
        for (AbstractNode theLastNode : theLastNodes) {
            theLastNode.addNext(timedGroupEnd);
        }

        //--------------------

        addTimeInput(timeInput);

        timedGroupStart.addChildGroups(pattern.getChildGroups());
        addChildGroup(timedGroupStart);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        addTimeInputs(pattern.getTimeInputs());
        addTransitionGuardClosures(pattern.getTransitionGuardClosures());

        return this;
    }

    public DefaultPatternDef thenAfter(long delay, TimeUnit delayUnit) {
        long someRandomLong = System.nanoTime() ^ System.identityHashCode(this);
        Id groupId = new DefaultId(
                "after-{delay-" + delay + "-" + delayUnit.name() + "}-" +
                        someRandomLong);

        DefaultTimedGroupStart timedGroupStart = new DefaultTimedGroupStart();
        timedGroupStart.setDurationMillis(TimeUnit.MILLISECONDS.convert(delay, delayUnit));
        timedGroupStart.setGroupId(groupId);

        //--------------------

        OccursAfterTimedGroupEnd timedGroupEnd = new OccursAfterTimedGroupEnd();
        timedGroupEnd.setGroupId(groupId);
        timedGroupEnd.setGroupStart(timedGroupStart);

        DefaultTimeInputDef timeInput = new DefaultTimeInputDef();
        timeInput.setSource(new PlaceholderTimeSource(groupId));
        timedGroupEnd.setInputDef(timeInput);

        timedGroupStart.setGroupEnd(timedGroupEnd);
        timedGroupStart.addNext(timedGroupEnd);

        //--------------------

        addTimeInput(timeInput);

        addChildGroup(timedGroupStart);

        //--------------------

        connectToExistingChain(timedGroupStart);
        setAsDifferentLastNodes(timedGroupEnd);

        return this;
    }

    protected void addTimeInput(DefaultTimeInputDef timeInput) {
        if (timeInputs == null) {
            timeInputs = new LinkedList<DefaultTimeInputDef>();
        }

        timeInputs.add(timeInput);
    }

    /**
     * @param timeInputs Can be <code>null</code>.
     */
    protected void addTimeInputs(Collection<DefaultTimeInputDef> timeInputs) {
        if (timeInputs == null) {
            return;
        }

        for (DefaultTimeInputDef timeInput : timeInputs) {
            addTimeInput(timeInput);
        }
    }

    protected void addTransitionGuardClosure(TransitionGuardClosure closure) {
        if (transitionGuardClosures == null) {
            transitionGuardClosures = new LinkedList<TransitionGuardClosure>();
        }

        transitionGuardClosures.add(closure);
    }

    /**
     * @param closures Can be <code>null</code>.
     */
    protected void addTransitionGuardClosures(
            Collection<? extends TransitionGuardClosure> closures) {
        if (closures == null) {
            return;
        }

        for (TransitionGuardClosure closure : closures) {
            addTransitionGuardClosure(closure);
        }
    }

    protected void addChildGroup(GroupBoundaryStart boundaryStart) {
        if (childGroups == null) {
            childGroups = new LinkedList<GroupBoundaryStart>();
        }

        childGroups.add(boundaryStart);
    }

    /**
     * @param boundaryStarts Can be <code>null</code>.
     */
    protected void addChildGroups(Collection<? extends GroupBoundaryStart> boundaryStarts) {
        if (boundaryStarts == null) {
            return;
        }

        for (GroupBoundaryStart boundaryStart : boundaryStarts) {
            addChildGroup(boundaryStart);
        }
    }
}