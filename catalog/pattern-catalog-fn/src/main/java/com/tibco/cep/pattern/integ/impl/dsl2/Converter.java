package com.tibco.cep.pattern.integ.impl.dsl2;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.tibco.cep.common.resource.Id;
import com.tibco.cep.pattern.integ.admin.Session;
import com.tibco.cep.pattern.integ.dsl.PatternSubscriptionDef;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.PatternMember;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.PatternMembers;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.SourceRef;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.Then;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenAfter;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenAll;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenAnyOne;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenDuring;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenRepeat;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenWithin;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.Subscription;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.SubscriptionCorrelate;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.SubscriptionMatch;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Actual;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Event;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Identifier;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Parameter;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.TimeIdentifier;
import com.tibco.cep.pattern.matcher.dsl.InputDefLB;
import com.tibco.cep.pattern.matcher.dsl.PatternDef;
import com.tibco.cep.pattern.matcher.dsl.PatternDefLB;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultInputDefLB;
import com.tibco.cep.pattern.matcher.impl.dsl.DefaultPatternDefLB;
import com.tibco.cep.pattern.matcher.model.InputDef;
import com.tibco.cep.pattern.subscriber.master.EventDescriptor;

/*
* Author: Ashwin Jayaprakash / Date: Nov 17, 2009 / Time: 7:06:01 PM
*/

public class Converter {
    protected Definition definition;

    protected Map<String, Comparable> parameters;

    protected Session session;

    protected OntologyProvider ontologyProvider;

    protected HashMap<String, InputDef> aliasAndInputDefs;

    public Converter(Definition definition, Map<String, Comparable> parameters, Session session,
                     OntologyProvider ontologyProvider) {
        this.definition = definition;
        this.parameters = parameters;
        this.session = session;
        this.ontologyProvider = ontologyProvider;

        this.aliasAndInputDefs = new HashMap<String, InputDef>(4);
    }

    /**
     * @return The same session that was provided in the {@link #Converter(Definition, Map, Session,
     *         OntologyProvider) constructor} (for convenience).
     * @throws Exception
     */
    public Session convert() throws Exception {
        PatternSubscriptionDef subscriptionDef = session.definePatternSubscription();

        buildSubscription(subscriptionDef);

        //--------------

        PatternDef patternDef = session.createPattern();
        Pattern pattern = definition.getPattern();

        buildPattern(patternDef, pattern);

        session.setPattern(patternDef);

        return session;
    }

    public void discard() {
        definition = null;

        parameters = null;

        session = null;

        ontologyProvider = null;

        aliasAndInputDefs.clear();
        aliasAndInputDefs = null;
    }

    private Comparable getBindParameter(String name) {
        Comparable comparable = parameters.get(name);

        if (comparable == null) {
            throw new IllegalArgumentException(
                    "The bind variable [" + name + "] has not been set.");
        }

        return comparable;
    }

    private <T extends Comparable> T testAndExtractValue(Identifier<T> identifier,
                                                         Class<T> targetClass) {
        T result = null;

        if (identifier instanceof Parameter) {
            String paramName = ((Parameter) identifier).getBind();

            Object obj = getBindParameter(paramName);

            Class actualClass = obj.getClass();
            if (targetClass.isAssignableFrom(actualClass) == false) {
                throw new IllegalArgumentException(
                        "The bind variable's [" + paramName + "] value [" + obj +
                                "] is of the wrong type [" + actualClass.getName() +
                                "]. Expected type is [" + targetClass.getName() + "]");
            }

            result = targetClass.cast(obj);
        }
        else if (identifier instanceof Actual) {
            Object obj = ((Actual) identifier).getValue();

            Class actualClass = obj.getClass();
            if (targetClass.isAssignableFrom(actualClass) == false) {
                throw new IllegalArgumentException(
                        "The value [" + obj + "] is of the wrong type [" + actualClass.getName() +
                                "]. Expected type is [" + targetClass.getName() + "]");
            }

            result = targetClass.cast(obj);
        }
        else {
            throw new IllegalArgumentException("The Identifier type [" +
                    identifier.getClass().getName() + "] is not supported.");
        }

        return result;
    }

    private void buildSubscription(PatternSubscriptionDef subscriptionDef) {
        Object[] firstSeenCorrelationDetails = null;

        Source[] sources = definition.getSources();

        for (Source source : sources) {
            String alias = source.getAlias();
            Event event = source.getEvent();

            //--------------

            EventDescriptor eventDescriptor = ontologyProvider.getEventDescriptor(event);
            if (eventDescriptor == null) {
                String validEvents = ontologyProvider.getEventDescriptors().toString();

                throw new IllegalArgumentException(
                        "The event URI [" + event.getUri() + "] with alias [" + alias +
                                "] is invalid. Valid events are " + validEvents);
            }

            Id eventDescriptorId = eventDescriptor.getResourceId();

            subscriptionDef
                    .listenTo(eventDescriptorId)
                    .as(alias);

            //--------------

            Subscription subscription = source.getSubscription();

            if (subscription == null) {
                throw new IllegalArgumentException(
                        "The event URI [" + event.getUri() + "] with alias [" + alias +
                                "] is invalid because it has no subscription defined.");
            }
            else if (subscription instanceof SubscriptionCorrelate) {
                SubscriptionCorrelate sc = (SubscriptionCorrelate) subscription;

                String field = sc.getField();
                Class fieldType =
                        verifyAndExtractCorrelationFieldType(event, alias, eventDescriptor, field);

                firstSeenCorrelationDetails =
                        checkCorrelationField(firstSeenCorrelationDetails, event, alias, field,
                                fieldType);

                subscriptionDef.use(field);
            }
            else if (subscription instanceof SubscriptionMatch) {
                SubscriptionMatch sm = (SubscriptionMatch) subscription;

                String field = sm.getField();
                Class fieldType =
                        verifyAndExtractCorrelationFieldType(event, alias, eventDescriptor, field);

                Identifier identifier = sm.getWith();

                if (identifier instanceof Actual) {
                    Actual actual = (Actual) identifier;

                    Comparable value = actual.getValue();

                    verifyLeftAndRightMatchFieldType(event, alias, eventDescriptor, field,
                            fieldType, value);

                    subscriptionDef.whereMatches(field, value);
                }
                else if (identifier instanceof Parameter) {
                    Parameter parameter = (Parameter) identifier;

                    String bind = parameter.getBind();
                    Comparable value = getBindParameter(bind);

                    verifyLeftAndRightMatchFieldType(event, alias, eventDescriptor, field,
                            fieldType, value);

                    subscriptionDef.whereMatches(field, value);
                }
                else {
                    throw new IllegalArgumentException(
                            "The event URI [" + event.getUri() + "] with alias [" + alias +
                                    "] is invalid because its subscription identifier type [" +
                                    identifier.getClass().getName() + "] is not supported.");
                }
            }
            else {
                throw new IllegalArgumentException(
                        "The event URI [" + event.getUri() + "] with alias [" + alias +
                                "] is invalid because its subscription type [" +
                                subscription.getClass().getName() + "] is not supported.");
            }

            //--------------

            if (aliasAndInputDefs.containsKey(alias)) {
                throw new IllegalArgumentException(
                        "The event alias [" + alias + "] defined for URI [" + event.getUri() +
                                "] is invalid because aliases have to be unique.");
            }

            InputDef inputDef = session.defineInput(alias);
            aliasAndInputDefs.put(alias, inputDef);
        }

        if (firstSeenCorrelationDetails == null) {
            throw new IllegalArgumentException(
                    "The subscription definition should have at least one correlation field specified.");
        }
    }

    private static Object[] checkCorrelationField(Object[] firstSeenCorrelationDetails,
                                                  Event event, String alias,
                                                  String field, Class fieldType) {
        if (firstSeenCorrelationDetails == null) {
            return new Object[]{event, alias, field, fieldType};
        }

        //---------------

        Class firstSeenFieldType = (Class) firstSeenCorrelationDetails[3];

        if (fieldType.equals(firstSeenFieldType) == false) {
            String s = "The event URI [" + event.getUri() + "] with alias [" + alias + "]" +
                    " is invalid because its correlation field [" + field + "]" +
                    " type [" + fieldType.getSimpleName() + "]" +
                    " is not the same as the first [" + firstSeenCorrelationDetails[1] + "]" +
                    " field's [" + firstSeenCorrelationDetails[2] + "]" +
                    " type [" + firstSeenFieldType.getSimpleName() + "].";

            throw new IllegalArgumentException(s);
        }

        return firstSeenCorrelationDetails;
    }

    private Class verifyAndExtractCorrelationFieldType(Event event, String alias,
                                                       EventDescriptor eventDescriptor,
                                                       String field) {
        String[] propertyNames = eventDescriptor.getSortedPropertyNames();

        int location = Arrays.binarySearch(propertyNames, field);

        if (location < 0 || location >= propertyNames.length) {
            throw new IllegalArgumentException(
                    "The correlation field [" + field + "] defined for event URI [" +
                            event.getUri() + "] with alias [" + alias +
                            "] is invalid because the field does not exist. Valid fields are " +
                            Arrays.<String>asList(propertyNames));
        }

        return eventDescriptor.getPropertyType(field);
    }

    private void verifyLeftAndRightMatchFieldType(Event event, String alias,
                                                  EventDescriptor eventDescriptor,
                                                  String field, Class lhsFieldType,
                                                  Comparable rhsFieldValue) {
        if (rhsFieldValue == null) {
            throw new IllegalArgumentException(
                    "The match field [" + field + "] defined for event URI [" +
                            event.getUri() + "] with alias [" + alias +
                            "] is of type [" + lhsFieldType.getName() +
                            "] but the value provided is null, which is not allowed.");
        }

        lhsFieldType = unboxToJavaTypeIfApplicable(lhsFieldType);
        Class rhsFieldType = unboxToJavaTypeIfApplicable(rhsFieldValue.getClass());

        if (lhsFieldType.isAssignableFrom(rhsFieldType) == false) {
            throw new IllegalArgumentException(
                    "The match field [" + field + "] defined for event URI [" +
                            event.getUri() + "] with alias [" + alias +
                            "] is of type [" + lhsFieldType.getName() +
                            "] but the value provided is of an incompatible type [" +
                            rhsFieldType.getName() + "].");
        }
    }

    private void buildPattern(PatternDef patternDef, Pattern pattern) {
        PatternMembers<? extends PatternMember> patternMembers = pattern.getMembers();

        buildPatternMembers(patternDef, patternMembers);
    }

    private void buildPatternMembers(PatternDef patternDef,
                                     Iterable<? extends PatternMember> allPMs) {
        for (PatternMember pm : allPMs) {
            //Direct SourceRefs will be treated as "then".
            if (pm instanceof SourceRef) {
                sourceRefToThen(patternDef, aliasAndInputDefs, (SourceRef) pm);
            }
            else if (pm instanceof PatternMembers) {
                //Process this recursively.
                buildPatternMembers(patternDef, ((PatternMembers) pm));
            }
            else if (pm instanceof Then) {
                PatternMember member = ((Then) pm).getMember();

                if (member instanceof SourceRef) {
                    sourceRefToThen(patternDef, aliasAndInputDefs, (SourceRef) member);
                }
                else {
                    //Just rolls up to current level.
                    buildPatternMembers(patternDef, asList(member));
                }
            }
            else if (pm instanceof ThenAnyOne) {
                PatternMembers<? extends PatternMember> members = ((ThenAnyOne) pm).getMembers();

                int sourceRefCount = countSourceRefs(members);

                //All SourceRefs.
                if (sourceRefCount == members.size()) {
                    InputDefLB inputDefLB = extractInputDefs(aliasAndInputDefs,
                            (PatternMembers<SourceRef>) members);

                    patternDef.thenAnyOne(inputDefLB);
                }
                //None or only some are SourceRefs.
                else {
                    PatternDefLB patternDefLB = new DefaultPatternDefLB();

                    //If some are directly SourceRefs here, we will treat them as "then(sourceRef)".
                    for (PatternMember member : members) {
                        PatternDef subPatternDef = session.createPattern();

                        buildPatternMembers(subPatternDef, asList(member));

                        patternDefLB.add(subPatternDef);
                    }

                    patternDef.thenAnyOne(patternDefLB);
                }
            }
            else if (pm instanceof ThenAll) {
                PatternMembers<? extends PatternMember> members = ((ThenAll) pm).getMembers();

                int sourceRefCount = countSourceRefs(members);

                //All SourceRefs.
                if (sourceRefCount == members.size()) {
                    InputDefLB inputDefLB = extractInputDefs(aliasAndInputDefs,
                            (PatternMembers<SourceRef>) members);

                    patternDef.thenAll(inputDefLB);
                }
                //None or only some are SourceRefs.
                else {
                    PatternDefLB patternDefLB = new DefaultPatternDefLB();

                    //If some are directly SourceRefs here, we will treat them as "then(sourceRef)".
                    for (PatternMember member : members) {
                        PatternDef subPatternDef = session.createPattern();

                        buildPatternMembers(subPatternDef, asList(member));

                        patternDefLB.add(subPatternDef);
                    }

                    patternDef.thenAll(patternDefLB);
                }
            }
            else if (pm instanceof ThenRepeat) {
                PatternMember member = ((ThenRepeat) pm).getMember();

                Identifier<Integer> $min = ((ThenRepeat) pm).getMin();
                Integer min = testAndExtractValue($min, Integer.class);

                Identifier<Integer> $max = ((ThenRepeat) pm).getMax();
                Integer max = testAndExtractValue($max, Integer.class);

                if (member instanceof SourceRef) {
                    String alias = ((SourceRef) member).getSourceAlias();
                    InputDef inputDef = getInputDef(alias);

                    patternDef.then(inputDef, min, max);
                }
                else {
                    PatternDef subPatternDef = session.createPattern();
                    buildPatternMembers(subPatternDef, asList(member));

                    patternDef.then(subPatternDef, min, max);
                }
            }
            else if (pm instanceof ThenWithin) {
                PatternMember member = ((ThenWithin) pm).getMember();

                TimeIdentifier<Long> $time = ((ThenWithin) pm).getTime();

                Long timeValue = testAndExtractValue($time, Long.class);
                TimeUnit timeUnit = $time.getTimeUnit();

                if (member instanceof SourceRef) {
                    String alias = ((SourceRef) member).getSourceAlias();
                    InputDef inputDef = getInputDef(alias);

                    patternDef.thenWithin(timeValue, timeUnit, inputDef);
                }
                else {
                    PatternDef subPatternDef = session.createPattern();
                    buildPatternMembers(subPatternDef, asList(member));

                    patternDef.thenWithin(timeValue, timeUnit, subPatternDef);
                }
            }
            else if (pm instanceof ThenDuring) {
                PatternMember member = ((ThenDuring) pm).getMember();

                TimeIdentifier<Long> $time = ((ThenDuring) pm).getTime();
                Long timeValue = testAndExtractValue($time, Long.class);
                TimeUnit timeUnit = $time.getTimeUnit();

                if (member instanceof SourceRef) {
                    String alias = ((SourceRef) member).getSourceAlias();
                    InputDef inputDef = getInputDef(alias);

                    patternDef.thenDuring(timeValue, timeUnit, inputDef);
                }
                else {
                    PatternDef subPatternDef = session.createPattern();
                    buildPatternMembers(subPatternDef, asList(member));

                    patternDef.thenDuring(timeValue, timeUnit, subPatternDef);
                }
            }
            else if (pm instanceof ThenAfter) {
                TimeIdentifier<Long> $time = ((ThenAfter) pm).getTime();
                Long timeValue = testAndExtractValue($time, Long.class);
                TimeUnit timeUnit = $time.getTimeUnit();

                patternDef.thenAfter(timeValue, timeUnit);
            }
            else {
                throw new IllegalArgumentException(
                        "The Pattern type [" + pm.getClass().getName() + "] is not supported.");
            }
        }
    }

    private InputDef getInputDef(String alias) {
        return getInputDef(aliasAndInputDefs, alias);
    }

    private static InputDef getInputDef(HashMap<String, InputDef> aliasAndInputDefs, String alias) {
        InputDef inputDef = aliasAndInputDefs.get(alias);

        if (inputDef == null) {
            throw new IllegalArgumentException(
                    "The alias [" + alias +
                            "] is invalid because there is no such source defined." +
                            " Valid source aliases are " + aliasAndInputDefs.keySet());
        }

        return inputDef;
    }

    private static InputDefLB extractInputDefs(HashMap<String, InputDef> aliasAndInputDefs,
                                               PatternMembers<SourceRef> members) {
        InputDefLB inputDefLB = new DefaultInputDefLB();

        for (SourceRef sourceRef : members) {
            String alias = sourceRef.getSourceAlias();

            InputDef inputDef = getInputDef(aliasAndInputDefs, alias);

            inputDefLB.add(inputDef);
        }
        return inputDefLB;
    }

    private static int countSourceRefs(PatternMembers<?> members) {
        int sourceRefCount = 0;

        for (PatternMember member : members) {
            if (member instanceof SourceRef) {
                sourceRefCount++;
            }
        }

        return sourceRefCount;
    }

    private static List<PatternMember> asList(PatternMember member) {
        LinkedList<PatternMember> list = new LinkedList<PatternMember>();
        list.add(member);

        return list;
    }

    private static void sourceRefToThen(PatternDef patternDef,
                                        HashMap<String, InputDef> aliasAndInputDefs,
                                        SourceRef sourceRef) {
        String alias = sourceRef.getSourceAlias();
        InputDef inputDef = getInputDef(aliasAndInputDefs, alias);

        patternDef.then(inputDef);
    }

    /**
     * @param boxedType
     * @return The same if not possible to unbox.
     */
    public static Class<? extends Comparable> unboxToJavaTypeIfApplicable(
            Class<? extends Comparable> boxedType) {

        if (boxedType.equals(Byte.class)) {
            return Byte.TYPE;
        }
        else if (boxedType.equals(Short.class)) {
            return Short.TYPE;
        }
        else if (boxedType.equals(Integer.class)) {
            return Integer.TYPE;
        }
        else if (boxedType.equals(Long.class)) {
            return Long.TYPE;
        }
        else if (boxedType.equals(Float.class)) {
            return Float.TYPE;
        }
        else if (boxedType.equals(Double.class)) {
            return Double.TYPE;
        }
        else if (boxedType.equals(Boolean.class)) {
            return Boolean.TYPE;
        }

        return boxedType;
    }
}
