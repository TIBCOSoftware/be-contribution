package com.tibco.cep.pattern.matcher.impl.master;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.SoftReference;

import com.tibco.cep.common.exception.RecoveryException;
import com.tibco.cep.common.resource.ResourceProvider;
import com.tibco.cep.pattern.matcher.exception.CopyException;
import com.tibco.cep.pattern.matcher.master.EndSource;
import com.tibco.cep.pattern.matcher.master.Plan;
import com.tibco.cep.pattern.matcher.master.Source;
import com.tibco.cep.pattern.matcher.model.Start;

/*
* Author: Ashwin Jayaprakash Date: Jul 7, 2009 Time: 2:27:53 PM
*/
public class DefaultPlan implements Plan {
    protected Source[] sources;

    protected InternalTimeSource timeSource;

    protected EndSource endSource;

    protected Start copyableFlow;

    protected transient volatile SoftReference<byte[]> serializedPlanBytes;

    public DefaultPlan() {
    }

    public Source[] getSources() {
        return sources;
    }

    public void setSources(Source... sources) {
        this.sources = sources;
    }

    public InternalTimeSource getTimeSource() {
        return timeSource;
    }

    public void setTimeSource(InternalTimeSource timeSource) {
        this.timeSource = timeSource;
    }

    public EndSource getEndSource() {
        return endSource;
    }

    public void setEndSource(EndSource endSource) {
        this.endSource = endSource;
    }

    public void setCopyableFlow(Start copyableFlow) {
        this.copyableFlow = copyableFlow;
    }

    public Start getCopyableFlow() {
        return copyableFlow;
    }

    private static byte[] serializeFlowToBytes(Start flowStart) throws IOException {
        byte[] bytes = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(flowStart);

            bytes = bos.toByteArray();
        }
        finally {
            try {
                bos.close();
            }
            catch (Exception e) {
                //Ignore.
            }
        }

        return bytes;
    }

    private byte[] getSerializedPlanBytes() throws IOException {
        SoftReference<byte[]> bytesRef = serializedPlanBytes;

        if (bytesRef != null) {
            byte[] bytes = bytesRef.get();

            if (bytes != null) {
                return bytes;
            }
        }

        //-------------

        byte[] bytes = serializeFlowToBytes(copyableFlow);

        //Just overwrite even if another Thread did this just before us. No locking.
        serializedPlanBytes = new SoftReference<byte[]>(bytes);

        return bytes;
    }

    public Start createNewFlow(ResourceProvider resourceProvider) throws CopyException {
        try {
            byte[] bytes = getSerializedPlanBytes();

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            Start newCopy = (Start) ois.readObject();

            ois.close();

            return newCopy.recover(resourceProvider);
        }
        catch (Exception e) {
            throw new CopyException(e);
        }
    }

    public DefaultPlan recover(ResourceProvider resourceProvider, Object... params)
            throws RecoveryException {
        for (int i = 0; i < sources.length; i++) {
            sources[i] = sources[i].recover(resourceProvider, params);
        }

        timeSource = timeSource.recover(resourceProvider, params);

        endSource = endSource.recover(resourceProvider, params);

        copyableFlow = copyableFlow.recover(resourceProvider, params);

        serializedPlanBytes = null;

        return this;
    }
}
