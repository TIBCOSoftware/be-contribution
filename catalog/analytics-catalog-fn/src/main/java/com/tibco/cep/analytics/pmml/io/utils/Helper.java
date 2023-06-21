package com.tibco.cep.analytics.pmml.io.utils;

import com.tibco.cep.runtime.model.element.Concept;
import com.tibco.cep.runtime.model.element.Property;
import com.tibco.cep.runtime.model.element.PropertyAtom;
import com.tibco.cep.runtime.model.element.impl.property.simple.*;
import com.tibco.cep.runtime.model.event.SimpleEvent;

/**
 * Created with IntelliJ IDEA.
 * User: dtsai
 * Date: 2/26/13
 * Time: 1:50 PM
 */
public class Helper {
    public static Object[] toParameters(Concept cept) throws Exception {
        Property[] props = cept.getProperties();
        Object[] parameters = new Object[props.length * 2];

        int index = 0;
        for (Property prop : props) {
            if (prop instanceof PropertyAtom) {
                PropertyAtom pas = (PropertyAtom) prop;
                String name = pas.getName();
                if (pas instanceof PropertyAtomStringSimple) {
                    parameters[index++] = name;
                    parameters[index++] = ((PropertyAtomStringSimple) pas).getString();
                } else if (pas instanceof PropertyAtomIntSimple) {
                    parameters[index++] = name;
                    parameters[index++] = ((PropertyAtomIntSimple) pas).getInt();
                } else if (pas instanceof PropertyAtomLongSimple) {
                    parameters[index++] = name;
                    parameters[index++] = ((PropertyAtomLongSimple) pas).getLong();
                } else if (pas instanceof PropertyAtomDoubleSimple) {
                    parameters[index++] = name;
                    parameters[index++] = ((PropertyAtomDoubleSimple) pas).getDouble();
                } else if (pas instanceof PropertyAtomBooleanSimple) {
                    parameters[index++] = name;
                    parameters[index++] = ((PropertyAtomBooleanSimple) pas).getBoolean();
                } else if (pas instanceof PropertyAtomDateTimeSimple) {
                    parameters[index++] = name;
                    parameters[index++] = ((PropertyAtomDateTimeSimple) pas).getDateTime();
                }
                //System.out.print("Name: "+name+" Value:"+ parameters[index-1]+"\n");
            }
        }
        return parameters;
    }

    public static Object[] toParameters(SimpleEvent event) throws Exception {
        String[] fields = event.getPropertyNames();
        Object[] parameters = new Object[fields.length * 2];

        int index = 0;
        for (String field : fields) {
            parameters[index] = field;
            parameters[index + 1] = event.getProperty(field);
            index += 2;
            // System.out.print("Name: "+field+" Value:"+ parameters[index-1]+"\n");
        }
        return parameters;
    }
}
