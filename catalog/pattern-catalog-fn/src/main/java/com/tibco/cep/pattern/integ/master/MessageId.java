package com.tibco.cep.pattern.integ.master;

import java.io.Serializable;

import com.tibco.cep.kernel.model.entity.Id;

/*
* Author: Ashwin Jayaprakash / Date: Dec 21, 2009 / Time: 11:56:56 AM
*/
public class MessageId implements Serializable {
	protected Id id;

    public MessageId(Id id) {
        this.id = id;
    }

    public Id getId() {
        return id;
    }

    public String getExtId() {
        return id.getExtId();
    }

    //-------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageId)) {
            return false;
        }

        MessageId messageId = (MessageId) o;
        if (!id.equals(messageId.id)) {
            return false;
        }
        if (id.getExtId() != null ? !id.getExtId().equals(messageId.id.getExtId()) : messageId.id.getExtId() != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (id.getExtId() != null ? id.getExtId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", extId='" + id.getExtId() + "}";
    }
}
