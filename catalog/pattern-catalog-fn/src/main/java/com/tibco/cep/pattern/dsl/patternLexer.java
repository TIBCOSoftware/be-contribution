// $ANTLR 3.2 Sep 23, 2009 12:02:23 Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g 2014-04-11 11:18:07

	package com.tibco.cep.pattern.dsl; 

	import com.tibco.cep.pattern.dsl.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class patternLexer extends Lexer {
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
    public static final int DOUBLE=84;
    public static final int BIND_SYMBOL=73;
    public static final int SUCH=43;
    public static final int THEN_AFTER_TOKEN=23;
    public static final int RBRACE=72;
    public static final int NameFirstCharacter=85;
    public static final int NULL=64;
    public static final int ESCAPE_KEYWORD=66;
    public static final int DOUBLE_TOKEN=33;
    public static final int LONG_SUFFIX=78;
    public static final int WHITESPACE=89;
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

    public patternLexer() {;} 
    public patternLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public patternLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g"; }

    // $ANTLR start "T__91"
    public final void mT__91() throws RecognitionException {
        try {
            int _type = T__91;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:9:7: ( '$date(' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:9:9: '$date('
            {
            match("$date("); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__91"

    // $ANTLR start "T__92"
    public final void mT__92() throws RecognitionException {
        try {
            int _type = T__92;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:10:7: ( '$datetime(' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:10:9: '$datetime('
            {
            match("$datetime("); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__92"

    // $ANTLR start "T__93"
    public final void mT__93() throws RecognitionException {
        try {
            int _type = T__93;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:11:7: ( '=' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:11:9: '='
            {
            match('='); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__93"

    // $ANTLR start "T__94"
    public final void mT__94() throws RecognitionException {
        try {
            int _type = T__94;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:12:7: ( '.' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:12:9: '.'
            {
            match('.'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__94"

    // $ANTLR start "NEWLINE"
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:114:13: ( ( '\\r\\n' | '\\r' | '\\n' ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:114:15: ( '\\r\\n' | '\\r' | '\\n' )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:114:15: ( '\\r\\n' | '\\r' | '\\n' )
            int alt1=3;
            int LA1_0 = input.LA(1);

            if ( (LA1_0=='\r') ) {
                int LA1_1 = input.LA(2);

                if ( (LA1_1=='\n') ) {
                    alt1=1;
                }
                else {
                    alt1=2;}
            }
            else if ( (LA1_0=='\n') ) {
                alt1=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;
            }
            switch (alt1) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:114:17: '\\r\\n'
                    {
                    match("\r\n"); 


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:115:15: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 3 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:116:17: '\\n'
                    {
                    match('\n'); 

                    }
                    break;

            }

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NEWLINE"

    // $ANTLR start "DEFINE"
    public final void mDEFINE() throws RecognitionException {
        try {
            int _type = DEFINE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:119:10: ( 'define' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:119:12: 'define'
            {
            match("define"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DEFINE"

    // $ANTLR start "PATTERN"
    public final void mPATTERN() throws RecognitionException {
        try {
            int _type = PATTERN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:120:11: ( 'pattern' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:120:13: 'pattern'
            {
            match("pattern"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "PATTERN"

    // $ANTLR start "USING"
    public final void mUSING() throws RecognitionException {
        try {
            int _type = USING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:121:9: ( 'using' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:121:11: 'using'
            {
            match("using"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "USING"

    // $ANTLR start "EVENT"
    public final void mEVENT() throws RecognitionException {
        try {
            int _type = EVENT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:122:9: ( 'event' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:122:11: 'event'
            {
            match("event"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "EVENT"

    // $ANTLR start "AS"
    public final void mAS() throws RecognitionException {
        try {
            int _type = AS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:123:6: ( 'as' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:123:8: 'as'
            {
            match("as"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AS"

    // $ANTLR start "WHERE"
    public final void mWHERE() throws RecognitionException {
        try {
            int _type = WHERE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:124:9: ( 'where' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:124:11: 'where'
            {
            match("where"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHERE"

    // $ANTLR start "AND"
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:125:7: ( 'and' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:125:9: 'and'
            {
            match("and"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AND"

    // $ANTLR start "SUCH"
    public final void mSUCH() throws RecognitionException {
        try {
            int _type = SUCH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:126:8: ( 'such' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:126:10: 'such'
            {
            match("such"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SUCH"

    // $ANTLR start "THAT"
    public final void mTHAT() throws RecognitionException {
        try {
            int _type = THAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:127:8: ( 'that' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:127:10: 'that'
            {
            match("that"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "THAT"

    // $ANTLR start "STARTS"
    public final void mSTARTS() throws RecognitionException {
        try {
            int _type = STARTS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:128:10: ( 'starts' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:128:12: 'starts'
            {
            match("starts"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STARTS"

    // $ANTLR start "WITH"
    public final void mWITH() throws RecognitionException {
        try {
            int _type = WITH;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:129:8: ( 'with' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:129:10: 'with'
            {
            match("with"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WITH"

    // $ANTLR start "ANY"
    public final void mANY() throws RecognitionException {
        try {
            int _type = ANY;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:130:7: ( 'any' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:130:9: 'any'
            {
            match("any"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ANY"

    // $ANTLR start "ONE"
    public final void mONE() throws RecognitionException {
        try {
            int _type = ONE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:131:7: ( 'one' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:131:9: 'one'
            {
            match("one"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ONE"

    // $ANTLR start "ALL"
    public final void mALL() throws RecognitionException {
        try {
            int _type = ALL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:132:7: ( 'all' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:132:9: 'all'
            {
            match("all"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALL"

    // $ANTLR start "THEN"
    public final void mTHEN() throws RecognitionException {
        try {
            int _type = THEN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:133:8: ( 'then' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:133:10: 'then'
            {
            match("then"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "THEN"

    // $ANTLR start "REPEAT"
    public final void mREPEAT() throws RecognitionException {
        try {
            int _type = REPEAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:134:10: ( 'repeat' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:134:12: 'repeat'
            {
            match("repeat"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "REPEAT"

    // $ANTLR start "WITHIN"
    public final void mWITHIN() throws RecognitionException {
        try {
            int _type = WITHIN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:135:10: ( 'within' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:135:12: 'within'
            {
            match("within"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WITHIN"

    // $ANTLR start "DURING"
    public final void mDURING() throws RecognitionException {
        try {
            int _type = DURING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:136:10: ( 'during' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:136:12: 'during'
            {
            match("during"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DURING"

    // $ANTLR start "TO"
    public final void mTO() throws RecognitionException {
        try {
            int _type = TO;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:137:6: ( 'to' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:137:8: 'to'
            {
            match("to"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TO"

    // $ANTLR start "TIMES"
    public final void mTIMES() throws RecognitionException {
        try {
            int _type = TIMES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:138:9: ( 'times' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:138:11: 'times'
            {
            match("times"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TIMES"

    // $ANTLR start "AFTER"
    public final void mAFTER() throws RecognitionException {
        try {
            int _type = AFTER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:139:9: ( 'after' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:139:11: 'after'
            {
            match("after"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "AFTER"

    // $ANTLR start "DAYS"
    public final void mDAYS() throws RecognitionException {
        try {
            int _type = DAYS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:140:8: ( 'day' | 'days' )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0=='d') ) {
                int LA2_1 = input.LA(2);

                if ( (LA2_1=='a') ) {
                    int LA2_2 = input.LA(3);

                    if ( (LA2_2=='y') ) {
                        int LA2_3 = input.LA(4);

                        if ( (LA2_3=='s') ) {
                            alt2=2;
                        }
                        else {
                            alt2=1;}
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 2, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 2, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:140:10: 'day'
                    {
                    match("day"); 


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:140:18: 'days'
                    {
                    match("days"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DAYS"

    // $ANTLR start "HOURS"
    public final void mHOURS() throws RecognitionException {
        try {
            int _type = HOURS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:141:9: ( 'hour' | 'hours' )
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='h') ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1=='o') ) {
                    int LA3_2 = input.LA(3);

                    if ( (LA3_2=='u') ) {
                        int LA3_3 = input.LA(4);

                        if ( (LA3_3=='r') ) {
                            int LA3_4 = input.LA(5);

                            if ( (LA3_4=='s') ) {
                                alt3=2;
                            }
                            else {
                                alt3=1;}
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 3, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 3, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 3, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:141:11: 'hour'
                    {
                    match("hour"); 


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:141:20: 'hours'
                    {
                    match("hours"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "HOURS"

    // $ANTLR start "MINUTES"
    public final void mMINUTES() throws RecognitionException {
        try {
            int _type = MINUTES;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:142:11: ( 'minute' | 'minutes' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='m') ) {
                int LA4_1 = input.LA(2);

                if ( (LA4_1=='i') ) {
                    int LA4_2 = input.LA(3);

                    if ( (LA4_2=='n') ) {
                        int LA4_3 = input.LA(4);

                        if ( (LA4_3=='u') ) {
                            int LA4_4 = input.LA(5);

                            if ( (LA4_4=='t') ) {
                                int LA4_5 = input.LA(6);

                                if ( (LA4_5=='e') ) {
                                    int LA4_6 = input.LA(7);

                                    if ( (LA4_6=='s') ) {
                                        alt4=2;
                                    }
                                    else {
                                        alt4=1;}
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 4, 5, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 4, 4, input);

                                throw nvae;
                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 4, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 4, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 4, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:142:13: 'minute'
                    {
                    match("minute"); 


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:142:24: 'minutes'
                    {
                    match("minutes"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MINUTES"

    // $ANTLR start "SECONDS"
    public final void mSECONDS() throws RecognitionException {
        try {
            int _type = SECONDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:143:11: ( 'second' | 'seconds' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='s') ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1=='e') ) {
                    int LA5_2 = input.LA(3);

                    if ( (LA5_2=='c') ) {
                        int LA5_3 = input.LA(4);

                        if ( (LA5_3=='o') ) {
                            int LA5_4 = input.LA(5);

                            if ( (LA5_4=='n') ) {
                                int LA5_5 = input.LA(6);

                                if ( (LA5_5=='d') ) {
                                    int LA5_6 = input.LA(7);

                                    if ( (LA5_6=='s') ) {
                                        alt5=2;
                                    }
                                    else {
                                        alt5=1;}
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 5, 5, input);

                                    throw nvae;
                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 5, 4, input);

                                throw nvae;
                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 5, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 5, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 5, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:143:13: 'second'
                    {
                    match("second"); 


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:143:24: 'seconds'
                    {
                    match("seconds"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SECONDS"

    // $ANTLR start "MILLISECONDS"
    public final void mMILLISECONDS() throws RecognitionException {
        try {
            int _type = MILLISECONDS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:144:15: ( 'millisecond' | 'milliseconds' )
            int alt6=2;
            alt6 = dfa6.predict(input);
            switch (alt6) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:144:17: 'millisecond'
                    {
                    match("millisecond"); 


                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:144:33: 'milliseconds'
                    {
                    match("milliseconds"); 


                    }
                    break;

            }
            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "MILLISECONDS"

    // $ANTLR start "TRUE"
    public final void mTRUE() throws RecognitionException {
        try {
            int _type = TRUE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:145:8: ( 'true' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:145:10: 'true'
            {
            match("true"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "TRUE"

    // $ANTLR start "FALSE"
    public final void mFALSE() throws RecognitionException {
        try {
            int _type = FALSE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:146:9: ( 'false' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:146:11: 'false'
            {
            match("false"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FALSE"

    // $ANTLR start "NULL"
    public final void mNULL() throws RecognitionException {
        try {
            int _type = NULL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:147:8: ( 'null' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:147:10: 'null'
            {
            match("null"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NULL"

    // $ANTLR start "ESCAPE_SPACE"
    public final void mESCAPE_SPACE() throws RecognitionException {
        try {
            int _type = ESCAPE_SPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:149:15: ( '\\\\ ' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:149:17: '\\\\ '
            {
            match("\\ "); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ESCAPE_SPACE"

    // $ANTLR start "ESCAPE_KEYWORD"
    public final void mESCAPE_KEYWORD() throws RecognitionException {
        try {
            int _type = ESCAPE_KEYWORD;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:150:17: ( '#' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:150:19: '#'
            {
            match('#'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ESCAPE_KEYWORD"

    // $ANTLR start "COMMA"
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:151:9: ( ',' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:151:11: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "COMMA"

    // $ANTLR start "SEMI"
    public final void mSEMI() throws RecognitionException {
        try {
            int _type = SEMI;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:152:8: ( ';' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:152:10: ';'
            {
            match(';'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SEMI"

    // $ANTLR start "LPAREN"
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:153:10: ( '(' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:153:12: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LPAREN"

    // $ANTLR start "RPAREN"
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:154:10: ( ')' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:154:12: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RPAREN"

    // $ANTLR start "LBRACE"
    public final void mLBRACE() throws RecognitionException {
        try {
            int _type = LBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:155:10: ( '{' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:155:12: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LBRACE"

    // $ANTLR start "RBRACE"
    public final void mRBRACE() throws RecognitionException {
        try {
            int _type = RBRACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:156:10: ( '}' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:156:12: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "RBRACE"

    // $ANTLR start "BIND_SYMBOL"
    public final void mBIND_SYMBOL() throws RecognitionException {
        try {
            int _type = BIND_SYMBOL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:157:14: ( '$' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:157:16: '$'
            {
            match('$'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "BIND_SYMBOL"

    // $ANTLR start "QUOTES"
    public final void mQUOTES() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:158:18: ( ( '\"' ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:158:20: ( '\"' )
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:158:20: ( '\"' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:158:21: '\"'
            {
            match('\"'); 

            }


            }

        }
        finally {
        }
    }
    // $ANTLR end "QUOTES"

    // $ANTLR start "CHAR"
    public final void mCHAR() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:160:16: ( ( 'a' .. 'z' | 'A' .. 'Z' | '/' | '_' | '-' | '0' .. '9' ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:160:18: ( 'a' .. 'z' | 'A' .. 'Z' | '/' | '_' | '-' | '0' .. '9' )
            {
            if ( input.LA(1)=='-'||(input.LA(1)>='/' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "CHAR"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:161:17: ( '0' .. '9' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:161:19: '0' .. '9'
            {
            matchRange('0','9'); 

            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "SIGN"
    public final void mSIGN() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:162:16: ( '+' | '-' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "SIGN"

    // $ANTLR start "LONG_SUFFIX"
    public final void mLONG_SUFFIX() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:164:22: ( 'l' | 'L' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "LONG_SUFFIX"

    // $ANTLR start "DOUBLE_SUFFIX"
    public final void mDOUBLE_SUFFIX() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:165:24: ( 'd' | 'D' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE_SUFFIX"

    // $ANTLR start "SIGNED"
    public final void mSIGNED() throws RecognitionException {
        try {
            int _type = SIGNED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:167:10: ( SIGN ( DIGIT )+ )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:167:12: SIGN ( DIGIT )+
            {
            mSIGN(); 
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:167:17: ( DIGIT )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:167:17: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "SIGNED"

    // $ANTLR start "UNSIGNED"
    public final void mUNSIGNED() throws RecognitionException {
        try {
            int _type = UNSIGNED;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:168:11: ( ( DIGIT )+ )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:168:13: ( DIGIT )+
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:168:13: ( DIGIT )+
            int cnt8=0;
            loop8:
            do {
                int alt8=2;
                int LA8_0 = input.LA(1);

                if ( ((LA8_0>='0' && LA8_0<='9')) ) {
                    alt8=1;
                }


                switch (alt8) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:168:13: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    if ( cnt8 >= 1 ) break loop8;
                        EarlyExitException eee =
                            new EarlyExitException(8, input);
                        throw eee;
                }
                cnt8++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "UNSIGNED"

    // $ANTLR start "LONG"
    public final void mLONG() throws RecognitionException {
        try {
            int _type = LONG;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:170:8: ( ( SIGNED | UNSIGNED ) LONG_SUFFIX )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:170:10: ( SIGNED | UNSIGNED ) LONG_SUFFIX
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:170:10: ( SIGNED | UNSIGNED )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0=='+'||LA9_0=='-') ) {
                alt9=1;
            }
            else if ( ((LA9_0>='0' && LA9_0<='9')) ) {
                alt9=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:170:11: SIGNED
                    {
                    mSIGNED(); 

                    }
                    break;
                case 2 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:170:20: UNSIGNED
                    {
                    mUNSIGNED(); 

                    }
                    break;

            }

            mLONG_SUFFIX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "LONG"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:9: ( ( SIGN )? ( DIGIT )+ '.' ( DIGIT )* )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:11: ( SIGN )? ( DIGIT )+ '.' ( DIGIT )*
            {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:11: ( SIGN )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( (LA10_0=='+'||LA10_0=='-') ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:11: SIGN
                    {
                    mSIGN(); 

                    }
                    break;

            }

            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:17: ( DIGIT )+
            int cnt11=0;
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='0' && LA11_0<='9')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:17: DIGIT
            	    {
            	    mDIGIT(); 

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

            match('.'); 
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:28: ( DIGIT )*
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>='0' && LA12_0<='9')) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:172:28: DIGIT
            	    {
            	    mDIGIT(); 

            	    }
            	    break;

            	default :
            	    break loop12;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "DOUBLE"
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:173:10: ( FLOAT DOUBLE_SUFFIX )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:173:12: FLOAT DOUBLE_SUFFIX
            {
            mFLOAT(); 
            mDOUBLE_SUFFIX(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "DOUBLE"

    // $ANTLR start "NameFirstCharacter"
    public final void mNameFirstCharacter() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:176:19: ( CHAR | '\\u0041' .. '\\u005a' | '\\u0061' .. '\\u007a' | '\\u00c0' .. '\\u00d6' | '\\u00d8' .. '\\u00f6' | '\\u00f8' .. '\\u00ff' | '\\u0100' .. '\\u1fff' | '\\u3040' .. '\\u318f' | '\\u3300' .. '\\u337f' | '\\u3400' .. '\\u3d2d' | '\\u4e00' .. '\\u9fff' | '\\uf900' .. '\\ufaff' | '\\uff61' .. '\\uff9f' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            if ( input.LA(1)=='-'||(input.LA(1)>='/' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF')||(input.LA(1)>='\uFF61' && input.LA(1)<='\uFF9F') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "NameFirstCharacter"

    // $ANTLR start "NameCharacter"
    public final void mNameCharacter() throws RecognitionException {
        try {
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:194:14: ( '\\u0030' .. '\\u0039' | '\\u0660' .. '\\u0669' | '\\u06f0' .. '\\u06f9' | '\\u0966' .. '\\u096f' | '\\u09e6' .. '\\u09ef' | '\\u0a66' .. '\\u0a6f' | '\\u0ae6' .. '\\u0aef' | '\\u0b66' .. '\\u0b6f' | '\\u0be7' .. '\\u0bef' | '\\u0c66' .. '\\u0c6f' | '\\u0ce6' .. '\\u0cef' | '\\u0d66' .. '\\u0d6f' | '\\u0e50' .. '\\u0e59' | '\\u0ed0' .. '\\u0ed9' | '\\u1040' .. '\\u1049' )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            {
            if ( (input.LA(1)>='0' && input.LA(1)<='9')||(input.LA(1)>='\u0660' && input.LA(1)<='\u0669')||(input.LA(1)>='\u06F0' && input.LA(1)<='\u06F9')||(input.LA(1)>='\u0966' && input.LA(1)<='\u096F')||(input.LA(1)>='\u09E6' && input.LA(1)<='\u09EF')||(input.LA(1)>='\u0A66' && input.LA(1)<='\u0A6F')||(input.LA(1)>='\u0AE6' && input.LA(1)<='\u0AEF')||(input.LA(1)>='\u0B66' && input.LA(1)<='\u0B6F')||(input.LA(1)>='\u0BE7' && input.LA(1)<='\u0BEF')||(input.LA(1)>='\u0C66' && input.LA(1)<='\u0C6F')||(input.LA(1)>='\u0CE6' && input.LA(1)<='\u0CEF')||(input.LA(1)>='\u0D66' && input.LA(1)<='\u0D6F')||(input.LA(1)>='\u0E50' && input.LA(1)<='\u0E59')||(input.LA(1)>='\u0ED0' && input.LA(1)<='\u0ED9')||(input.LA(1)>='\u1040' && input.LA(1)<='\u1049') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

        }
        finally {
        }
    }
    // $ANTLR end "NameCharacter"

    // $ANTLR start "IDENTIFIER"
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:213:11: ( NameFirstCharacter ( NameFirstCharacter | NameCharacter )* )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:214:12: NameFirstCharacter ( NameFirstCharacter | NameCharacter )*
            {
            mNameFirstCharacter(); 
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:214:31: ( NameFirstCharacter | NameCharacter )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0=='-'||(LA13_0>='/' && LA13_0<='9')||(LA13_0>='A' && LA13_0<='Z')||LA13_0=='_'||(LA13_0>='a' && LA13_0<='z')||(LA13_0>='\u00C0' && LA13_0<='\u00D6')||(LA13_0>='\u00D8' && LA13_0<='\u00F6')||(LA13_0>='\u00F8' && LA13_0<='\u1FFF')||(LA13_0>='\u3040' && LA13_0<='\u318F')||(LA13_0>='\u3300' && LA13_0<='\u337F')||(LA13_0>='\u3400' && LA13_0<='\u3D2D')||(LA13_0>='\u4E00' && LA13_0<='\u9FFF')||(LA13_0>='\uF900' && LA13_0<='\uFAFF')||(LA13_0>='\uFF61' && LA13_0<='\uFF9F')) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:
            	    {
            	    if ( input.LA(1)=='-'||(input.LA(1)>='/' && input.LA(1)<='9')||(input.LA(1)>='A' && input.LA(1)<='Z')||input.LA(1)=='_'||(input.LA(1)>='a' && input.LA(1)<='z')||(input.LA(1)>='\u00C0' && input.LA(1)<='\u00D6')||(input.LA(1)>='\u00D8' && input.LA(1)<='\u00F6')||(input.LA(1)>='\u00F8' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u3040' && input.LA(1)<='\u318F')||(input.LA(1)>='\u3300' && input.LA(1)<='\u337F')||(input.LA(1)>='\u3400' && input.LA(1)<='\u3D2D')||(input.LA(1)>='\u4E00' && input.LA(1)<='\u9FFF')||(input.LA(1)>='\uF900' && input.LA(1)<='\uFAFF')||(input.LA(1)>='\uFF61' && input.LA(1)<='\uFF9F') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "IDENTIFIER"

    // $ANTLR start "NAME"
    public final void mNAME() throws RecognitionException {
        try {
            int _type = NAME;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:217:8: ( QUOTES ( options {greedy=false; } : . )* QUOTES )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:217:10: QUOTES ( options {greedy=false; } : . )* QUOTES
            {
            mQUOTES(); 
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:217:17: ( options {greedy=false; } : . )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0=='\"') ) {
                    alt14=2;
                }
                else if ( ((LA14_0>='\u0000' && LA14_0<='!')||(LA14_0>='#' && LA14_0<='\uFFFF')) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:217:48: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);

            mQUOTES(); 
            setText(getText().substring(1,getText().length()-1));

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NAME"

    // $ANTLR start "WHITESPACE"
    public final void mWHITESPACE() throws RecognitionException {
        try {
            int _type = WHITESPACE;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:328:13: ( ( ' ' | '\\t' | '\\f' ) )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:328:15: ( ' ' | '\\t' | '\\f' )
            {
            if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}

            _channel=HIDDEN;

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WHITESPACE"

    // $ANTLR start "ALLCHARS"
    public final void mALLCHARS() throws RecognitionException {
        try {
            int _type = ALLCHARS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:333:11: ( . )
            // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:333:13: .
            {
            matchAny(); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALLCHARS"

    public void mTokens() throws RecognitionException {
        // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:8: ( T__91 | T__92 | T__93 | T__94 | NEWLINE | DEFINE | PATTERN | USING | EVENT | AS | WHERE | AND | SUCH | THAT | STARTS | WITH | ANY | ONE | ALL | THEN | REPEAT | WITHIN | DURING | TO | TIMES | AFTER | DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS | TRUE | FALSE | NULL | ESCAPE_SPACE | ESCAPE_KEYWORD | COMMA | SEMI | LPAREN | RPAREN | LBRACE | RBRACE | BIND_SYMBOL | SIGNED | UNSIGNED | LONG | FLOAT | DOUBLE | IDENTIFIER | NAME | WHITESPACE | ALLCHARS )
        int alt15=52;
        alt15 = dfa15.predict(input);
        switch (alt15) {
            case 1 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:10: T__91
                {
                mT__91(); 

                }
                break;
            case 2 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:16: T__92
                {
                mT__92(); 

                }
                break;
            case 3 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:22: T__93
                {
                mT__93(); 

                }
                break;
            case 4 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:28: T__94
                {
                mT__94(); 

                }
                break;
            case 5 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:34: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 6 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:42: DEFINE
                {
                mDEFINE(); 

                }
                break;
            case 7 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:49: PATTERN
                {
                mPATTERN(); 

                }
                break;
            case 8 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:57: USING
                {
                mUSING(); 

                }
                break;
            case 9 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:63: EVENT
                {
                mEVENT(); 

                }
                break;
            case 10 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:69: AS
                {
                mAS(); 

                }
                break;
            case 11 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:72: WHERE
                {
                mWHERE(); 

                }
                break;
            case 12 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:78: AND
                {
                mAND(); 

                }
                break;
            case 13 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:82: SUCH
                {
                mSUCH(); 

                }
                break;
            case 14 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:87: THAT
                {
                mTHAT(); 

                }
                break;
            case 15 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:92: STARTS
                {
                mSTARTS(); 

                }
                break;
            case 16 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:99: WITH
                {
                mWITH(); 

                }
                break;
            case 17 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:104: ANY
                {
                mANY(); 

                }
                break;
            case 18 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:108: ONE
                {
                mONE(); 

                }
                break;
            case 19 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:112: ALL
                {
                mALL(); 

                }
                break;
            case 20 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:116: THEN
                {
                mTHEN(); 

                }
                break;
            case 21 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:121: REPEAT
                {
                mREPEAT(); 

                }
                break;
            case 22 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:128: WITHIN
                {
                mWITHIN(); 

                }
                break;
            case 23 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:135: DURING
                {
                mDURING(); 

                }
                break;
            case 24 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:142: TO
                {
                mTO(); 

                }
                break;
            case 25 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:145: TIMES
                {
                mTIMES(); 

                }
                break;
            case 26 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:151: AFTER
                {
                mAFTER(); 

                }
                break;
            case 27 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:157: DAYS
                {
                mDAYS(); 

                }
                break;
            case 28 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:162: HOURS
                {
                mHOURS(); 

                }
                break;
            case 29 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:168: MINUTES
                {
                mMINUTES(); 

                }
                break;
            case 30 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:176: SECONDS
                {
                mSECONDS(); 

                }
                break;
            case 31 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:184: MILLISECONDS
                {
                mMILLISECONDS(); 

                }
                break;
            case 32 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:197: TRUE
                {
                mTRUE(); 

                }
                break;
            case 33 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:202: FALSE
                {
                mFALSE(); 

                }
                break;
            case 34 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:208: NULL
                {
                mNULL(); 

                }
                break;
            case 35 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:213: ESCAPE_SPACE
                {
                mESCAPE_SPACE(); 

                }
                break;
            case 36 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:226: ESCAPE_KEYWORD
                {
                mESCAPE_KEYWORD(); 

                }
                break;
            case 37 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:241: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 38 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:247: SEMI
                {
                mSEMI(); 

                }
                break;
            case 39 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:252: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 40 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:259: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 41 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:266: LBRACE
                {
                mLBRACE(); 

                }
                break;
            case 42 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:273: RBRACE
                {
                mRBRACE(); 

                }
                break;
            case 43 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:280: BIND_SYMBOL
                {
                mBIND_SYMBOL(); 

                }
                break;
            case 44 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:292: SIGNED
                {
                mSIGNED(); 

                }
                break;
            case 45 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:299: UNSIGNED
                {
                mUNSIGNED(); 

                }
                break;
            case 46 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:308: LONG
                {
                mLONG(); 

                }
                break;
            case 47 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:313: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 48 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:319: DOUBLE
                {
                mDOUBLE(); 

                }
                break;
            case 49 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:326: IDENTIFIER
                {
                mIDENTIFIER(); 

                }
                break;
            case 50 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:337: NAME
                {
                mNAME(); 

                }
                break;
            case 51 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:342: WHITESPACE
                {
                mWHITESPACE(); 

                }
                break;
            case 52 :
                // Q:\\be\\branches\\5.2\\runtime\\modules\\pattern\\pattern-lang\\src\\com\\tibco\\cep\\pattern\\dsl\\pattern.g:1:353: ALLCHARS
                {
                mALLCHARS(); 

                }
                break;

        }

    }


    protected DFA6 dfa6 = new DFA6(this);
    protected DFA15 dfa15 = new DFA15(this);
    static final String DFA6_eotS =
        "\13\uffff\1\15\2\uffff";
    static final String DFA6_eofS =
        "\16\uffff";
    static final String DFA6_minS =
        "\1\155\1\151\2\154\1\151\1\163\1\145\1\143\1\157\1\156\1\144\1"+
        "\163\2\uffff";
    static final String DFA6_maxS =
        "\1\155\1\151\2\154\1\151\1\163\1\145\1\143\1\157\1\156\1\144\1"+
        "\163\2\uffff";
    static final String DFA6_acceptS =
        "\14\uffff\1\2\1\1";
    static final String DFA6_specialS =
        "\16\uffff}>";
    static final String[] DFA6_transitionS = {
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
            "",
            ""
    };

    static final short[] DFA6_eot = DFA.unpackEncodedString(DFA6_eotS);
    static final short[] DFA6_eof = DFA.unpackEncodedString(DFA6_eofS);
    static final char[] DFA6_min = DFA.unpackEncodedStringToUnsignedChars(DFA6_minS);
    static final char[] DFA6_max = DFA.unpackEncodedStringToUnsignedChars(DFA6_maxS);
    static final short[] DFA6_accept = DFA.unpackEncodedString(DFA6_acceptS);
    static final short[] DFA6_special = DFA.unpackEncodedString(DFA6_specialS);
    static final short[][] DFA6_transition;

    static {
        int numStates = DFA6_transitionS.length;
        DFA6_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA6_transition[i] = DFA.unpackEncodedString(DFA6_transitionS[i]);
        }
    }

    class DFA6 extends DFA {

        public DFA6(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 6;
            this.eot = DFA6_eot;
            this.eof = DFA6_eof;
            this.min = DFA6_min;
            this.max = DFA6_max;
            this.accept = DFA6_accept;
            this.special = DFA6_special;
            this.transition = DFA6_transition;
        }
        public String getDescription() {
            return "144:1: MILLISECONDS : ( 'millisecond' | 'milliseconds' );";
        }
    }
    static final String DFA15_eotS =
        "\1\uffff\1\44\4\uffff\16\53\1\42\7\uffff\1\53\1\113\1\42\1\uffff"+
        "\1\42\7\uffff\3\53\1\uffff\3\53\1\131\11\53\1\145\10\53\10\uffff"+
        "\1\157\1\uffff\1\113\1\160\1\162\1\157\3\uffff\2\53\1\170\3\53\1"+
        "\uffff\1\174\1\175\1\176\10\53\1\uffff\2\53\1\u0089\6\53\2\uffff"+
        "\1\162\3\uffff\2\53\1\170\1\uffff\3\53\3\uffff\2\53\1\u0099\1\u009a"+
        "\2\53\1\u009d\1\u009e\1\53\1\u00a0\1\uffff\1\53\1\u00a3\3\53\1\u00a7"+
        "\1\uffff\3\53\1\u00ad\1\u00ae\1\u00af\1\u00b0\1\53\2\uffff\2\53"+
        "\2\uffff\1\u00b4\1\uffff\1\53\1\u00a3\1\uffff\2\53\1\u00b8\3\uffff"+
        "\1\u00b9\1\u00ba\1\53\4\uffff\1\u00bc\1\u00bd\1\u00bf\1\uffff\1"+
        "\u00c0\1\u00c2\1\53\3\uffff\1\u00c4\2\uffff\1\u00bf\2\uffff\1\u00c2"+
        "\1\uffff\1\53\1\uffff\3\53\2\u00ca\1\uffff";
    static final String DFA15_eofS =
        "\u00cb\uffff";
    static final String DFA15_minS =
        "\1\0\1\144\4\uffff\2\141\1\163\1\166\1\146\1\150\1\145\1\150\1"+
        "\156\1\145\1\157\1\151\1\141\1\165\1\40\7\uffff\1\60\1\55\1\60\1"+
        "\uffff\1\0\2\uffff\1\141\4\uffff\1\146\1\162\1\171\1\uffff\1\164"+
        "\1\151\1\145\1\55\1\144\1\154\1\164\1\145\1\164\1\143\1\141\1\143"+
        "\1\141\1\55\1\155\1\165\1\145\1\160\1\165\3\154\10\uffff\1\55\1"+
        "\uffff\2\55\1\60\1\56\2\uffff\1\164\2\151\1\55\1\164\2\156\1\uffff"+
        "\3\55\1\145\1\162\2\150\1\162\1\157\1\164\1\156\1\uffff\2\145\1"+
        "\55\1\145\1\162\1\165\1\154\1\163\1\154\2\uffff\1\60\2\uffff\1\145"+
        "\2\156\1\55\1\uffff\1\145\1\147\1\164\3\uffff\1\162\1\145\2\55\1"+
        "\164\1\156\2\55\1\163\1\55\1\uffff\1\141\1\55\1\164\1\151\1\145"+
        "\1\55\1\50\1\145\1\147\1\162\4\55\1\156\2\uffff\1\163\1\144\2\uffff"+
        "\1\55\1\uffff\1\164\1\55\1\uffff\1\145\1\163\1\55\3\uffff\2\55\1"+
        "\156\4\uffff\3\55\1\uffff\2\55\1\145\3\uffff\1\55\2\uffff\1\55\2"+
        "\uffff\1\55\1\uffff\1\143\1\uffff\1\157\1\156\1\144\2\55\1\uffff";
    static final String DFA15_maxS =
        "\1\uffff\1\144\4\uffff\1\165\1\141\1\163\1\166\1\163\1\151\1\165"+
        "\1\162\1\156\1\145\1\157\1\151\1\141\1\165\1\40\7\uffff\1\71\1\uff9f"+
        "\1\71\1\uffff\1\uffff\2\uffff\1\141\4\uffff\1\146\1\162\1\171\1"+
        "\uffff\1\164\1\151\1\145\1\uff9f\1\171\1\154\1\164\1\145\1\164\1"+
        "\143\1\141\1\143\1\145\1\uff9f\1\155\1\165\1\145\1\160\1\165\1\156"+
        "\2\154\10\uffff\1\uff9f\1\uffff\2\uff9f\1\144\1\154\2\uffff\1\164"+
        "\2\151\1\uff9f\1\164\2\156\1\uffff\3\uff9f\1\145\1\162\2\150\1\162"+
        "\1\157\1\164\1\156\1\uffff\2\145\1\uff9f\1\145\1\162\1\165\1\154"+
        "\1\163\1\154\2\uffff\1\144\2\uffff\1\145\2\156\1\uff9f\1\uffff\1"+
        "\145\1\147\1\164\3\uffff\1\162\1\145\2\uff9f\1\164\1\156\2\uff9f"+
        "\1\163\1\uff9f\1\uffff\1\141\1\uff9f\1\164\1\151\1\145\1\uff9f\1"+
        "\164\1\145\1\147\1\162\4\uff9f\1\156\2\uffff\1\163\1\144\2\uffff"+
        "\1\uff9f\1\uffff\1\164\1\uff9f\1\uffff\1\145\1\163\1\uff9f\3\uffff"+
        "\2\uff9f\1\156\4\uffff\3\uff9f\1\uffff\2\uff9f\1\145\3\uffff\1\uff9f"+
        "\2\uffff\1\uff9f\2\uffff\1\uff9f\1\uffff\1\143\1\uffff\1\157\1\156"+
        "\1\144\2\uff9f\1\uffff";
    static final String DFA15_acceptS =
        "\2\uffff\1\3\1\4\2\5\17\uffff\1\44\1\45\1\46\1\47\1\50\1\51\1\52"+
        "\3\uffff\1\61\1\uffff\1\63\1\64\1\uffff\1\53\1\3\1\4\1\5\3\uffff"+
        "\1\61\26\uffff\1\43\1\44\1\45\1\46\1\47\1\50\1\51\1\52\1\uffff\1"+
        "\55\4\uffff\1\62\1\63\7\uffff\1\12\13\uffff\1\30\11\uffff\1\54\1"+
        "\56\1\uffff\1\57\1\60\4\uffff\1\33\3\uffff\1\14\1\21\1\23\12\uffff"+
        "\1\22\17\uffff\1\20\1\15\2\uffff\1\16\1\24\1\uffff\1\40\2\uffff"+
        "\1\34\3\uffff\1\42\1\1\1\2\3\uffff\1\10\1\11\1\32\1\13\3\uffff\1"+
        "\31\3\uffff\1\41\1\6\1\27\1\uffff\1\26\1\17\1\uffff\1\36\1\25\1"+
        "\uffff\1\35\1\uffff\1\7\5\uffff\1\37";
    static final String DFA15_specialS =
        "\1\0\37\uffff\1\1\u00aa\uffff}>";
    static final String[] DFA15_transitionS = {
            "\11\42\1\41\1\5\1\42\1\41\1\4\22\42\1\41\1\42\1\40\1\25\1\1"+
            "\3\42\1\30\1\31\1\42\1\36\1\26\1\34\1\3\1\37\12\35\1\42\1\27"+
            "\1\42\1\2\3\42\32\37\1\42\1\24\2\42\1\37\1\42\1\12\2\37\1\6"+
            "\1\11\1\22\1\37\1\20\4\37\1\21\1\23\1\16\1\7\1\37\1\17\1\14"+
            "\1\15\1\10\1\37\1\13\3\37\1\32\1\42\1\33\102\42\27\37\1\42\37"+
            "\37\1\42\u1f08\37\u1040\42\u0150\37\u0170\42\u0080\37\u0080"+
            "\42\u092e\37\u10d2\42\u5200\37\u5900\42\u0200\37\u0461\42\77"+
            "\37\140\42",
            "\1\43",
            "",
            "",
            "",
            "",
            "\1\52\3\uffff\1\50\17\uffff\1\51",
            "\1\54",
            "\1\55",
            "\1\56",
            "\1\62\5\uffff\1\61\1\uffff\1\60\4\uffff\1\57",
            "\1\63\1\64",
            "\1\67\16\uffff\1\66\1\65",
            "\1\70\1\72\5\uffff\1\71\2\uffff\1\73",
            "\1\74",
            "\1\75",
            "\1\76",
            "\1\77",
            "\1\100",
            "\1\101",
            "\1\102",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\12\112",
            "\1\53\1\116\1\53\12\114\7\uffff\13\53\1\115\16\53\4\uffff"+
            "\1\53\1\uffff\13\53\1\115\16\53\105\uffff\27\53\1\uffff\37\53"+
            "\1\uffff\u1f08\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53"+
            "\u0080\uffff\u092e\53\u10d2\uffff\u5200\53\u5900\uffff\u0200"+
            "\53\u0461\uffff\77\53",
            "\12\117",
            "",
            "\0\120",
            "",
            "",
            "\1\122",
            "",
            "",
            "",
            "",
            "\1\123",
            "\1\124",
            "\1\125",
            "",
            "\1\126",
            "\1\127",
            "\1\130",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\132\24\uffff\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "\1\140",
            "\1\141",
            "\1\142",
            "\1\143\3\uffff\1\144",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\146",
            "\1\147",
            "\1\150",
            "\1\151",
            "\1\152",
            "\1\154\1\uffff\1\153",
            "\1\155",
            "\1\156",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\53\1\116\1\53\12\112\7\uffff\13\53\1\115\16\53\4\uffff"+
            "\1\53\1\uffff\13\53\1\115\16\53\105\uffff\27\53\1\uffff\37\53"+
            "\1\uffff\u1f08\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53"+
            "\u0080\uffff\u092e\53\u10d2\uffff\u5200\53\u5900\uffff\u0200"+
            "\53\u0461\uffff\77\53",
            "",
            "\1\53\1\116\1\53\12\114\7\uffff\13\53\1\115\16\53\4\uffff"+
            "\1\53\1\uffff\13\53\1\115\16\53\105\uffff\27\53\1\uffff\37\53"+
            "\1\uffff\u1f08\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53"+
            "\u0080\uffff\u092e\53\u10d2\uffff\u5200\53\u5900\uffff\u0200"+
            "\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\12\161\12\uffff\1\163\37\uffff\1\163",
            "\1\116\1\uffff\12\117\22\uffff\1\160\37\uffff\1\160",
            "",
            "",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\22"+
            "\53\1\167\7\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53"+
            "\u1040\uffff\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e"+
            "\53\u10d2\uffff\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77"+
            "\53",
            "\1\171",
            "\1\172",
            "\1\173",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\177",
            "\1\u0080",
            "\1\u0081",
            "\1\u0082",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "",
            "\1\u0087",
            "\1\u0088",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\u008a",
            "\1\u008b",
            "\1\u008c",
            "\1\u008d",
            "\1\u008e",
            "\1\u008f",
            "",
            "",
            "\12\161\12\uffff\1\163\37\uffff\1\163",
            "",
            "",
            "\1\u0090",
            "\1\u0091",
            "\1\u0092",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "\1\u0093",
            "\1\u0094",
            "\1\u0095",
            "",
            "",
            "",
            "\1\u0096",
            "\1\u0097",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\10"+
            "\53\1\u0098\21\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08"+
            "\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e"+
            "\53\u10d2\uffff\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77"+
            "\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\u009b",
            "\1\u009c",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\u009f",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "\1\u00a1",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\22"+
            "\53\1\u00a2\7\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08"+
            "\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e"+
            "\53\u10d2\uffff\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77"+
            "\53",
            "\1\u00a4",
            "\1\u00a5",
            "\1\u00a6",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\u00a8\113\uffff\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "\1\u00ac",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\u00b1",
            "",
            "",
            "\1\u00b2",
            "\1\u00b3",
            "",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "\1\u00b5",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "\1\u00b6",
            "\1\u00b7",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\u00bb",
            "",
            "",
            "",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\22"+
            "\53\1\u00be\7\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08"+
            "\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e"+
            "\53\u10d2\uffff\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77"+
            "\53",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\22"+
            "\53\1\u00c1\7\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08"+
            "\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e"+
            "\53\u10d2\uffff\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77"+
            "\53",
            "\1\u00c3",
            "",
            "",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            "",
            "\1\u00c5",
            "",
            "\1\u00c6",
            "\1\u00c7",
            "\1\u00c8",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\22"+
            "\53\1\u00c9\7\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08"+
            "\53\u1040\uffff\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e"+
            "\53\u10d2\uffff\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77"+
            "\53",
            "\1\53\1\uffff\13\53\7\uffff\32\53\4\uffff\1\53\1\uffff\32"+
            "\53\105\uffff\27\53\1\uffff\37\53\1\uffff\u1f08\53\u1040\uffff"+
            "\u0150\53\u0170\uffff\u0080\53\u0080\uffff\u092e\53\u10d2\uffff"+
            "\u5200\53\u5900\uffff\u0200\53\u0461\uffff\77\53",
            ""
    };

    static final short[] DFA15_eot = DFA.unpackEncodedString(DFA15_eotS);
    static final short[] DFA15_eof = DFA.unpackEncodedString(DFA15_eofS);
    static final char[] DFA15_min = DFA.unpackEncodedStringToUnsignedChars(DFA15_minS);
    static final char[] DFA15_max = DFA.unpackEncodedStringToUnsignedChars(DFA15_maxS);
    static final short[] DFA15_accept = DFA.unpackEncodedString(DFA15_acceptS);
    static final short[] DFA15_special = DFA.unpackEncodedString(DFA15_specialS);
    static final short[][] DFA15_transition;

    static {
        int numStates = DFA15_transitionS.length;
        DFA15_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA15_transition[i] = DFA.unpackEncodedString(DFA15_transitionS[i]);
        }
    }

    class DFA15 extends DFA {

        public DFA15(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 15;
            this.eot = DFA15_eot;
            this.eof = DFA15_eof;
            this.min = DFA15_min;
            this.max = DFA15_max;
            this.accept = DFA15_accept;
            this.special = DFA15_special;
            this.transition = DFA15_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__91 | T__92 | T__93 | T__94 | NEWLINE | DEFINE | PATTERN | USING | EVENT | AS | WHERE | AND | SUCH | THAT | STARTS | WITH | ANY | ONE | ALL | THEN | REPEAT | WITHIN | DURING | TO | TIMES | AFTER | DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS | TRUE | FALSE | NULL | ESCAPE_SPACE | ESCAPE_KEYWORD | COMMA | SEMI | LPAREN | RPAREN | LBRACE | RBRACE | BIND_SYMBOL | SIGNED | UNSIGNED | LONG | FLOAT | DOUBLE | IDENTIFIER | NAME | WHITESPACE | ALLCHARS );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA15_0 = input.LA(1);

                        s = -1;
                        if ( (LA15_0=='$') ) {s = 1;}

                        else if ( (LA15_0=='=') ) {s = 2;}

                        else if ( (LA15_0=='.') ) {s = 3;}

                        else if ( (LA15_0=='\r') ) {s = 4;}

                        else if ( (LA15_0=='\n') ) {s = 5;}

                        else if ( (LA15_0=='d') ) {s = 6;}

                        else if ( (LA15_0=='p') ) {s = 7;}

                        else if ( (LA15_0=='u') ) {s = 8;}

                        else if ( (LA15_0=='e') ) {s = 9;}

                        else if ( (LA15_0=='a') ) {s = 10;}

                        else if ( (LA15_0=='w') ) {s = 11;}

                        else if ( (LA15_0=='s') ) {s = 12;}

                        else if ( (LA15_0=='t') ) {s = 13;}

                        else if ( (LA15_0=='o') ) {s = 14;}

                        else if ( (LA15_0=='r') ) {s = 15;}

                        else if ( (LA15_0=='h') ) {s = 16;}

                        else if ( (LA15_0=='m') ) {s = 17;}

                        else if ( (LA15_0=='f') ) {s = 18;}

                        else if ( (LA15_0=='n') ) {s = 19;}

                        else if ( (LA15_0=='\\') ) {s = 20;}

                        else if ( (LA15_0=='#') ) {s = 21;}

                        else if ( (LA15_0==',') ) {s = 22;}

                        else if ( (LA15_0==';') ) {s = 23;}

                        else if ( (LA15_0=='(') ) {s = 24;}

                        else if ( (LA15_0==')') ) {s = 25;}

                        else if ( (LA15_0=='{') ) {s = 26;}

                        else if ( (LA15_0=='}') ) {s = 27;}

                        else if ( (LA15_0=='-') ) {s = 28;}

                        else if ( ((LA15_0>='0' && LA15_0<='9')) ) {s = 29;}

                        else if ( (LA15_0=='+') ) {s = 30;}

                        else if ( (LA15_0=='/'||(LA15_0>='A' && LA15_0<='Z')||LA15_0=='_'||(LA15_0>='b' && LA15_0<='c')||LA15_0=='g'||(LA15_0>='i' && LA15_0<='l')||LA15_0=='q'||LA15_0=='v'||(LA15_0>='x' && LA15_0<='z')||(LA15_0>='\u00C0' && LA15_0<='\u00D6')||(LA15_0>='\u00D8' && LA15_0<='\u00F6')||(LA15_0>='\u00F8' && LA15_0<='\u1FFF')||(LA15_0>='\u3040' && LA15_0<='\u318F')||(LA15_0>='\u3300' && LA15_0<='\u337F')||(LA15_0>='\u3400' && LA15_0<='\u3D2D')||(LA15_0>='\u4E00' && LA15_0<='\u9FFF')||(LA15_0>='\uF900' && LA15_0<='\uFAFF')||(LA15_0>='\uFF61' && LA15_0<='\uFF9F')) ) {s = 31;}

                        else if ( (LA15_0=='\"') ) {s = 32;}

                        else if ( (LA15_0=='\t'||LA15_0=='\f'||LA15_0==' ') ) {s = 33;}

                        else if ( ((LA15_0>='\u0000' && LA15_0<='\b')||LA15_0=='\u000B'||(LA15_0>='\u000E' && LA15_0<='\u001F')||LA15_0=='!'||(LA15_0>='%' && LA15_0<='\'')||LA15_0=='*'||LA15_0==':'||LA15_0=='<'||(LA15_0>='>' && LA15_0<='@')||LA15_0=='['||(LA15_0>=']' && LA15_0<='^')||LA15_0=='`'||LA15_0=='|'||(LA15_0>='~' && LA15_0<='\u00BF')||LA15_0=='\u00D7'||LA15_0=='\u00F7'||(LA15_0>='\u2000' && LA15_0<='\u303F')||(LA15_0>='\u3190' && LA15_0<='\u32FF')||(LA15_0>='\u3380' && LA15_0<='\u33FF')||(LA15_0>='\u3D2E' && LA15_0<='\u4DFF')||(LA15_0>='\uA000' && LA15_0<='\uF8FF')||(LA15_0>='\uFB00' && LA15_0<='\uFF60')||(LA15_0>='\uFFA0' && LA15_0<='\uFFFF')) ) {s = 34;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA15_32 = input.LA(1);

                        s = -1;
                        if ( ((LA15_32>='\u0000' && LA15_32<='\uFFFF')) ) {s = 80;}

                        else s = 34;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 15, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}