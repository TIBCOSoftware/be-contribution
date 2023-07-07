package com.tibco.cep.pattern.integ.impl.dsl2;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Event;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Function;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Identifier;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.TimeIdentifier;

/*
* Author: Ashwin Jayaprakash Date: Sep 17, 2009 Time: 5:49:03 PM
*/

//todo Implement toString()


public class Definition implements Serializable {
    public String uri;

    public Source[] sources;

    public Pattern pattern;

    public Function success;

    public Function failure;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Source[] getSources() {
        return sources;
    }

    public void setSources(Source[] sources) {
        this.sources = sources;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Function getSuccess() {
        return success;
    }

    public void setSuccess(Function success) {
        this.success = success;
    }

    public Function getFailure() {
        return failure;
    }

    public void setFailure(Function failure) {
        this.failure = failure;
    }

    //-----------------

    public static class Source implements Serializable {
        public String alias;

        public Event event;

        public Subscription subscription;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
        }

        public Subscription getSubscription() {
            return subscription;
        }

        public void setSubscription(Subscription subscription) {
            this.subscription = subscription;
        }

        //-----------------

        public static interface Subscription extends Serializable {
        }

        public static class SubscriptionCorrelate implements Subscription {
            public String field;

            public SubscriptionCorrelate() {
            }

            public SubscriptionCorrelate(String field) {
                this.field = field;
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }
        }

        public static class SubscriptionMatch implements Subscription {
            public String field;

            public Identifier with;

            public SubscriptionMatch() {
            }

            public SubscriptionMatch(String field, Identifier with) {
                this.field = field;
                this.with = with;
            }

            public String getField() {
                return field;
            }

            public void setField(String field) {
                this.field = field;
            }

            public Identifier getWith() {
                return with;
            }

            public void setWith(Identifier with) {
                this.with = with;
            }
        }
    }

    //-----------------

    public static class Pattern {
        public PatternMembers<? extends PatternMember> members;

        public Pattern() {
        }

        public Pattern(PatternMembers<? extends PatternMember> members) {
            this.members = members;
        }

        public PatternMembers<? extends PatternMember> getMembers() {
            return members;
        }

        public void setMembers(PatternMembers<? extends PatternMember> members) {
            this.members = members;
        }

        //-----------------

        public static interface PatternMember extends Serializable {
            boolean isSpecial();
        }

        public static class PatternMembers<M extends PatternMember>
                implements PatternMember, Iterable<M> {
            public List<M> list;

            public PatternMembers() {
            }

            public PatternMembers(List<M> list) {
                this.list = list;
            }

            public PatternMembers(M member) {
                doAdd(member);
            }

            private void doAdd(M member) {
                if (list == null) {
                    list = new LinkedList<M>();
                }

                list.add(member);
            }

            public final boolean isSpecial() {
                return true;
            }

            public List<M> getList() {
                return list;
            }

            public void setList(List<M> list) {
                this.list = list;
            }

            /**
             * @param member
             * @return <code>this</code>.
             */
            public PatternMembers<M> addMember(M member) {
                doAdd(member);

                return this;
            }

            public Iterator<M> iterator() {
                return (list == null) ? null : list.iterator();
            }

            public int size() {
                return (list == null) ? 0 : list.size();
            }
        }

        /**
         * References {@link Source}.
         */
        public static class SourceRef implements PatternMember {
            public String sourceAlias;

            public SourceRef() {
            }

            public SourceRef(String sourceAlias) {
                this.sourceAlias = sourceAlias;
            }

            public final boolean isSpecial() {
                return true;
            }

            public String getSourceAlias() {
                return sourceAlias;
            }

            public void setSourceAlias(String sourceAlias) {
                this.sourceAlias = sourceAlias;
            }
        }

        public abstract static class NonSpecialPatternMember implements PatternMember {
            public final boolean isSpecial() {
                return false;
            }
        }

        public static class Then extends NonSpecialPatternMember {
            public PatternMember member;

            public Then() {
            }

            public Then(PatternMember member) {
                this.member = member;
            }

            public PatternMember getMember() {
                return member;
            }

            public void setMember(PatternMember member) {
                this.member = member;
            }
        }

        public static class ThenAnyOne extends NonSpecialPatternMember {
            public PatternMembers members;

            public ThenAnyOne() {
            }

            public ThenAnyOne(PatternMembers members) {
                this.members = members;
            }

            public PatternMembers getMembers() {
                return members;
            }

            public void setMembers(PatternMembers members) {
                this.members = members;
            }
        }

        public static class ThenAll extends NonSpecialPatternMember {
            public PatternMembers members;

            public ThenAll() {
            }

            public ThenAll(PatternMembers members) {
                this.members = members;
            }

            public PatternMembers getMembers() {
                return members;
            }

            public void setMembers(PatternMembers members) {
                this.members = members;
            }
        }

        public static class ThenRepeat extends NonSpecialPatternMember {
            public PatternMember member;

            public Identifier<Integer> min;

            public Identifier<Integer> max;

            public ThenRepeat() {
            }

            public ThenRepeat(PatternMember member, Identifier<Integer> min,
                              Identifier<Integer> max) {
                this.member = member;
                this.min = min;
                this.max = max;
            }

            public PatternMember getMember() {
                return member;
            }

            public void setMember(PatternMember member) {
                this.member = member;
            }

            public Identifier<Integer> getMin() {
                return min;
            }

            public void setMin(Identifier<Integer> min) {
                this.min = min;
            }

            public Identifier<Integer> getMax() {
                return max;
            }

            public void setMax(Identifier<Integer> max) {
                this.max = max;
            }
        }

        public static class ThenWithin extends NonSpecialPatternMember {
            public PatternMember member;

            public TimeIdentifier<Long> time;

            public ThenWithin() {
            }

            public ThenWithin(PatternMember member, TimeIdentifier<Long> time) {
                this.member = member;
                this.time = time;
            }

            public PatternMember getMember() {
                return member;
            }

            public void setMember(PatternMember member) {
                this.member = member;
            }

            public TimeIdentifier<Long> getTime() {
                return time;
            }

            public void setTime(TimeIdentifier<Long> time) {
                this.time = time;
            }
        }

        public static class ThenDuring extends NonSpecialPatternMember {
            public PatternMember member;

            public TimeIdentifier<Long> time;

            public ThenDuring() {
            }

            public ThenDuring(PatternMember member, TimeIdentifier<Long> time) {
                this.member = member;
                this.time = time;
            }

            public PatternMember getMember() {
                return member;
            }

            public void setMember(PatternMember member) {
                this.member = member;
            }

            public TimeIdentifier<Long> getTime() {
                return time;
            }

            public void setTime(TimeIdentifier<Long> time) {
                this.time = time;
            }
        }

        public static class ThenAfter extends NonSpecialPatternMember {
            public TimeIdentifier<Long> time;

            public ThenAfter() {
            }

            public ThenAfter(TimeIdentifier<Long> time) {
                this.time = time;
            }

            public TimeIdentifier<Long> getTime() {
                return time;
            }

            public void setTime(TimeIdentifier<Long> time) {
                this.time = time;
            }
        }

        public static class StartsWith extends Then {
            public StartsWith() {
            }

            public StartsWith(PatternMember member) {
                super(member);
            }
        }

        public static class StartsWithAnyOne extends ThenAnyOne {
            public StartsWithAnyOne() {
            }

            public StartsWithAnyOne(PatternMembers members) {
                super(members);
            }
        }

        public static class StartsWithAll extends ThenAll {
            public StartsWithAll() {
            }

            public StartsWithAll(PatternMembers members) {
                super(members);
            }
        }

        public static class StartsWithRepeat extends ThenRepeat {
            public StartsWithRepeat() {
            }

            public StartsWithRepeat(PatternMember member, Identifier<Integer> min,
                                    Identifier<Integer> max) {
                super(member, min, max);
            }
        }
    }

    //-----------------

    public static class Util {
        public static interface Identifier<T> extends Serializable {
        }

        public static class Actual<T extends Comparable> implements Identifier<T> {
            public T value;

            public Actual() {
            }

            public Actual(T value) {
                this.value = value;
            }

            public T getValue() {
                return value;
            }

            public void setValue(T value) {
                this.value = value;
            }
        }

        public static class Parameter<T> implements Identifier<T> {
            public String bind;

            public Parameter() {
            }

            public Parameter(String bind) {
                this.bind = bind;
            }

            public String getBind() {
                return bind;
            }

            public void setBind(String bind) {
                this.bind = bind;
            }
        }

        public static interface TimeIdentifier<T> extends Identifier<T> {
            TimeUnit getTimeUnit();
        }

        public static class ActualTime<T extends Comparable> extends Actual<T>
                implements TimeIdentifier<T> {
            protected TimeUnit timeUnit;

            public ActualTime() {
            }

            public ActualTime(T value, TimeUnit timeUnit) {
                super(value);
                this.timeUnit = timeUnit;
            }

            public TimeUnit getTimeUnit() {
                return timeUnit;
            }

            public void setTimeUnit(TimeUnit timeUnit) {
                this.timeUnit = timeUnit;
            }
        }

        public static class ParameterTime<T> extends Parameter<T> implements TimeIdentifier<T> {
            protected TimeUnit timeUnit;

            public ParameterTime() {
            }

            public ParameterTime(String bind, TimeUnit timeUnit) {
                super(bind);
                this.timeUnit = timeUnit;
            }

            public TimeUnit getTimeUnit() {
                return timeUnit;
            }

            public void setTimeUnit(TimeUnit timeUnit) {
                this.timeUnit = timeUnit;
            }
        }

        public static interface Artifact extends Serializable {
        }

        public static class Event implements Artifact {
            public String uri;

            public Event() {
            }

            public Event(String uri) {
                this.uri = uri;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }
        }

        public static class Function implements Artifact {
            public String uri;

            public Function() {
            }

            public Function(String uri) {
                this.uri = uri;
            }

            public String getUri() {
                return uri;
            }

            public void setUri(String uri) {
                this.uri = uri;
            }
        }
    }
}
