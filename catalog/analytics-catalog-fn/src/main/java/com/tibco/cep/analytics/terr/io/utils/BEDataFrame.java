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
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class BEDataFrame {

    ConcurrentHashMap nameMap = new ConcurrentHashMap();
    ConcurrentHashMap nameSize = new ConcurrentHashMap();
    final Logger LOGGER = LogManagerFactory.getLogManager().getLogger(BEDataFrame.class);
    public static final String DATA_FRAME_VARIABLE = "BE_TERR_td";
    int frameSize;
    int nameIndex;
    TerrDataFrame td;
    static final String RInt = "com.tibco.terr.TerrInteger";
    static final String RBool = "com.tibco.terr.TerrByte";
    static final String RFrame = "com.tibco.terr.TerrDataFrame";
    static final String RList = "com.tibco.terr.TerrList";
    static final String RDouble = "com.tibco.terr.TerrDouble";
    static final String RString = "com.tibco.terr.TerrString";

    public BEDataFrame(int size) {

        td = new TerrDataFrame(new String[size], new TerrData[size]);
        frameSize = size;

    }

    public final int getSize() {

        return this.frameSize;
    }

    public void addToFrame(String name, String type, int row, Object value) throws Exception {

        int index;
        switch (type) {
            case "names":
                td.names[nameIndex] = name;
                nameMap.put(name, nameIndex);
                nameSize.put(name, (Integer) value);
                nameIndex++;
                break;


            case "Integer":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrInteger();
                    ((TerrInteger) td.data[index]).data = (new int[(Integer) nameSize.get(name)]);
                }
                ((TerrInteger) td.data[index]).data[row] = (Integer) value;
                break;

            case "Long":
                //There is no support for Long in R. Thus, using double-precision number
                //to store the value
                index = (Integer) nameMap.get(name);
                if (td.data[index] == null) {
                    td.data[index] = new TerrDouble();
                    ((TerrDouble) td.data[index]).data = (new double[(Integer) nameSize.get(name)]);

                }
                ((TerrDouble) td.data[index]).data[row] = (long) value;
                break;


            case "String":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrString();
                    ((TerrString) td.data[index]).data = (new String[(Integer) nameSize.get(name)]);
                }

                ((TerrString) td.data[index]).data[row] = (String) value;
                break;


            case "Double":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrDouble();
                    ((TerrDouble) td.data[index]).data = (new double[(Integer) nameSize.get(name)]);
                }

                ((TerrDouble) td.data[index]).data[row] = (Double) value;
                break;


            case "Frame":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrDataFrame();
                    BEDataFrame b = (BEDataFrame) value;
                    td.data[index] = b.getDataFrame();
                    //indexMapFrame.put(index,0);
                }
                break;


            case "List":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrList();
                    BEDataList b = (BEDataList) value;
                    td.data[index] = b.getDataList();
                    //indexMapFrame.put(index,0);
                }

                break;

            case "Boolean":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrLogical();
                    ((TerrLogical) td.data[index]).data = (new byte[(Integer) nameSize.get(name)]);

                }

                ((TerrLogical) td.data[index]).data[row] = (byte) ((Boolean) value ? 1 : 0);
                break;

            case "DateTime":
                index = (Integer) nameMap.get(name);

                if (td.data[index] == null) {
                    td.data[index] = new TerrDouble();
                    ((TerrDouble) td.data[index]).data = (new double[(Integer) nameSize.get(name)]);

                }

                ((TerrDouble) td.data[index]).data[row] = (double) (((Calendar)value).getTimeInMillis());
                break;

            default:
                LOGGER.log(Level.DEBUG, "Unsupported dataType found");
                break;

        }


    }

    public void printFrame(TerrJavaRemote engine) throws Exception {
        TERRLifetime.checkTERREngine();
        engine.setVariable(DATA_FRAME_VARIABLE, td);
        engine.evaluateInteractive("print(" + DATA_FRAME_VARIABLE + ")");
        //removing the object from TERR Memory
        engine.evaluateInteractive("rm("+DATA_FRAME_VARIABLE+")");
    }

    public final TerrDataFrame getDataFrame() {
        return this.td;
    }

    public final String[] getColumnNames() {

        return Arrays.copyOf(td.names, td.names.length);

    }

    public void removeRow(int row) {

        for (int i = 0; i < nameIndex; i++) {
            ((TerrDataFrame) td.data[i]).data[row] = null;
        }

    }

    public Object getCellValue(String name, int row) {
        int ind = (Integer) nameMap.get(name);
        String classCheck = td.data[ind].getClass().getName();
        Object obj;
        switch(classCheck){

            case RDouble:
                return ((TerrDouble) td.data[ind]).data[row];

            case RInt:
                return ((TerrInteger) td.data[ind]).data[row];

            case RBool:
                return ((TerrByte) td.data[ind]).data[row];

            case RString:
                return ((TerrString) td.data[ind]).data[row];

            case RList:
                return  ((TerrList) td.data[ind]).data[row];

            case RFrame:
                return  ((TerrDataFrame) td.data[ind]).data[row];

        }

        throw new RuntimeException("Unsupported Datatype");
    }
}
