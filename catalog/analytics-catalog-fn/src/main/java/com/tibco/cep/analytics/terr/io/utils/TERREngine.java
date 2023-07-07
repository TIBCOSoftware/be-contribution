package com.tibco.cep.analytics.terr.io.utils;

import com.tibco.terr.TerrJavaRemote;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya Gawde
 * Date: 8/19/14
 * Time: 1:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TERREngine {
    final TERROutHandler OUT;
    TerrJavaRemote engine;
    public TERREngine() {
        OUT = new TERROutHandler();
        engine = new TerrJavaRemote();
        engine.setOutputHandler(OUT);
    }

    public boolean checkEngine() throws Exception {
        return engine.isEngineRunning();
    }

    public void start() throws Exception {
        engine.startEngine();
    }

    public void stop() throws Exception {
        engine.stopEngine();
    }

    public final TerrJavaRemote getRemote() {
        return this.engine;
    }

}
