package com.tibco.cep.analytics.terr.io.utils;

import com.tibco.cep.kernel.service.logging.Level;
import com.tibco.cep.kernel.service.logging.LogManagerFactory;
import com.tibco.cep.kernel.service.logging.Logger;
import com.tibco.terr.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Ameya Gawde
 * Date: 5/30/14
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class BEDataList {

    final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(BEDataList.class);
    ConcurrentHashMap nameMap = new ConcurrentHashMap();
    //HashMap<Integer,Integer> indexMapList = new HashMap();
    ConcurrentHashMap nameSize = new ConcurrentHashMap();
    public static final String DATA_LIST_VARIABLE = "BE_TERR_tl";
    int listSize;
    TerrList tl;
    int nameIndex;
    static final String RInt = "com.tibco.terr.TerrInteger";
    static final String RBool = "com.tibco.terr.TerrByte";
    static final String RFrame = "com.tibco.terr.TerrDataFrame";
    static final String RList = "com.tibco.terr.TerrList";
    static final String RDouble = "com.tibco.terr.TerrDouble";
    static final String RString = "com.tibco.terr.TerrString";


    public BEDataList(int size) {

        tl = new TerrList(new String[size], new TerrData[size]);
        listSize = size;

    }

    public final int getSize() {

        return this.listSize;
    }

    public void addToList(String name, String type, int row, Object value) throws Exception {

        int index;
        switch (type) {

            case "names":
            tl.names[nameIndex] = name;
            nameMap.put(name, nameIndex);
            nameSize.put(name, (Integer) value);
            nameIndex++;
                break;

            case "Integer":
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrInteger();
                    ((TerrInteger) tl.data[index]).data = (new int[(Integer) nameSize.get(name)]);

                }
                ((TerrInteger) tl.data[index]).data[row] = (Integer) value;
                break;

            case "Long":
                //There is no support for Long in R. Thus, using double-precision number
                //to store the value
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrDouble();
                    ((TerrDouble) tl.data[index]).data = (new double[(Integer) nameSize.get(name)]);

                }
                ((TerrDouble) tl.data[index]).data[row] = (long) value;
                break;

            case  "String":
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrString();
                    ((TerrString) tl.data[index]).data = (new String[(Integer) nameSize.get(name)]);

                }
                ((TerrString) tl.data[index]).data[row] = (String) value;
                break;

            case  "Double":
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrDouble();
                    ((TerrDouble) tl.data[index]).data = (new double[(Integer) nameSize.get(name)]);

                }
                ((TerrDouble) tl.data[index]).data[row] = (Double) value;
                break;

            case  "Frame":
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrDataFrame();
                    BEDataFrame b = (BEDataFrame) value;
                    tl.data[index] = b.getDataFrame();
                    //((TerrDataFrame)inList.data[index]).data[row]= (TerrData) value;
                    //indexMapList.put(index,0);
                }
                break;

            case "List":
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrList();
                    BEDataList b = (BEDataList) value;
                    tl.data[index] = b.getDataList();

                }
                break;

            case  "Boolean":
                index = (Integer) nameMap.get(name);
                if (tl.data[index] == null) {
                    tl.data[index] = new TerrLogical();
                    ((TerrLogical) tl.data[index]).data = (new byte[(Integer) nameSize.get(name)]);
                }

                ((TerrLogical) tl.data[index]).data[row] = (byte) ((Boolean) value ? 1 : 0);
                break;

            case  "DateTime":
                index = (Integer) nameMap.get(name);

                if (tl.data[index] == null) {
                    tl.data[index] = new TerrDouble();
                    ((TerrDouble) tl.data[index]).data = (new double[(Integer) nameSize.get(name)]);
                }

                ((TerrDouble) tl.data[index]).data[row] = (double) (((Calendar)value).getTimeInMillis());

                break;

            default:        LOGGER.log(Level.DEBUG,"Unsupported dataType found"); break;

        }

    }

    public void printList(TerrJavaRemote engine) throws Exception {
        TERRLifetime.checkTERREngine();
        engine.setVariable(DATA_LIST_VARIABLE, tl);
        engine.evaluateInteractive("print(" + DATA_LIST_VARIABLE + ")");
        //removing the object from TERR Memory
        engine.evaluateInteractive("rm("+DATA_LIST_VARIABLE+")");
    }

    public final TerrList getDataList() {
        return this.tl;
    }

    public final String[] getColumnNames() {
        String columnArray[] = new String[tl.names.length];
        int i = 0;
        for (String s : tl.names) {
            columnArray[i++] = s;
        }
        return columnArray;

    }

    public Object [] get(String name) {

        Object [] arr = new Object[(int)nameSize.get(name)];
        int ind = (Integer) nameMap.get(name);
        String classCheck =  tl.data[ind].getClass().getName();
        for (int i = 0; i < (int)nameSize.get(name); i++) {
            switch(classCheck){

                case RDouble:
                    arr[i] = ((TerrDouble) tl.data[ind]).data[i];
                    break;
                case RInt:
                    arr[i] = ((TerrInteger) tl.data[ind]).data[i];
                    break;

                case RBool:
                    arr[i] = ((TerrByte) tl.data[ind]).data[i];
                    break;
                case RString:
                    arr[i] = ((TerrString) tl.data[ind]).data[i];
                    break;
                case RList:
                    arr[i] = ((TerrList) tl.data[ind]).data[i];
                    break;
                case RFrame:
                    arr[i] = ((TerrDataFrame) tl.data[ind]).data[i];
                    break;


            }

        }

        return arr;

    }

    public final String[] getListNames() {

        return Arrays.copyOf(tl.names, tl.names.length);

    }

    public void removeRow(int row) {

        for (int i = 0; i < nameIndex; i++) {

            ((TerrList) tl.data[i]).data[row] = null;

        }

    }
}

