// $ANTLR 3.2 Sep 23, 2009 12:02:23 Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g 2014-04-11 11:18:06

	package com.tibco.cep.pattern.dsl; 

	import com.tibco.cep.pattern.dsl.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;


import org.antlr.runtime.tree.*;

public class patternParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "DEFINE_PATTERN_TOKEN", "USING_TOKEN", "EVENT_LIST_TOKEN", "EVENT_TOKEN", "SUBSCRIPTION_LIST_TOKEN", "SUBSCRIPTION_TOKEN", "MATCH_TOKEN", "WHERE_TOKEN", "STARTS_WITH_TOKEN", "ITEM_TOKEN", "ANY_ONE_TOKEN", "ALL_TOKEN", "SUBPATTERN_ITEM_TOKEN", "THEN_TOKEN", "THEN_ANY_ONE_TOKEN", "THEN_ALL_TOKEN", "THEN_REPEAT_TOKEN", "THEN_WITHIN_TOKEN", "THEN_DURING_TOKEN", "THEN_AFTER_TOKEN", "REPEAT_TOKEN", "DATE_TOKEN", "DATETIME_TOKEN", "BIND_TOKEN", "STRING_TOKEN", "BOOLEAN_TOKEN", "INTEGER_TOKEN", "LONG_TOKEN", "FLOAT_TOKEN", "DOUBLE_TOKEN", "LITERAL_TOKEN", "NEWLINE", "DEFINE", "PATTERN", "USING", "EVENT", "AS", "WHERE", "AND", "SUCH", "THAT", "STARTS", "WITH", "ANY", "ONE", "ALL", "THEN", "REPEAT", "WITHIN", "DURING", "TO", "TIMES", "AFTER", "DAYS", "HOURS", "MINUTES", "SECONDS", "MILLISECONDS", "TRUE", "FALSE", "NULL", "ESCAPE_SPACE", "ESCAPE_KEYWORD", "COMMA", "SEMI", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "BIND_SYMBOL", "QUOTES", "CHAR", "DIGIT", "SIGN", "LONG_SUFFIX", "DOUBLE_SUFFIX", "SIGNED", "UNSIGNED", "LONG", "FLOAT", "DOUBLE", "NameFirstCharacter", "NameCharacter", "IDENTIFIER", "NAME", "WHITESPACE", "ALLCHARS", "'$date('", "'$datetime('", "'='", "'.'"
    };
    public static final int SIGN=77;
    public static final int SIGNED=80;
    public static final int CHAR=75;
    public static final int THEN_REPEAT_TOKEN=20;
    public static final int ONE=48;
    public static final int QUOTES=74;
    public static final int EOF=-1;
    public static final int STRING_TOKEN=28;
    public static final int PATTERN=37;
    public static final int T__93=93;
    public static final int T__94=94;
    public static final int T__91=91;
    public static final int RPAREN=70;
    public static final int NAME=88;
    public static final int T__92=92;
    public static final int THAT=44;
    public static final int USING_TOKEN=5;
    public static final int USING=38;
    public static final int SUBSCRIPTION_LIST_TOKEN=8;
    public static final int LONG_TOKEN=31;
    public static final int LITERAL_TOKEN=34;
    public static final int BIND_SYMBOL=73;
    public static final int DOUBLE=84;
    public static final int SUCH=43;
    public static final int THEN_AFTER_TOKEN=23;
    public static final int RBRACE=72;
    public static final int NameFirstCharacter=85;
    public static final int NULL=64;
    public static final int ESCAPE_KEYWORD=66;
    public static final int DOUBLE_TOKEN=33;
    public static final int WHITESPACE=89;
    public static final int LONG_SUFFIX=78;
    public static final int BOOLEAN_TOKEN=29;
    public static final int ALL_TOKEN=15;
    public static final int MATCH_TOKEN=10;
    public static final int REPEAT_TOKEN=24;
    public static final int EVENT_TOKEN=7;
    public static final int MILLISECONDS=61;
    public static final int DEFINE_PATTERN_TOKEN=4;
    public static final int STARTS_WITH_TOKEN=12;
    public static final int STARTS=45;
    public static final int THEN_ANY_ONE_TOKEN=18;
    public static final int WITHIN=52;
    public static final int ESCAPE_SPACE=65;
    public static final int REPEAT=51;
    public static final int DURING=53;
    public static final int NameCharacter=86;
    public static final int THEN_DURING_TOKEN=22;
    public static final int DATETIME_TOKEN=26;
    public static final int FALSE=63;
    public static final int THEN_WITHIN_TOKEN=21;
    public static final int WHERE=41;
    public static final int BIND_TOKEN=27;
    public static final int SUBSCRIPTION_TOKEN=9;
    public static final int LBRACE=71;
    public static final int ITEM_TOKEN=13;
    public static final int SECONDS=60;
    public static final int FLOAT=83;
    public static final int SUBPATTERN_ITEM_TOKEN=16;
    public static final int AND=42;
    public static final int DEFINE=36;
    public static final int LPAREN=69;
    public static final int AS=40;
    public static final int THEN=50;
    public static final int COMMA=67;
    public static final int IDENTIFIER=87;
    public static final int ALL=49;
    public static final int DIGIT=76;
    public static final int ANY_ONE_TOKEN=14;
    public static final int WITH=46;
    public static final int ALLCHARS=90;
    public static final int TO=54;
    public static final int THEN_TOKEN=17;
    public static final int EVENT_LIST_TOKEN=6;
    public static final int WHERE_TOKEN=11;
    public static final int INTEGER_TOKEN=30;
    public static final int AFTER=56;
    public static final int EVENT=39;
    public static final int TRUE=62;
    public static final int SEMI=68;
    public static final int THEN_ALL_TOKEN=19;
    public static final int MINUTES=59;
    public static final int DOUBLE_SUFFIX=79;
    public static final int DAYS=57;
    public static final int DATE_TOKEN=25;
    public static final int ANY=47;
    public static final int UNSIGNED=81;
    public static final int NEWLINE=35;
    public static final int FLOAT_TOKEN=32;
    public static final int TIMES=55;
    public static final int LONG=82;
    public static final int HOURS=58;

    // delegates
    // delegators


        public patternParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public patternParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        
    protected TreeAdaptor adaptor = new CommonTreeAdaptor();

    public void setTreeAdaptor(TreeAdaptor adaptor) {
        this.adaptor = adaptor;
    }
    public TreeAdaptor getTreeAdaptor() {
        return adaptor;
    }

    public String[] getTokenNames() { return patternParser.tokenNames; }
    public String getGrammarFileName() { return "Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g"; }


    	ExceptionHandler handler = new ExceptionHandler();
    	
    	public LanguageException createException(IntStream intStream) {
    		CommonTokenStream cts = (CommonTokenStream) intStream;
    		patternLexer pl = (patternLexer) cts.getTokenSource();
    		PatternLanguageLexer pll = new PatternLanguageLexer(pl);
    		ANTLRStringStream acs = (ANTLRStringStream) pll.getInputSource();
    		String inputText = acs.substring(0, acs.size()-1);
    		int errorline = cts.LT(1).getLine();
    		int errorpos = cts.LT(1).getCharPositionInLine();
           		if (errorpos < 0) { //backtrack since token is missing or invalid
    			errorline = cts.LT(-1).getLine();
        			errorpos = ((CommonToken) cts.LT(-1)).getStopIndex()+1;
    			return new LanguageException("Error on line " + errorline + " at position " + errorpos + ":" + 
    			inputText.substring(0, errorpos) + "^" + inputText.substring(errorpos), inputText, errorpos);
           		}
    		return new LanguageException("Error on line " + errorline + " at position " + errorpos + ":" + 
    			inputText.substring(0, errorpos) + "^" + inputText.substring(errorpos), inputText, errorpos);
    	}
    	
    	public void mismatch(IntStream intStream, int ttype, BitSet follow) 
    			throws RecognitionException {
    		handler.addException(createException(intStream));
    	}
    	
    	protected Object recoverFromMismatchedToken(IntStream intStream, int i, BitSet bitSet) 
    			throws RecognitionException { 
    		handler.addException(createException(intStream));
    		return null;
    	}

    	public Object recoverFromMismatchedSet(IntStream intStream, RecognitionException e, BitSet follow) 
    			throws RecognitionException {
    		handler.addException(createException(intStream));
    		return null;
    	}
    	
    	public ExceptionHandler getExceptionHandler() {
    	        return handler;
    	}


    public static class digits_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "digits"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:219:1: digits : ( SIGNED | UNSIGNED );
    public final patternParser.digits_return digits() throws RecognitionException {
        patternParser.digits_return retval = new patternParser.digits_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set1=null;

        Object set1_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:219:10: ( SIGNED | UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            root_0 = (Object)adaptor.nil();

            set1=(Token)input.LT(1);
            if ( (input.LA(1)>=SIGNED && input.LA(1)<=UNSIGNED) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set1));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "digits"

    public static class year_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "year"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:221:1: year : UNSIGNED ;
    public final patternParser.year_return year() throws RecognitionException {
        patternParser.year_return retval = new patternParser.year_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED2=null;

        Object UNSIGNED2_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:221:8: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:221:10: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED2=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_year1386); 
            UNSIGNED2_tree = (Object)adaptor.create(UNSIGNED2);
            adaptor.addChild(root_0, UNSIGNED2_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "year"

    public static class month_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "month"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:222:1: month : UNSIGNED ;
    public final patternParser.month_return month() throws RecognitionException {
        patternParser.month_return retval = new patternParser.month_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED3=null;

        Object UNSIGNED3_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:222:9: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:222:11: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED3=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_month1395); 
            UNSIGNED3_tree = (Object)adaptor.create(UNSIGNED3);
            adaptor.addChild(root_0, UNSIGNED3_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "month"

    public static class day_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "day"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:223:1: day : UNSIGNED ;
    public final patternParser.day_return day() throws RecognitionException {
        patternParser.day_return retval = new patternParser.day_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED4=null;

        Object UNSIGNED4_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:223:7: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:223:9: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED4=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_day1404); 
            UNSIGNED4_tree = (Object)adaptor.create(UNSIGNED4);
            adaptor.addChild(root_0, UNSIGNED4_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "day"

    public static class hour_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "hour"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:224:1: hour : UNSIGNED ;
    public final patternParser.hour_return hour() throws RecognitionException {
        patternParser.hour_return retval = new patternParser.hour_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED5=null;

        Object UNSIGNED5_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:224:8: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:224:10: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED5=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_hour1413); 
            UNSIGNED5_tree = (Object)adaptor.create(UNSIGNED5);
            adaptor.addChild(root_0, UNSIGNED5_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "hour"

    public static class minute_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "minute"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:225:1: minute : UNSIGNED ;
    public final patternParser.minute_return minute() throws RecognitionException {
        patternParser.minute_return retval = new patternParser.minute_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED6=null;

        Object UNSIGNED6_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:225:10: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:225:12: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED6=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_minute1422); 
            UNSIGNED6_tree = (Object)adaptor.create(UNSIGNED6);
            adaptor.addChild(root_0, UNSIGNED6_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "minute"

    public static class second_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "second"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:226:1: second : UNSIGNED ;
    public final patternParser.second_return second() throws RecognitionException {
        patternParser.second_return retval = new patternParser.second_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED7=null;

        Object UNSIGNED7_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:226:10: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:226:12: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED7=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_second1431); 
            UNSIGNED7_tree = (Object)adaptor.create(UNSIGNED7);
            adaptor.addChild(root_0, UNSIGNED7_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "second"

    public static class millisecond_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "millisecond"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:227:1: millisecond : UNSIGNED ;
    public final patternParser.millisecond_return millisecond() throws RecognitionException {
        patternParser.millisecond_return retval = new patternParser.millisecond_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED8=null;

        Object UNSIGNED8_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:227:14: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:227:16: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED8=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_millisecond1439); 
            UNSIGNED8_tree = (Object)adaptor.create(UNSIGNED8);
            adaptor.addChild(root_0, UNSIGNED8_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "millisecond"

    public static class keywords_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "keywords"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:229:1: keywords : ( DEFINE | PATTERN | USING | EVENT | AS | WHERE | AND | SUCH | THAT | STARTS | WITH | ANY | ONE | ALL | THEN | REPEAT | WITHIN | DURING | TO | TIMES | AFTER | DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS | TRUE | FALSE | NULL );
    public final patternParser.keywords_return keywords() throws RecognitionException {
        patternParser.keywords_return retval = new patternParser.keywords_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set9=null;

        Object set9_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:229:11: ( DEFINE | PATTERN | USING | EVENT | AS | WHERE | AND | SUCH | THAT | STARTS | WITH | ANY | ONE | ALL | THEN | REPEAT | WITHIN | DURING | TO | TIMES | AFTER | DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS | TRUE | FALSE | NULL )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            root_0 = (Object)adaptor.nil();

            set9=(Token)input.LT(1);
            if ( (input.LA(1)>=DEFINE && input.LA(1)<=NULL) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set9));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "keywords"

    public static class escaped_keyword_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "escaped_keyword"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:232:1: escaped_keyword : ESCAPE_KEYWORD keywords -> keywords ;
    public final patternParser.escaped_keyword_return escaped_keyword() throws RecognitionException {
        patternParser.escaped_keyword_return retval = new patternParser.escaped_keyword_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ESCAPE_KEYWORD10=null;
        patternParser.keywords_return keywords11 = null;


        Object ESCAPE_KEYWORD10_tree=null;
        RewriteRuleTokenStream stream_ESCAPE_KEYWORD=new RewriteRuleTokenStream(adaptor,"token ESCAPE_KEYWORD");
        RewriteRuleSubtreeStream stream_keywords=new RewriteRuleSubtreeStream(adaptor,"rule keywords");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:232:18: ( ESCAPE_KEYWORD keywords -> keywords )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:232:20: ESCAPE_KEYWORD keywords
            {
            ESCAPE_KEYWORD10=(Token)match(input,ESCAPE_KEYWORD,FOLLOW_ESCAPE_KEYWORD_in_escaped_keyword1519);  
            stream_ESCAPE_KEYWORD.add(ESCAPE_KEYWORD10);

            pushFollow(FOLLOW_keywords_in_escaped_keyword1521);
            keywords11=keywords();

            state._fsp--;

            stream_keywords.add(keywords11.getTree());


            // AST REWRITE
            // elements: keywords
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 232:44: -> keywords
            {
                adaptor.addChild(root_0, stream_keywords.nextTree());

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "escaped_keyword"

    public static class bindvar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "bindvar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:234:1: bindvar : ( BIND_SYMBOL identifier ) ;
    public final patternParser.bindvar_return bindvar() throws RecognitionException {
        patternParser.bindvar_return retval = new patternParser.bindvar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token BIND_SYMBOL12=null;
        patternParser.identifier_return identifier13 = null;


        Object BIND_SYMBOL12_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:234:11: ( ( BIND_SYMBOL identifier ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:234:13: ( BIND_SYMBOL identifier )
            {
            root_0 = (Object)adaptor.nil();

            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:234:13: ( BIND_SYMBOL identifier )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:234:14: BIND_SYMBOL identifier
            {
            BIND_SYMBOL12=(Token)match(input,BIND_SYMBOL,FOLLOW_BIND_SYMBOL_in_bindvar1536); 
            BIND_SYMBOL12_tree = (Object)adaptor.create(BIND_SYMBOL12);
            adaptor.addChild(root_0, BIND_SYMBOL12_tree);

            pushFollow(FOLLOW_identifier_in_bindvar1538);
            identifier13=identifier();

            state._fsp--;

            adaptor.addChild(root_0, identifier13.getTree());

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "bindvar"

    public static class datevar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "datevar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:1: datevar : ( '$date(' d11= year COMMA d12= month COMMA d13= day ( COMMA d14= stringvar )? ')' -> ^( DATE_TOKEN $d11 $d12 $d13 ( $d14)? ) | '$date(' d21= year COMMA d22= month COMMA d23= day COMMA d24= NULL ')' -> ^( DATE_TOKEN $d21 $d22 $d23) );
    public final patternParser.datevar_return datevar() throws RecognitionException {
        patternParser.datevar_return retval = new patternParser.datevar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token d24=null;
        Token string_literal14=null;
        Token COMMA15=null;
        Token COMMA16=null;
        Token COMMA17=null;
        Token char_literal18=null;
        Token string_literal19=null;
        Token COMMA20=null;
        Token COMMA21=null;
        Token COMMA22=null;
        Token char_literal23=null;
        patternParser.year_return d11 = null;

        patternParser.month_return d12 = null;

        patternParser.day_return d13 = null;

        patternParser.stringvar_return d14 = null;

        patternParser.year_return d21 = null;

        patternParser.month_return d22 = null;

        patternParser.day_return d23 = null;


        Object d24_tree=null;
        Object string_literal14_tree=null;
        Object COMMA15_tree=null;
        Object COMMA16_tree=null;
        Object COMMA17_tree=null;
        Object char_literal18_tree=null;
        Object string_literal19_tree=null;
        Object COMMA20_tree=null;
        Object COMMA21_tree=null;
        Object COMMA22_tree=null;
        Object char_literal23_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_91=new RewriteRuleTokenStream(adaptor,"token 91");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_NULL=new RewriteRuleTokenStream(adaptor,"token NULL");
        RewriteRuleSubtreeStream stream_month=new RewriteRuleSubtreeStream(adaptor,"rule month");
        RewriteRuleSubtreeStream stream_year=new RewriteRuleSubtreeStream(adaptor,"rule year");
        RewriteRuleSubtreeStream stream_day=new RewriteRuleSubtreeStream(adaptor,"rule day");
        RewriteRuleSubtreeStream stream_stringvar=new RewriteRuleSubtreeStream(adaptor,"rule stringvar");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:11: ( '$date(' d11= year COMMA d12= month COMMA d13= day ( COMMA d14= stringvar )? ')' -> ^( DATE_TOKEN $d11 $d12 $d13 ( $d14)? ) | '$date(' d21= year COMMA d22= month COMMA d23= day COMMA d24= NULL ')' -> ^( DATE_TOKEN $d21 $d22 $d23) )
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:13: '$date(' d11= year COMMA d12= month COMMA d13= day ( COMMA d14= stringvar )? ')'
                    {
                    string_literal14=(Token)match(input,91,FOLLOW_91_in_datevar1549);  
                    stream_91.add(string_literal14);

                    pushFollow(FOLLOW_year_in_datevar1553);
                    d11=year();

                    state._fsp--;

                    stream_year.add(d11.getTree());
                    COMMA15=(Token)match(input,COMMA,FOLLOW_COMMA_in_datevar1555);  
                    stream_COMMA.add(COMMA15);

                    pushFollow(FOLLOW_month_in_datevar1559);
                    d12=month();

                    state._fsp--;

                    stream_month.add(d12.getTree());
                    COMMA16=(Token)match(input,COMMA,FOLLOW_COMMA_in_datevar1561);  
                    stream_COMMA.add(COMMA16);

                    pushFollow(FOLLOW_day_in_datevar1565);
                    d13=day();

                    state._fsp--;

                    stream_day.add(d13.getTree());
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:61: ( COMMA d14= stringvar )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==COMMA) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:62: COMMA d14= stringvar
                            {
                            COMMA17=(Token)match(input,COMMA,FOLLOW_COMMA_in_datevar1568);  
                            stream_COMMA.add(COMMA17);

                            pushFollow(FOLLOW_stringvar_in_datevar1572);
                            d14=stringvar();

                            state._fsp--;

                            stream_stringvar.add(d14.getTree());

                            }
                            break;

                    }

                    char_literal18=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_datevar1576);  
                    stream_RPAREN.add(char_literal18);



                    // AST REWRITE
                    // elements: d11, d14, d13, d12
                    // token labels: 
                    // rule labels: d12, d11, retval, d14, d13
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_d12=new RewriteRuleSubtreeStream(adaptor,"rule d12",d12!=null?d12.tree:null);
                    RewriteRuleSubtreeStream stream_d11=new RewriteRuleSubtreeStream(adaptor,"rule d11",d11!=null?d11.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_d14=new RewriteRuleSubtreeStream(adaptor,"rule d14",d14!=null?d14.tree:null);
                    RewriteRuleSubtreeStream stream_d13=new RewriteRuleSubtreeStream(adaptor,"rule d13",d13!=null?d13.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 236:88: -> ^( DATE_TOKEN $d11 $d12 $d13 ( $d14)? )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:91: ^( DATE_TOKEN $d11 $d12 $d13 ( $d14)? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DATE_TOKEN, "DATE_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_d11.nextTree());
                        adaptor.addChild(root_1, stream_d12.nextTree());
                        adaptor.addChild(root_1, stream_d13.nextTree());
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:236:119: ( $d14)?
                        if ( stream_d14.hasNext() ) {
                            adaptor.addChild(root_1, stream_d14.nextTree());

                        }
                        stream_d14.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:237:9: '$date(' d21= year COMMA d22= month COMMA d23= day COMMA d24= NULL ')'
                    {
                    string_literal19=(Token)match(input,91,FOLLOW_91_in_datevar1605);  
                    stream_91.add(string_literal19);

                    pushFollow(FOLLOW_year_in_datevar1609);
                    d21=year();

                    state._fsp--;

                    stream_year.add(d21.getTree());
                    COMMA20=(Token)match(input,COMMA,FOLLOW_COMMA_in_datevar1611);  
                    stream_COMMA.add(COMMA20);

                    pushFollow(FOLLOW_month_in_datevar1615);
                    d22=month();

                    state._fsp--;

                    stream_month.add(d22.getTree());
                    COMMA21=(Token)match(input,COMMA,FOLLOW_COMMA_in_datevar1617);  
                    stream_COMMA.add(COMMA21);

                    pushFollow(FOLLOW_day_in_datevar1621);
                    d23=day();

                    state._fsp--;

                    stream_day.add(d23.getTree());
                    COMMA22=(Token)match(input,COMMA,FOLLOW_COMMA_in_datevar1623);  
                    stream_COMMA.add(COMMA22);

                    d24=(Token)match(input,NULL,FOLLOW_NULL_in_datevar1627);  
                    stream_NULL.add(d24);

                    char_literal23=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_datevar1629);  
                    stream_RPAREN.add(char_literal23);



                    // AST REWRITE
                    // elements: d23, d21, d22
                    // token labels: 
                    // rule labels: retval, d23, d22, d21
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_d23=new RewriteRuleSubtreeStream(adaptor,"rule d23",d23!=null?d23.tree:null);
                    RewriteRuleSubtreeStream stream_d22=new RewriteRuleSubtreeStream(adaptor,"rule d22",d22!=null?d22.tree:null);
                    RewriteRuleSubtreeStream stream_d21=new RewriteRuleSubtreeStream(adaptor,"rule d21",d21!=null?d21.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 237:76: -> ^( DATE_TOKEN $d21 $d22 $d23)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:237:79: ^( DATE_TOKEN $d21 $d22 $d23)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DATE_TOKEN, "DATE_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_d21.nextTree());
                        adaptor.addChild(root_1, stream_d22.nextTree());
                        adaptor.addChild(root_1, stream_d23.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "datevar"

    public static class datetimevar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "datetimevar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:239:1: datetimevar : ( '$datetime(' d11= year COMMA d12= month COMMA d13= day COMMA d14= hour COMMA d15= minute COMMA d16= second COMMA d17= millisecond ( COMMA d18= stringvar )? ')' -> ^( DATETIME_TOKEN $d11 $d12 $d13 $d14 $d15 $d16 $d17 ( $d18)? ) | '$datetime(' d21= year COMMA d22= month COMMA d23= day COMMA d24= hour COMMA d25= minute COMMA d26= second COMMA d27= millisecond COMMA d28= NULL ')' -> ^( DATETIME_TOKEN $d21 $d22 $d23 $d24 $d25 $d26 $d27) );
    public final patternParser.datetimevar_return datetimevar() throws RecognitionException {
        patternParser.datetimevar_return retval = new patternParser.datetimevar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token d28=null;
        Token string_literal24=null;
        Token COMMA25=null;
        Token COMMA26=null;
        Token COMMA27=null;
        Token COMMA28=null;
        Token COMMA29=null;
        Token COMMA30=null;
        Token COMMA31=null;
        Token char_literal32=null;
        Token string_literal33=null;
        Token COMMA34=null;
        Token COMMA35=null;
        Token COMMA36=null;
        Token COMMA37=null;
        Token COMMA38=null;
        Token COMMA39=null;
        Token COMMA40=null;
        Token char_literal41=null;
        patternParser.year_return d11 = null;

        patternParser.month_return d12 = null;

        patternParser.day_return d13 = null;

        patternParser.hour_return d14 = null;

        patternParser.minute_return d15 = null;

        patternParser.second_return d16 = null;

        patternParser.millisecond_return d17 = null;

        patternParser.stringvar_return d18 = null;

        patternParser.year_return d21 = null;

        patternParser.month_return d22 = null;

        patternParser.day_return d23 = null;

        patternParser.hour_return d24 = null;

        patternParser.minute_return d25 = null;

        patternParser.second_return d26 = null;

        patternParser.millisecond_return d27 = null;


        Object d28_tree=null;
        Object string_literal24_tree=null;
        Object COMMA25_tree=null;
        Object COMMA26_tree=null;
        Object COMMA27_tree=null;
        Object COMMA28_tree=null;
        Object COMMA29_tree=null;
        Object COMMA30_tree=null;
        Object COMMA31_tree=null;
        Object char_literal32_tree=null;
        Object string_literal33_tree=null;
        Object COMMA34_tree=null;
        Object COMMA35_tree=null;
        Object COMMA36_tree=null;
        Object COMMA37_tree=null;
        Object COMMA38_tree=null;
        Object COMMA39_tree=null;
        Object COMMA40_tree=null;
        Object char_literal41_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_92=new RewriteRuleTokenStream(adaptor,"token 92");
        RewriteRuleTokenStream stream_COMMA=new RewriteRuleTokenStream(adaptor,"token COMMA");
        RewriteRuleTokenStream stream_NULL=new RewriteRuleTokenStream(adaptor,"token NULL");
        RewriteRuleSubtreeStream stream_minute=new RewriteRuleSubtreeStream(adaptor,"rule minute");
        RewriteRuleSubtreeStream stream_second=new RewriteRuleSubtreeStream(adaptor,"rule second");
        RewriteRuleSubtreeStream stream_millisecond=new RewriteRuleSubtreeStream(adaptor,"rule millisecond");
        RewriteRuleSubtreeStream stream_month=new RewriteRuleSubtreeStream(adaptor,"rule month");
        RewriteRuleSubtreeStream stream_year=new RewriteRuleSubtreeStream(adaptor,"rule year");
        RewriteRuleSubtreeStream stream_day=new RewriteRuleSubtreeStream(adaptor,"rule day");
        RewriteRuleSubtreeStream stream_hour=new RewriteRuleSubtreeStream(adaptor,"rule hour");
        RewriteRuleSubtreeStream stream_stringvar=new RewriteRuleSubtreeStream(adaptor,"rule stringvar");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:239:14: ( '$datetime(' d11= year COMMA d12= month COMMA d13= day COMMA d14= hour COMMA d15= minute COMMA d16= second COMMA d17= millisecond ( COMMA d18= stringvar )? ')' -> ^( DATETIME_TOKEN $d11 $d12 $d13 $d14 $d15 $d16 $d17 ( $d18)? ) | '$datetime(' d21= year COMMA d22= month COMMA d23= day COMMA d24= hour COMMA d25= minute COMMA d26= second COMMA d27= millisecond COMMA d28= NULL ')' -> ^( DATETIME_TOKEN $d21 $d22 $d23 $d24 $d25 $d26 $d27) )
            int alt4=2;
            alt4 = dfa4.predict(input);
            switch (alt4) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:239:16: '$datetime(' d11= year COMMA d12= month COMMA d13= day COMMA d14= hour COMMA d15= minute COMMA d16= second COMMA d17= millisecond ( COMMA d18= stringvar )? ')'
                    {
                    string_literal24=(Token)match(input,92,FOLLOW_92_in_datetimevar1653);  
                    stream_92.add(string_literal24);

                    pushFollow(FOLLOW_year_in_datetimevar1657);
                    d11=year();

                    state._fsp--;

                    stream_year.add(d11.getTree());
                    COMMA25=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1659);  
                    stream_COMMA.add(COMMA25);

                    pushFollow(FOLLOW_month_in_datetimevar1663);
                    d12=month();

                    state._fsp--;

                    stream_month.add(d12.getTree());
                    COMMA26=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1665);  
                    stream_COMMA.add(COMMA26);

                    pushFollow(FOLLOW_day_in_datetimevar1669);
                    d13=day();

                    state._fsp--;

                    stream_day.add(d13.getTree());
                    COMMA27=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1671);  
                    stream_COMMA.add(COMMA27);

                    pushFollow(FOLLOW_hour_in_datetimevar1675);
                    d14=hour();

                    state._fsp--;

                    stream_hour.add(d14.getTree());
                    COMMA28=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1677);  
                    stream_COMMA.add(COMMA28);

                    pushFollow(FOLLOW_minute_in_datetimevar1681);
                    d15=minute();

                    state._fsp--;

                    stream_minute.add(d15.getTree());
                    COMMA29=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1683);  
                    stream_COMMA.add(COMMA29);

                    pushFollow(FOLLOW_second_in_datetimevar1687);
                    d16=second();

                    state._fsp--;

                    stream_second.add(d16.getTree());
                    COMMA30=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1690);  
                    stream_COMMA.add(COMMA30);

                    pushFollow(FOLLOW_millisecond_in_datetimevar1699);
                    d17=millisecond();

                    state._fsp--;

                    stream_millisecond.add(d17.getTree());
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:240:21: ( COMMA d18= stringvar )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==COMMA) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:240:22: COMMA d18= stringvar
                            {
                            COMMA31=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1702);  
                            stream_COMMA.add(COMMA31);

                            pushFollow(FOLLOW_stringvar_in_datetimevar1706);
                            d18=stringvar();

                            state._fsp--;

                            stream_stringvar.add(d18.getTree());

                            }
                            break;

                    }

                    char_literal32=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_datetimevar1710);  
                    stream_RPAREN.add(char_literal32);



                    // AST REWRITE
                    // elements: d18, d16, d13, d17, d15, d11, d12, d14
                    // token labels: 
                    // rule labels: d12, d11, retval, d14, d13, d16, d15, d18, d17
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_d12=new RewriteRuleSubtreeStream(adaptor,"rule d12",d12!=null?d12.tree:null);
                    RewriteRuleSubtreeStream stream_d11=new RewriteRuleSubtreeStream(adaptor,"rule d11",d11!=null?d11.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_d14=new RewriteRuleSubtreeStream(adaptor,"rule d14",d14!=null?d14.tree:null);
                    RewriteRuleSubtreeStream stream_d13=new RewriteRuleSubtreeStream(adaptor,"rule d13",d13!=null?d13.tree:null);
                    RewriteRuleSubtreeStream stream_d16=new RewriteRuleSubtreeStream(adaptor,"rule d16",d16!=null?d16.tree:null);
                    RewriteRuleSubtreeStream stream_d15=new RewriteRuleSubtreeStream(adaptor,"rule d15",d15!=null?d15.tree:null);
                    RewriteRuleSubtreeStream stream_d18=new RewriteRuleSubtreeStream(adaptor,"rule d18",d18!=null?d18.tree:null);
                    RewriteRuleSubtreeStream stream_d17=new RewriteRuleSubtreeStream(adaptor,"rule d17",d17!=null?d17.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 240:48: -> ^( DATETIME_TOKEN $d11 $d12 $d13 $d14 $d15 $d16 $d17 ( $d18)? )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:240:51: ^( DATETIME_TOKEN $d11 $d12 $d13 $d14 $d15 $d16 $d17 ( $d18)? )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DATETIME_TOKEN, "DATETIME_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_d11.nextTree());
                        adaptor.addChild(root_1, stream_d12.nextTree());
                        adaptor.addChild(root_1, stream_d13.nextTree());
                        adaptor.addChild(root_1, stream_d14.nextTree());
                        adaptor.addChild(root_1, stream_d15.nextTree());
                        adaptor.addChild(root_1, stream_d16.nextTree());
                        adaptor.addChild(root_1, stream_d17.nextTree());
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:240:103: ( $d18)?
                        if ( stream_d18.hasNext() ) {
                            adaptor.addChild(root_1, stream_d18.nextTree());

                        }
                        stream_d18.reset();

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:241:8: '$datetime(' d21= year COMMA d22= month COMMA d23= day COMMA d24= hour COMMA d25= minute COMMA d26= second COMMA d27= millisecond COMMA d28= NULL ')'
                    {
                    string_literal33=(Token)match(input,92,FOLLOW_92_in_datetimevar1750);  
                    stream_92.add(string_literal33);

                    pushFollow(FOLLOW_year_in_datetimevar1754);
                    d21=year();

                    state._fsp--;

                    stream_year.add(d21.getTree());
                    COMMA34=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1756);  
                    stream_COMMA.add(COMMA34);

                    pushFollow(FOLLOW_month_in_datetimevar1760);
                    d22=month();

                    state._fsp--;

                    stream_month.add(d22.getTree());
                    COMMA35=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1762);  
                    stream_COMMA.add(COMMA35);

                    pushFollow(FOLLOW_day_in_datetimevar1766);
                    d23=day();

                    state._fsp--;

                    stream_day.add(d23.getTree());
                    COMMA36=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1768);  
                    stream_COMMA.add(COMMA36);

                    pushFollow(FOLLOW_hour_in_datetimevar1772);
                    d24=hour();

                    state._fsp--;

                    stream_hour.add(d24.getTree());
                    COMMA37=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1774);  
                    stream_COMMA.add(COMMA37);

                    pushFollow(FOLLOW_minute_in_datetimevar1778);
                    d25=minute();

                    state._fsp--;

                    stream_minute.add(d25.getTree());
                    COMMA38=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1780);  
                    stream_COMMA.add(COMMA38);

                    pushFollow(FOLLOW_second_in_datetimevar1784);
                    d26=second();

                    state._fsp--;

                    stream_second.add(d26.getTree());
                    COMMA39=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1787);  
                    stream_COMMA.add(COMMA39);

                    pushFollow(FOLLOW_millisecond_in_datetimevar1798);
                    d27=millisecond();

                    state._fsp--;

                    stream_millisecond.add(d27.getTree());
                    COMMA40=(Token)match(input,COMMA,FOLLOW_COMMA_in_datetimevar1800);  
                    stream_COMMA.add(COMMA40);

                    d28=(Token)match(input,NULL,FOLLOW_NULL_in_datetimevar1804);  
                    stream_NULL.add(d28);

                    char_literal41=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_datetimevar1806);  
                    stream_RPAREN.add(char_literal41);



                    // AST REWRITE
                    // elements: d24, d27, d21, d23, d26, d22, d25
                    // token labels: 
                    // rule labels: d25, d24, retval, d23, d22, d21, d27, d26
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_d25=new RewriteRuleSubtreeStream(adaptor,"rule d25",d25!=null?d25.tree:null);
                    RewriteRuleSubtreeStream stream_d24=new RewriteRuleSubtreeStream(adaptor,"rule d24",d24!=null?d24.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_d23=new RewriteRuleSubtreeStream(adaptor,"rule d23",d23!=null?d23.tree:null);
                    RewriteRuleSubtreeStream stream_d22=new RewriteRuleSubtreeStream(adaptor,"rule d22",d22!=null?d22.tree:null);
                    RewriteRuleSubtreeStream stream_d21=new RewriteRuleSubtreeStream(adaptor,"rule d21",d21!=null?d21.tree:null);
                    RewriteRuleSubtreeStream stream_d27=new RewriteRuleSubtreeStream(adaptor,"rule d27",d27!=null?d27.tree:null);
                    RewriteRuleSubtreeStream stream_d26=new RewriteRuleSubtreeStream(adaptor,"rule d26",d26!=null?d26.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 242:42: -> ^( DATETIME_TOKEN $d21 $d22 $d23 $d24 $d25 $d26 $d27)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:242:45: ^( DATETIME_TOKEN $d21 $d22 $d23 $d24 $d25 $d26 $d27)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DATETIME_TOKEN, "DATETIME_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_d21.nextTree());
                        adaptor.addChild(root_1, stream_d22.nextTree());
                        adaptor.addChild(root_1, stream_d23.nextTree());
                        adaptor.addChild(root_1, stream_d24.nextTree());
                        adaptor.addChild(root_1, stream_d25.nextTree());
                        adaptor.addChild(root_1, stream_d26.nextTree());
                        adaptor.addChild(root_1, stream_d27.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "datetimevar"

    public static class stringvar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "stringvar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:244:1: stringvar : NAME ;
    public final patternParser.stringvar_return stringvar() throws RecognitionException {
        patternParser.stringvar_return retval = new patternParser.stringvar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME42=null;

        Object NAME42_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:244:12: ( NAME )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:244:14: NAME
            {
            root_0 = (Object)adaptor.nil();

            NAME42=(Token)match(input,NAME,FOLLOW_NAME_in_stringvar1842); 
            NAME42_tree = (Object)adaptor.create(NAME42);
            adaptor.addChild(root_0, NAME42_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "stringvar"

    public static class booleanvar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "booleanvar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:246:1: booleanvar : ( TRUE | FALSE );
    public final patternParser.booleanvar_return booleanvar() throws RecognitionException {
        patternParser.booleanvar_return retval = new patternParser.booleanvar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set43=null;

        Object set43_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:246:13: ( TRUE | FALSE )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            root_0 = (Object)adaptor.nil();

            set43=(Token)input.LT(1);
            if ( (input.LA(1)>=TRUE && input.LA(1)<=FALSE) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set43));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "booleanvar"

    public static class integervar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "integervar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:248:1: integervar : ( SIGNED | UNSIGNED );
    public final patternParser.integervar_return integervar() throws RecognitionException {
        patternParser.integervar_return retval = new patternParser.integervar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set44=null;

        Object set44_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:248:13: ( SIGNED | UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            root_0 = (Object)adaptor.nil();

            set44=(Token)input.LT(1);
            if ( (input.LA(1)>=SIGNED && input.LA(1)<=UNSIGNED) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set44));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "integervar"

    public static class unsignedintegervar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "unsignedintegervar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:250:1: unsignedintegervar : UNSIGNED ;
    public final patternParser.unsignedintegervar_return unsignedintegervar() throws RecognitionException {
        patternParser.unsignedintegervar_return retval = new patternParser.unsignedintegervar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token UNSIGNED45=null;

        Object UNSIGNED45_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:250:20: ( UNSIGNED )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:250:22: UNSIGNED
            {
            root_0 = (Object)adaptor.nil();

            UNSIGNED45=(Token)match(input,UNSIGNED,FOLLOW_UNSIGNED_in_unsignedintegervar1876); 
            UNSIGNED45_tree = (Object)adaptor.create(UNSIGNED45);
            adaptor.addChild(root_0, UNSIGNED45_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "unsignedintegervar"

    public static class longvar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "longvar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:252:1: longvar : LONG ;
    public final patternParser.longvar_return longvar() throws RecognitionException {
        patternParser.longvar_return retval = new patternParser.longvar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LONG46=null;

        Object LONG46_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:252:11: ( LONG )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:252:13: LONG
            {
            root_0 = (Object)adaptor.nil();

            LONG46=(Token)match(input,LONG,FOLLOW_LONG_in_longvar1886); 
            LONG46_tree = (Object)adaptor.create(LONG46);
            adaptor.addChild(root_0, LONG46_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "longvar"

    public static class floatvar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "floatvar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:254:1: floatvar : FLOAT ;
    public final patternParser.floatvar_return floatvar() throws RecognitionException {
        patternParser.floatvar_return retval = new patternParser.floatvar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token FLOAT47=null;

        Object FLOAT47_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:254:11: ( FLOAT )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:254:13: FLOAT
            {
            root_0 = (Object)adaptor.nil();

            FLOAT47=(Token)match(input,FLOAT,FOLLOW_FLOAT_in_floatvar1895); 
            FLOAT47_tree = (Object)adaptor.create(FLOAT47);
            adaptor.addChild(root_0, FLOAT47_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "floatvar"

    public static class doublevar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "doublevar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:256:1: doublevar : DOUBLE ;
    public final patternParser.doublevar_return doublevar() throws RecognitionException {
        patternParser.doublevar_return retval = new patternParser.doublevar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DOUBLE48=null;

        Object DOUBLE48_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:256:13: ( DOUBLE )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:256:15: DOUBLE
            {
            root_0 = (Object)adaptor.nil();

            DOUBLE48=(Token)match(input,DOUBLE,FOLLOW_DOUBLE_in_doublevar1905); 
            DOUBLE48_tree = (Object)adaptor.create(DOUBLE48);
            adaptor.addChild(root_0, DOUBLE48_tree);


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "doublevar"

    public static class identifier_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "identifier"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:258:1: identifier : ( escaped_keyword | digits | IDENTIFIER );
    public final patternParser.identifier_return identifier() throws RecognitionException {
        patternParser.identifier_return retval = new patternParser.identifier_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token IDENTIFIER51=null;
        patternParser.escaped_keyword_return escaped_keyword49 = null;

        patternParser.digits_return digits50 = null;


        Object IDENTIFIER51_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:258:13: ( escaped_keyword | digits | IDENTIFIER )
            int alt5=3;
            switch ( input.LA(1) ) {
            case ESCAPE_KEYWORD:
                {
                alt5=1;
                }
                break;
            case SIGNED:
            case UNSIGNED:
                {
                alt5=2;
                }
                break;
            case IDENTIFIER:
                {
                alt5=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }

            switch (alt5) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:258:15: escaped_keyword
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_escaped_keyword_in_identifier1914);
                    escaped_keyword49=escaped_keyword();

                    state._fsp--;

                    adaptor.addChild(root_0, escaped_keyword49.getTree());

                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:258:33: digits
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_digits_in_identifier1918);
                    digits50=digits();

                    state._fsp--;

                    adaptor.addChild(root_0, digits50.getTree());

                    }
                    break;
                case 3 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:258:42: IDENTIFIER
                    {
                    root_0 = (Object)adaptor.nil();

                    IDENTIFIER51=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifier1922); 
                    IDENTIFIER51_tree = (Object)adaptor.create(IDENTIFIER51);
                    adaptor.addChild(root_0, IDENTIFIER51_tree);


                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "identifier"

    public static class main_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "main"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:260:1: main : ( query EOF -> query | EOF -> EOF );
    public final patternParser.main_return main() throws RecognitionException {
        patternParser.main_return retval = new patternParser.main_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token EOF53=null;
        Token EOF54=null;
        patternParser.query_return query52 = null;


        Object EOF53_tree=null;
        Object EOF54_tree=null;
        RewriteRuleTokenStream stream_EOF=new RewriteRuleTokenStream(adaptor,"token EOF");
        RewriteRuleSubtreeStream stream_query=new RewriteRuleSubtreeStream(adaptor,"rule query");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:260:8: ( query EOF -> query | EOF -> EOF )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==DEFINE) ) {
                alt6=1;
            }
            else if ( (LA6_0==EOF) ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:260:10: query EOF
                    {
                    pushFollow(FOLLOW_query_in_main1932);
                    query52=query();

                    state._fsp--;

                    stream_query.add(query52.getTree());
                    EOF53=(Token)match(input,EOF,FOLLOW_EOF_in_main1934);  
                    stream_EOF.add(EOF53);



                    // AST REWRITE
                    // elements: query
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 260:20: -> query
                    {
                        adaptor.addChild(root_0, stream_query.nextTree());

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:261:8: EOF
                    {
                    EOF54=(Token)match(input,EOF,FOLLOW_EOF_in_main1947);  
                    stream_EOF.add(EOF54);



                    // AST REWRITE
                    // elements: EOF
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 261:12: -> EOF
                    {
                        adaptor.addChild(root_0, stream_EOF.nextNode());

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "main"

    public static class query_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "query"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:263:1: query : define_pattern -> ^( define_pattern ) ;
    public final patternParser.query_return query() throws RecognitionException {
        patternParser.query_return retval = new patternParser.query_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.define_pattern_return define_pattern55 = null;


        RewriteRuleSubtreeStream stream_define_pattern=new RewriteRuleSubtreeStream(adaptor,"rule define_pattern");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:263:9: ( define_pattern -> ^( define_pattern ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:263:11: define_pattern
            {
            pushFollow(FOLLOW_define_pattern_in_query1961);
            define_pattern55=define_pattern();

            state._fsp--;

            stream_define_pattern.add(define_pattern55.getTree());


            // AST REWRITE
            // elements: define_pattern
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 263:26: -> ^( define_pattern )
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:263:29: ^( define_pattern )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_define_pattern.nextNode(), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "query"

    public static class define_pattern_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "define_pattern"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:265:1: define_pattern : DEFINE PATTERN id= patternname u= using -> ^( DEFINE_PATTERN_TOKEN $id $u) ;
    public final patternParser.define_pattern_return define_pattern() throws RecognitionException {
        patternParser.define_pattern_return retval = new patternParser.define_pattern_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token DEFINE56=null;
        Token PATTERN57=null;
        patternParser.patternname_return id = null;

        patternParser.using_return u = null;


        Object DEFINE56_tree=null;
        Object PATTERN57_tree=null;
        RewriteRuleTokenStream stream_PATTERN=new RewriteRuleTokenStream(adaptor,"token PATTERN");
        RewriteRuleTokenStream stream_DEFINE=new RewriteRuleTokenStream(adaptor,"token DEFINE");
        RewriteRuleSubtreeStream stream_using=new RewriteRuleSubtreeStream(adaptor,"rule using");
        RewriteRuleSubtreeStream stream_patternname=new RewriteRuleSubtreeStream(adaptor,"rule patternname");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:265:17: ( DEFINE PATTERN id= patternname u= using -> ^( DEFINE_PATTERN_TOKEN $id $u) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:265:19: DEFINE PATTERN id= patternname u= using
            {
            DEFINE56=(Token)match(input,DEFINE,FOLLOW_DEFINE_in_define_pattern1976);  
            stream_DEFINE.add(DEFINE56);

            PATTERN57=(Token)match(input,PATTERN,FOLLOW_PATTERN_in_define_pattern1978);  
            stream_PATTERN.add(PATTERN57);

            pushFollow(FOLLOW_patternname_in_define_pattern1982);
            id=patternname();

            state._fsp--;

            stream_patternname.add(id.getTree());
            pushFollow(FOLLOW_using_in_define_pattern1986);
            u=using();

            state._fsp--;

            stream_using.add(u.getTree());


            // AST REWRITE
            // elements: u, id
            // token labels: 
            // rule labels: id, retval, u
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_id=new RewriteRuleSubtreeStream(adaptor,"rule id",id!=null?id.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_u=new RewriteRuleSubtreeStream(adaptor,"rule u",u!=null?u.tree:null);

            root_0 = (Object)adaptor.nil();
            // 265:57: -> ^( DEFINE_PATTERN_TOKEN $id $u)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:265:60: ^( DEFINE_PATTERN_TOKEN $id $u)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DEFINE_PATTERN_TOKEN, "DEFINE_PATTERN_TOKEN"), root_1);

                adaptor.addChild(root_1, stream_id.nextTree());
                adaptor.addChild(root_1, stream_u.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "define_pattern"

    public static class patternname_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "patternname"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:267:1: patternname : ( NAME | identifier );
    public final patternParser.patternname_return patternname() throws RecognitionException {
        patternParser.patternname_return retval = new patternParser.patternname_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token NAME58=null;
        patternParser.identifier_return identifier59 = null;


        Object NAME58_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:267:14: ( NAME | identifier )
            int alt7=2;
            int LA7_0 = input.LA(1);

            if ( (LA7_0==NAME) ) {
                alt7=1;
            }
            else if ( (LA7_0==ESCAPE_KEYWORD||(LA7_0>=SIGNED && LA7_0<=UNSIGNED)||LA7_0==IDENTIFIER) ) {
                alt7=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 7, 0, input);

                throw nvae;
            }
            switch (alt7) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:267:16: NAME
                    {
                    root_0 = (Object)adaptor.nil();

                    NAME58=(Token)match(input,NAME,FOLLOW_NAME_in_patternname2012); 
                    NAME58_tree = (Object)adaptor.create(NAME58);
                    adaptor.addChild(root_0, NAME58_tree);


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:267:23: identifier
                    {
                    root_0 = (Object)adaptor.nil();

                    pushFollow(FOLLOW_identifier_in_patternname2016);
                    identifier59=identifier();

                    state._fsp--;

                    adaptor.addChild(root_0, identifier59.getTree());

                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "patternname"

    public static class using_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "using"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:269:1: using : USING events= event_list w= with -> ^( USING ^( EVENT_LIST_TOKEN $events) $w) ;
    public final patternParser.using_return using() throws RecognitionException {
        patternParser.using_return retval = new patternParser.using_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token USING60=null;
        patternParser.event_list_return events = null;

        patternParser.with_return w = null;


        Object USING60_tree=null;
        RewriteRuleTokenStream stream_USING=new RewriteRuleTokenStream(adaptor,"token USING");
        RewriteRuleSubtreeStream stream_with=new RewriteRuleSubtreeStream(adaptor,"rule with");
        RewriteRuleSubtreeStream stream_event_list=new RewriteRuleSubtreeStream(adaptor,"rule event_list");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:269:9: ( USING events= event_list w= with -> ^( USING ^( EVENT_LIST_TOKEN $events) $w) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:269:11: USING events= event_list w= with
            {
            USING60=(Token)match(input,USING,FOLLOW_USING_in_using2026);  
            stream_USING.add(USING60);

            pushFollow(FOLLOW_event_list_in_using2030);
            events=event_list();

            state._fsp--;

            stream_event_list.add(events.getTree());
            pushFollow(FOLLOW_with_in_using2034);
            w=with();

            state._fsp--;

            stream_with.add(w.getTree());


            // AST REWRITE
            // elements: USING, events, w
            // token labels: 
            // rule labels: w, retval, events
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_w=new RewriteRuleSubtreeStream(adaptor,"rule w",w!=null?w.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_events=new RewriteRuleSubtreeStream(adaptor,"rule events",events!=null?events.tree:null);

            root_0 = (Object)adaptor.nil();
            // 269:42: -> ^( USING ^( EVENT_LIST_TOKEN $events) $w)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:269:45: ^( USING ^( EVENT_LIST_TOKEN $events) $w)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_USING.nextNode(), root_1);

                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:269:53: ^( EVENT_LIST_TOKEN $events)
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(EVENT_LIST_TOKEN, "EVENT_LIST_TOKEN"), root_2);

                adaptor.addChild(root_2, stream_events.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_w.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "using"

    public static class event_list_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "event_list"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:1: event_list : ( event ( AND )? )+ -> ( ^( event ) )+ ;
    public final patternParser.event_list_return event_list() throws RecognitionException {
        patternParser.event_list_return retval = new patternParser.event_list_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND62=null;
        patternParser.event_return event61 = null;


        Object AND62_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_event=new RewriteRuleSubtreeStream(adaptor,"rule event");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:13: ( ( event ( AND )? )+ -> ( ^( event ) )+ )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:15: ( event ( AND )? )+
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:15: ( event ( AND )? )+
            int cnt9=0;
            loop9:
            do {
                int alt9=2;
                int LA9_0 = input.LA(1);

                if ( (LA9_0==ESCAPE_KEYWORD||(LA9_0>=SIGNED && LA9_0<=UNSIGNED)||(LA9_0>=IDENTIFIER && LA9_0<=NAME)) ) {
                    alt9=1;
                }


                switch (alt9) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:16: event ( AND )?
            	    {
            	    pushFollow(FOLLOW_event_in_event_list2060);
            	    event61=event();

            	    state._fsp--;

            	    stream_event.add(event61.getTree());
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:22: ( AND )?
            	    int alt8=2;
            	    int LA8_0 = input.LA(1);

            	    if ( (LA8_0==AND) ) {
            	        alt8=1;
            	    }
            	    switch (alt8) {
            	        case 1 :
            	            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:22: AND
            	            {
            	            AND62=(Token)match(input,AND,FOLLOW_AND_in_event_list2062);  
            	            stream_AND.add(AND62);


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt9 >= 1 ) break loop9;
                        EarlyExitException eee =
                            new EarlyExitException(9, input);
                        throw eee;
                }
                cnt9++;
            } while (true);



            // AST REWRITE
            // elements: event
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 271:29: -> ( ^( event ) )+
            {
                if ( !(stream_event.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_event.hasNext() ) {
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:271:32: ^( event )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(stream_event.nextNode(), root_1);

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_event.reset();

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "event_list"

    public static class event_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "event"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:273:1: event : id= patternname AS alias= identifier -> ^( EVENT $id $alias) ;
    public final patternParser.event_return event() throws RecognitionException {
        patternParser.event_return retval = new patternParser.event_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AS63=null;
        patternParser.patternname_return id = null;

        patternParser.identifier_return alias = null;


        Object AS63_tree=null;
        RewriteRuleTokenStream stream_AS=new RewriteRuleTokenStream(adaptor,"token AS");
        RewriteRuleSubtreeStream stream_patternname=new RewriteRuleSubtreeStream(adaptor,"rule patternname");
        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:273:9: (id= patternname AS alias= identifier -> ^( EVENT $id $alias) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:273:11: id= patternname AS alias= identifier
            {
            pushFollow(FOLLOW_patternname_in_event2084);
            id=patternname();

            state._fsp--;

            stream_patternname.add(id.getTree());
            AS63=(Token)match(input,AS,FOLLOW_AS_in_event2086);  
            stream_AS.add(AS63);

            pushFollow(FOLLOW_identifier_in_event2090);
            alias=identifier();

            state._fsp--;

            stream_identifier.add(alias.getTree());


            // AST REWRITE
            // elements: alias, id
            // token labels: 
            // rule labels: id, retval, alias
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_id=new RewriteRuleSubtreeStream(adaptor,"rule id",id!=null?id.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_alias=new RewriteRuleSubtreeStream(adaptor,"rule alias",alias!=null?alias.tree:null);

            root_0 = (Object)adaptor.nil();
            // 273:46: -> ^( EVENT $id $alias)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:273:49: ^( EVENT $id $alias)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(EVENT, "EVENT"), root_1);

                adaptor.addChild(root_1, stream_id.nextTree());
                adaptor.addChild(root_1, stream_alias.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "event"

    public static class with_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "with"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:275:1: with : WITH subs= subscription_list s= starts_with -> ^( WHERE ^( SUBSCRIPTION_LIST_TOKEN $subs) $s) ;
    public final patternParser.with_return with() throws RecognitionException {
        patternParser.with_return retval = new patternParser.with_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token WITH64=null;
        patternParser.subscription_list_return subs = null;

        patternParser.starts_with_return s = null;


        Object WITH64_tree=null;
        RewriteRuleTokenStream stream_WITH=new RewriteRuleTokenStream(adaptor,"token WITH");
        RewriteRuleSubtreeStream stream_subscription_list=new RewriteRuleSubtreeStream(adaptor,"rule subscription_list");
        RewriteRuleSubtreeStream stream_starts_with=new RewriteRuleSubtreeStream(adaptor,"rule starts_with");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:275:8: ( WITH subs= subscription_list s= starts_with -> ^( WHERE ^( SUBSCRIPTION_LIST_TOKEN $subs) $s) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:275:10: WITH subs= subscription_list s= starts_with
            {
            WITH64=(Token)match(input,WITH,FOLLOW_WITH_in_with2112);  
            stream_WITH.add(WITH64);

            pushFollow(FOLLOW_subscription_list_in_with2116);
            subs=subscription_list();

            state._fsp--;

            stream_subscription_list.add(subs.getTree());
            pushFollow(FOLLOW_starts_with_in_with2120);
            s=starts_with();

            state._fsp--;

            stream_starts_with.add(s.getTree());


            // AST REWRITE
            // elements: s, subs
            // token labels: 
            // rule labels: retval, s, subs
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);
            RewriteRuleSubtreeStream stream_subs=new RewriteRuleSubtreeStream(adaptor,"rule subs",subs!=null?subs.tree:null);

            root_0 = (Object)adaptor.nil();
            // 275:52: -> ^( WHERE ^( SUBSCRIPTION_LIST_TOKEN $subs) $s)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:275:55: ^( WHERE ^( SUBSCRIPTION_LIST_TOKEN $subs) $s)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(WHERE, "WHERE"), root_1);

                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:275:63: ^( SUBSCRIPTION_LIST_TOKEN $subs)
                {
                Object root_2 = (Object)adaptor.nil();
                root_2 = (Object)adaptor.becomeRoot((Object)adaptor.create(SUBSCRIPTION_LIST_TOKEN, "SUBSCRIPTION_LIST_TOKEN"), root_2);

                adaptor.addChild(root_2, stream_subs.nextTree());

                adaptor.addChild(root_1, root_2);
                }
                adaptor.addChild(root_1, stream_s.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "with"

    public static class subscription_list_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subscription_list"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:1: subscription_list : ( subscription ( AND )? )+ -> ( ^( subscription ) )+ ;
    public final patternParser.subscription_list_return subscription_list() throws RecognitionException {
        patternParser.subscription_list_return retval = new patternParser.subscription_list_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token AND66=null;
        patternParser.subscription_return subscription65 = null;


        Object AND66_tree=null;
        RewriteRuleTokenStream stream_AND=new RewriteRuleTokenStream(adaptor,"token AND");
        RewriteRuleSubtreeStream stream_subscription=new RewriteRuleSubtreeStream(adaptor,"rule subscription");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:19: ( ( subscription ( AND )? )+ -> ( ^( subscription ) )+ )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:21: ( subscription ( AND )? )+
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:21: ( subscription ( AND )? )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( (LA11_0==ESCAPE_KEYWORD||(LA11_0>=SIGNED && LA11_0<=UNSIGNED)||LA11_0==IDENTIFIER) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:22: subscription ( AND )?
            	    {
            	    pushFollow(FOLLOW_subscription_in_subscription_list2145);
            	    subscription65=subscription();

            	    state._fsp--;

            	    stream_subscription.add(subscription65.getTree());
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:35: ( AND )?
            	    int alt10=2;
            	    int LA10_0 = input.LA(1);

            	    if ( (LA10_0==AND) ) {
            	        alt10=1;
            	    }
            	    switch (alt10) {
            	        case 1 :
            	            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:35: AND
            	            {
            	            AND66=(Token)match(input,AND,FOLLOW_AND_in_subscription_list2147);  
            	            stream_AND.add(AND66);


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt11 >= 1 ) break loop11;
                        EarlyExitException eee =
                            new EarlyExitException(11, input);
                        throw eee;
                }
                cnt11++;
            } while (true);



            // AST REWRITE
            // elements: subscription
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 277:42: -> ( ^( subscription ) )+
            {
                if ( !(stream_subscription.hasNext()) ) {
                    throw new RewriteEarlyExitException();
                }
                while ( stream_subscription.hasNext() ) {
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:277:45: ^( subscription )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(stream_subscription.nextNode(), root_1);

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_subscription.reset();

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "subscription_list"

    public static class subscription_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subscription"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:1: subscription : f= field ( '=' s= subscriptionvar )? -> ^( SUBSCRIPTION_TOKEN $f ( $s)? ) ;
    public final patternParser.subscription_return subscription() throws RecognitionException {
        patternParser.subscription_return retval = new patternParser.subscription_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal67=null;
        patternParser.field_return f = null;

        patternParser.subscriptionvar_return s = null;


        Object char_literal67_tree=null;
        RewriteRuleTokenStream stream_93=new RewriteRuleTokenStream(adaptor,"token 93");
        RewriteRuleSubtreeStream stream_field=new RewriteRuleSubtreeStream(adaptor,"rule field");
        RewriteRuleSubtreeStream stream_subscriptionvar=new RewriteRuleSubtreeStream(adaptor,"rule subscriptionvar");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:15: (f= field ( '=' s= subscriptionvar )? -> ^( SUBSCRIPTION_TOKEN $f ( $s)? ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:17: f= field ( '=' s= subscriptionvar )?
            {
            pushFollow(FOLLOW_field_in_subscription2168);
            f=field();

            state._fsp--;

            stream_field.add(f.getTree());
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:25: ( '=' s= subscriptionvar )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==93) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:26: '=' s= subscriptionvar
                    {
                    char_literal67=(Token)match(input,93,FOLLOW_93_in_subscription2171);  
                    stream_93.add(char_literal67);

                    pushFollow(FOLLOW_subscriptionvar_in_subscription2175);
                    s=subscriptionvar();

                    state._fsp--;

                    stream_subscriptionvar.add(s.getTree());

                    }
                    break;

            }



            // AST REWRITE
            // elements: s, f
            // token labels: 
            // rule labels: f, retval, s
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

            root_0 = (Object)adaptor.nil();
            // 279:50: -> ^( SUBSCRIPTION_TOKEN $f ( $s)? )
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:53: ^( SUBSCRIPTION_TOKEN $f ( $s)? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(SUBSCRIPTION_TOKEN, "SUBSCRIPTION_TOKEN"), root_1);

                adaptor.addChild(root_1, stream_f.nextTree());
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:279:77: ( $s)?
                if ( stream_s.hasNext() ) {
                    adaptor.addChild(root_1, stream_s.nextTree());

                }
                stream_s.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "subscription"

    public static class subscriptionvar_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subscriptionvar"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:281:1: subscriptionvar : (d= datevar -> $d | dt= datetimevar -> $dt | s= stringvar -> ^( STRING_TOKEN $s) | b= booleanvar -> ^( BOOLEAN_TOKEN $b) | iv= integervar -> ^( INTEGER_TOKEN $iv) | l= longvar -> ^( LONG_TOKEN $l) | f= floatvar -> ^( FLOAT_TOKEN $f) | dv= doublevar -> ^( DOUBLE_TOKEN $dv) | bv= bindvar -> ^( BIND_TOKEN $bv) ) ;
    public final patternParser.subscriptionvar_return subscriptionvar() throws RecognitionException {
        patternParser.subscriptionvar_return retval = new patternParser.subscriptionvar_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.datevar_return d = null;

        patternParser.datetimevar_return dt = null;

        patternParser.stringvar_return s = null;

        patternParser.booleanvar_return b = null;

        patternParser.integervar_return iv = null;

        patternParser.longvar_return l = null;

        patternParser.floatvar_return f = null;

        patternParser.doublevar_return dv = null;

        patternParser.bindvar_return bv = null;


        RewriteRuleSubtreeStream stream_datevar=new RewriteRuleSubtreeStream(adaptor,"rule datevar");
        RewriteRuleSubtreeStream stream_datetimevar=new RewriteRuleSubtreeStream(adaptor,"rule datetimevar");
        RewriteRuleSubtreeStream stream_bindvar=new RewriteRuleSubtreeStream(adaptor,"rule bindvar");
        RewriteRuleSubtreeStream stream_longvar=new RewriteRuleSubtreeStream(adaptor,"rule longvar");
        RewriteRuleSubtreeStream stream_doublevar=new RewriteRuleSubtreeStream(adaptor,"rule doublevar");
        RewriteRuleSubtreeStream stream_booleanvar=new RewriteRuleSubtreeStream(adaptor,"rule booleanvar");
        RewriteRuleSubtreeStream stream_floatvar=new RewriteRuleSubtreeStream(adaptor,"rule floatvar");
        RewriteRuleSubtreeStream stream_stringvar=new RewriteRuleSubtreeStream(adaptor,"rule stringvar");
        RewriteRuleSubtreeStream stream_integervar=new RewriteRuleSubtreeStream(adaptor,"rule integervar");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:281:18: ( (d= datevar -> $d | dt= datetimevar -> $dt | s= stringvar -> ^( STRING_TOKEN $s) | b= booleanvar -> ^( BOOLEAN_TOKEN $b) | iv= integervar -> ^( INTEGER_TOKEN $iv) | l= longvar -> ^( LONG_TOKEN $l) | f= floatvar -> ^( FLOAT_TOKEN $f) | dv= doublevar -> ^( DOUBLE_TOKEN $dv) | bv= bindvar -> ^( BIND_TOKEN $bv) ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:281:20: (d= datevar -> $d | dt= datetimevar -> $dt | s= stringvar -> ^( STRING_TOKEN $s) | b= booleanvar -> ^( BOOLEAN_TOKEN $b) | iv= integervar -> ^( INTEGER_TOKEN $iv) | l= longvar -> ^( LONG_TOKEN $l) | f= floatvar -> ^( FLOAT_TOKEN $f) | dv= doublevar -> ^( DOUBLE_TOKEN $dv) | bv= bindvar -> ^( BIND_TOKEN $bv) )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:281:20: (d= datevar -> $d | dt= datetimevar -> $dt | s= stringvar -> ^( STRING_TOKEN $s) | b= booleanvar -> ^( BOOLEAN_TOKEN $b) | iv= integervar -> ^( INTEGER_TOKEN $iv) | l= longvar -> ^( LONG_TOKEN $l) | f= floatvar -> ^( FLOAT_TOKEN $f) | dv= doublevar -> ^( DOUBLE_TOKEN $dv) | bv= bindvar -> ^( BIND_TOKEN $bv) )
            int alt13=9;
            alt13 = dfa13.predict(input);
            switch (alt13) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:281:21: d= datevar
                    {
                    pushFollow(FOLLOW_datevar_in_subscriptionvar2202);
                    d=datevar();

                    state._fsp--;

                    stream_datevar.add(d.getTree());


                    // AST REWRITE
                    // elements: d
                    // token labels: 
                    // rule labels: retval, d
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_d=new RewriteRuleSubtreeStream(adaptor,"rule d",d!=null?d.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 281:31: -> $d
                    {
                        adaptor.addChild(root_0, stream_d.nextTree());

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:282:9: dt= datetimevar
                    {
                    pushFollow(FOLLOW_datetimevar_in_subscriptionvar2219);
                    dt=datetimevar();

                    state._fsp--;

                    stream_datetimevar.add(dt.getTree());


                    // AST REWRITE
                    // elements: dt
                    // token labels: 
                    // rule labels: dt, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_dt=new RewriteRuleSubtreeStream(adaptor,"rule dt",dt!=null?dt.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 282:24: -> $dt
                    {
                        adaptor.addChild(root_0, stream_dt.nextTree());

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:283:9: s= stringvar
                    {
                    pushFollow(FOLLOW_stringvar_in_subscriptionvar2236);
                    s=stringvar();

                    state._fsp--;

                    stream_stringvar.add(s.getTree());


                    // AST REWRITE
                    // elements: s
                    // token labels: 
                    // rule labels: retval, s
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_s=new RewriteRuleSubtreeStream(adaptor,"rule s",s!=null?s.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 283:21: -> ^( STRING_TOKEN $s)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:283:24: ^( STRING_TOKEN $s)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STRING_TOKEN, "STRING_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_s.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:284:9: b= booleanvar
                    {
                    pushFollow(FOLLOW_booleanvar_in_subscriptionvar2257);
                    b=booleanvar();

                    state._fsp--;

                    stream_booleanvar.add(b.getTree());


                    // AST REWRITE
                    // elements: b
                    // token labels: 
                    // rule labels: retval, b
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_b=new RewriteRuleSubtreeStream(adaptor,"rule b",b!=null?b.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 284:22: -> ^( BOOLEAN_TOKEN $b)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:284:25: ^( BOOLEAN_TOKEN $b)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BOOLEAN_TOKEN, "BOOLEAN_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_b.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 5 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:285:9: iv= integervar
                    {
                    pushFollow(FOLLOW_integervar_in_subscriptionvar2278);
                    iv=integervar();

                    state._fsp--;

                    stream_integervar.add(iv.getTree());


                    // AST REWRITE
                    // elements: iv
                    // token labels: 
                    // rule labels: retval, iv
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_iv=new RewriteRuleSubtreeStream(adaptor,"rule iv",iv!=null?iv.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 285:23: -> ^( INTEGER_TOKEN $iv)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:285:26: ^( INTEGER_TOKEN $iv)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(INTEGER_TOKEN, "INTEGER_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_iv.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 6 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:286:9: l= longvar
                    {
                    pushFollow(FOLLOW_longvar_in_subscriptionvar2299);
                    l=longvar();

                    state._fsp--;

                    stream_longvar.add(l.getTree());


                    // AST REWRITE
                    // elements: l
                    // token labels: 
                    // rule labels: retval, l
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_l=new RewriteRuleSubtreeStream(adaptor,"rule l",l!=null?l.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 286:19: -> ^( LONG_TOKEN $l)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:286:22: ^( LONG_TOKEN $l)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(LONG_TOKEN, "LONG_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_l.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 7 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:287:9: f= floatvar
                    {
                    pushFollow(FOLLOW_floatvar_in_subscriptionvar2320);
                    f=floatvar();

                    state._fsp--;

                    stream_floatvar.add(f.getTree());


                    // AST REWRITE
                    // elements: f
                    // token labels: 
                    // rule labels: f, retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_f=new RewriteRuleSubtreeStream(adaptor,"rule f",f!=null?f.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 287:20: -> ^( FLOAT_TOKEN $f)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:287:23: ^( FLOAT_TOKEN $f)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(FLOAT_TOKEN, "FLOAT_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_f.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 8 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:288:9: dv= doublevar
                    {
                    pushFollow(FOLLOW_doublevar_in_subscriptionvar2341);
                    dv=doublevar();

                    state._fsp--;

                    stream_doublevar.add(dv.getTree());


                    // AST REWRITE
                    // elements: dv
                    // token labels: 
                    // rule labels: retval, dv
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_dv=new RewriteRuleSubtreeStream(adaptor,"rule dv",dv!=null?dv.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 288:22: -> ^( DOUBLE_TOKEN $dv)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:288:25: ^( DOUBLE_TOKEN $dv)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(DOUBLE_TOKEN, "DOUBLE_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_dv.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 9 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:289:9: bv= bindvar
                    {
                    pushFollow(FOLLOW_bindvar_in_subscriptionvar2362);
                    bv=bindvar();

                    state._fsp--;

                    stream_bindvar.add(bv.getTree());


                    // AST REWRITE
                    // elements: bv
                    // token labels: 
                    // rule labels: retval, bv
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_bv=new RewriteRuleSubtreeStream(adaptor,"rule bv",bv!=null?bv.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 289:20: -> ^( BIND_TOKEN $bv)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:289:23: ^( BIND_TOKEN $bv)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BIND_TOKEN, "BIND_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_bv.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "subscriptionvar"

    public static class field_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "field"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:292:1: field : id= identifier '.' fieldname= identifier -> ^( $id $fieldname) ;
    public final patternParser.field_return field() throws RecognitionException {
        patternParser.field_return retval = new patternParser.field_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token char_literal68=null;
        patternParser.identifier_return id = null;

        patternParser.identifier_return fieldname = null;


        Object char_literal68_tree=null;
        RewriteRuleTokenStream stream_94=new RewriteRuleTokenStream(adaptor,"token 94");
        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:292:9: (id= identifier '.' fieldname= identifier -> ^( $id $fieldname) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:292:11: id= identifier '.' fieldname= identifier
            {
            pushFollow(FOLLOW_identifier_in_field2391);
            id=identifier();

            state._fsp--;

            stream_identifier.add(id.getTree());
            char_literal68=(Token)match(input,94,FOLLOW_94_in_field2393);  
            stream_94.add(char_literal68);

            pushFollow(FOLLOW_identifier_in_field2397);
            fieldname=identifier();

            state._fsp--;

            stream_identifier.add(fieldname.getTree());


            // AST REWRITE
            // elements: id, fieldname
            // token labels: 
            // rule labels: id, retval, fieldname
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_id=new RewriteRuleSubtreeStream(adaptor,"rule id",id!=null?id.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_fieldname=new RewriteRuleSubtreeStream(adaptor,"rule fieldname",fieldname!=null?fieldname.tree:null);

            root_0 = (Object)adaptor.nil();
            // 292:50: -> ^( $id $fieldname)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:292:53: ^( $id $fieldname)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_id.nextNode(), root_1);

                adaptor.addChild(root_1, stream_fieldname.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "field"

    public static class starts_with_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "starts_with"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:294:1: starts_with : STARTS WITH p= subpattern -> ^( STARTS_WITH_TOKEN $p) ;
    public final patternParser.starts_with_return starts_with() throws RecognitionException {
        patternParser.starts_with_return retval = new patternParser.starts_with_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token STARTS69=null;
        Token WITH70=null;
        patternParser.subpattern_return p = null;


        Object STARTS69_tree=null;
        Object WITH70_tree=null;
        RewriteRuleTokenStream stream_STARTS=new RewriteRuleTokenStream(adaptor,"token STARTS");
        RewriteRuleTokenStream stream_WITH=new RewriteRuleTokenStream(adaptor,"token WITH");
        RewriteRuleSubtreeStream stream_subpattern=new RewriteRuleSubtreeStream(adaptor,"rule subpattern");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:294:14: ( STARTS WITH p= subpattern -> ^( STARTS_WITH_TOKEN $p) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:294:16: STARTS WITH p= subpattern
            {
            STARTS69=(Token)match(input,STARTS,FOLLOW_STARTS_in_starts_with2416);  
            stream_STARTS.add(STARTS69);

            WITH70=(Token)match(input,WITH,FOLLOW_WITH_in_starts_with2418);  
            stream_WITH.add(WITH70);

            pushFollow(FOLLOW_subpattern_in_starts_with2422);
            p=subpattern();

            state._fsp--;

            stream_subpattern.add(p.getTree());


            // AST REWRITE
            // elements: p
            // token labels: 
            // rule labels: retval, p
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_p=new RewriteRuleSubtreeStream(adaptor,"rule p",p!=null?p.tree:null);

            root_0 = (Object)adaptor.nil();
            // 294:41: -> ^( STARTS_WITH_TOKEN $p)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:294:44: ^( STARTS_WITH_TOKEN $p)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(STARTS_WITH_TOKEN, "STARTS_WITH_TOKEN"), root_1);

                adaptor.addChild(root_1, stream_p.nextTree());

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "starts_with"

    public static class subpattern_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "subpattern"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:1: subpattern : (i= items then_list ) -> ^( $i ( then_list )? ) ;
    public final patternParser.subpattern_return subpattern() throws RecognitionException {
        patternParser.subpattern_return retval = new patternParser.subpattern_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.items_return i = null;

        patternParser.then_list_return then_list71 = null;


        RewriteRuleSubtreeStream stream_items=new RewriteRuleSubtreeStream(adaptor,"rule items");
        RewriteRuleSubtreeStream stream_then_list=new RewriteRuleSubtreeStream(adaptor,"rule then_list");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:13: ( (i= items then_list ) -> ^( $i ( then_list )? ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:15: (i= items then_list )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:15: (i= items then_list )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:16: i= items then_list
            {
            pushFollow(FOLLOW_items_in_subpattern2443);
            i=items();

            state._fsp--;

            stream_items.add(i.getTree());
            pushFollow(FOLLOW_then_list_in_subpattern2445);
            then_list71=then_list();

            state._fsp--;

            stream_then_list.add(then_list71.getTree());

            }



            // AST REWRITE
            // elements: then_list, i
            // token labels: 
            // rule labels: retval, i
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
            RewriteRuleSubtreeStream stream_i=new RewriteRuleSubtreeStream(adaptor,"rule i",i!=null?i.tree:null);

            root_0 = (Object)adaptor.nil();
            // 296:35: -> ^( $i ( then_list )? )
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:38: ^( $i ( then_list )? )
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_i.nextNode(), root_1);

                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:296:43: ( then_list )?
                if ( stream_then_list.hasNext() ) {
                    adaptor.addChild(root_1, stream_then_list.nextTree());

                }
                stream_then_list.reset();

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "subpattern"

    public static class item_list_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "item_list"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:298:1: item_list : LPAREN i1= items ( COMMA i2= items )+ RPAREN ;
    public final patternParser.item_list_return item_list() throws RecognitionException {
        patternParser.item_list_return retval = new patternParser.item_list_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token LPAREN72=null;
        Token COMMA73=null;
        Token RPAREN74=null;
        patternParser.items_return i1 = null;

        patternParser.items_return i2 = null;


        Object LPAREN72_tree=null;
        Object COMMA73_tree=null;
        Object RPAREN74_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:298:12: ( LPAREN i1= items ( COMMA i2= items )+ RPAREN )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:298:14: LPAREN i1= items ( COMMA i2= items )+ RPAREN
            {
            root_0 = (Object)adaptor.nil();

            LPAREN72=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_item_list2471); 
            pushFollow(FOLLOW_items_in_item_list2476);
            i1=items();

            state._fsp--;

            adaptor.addChild(root_0, i1.getTree());
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:298:31: ( COMMA i2= items )+
            int cnt14=0;
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==COMMA) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:298:32: COMMA i2= items
            	    {
            	    COMMA73=(Token)match(input,COMMA,FOLLOW_COMMA_in_item_list2479); 
            	    pushFollow(FOLLOW_items_in_item_list2484);
            	    i2=items();

            	    state._fsp--;

            	    adaptor.addChild(root_0, i2.getTree());

            	    }
            	    break;

            	default :
            	    if ( cnt14 >= 1 ) break loop14;
                        EarlyExitException eee =
                            new EarlyExitException(14, input);
                        throw eee;
                }
                cnt14++;
            } while (true);

            RPAREN74=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_item_list2488); 

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "item_list"

    public static class item_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "item"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:300:1: item : id= identifier -> ^( $id) ;
    public final patternParser.item_return item() throws RecognitionException {
        patternParser.item_return retval = new patternParser.item_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.identifier_return id = null;


        RewriteRuleSubtreeStream stream_identifier=new RewriteRuleSubtreeStream(adaptor,"rule identifier");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:300:8: (id= identifier -> ^( $id) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:300:10: id= identifier
            {
            pushFollow(FOLLOW_identifier_in_item2501);
            id=identifier();

            state._fsp--;

            stream_identifier.add(id.getTree());


            // AST REWRITE
            // elements: id
            // token labels: 
            // rule labels: id, retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_id=new RewriteRuleSubtreeStream(adaptor,"rule id",id!=null?id.tree:null);
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 300:24: -> ^( $id)
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:300:27: ^( $id)
                {
                Object root_1 = (Object)adaptor.nil();
                root_1 = (Object)adaptor.becomeRoot(stream_id.nextNode(), root_1);

                adaptor.addChild(root_0, root_1);
                }

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "item"

    public static class then_list_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "then_list"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:1: then_list : ( then )* -> ( ^( then ) )* ;
    public final patternParser.then_list_return then_list() throws RecognitionException {
        patternParser.then_list_return retval = new patternParser.then_list_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.then_return then75 = null;


        RewriteRuleSubtreeStream stream_then=new RewriteRuleSubtreeStream(adaptor,"rule then");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:12: ( ( then )* -> ( ^( then ) )* )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:14: ( then )*
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:14: ( then )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==THEN) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:15: then
            	    {
            	    pushFollow(FOLLOW_then_in_then_list2518);
            	    then75=then();

            	    state._fsp--;

            	    stream_then.add(then75.getTree());

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);



            // AST REWRITE
            // elements: then
            // token labels: 
            // rule labels: retval
            // token list labels: 
            // rule list labels: 
            // wildcard labels: 
            retval.tree = root_0;
            RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

            root_0 = (Object)adaptor.nil();
            // 302:22: -> ( ^( then ) )*
            {
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:25: ( ^( then ) )*
                while ( stream_then.hasNext() ) {
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:302:25: ^( then )
                    {
                    Object root_1 = (Object)adaptor.nil();
                    root_1 = (Object)adaptor.becomeRoot(stream_then.nextNode(), root_1);

                    adaptor.addChild(root_0, root_1);
                    }

                }
                stream_then.reset();

            }

            retval.tree = root_0;
            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "then_list"

    public static class then_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "then"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:304:1: then : ( THEN i= items -> ^( THEN_TOKEN $i) | THEN WITHIN timeunit= time i= items -> ^( THEN_WITHIN_TOKEN $timeunit $i) | THEN DURING timeunit= time i= items -> ^( THEN_DURING_TOKEN $timeunit $i) | THEN AFTER timeunit= time -> ^( THEN_AFTER_TOKEN $timeunit) ) ;
    public final patternParser.then_return then() throws RecognitionException {
        patternParser.then_return retval = new patternParser.then_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token THEN76=null;
        Token THEN77=null;
        Token WITHIN78=null;
        Token THEN79=null;
        Token DURING80=null;
        Token THEN81=null;
        Token AFTER82=null;
        patternParser.items_return i = null;

        patternParser.time_return timeunit = null;


        Object THEN76_tree=null;
        Object THEN77_tree=null;
        Object WITHIN78_tree=null;
        Object THEN79_tree=null;
        Object DURING80_tree=null;
        Object THEN81_tree=null;
        Object AFTER82_tree=null;
        RewriteRuleTokenStream stream_WITHIN=new RewriteRuleTokenStream(adaptor,"token WITHIN");
        RewriteRuleTokenStream stream_THEN=new RewriteRuleTokenStream(adaptor,"token THEN");
        RewriteRuleTokenStream stream_DURING=new RewriteRuleTokenStream(adaptor,"token DURING");
        RewriteRuleTokenStream stream_AFTER=new RewriteRuleTokenStream(adaptor,"token AFTER");
        RewriteRuleSubtreeStream stream_time=new RewriteRuleSubtreeStream(adaptor,"rule time");
        RewriteRuleSubtreeStream stream_items=new RewriteRuleSubtreeStream(adaptor,"rule items");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:304:8: ( ( THEN i= items -> ^( THEN_TOKEN $i) | THEN WITHIN timeunit= time i= items -> ^( THEN_WITHIN_TOKEN $timeunit $i) | THEN DURING timeunit= time i= items -> ^( THEN_DURING_TOKEN $timeunit $i) | THEN AFTER timeunit= time -> ^( THEN_AFTER_TOKEN $timeunit) ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:304:10: ( THEN i= items -> ^( THEN_TOKEN $i) | THEN WITHIN timeunit= time i= items -> ^( THEN_WITHIN_TOKEN $timeunit $i) | THEN DURING timeunit= time i= items -> ^( THEN_DURING_TOKEN $timeunit $i) | THEN AFTER timeunit= time -> ^( THEN_AFTER_TOKEN $timeunit) )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:304:10: ( THEN i= items -> ^( THEN_TOKEN $i) | THEN WITHIN timeunit= time i= items -> ^( THEN_WITHIN_TOKEN $timeunit $i) | THEN DURING timeunit= time i= items -> ^( THEN_DURING_TOKEN $timeunit $i) | THEN AFTER timeunit= time -> ^( THEN_AFTER_TOKEN $timeunit) )
            int alt16=4;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==THEN) ) {
                switch ( input.LA(2) ) {
                case WITHIN:
                    {
                    alt16=2;
                    }
                    break;
                case DURING:
                    {
                    alt16=3;
                    }
                    break;
                case AFTER:
                    {
                    alt16=4;
                    }
                    break;
                case ANY:
                case ALL:
                case REPEAT:
                case ESCAPE_KEYWORD:
                case LPAREN:
                case SIGNED:
                case UNSIGNED:
                case IDENTIFIER:
                    {
                    alt16=1;
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 16, 1, input);

                    throw nvae;
                }

            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:304:11: THEN i= items
                    {
                    THEN76=(Token)match(input,THEN,FOLLOW_THEN_in_then2538);  
                    stream_THEN.add(THEN76);

                    pushFollow(FOLLOW_items_in_then2542);
                    i=items();

                    state._fsp--;

                    stream_items.add(i.getTree());


                    // AST REWRITE
                    // elements: i
                    // token labels: 
                    // rule labels: retval, i
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_i=new RewriteRuleSubtreeStream(adaptor,"rule i",i!=null?i.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 304:24: -> ^( THEN_TOKEN $i)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:304:27: ^( THEN_TOKEN $i)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(THEN_TOKEN, "THEN_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_i.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:305:9: THEN WITHIN timeunit= time i= items
                    {
                    THEN77=(Token)match(input,THEN,FOLLOW_THEN_in_then2561);  
                    stream_THEN.add(THEN77);

                    WITHIN78=(Token)match(input,WITHIN,FOLLOW_WITHIN_in_then2563);  
                    stream_WITHIN.add(WITHIN78);

                    pushFollow(FOLLOW_time_in_then2567);
                    timeunit=time();

                    state._fsp--;

                    stream_time.add(timeunit.getTree());
                    pushFollow(FOLLOW_items_in_then2571);
                    i=items();

                    state._fsp--;

                    stream_items.add(i.getTree());


                    // AST REWRITE
                    // elements: i, timeunit
                    // token labels: 
                    // rule labels: retval, timeunit, i
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_timeunit=new RewriteRuleSubtreeStream(adaptor,"rule timeunit",timeunit!=null?timeunit.tree:null);
                    RewriteRuleSubtreeStream stream_i=new RewriteRuleSubtreeStream(adaptor,"rule i",i!=null?i.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 305:43: -> ^( THEN_WITHIN_TOKEN $timeunit $i)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:305:46: ^( THEN_WITHIN_TOKEN $timeunit $i)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(THEN_WITHIN_TOKEN, "THEN_WITHIN_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_timeunit.nextTree());
                        adaptor.addChild(root_1, stream_i.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:306:9: THEN DURING timeunit= time i= items
                    {
                    THEN79=(Token)match(input,THEN,FOLLOW_THEN_in_then2593);  
                    stream_THEN.add(THEN79);

                    DURING80=(Token)match(input,DURING,FOLLOW_DURING_in_then2595);  
                    stream_DURING.add(DURING80);

                    pushFollow(FOLLOW_time_in_then2599);
                    timeunit=time();

                    state._fsp--;

                    stream_time.add(timeunit.getTree());
                    pushFollow(FOLLOW_items_in_then2603);
                    i=items();

                    state._fsp--;

                    stream_items.add(i.getTree());


                    // AST REWRITE
                    // elements: timeunit, i
                    // token labels: 
                    // rule labels: retval, timeunit, i
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_timeunit=new RewriteRuleSubtreeStream(adaptor,"rule timeunit",timeunit!=null?timeunit.tree:null);
                    RewriteRuleSubtreeStream stream_i=new RewriteRuleSubtreeStream(adaptor,"rule i",i!=null?i.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 306:43: -> ^( THEN_DURING_TOKEN $timeunit $i)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:306:46: ^( THEN_DURING_TOKEN $timeunit $i)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(THEN_DURING_TOKEN, "THEN_DURING_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_timeunit.nextTree());
                        adaptor.addChild(root_1, stream_i.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:307:9: THEN AFTER timeunit= time
                    {
                    THEN81=(Token)match(input,THEN,FOLLOW_THEN_in_then2625);  
                    stream_THEN.add(THEN81);

                    AFTER82=(Token)match(input,AFTER,FOLLOW_AFTER_in_then2627);  
                    stream_AFTER.add(AFTER82);

                    pushFollow(FOLLOW_time_in_then2631);
                    timeunit=time();

                    state._fsp--;

                    stream_time.add(timeunit.getTree());


                    // AST REWRITE
                    // elements: timeunit
                    // token labels: 
                    // rule labels: retval, timeunit
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_timeunit=new RewriteRuleSubtreeStream(adaptor,"rule timeunit",timeunit!=null?timeunit.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 307:34: -> ^( THEN_AFTER_TOKEN $timeunit)
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:307:37: ^( THEN_AFTER_TOKEN $timeunit)
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(THEN_AFTER_TOKEN, "THEN_AFTER_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_timeunit.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "then"

    public static class repeat_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "repeat"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:310:1: repeat : (minval= value TO maxval= value TIMES items -> ^( REPEAT_TOKEN $minval $maxval items ) | times= value TIMES items -> ^( REPEAT_TOKEN $times items ) );
    public final patternParser.repeat_return repeat() throws RecognitionException {
        patternParser.repeat_return retval = new patternParser.repeat_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token TO83=null;
        Token TIMES84=null;
        Token TIMES86=null;
        patternParser.value_return minval = null;

        patternParser.value_return maxval = null;

        patternParser.value_return times = null;

        patternParser.items_return items85 = null;

        patternParser.items_return items87 = null;


        Object TO83_tree=null;
        Object TIMES84_tree=null;
        Object TIMES86_tree=null;
        RewriteRuleTokenStream stream_TO=new RewriteRuleTokenStream(adaptor,"token TO");
        RewriteRuleTokenStream stream_TIMES=new RewriteRuleTokenStream(adaptor,"token TIMES");
        RewriteRuleSubtreeStream stream_items=new RewriteRuleSubtreeStream(adaptor,"rule items");
        RewriteRuleSubtreeStream stream_value=new RewriteRuleSubtreeStream(adaptor,"rule value");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:310:10: (minval= value TO maxval= value TIMES items -> ^( REPEAT_TOKEN $minval $maxval items ) | times= value TIMES items -> ^( REPEAT_TOKEN $times items ) )
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==BIND_SYMBOL) ) {
                switch ( input.LA(2) ) {
                case ESCAPE_KEYWORD:
                    {
                    int LA17_3 = input.LA(3);

                    if ( ((LA17_3>=DEFINE && LA17_3<=NULL)) ) {
                        int LA17_8 = input.LA(4);

                        if ( (LA17_8==TO) ) {
                            alt17=1;
                        }
                        else if ( (LA17_8==TIMES) ) {
                            alt17=2;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 17, 8, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 3, input);

                        throw nvae;
                    }
                    }
                    break;
                case SIGNED:
                case UNSIGNED:
                    {
                    int LA17_4 = input.LA(3);

                    if ( (LA17_4==TO) ) {
                        alt17=1;
                    }
                    else if ( (LA17_4==TIMES) ) {
                        alt17=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 4, input);

                        throw nvae;
                    }
                    }
                    break;
                case IDENTIFIER:
                    {
                    int LA17_5 = input.LA(3);

                    if ( (LA17_5==TIMES) ) {
                        alt17=2;
                    }
                    else if ( (LA17_5==TO) ) {
                        alt17=1;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 17, 5, input);

                        throw nvae;
                    }
                    }
                    break;
                default:
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA17_0==UNSIGNED) ) {
                int LA17_2 = input.LA(2);

                if ( (LA17_2==TIMES) ) {
                    alt17=2;
                }
                else if ( (LA17_2==TO) ) {
                    alt17=1;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 17, 2, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 17, 0, input);

                throw nvae;
            }
            switch (alt17) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:310:12: minval= value TO maxval= value TIMES items
                    {
                    pushFollow(FOLLOW_value_in_repeat2660);
                    minval=value();

                    state._fsp--;

                    stream_value.add(minval.getTree());
                    TO83=(Token)match(input,TO,FOLLOW_TO_in_repeat2662);  
                    stream_TO.add(TO83);

                    pushFollow(FOLLOW_value_in_repeat2666);
                    maxval=value();

                    state._fsp--;

                    stream_value.add(maxval.getTree());
                    TIMES84=(Token)match(input,TIMES,FOLLOW_TIMES_in_repeat2668);  
                    stream_TIMES.add(TIMES84);

                    pushFollow(FOLLOW_items_in_repeat2670);
                    items85=items();

                    state._fsp--;

                    stream_items.add(items85.getTree());


                    // AST REWRITE
                    // elements: minval, maxval, items
                    // token labels: 
                    // rule labels: minval, retval, maxval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_minval=new RewriteRuleSubtreeStream(adaptor,"rule minval",minval!=null?minval.tree:null);
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_maxval=new RewriteRuleSubtreeStream(adaptor,"rule maxval",maxval!=null?maxval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 310:53: -> ^( REPEAT_TOKEN $minval $maxval items )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:310:56: ^( REPEAT_TOKEN $minval $maxval items )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(REPEAT_TOKEN, "REPEAT_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_minval.nextTree());
                        adaptor.addChild(root_1, stream_maxval.nextTree());
                        adaptor.addChild(root_1, stream_items.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:311:9: times= value TIMES items
                    {
                    pushFollow(FOLLOW_value_in_repeat2696);
                    times=value();

                    state._fsp--;

                    stream_value.add(times.getTree());
                    TIMES86=(Token)match(input,TIMES,FOLLOW_TIMES_in_repeat2698);  
                    stream_TIMES.add(TIMES86);

                    pushFollow(FOLLOW_items_in_repeat2700);
                    items87=items();

                    state._fsp--;

                    stream_items.add(items87.getTree());


                    // AST REWRITE
                    // elements: items, times
                    // token labels: 
                    // rule labels: retval, times
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);
                    RewriteRuleSubtreeStream stream_times=new RewriteRuleSubtreeStream(adaptor,"rule times",times!=null?times.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 311:33: -> ^( REPEAT_TOKEN $times items )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:311:36: ^( REPEAT_TOKEN $times items )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(REPEAT_TOKEN, "REPEAT_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_times.nextTree());
                        adaptor.addChild(root_1, stream_items.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }
            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "repeat"

    public static class items_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "items"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:313:1: items : ( item -> ^( item ) | ANY ONE item_list -> ^( ANY_ONE_TOKEN item_list ) | ALL item_list -> ^( ALL_TOKEN item_list ) | REPEAT repeat -> ^( THEN_REPEAT_TOKEN repeat ) | LPAREN subpattern RPAREN -> ^( subpattern ) ) ;
    public final patternParser.items_return items() throws RecognitionException {
        patternParser.items_return retval = new patternParser.items_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token ANY89=null;
        Token ONE90=null;
        Token ALL92=null;
        Token REPEAT94=null;
        Token LPAREN96=null;
        Token RPAREN98=null;
        patternParser.item_return item88 = null;

        patternParser.item_list_return item_list91 = null;

        patternParser.item_list_return item_list93 = null;

        patternParser.repeat_return repeat95 = null;

        patternParser.subpattern_return subpattern97 = null;


        Object ANY89_tree=null;
        Object ONE90_tree=null;
        Object ALL92_tree=null;
        Object REPEAT94_tree=null;
        Object LPAREN96_tree=null;
        Object RPAREN98_tree=null;
        RewriteRuleTokenStream stream_RPAREN=new RewriteRuleTokenStream(adaptor,"token RPAREN");
        RewriteRuleTokenStream stream_ANY=new RewriteRuleTokenStream(adaptor,"token ANY");
        RewriteRuleTokenStream stream_ONE=new RewriteRuleTokenStream(adaptor,"token ONE");
        RewriteRuleTokenStream stream_REPEAT=new RewriteRuleTokenStream(adaptor,"token REPEAT");
        RewriteRuleTokenStream stream_ALL=new RewriteRuleTokenStream(adaptor,"token ALL");
        RewriteRuleTokenStream stream_LPAREN=new RewriteRuleTokenStream(adaptor,"token LPAREN");
        RewriteRuleSubtreeStream stream_item_list=new RewriteRuleSubtreeStream(adaptor,"rule item_list");
        RewriteRuleSubtreeStream stream_item=new RewriteRuleSubtreeStream(adaptor,"rule item");
        RewriteRuleSubtreeStream stream_repeat=new RewriteRuleSubtreeStream(adaptor,"rule repeat");
        RewriteRuleSubtreeStream stream_subpattern=new RewriteRuleSubtreeStream(adaptor,"rule subpattern");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:313:9: ( ( item -> ^( item ) | ANY ONE item_list -> ^( ANY_ONE_TOKEN item_list ) | ALL item_list -> ^( ALL_TOKEN item_list ) | REPEAT repeat -> ^( THEN_REPEAT_TOKEN repeat ) | LPAREN subpattern RPAREN -> ^( subpattern ) ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:313:11: ( item -> ^( item ) | ANY ONE item_list -> ^( ANY_ONE_TOKEN item_list ) | ALL item_list -> ^( ALL_TOKEN item_list ) | REPEAT repeat -> ^( THEN_REPEAT_TOKEN repeat ) | LPAREN subpattern RPAREN -> ^( subpattern ) )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:313:11: ( item -> ^( item ) | ANY ONE item_list -> ^( ANY_ONE_TOKEN item_list ) | ALL item_list -> ^( ALL_TOKEN item_list ) | REPEAT repeat -> ^( THEN_REPEAT_TOKEN repeat ) | LPAREN subpattern RPAREN -> ^( subpattern ) )
            int alt18=5;
            switch ( input.LA(1) ) {
            case ESCAPE_KEYWORD:
            case SIGNED:
            case UNSIGNED:
            case IDENTIFIER:
                {
                alt18=1;
                }
                break;
            case ANY:
                {
                alt18=2;
                }
                break;
            case ALL:
                {
                alt18=3;
                }
                break;
            case REPEAT:
                {
                alt18=4;
                }
                break;
            case LPAREN:
                {
                alt18=5;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 18, 0, input);

                throw nvae;
            }

            switch (alt18) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:313:12: item
                    {
                    pushFollow(FOLLOW_item_in_items2722);
                    item88=item();

                    state._fsp--;

                    stream_item.add(item88.getTree());


                    // AST REWRITE
                    // elements: item
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 313:17: -> ^( item )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:313:20: ^( item )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_item.nextNode(), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:314:9: ANY ONE item_list
                    {
                    ANY89=(Token)match(input,ANY,FOLLOW_ANY_in_items2738);  
                    stream_ANY.add(ANY89);

                    ONE90=(Token)match(input,ONE,FOLLOW_ONE_in_items2740);  
                    stream_ONE.add(ONE90);

                    pushFollow(FOLLOW_item_list_in_items2742);
                    item_list91=item_list();

                    state._fsp--;

                    stream_item_list.add(item_list91.getTree());


                    // AST REWRITE
                    // elements: item_list
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 314:27: -> ^( ANY_ONE_TOKEN item_list )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:314:30: ^( ANY_ONE_TOKEN item_list )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ANY_ONE_TOKEN, "ANY_ONE_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_item_list.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 3 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:315:9: ALL item_list
                    {
                    ALL92=(Token)match(input,ALL,FOLLOW_ALL_in_items2760);  
                    stream_ALL.add(ALL92);

                    pushFollow(FOLLOW_item_list_in_items2762);
                    item_list93=item_list();

                    state._fsp--;

                    stream_item_list.add(item_list93.getTree());


                    // AST REWRITE
                    // elements: item_list
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 315:23: -> ^( ALL_TOKEN item_list )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:315:26: ^( ALL_TOKEN item_list )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(ALL_TOKEN, "ALL_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_item_list.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 4 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:316:9: REPEAT repeat
                    {
                    REPEAT94=(Token)match(input,REPEAT,FOLLOW_REPEAT_in_items2780);  
                    stream_REPEAT.add(REPEAT94);

                    pushFollow(FOLLOW_repeat_in_items2782);
                    repeat95=repeat();

                    state._fsp--;

                    stream_repeat.add(repeat95.getTree());


                    // AST REWRITE
                    // elements: repeat
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 316:23: -> ^( THEN_REPEAT_TOKEN repeat )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:316:26: ^( THEN_REPEAT_TOKEN repeat )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(THEN_REPEAT_TOKEN, "THEN_REPEAT_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_repeat.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 5 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:317:9: LPAREN subpattern RPAREN
                    {
                    LPAREN96=(Token)match(input,LPAREN,FOLLOW_LPAREN_in_items2800);  
                    stream_LPAREN.add(LPAREN96);

                    pushFollow(FOLLOW_subpattern_in_items2802);
                    subpattern97=subpattern();

                    state._fsp--;

                    stream_subpattern.add(subpattern97.getTree());
                    RPAREN98=(Token)match(input,RPAREN,FOLLOW_RPAREN_in_items2804);  
                    stream_RPAREN.add(RPAREN98);



                    // AST REWRITE
                    // elements: subpattern
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 317:34: -> ^( subpattern )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:317:37: ^( subpattern )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot(stream_subpattern.nextNode(), root_1);

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "items"

    public static class value_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "value"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:320:1: value : ( bindvar -> ^( BIND_TOKEN bindvar ) | unsignedintegervar -> ^( INTEGER_TOKEN unsignedintegervar ) ) ;
    public final patternParser.value_return value() throws RecognitionException {
        patternParser.value_return retval = new patternParser.value_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.bindvar_return bindvar99 = null;

        patternParser.unsignedintegervar_return unsignedintegervar100 = null;


        RewriteRuleSubtreeStream stream_unsignedintegervar=new RewriteRuleSubtreeStream(adaptor,"rule unsignedintegervar");
        RewriteRuleSubtreeStream stream_bindvar=new RewriteRuleSubtreeStream(adaptor,"rule bindvar");
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:320:9: ( ( bindvar -> ^( BIND_TOKEN bindvar ) | unsignedintegervar -> ^( INTEGER_TOKEN unsignedintegervar ) ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:320:11: ( bindvar -> ^( BIND_TOKEN bindvar ) | unsignedintegervar -> ^( INTEGER_TOKEN unsignedintegervar ) )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:320:11: ( bindvar -> ^( BIND_TOKEN bindvar ) | unsignedintegervar -> ^( INTEGER_TOKEN unsignedintegervar ) )
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==BIND_SYMBOL) ) {
                alt19=1;
            }
            else if ( (LA19_0==UNSIGNED) ) {
                alt19=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 19, 0, input);

                throw nvae;
            }
            switch (alt19) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:320:12: bindvar
                    {
                    pushFollow(FOLLOW_bindvar_in_value2835);
                    bindvar99=bindvar();

                    state._fsp--;

                    stream_bindvar.add(bindvar99.getTree());


                    // AST REWRITE
                    // elements: bindvar
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 320:20: -> ^( BIND_TOKEN bindvar )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:320:23: ^( BIND_TOKEN bindvar )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(BIND_TOKEN, "BIND_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_bindvar.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:321:9: unsignedintegervar
                    {
                    pushFollow(FOLLOW_unsignedintegervar_in_value2853);
                    unsignedintegervar100=unsignedintegervar();

                    state._fsp--;

                    stream_unsignedintegervar.add(unsignedintegervar100.getTree());


                    // AST REWRITE
                    // elements: unsignedintegervar
                    // token labels: 
                    // rule labels: retval
                    // token list labels: 
                    // rule list labels: 
                    // wildcard labels: 
                    retval.tree = root_0;
                    RewriteRuleSubtreeStream stream_retval=new RewriteRuleSubtreeStream(adaptor,"rule retval",retval!=null?retval.tree:null);

                    root_0 = (Object)adaptor.nil();
                    // 321:28: -> ^( INTEGER_TOKEN unsignedintegervar )
                    {
                        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:321:31: ^( INTEGER_TOKEN unsignedintegervar )
                        {
                        Object root_1 = (Object)adaptor.nil();
                        root_1 = (Object)adaptor.becomeRoot((Object)adaptor.create(INTEGER_TOKEN, "INTEGER_TOKEN"), root_1);

                        adaptor.addChild(root_1, stream_unsignedintegervar.nextTree());

                        adaptor.addChild(root_0, root_1);
                        }

                    }

                    retval.tree = root_0;
                    }
                    break;

            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "value"

    public static class time_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "time"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:324:1: time : ( bindvar | unsignedintegervar ) timeunittype ;
    public final patternParser.time_return time() throws RecognitionException {
        patternParser.time_return retval = new patternParser.time_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        patternParser.bindvar_return bindvar101 = null;

        patternParser.unsignedintegervar_return unsignedintegervar102 = null;

        patternParser.timeunittype_return timeunittype103 = null;



        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:324:8: ( ( bindvar | unsignedintegervar ) timeunittype )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:324:10: ( bindvar | unsignedintegervar ) timeunittype
            {
            root_0 = (Object)adaptor.nil();

            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:324:10: ( bindvar | unsignedintegervar )
            int alt20=2;
            int LA20_0 = input.LA(1);

            if ( (LA20_0==BIND_SYMBOL) ) {
                alt20=1;
            }
            else if ( (LA20_0==UNSIGNED) ) {
                alt20=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 20, 0, input);

                throw nvae;
            }
            switch (alt20) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:324:11: bindvar
                    {
                    pushFollow(FOLLOW_bindvar_in_time2885);
                    bindvar101=bindvar();

                    state._fsp--;

                    adaptor.addChild(root_0, bindvar101.getTree());

                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:324:21: unsignedintegervar
                    {
                    pushFollow(FOLLOW_unsignedintegervar_in_time2889);
                    unsignedintegervar102=unsignedintegervar();

                    state._fsp--;

                    adaptor.addChild(root_0, unsignedintegervar102.getTree());

                    }
                    break;

            }

            pushFollow(FOLLOW_timeunittype_in_time2892);
            timeunittype103=timeunittype();

            state._fsp--;

            adaptor.addChild(root_0, timeunittype103.getTree());

            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "time"

    public static class timeunittype_return extends ParserRuleReturnScope {
        Object tree;
        public Object getTree() { return tree; }
    };

    // $ANTLR start "timeunittype"
    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:326:1: timeunittype : ( DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS );
    public final patternParser.timeunittype_return timeunittype() throws RecognitionException {
        patternParser.timeunittype_return retval = new patternParser.timeunittype_return();
        retval.start = input.LT(1);

        Object root_0 = null;

        Token set104=null;

        Object set104_tree=null;

        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:326:16: ( DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            root_0 = (Object)adaptor.nil();

            set104=(Token)input.LT(1);
            if ( (input.LA(1)>=DAYS && input.LA(1)<=MILLISECONDS) ) {
                input.consume();
                adaptor.addChild(root_0, (Object)adaptor.create(set104));
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

            retval.tree = (Object)adaptor.rulePostProcessing(root_0);
            adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

        }


        	catch(RecognitionException e2) { 
        		handler.addException(createException(input));
        		throw e2;
        	} catch(Exception e3) { 
        		//ignore only rewrite empty stream exception
        		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
        			e3 instanceof LanguageException) ) {
        			LanguageException le = new LanguageException(e3);
        			handler.addException(le);
        		}
        	}
        finally {
        }
        return retval;
    }
    // $ANTLR end "timeunittype"

    // Delegated rules


    protected DFA2 dfa2 = new DFA2(this);
    protected DFA4 dfa4 = new DFA4(this);
    protected DFA13 dfa13 = new DFA13(this);
    static final String DFA2_eotS =
        "\12\uffff";
    static final String DFA2_eofS =
        "\12\uffff";
    static final String DFA2_minS =
        "\1\133\1\121\1\103\1\121\1\103\1\121\1\103\1\100\2\uffff";
    static final String DFA2_maxS =
        "\1\133\1\121\1\103\1\121\1\103\1\121\1\106\1\130\2\uffff";
    static final String DFA2_acceptS =
        "\10\uffff\1\1\1\2";
    static final String DFA2_specialS =
        "\12\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7\2\uffff\1\10",
            "\1\11\27\uffff\1\10",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "236:1: datevar : ( '$date(' d11= year COMMA d12= month COMMA d13= day ( COMMA d14= stringvar )? ')' -> ^( DATE_TOKEN $d11 $d12 $d13 ( $d14)? ) | '$date(' d21= year COMMA d22= month COMMA d23= day COMMA d24= NULL ')' -> ^( DATE_TOKEN $d21 $d22 $d23) );";
        }
    }
    static final String DFA4_eotS =
        "\22\uffff";
    static final String DFA4_eofS =
        "\22\uffff";
    static final String DFA4_minS =
        "\1\134\1\121\1\103\1\121\1\103\1\121\1\103\1\121\1\103\1\121\1"+
        "\103\1\121\1\103\1\121\1\103\1\100\2\uffff";
    static final String DFA4_maxS =
        "\1\134\1\121\1\103\1\121\1\103\1\121\1\103\1\121\1\103\1\121\1"+
        "\103\1\121\1\103\1\121\1\106\1\130\2\uffff";
    static final String DFA4_acceptS =
        "\20\uffff\1\1\1\2";
    static final String DFA4_specialS =
        "\22\uffff}>";
    static final String[] DFA4_transitionS = {
            "\1\1",
            "\1\2",
            "\1\3",
            "\1\4",
            "\1\5",
            "\1\6",
            "\1\7",
            "\1\10",
            "\1\11",
            "\1\12",
            "\1\13",
            "\1\14",
            "\1\15",
            "\1\16",
            "\1\17\2\uffff\1\20",
            "\1\21\27\uffff\1\20",
            "",
            ""
    };

    static final short[] DFA4_eot = DFA.unpackEncodedString(DFA4_eotS);
    static final short[] DFA4_eof = DFA.unpackEncodedString(DFA4_eofS);
    static final char[] DFA4_min = DFA.unpackEncodedStringToUnsignedChars(DFA4_minS);
    static final char[] DFA4_max = DFA.unpackEncodedStringToUnsignedChars(DFA4_maxS);
    static final short[] DFA4_accept = DFA.unpackEncodedString(DFA4_acceptS);
    static final short[] DFA4_special = DFA.unpackEncodedString(DFA4_specialS);
    static final short[][] DFA4_transition;

    static {
        int numStates = DFA4_transitionS.length;
        DFA4_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA4_transition[i] = DFA.unpackEncodedString(DFA4_transitionS[i]);
        }
    }

    class DFA4 extends DFA {

        public DFA4(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 4;
            this.eot = DFA4_eot;
            this.eof = DFA4_eof;
            this.min = DFA4_min;
            this.max = DFA4_max;
            this.accept = DFA4_accept;
            this.special = DFA4_special;
            this.transition = DFA4_transition;
        }
        public String getDescription() {
            return "239:1: datetimevar : ( '$datetime(' d11= year COMMA d12= month COMMA d13= day COMMA d14= hour COMMA d15= minute COMMA d16= second COMMA d17= millisecond ( COMMA d18= stringvar )? ')' -> ^( DATETIME_TOKEN $d11 $d12 $d13 $d14 $d15 $d16 $d17 ( $d18)? ) | '$datetime(' d21= year COMMA d22= month COMMA d23= day COMMA d24= hour COMMA d25= minute COMMA d26= second COMMA d27= millisecond COMMA d28= NULL ')' -> ^( DATETIME_TOKEN $d21 $d22 $d23 $d24 $d25 $d26 $d27) );";
        }
    }
    static final String DFA13_eotS =
        "\12\uffff";
    static final String DFA13_eofS =
        "\12\uffff";
    static final String DFA13_minS =
        "\1\76\11\uffff";
    static final String DFA13_maxS =
        "\1\134\11\uffff";
    static final String DFA13_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11";
    static final String DFA13_specialS =
        "\12\uffff}>";
    static final String[] DFA13_transitionS = {
            "\2\4\11\uffff\1\11\6\uffff\2\5\1\6\1\7\1\10\3\uffff\1\3\2\uffff"+
            "\1\1\1\2",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA13_eot = DFA.unpackEncodedString(DFA13_eotS);
    static final short[] DFA13_eof = DFA.unpackEncodedString(DFA13_eofS);
    static final char[] DFA13_min = DFA.unpackEncodedStringToUnsignedChars(DFA13_minS);
    static final char[] DFA13_max = DFA.unpackEncodedStringToUnsignedChars(DFA13_maxS);
    static final short[] DFA13_accept = DFA.unpackEncodedString(DFA13_acceptS);
    static final short[] DFA13_special = DFA.unpackEncodedString(DFA13_specialS);
    static final short[][] DFA13_transition;

    static {
        int numStates = DFA13_transitionS.length;
        DFA13_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA13_transition[i] = DFA.unpackEncodedString(DFA13_transitionS[i]);
        }
    }

    class DFA13 extends DFA {

        public DFA13(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 13;
            this.eot = DFA13_eot;
            this.eof = DFA13_eof;
            this.min = DFA13_min;
            this.max = DFA13_max;
            this.accept = DFA13_accept;
            this.special = DFA13_special;
            this.transition = DFA13_transition;
        }
        public String getDescription() {
            return "281:20: (d= datevar -> $d | dt= datetimevar -> $dt | s= stringvar -> ^( STRING_TOKEN $s) | b= booleanvar -> ^( BOOLEAN_TOKEN $b) | iv= integervar -> ^( INTEGER_TOKEN $iv) | l= longvar -> ^( LONG_TOKEN $l) | f= floatvar -> ^( FLOAT_TOKEN $f) | dv= doublevar -> ^( DOUBLE_TOKEN $dv) | bv= bindvar -> ^( BIND_TOKEN $bv) )";
        }
    }
 

    public static final BitSet FOLLOW_set_in_digits0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_year1386 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_month1395 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_day1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_hour1413 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_minute1422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_second1431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_millisecond1439 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_keywords0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ESCAPE_KEYWORD_in_escaped_keyword1519 = new BitSet(new long[]{0xFFFFFFF000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_keywords_in_escaped_keyword1521 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BIND_SYMBOL_in_bindvar1536 = new BitSet(new long[]{0x0000000000000000L,0x0000000000830004L});
    public static final BitSet FOLLOW_identifier_in_bindvar1538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_datevar1549 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_year_in_datevar1553 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datevar1555 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_month_in_datevar1559 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datevar1561 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_day_in_datevar1565 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_COMMA_in_datevar1568 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_stringvar_in_datevar1572 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_RPAREN_in_datevar1576 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_91_in_datevar1605 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_year_in_datevar1609 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datevar1611 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_month_in_datevar1615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datevar1617 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_day_in_datevar1621 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datevar1623 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_NULL_in_datevar1627 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_RPAREN_in_datevar1629 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_datetimevar1653 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_year_in_datetimevar1657 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1659 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_month_in_datetimevar1663 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1665 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_day_in_datetimevar1669 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1671 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_hour_in_datetimevar1675 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1677 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_minute_in_datetimevar1681 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1683 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_second_in_datetimevar1687 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1690 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_millisecond_in_datetimevar1699 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1702 = new BitSet(new long[]{0x0000000000000000L,0x0000000001000000L});
    public static final BitSet FOLLOW_stringvar_in_datetimevar1706 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_RPAREN_in_datetimevar1710 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_92_in_datetimevar1750 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_year_in_datetimevar1754 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1756 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_month_in_datetimevar1760 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1762 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_day_in_datetimevar1766 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1768 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_hour_in_datetimevar1772 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1774 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_minute_in_datetimevar1778 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1780 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_second_in_datetimevar1784 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1787 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_millisecond_in_datetimevar1798 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_datetimevar1800 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_NULL_in_datetimevar1804 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_RPAREN_in_datetimevar1806 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_stringvar1842 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_booleanvar0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_integervar0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_UNSIGNED_in_unsignedintegervar1876 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LONG_in_longvar1886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FLOAT_in_floatvar1895 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOUBLE_in_doublevar1905 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_escaped_keyword_in_identifier1914 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_digits_in_identifier1918 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifier1922 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_query_in_main1932 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_main1934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EOF_in_main1947 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_define_pattern_in_query1961 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFINE_in_define_pattern1976 = new BitSet(new long[]{0x0000002000000000L});
    public static final BitSet FOLLOW_PATTERN_in_define_pattern1978 = new BitSet(new long[]{0x0000000000000000L,0x0000000001830004L});
    public static final BitSet FOLLOW_patternname_in_define_pattern1982 = new BitSet(new long[]{0x0000004000000000L});
    public static final BitSet FOLLOW_using_in_define_pattern1986 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NAME_in_patternname2012 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_patternname2016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_USING_in_using2026 = new BitSet(new long[]{0x0000000000000000L,0x0000000001830004L});
    public static final BitSet FOLLOW_event_list_in_using2030 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_with_in_using2034 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_event_in_event_list2060 = new BitSet(new long[]{0x0000040000000002L,0x0000000001830004L});
    public static final BitSet FOLLOW_AND_in_event_list2062 = new BitSet(new long[]{0x0000000000000002L,0x0000000001830004L});
    public static final BitSet FOLLOW_patternname_in_event2084 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_AS_in_event2086 = new BitSet(new long[]{0x0000000000000000L,0x0000000000830004L});
    public static final BitSet FOLLOW_identifier_in_event2090 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WITH_in_with2112 = new BitSet(new long[]{0x0000000000000000L,0x0000000000830004L});
    public static final BitSet FOLLOW_subscription_list_in_with2116 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_starts_with_in_with2120 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_subscription_in_subscription_list2145 = new BitSet(new long[]{0x0000040000000002L,0x0000000000830004L});
    public static final BitSet FOLLOW_AND_in_subscription_list2147 = new BitSet(new long[]{0x0000000000000002L,0x0000000000830004L});
    public static final BitSet FOLLOW_field_in_subscription2168 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_93_in_subscription2171 = new BitSet(new long[]{0xC000000000000000L,0x00000000191F0200L});
    public static final BitSet FOLLOW_subscriptionvar_in_subscription2175 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datevar_in_subscriptionvar2202 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_datetimevar_in_subscriptionvar2219 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringvar_in_subscriptionvar2236 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_booleanvar_in_subscriptionvar2257 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_integervar_in_subscriptionvar2278 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_longvar_in_subscriptionvar2299 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_floatvar_in_subscriptionvar2320 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_doublevar_in_subscriptionvar2341 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bindvar_in_subscriptionvar2362 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_field2391 = new BitSet(new long[]{0x0000000000000000L,0x0000000040000000L});
    public static final BitSet FOLLOW_94_in_field2393 = new BitSet(new long[]{0x0000000000000000L,0x0000000000830004L});
    public static final BitSet FOLLOW_identifier_in_field2397 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STARTS_in_starts_with2416 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_WITH_in_starts_with2418 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_subpattern_in_starts_with2422 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_items_in_subpattern2443 = new BitSet(new long[]{0x0004000000000000L});
    public static final BitSet FOLLOW_then_list_in_subpattern2445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_item_list2471 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_item_list2476 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000008L});
    public static final BitSet FOLLOW_COMMA_in_item_list2479 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_item_list2484 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000048L});
    public static final BitSet FOLLOW_RPAREN_in_item_list2488 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifier_in_item2501 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_then_in_then_list2518 = new BitSet(new long[]{0x0004000000000002L});
    public static final BitSet FOLLOW_THEN_in_then2538 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_then2542 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THEN_in_then2561 = new BitSet(new long[]{0x0010000000000000L});
    public static final BitSet FOLLOW_WITHIN_in_then2563 = new BitSet(new long[]{0xC000000000000000L,0x00000000191F0200L});
    public static final BitSet FOLLOW_time_in_then2567 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_then2571 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THEN_in_then2593 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_DURING_in_then2595 = new BitSet(new long[]{0xC000000000000000L,0x00000000191F0200L});
    public static final BitSet FOLLOW_time_in_then2599 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_then2603 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THEN_in_then2625 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_AFTER_in_then2627 = new BitSet(new long[]{0xC000000000000000L,0x00000000191F0200L});
    public static final BitSet FOLLOW_time_in_then2631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_repeat2660 = new BitSet(new long[]{0x0040000000000000L});
    public static final BitSet FOLLOW_TO_in_repeat2662 = new BitSet(new long[]{0xC000000000000000L,0x00000000191F0200L});
    public static final BitSet FOLLOW_value_in_repeat2666 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_TIMES_in_repeat2668 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_repeat2670 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_value_in_repeat2696 = new BitSet(new long[]{0x0080000000000000L});
    public static final BitSet FOLLOW_TIMES_in_repeat2698 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_items_in_repeat2700 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_item_in_items2722 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ANY_in_items2738 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_ONE_in_items2740 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_item_list_in_items2742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ALL_in_items2760 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000020L});
    public static final BitSet FOLLOW_item_list_in_items2762 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REPEAT_in_items2780 = new BitSet(new long[]{0xC000000000000000L,0x00000000191F0200L});
    public static final BitSet FOLLOW_repeat_in_items2782 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_items2800 = new BitSet(new long[]{0x000A800000000000L,0x0000000000830024L});
    public static final BitSet FOLLOW_subpattern_in_items2802 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000040L});
    public static final BitSet FOLLOW_RPAREN_in_items2804 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bindvar_in_value2835 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unsignedintegervar_in_value2853 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_bindvar_in_time2885 = new BitSet(new long[]{0x3E00000000000000L});
    public static final BitSet FOLLOW_unsignedintegervar_in_time2889 = new BitSet(new long[]{0x3E00000000000000L});
    public static final BitSet FOLLOW_timeunittype_in_time2892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_timeunittype0 = new BitSet(new long[]{0x0000000000000002L});

}