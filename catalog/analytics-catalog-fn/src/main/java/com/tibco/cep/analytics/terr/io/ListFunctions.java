package com.tibco.cep.analytics.terr.io;

import com.tibco.be.model.functions.BEFunction;
import com.tibco.be.model.functions.BEMapper;
import com.tibco.be.model.functions.BEPackage;
import com.tibco.be.model.functions.FunctionParamDescriptor;
import com.tibco.cep.runtime.model.element.Concept;

import static com.tibco.be.model.functions.FunctionDomain.ACTION;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya Gawde
 * Date: 8/21/14
 * Time: 3:48 PM
 * To change this template use File | Settings | File Templates.
 */

@BEPackage(
        catalog = "CEP Analytics",
        category = "Analytics.TERR.DataList",
        synopsis = "Functions to access TERR.")

public class ListFunctions {

    @BEFunction(
            name = "debugOut",
            synopsis = "Prints datalist",
            signature = "void debugOut(Object list)",
            params = {
                    @FunctionParamDescriptor(name = "list", type = "Object", desc = "Terr List Object")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "void", desc = ""),
            version = "5.2",
            see = "",
            mapper = @BEMapper(),
            description = "Prints the data list",
            cautions = "In case a complex TERR datastructure like another frame or list exists inside a list, then the debugOut function will just print the object reference of it.<br/>The various frame/list related functions can be then used to retrieve data from these datastructures.",
            fndomain = {ACTION},
            example = "Analytics.DataList.debugOut(list);"
    )

    public static void debugOut(Object list)  {
        ListFunctionsDelegate.debugOut(list);

    }

    @BEFunction(
            name = "createList",
            synopsis = "Calls the Terr engine to create datalist",
            signature = "Object createList(String [] names, Object [] data)",
            params = {
                    @FunctionParamDescriptor(name = "names", type = "String []", desc = "Array of column names<br/>If an empty array is provided can not create a List and a null value will be returned."),
                    @FunctionParamDescriptor(name = "data", type = "Object []", desc = "Data array")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = "If the names is empty, then a null Object is returned back."),
            version = "5.2",
            see = "",
            mapper = @BEMapper(),
            description = "Creates and returns a new terr list object.",
            cautions = "none",
            fndomain = {ACTION},
            example = "Object cust = Analytics.DataList.createList(names, data);"
    )
    public static Object createList(String [] names, Object [] objects)  {

        return ListFunctionsDelegate.createList(names, objects);

    }

    @BEFunction(
            name = "conceptToDataList",
            synopsis = "Converts Concept to datalist",
            signature = "Object conceptToDataList(Concept concept)",
            params = {
                    @FunctionParamDescriptor(name = "concept", type = "Concept", desc = "Concept")
            },
            freturn = @FunctionParamDescriptor(name = "", type = "Object", desc = ""),
            version = "5.2",
            see = "",
            mapper = @BEMapper(),
            description = "Converts the concept to a new terr list object.",
            cautions = "id and extId are also added to data list",
            fndomain = {ACTION},
            example = "Object cust = Analytics.DataList.conceptToDataList(concept);"
    )
    public static Object conceptToDataList(Concept concept)  {
        return ListFunctionsDelegate.conceptToDataList(concept);

    }
    
    @BEFunction(
    		name = "getElementCount",
    		synopsis = "Returns the total number of elements present in the TERR DataList Object passed.",
    		signature = "int getElementCount(Object dataList)",
    		params = {
    				@FunctionParamDescriptor(name = "dataList", type = "Object", desc = "The DataList Object.")
    		},
    		freturn = @FunctionParamDescriptor(name = "", type = "int", desc = "Count of the number of elements in DataList"),
    		version = "5.2",
    		see = "",
    		mapper = @BEMapper(),
    		description = "Returns the total number of elements present in the TERR DataList Object passed.",
    		cautions = "",
    		fndomain = {ACTION},
    		example = "int totalElements = Analytics.DataList.getElementCount(dataList);"
    )
    public static int getElementCount(Object dataList){
    	return ListFunctionsDelegate.getElementCount(dataList);
    }
    
    @BEFunction(
    		name = "getElement",
    		synopsis = "Returns the Element at the index mentioned from the passed DataList Object.",
    		signature = "Object[] getElement(Object dataList, int elementIndex)",
    		params = {
    				@FunctionParamDescriptor(name = "dataList", type = "Object", desc = "The DataList Object."),
    				@FunctionParamDescriptor(name = "elementIndex", type = "int", desc = "The index of the element to be fetched from the passed DataList Object.")
    		},
    		freturn = @FunctionParamDescriptor(name = "", type = "Object[]", desc = "Object array of the Element present at the provided index."),
    		version = "5.2",
    		see = "",
    		mapper = @BEMapper(),
    		description = "Returns the Element at the index mentioned from the passed DataList Object.",
    		cautions = "",
    		fndomain = {ACTION},
    		example = "int elementIndex = 2;<br/>Object[] element = Analytics.DataList.getElement(dataList,elementIndex);"
    )
    public static Object[] getElement(Object dataList, int elementIndex){
    	return ListFunctionsDelegate.getElement(dataList, elementIndex);
    }
    
    @BEFunction(
    		name = "getElementType",
    		synopsis = "Returns the datatype of the element present at the index mentioned in the passed DataList Object.",
    		signature = "String getElementType(Object dataList, int elementIndex)",
    		params = {
    				@FunctionParamDescriptor(name = "dataList", type = "Object", desc = "The DataList Object."),
    				@FunctionParamDescriptor(name = "elementIndex", type = "int", desc = "The index of the element whose type needs to be fetched from the passed DataList Object.")
    		},
    		freturn = @FunctionParamDescriptor(name = "", type = "String", desc = "Returns the datatype of the element present at the index mentioned in the passed DataList Object."),
    		version = "5.2",
    		see = "",
    		mapper = @BEMapper(),
    		description = "Returns the datatype of the element present at the index mentioned in the passed DataList Object.",
    		cautions = "",
    		fndomain = {ACTION},
    		example = "int elementIndex = 2;<br/>String elementType = Analytics.DataList.getElementType(dataList,elementIndex);"
    )
    public static String getElementType(Object dataList, int elementIndex){
    	return ListFunctionsDelegate.getElementType(dataList, elementIndex);
    }


}
