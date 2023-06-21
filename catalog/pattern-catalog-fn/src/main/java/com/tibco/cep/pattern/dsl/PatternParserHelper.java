package com.tibco.cep.pattern.dsl;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.Tree;

import com.tibco.cep.pattern.integ.impl.dsl2.Definition;

public class PatternParserHelper {

    protected String input;

    protected Tree tree;
    protected patternLexer lexer;
    protected patternParser parser;
    protected Definition definition;

    public PatternParserHelper(String input) {
        this.input = input;
    }

    public Definition parse() throws LanguageException {

        try {
            CharStream charStream = new ANTLRNoCaseStringStream(input);
            lexer = new patternLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser = new patternParser(tokens);
            tree = (CommonTree) parser.main().getTree();
        } catch (Exception e) {
            if (parser.getExceptionHandler() == null)
                throw new LanguageException(e);
        }

        if (tree == null) {
            LanguageException exception = parser.getExceptionHandler().getException();
            if (exception != null) throw exception;
        }

        PatternGenerator patternGenerator = new PatternGenerator(tree);
        definition = patternGenerator.execute();

        return definition;
    }
}
