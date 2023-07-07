package com.tibco.cep.analytics.terr.io;

import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya Gawde
 * Date: 8/21/14
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */

@BEPackage(
        catalog = "CEP Analytics",
        category = "Analytics.TERR.DataFrame",
        synopsis = "Functions to access TERR.")

public class FrameFunctions {

    @BEFunction(
            name = "debugOut",
            synopsis = "Prints data frame",
            signature = "void debugOut(Object frame)",
            params = {
                    @FunctionParamDescriptor(name = "frame", type = "Object", desc = "Terr Frame Object")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "", desc = ""),
            version = "5.2",
            see = "",
            mapper = @BEMapper(),
            description = "Prints the data frame",
            cautions = "In case a complex TERR datastructure like another frame or list exists inside a frame, then the debugOut function will just print the object reference of it.<br/>The various frame/list related functions can be then used to retrieve data from these datastructures.",
            fndomain = {ACTION},
            example = "Analytics.DataFrame.debugOut(frame);"
    )
    public static void debugOut(Object frame)  {
        FrameFunctionsDelegate.debugOut(frame);

    }

    @BEFunction(
            name = "createFrame",
            synopsis = "Calls the Terr engine to create data frame",
            signature = "Object createFrame(String [] names, Object [] data)",
            params = {
                    @FunctionParamDescriptor(name = "names", type = "String []", desc = "Array of column names"),
                    @FunctionParamDescriptor(name = "data", type = "Object []", desc = "Data array")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
            version = "5.2",
            see = "",
            mapper = @BEMapper(),
            description = "Creates and returns a new terr frame object.",
            cautions = "none",
            fndomain = {ACTION},
            example = "Object cust = Analytics.DataFrame.createFrame(names, data);"
    )
    public static Object createFrame(String [] names, Object [] objects)  {

        return FrameFunctionsDelegate.createFrame(names, objects);

    }
    
    @BEFunction(
    		name = "getColumnCount",
    		synopsis = "Returns the total number of columns present in the passed dataframe object",
    		signature = "int getColumnCount(Object dataFrame)",
    		params = {
    				@FunctionParamDescriptor(name="dataFrame", type="Object", desc="A TerrDataFrame type Object")
    		},
    		freturn = @FunctionParamDescriptor(name="", type="int", desc="Number of columns present in the passed TerrDataFrame Object")
    		,
    		version = "5.2",
    		see = "",
    		mapper = @BEMapper(),
    		description = "Returns the total number of columns present in the passed dataframe object",
    		cautions = "none",
    		fndomain = {ACTION},
    		example = "Object cust = Analytics.DataFrame.createFrame(names, data);<br/>int numberOfColumns = Analytics.DataFrame.getColumnCount(cust);"
    )
    public static int getColumnCount(Object dataFrame){
    	return FrameFunctionsDelegate.getColumnCount(dataFrame); 
    }
    
    @BEFunction(
    		name = "getColumn",
    		synopsis = "Returns the column specified by the column number from the data frame object.<br/>If the column number specified is incorrect returns null value.",
    		signature = "Object[] getColumn(Object dataFrame, int columnIndex)",
    		params = {
    				@FunctionParamDescriptor(name="dataFrame", type="Object", desc="A TerrDataFrame type of Object"),
    				@FunctionParamDescriptor(name="columnIndex", type="int", desc="The column number to be retrieved from the data frame")
    				
    		},
    		freturn = @FunctionParamDescriptor(name="", type="Object[]", desc="The column retrieved from the TerrDataFrame Object.<br/>If an incorrect column number is passed then a null Object is returned."),
    		version = "5.2",
    		see = "",
    		mapper = @BEMapper(),
    		description = "Returns the column specified by the column number from the data frame object.<br/>If the column number specified is incorrect returns null value.",
    		cautions = "none",
    		fndomain = {ACTION},
    		example = "Object cust = Analytics.DataFrame.createFrame(names, data);<br/>int columnNumber = 1;<br/>Object[] col1 = Analytics.DataFrame.getColumn(cust,columnNumber);<br/>When the getColumn returned is of type Factor, then the returned Object[] has a String array and an Integer array as the values returned<br/>Usage when the column is a factor should be as follows:-<br/>Object[] data = Analytics.DataFrame.getColumn(df,1);<br/>String[] data0 = data[0];<br/>int[] data1 = data[1];<br/>The array data0 contains all unique elements and the array data1 represents the Levels.<br/>"
    )
    public static Object[] getColumn(Object dataFrame, int columnIndex){
    	return FrameFunctionsDelegate.getColumn(dataFrame, columnIndex);
    }
    
    @BEFunction(
    		name = "getColumnType",
    		synopsis = "Returns the column type for the column number specified from the data frame object.<br/>If the column number specified is incorrect returns null value.",
    		signature = "String getColumnType(Object dataFrame, int columnNumber)",
    		params = {
    				@FunctionParamDescriptor(name="dataFrame", type="Object", desc="A TerrDataFrame type of Object"),
    				@FunctionParamDescriptor(name="columnIndex", type="int", desc="The column number whose type needs to retrieved from the data frame")
    		},
    		freturn = @FunctionParamDescriptor(name="", type="String", desc="Returns the column type for the column number specified from the data frame object.<br/>If the column number specified is incorrect returns null value."),
    		version = "5.2",
    		see = "",
    		mapper = @BEMapper(),
    		description = "Returns the column type for the column number specified from the data frame object.<br/>If the column number specified is incorrect returns null value.",
    		cautions = "none",
    		fndomain = {ACTION},
    		example = "Object cust = Analytics.DataFrame.createFrame(names, data);<br/>int columnNumber = 1;<br/>String columnType = Analytics.DataFrame.getColumnType(dataFrame, columnNumber);"
    )
    public static String getColumnType(Object dataFrame, int columnIndex){
    	return FrameFunctionsDelegate.getColumnType(dataFrame, columnIndex);
    }

}
