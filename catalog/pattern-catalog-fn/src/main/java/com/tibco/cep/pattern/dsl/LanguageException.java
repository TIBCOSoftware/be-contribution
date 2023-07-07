package com.tibco.cep.pattern.dsl;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:41:45 PM
*/

public class LanguageException extends Exception {

    int column;
    String input;
    String message;

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public LanguageException(String message) {
        this.message = message;
    }

    public LanguageException(String message, Throwable cause) {
        this.message = message;
        this.initCause(cause);
    }

    public LanguageException(String input, int column) {
        this.input = input;
        this.column = column;
    }

    public LanguageException(String message, String input, int column) {
        this.input = input;
        this.column = column;
        this.message = message;
    }

    public LanguageException(Throwable cause) {
        this.initCause(cause);
    }

    public LanguageException(String message, String input, int column, Throwable cause) {
        this.input = input;
        this.column = column;
        this.message = message;
        this.initCause(cause);
    }

    public String getMessage() {
        return message;
    }
}
