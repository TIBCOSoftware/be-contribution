package com.tibco.cep.analytics.terr.io;

import com.tibco.cep.analytics.terr.io.utils.TERREngine;
import com.tibco.cep.kernel.model.entity.Id;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.cep.runtime.model.element.*;
import com.tibco.cep.runtime.model.element.impl.property.history.*;
import com.tibco.cep.runtime.model.element.impl.property.simple.*;
import com.tibco.terr.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya
 * Date: 11/14/14
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListFunctionsDelegate {

    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(ListFunctionsDelegate.class);
    static final int FACTOR_INDEX = 2;
    static final int FRAME_LIST_LENGTH = 1;
    static final int FRAME_LIST_INDEX = 0;
    
    public static Object createList(String [] names, Object [] objects)  {
        TerrList terrList = new TerrList(names,new TerrData[objects.length]);

        for(int i = 0 ; i < objects.length; i++) {

            Object obj = objects[i];

            if(obj instanceof int[]){
                terrList.data[i] = new TerrInteger((int [])obj);
                continue;
            }

            if(obj instanceof Integer){
                terrList.data[i] = new TerrInteger((Integer)obj);
                continue;
            }

            if(obj instanceof double[]){
                terrList.data[i] = new TerrDouble((double [])obj);
                continue;
            }

            if(obj instanceof Double){
                terrList.data[i] = new TerrDouble((Double)obj);
                continue;
            }

            if(obj instanceof long[]){
                long [] arr = (long []) obj;
                double [] arrDouble = new double [arr.length];
                for(int j =0 ; j <arr.length; j++) {
                    Long t = new Long(arr[j]);
                    arrDouble[j] = t.doubleValue();
                }
                terrList.data[i] = new TerrDouble(arrDouble);
                continue;
            }

            if(obj instanceof Long){
                Long t = new Long((long)obj);
                terrList.data[i] = new TerrDouble(t.doubleValue());
                continue;
            }

            if(obj instanceof String[]){
                terrList.data[i] = new TerrString((String [])obj);
                continue;
            }

            if(obj instanceof String){
                terrList.data[i] = new TerrString((String)obj);
                continue;
            }

            if(obj instanceof boolean[]){
                terrList.data[i] = new TerrLogical((boolean [])obj);
                continue;
            }

            if(obj instanceof Boolean){
                terrList.data[i] = new TerrLogical((Boolean)obj);
                continue;
            }

            if(obj instanceof Calendar[]){
                String [] time = new String[((Calendar[]) obj).length];
                for(int j = 0; j < ((Calendar[])obj).length; j++) {
                    Calendar calendar = (Calendar)((Object[])obj)[j];
                    time[j] = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(calendar.getTime());
                }
                terrList.data[i] = new TerrString(time);
                continue;
            }

            if(obj instanceof Calendar){
                terrList.data[i] = new TerrString(TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(((Calendar) obj).getTime()));
                continue;
            }

            if(obj instanceof TerrDataFrame){
                terrList.data[i] = (TerrDataFrame)obj;
                continue;
            }

            if(obj instanceof TerrList){
                terrList.data[i] = (TerrList)obj;
                continue;
            }

        }

        return terrList;

    }



    public static Object conceptToDataList(Concept concept)  {
        final Set<Concept> visitedConcept = new HashSet<>();
        String colName;
        Property[] props = concept.getProperties();
        TerrList terrList = new TerrList(new String[props.length+2], new TerrData[props.length+2]);
        Map<String, Integer> columns = new HashMap<>();
        int i =0;
        colName = "id";
        terrList.names[i] = colName;
        columns.put(colName,i);
        terrList.data[columns.get(colName)] = (Id.useLegacyID) ? new TerrDouble((double)concept.getId().getLongValue()) : new TerrString(concept.getId().toString());
        if (Id.useLegacyID) {
        	i++;
        	colName = "extId";
        	terrList.names[i] = colName;
        	columns.put(colName,i);
        	terrList.data[columns.get(colName)] = new TerrString(concept.getExtId());
        }
        i++;
        int histInt = 0, histBool = 0, histString = 0, histContainedConcept = 0, histRefConcept = 0;
        for(Property prop : props) {
            try {
                if(prop instanceof PropertyAtom) {
                    PropertyAtom pas = (PropertyAtom) prop;
                    colName = pas.getName();
                    if(!columns.containsKey(colName)) {
                        terrList.names[i] = colName;
                        columns.put(colName, i);
                        i++;
                    }
                    if (pas instanceof PropertyAtomStringSimple) {
                        terrList.data[columns.get(colName)] = new TerrString(pas.getString());
                    } else if (pas instanceof PropertyAtomIntSimple) {
                        terrList.data[columns.get(colName)] = new TerrInteger(((PropertyAtomIntSimple) pas).getInt());
                    } else if (pas instanceof PropertyAtomLongSimple) {
                        terrList.data[columns.get(colName)] = new TerrDouble((double)((PropertyAtomLongSimple) pas).getLong());
                    } else if (pas instanceof PropertyAtomDoubleSimple) {
                        terrList.data[columns.get(colName)] = new TerrDouble(((PropertyAtomDoubleSimple) pas).getDouble());
                    } else if (pas instanceof PropertyAtomBooleanSimple) {
                        terrList.data[columns.get(colName)] = new TerrLogical(((PropertyAtomBooleanSimple) pas).getBoolean());
                    } else if (pas instanceof PropertyAtomDateTimeSimple) {
                        String time = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(((PropertyAtomDateTimeSimple) pas).getDateTime().getTime());
                        terrList.data[columns.get(colName)] = new TerrString(time);
                    } else if (pas instanceof PropertyAtomContainedConceptSimple) {
                        Concept c = ((PropertyAtomContainedConceptSimple)pas).getConcept();
                        if(!visitedConcept.contains(c)) {
                            terrList.data[columns.get(colName)] = (TerrList)conceptToDataList(c);
                            visitedConcept.add(c);
                        }
                    } else if (pas instanceof PropertyAtomConceptReferenceSimple) {
                        Concept c = ((PropertyAtomConceptReferenceSimple)pas).getConcept();
                        terrList.data[columns.get(colName)] = (Id.useLegacyID) ? new TerrDouble((double)c.getId().getLongValue()) : new TerrString(c.getId().toString());
                    } else  if (pas instanceof PropertyAtomStringImpl) {
                        String [] arrHist = new String[pas.getHistorySize()];
                        for(int ind = 0 ; ind < pas.getHistorySize(); ind++) {
                            arrHist[ind] = pas.getStringAtIdx(ind);
                        }
                        terrList.data[columns.get(colName)] = new TerrString(arrHist);
                    } else if (pas instanceof PropertyAtomIntImpl) {
                        int [] arrHist = new int[pas.getHistorySize()];;
                        for(int ind = 0 ; ind < pas.getHistorySize(); ind++) {
                            arrHist[ind] = (int) pas.getValueAtIdx(ind);
                        }
                        terrList.data[columns.get(colName)] = new TerrInteger(arrHist);
                    } else if (pas instanceof PropertyAtomLongImpl) {
                        double [] arrHist = new double[pas.getHistorySize()];;
                        for(int ind = 0 ; ind < pas.getHistorySize(); ind++) {
                            Long tempLong = (long)pas.getValueAtIdx(ind);
                            arrHist[ind] = tempLong.doubleValue();
                        }
                        terrList.data[columns.get(colName)] = new TerrDouble(arrHist);
                    } else if (pas instanceof PropertyAtomDoubleImpl) {
                        double [] arrHist = new double[pas.getHistorySize()];;
                        for(int ind = 0 ; ind < pas.getHistorySize(); ind++) {
                            arrHist[ind] = (double) pas.getValueAtIdx(ind);
                        }
                        terrList.data[columns.get(colName)] = new TerrDouble(arrHist);
                    } else if (pas instanceof PropertyAtomBooleanImpl) {
                        boolean  [] arrHist = new boolean[pas.getHistorySize()];;
                        for(int ind = 0 ; ind < pas.getHistorySize(); ind++) {
                            arrHist[ind] = (boolean) pas.getValueAtIdx(ind);
                        }
                        terrList.data[columns.get(colName)] = new TerrLogical(arrHist);
                    } else if (pas instanceof PropertyAtomDateTimeImpl) {
                        Calendar [] arrHist = new Calendar[pas.getHistorySize()];
                        for(int ind = 0 ; ind < pas.getHistorySize(); ind++) {
                            arrHist[ind] = (Calendar) pas.getValueAtIdx(ind);
                        }
                        String[] time = new String[pas.getHistorySize()];
                        for(int ind = 0 ; ind < arrHist.length; ind++)
                            if(arrHist[ind] != null)
                                time[ind] = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(arrHist[ind].getTime());
                        terrList.data[columns.get(colName)] = new TerrString(time);
                    } else if (pas instanceof PropertyAtomContainedConceptImpl) {
                        Concept c = ((PropertyAtomContainedConceptImpl)pas).getConcept();
                        if(!visitedConcept.contains(c)) {
                            terrList.data[columns.get(colName)] = (TerrList)conceptToDataList(c);
                            visitedConcept.add(c);
                        }
                    } else if (pas instanceof PropertyAtomConceptReferenceImpl) {
                        Concept c = ((PropertyAtomConceptReferenceImpl)pas).getConcept();
                        terrList.data[columns.get(colName)] = (Id.useLegacyID) ? new TerrDouble((double)c.getId().getLongValue()) : new TerrString(c.getId().toString());;
                    }
                    continue;
                }
            }
            catch (PropertyException pe) {
                LOGGER.log(Level.ERROR,"%s",pe);
            }


            if(prop instanceof PropertyArray) {
                PropertyArray pas = (PropertyArray) prop;
                colName = pas.getName();
                if(!columns.containsKey(colName)) {
                    terrList.names[i] = colName;
                    columns.put(colName, i);
                    i++;
                }

                if (pas instanceof PropertyArrayString) {
                    String [] arr = new String[pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = atom.getString();
                    }
                    terrList.data[columns.get(colName)] = new TerrString(arr);
                } else if (pas instanceof PropertyArrayIntSimple) {
                    int [] arr = new int[pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = ((PropertyAtomIntSimple)atom).getInt();
                    }
                    terrList.data[columns.get(colName)] = new TerrInteger(arr);
                } else if (pas instanceof PropertyArrayLongSimple) {
                    double [] arr = new double [pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = ((PropertyAtomLongSimple)atom).getLong();
                    }
                    terrList.data[columns.get(colName)] = new TerrDouble(arr);
                } else if (pas instanceof PropertyArrayDoubleSimple) {
                    double [] arr = new double [pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = ((PropertyAtomDoubleSimple)atom).getDouble();
                    }
                    terrList.data[columns.get(colName)] = new TerrDouble(arr);
                } else if (pas instanceof PropertyArrayBooleanSimple) {
                    boolean  [] arr = new boolean [pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = ((PropertyAtomBooleanSimple)atom).getBoolean();
                    }
                    terrList.data[columns.get(colName)] = new TerrLogical(arr);
                } else if (pas instanceof PropertyArrayDateTimeSimple) {
                    String [] arr = new String [pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(((PropertyAtomDateTimeSimple)atom).getDateTime().getTime());
                    }
                    terrList.data[columns.get(colName)] = new TerrString(arr);
                } else  if (pas instanceof PropertyArrayContainedConceptSimple) {
                    Concept [] arr = new Concept [pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        arr[ind] = ((PropertyAtomContainedConceptSimple)atom).getConcept();
                    }
                    for(Concept c :arr){
                        if(!visitedConcept.contains(c)) {
                            terrList.data[columns.get(colName)] = (TerrList)conceptToDataList(c);
                            visitedConcept.add(c);
                        }
                    }
                } else  if (pas instanceof PropertyArrayConceptReferenceSimple) {
                    Concept [] arr = new Concept [pas.length()];
                    for(int ind = 0; ind < pas.length(); ind++) {
                        PropertyAtom atom = pas.get(ind);
                        //arr[ind] = ((PropertyAtomContainedConceptSimple)atom).getConcept();
                        //BE-22555 resolution
                        arr[ind] = ((PropertyAtomConceptReferenceSimple)atom).getConcept();
                    }
                    for(Concept c : arr) {
                        terrList.data[columns.get(colName)] = (Id.useLegacyID) ? new TerrDouble((double)c.getId().getLongValue()) : new TerrString(c.getId().toString());
                    }
                }

            }

        }

        return terrList;

    }

    
    
    public static int getElementCount(Object dataList){
    	TerrList listObj = TerrList.class.cast(dataList);
    	return listObj.data.length;
    }
    
    public static Object[] getElement(Object dataList, int elementIndex){
    	Object[] elementValue = null;
    	TerrList listObj = TerrList.class.cast(dataList);
    	int listLength = listObj.data.length;
    	try{
    		if(elementIndex > listLength){
    			String errMsg = "The index provided " + elementIndex + " is greater than the length " + listLength + " of the DataList.";
    			throw new Exception(errMsg);
    		}
    		TerrData element = listObj.data[elementIndex];
    		String elementType = element.getType().name();
    		if(elementType.equalsIgnoreCase(TerrData.Type.BYTE.name())){
    			TerrByte byteData = TerrByte.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[byteData.data.length];
    			for(int i = 0; i < byteData.data.length; i++){
    				elementValue[i] = byteData.data[i];
    			}
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.DATAFRAME.name())){
    			TerrDataFrame frameData = TerrDataFrame.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[FRAME_LIST_LENGTH];
    			elementValue[FRAME_LIST_INDEX] = frameData;
    			/*elementValue = new Object[frameData.data.length];
    			for(int i = 0; i < frameData.data.length; i++){
    				elementValue[i] = frameData.data[i];
    			}*/
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.DOUBLE.name())){
    			TerrDouble doubleData = TerrDouble.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[doubleData.data.length];
    			for(int i = 0; i < doubleData.data.length; i++){
    				elementValue[i] = doubleData.data[i];
    			}
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.FACTOR.name())){
    			TerrFactor factorData = TerrFactor.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[FACTOR_INDEX];
    			elementValue[0] = factorData.data;
    			elementValue[1] = factorData.levels.data;
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.INTEGER.name())){
    			TerrInteger integerData = TerrInteger.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[integerData.data.length];
    			for(int i = 0; i < integerData.data.length; i++){
    				elementValue[i] = integerData.data[i];
    			}
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.LIST.name())){
    			TerrList listData = TerrList.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[FRAME_LIST_LENGTH];
    			elementValue[FRAME_LIST_INDEX] = listData;
    			/*elementValue = new Object[listData.data.length];
    			for(int i = 0; i < listData.data.length; i++){
    				elementValue[i] = listData.data[i];
    			}*/
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.LOGICAL.name())){
    			TerrLogical logicalData = TerrLogical.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[logicalData.data.length];
    			for(int i = 0; i < logicalData.data.length; i++){
    				elementValue[i] = logicalData.data[i];
    			}
    		}
    		else if(elementType.equalsIgnoreCase(TerrData.Type.STRING.name())){
    			TerrString stringData = TerrString.class.cast(listObj.data[elementIndex]);
    			elementValue = new Object[stringData.data.length];
    			for(int i = 0; i < stringData.data.length; i++){
    				elementValue[i] = stringData.data[i];
    			}
    		}
    	}
    	catch(Exception exp){
    		LOGGER.log(Level.ERROR, "%s", exp);
    	}
    	
    	return elementValue;
    }
    
    public static String getElementType(Object dataList, int elementIndex){
    	String elementType = null;
    	TerrList listObj = TerrList.class.cast(dataList);
    	int listLength = listObj.data.length;
    	try{
    		if(elementIndex > listLength){
        		String errMsg = "The index provided " + elementIndex + " is greater than the length " + listLength + " of the DataList.";
        		throw new Exception(errMsg);
        	}
    		TerrData element = listObj.data[elementIndex];
    		elementType = element.getType().name();
    	}
    	catch(Exception exp){
    		LOGGER.log(Level.ERROR, "%s", exp);
    	}
    	return elementType;
    }
    
    public static void debugOut(Object list){
    	try{
    		TerrList listObj = TerrList.class.cast(list);
    		int length = listObj.data.length;
    		String[] names = null;
    		if(listObj.names != null && listObj.names.length < listObj.data.length){
    			names = new String[listObj.data.length];
    			for(int i = 0; i < listObj.names.length; i++){
    				names[i] = listObj.names[i];
    			}
    		}
    		else{
    			names = listObj.names;
    		}
    		//String[] names = listObj.names;
    		boolean isPresentNames = false;
    		if(names != null && names.length != 0)
    			isPresentNames = true;
    		
    		for(int i = 0; i < length; i++){
    			TerrData obj = listObj.data[i];
    			String msg = "";
    			if(obj.getType().equals(TerrData.Type.BYTE)){
    				TerrByte byteObj = TerrByte.class.cast(obj);
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				msg += "[" + 1 + "]\t";
    				for(int j = 0; j < byteObj.data.length; j++){
    					msg += byteObj.data[j] + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    			}
    			else if(obj.getType().equals(TerrData.Type.DATAFRAME)){
    				TerrDataFrame frameObj = TerrDataFrame.class.cast(obj);
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				FrameFunctionsDelegate.debugOut(frameObj);
    			}
    			else if(obj.getType().equals(TerrData.Type.DOUBLE)){
    				TerrDouble doubleObj = TerrDouble.class.cast(obj);
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				msg += "[" + 1 + "]\t";
    				for(int j = 0; j < doubleObj.data.length; j++){
    					msg += doubleObj.data[j] + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    			}
    			else if(obj.getType().equals(TerrData.Type.FACTOR)){
    				TerrFactor factorObj = TerrFactor.class.cast(obj);
    				String[] values = factorObj.levels.data;
    				int[] indices = factorObj.data;
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO,"[["+(i+1)+"]]");
    				}
    				msg += "[" + 1 + "]\t";
    				for(int j = 0; j < indices.length; j++){
    					msg += values[indices[j]-1].trim() + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    				msg += "Levels: ";
    				for(String val : values){
    					msg += val.trim() + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    			}
    			else if(obj.getType().equals(TerrData.Type.INTEGER)){
    				TerrInteger integerObj = TerrInteger.class.cast(obj);
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				msg += "[" + 1 + "]\t";
    				for(int j = 0; j < integerObj.data.length; j++){
    					msg += integerObj.data[j] + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    			}
    			else if(obj.getType().equals(TerrData.Type.LIST)){
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				ListFunctionsDelegate.debugOut(obj);
    			}
    			else if(obj.getType().equals(TerrData.Type.LOGICAL)){
    				TerrLogical logicalObj = TerrLogical.class.cast(obj);
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				msg += "[" + 1 + "]\t";
    				for(int j = 0; j < logicalObj.data.length; j++){
    					msg += logicalObj.data[j] + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    			}
    			else if(obj.getType().equals(TerrData.Type.STRING)){
    				TerrString stringObj = TerrString.class.cast(obj);
    				if(isPresentNames && names[i] != null && names[i].length() != 0){
    					LOGGER.log(Level.INFO, "$" + names[i]);
    				}
    				else{
    					LOGGER.log(Level.INFO, "[[" + (i+1) + "]]");
    				}
    				msg += "[" + 1 + "]\t";
    				for(int j = 0; j < stringObj.data.length; j++){
    					msg += "\"" + stringObj.data[j] + "\"" + "\t";
    				}
    				msg = msg.trim();
    				LOGGER.log(Level.INFO, msg);
    				msg = "";
    			}
    		}
    	}
    	catch(Exception e){
    		LOGGER.log(Level.ERROR, "%s", e);
    	}
    }
}
