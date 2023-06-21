package com.tibco.cep.analytics.terr.io;

import com.tibco.cep.analytics.terr.io.utils.TERREngine;
import com.tibco.cep.analytics.terr.io.utils.TERRException;
import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.terr.*;
import com.tibco.terr.TerrData.Type;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya
 * Date: 11/17/14
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FrameFunctionsDelegate {

    static final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(FrameFunctionsDelegate.class);
    static final int FACTORINDEX = 2;
    static final int DATAFRAMEINDEX = 1;
    static final int DATALISTINDEX = 1;
    static final String NAMEPLACEHOLDER = "PLACEHOLDER";
    static final TerrString DATAPLACEHOLDER = new TerrString(NAMEPLACEHOLDER);

    public static Object createFrame(String [] names, Object [] objects) {

        TerrDataFrame dataFrame = new TerrDataFrame(names,new TerrData[objects.length]);
        int sizeCheck=0;
        try {
            for(int i = 0 ; i < objects.length; i++) {

                Object obj = objects[i];

                if(obj instanceof int[] || obj instanceof Integer[]){
                	if(obj instanceof Integer[]){
                		Integer[] tempArr = (Integer[])obj;
                		int[] tempIntArr = new int[tempArr.length];
                		for(int j = 0; j < tempArr.length; j++){
                			tempIntArr[j] = tempArr[j].intValue();
                		}
                		obj = tempIntArr;
                	}	
                    if(i == 0)
                        sizeCheck = ((int [])obj).length;
                    else if(sizeCheck != ((int [])obj).length)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrInteger((int [])obj);
                    continue;
                }

                if(obj instanceof Integer){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrInteger((Integer)obj);
                    continue;
                }

                if(obj instanceof double[] || obj instanceof Double[]){
                	if(obj instanceof Double[]){
                		Double[] tempArr = (Double[])obj;
                		double[] tempDoubleArr = new double[tempArr.length];
                		for(int j = 0 ; j < tempArr.length; j++){
                			tempDoubleArr[j] = tempArr[j].doubleValue();
                		}
                		obj = tempDoubleArr;
                	}
                	
                    if(i == 0)
                        sizeCheck = ((double [])obj).length;
                    else if(sizeCheck != ((double [])obj).length)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrDouble((double [])obj);
                    continue;
                }

                if(obj instanceof Double){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrDouble((Double)obj);
                    continue;
                }

                if(obj instanceof long[] || obj instanceof Long[]){
                	if(obj instanceof Long[]){
                		Long[] tempArr = (Long[])obj;
                		long[] tempLongArr = new long[tempArr.length];
                		for(int j = 0; j < tempArr.length; j++){
                			tempLongArr[j] = tempArr[j].longValue();
                		}
                		obj = tempLongArr;
                	}
                	
                    if(i == 0)
                        sizeCheck = ((long [])obj).length;
                    else if(sizeCheck != ((long [])obj).length)
                        throw new TERRException("Unequal number of rows in data frame");
                    long [] arr = (long []) obj;
                    double [] arrDouble = new double [arr.length];
                    for(int j =0 ; j <arr.length; j++) {
                        Long t = new Long(arr[j]);
                        arrDouble[j] = t.doubleValue();
                    }
                    dataFrame.data[i] = new TerrDouble(arrDouble);
                    continue;
                }

                if(obj instanceof Long){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    Long t = new Long((long)obj);
                    dataFrame.data[i] = new TerrDouble(t.doubleValue());
                    continue;
                }

                if(obj instanceof String[]){
                    if(i == 0)
                        sizeCheck = ((String [])obj).length;
                    else if(sizeCheck != ((String [])obj).length)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrString((String [])obj);
                    continue;
                }

                if(obj instanceof String){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrString((String)obj);
                    continue;
                }

                if(obj instanceof boolean[] || obj instanceof Boolean[]){
                	if(obj instanceof Boolean[]){
                		Boolean[] tempArr = (Boolean[])obj;
                		boolean[] tempBooleanArr = new boolean[tempArr.length];
                		for(int j = 0; j < tempArr.length; j++){
                			tempBooleanArr[j] = tempArr[j].booleanValue();
                		}
                		obj = tempBooleanArr;
                	}
                	
                    if(i == 0)
                        sizeCheck = ((boolean [])obj).length;
                    else if(sizeCheck != ((boolean [])obj).length)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrLogical((boolean [])obj);
                    continue;
                }

                if(obj instanceof Boolean){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrLogical((Boolean)obj);
                    continue;
                }

                if(obj instanceof Calendar[]){
                    if(i == 0)
                        sizeCheck = ((Calendar [])obj).length;
                    else if(sizeCheck != ((Calendar [])obj).length)
                        throw new TERRException("Unequal number of rows in data frame");
                    String [] time = new String[((Calendar[]) obj).length];
                    for(int j = 0; j < ((Calendar[])obj).length; j++) {
                        Calendar calendar = (Calendar)((Object[])obj)[j];
                        time[j] = TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(calendar.getTime());
                    }
                    dataFrame.data[i] = new TerrString(time);
                }

                if(obj instanceof Calendar){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = new TerrString(TerrFunctionsDelegate.SIMPLE_DATE_FORMAT.format(((Calendar) obj).getTime()));
                    continue;
                }

                if(obj instanceof TerrDataFrame){
                    if(i == 0)
                        sizeCheck = 1;
                    else if(sizeCheck != 1)
                        throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = (TerrDataFrame)obj;
                    continue;
                }

                if(obj instanceof TerrList){
                    if(i == 0)
                        sizeCheck = 1;
                    //else if(sizeCheck != 1)
                    //    throw new TERRException("Unequal number of rows in data frame");
                    dataFrame.data[i] = (TerrList)obj;
                    continue;
                }

            }
        }
        catch (Exception e){
            LOGGER.log(Level.ERROR, "%s", e);
        }

        return dataFrame;

    }

    
    public static void debugOut(Object frame){
    	try{
    		TerrDataFrame frameObj = TerrDataFrame.class.cast(frame);
    		String[] columnNames = frameObj.names;
    		String msg = "#\t";
    		for(String name : columnNames)
    			msg += name + "\t";
    		msg = msg.trim();
    		//msg = "\t" + msg;
    		LOGGER.log(Level.INFO, msg);
    		msg = "";
    		TerrData[] column = frameObj.data;
    		int length = 0;
    		if(column[0].getType().equals(TerrData.Type.BYTE)){
    			length = (TerrByte.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.DATAFRAME)){
    			length = (TerrDataFrame.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.DOUBLE)){
    			length = (TerrDouble.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.FACTOR)){
    			length = (TerrFactor.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.INTEGER)){
    			length = (TerrInteger.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.LIST)){
    			length = (TerrList.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.LOGICAL)){
    			length = (TerrLogical.class.cast(column[0])).data.length;
    		}
    		else if(column[0].getType().equals(TerrData.Type.STRING)){
    			length = (TerrString.class.cast(column[0])).data.length;
    		}
    		
    		for(int i = 0; i < length; i++){
    			msg += (i+1) + "\t";
    			for(int j = 0; j < columnNames.length; j++){
    				if(column[j].getType().equals(TerrData.Type.BYTE)){
    					msg += TerrByte.class.cast(column[j]).data[i] + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.DATAFRAME)){
    					//msg += TerrDataFrame.class.cast(column[j]).data[i].toString() + "\t";
    					msg += TerrDataFrame.class.cast(column[j]).toString() + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.DOUBLE)){
    					msg += TerrDouble.class.cast(column[j]).data[i] + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.FACTOR)){
    					TerrFactor obj = TerrFactor.class.cast(column[j]);
    					String[] values = obj.levels.data;
    					int[] indexes = obj.data;
    					int indexLookup = indexes[i]-1;
    					msg += values[indexLookup] + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.INTEGER)){
    					msg += TerrInteger.class.cast(column[j]).data[i] + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.LIST)){
    					//msg += TerrList.class.cast(column[j]).data[i].toString() + "\t";
    					msg += TerrList.class.cast(column[j]).toString() + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.LOGICAL)){
    					msg += TerrLogical.class.cast(column[j]).data[i] + "\t";
    				}
    				else if(column[j].getType().equals(TerrData.Type.STRING)){
    					msg += TerrString.class.cast(column[j]).data[i] + "\t";
    				}
    			}
    			msg = msg.trim();
    			LOGGER.log(Level.INFO, msg);
    			msg = "";
    		}
    	}
    	catch(Exception e){
    		LOGGER.log(Level.ERROR, "%s", e);
    	}
    }
    
    /*public static void debugOut(String engine, Object frame) {
        TERREngine terrEngine = TerrFunctionsDelegate.TERR_ENGINE_MAP.get(engine);
        TerrJavaRemote remote = terrEngine.getRemote();
        try {
            remote.setVariable("tmpFramePrintVar", (TerrDataFrame)frame);
            remote.evaluateInteractive("print(tmpFramePrintVar)");
        } catch (Exception e) {
            LOGGER.log(Level.ERROR,"%s", e );
        }

        finally {
            try {
                remote.evaluateInteractive("rm(tmpFramePrintVar)");
            } catch (Exception e) {
                LOGGER.log(Level.ERROR,"%s", e );
            }
        }

    }*/
    
    public static int getColumnCount(Object dataFrame){
    	int numberOfColumns = 0;
    	try{
    		TerrDataFrame dfObj = TerrDataFrame.class.cast(dataFrame);
    		numberOfColumns = dfObj.data.length;
    	}
    	catch(NullPointerException npe){
    		LOGGER.log(Level.ERROR, "%s", npe);
    	}
    	catch(ClassCastException cce){
    		LOGGER.log(Level.ERROR, "%s", cce);
    	}
    	return numberOfColumns;
    }
    
    public static Object[] getColumn(Object dataFrame, int columnIndex){
    	Object[] column = null;
    	try{
    		TerrDataFrame dfObj = TerrDataFrame.class.cast(dataFrame);
    		int maxColumns = getColumnCount(dataFrame);
    		int dataLength = 0;
    		int index = 0;
    		if(columnIndex > maxColumns){
    			LOGGER.log(Level.INFO, "Maximum number of columns in the provided dataframe object are %d and the requested column number %d is greater.", maxColumns, columnIndex);
    			return column;
    		}
    		TerrData obj = dfObj.data[columnIndex];
    		if(obj.getType().equals(TerrData.Type.BYTE)){
    			TerrByte byteData = TerrByte.class.cast(obj);
    			dataLength = byteData.data.length;
    			column = new Object[dataLength];
    			while(index < dataLength){
    				column[index] = byteData.data[index];
    				index++;
    			}
    		}
    		else if(obj.getType().equals(TerrData.Type.DATAFRAME)){
    			TerrDataFrame dataframeData = TerrDataFrame.class.cast(obj);
    			column = new Object[DATAFRAMEINDEX];
    			while(index < column.length){
    				column[index] = dataframeData;
    				index++;
    			}
    			//dataLength = dataframeData.getMaxColumnLength();
    			//column = new Object[dataLength];
    			//while(index < dataLength){
    			//	column[index] = dataframeData.data[index];
    			//	index++;
    			//}
    		}
    		else if(obj.getType().equals(TerrData.Type.DOUBLE)){
    			TerrDouble doubleData = TerrDouble.class.cast(obj);
    			dataLength = doubleData.data.length;
    			column = new Object[dataLength];
    			while(index < dataLength){
    				column[index] = doubleData.data[index];
    				index++;
    			}
    		}
    		else if(obj.getType().equals(TerrData.Type.FACTOR)){
    			TerrFactor factorData = TerrFactor.class.cast(obj);
    			String[] levels = factorData.levels.data;
    			int[] indices = factorData.data;
    			column = new Object[FACTORINDEX];
    			column[0] = levels;
    			column[1] = indices;
    		}
    		else if(obj.getType().equals(TerrData.Type.INTEGER)){
    			TerrInteger integerData = TerrInteger.class.cast(obj);
    			dataLength = integerData.data.length;
    			column = new Object[dataLength];
    			while(index < dataLength){
    				column[index] = integerData.data[index];
    				index++;
    			}
    			
    		}
    		else if(obj.getType().equals(TerrData.Type.LIST)){
    			TerrList listData = TerrList.class.cast(obj);
    			column = new Object[DATALISTINDEX];
    			while(index < column.length){
    				column[index] = listData;
    				index++;
    			}
    			/*dataLength = listData.data.length;
    			column = new Object[dataLength];
    			while(index < dataLength){
    				column[index] = listData.data[index];
    				index++;
    			}*/
    		}
    		else if(obj.getType().equals(TerrData.Type.LOGICAL)){
    			TerrLogical logicalData = TerrLogical.class.cast(obj);
    			dataLength = logicalData.data.length;
    			column = new Object[dataLength];
    			while(index < dataLength){
    				column[index] = logicalData.data[index];
    				index++;
    			}
    		}
    		else if(obj.getType().equals(TerrData.Type.STRING)){
    			TerrString stringData = TerrString.class.cast(obj);
    			dataLength = stringData.data.length;
    			column = new Object[dataLength];
    			while(index < dataLength){
    				column[index] = stringData.data[index];
    				index++;
    			}
    		}
    	}
    	catch(NullPointerException npe){
    		LOGGER.log(Level.ERROR, "%s", npe);
    	}
    	catch(ClassCastException cce){
    		LOGGER.log(Level.ERROR, "%s", cce);
    	}
    	catch(Exception e){
    		LOGGER.log(Level.ERROR, "%s", e);
    	}
    	return column;
    }
    
    public static String getColumnType(Object dataFrame, int columnIndex){
    	String columnType = null;
    	try{
    		TerrDataFrame dfObj = TerrDataFrame.class.cast(dataFrame);
    		int maxColumns = getColumnCount(dataFrame);
    		if(columnIndex > maxColumns){
    			LOGGER.log(Level.INFO, "Maximum number of columns in the provided dataframe object are %d and the requested column number %d is greater.", maxColumns, columnIndex);
    			return columnType;
    		}
    		TerrData[] cols = dfObj.data;
    		TerrData col = cols[columnIndex];
    		if(col == null)
    			throw new NullPointerException("col is null");
    		//columnType = col.getType().toString();
    		if(col.getType().equals(TerrData.Type.BYTE)){
    			columnType = TerrData.Type.BYTE.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.DATAFRAME)){
    			columnType = TerrData.Type.DATAFRAME.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.DOUBLE)){
    			columnType = TerrData.Type.DOUBLE.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.FACTOR)){
    			columnType = TerrData.Type.FACTOR.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.INTEGER)){
    			columnType = TerrData.Type.INTEGER.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.LIST)){
    			columnType = TerrData.Type.LIST.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.LOGICAL)){
    			columnType = TerrData.Type.LOGICAL.toString();
    		}
    		else if(col.getType().equals(TerrData.Type.STRING)){
    			columnType = TerrData.Type.STRING.toString();
    		}
    	}
    	catch(NullPointerException npe){
    		LOGGER.log(Level.ERROR, "%s", npe);
    	}
    	catch(ClassCastException cce){
    		LOGGER.log(Level.ERROR, "%s", cce);
    	}
    	catch(Exception e){
    		LOGGER.log(Level.ERROR, "%s", e);
    	}
    	return columnType;
    }


}
