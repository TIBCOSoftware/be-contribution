package com.tibco.cep.pattern.dsl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.tibco.cep.pattern.integ.impl.dsl2.Definition;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.PatternMember;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.PatternMembers;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.StartsWith;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.StartsWithAll;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.StartsWithAnyOne;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenDuring;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Pattern.ThenWithin;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.Subscription;
import com.tibco.cep.pattern.integ.impl.dsl2.Definition.Source.SubscriptionCorrelate;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:43:04 PM
*/

public class PatternValidator {

    Definition definition;

    public PatternValidator(Definition definition) {
        this.definition = definition;
    }

    public boolean validate() throws LanguageException {
        return correlatedEventExists()
                && mustUseCorrelation();
    }

    public boolean correlatedEventExists() throws LanguageException {
        boolean correlationEventExists = false;

        //Atleast one event in the correlation
        Pattern pattern = definition.getPattern();
        Source[] sources = definition.getSources();

        for (Source source : sources) {
            Subscription subscription = source.getSubscription();
            if (subscription instanceof SubscriptionCorrelate) {
                correlationEventExists = true;
                break;
            }
        }

        if (!correlationEventExists) {
            throw new LanguageException("No correlated events exist");
        }

        Set<String> sourceRefsAliasList = new HashSet<String>();
        for (Source source : sources) {
            sourceRefsAliasList.add(source.getAlias());
        }

        //collect all aliases from children in the Pattern and if
        //there is not match, throw exception
        List<PatternMember> startsWithList = new LinkedList<PatternMember>();
        getListOfAliasesUsedInPattern(pattern, startsWithList);

        List<String> startsWithAliasList = new LinkedList<String>();
        for (PatternMember patternMember : startsWithList) {
            if (patternMember instanceof StartsWith) {
                StartsWith startsWith = (StartsWith) patternMember;
                //TODO - Need to get source refs
                //startsWithAliasList.add(startsWith.getSourceRef());
            } else if (patternMember instanceof StartsWithAnyOne) {
                StartsWithAnyOne startsWithAnyOne = (StartsWithAnyOne) patternMember;
                //TODO - Need to get source refs
                //String[] sourceRefs = startsWithAnyOne.getSourceRefs();
                //startsWithAliasList.addAll(Arrays.asList(sourceRefs));
            }
        }

        for (String alias : startsWithAliasList) {
            if (!sourceRefsAliasList.contains(alias)) {
                correlationEventExists = false;
                throw new LanguageException("Alias is not defined in sources");
            }
        }

        return correlationEventExists;
    }

    public void getListOfAliasesUsedInPattern(Pattern pattern, List<PatternMember> startsWithList) {
        if (true) return;
        PatternMembers subpattern = pattern.getMembers();
        startsWithList = subpattern.getList();
        for (PatternMember eachSubPattern : startsWithList) {
            if (eachSubPattern instanceof StartsWith || eachSubPattern instanceof StartsWithAnyOne ||
                    eachSubPattern instanceof StartsWithAll) {
                startsWithList.add(eachSubPattern);
            }
            //TODO - Need to also do this for then, thenAnyOne, thenAll, thenRepeat and thenAfter
            if (eachSubPattern instanceof ThenWithin) {
                ThenWithin thenWithin = (ThenWithin) eachSubPattern;
                //TODO - Need to fix this
                //getListOfAliasesUsedInPattern(thenWithin.getPatternMembers(), startsWithList);
            } else if (eachSubPattern instanceof ThenDuring) {
                ThenDuring thenDuring = (ThenDuring) eachSubPattern;
                //TODO - Need to fix this
                //getListOfAliasesUsedInPattern(thenDuring.getPatternMembers(), startsWithList);
            }
        }
    }

    private boolean mustUseCorrelation() {
        //If first item in sequence is "then-any-one" or "then-all", all events in subsequence must use correlation
        return true;
    }
}
