package com.tibco.cep.analytics.terr.io.utils;

import com.tibco.terr.TerrJava;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya Gawde
 * Date: 8/7/14
 * Time: 1:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TERRLifetime {

    //The methods in this class are for in-process computation.

    static final TERROutHandler OUT = new TERROutHandler();

    public static synchronized void checkTERREngine() throws Exception {

        if (!TerrJava.isEngineRunning()){
            TerrJava.setOutputHandler(OUT);
            TerrJava.startEngine();
        }

    }

    public long getFreeMemory(){

        return TerrJava.getJavaMemory()[0];
    }

    public long getTotalMemory(){

        return TerrJava.getJavaMemory()[1];
    }

    public long getMaxMemory(){

        return TerrJava.getJavaMemory()[2];
    }

    public void shutDown() throws Exception {

        TerrJava.stopEngine();
    }

}
