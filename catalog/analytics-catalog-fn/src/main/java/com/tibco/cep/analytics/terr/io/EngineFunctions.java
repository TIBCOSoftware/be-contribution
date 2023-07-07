package com.tibco.cep.analytics.terr.io;

import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;

import static com.tibco.be.model.functions.FunctionDomain.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya
 * Date: 1/7/15
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */

@BEPackage(
        catalog = "CEP Analytics",
        category = "Analytics.TERR.Engine",
        synopsis = "Functions to access TERR.")

public class EngineFunctions {

    @BEFunction(
        name = "createEngine",
        signature = "boolean createEngine(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "boolean", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Creates an instance of TERR Engine and returns true if successful.",
        cautions = "none",
        fndomain = {ACTION},
        example = "boolean val = Analytics.Engine.createEngine(\"engine1\");"
    )
    public static boolean createEngine(String engineName) {
        return TerrFunctionsDelegate.createEngine(engineName);
    }

    @BEFunction(
        name = "engineExists",
        signature = "boolean engineExists(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "boolean", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Checks if the TERR engine or engine pool exists.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = "Analytics.Engine.engineExists(\"engine1\");"
    )
    public static boolean engineExists(String engineName) {
        return TerrFunctionsDelegate.isEngineCreated(engineName);
    }

    @BEFunction(
        name = "deleteEngine",
        signature = "void deleteEngine(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Deletes instance of TERR Engine or Engine pool. If engine pool name is passed as a parameter all the engines will be deleted in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.deleteEngine(\"engine1\");"
    )
    public static void deleteEngine(String engineName) {
        TerrFunctionsDelegate.deleteEngine(engineName);
    }

    @BEFunction(
        name = "startEngine",
        signature = "void startEngine(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Starts the given engine or engine pool.",
        cautions = "A given engine or engine pool should be started only once. If engine pool name is passed as a parameter, all the engines will be started in the engine pool",
        fndomain = {ACTION},
        example = "Analytics.Engine.startEngine(\"engine1\");"
    )
    public static void  startEngine(String engine) {
        TerrFunctionsDelegate.startEngine(engine);
    }

    @BEFunction(
        name = "stopEngine",
        signature = "void stopEngine(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Stops the given engine. If engine pool name is passed as a parameter, all the engines will be stopped in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.stopEngine(\"engine1\");"
    )

    public static void stopEngine(String engineName) {
        TerrFunctionsDelegate.stopEngine(engineName);
    }

    @BEFunction(
        name = "isEngineRunning",
        signature = "boolean isEngineRunning(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "boolean", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "returns true if the engine is ON or false when it is OFF. If engine pool name is passed as a parameter, the status will be returned from Terr engine assigned to the thread invoking this function.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = "boolean status = Analytics.Engine.isEngineRunning(\"engine1\");"
    )

    public static boolean  isEngineRunning(String engineName) {
        return TerrFunctionsDelegate.isEngineRunning(engineName);
    }

    @BEFunction(
        name = "getVariable",
        signature = "Object[] getVariable(String engine, String var)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "var", type = "String", desc = "Variable Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object[]", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Gets the variable from TERR. If engine pool name is passed as a parameter, the variable value will be returned from Terr engine assigned to the thread invoking this function.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = "Object[] var = Analytics.Engine.getVariable(\"engine1\"' \"var\");"
    )
    public static Object  getVariable(String engineName, String var) {
        return TerrFunctionsDelegate.getVariable(engineName, var);
    }
    
    @BEFunction(
            name = "deleteVariable",
            signature = "void deleteVariable(String engine, String var)",
            params = {
                    @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                    @FunctionParamDescriptor(name = "var", type = "String", desc = "Variable Name")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
            version = "5.4",
            see = "",
            mapper = @BEMapper(),
            description = "Deletes the variable from TERR Engine; Throws an exception if it fails. Ignores (warning message logged at debug level) if the variable does not exist. If engine pool name is passed as a parameter, it will delete the variable from all engines in the pool",
            cautions = "none",
            fndomain = {ACTION, CONDITION, QUERY},
            example = "Analytics.Engine.deleteVariable(\"engine1\"' \"var\");"
        )
        public static void deleteVariable(String engineName, String var) {
             TerrFunctionsDelegate.deleteVariable(engineName, var);
        }

    @BEFunction(
        name = "getLastErrorMessage",
        signature = "String getLastErrorMessage(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "String", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Gets the last error message of the engine. If engine pool name is passed as a parameter, the error message will be retrieved from Terr engine assigned to the thread invoking this function.",
        cautions = "none",
        fndomain = {ACTION, CONDITION, QUERY},
        example = "String error = Analytics.Engine.getLastErrorMessage(\"engine1\");"
    )
    public static String  getLastErrorMessage(String engineName) {
        return TerrFunctionsDelegate.getLastErrorMessage(engineName);
    }

    @BEFunction(
        name = "interrupt",
        signature = "void interrupt(String engine)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "interrupt the given engine",
        cautions = " If the interrupt method is called while the TERR engine is executing an expression, this execution is interrupted. If engine pool name is passed as a parameter, the execution will be interrupted in the Terr engine assigned to the thread invoking this function.",
        fndomain = {ACTION},
        example = "Analytics.Engine.interrupt(\"engine1\");"
    )
    public static void  interrupt(String engineName) {
        TerrFunctionsDelegate.interrupt(engineName);
    }

    @BEFunction(
        name = "setTerrHome",
        signature = "void setTerrHome(String engine, String Path)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "Path", type = "String", desc = "Terr Home Path")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Sets the path to the base of the TERR installation (the directory containing subdirectories bin, library, and so on) that will be used for the spawned TERR engine.If engine pool name is passed as a parameter, the path will be set to all the engines in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.setTerrHome(\"engine1\", terr_home_path);"
    )
    public static void  setTerrHome(String engineName, String filePath) {
        TerrFunctionsDelegate.setTerrHome(engineName, filePath);
    }
    
    @BEFunction(
        name = "setJavaHome",
        signature = "void setJavaHome(String engine, String Path)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "Path", type = "String", desc = "Java Home Path")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Sets the path to Java Home.If engine pool name is passed as a parameter, the java home will be set to all the engines in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.setJavaHome(\"engine1\", java_home_path);"
    )
    public static void  setJavaHome(String engineName, String filePath) {
        TerrFunctionsDelegate.setJavaHome(engineName, filePath);
    }
    
    @BEFunction(
        name = "setJavaOptions",
        signature = "void setJavaOptions(String engine, String Options)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "Options", type = "String", desc = "Java Options")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Set the java options that get used when the TERR engine starts. Note - this API has no effect on an already started TERR engine.If engine pool name is passed as a parameter, the java options will be set to all the engines in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.setJavaOptions(\"engine1\", \"-Xdebug -Xrunjdwp:transport=dt_socket,address=4444,server=y,suspend=n\");"
    )
    public static void  setJavaOptions(String engineName, String options) {
        TerrFunctionsDelegate.setJavaOptions(engineName, options);
    }

    @BEFunction(
        name = "setEngineParameters",
        signature = "void setEngineParameters(String engine, String Parameters)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "Parameters", type = "String", desc = "Engine Parameters")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Set the engine parameters that control the initialization and execution of the spawned TERR engine.If engine pool name is passed as a parameter, the engine parameters will be set to all the engines in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.setEngineParameters(\"engine1\", \"Session.SaveHistory\");"
    )
    public static void  setEngineParameters(String engineName, String parameters) {
        TerrFunctionsDelegate.setEngineParameters(engineName,parameters);
    }

    @BEFunction(
        name = "setVariable",
        signature = "void setVariable(String engine, String var, Object v)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "var", type = "String", desc = "Variable Name"),
                @FunctionParamDescriptor(name = "v", type = "Object", desc = "Object that needs to be set to given variable name")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Sets the variable within TERR.If engine pool name is passed as a parameter, the variable will be set in all the engines in the engine pool",
        cautions = "none",
        fndomain = {ACTION},
        example = "Analytics.Engine.setVariable(\"engine1\", \"varName\", v);"
    )
    public static void  setVariable(String engineName, String var, Object v) {
        TerrFunctionsDelegate.setVariable(engineName, var, v);
    }
    
    

    @BEFunction(
        name = "invokeTERRFunction",
        signature = "Object[] invokeTERRFunction(String engine, String functionName, Object... args)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "functionName", type = "String", desc = "Function Name"),
                @FunctionParamDescriptor(name = "args", type = "Object...", desc = "Element of Object.. should be String if it is TERR variable name or object if within BE node. It should be null if function doesn't take arguments"),
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Object[]", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Calls the Terr engine and returns the function results.If engine pool name is passed as a parameter, the function will be executed in the Terr engine assigned to the thread invoking this function.",
        cautions = "Boolean values are returned in int format since there are 3 types of R: TRUE(1), FALSE(0), NA(-1)" +
                "This function should be used after the TERR function is made known to TERR using engineExecute() " +
        		"\nWhen trying to pass non-primitive arrays, example DateTime[] or String[], then typecasting to Object is necessary<br/>For example:-<br/>String[] arr = {\"Hello\",\"World\"};<br/>Object myarr = arr;<br/>Analytics.Engine.invokeTERRFunction(\"TerrEngineName\",\"TerrFunctionName\",myarr);<br/>",
        fndomain = {ACTION},
        example = "Analytics.Model.invokeTERRFunction(\"engine1\", \"testFunction\", args);<br/>When the argument being passed is an array of Objects then it is needed to type case it to Object before passing it to invokeTERRFunction method.<br/>For example:-<br/>DateTime[] myDate = {DateTime.now(),DateTime.addDay(DateTime.now(),1);<br/>Object myObjDate = myDate;<br/>terrResult = Analytics.Engine.invokeTERRFunction(\"TerrEngine\",\"getVector\",myObjDate);<br/>};"
    )
    public static Object[] invokeTERRFunction(String engineName, String functionName, Object... args)  {
        return TerrFunctionsDelegate.invokeTERRFunction(engineName, functionName, args);
    }

    @BEFunction(
        name = "engineExecute",
        signature = "void engineExecute(String engine, String Rscript, boolean interactive)",
        params = {
                @FunctionParamDescriptor(name = "engine", type = "String", desc = "Engine or Engine Pool Name"),
                @FunctionParamDescriptor(name = "RScript", type = "String", desc = "R Script to be evaluated"),
                @FunctionParamDescriptor(name = "interactive", type = "Boolean", desc = " If the argument interactive is true, the expression is evaluated as if it was in an interactive console")
        },
        freturn = @FunctionParamDescriptor(name = "", type = "Void", desc = ""),
        version = "5.2",
        see = "",
        mapper = @BEMapper(),
        description = "Parse and evaluate an expression in the given TERR engine.If engine pool name is passed as a parameter, the expression will be evaluated in all the engines in the engine pool ",
        cautions = "Use try catch block with getLastErrorMessage() inside catch to get detailed error message",
        fndomain = {ACTION},
        example = "Analytics.Model.engineExecute(\"engine1\", \"a<-3\", true);"
    )
    public static void engineExecute (String engineName, String RScript, boolean interactive){
        TerrFunctionsDelegate.engineExecute(engineName, RScript, interactive);
    }
    @BEFunction(
            name = "createEnginePool",
            signature = "boolean createEnginePool(int poolSize, String poolName)",
            params = {
                    @FunctionParamDescriptor(name = "poolSize", type = "int", desc = "Number of engines to create"),
                    @FunctionParamDescriptor(name = "poolName", type = "String", desc = "Name of the engine pool")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "boolean", desc = ""),
            version = "5.4",
            see = "",
            mapper = @BEMapper(),
            description = "Create a pool of TERR Engines with given size and name. This pool will be shared among the BE threads",
            cautions = "none",
            fndomain = {ACTION},
            example = "boolean val = Analytics.Engine.createEnginePool(5, \"mypool\");"
        )
    public static boolean createEnginePool(int poolSize, String poolName) {
        return TerrFunctionsDelegate.createEnginePool(poolSize,poolName);
    }
}
