package com.tibco.cep.analytics.terr.io.utils;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.terr.TerrJava;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya Gawde
 * Date: 8/5/14
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TERROutHandler implements TerrJava.OutputHandler {

    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(TERROutHandler.class);
    static final StringBuffer OUTPUT_BUFFER = new StringBuffer();


    @Override
    public void write(String s, boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.

        for(int i = 0; i < s.length();i++) {
            if (s.charAt(i)=='\n') {
                LOGGER.log(Level.DEBUG, "%s", OUTPUT_BUFFER.toString());
                OUTPUT_BUFFER.delete(0, OUTPUT_BUFFER.length());
            }
            else {
                OUTPUT_BUFFER.append(s.charAt(i));
            }
        }

    }
}
