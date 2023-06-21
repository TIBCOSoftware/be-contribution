package com.tibco.cep.pattern.subscriber.impl.master;

import java.io.Serializable;
import java.util.Calendar;

import com.tibco.be.model.rdf.primitives.RDFBooleanTerm;
import com.tibco.be.model.rdf.primitives.RDFDateTimeTerm;
import com.tibco.be.model.rdf.primitives.RDFDoubleTerm;
import com.tibco.be.model.rdf.primitives.RDFIntegerTerm;
import com.tibco.be.model.rdf.primitives.RDFLongTerm;
import com.tibco.be.model.rdf.primitives.RDFPrimitiveTerm;
import com.tibco.be.model.rdf.primitives.RDFStringTerm;

/*
* Author: Ashwin Jayaprakash / Date: Jan 13, 2010 / Time: 2:25:34 PM
*/

public class TypeHelper implements Serializable {
    public static Class convertRDFtoJavaType(RDFPrimitiveTerm primitiveTerm) {
        if (primitiveTerm.getClass().equals(RDFStringTerm.class)) {
            return String.class;
        }
        else if (primitiveTerm.getClass().equals(RDFIntegerTerm.class)) {
            return Integer.TYPE;
        }
        else if (primitiveTerm.getClass().equals(RDFLongTerm.class)) {
            return Long.TYPE;
        }
        else if (primitiveTerm.getClass().equals(RDFDoubleTerm.class)) {
            return Double.TYPE;
        }
        else if (primitiveTerm.getClass().equals(RDFBooleanTerm.class)) {
            return Boolean.TYPE;
        }
        else if (primitiveTerm.getClass().equals(RDFDateTimeTerm.class)) {
            return Calendar.class;
        }

        return Object.class;
    }
}