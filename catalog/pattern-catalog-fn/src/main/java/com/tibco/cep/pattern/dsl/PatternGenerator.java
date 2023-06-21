package com.tibco.cep.pattern.dsl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.antlr.runtime.tree.Tree;

import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.PatternMember;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.PatternMembers;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.SourceRef;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.StartsWith;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.StartsWithAll;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.StartsWithAnyOne;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenRepeat;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.SubscriptionCorrelate;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.SubscriptionMatch;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Actual;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.ActualTime;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Event;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Identifier;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.Parameter;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.ParameterTime;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Util.TimeIdentifier;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:43:30 PM
*/

public class PatternGenerator {

    enum Token {
        TRUE,
        FALSE
    }

    private Tree tree;

    public PatternGenerator(Tree tree) {
        this.tree = tree;
    }

    public Definition execute() throws LanguageException {

        if (tree == null) return null;

        //Get the pattern name
        Tree child = tree.getChild(0);
        String patternName = getPatternName(child);

        //Get the event list child
        child = tree.getChild(1);
        EventHelper[] events = getEvents(child.getChild(0));

        //Get the subscription list
        child = child.getChild(1);
        Map<String, SubscriptionHelper> subscriptions = getSubscriptions(child.getChild(0));

        //Merge the alias and events to create sources
        Source[] sources = mergeSources(events, subscriptions);

        //Get the pattern sequence
        child = child.getChild(1);
        Pattern pattern = null;
        if (child.getType() == patternParser.STARTS_WITH_TOKEN) {
            pattern = new Pattern(getPatternMembers(child.getChild(0)));
        }

        //Generate pattern using the parsed data
        Definition definition = new Definition();

        definition.setUri(patternName);
        definition.setSources(sources);
        definition.setPattern(pattern);

        PatternValidator validator = new PatternValidator(definition);
        validator.validate();

        return definition;
    }

    private String getPatternName(Tree tree) {
        return tree.getText();
    }

    private EventHelper[] getEvents(Tree tree) throws LanguageException {
        int numEvents = tree.getChildCount();
        EventHelper event[] = new EventHelper[numEvents];
        HashSet<String> aliases = new HashSet<String>();
        for (int i = 0; i < numEvents; i++) {
            Tree eventNode = tree.getChild(i);
            String uri = eventNode.getChild(0).getText();
            String alias = eventNode.getChild(1).getText();
            if (!aliases.add(alias)) {
            	throw new LanguageException("Alias [" + alias + 
            		"] must not have multiple event URI");
            }
            event[i] = new EventHelper(uri, alias);
        }
        return event;
    }

    private Map<String, SubscriptionHelper> getSubscriptions(Tree tree) throws LanguageException {

        int numSubscriptions = tree.getChildCount();
        SubscriptionHelper subscription[] = new SubscriptionHelper[numSubscriptions];

        LinkedHashMap<String, SubscriptionHelper> map =
                new LinkedHashMap<String, SubscriptionHelper>(numSubscriptions, 0.70f);

        for (int i = 0; i < numSubscriptions; i++) {
            Tree subscriptionNode = tree.getChild(i);
            if (subscriptionNode.getChildCount() == 1) {
                SubscriptionHelper subscriptionHelper = new SubscriptionHelper(subscriptionNode.getChild(0).getText(),
                		subscriptionNode.getChild(0).getChild(0).getText());
                subscription[i] = subscriptionHelper;
            } else if (subscriptionNode.getChildCount() == 2) {
                SubscriptionHelper subscriptionHelper = new SubscriptionHelper(subscriptionNode.getChild(0).getText(),
                		subscriptionNode.getChild(0).getChild(0).getText(), subscriptionNode.getChild(1));
                subscription[i] = subscriptionHelper;
            } else if (subscriptionNode.getChildCount() == 3) {
                SubscriptionHelper subscriptionHelper = new SubscriptionHelper(subscriptionNode.getChild(0).getText(),
                		subscriptionNode.getChild(0).getChild(0).getText(), subscriptionNode.getChild(2));
                subscription[i] = subscriptionHelper;
            }
            subscription[i].validate();
            if (map.containsKey(subscription[i].getAlias())) {
            	throw new LanguageException("Alias [" + subscription[i].getAlias() + 
            			"] must not have multiple subscriptions");
            }
            map.put(subscription[i].getAlias(), subscription[i]);
        }
        return map;
    }

    private Source[] mergeSources(EventHelper[] events, Map<String, SubscriptionHelper> subscriptions) throws LanguageException {
        Source sources[] = new Source[events.length];
        for (int i = 0; i < events.length; i++) {
            Event event = new Event();
            event.setUri(events[i].getEventURI());
            sources[i] = new Source();
            sources[i].setEvent(event);
            sources[i].setAlias(events[i].getAlias());

            //Now mash with alias in the subscription helper
            SubscriptionHelper subscription = subscriptions.get(events[i].getAlias());
            if (subscription != null && sources[i].getAlias().equals(subscription.getAlias())) {
                if (subscription.correlation()) {
                    SubscriptionCorrelate correlate = new SubscriptionCorrelate();
                    correlate.setField(subscription.getField());
                    sources[i].setSubscription(correlate);
                } else {
                    SubscriptionMatch match = new SubscriptionMatch();
                    match.setField(subscription.getField());
                    Tree tree = subscription.getValue();
                    int type = tree.getType();
                    if (type == patternParser.FLOAT_TOKEN) {
                        String value = tree.getChild(0).getText();
                        //BE does not support floats, so we upcast to double
                        Actual<Double> actual = new Actual<Double>();
                        try {
                            actual.setValue(Double.valueOf(value));
                        } catch (NumberFormatException e) {
                        	throw new LanguageException("Double value must be between [" + Double.MIN_VALUE + 
                        			"] and [" + Double.MAX_VALUE + "]", e);
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.DOUBLE_TOKEN) {
                        String value = tree.getChild(0).getText();
                        Actual<Double> actual = new Actual<Double>();
                        try {
                            actual.setValue(Double.valueOf(value));
                        } catch (NumberFormatException e) {
                        	throw new LanguageException("Double value must be between [" + Double.MIN_VALUE + 
                        			"] and [" + Double.MAX_VALUE + "]", e);
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.INTEGER_TOKEN) {
                        String value = tree.getChild(0).getText();
                        Actual<Integer> actual = new Actual<Integer>();
                        try {
                            actual.setValue(Integer.valueOf(value));
                        } catch (NumberFormatException e) {
                        	throw new LanguageException("Integer value must be between [" + Integer.MIN_VALUE + 
                        			"] and [" + Integer.MAX_VALUE + "]", e);
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.LONG_TOKEN) {
                        String value = tree.getChild(0).getText();
                        Actual<Long> actual = new Actual<Long>();
                        try {
                            actual.setValue(Long.valueOf(value.substring(0,value.length()-1)));
                        } catch (NumberFormatException e) {
                        	throw new LanguageException("Long value must be between [" + Long.MIN_VALUE + 
                        			"] and [" + Long.MAX_VALUE + "]", e);
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.BOOLEAN_TOKEN) {
                        Actual<Boolean> actual = new Actual<Boolean>();
                        type = tree.getChild(0).getType();
                        if (type == patternParser.TRUE) {
                            actual.setValue(true);
                        } else if (type == patternParser.FALSE) {
                            actual.setValue(false);
                        } else {
                            throw new LanguageException("Boolean must only be either true or false");
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.DATE_TOKEN) {
                        Actual<Calendar> actual = new Actual<Calendar>();
                        if (tree.getChildCount() == 3) {
                            actual.setValue(getDateTime(tree.getChild(0).getText(),
                                    tree.getChild(1).getText(),
                                    tree.getChild(2).getText(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    TimeZone.getDefault()));
                        } else if (tree.getChildCount() == 4) {
                            actual.setValue(getDateTime(tree.getChild(0).getText(),
                                    tree.getChild(1).getText(),
                                    tree.getChild(2).getText(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    TimeZone.getTimeZone(tree.getChild(3).getText())));
                        } else {
                            throw new LanguageException("Incorrect number of date parameters");
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.DATETIME_TOKEN) {
                        Actual<Calendar> actual = new Actual<Calendar>();
                        if (tree.getChildCount() == 7) {
                            actual.setValue(getDateTime(tree.getChild(0).getText(),
                                    tree.getChild(1).getText(),
                                    tree.getChild(2).getText(),
                                    tree.getChild(3).getText(),
                                    tree.getChild(4).getText(),
                                    tree.getChild(5).getText(),
                                    tree.getChild(6).getText(),
                                    TimeZone.getDefault())
                            );
                        } else if (tree.getChildCount() == 8) {
                            actual.setValue(getDateTime(tree.getChild(0).getText(),
                                    tree.getChild(1).getText(),
                                    tree.getChild(2).getText(),
                                    tree.getChild(3).getText(),
                                    tree.getChild(4).getText(),
                                    tree.getChild(5).getText(),
                                    tree.getChild(6).getText(),
                                    TimeZone.getTimeZone(tree.getChild(7).getText()))
                            );
                        } else {
                            throw new LanguageException("Incorrect number of datetime parameters");
                        }
                        match.setWith(actual);
                    } else if (type == patternParser.STRING_TOKEN) {
                        String value = tree.getChild(0).getText();
                        Actual<String> actual = new Actual<String>();
                        actual.setValue(value);
                        match.setWith(actual);
                    } else if (type == patternParser.BIND_TOKEN) {
                        String value = tree.getChild(1).getText();
                        Parameter<String> param = new Parameter<String>();
                        param.setBind(value);
                        match.setWith(param);
                    } else {
                        throw new LanguageException("Incorrect date format");
                    }
                    sources[i].setSubscription(match);
                }
            }
        }
        return sources;
    }

    private PatternMembers<PatternMember> getPatternMembers(Tree tree) throws LanguageException {

        PatternMembers<PatternMember> patternMembers = new PatternMembers<PatternMember>();

        if (tree == null) {
            return null;
        } else {
            List<PatternMember> list = new ArrayList<PatternMember>();
            PatternMember startsWithPatternMember = getSubPattern(tree);
            list.add(startsWithPatternMember);
            for (int i = 0; i < tree.getChildCount(); i++) {
                PatternMember thenPatternMember = getThenPatternMember(tree.getChild(i));
                if (thenPatternMember != null) list.add(thenPatternMember);
            }
            patternMembers.setList(list);
        }

        return patternMembers;
    }

    private Calendar getDateTime(String year, String month, String day, String hour, String minute, String second,
                             String millisecond, TimeZone tz) throws LanguageException {

    	int years = 0, months = 1, days = 0, hours = 0, minutes = 0, seconds = 0, milliseconds = 0;
    	
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.clear();
        calendar.setLenient(false);

        try {
            if (year != null) years = Integer.valueOf(year);
            if (month != null) months = Integer.valueOf(month);
            if (day != null) days = Integer.valueOf(day);
            if (hour != null) hours = Integer.valueOf(hour);
            if (minute != null) minutes = Integer.valueOf(minute);
            if (second != null) seconds = Integer.valueOf(second);
            if (millisecond != null) milliseconds = Integer.valueOf(millisecond);
            
            calendar.set(years, months-1, days, hours, minutes, seconds);
            calendar.add(Calendar.MILLISECOND, milliseconds);
            calendar.setTimeZone(tz);
        	calendar.getTime(); //force date validation
        	
            calendar.setLenient(true); //needed for JDK
        	
        } catch (Exception e) {
        	throw new LanguageException("Invalid value provided for date parameter", e);
        }

        return calendar;
    }

    private PatternMember getSubPattern(Tree tree) throws LanguageException {
        if (tree == null) return null;
        if (tree.getType() == patternParser.ANY_ONE_TOKEN) {
            return new StartsWithAnyOne(getSourceRefs(tree));
        } else if (tree.getType() == patternParser.ALL_TOKEN) {
            return new StartsWithAll(getSourceRefs(tree));
        } else if (tree.getType() == patternParser.THEN_REPEAT_TOKEN) {
            return parseStartsWithRepeat(tree);
        } else if (tree.getType() == patternParser.THEN_TOKEN) {
            return getThenPatternMember(tree);
        }
        return new StartsWith(new SourceRef(tree.getText()));
    }

    private PatternMembers<PatternMember> getSourceRefs(Tree tree) throws LanguageException {
        List<PatternMember> sourceRefList = new ArrayList<PatternMember>();
        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree childTree =tree.getChild(i);
            String t = childTree.getText();

            if (!t.startsWith("THEN")) {
                if(childTree.getChildCount() > 0){
                    PatternMembers<PatternMember> pms = getPatternMembers(childTree);
                    sourceRefList.add(pms);
                }
                else{
                    sourceRefList.add(new SourceRef(t));
                }
            }
        }
        return new PatternMembers<PatternMember>(sourceRefList);
    }

    public PatternMember getThenPatternMember(Tree tree) throws LanguageException {

        if (tree == null || (!tree.getText().startsWith("THEN"))) return null;

        if (tree.getType() == patternParser.THEN_TOKEN) {
            PatternMember subPattern = subPattern(tree.getChild(0));
            return (subPattern == null)?parseThen(tree):subPattern;
        } else if (tree.getType() == patternParser.THEN_WITHIN_TOKEN) {
            return parseThenWithin(tree);
        } else if (tree.getType() == patternParser.THEN_DURING_TOKEN) {
            return parseThenDuring(tree);
        } else if (tree.getType() == patternParser.THEN_AFTER_TOKEN) {
            return parseThenAfter(tree);
        }
        
        return null;
    }

    private PatternMember subPattern(Tree tree) throws LanguageException {
        if (tree.getType() == patternParser.ANY_ONE_TOKEN) {
            return parseThenAnyOne(tree);
        } else if (tree.getType() == patternParser.ALL_TOKEN) {
            return parseThenAll(tree);
        } else if (tree.getType() == patternParser.THEN_REPEAT_TOKEN) {
            return parseThenRepeat(tree);
        } else if (hasSubPatterns(tree)) {
        	return getPatternMembers(tree);
        }
        return null;
    }

    private Definition.Pattern.Then parseThen(Tree tree) {
        return new Definition.Pattern.Then(new Definition.Pattern.SourceRef(tree.getChild(0).getText()));
    }

    private Definition.Pattern.PatternMember parseThenAll(Tree tree) throws LanguageException {
        Definition.Pattern.ThenAll thenAll = new Definition.Pattern.ThenAll();
        if (hasSubPatterns(tree)) {
            if (tree.getChildCount() > 1) {
                Definition.Pattern.PatternMembers<Definition.Pattern.PatternMember> patternMembers = new Definition.Pattern.PatternMembers<Definition.Pattern.PatternMember>();
                for (int i = 0; i < tree.getChildCount(); i++) {
                    patternMembers.addMember(getPatternMembers(tree.getChild(i)));
                }
                thenAll.setMembers(patternMembers);
            }
        } else {
            thenAll.setMembers(getSourceRefs(tree));
        }
        return thenAll;
    }

    private Definition.Pattern.PatternMember parseThenAnyOne(Tree tree) throws LanguageException {
        Definition.Pattern.ThenAnyOne thenAnyOne = new Definition.Pattern.ThenAnyOne();
        if (hasSubPatterns(tree)) {
            Definition.Pattern.PatternMembers<Definition.Pattern.PatternMember> patternMembers = new Definition.Pattern.PatternMembers<Definition.Pattern.PatternMember>();
            for (int i = 0; i < tree.getChildCount(); i++) {
                if (tree.getChild(i).getChildCount() > 0) {
                    patternMembers.addMember(getPatternMembers(tree.getChild(i)));
                } else {
                    List<PatternMember> sourceRefList = new ArrayList<PatternMember>();
                    sourceRefList.add(new SourceRef(tree.getChild(i).getText()));
                    patternMembers.addMember(new PatternMembers<PatternMember>(sourceRefList));
                }
            }
            thenAnyOne.setMembers(patternMembers);
        } else {
            thenAnyOne.setMembers(getSourceRefs(tree));
        }
        return thenAnyOne;
    }

    private Definition.Pattern.PatternMember parseThenAfter(Tree tree) throws LanguageException {
        Definition.Pattern.ThenAfter thenAfter = new Definition.Pattern.ThenAfter();
        thenAfter.setTime(getTimeIdentifier(tree));
        return thenAfter;
    }

    private Definition.Pattern.PatternMember parseThenDuring(Tree tree) throws LanguageException {
        Definition.Pattern.ThenDuring thenDuring = new Definition.Pattern.ThenDuring();
        thenDuring.setTime(getTimeIdentifier(tree));
        if (tree.getChildCount() == 3) {
            thenDuring.setMember(getPatternMembers(tree.getChild(2)));
        } else if (tree.getChildCount() == 4) {
            thenDuring.setMember(getPatternMembers(tree.getChild(3)));
        }
        return thenDuring;
    }

    private Definition.Pattern.PatternMember parseThenWithin(Tree tree) throws LanguageException {
        Definition.Pattern.ThenWithin thenWithin = new Definition.Pattern.ThenWithin();
        thenWithin.setTime(getTimeIdentifier(tree));
        if (tree.getChildCount() == 3) {
            thenWithin.setMember(getPatternMembers(tree.getChild(2)));
        } else if (tree.getChildCount() == 4) {
            thenWithin.setMember(getPatternMembers(tree.getChild(3)));
        }
        return thenWithin;
    }

    private Definition.Pattern.PatternMember parseThenRepeat(Tree tree) throws LanguageException {
        Definition.Pattern.ThenRepeat thenRepeat = new Definition.Pattern.ThenRepeat();
        Tree child1 = tree.getChild(0).getChild(0);
        Tree child2 = tree.getChild(0).getChild(1);
        Tree child3 = tree.getChild(0).getChild(2);
        
        if (child3 == null) {
        	//min and max are the same value
            if (child1.getType() == patternParser.BIND_TOKEN) {
                thenRepeat.setMin(getRepeatParameter(child1.getChild(1).getText()));
                thenRepeat.setMax(getRepeatParameter(child1.getChild(1).getText()));
            } else if (child1.getType() == patternParser.INTEGER_TOKEN) {
                thenRepeat.setMin(getRepeatActual(child1.getChild(0).getText()));
                thenRepeat.setMax(getRepeatActual(child1.getChild(0).getText()));
            }
            
            PatternMember subPattern = subPattern(child2);
            if (subPattern == null) {
                thenRepeat.setMember(new Definition.Pattern.SourceRef(child2.getText()));
            } else {
                thenRepeat.setMember(subPattern);
            }
        } else {
            if (child1.getType() == patternParser.BIND_TOKEN) {
                thenRepeat.setMin(getRepeatParameter(child1.getChild(1).getText()));
            } else if (child1.getType() == patternParser.INTEGER_TOKEN) {
                thenRepeat.setMin(getRepeatActual(child1.getChild(0).getText()));
            }
            if (child2.getType() == patternParser.BIND_TOKEN) {
                thenRepeat.setMax(getRepeatParameter(child2.getChild(1).getText()));
            } else if (child2.getType() == patternParser.INTEGER_TOKEN) {
                thenRepeat.setMax(getRepeatActual(child2.getChild(0).getText()));
            }
            
            PatternMember subPattern = subPattern(child3);
            if (subPattern == null) {
                thenRepeat.setMember(new Definition.Pattern.SourceRef(child3.getText()));
            } else {
                thenRepeat.setMember(subPattern);
            }
        }

        validateRepeatValues(thenRepeat);
        
        return thenRepeat;
    }

    private PatternMember parseStartsWithRepeat(Tree tree) throws LanguageException {
        Definition.Pattern.StartsWithRepeat startsWithRepeat = new Definition.Pattern.StartsWithRepeat();
        Tree child1 = tree.getChild(0).getChild(0);
        Tree child2 = tree.getChild(0).getChild(1);
        Tree child3 = tree.getChild(0).getChild(2);
        
        if (child3 == null) {
            if (child1.getType() == patternParser.BIND_TOKEN) {
                startsWithRepeat.setMin(getRepeatParameter(child1.getChild(1).getText()));
                startsWithRepeat.setMax(getRepeatParameter(child1.getChild(1).getText()));
            } else if (child1.getType() == patternParser.INTEGER_TOKEN) {
                startsWithRepeat.setMin(getRepeatActual(child1.getChild(0).getText()));
                startsWithRepeat.setMax(getRepeatActual(child1.getChild(0).getText()));
            }
            
            PatternMember subPattern = subPattern(child2);
            if (subPattern == null) {
                startsWithRepeat.setMember(new Definition.Pattern.SourceRef(child2.getText()));
            } else {
                startsWithRepeat.setMember(subPattern);
            }
        } else {
            if (child1.getType() == patternParser.BIND_TOKEN) {
                startsWithRepeat.setMin(getRepeatParameter(child1.getChild(1).getText()));
            } else if (child1.getType() == patternParser.INTEGER_TOKEN) {
                startsWithRepeat.setMin(getRepeatActual(child1.getChild(0).getText()));
            }
            if (child2.getType() == patternParser.BIND_TOKEN) {
                startsWithRepeat.setMax(getRepeatParameter(child2.getChild(1).getText()));
            } else if (child2.getType() == patternParser.INTEGER_TOKEN) {
                startsWithRepeat.setMax(getRepeatActual(child2.getChild(0).getText()));
            }
            
            PatternMember subPattern = subPattern(child3);
            if (subPattern == null) {
                startsWithRepeat.setMember(new Definition.Pattern.SourceRef(child3.getText()));
            } else {
                startsWithRepeat.setMember(subPattern);
            }
        }
        validateRepeatValues(startsWithRepeat);
        
        return startsWithRepeat;
    }

    private boolean hasSubPatterns(Tree tree) {
        for (int i = 0; i < tree.getChildCount(); i++) {
            Tree child = tree.getChild(i);
            if (child.getChildCount() > 0) {
                //for (int j = 0; j < child.getChildCount(); j++) {
                //  if (child.getChild(j).getChildCount() > 0) {
                //this means that children contain subpatterns
                return true;
                //}
                //}
            }
        }
        return false;
    }

	private void validateRepeatValues(ThenRepeat thenRepeat)
			throws LanguageException {
		if (thenRepeat.getMin() instanceof Actual && thenRepeat.getMax() instanceof Actual) {
            int min = ((Actual<Integer>)thenRepeat.getMin()).getValue();
            int max = ((Actual<Integer>)thenRepeat.getMax()).getValue();
            if (max < min) throw new LanguageException("Max value [" + max + "] must be more than min value [" + min + "]");
        }
	}

    private TimeIdentifier<Long> getTimeIdentifier(Tree tree) throws LanguageException {
        if (tree.getChild(0).getType() == patternParser.BIND_SYMBOL) {
            //bind parameter
            String val = tree.getChild(1).getText();
            int valUnit = tree.getChild(2).getType();
            return getTimeParameter(val, valUnit);
        } else {
            //actual
            String val = tree.getChild(0).getText();
            int valUnit = tree.getChild(1).getType();
            return getTimeActual(val, valUnit);
        }
    }

    private TimeIdentifier<Long> getTimeActual(String val, int valUnit) throws LanguageException {
        ActualTime<Long> actual = new ActualTime<Long>();
        long value = 0;
        try {
            value = Long.parseLong(val);
        } catch (NumberFormatException e) {
        	throw new LanguageException("Long value must be between [" + Long.MIN_VALUE + 
        			"] and [" + Long.MAX_VALUE + "]", e);
        }
        actual.setValue(value * convertToMilliseconds(valUnit));
        actual.setTimeUnit(TimeUnit.MILLISECONDS);
        return actual;
    }

    private ParameterTime<Long> getTimeParameter(String val, int valUnit) {
        ParameterTime<Long> param = new ParameterTime<Long>();
        param.setBind(val);
        param.setTimeUnit(getTimeUnit(valUnit));
        return param;
    }

    private Identifier<Integer> getRepeatParameter(String val) {
        Parameter<Integer> param = new Parameter<Integer>();
        param.setBind(val);
        return param;
    }

    private Identifier<Integer> getRepeatActual(String val) throws LanguageException {
        Actual<Integer> actual = new Actual<Integer>();
        try {
            actual.setValue(Integer.valueOf(val));
        } catch (NumberFormatException e) {
        	throw new LanguageException("Integer value must be between [" + Integer.MIN_VALUE + 
        			"] and [" + Integer.MAX_VALUE + "]", e);
        }
        return actual;
    }

    static Long convertToMilliseconds(int valUnit) {
        if (valUnit == patternParser.MILLISECONDS) {
            return 1L;
        } else if (valUnit == patternParser.SECONDS) {
            return 1000L;
        } else if (valUnit == patternParser.MINUTES) {
            return 60000L;
        } else if (valUnit == patternParser.HOURS) {
            return 3600000L;
        } else if (valUnit == patternParser.DAYS) {
            return 86400000L;
        }
        return 0L;
    }

    static TimeUnit getTimeUnit(int valUnit) {
        if (valUnit == patternParser.MILLISECONDS) {
            return TimeUnit.MILLISECONDS;
        } else if (valUnit == patternParser.SECONDS) {
            return TimeUnit.SECONDS;
        } else if (valUnit == patternParser.MINUTES) {
            return TimeUnit.MINUTES;
        } else if (valUnit == patternParser.HOURS) {
            return TimeUnit.HOURS;
        } else if (valUnit == patternParser.DAYS) {
            return TimeUnit.DAYS;
        }
        return null;
    }

    static class EventHelper {

        private String eventURI;
        private String alias;

        public EventHelper(String name, String alias) {
            this.eventURI = name;
            this.alias = alias;
        }

        public String getEventURI() {
            return eventURI;
        }

        public void setEventURI(String eventURI) {
            this.eventURI = eventURI;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public void validate() throws LanguageException {
        	if (!Util.isValidIdentifier(alias)) throw new LanguageException("Concept name \"" + alias + 
			"\" is not a valid Java identifier");
        }
    }

    static class SubscriptionHelper {

        private String alias;
        private String field;
        private Tree value;

        SubscriptionHelper(String alias, String field) {
            this.alias = alias;
            this.field = field;
        }

        SubscriptionHelper(String alias, String field, Tree value) {
            this.alias = alias;
            this.field = field;
            this.value = value;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public Tree getValue() {
            return value;
        }

        public void setValue(Tree value) {
            this.value = value;
        }

        public boolean correlation() {
            return value == null;
        }

        public void validate() throws LanguageException {
        	if (!Util.isValidIdentifier(field)) throw new LanguageException("Concept field name \"" + field + 
			"\" is not a valid Java identifier");
        }
    }
    
    static class Util {
        public static boolean isIdentifierStart(char ch) {
            return ch != '$' && Character.isJavaIdentifierStart(ch);
        }
    	public static boolean isIdentifierPart(char ch) {
    	    return ch != '$' && Character.isJavaIdentifierPart(ch);
    	}
    	public static boolean isProjectIdentifierPart(char ch) {
    	    return (ch == '-') || (ch != '$' && Character.isJavaIdentifierPart(ch));
    	}
    	public static boolean isValidIdentifier(String identifier) {
    	    if(identifier == null || identifier.length() <= 0) return false;
    	    boolean start = true;
    	    for(int ii = 0; ii < identifier.length(); ii++) {
    	        char ch = identifier.charAt(ii);
    	        if(!start || (start = false)) {
    	            if(!isIdentifierPart(ch)) return false;
    	        } else {
    	            if(!isIdentifierStart(ch)) return false;
    	        }
    	    }
    	    return true;
    	}
    }
}
