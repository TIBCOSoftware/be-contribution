package com.tibco.cep.analytics.terr.io;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.cep.analytics.terr.io.utils.BEDataFrame;
import com.tibco.cep.analytics.terr.io.utils.BEDataList;
import com.tibco.cep.analytics.terr.io.utils.TERREngine;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.terr.TerrData;
import com.tibco.terr.TerrDataFrame;
import com.tibco.terr.TerrDouble;
import com.tibco.terr.TerrInteger;
import com.tibco.terr.TerrJavaRemote;
import com.tibco.terr.TerrList;
import com.tibco.terr.TerrLogical;
import com.tibco.terr.TerrString;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya
 * Date: 11/17/14
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TerrFunctionsDelegate {

    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(TerrFunctionsDelegate.class);

    static final String TERR_DOUBLE_CLASS = "com.tibco.terr.TerrDouble";
    static final String TERR_INTEGER_CLASS = "com.tibco.terr.TerrInteger";
    static final String TERR_STRING_CLASS = "com.tibco.terr.TerrString";
    static final String TERR_LOGICAL_CLASS = "com.tibco.terr.TerrLogical";
    static final String BE_DATA_LIST_CLASS = "com.tibco.cep.analytics.terr.io.utils.BEDataList";
    static final String BE_DATA_FRAME_CLASS = "com.tibco.cep.analytics.terr.io.utils.BEDataFrame";
    static final String TERR_LIST_CLASS = "com.tibco.terr.TerrList";
    static final String TERR_FRAME_CLASS = "com.tibco.terr.TerrDataFrame";

    static final String INT_ARRAY = "[I";
    static final String DOUBLE_ARRAY = "[D";
    static final String BOOLEAN_ARRAY = "[Z";
    static final String LONG_ARRAY = "[J";
    static final String CALENDAR_ARRAY ="[Ljava.util.Calendar;";
    static final String STRING_ARRAY = "[Ljava.lang.String;";

    static final String TWO_INT_ARRAY = "[[I";
    static final String TWO_DOUBLE_ARRAY = "[[D";
    static final String TWO_BOOLEAN_ARRAY = "[[Z";
    static final String TWO_LONG_ARRAY = "[[J";

    static final String STRING_CLASS = "java.lang.String";
    static final String INTEGER_CLASS = "java.lang.Integer";
    static final String DOUBLE_CLASS = "java.lang.Double";
    static final String BOOLEAN_CLASS = "java.lang.Boolean";
    static final String LONG_CLASS = "java.lang.Long";
    static final String CALENDAR_CLASS = "java.util.GregorianCalendar";

    static final String RESULT_VARIABLE_NAME = ".BE_TERR_result";
    static final String DATA_LIST_VARIABLE = ".BE_TERR_dl";
    static final String DATA_FRAME_VARIABLE = ".BE_TERR_df";
//    static final String MODEL_LIST_VARIABLE = "BE_TERR_ml";

    static final String EVALUATION_ERROR = "EvaluationError";
    static final String EXPRESSION_ERROR = "IncompleteExpression";
    static final String INCOMPLETE_STRING = "IncompleteString";
    static final String PARSER_ERROR = "ParserError";
    static final String ENGINE_OFF = "EngineNotRunning";
    static final String ENGINE_EXCEPTION = "EngineException";
    static final String ENGINE_QUIT = "Quit";

    static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    static final Map<String, TerrEnginePool> terr_engine_map = new ConcurrentHashMap<>();
    
    public static void checkErrorCode(String error, String lastError) {
        switch (error) {
        case EVALUATION_ERROR:
            throw new RuntimeException("EvaluationError\n" + lastError);

        case EXPRESSION_ERROR:
            throw new RuntimeException("Incomplete expression\n" + lastError);

        case INCOMPLETE_STRING:
            throw new RuntimeException("Incomplete string\n" + lastError);

        case PARSER_ERROR:
            throw new RuntimeException("ParserError\n" + lastError);

        case ENGINE_OFF:
            throw new RuntimeException("EngineNotRunning\n" + lastError);

        case ENGINE_EXCEPTION:
            throw new RuntimeException("Exception in Terr Engine \n" + lastError);

        case ENGINE_QUIT:
            throw new RuntimeException("Expression evaluated to engine q() command \n" + lastError);
        }
    }

    public static Object [] parseResult(TerrData res){
    	String resClassName = null;
    	if (res != null) {
    		resClassName = res.getClass().getName();
    	}
    	
    	if (resClassName != null) {
            switch (resClassName) {

            case TERR_DOUBLE_CLASS:{
                TerrDouble resDouble = (TerrDouble) res;
                Double arr[] = new Double[resDouble.data.length];
                for (int i = 0; i < resDouble.data.length; i++) {
                    arr[i] = resDouble.data[i];
                }
                return arr;
            }

            case TERR_INTEGER_CLASS:{
                TerrInteger resInt = (TerrInteger) res;
                Integer arr[] = new Integer[resInt.data.length];
                for (int i = 0; i < resInt.data.length; i++) {
                    arr[i] = resInt.data[i];
                }
                return arr;
            }

            case TERR_STRING_CLASS: {
                TerrString resStr = (TerrString) res;
                String arr[] = new String[resStr.data.length];
                for (int i = 0; i < resStr.data.length; i++) {
                    arr[i] = resStr.data[i];
                }
                return arr;
            }

            case TERR_LOGICAL_CLASS: {
                TerrLogical resLogical = (TerrLogical) res;
                Integer arr[] = new Integer[resLogical.data.length];
                for (int i = 0; i < resLogical.data.length; i++) {
                    //There are 3 boolean values in R
                    switch (resLogical.data[i]){
                        case 0:
                            arr[i] = 0;
                            break;

                        case 1:
                            arr[i] = 1;
                            break;

                        case -1 :
                            arr[i] = -1;
                            break;

                    }
                }
                return arr;
            }

            case TERR_LIST_CLASS: {
                Object [] arr = new Object[1];
                arr[0] = res;
                return arr;
            }

            case TERR_FRAME_CLASS: {
                Object [] arr = new Object[1];
                arr[0] = res;
                return arr;
            }

          }
    	}
        return null;
    }

    public static void clearTempMemory (TerrJavaRemote remote, int frames, int lists, boolean success) {
    	long TID = Thread.currentThread().getId(); 
    	try {
            for (int i = 0; i < frames ; i++) {
                remote.evaluateInteractive("rm("+DATA_FRAME_VARIABLE+ TID +i+")");
            }

            for (int i = 0; i < lists ; i++) {
                remote.evaluateInteractive("rm("+DATA_LIST_VARIABLE+ TID +i+")");
            }
            if (success) {
                remote.evaluateInteractive("rm("+RESULT_VARIABLE_NAME+ TID +")");
            }
        }
        catch (Exception e){
            LOGGER.log(Level.ERROR,"%s", e );
        }
    }

    public static Object[] invokeTERRFunction(String engineName, String functionName, Object... args)  {
        TERREngine terrEngine = getTerrEngineFromPool(engineName);
        boolean success = true;
        TerrJavaRemote remote = terrEngine.getRemote();
        StringBuffer evaluation = new StringBuffer();
        evaluation.append(functionName);
        evaluation.append("(");
        int listCount = 0, frameCount = 0;
        
        final long TID = Thread.currentThread().getId();
        
        try {
            if (args == null) {
                evaluation.append(")");
            } else {
                for (int i = 0 ;i < args.length ;i++) {
                    String check = args[i].getClass().getName();
                    String tempVar = "";
                    switch (check) {
                        case BE_DATA_LIST_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            BEDataList b = (BEDataList) args[i];
                            TerrList d = b.getDataList();
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case BE_DATA_FRAME_CLASS : {
                            tempVar = DATA_FRAME_VARIABLE + TID + frameCount;
                            BEDataFrame b = (BEDataFrame) args[i];
                            TerrDataFrame d = b.getDataFrame();
                            remote.setVariable(tempVar,d);
                            frameCount++;
                            break;
                        }

                        case STRING_CLASS : {
                        	//Commented the code to address JIRA BE-22421
                           // if(remote.getVariable((String)args[i]) == null){
                           //     throw new RuntimeException("Variable "+args[i]+" not present within TERR");
                           // }
                            tempVar = "\"" + (String) args[i] + "\"";
                            break;
                        }
                        
                        case STRING_ARRAY: {
                        	tempVar = DATA_LIST_VARIABLE + TID + listCount;
                        	TerrString s = new TerrString((String[])args[i]);
                        	remote.setVariable(tempVar, s);
                        	listCount++;
                        	break;
                        }

                        case TERR_LIST_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrList d = (TerrList) args[i];
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case TERR_FRAME_CLASS : {
                            tempVar = DATA_FRAME_VARIABLE + TID + frameCount;
                            TerrDataFrame d = (TerrDataFrame) args[i];
                            remote.setVariable(tempVar,d);
                            frameCount++;
                            break;
                        }

                        case CALENDAR_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            String time = SIMPLE_DATE_FORMAT.format(((Calendar) args[i]).getTime());
                            remote.setVariable(tempVar,new TerrString(time));
                            listCount++;
                            break;
                        }

                        case CALENDAR_ARRAY : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            Calendar [] arr = (Calendar []) args[i];
                            String [] time = new String[arr.length];
                            for(int j =0; j < arr.length; j++) {
                             time[j] = SIMPLE_DATE_FORMAT.format(((Calendar) arr[i]).getTime());
                            }
                            remote.setVariable(tempVar,new TerrString(time));
                            listCount++;
                            break;
                        }

                        case INT_ARRAY : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrInteger d = new TerrInteger((int [])args[i]);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;

                        }

                        case DOUBLE_ARRAY : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrDouble d = new TerrDouble((double [])args[i]);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case BOOLEAN_ARRAY : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrLogical d = new TerrLogical((boolean [])args[i]);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case LONG_ARRAY : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            long [] arr = (long []) args[i];
                            double [] arrDouble = new double[arr.length];
                            for (int j = 0, k =0 ; j< arr.length; j++, k++){
                                Long l = new Long (arr[k]);
                                arrDouble[j] = l.doubleValue();
                            }
                            TerrDouble d = new TerrDouble(arrDouble);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case TWO_INT_ARRAY : {
                            tempVar = DATA_FRAME_VARIABLE + TID +listCount;
                            TerrDataFrame d = new TerrDataFrame();
                            int c =0;
                            for(int [] a : (int [][])args[i]) {
                                d.data[c]= new TerrInteger(a);
                                c++;
                            }
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case TWO_DOUBLE_ARRAY : {
                            tempVar = DATA_FRAME_VARIABLE + TID +listCount;
                            TerrDataFrame d = new TerrDataFrame();
                            int c =0;
                            for(double [] a : (double [][])args[i]) {
                                d.data[c]= new TerrDouble(a);
                                c++;
                            }
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case TWO_LONG_ARRAY : {
                            tempVar = DATA_FRAME_VARIABLE + TID +listCount;
                            TerrDataFrame d = new TerrDataFrame();
                            int c =0;
                            for(long [] a : (long [][])args[i]) {
                                double [] arrDouble = new double[a.length];
                                for (int j = 0 ; j< a.length; j++){
                                    Long l = new Long (a[i]);
                                    arrDouble[j] = l.doubleValue();
                                }
                                d.data[c]= new TerrDouble(arrDouble);
                                c++;
                            }
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case TWO_BOOLEAN_ARRAY : {
                            tempVar = DATA_FRAME_VARIABLE + TID +listCount;
                            TerrDataFrame d = new TerrDataFrame();
                            int c =0;
                            for(boolean [] a : (boolean [][])args[i]) {
                                d.data[c]= new TerrLogical(a);
                                c++;
                            }
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case INTEGER_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrInteger d = new TerrInteger((int)args[i]);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case DOUBLE_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrDouble d = new TerrDouble((double)args[i]);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case LONG_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            Long l = new Long ((long)args[i]);
                            TerrDouble d = new TerrDouble(l.doubleValue());
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }

                        case BOOLEAN_CLASS : {
                            tempVar = DATA_LIST_VARIABLE + TID + listCount;
                            TerrLogical d = new TerrLogical((boolean)args[i]);
                            remote.setVariable(tempVar,d);
                            listCount++;
                            break;
                        }
                    }

                    if( i != 0) {
                        evaluation.append(",");
                    }
                    evaluation.append(tempVar);

                }
                evaluation.append(")");

            }
            String evaluationExpression = evaluation.toString();
            LOGGER.log(Level.DEBUG, "Evaluated expression:  %s", evaluationExpression);
            final String status = remote.evaluateInteractive(RESULT_VARIABLE_NAME + TID + "<-" + evaluationExpression);
            TerrData res = remote.getVariable(RESULT_VARIABLE_NAME+ TID);
            
            if (res == null && !status.equalsIgnoreCase("success")){
                success =false;
                checkErrorCode(status, remote.getLastErrorMessage());
            }
            return parseResult(res);
            
        } catch (Exception e){
            success = false;
            throw new RuntimeException(e);
        } finally {
            clearTempMemory(remote, frameCount, listCount,success);
        }
    }

    private static TERREngine getTerrEngineFromPool(String engineName) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        TERREngine terrEngine = pool.chooseTerrEngine();
        return terrEngine;
    }

    private static TerrEnginePool getTerrEnginePool(String engineName) {
        TerrEnginePool pool = terr_engine_map.get(engineName);
        if (pool == null) {
            throw new RuntimeException("Engine or Engine pool does not exist");
        }
        return pool;
    }

    public static boolean createEngine(String engineName) {
        return createEnginePool(1, engineName);
    }

    public static boolean isEngineCreated(String engineName) {
        if (terr_engine_map.get(engineName) == null) {
            return false;
        } else
            return true;
    }

    public static void deleteEngine(String engineName) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        try {
            pool.clearPool();
        } catch (Exception e) {
            // ignore the exception
            LOGGER.log(Level.DEBUG, "Deleted:  [%s]", engineName);
        }
        terr_engine_map.remove(engineName);
    }

    public static void startEngine(String engine) {
        TerrEnginePool pool = getTerrEnginePool(engine);
        try {
            pool.startEngines();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void stopEngine(String engineName) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        try {
            pool.stopEngines();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isEngineRunning(String engineName) {
        TERREngine terrEngine = getTerrEngineFromPool(engineName);
        try {
            return terrEngine.checkEngine();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getVariable(String engineName, String var) {
        TERREngine terrEngine = getTerrEngineFromPool(engineName);
        TerrJavaRemote remote = terrEngine.getRemote();
        try {
            return parseResult(remote.getVariable(var));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLastErrorMessage(String engineName) {
        TERREngine terrEngine = getTerrEngineFromPool(engineName);
        TerrJavaRemote remote = terrEngine.getRemote();
        try {
            return remote.getLastErrorMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void interrupt(String engineName) {
        TERREngine terrEngine = getTerrEngineFromPool(engineName);
        TerrJavaRemote remote = terrEngine.getRemote();
        try {
            remote.interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTerrHome(String engineName, String filePath) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        try {
            pool.setTerrHome(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setJavaHome(String engineName, String filePath) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        try {
            pool.setJavaHome(filePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setJavaOptions(String engineName, String options) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        try {
            pool.setJavaOptions(options);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setEngineParameters(String engineName, String parameters) {
        TerrEnginePool pool = getTerrEnginePool(engineName);
        try {
            pool.setEngineParameters(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void engineExecute(String engineName, String RScript, boolean interactive) {
        try {
            TerrEnginePool pool = getTerrEnginePool(engineName);
            pool.engineExecute(RScript, interactive);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setVariable(String engineName, String var, Object v) {
        try {
            TerrEnginePool pool = getTerrEnginePool(engineName);
            pool.setVariable(var, v);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteVariable(String engineName, String var) {
        try {
            TerrEnginePool pool = getTerrEnginePool(engineName);
            pool.deleteVariable(var);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean createEnginePool(int poolSize, String poolName) {
        if (poolSize < 0) {
            throw new IllegalArgumentException("The pool size can not be less than zero");
        }
        if (terr_engine_map.get(poolName) != null) {
            String logPrefix = "";
            if (poolSize == 1) {
                logPrefix = "Engine Name";
            } else {
                logPrefix = "Pool Name";
            }
            LOGGER.log(Level.INFO, "%s already in use", logPrefix);
            return false;
        } else {
            terr_engine_map.put(poolName, new TerrEnginePool(poolSize, poolName));
            return true;
        }
    }
}
