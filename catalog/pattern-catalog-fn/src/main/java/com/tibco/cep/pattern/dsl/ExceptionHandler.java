package com.tibco.cep.pattern.dsl;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;

/*
* Author: Anil Jeswani / Date: Nov 16, 2009 / Time: 2:42:14 PM
*/

public class ExceptionHandler {

    String message;
    List<LanguageException> exceptionList = new ArrayList<LanguageException>();

    public void addException(LanguageException exception) throws RecognitionException {
        if (message == null) {
            message = exception.getMessage();
            exceptionList.add(exception);
            throw new RecognitionException();
        }

        if (!message.equalsIgnoreCase(exception.getMessage())) {
            message += exception.getMessage();
            exceptionList.add(exception);
        }
    }

    public LanguageException getException() {
        if (message == null || exceptionList.size() == 1) return exceptionList.get(0);

        LanguageException languageException;
        if (message != null) {
            languageException = new LanguageException(message);
        } else {
            return null;
        }
        //return first exception
        return exceptionList == null ? null : exceptionList.get(0);
    }
}
