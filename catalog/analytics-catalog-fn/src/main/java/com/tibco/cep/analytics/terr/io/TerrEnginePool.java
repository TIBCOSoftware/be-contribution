package com.tibco.cep.analytics.terr.io;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import com.tibco.cep.analytics.terr.io.utils.TERREngine;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.terr.TerrDataFrame;
import com.tibco.terr.TerrDouble;
import com.tibco.terr.TerrInteger;
import com.tibco.terr.TerrJavaRemote;
import com.tibco.terr.TerrList;
import com.tibco.terr.TerrLogical;
import com.tibco.terr.TerrString;

public class TerrEnginePool {

    private TERREngine[] terrEnginePool;

    private AtomicInteger currEngine;

    private final int poolSize;

    private final Map<Long, TERREngine> thread_engine_map;

    private final String name;

    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(TerrEnginePool.class);

    public TerrEnginePool(int numberOfEngines, String poolName) {
        poolSize = numberOfEngines;
        this.name = poolName;
        thread_engine_map = new ConcurrentHashMap<>();
        currEngine = new AtomicInteger();
        terrEnginePool = new TERREngine[poolSize];
        for (int i = 0; i < poolSize; i++) {
            terrEnginePool[i] = new TERREngine();
        }
    }

    public TERREngine chooseTerrEngine() {
        if (poolSize < 1) {
            throw new RuntimeException("No TERR engines are created");
        }
        TERREngine rEngine = null;
        long threadId = Thread.currentThread().getId();
        if (thread_engine_map.containsKey(threadId)) {
            rEngine = thread_engine_map.get(threadId);
        } else {
            int index = currEngine.getAndIncrement();
            if (index >= poolSize) {
                currEngine.set(0);
                index = 0;
            }
            rEngine = terrEnginePool[index];
            thread_engine_map.put(threadId, rEngine);
        }
        return rEngine;
    }

    public synchronized void startEngines() throws Exception {
        for (TERREngine engine : terrEnginePool) {
            engine.start();
        }
        LOGGER.log(Level.DEBUG, "Started %s [%s]", getLogString(), this.name);
    }

    public void stopEngines() throws Exception {
        for (TERREngine engine : terrEnginePool) {
            if (engine != null) {
                engine.stop();
            }
        }
        LOGGER.log(Level.DEBUG, "Stopped %s [%s]", getLogString(), this.name);
    }
    
    public void clearPool() throws Exception {
        stopEngines();
        terrEnginePool = null;
    }

    public synchronized void setTerrHome(String filePath) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            engine.getRemote().setTerrHome(filePath);
        }
        LOGGER.log(Level.DEBUG, "Set Terr home [%s] in %s [%s]", filePath, getLogString(), this.name);
    }

    public synchronized void setJavaHome(String filePath) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            engine.getRemote().setJavaHome(filePath);
        }
        LOGGER.log(Level.DEBUG, "Set java home [%s] in %s [%s]", filePath, getLogString(), this.name);
    }

    public synchronized void setJavaOptions(String options) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            engine.getRemote().setJavaOptions(options);
        }
        LOGGER.log(Level.DEBUG, "Set java options [%s] in %s [%s]", options, getLogString(), this.name);
    }

    public synchronized void setEngineParameters(String parameters) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            engine.getRemote().setEngineParameters(parameters);
        }
        LOGGER.log(Level.DEBUG, "Set Engine parameters [%s] in %s [%s]", parameters, getLogString(), this.name);
    }

    public synchronized void engineExecute(String RScript, boolean interactive) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            TerrJavaRemote remote = engine.getRemote();
            String status = remote.engineExecute(RScript, interactive);
            if (status != null && !status.isEmpty())
                throw new RuntimeException(status);
        }
        LOGGER.log(Level.DEBUG, "Executed the script [%s] in %s [%s]", RScript, getLogString(), this.name);
    }

    public synchronized void setVariable(String var, Object v) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            this.setVariable(engine, var, v);
        }
        LOGGER.log(Level.DEBUG, "Set variable [%s] in %s [%s]", var, getLogString(), this.name);
    }

    private void setVariable(TERREngine terrEngine, String var, Object v) throws Exception {
        TerrJavaRemote remote = terrEngine.getRemote();
        if (v instanceof TerrDataFrame) {
            remote.setVariable(var, (TerrDataFrame) v);
        }

        else if (v instanceof TerrList) {
            remote.setVariable(var, (TerrList) v);
        }

        else if (v instanceof int[]) {
            remote.setVariable(var, new TerrInteger((int[]) v));
        }

        else if (v instanceof Integer) {
            remote.setVariable(var, new TerrInteger((Integer) v));
        }

        else if (v instanceof double[]) {
            remote.setVariable(var, new TerrDouble((double[]) v));
        }

        else if (v instanceof Double) {
            remote.setVariable(var, new TerrDouble((Double) v));
        }

        else if (v instanceof long[]) {
            long[] arr = (long[]) v;
            double[] arrDouble = new double[arr.length];
            for (int j = 0; j < arr.length; j++) {
                Long t = new Long(arr[j]);
                arrDouble[j] = t.doubleValue();
            }
            remote.setVariable(var, new TerrDouble(arrDouble));
        }

        else if (v instanceof Long) {
            Long t = new Long((long) v);
            remote.setVariable(var, new TerrDouble(t.doubleValue()));
        }

        else if (v instanceof String[]) {
            remote.setVariable(var, new TerrString((String[]) v));
        }

        else if (v instanceof String) {
            remote.setVariable(var, new TerrString((String) v));
        }

        else if (v instanceof boolean[]) {
            remote.setVariable(var, new TerrLogical((boolean[]) v));
        }

        else if (v instanceof Boolean) {
            remote.setVariable(var, new TerrLogical((Boolean) v));
        }

        else if (v instanceof Calendar[]) {
            String[] arrString = new String[((Calendar[]) v).length];
            for (int j = 0; j < ((Calendar[]) v).length; j++) {
                Calendar calendar = (Calendar) ((Object[]) v)[j];
                arrString[j] = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(calendar.getTime());
            }
            remote.setVariable(var, new TerrString(arrString));
        }

        else if (v instanceof Calendar) {
            String time = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(((Calendar) v).getTime());
            remote.setVariable(var, new TerrString(time));
        }
    }

    public void deleteVariable(String var) throws Exception {
        for (TERREngine engine : terrEnginePool) {
            TerrJavaRemote remote = engine.getRemote();
            final String status = engine.getRemote().evaluateInteractive("rm(" + var + ")");
            if (!status.equalsIgnoreCase("success")) {
                TerrFunctionsDelegate.checkErrorCode(status, remote.getLastErrorMessage());
            }
        }
        LOGGER.log(Level.DEBUG, "Deleted the variable [%s] in %s [%s]", var, getLogString(), this.name);
    }

    private String getLogString() {
        if (this.poolSize > 1) {
            return "engine pool:";
        } else {
            return "engine:";
        }
    }
}
